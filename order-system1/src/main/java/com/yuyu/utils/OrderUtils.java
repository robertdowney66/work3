package com.yuyu.utils;

import com.yuyu.dao.GoodsMapper;
import com.yuyu.dao.OrderMapper;
import com.yuyu.pojo.DropGood;
import com.yuyu.pojo.Goods;
import com.yuyu.pojo.Order;
import com.yuyu.pojo.OrderGoods;
import com.yuyu.utils.myExcption.IllegalParamException;
import com.yuyu.utils.myExcption.NotFoundIdException;
import com.yuyu.utils.myExcption.NullParamException;
import com.yuyu.utils.myExcption.OrderExistedException;
import org.apache.ibatis.session.SqlSession;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yuyu
 * 实现对订单操作的简化
 */
public class OrderUtils {


    /**
     * 实现增加订单
     * @param order
     */
    public static int saveOrder(Order order){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        GoodsMapper goodsMapper = sqlSession.getMapper(GoodsMapper.class);
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
        // 先检查订单是否已经存在
        Order orderById = getOrderById(order.getId());
        if(orderById!=null){
            sqlSession.close();
            throw new OrderExistedException("该订单已存在");
        }
        // 检查Goods是否存在
        List<Goods> goods = order.getGoods();
        // 该参数用于标记货物的总价格
        int sumprice = 0;
        // 该集合用于存储下面查询的goodById，以便于实现下面对order_goods表的修改
        List<Goods> goods1 = new ArrayList<>();
        for (Goods good : goods) {
            int gid = good.getId();
            Goods goodById = goodsMapper.getGoodById(gid);
            if(goodById==null){
                sqlSession.close();
                throw new NotFoundIdException("未找到该商品");
            }
            // 将补全信息的货物修改为所需参数
            goodById.setNum(good.getNum());
            goodById.setRemain(goodById.getRemain()-good.getNum());
            goods1.add(goodById);
            // 判断余量是否充足
            if(goodById.getRemain() < good.getNum()){
                sqlSession.close();
                throw new IllegalParamException("商品余量不足");
            }
            sumprice+=goodById.getPrice()*good.getNum();
        }


        // 检查订单金额是否正确
        if(sumprice!=order.getOrderPrice()){
            sqlSession.close();
            throw new IllegalParamException("订单金额与目标金额不同");
        }

        int i = orderMapper.saveOrder(order);
        for (Goods goods2 : goods1) {
            // 修改关联表中数据
            OrderGoodsUtils.savaOrderGoods(new OrderGoods(goods2.getId(),order.getId()),goods2.getNum());
            // 成功插入，修改Goods表中数据
            GoodsUtils.updateGood(goods2);
        }

        sqlSession.commit();
        sqlSession.close();
        return i;
    }

    /**
     * 通过id实现删除订单
     * @param id
     * @return int
     */
    public static int removeOrderById(int id){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
        // 查看订单是否存在
        Order orderById = getOrderById(id);
        if(orderById==null){
            sqlSession.close();
            throw new NotFoundIdException("未找到该订单");
        }
        int i = mapper.removeOrder(id);
        // 将关联表中数据删除
        List<OrderGoods> orderGoods = OrderGoodsUtils.listOrderGoodsByOid(orderById.getId());
        for (OrderGoods orderGood : orderGoods) {
            OrderGoodsUtils.removeOrderGoods(orderGood.getId());
        }
        sqlSession.commit();
        sqlSession.close();
        return i;
    }

    /**
     * 更改
     * @param order
     * @return int
     */
    public static int updateOrder(Order order){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
        // 查看该id订单是否存在
        Order orderById = getOrderById(order.getId());
        if(orderById==null){
            sqlSession.close();
            throw new NotFoundIdException("该订单不存在");
        }
        // 记录总共的金额，判断修改是否合法
        int sumprice=0;
        // 创建一个存储拥有完整信息的货物的集合
        List<Goods> goodsAbs = new ArrayList<>();
        // 查看要改变货物是否存在
        List<Goods> goods = order.getGoods();
        for (Goods good : goods) {
            Goods goodsById = GoodsUtils.getGoodsById(good.getId());
            if(goodsById==null){
                sqlSession.close();
                throw new NotFoundIdException("未找到订单中货物");
            }
            // 将goods补全(因为可能goods使用的不是全参构造器，详见Goods类中)
            goodsById.setNum(good.getNum());
            goodsAbs.add(goodsById);
            sumprice+=goodsById.getPrice()*goodsById.getNum();
        }
        // 将完整的货物集合赋予order
        order.setGoods(goodsAbs);
        // 查看改变后金额是否正确
        if(sumprice!=(order.getOrderPrice())){
            sqlSession.close();
            throw new IllegalParamException("输入总金额有误");
        }
        int i = orderMapper.updateOrder(order);
        // 删除关联表中原来的数据，存入新的数据
        List<OrderGoods> orderGoods = OrderGoodsUtils.listOrderGoodsByOid(order.getId());
        for (OrderGoods orderGood : orderGoods) {
            OrderGoodsUtils.removeOrderGoods(orderGood.getId());
        }
        for (Goods goodsAb : goodsAbs) {
            OrderGoodsUtils.savaOrderGoods(new OrderGoods(goodsAb.getId(),order.getId()),goodsAb.getNum());
        }
        sqlSession.commit();
        sqlSession.close();
        return i;
    }

    /**
     * 通过id查询相应订单
     * @param id
     * @return Order
     */
    public static Order getOrderById(int id){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
        // 先检测是否该订单有对应的废弃货物
        Order orderByIdWithDropGood = mapper.getOrderByIdWithDropGood(id);
        if(orderByIdWithDropGood!=null){
            // 说明存在废弃货物，然后需要检验订单是否还有非废弃货物
            Order orderById = mapper.getOrderById(id);
            if(orderById!=null){
                // 说明含有非废弃货物,需要对其进行补齐操作
                List<Goods> existedGoods = orderById.getGoods();
                List<Goods> dropGoods = orderByIdWithDropGood.getGoods();
                for (Goods dropGood : dropGoods) {
                    existedGoods.add(dropGood);
                }
                orderById.setGoods(existedGoods);
                return orderById;
            }else{
                // 说明不含非废弃货物，不需要补齐
                return orderByIdWithDropGood;
            }
        }else{
            // 说明订单不存在废弃货物，进行订单存在性检验
            Order orderById = mapper.getOrderById(id);
            if(orderById==null){
                System.out.println("第"+id+"号订单不存在");
                sqlSession.close();
                return null;
            }else{
                sqlSession.close();
                return orderById;
            }
        }
    }
    /**
     * 实现了多个id查询多个对象
     * @param args
     * @return List
     */
    public static List<Order> listOrderByIds(int ...args){
        // 排除未传入参数情况
        if(args.length==0){
            throw new NullParamException("未传入ID");
        }
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (int arg : args) {
            ids.add(arg);
        }
        HashMap<String, List> map = new HashMap<>(6);
        map.put("ids",ids);
        List<Order> orders = mapper.listOrderByIds(map);
        // 在查询后判断是否订单含有废弃货物
        for (int arg : args) {
            Order orderById = mapper.getOrderById(arg);
            if (orderById==null){
                // 判断是否是含有废弃货物的订单
                List<DropGood> dropGoods = DropGoodUtils.listDropGoodByOid(arg);
                if(dropGoods.isEmpty()){
                    // 说明该订单就不存在
                    System.out.println("第"+ arg +"号订单不存在");
                }else{
                    // 说明只有废弃货物,首先使用getOrderByIdSimple(该方法在类的最后)查询订单除了货物的信息
                    Order orderByIdSimple = getOrderByIdSimple(arg);
                    List<Goods> goods = new ArrayList<>();
                    for (DropGood dropGood : dropGoods) {
                        goods.add(new Goods(dropGood.getName(),dropGood.getNum()));
                    }
                    orderByIdSimple.setGoods(goods);
                    orders.add(orderByIdSimple);
                }
            }else{
                // 判断是否订单含有废弃货物
                List<DropGood> dropGoods = DropGoodUtils.listDropGoodByOid(arg);
                if (!dropGoods.isEmpty()){
                    // 说明含有废弃货物
                    orders.remove(orderById);
                    List<Goods> goods = orderById.getGoods();
                    for (DropGood dropGood : dropGoods) {
                        goods.add(new Goods(dropGood.getName(),dropGood.getNum()));
                    }
                    orderById.setGoods(goods);
                    orders.add(orderById);
                }
            }
        }
        sqlSession.close();
        return orders;
    }

    /**
     * 查询全部的订单
     * @return List
     */
    public static List<Order> listOrder(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
        List<Order> orders = mapper.listOrder();
        // 先考虑补全工作
        for (Order order : orders) {
            List<DropGood> dropGoods = DropGoodUtils.listDropGoodByOid(order.getId());
            if(!dropGoods.isEmpty()){
                // 说明含有废弃货物
                orders.remove(order);
                List<Goods> goods = order.getGoods();
                for (DropGood dropGood : dropGoods) {
                    goods.add(new Goods(dropGood.getName(),dropGood.getNum()));
                }
                order.setGoods(goods);
                orders.add(order);
            }
        }
        // 同时将全部都是废弃货物的订单加入，下面这个listOrderSimple是一个查询除商品外其他订单信息的方法，具体见本类最后
        List<Order> orders1 = listOrderSimple();
        for (Order order : orders1) {
            // 设置一个标记量
            boolean flag = true;
            for (Order order1 : orders) {
                if(order.getId()==order1.getId()){
                    flag = false;
                    break;
                }
            }
            if(flag){
                // 说明只含有废弃货物订单未加入
                List<DropGood> dropGoods = DropGoodUtils.listDropGoodByOid(order.getId());
                ArrayList<Goods> goods = new ArrayList<>();
                for (DropGood dropGood : dropGoods) {
                    goods.add(new Goods(dropGood.getName(),dropGood.getNum()));
                }
                order.setGoods(goods);
                orders.add(order);
            }
        }
        return orders;
    }

    /**
     * 实现查询订单按照时间从早到晚排序
     * @param args
     * @return List
     */
    public static List<Order> listOrderSortByTime(int ...args){
        List<Order> orders = listOrderByIds(args);
        orders.sort(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh-mm-ss");
                String format = simpleDateFormat.format(o1.getTime());
                String format1 = simpleDateFormat.format(o2.getTime());
                return format.compareTo(format1);
            }
        });
        return orders;
    }
    /**
     * 实现查询订单按照订单序号从小到大排序
     * @return
     */
    public static List<Order> listOrderSortById(int ...args){
        List<Order> orders = listOrderByIds(args);
        orders.sort(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return o1.getId()- o2.getId();
            }
        });
        return orders;
    }

    /**
     * 实现订单分页查询
     * @param startPage
     * @param pages
     * @return
     */
    public static List<Order> listOrderByLimit(int startPage,int pages){
        List<Order> orders = listOrder();
        orders.sort(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return o1.getId()- o2.getId();
            }
        });
        if(startPage+1>orders.size()){
            throw new IllegalParamException("开始数字超出");
        } else if (startPage+pages>orders.size()) {
            throw new IllegalParamException("结尾数字超出");
        }
        List<Order> orders1 = new ArrayList<>();
        for(int i=startPage;i<startPage+pages;i++){
            orders1.add(orders.get(i));
        }
        return orders1;
    }

    // 接下来多个方法是针对出现废弃货物订单的操作

    /**
     * 进行订单除了商品外的别的信息的查询
     * @param id
     * @return Order
     */
    private static Order getOrderByIdSimple(int id){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
        Order orderByIdSimple = mapper.getOrderByIdSimple(id);
        sqlSession.close();
        return orderByIdSimple;
    }

    /**
     * 进行所有订单除了商品外信息的查询
     * @return List
     */
    private static List<Order> listOrderSimple(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
        List<Order> orders = mapper.listOrderSimple();
        sqlSession.close();
        return orders;
    }

}
