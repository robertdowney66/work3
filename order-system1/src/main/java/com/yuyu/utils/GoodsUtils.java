package com.yuyu.utils;

import com.yuyu.dao.GoodsMapper;
import com.yuyu.pojo.Goods;
import com.yuyu.pojo.OrderGoods;
import com.yuyu.utils.myExcption.GoodsExistedException;
import com.yuyu.utils.myExcption.IllegalParamException;
import com.yuyu.utils.myExcption.NotFoundIdException;
import com.yuyu.utils.myExcption.NullParamException;
import org.apache.ibatis.session.SqlSession;

import java.util.*;

/**
 * @author yuyu
 * mapper中的调用依然繁琐，所以使用一个工具类将mapper中的操作移到此处
 */

public class GoodsUtils {

    /**
     * 增加货物
     * @param good
     */
    public static int saveGood(Goods good){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        // 判断货物是否存在
        Goods goodsById = getGoodsById(good.getId());
        if(goodsById!=null){
            sqlSession.close();
            throw new GoodsExistedException("该货物已存在");
        }
        // 判断货物价格，余量是否合法
        if(good.getPrice()<=0||good.getRemain()<=0){
            sqlSession.close();
            throw new IllegalParamException("输入数据非法");
        }
        int i = mapper.saveGood(good);
        sqlSession.commit();
        sqlSession.close();
        return i;
    }
    /**
     * 通过id删除对象
     * @param id
     */
    public static int removeGood(int id){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        // 判断货物是否存在
        Goods goodsById = getGoodsById(id);
        if(goodsById==null){
            sqlSession.close();
            throw new NotFoundIdException("未找到该货物");
        }
        // 判断货物是否在某个订单中
        List<OrderGoods> orderGoods = OrderGoodsUtils.listOrderGoodsByGid(id);
        if (!orderGoods.isEmpty()){
            for (OrderGoods orderGood : orderGoods) {
                // 将废弃货物放入drop_goods表中
                // 将num信息补全
                Goods numByOidAndGid = OrderGoodsUtils.getNumByOidAndGid(orderGood.getOid(), orderGood.getGid());
                DropGoodUtils.saveDropGood(new Goods(goodsById.getName(),numByOidAndGid.getNum()),orderGood.getOid());
            }
            // 删除关联表中相关数据
            for (OrderGoods orderGood : orderGoods) {
                OrderGoodsUtils.removeOrderGoods(orderGood.getId());
            }
        }
        int i = mapper.removeGood(id);
        sqlSession.commit();
        sqlSession.close();
        return i;
    }

    /**
     * 实现改变货物的操作
     * @param good
     * @return int
     */
    public static int updateGood(Goods good){

        SqlSession sqlSession = MybatisUtils.getSqlSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        // 判断该货物是否存在
        Goods goodsById = getGoodsById(good.getId());
        if(goodsById==null){
            sqlSession.close();
            throw new NotFoundIdException("未找到该货物");
        }
        // 判断更改后数据是否合法
        if(good.getPrice()<=0||good.getRemain()<0){
            sqlSession.close();
            throw new IllegalParamException("输入数据非法");
        }
        int i = mapper.updateGood(good);
        sqlSession.commit();
        sqlSession.close();
        return i;
    }
    /**
     * 将通过id查询商品的mapper操作转移在此实现，简化调用操作
     * @param args 传入不定个数到一个args数组中
     * @return list
     */
    public static List<Goods> listGoodsByIds(int ... args){
        //排除不输入参数情况
        if(args.length==0){
            throw new NullParamException("查询序号为空");
        }
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        ArrayList<Integer> list = new ArrayList<>();
        for (int arg : args) {
            list.add(arg);
        }
        // 这里初始化了HashMap是为了防止空间的浪费
        Map<String,List> map = new HashMap<>(6);
        map.put("ids",list);
        List<Goods> goods = mapper.listGoodsByIds(map);
        // 若输入不存在id给以回应
        for (int i = 0; i < args.length; i++) {
            boolean flog = true;
            for (Goods good : goods) {
                if (good.getId() == args[i]) {
                    flog = false;
                }
            }
            if (flog){
                System.out.println("第"+args[i]+"号货物不存在");
            }
        }
        sqlSession.close();
        return goods;
    }

    /**
     * 实现name查询简化，考虑了未传参数情况
     * @param args
     * @return list
     */
    public static List<Goods> listGoodsByNames(String ... args){
        //  排除不输入参数情况
        if(args.length==0){
            throw new NullParamException("查询名字为空");
        }
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        ArrayList<String> list = new ArrayList<>();
        for (String arg : args) {
            list.add(arg);
        }
        Map<String,List> map = new HashMap<>(6);
        map.put("names",list);
        List<Goods> goods = mapper.listGoodsByNames(map);
        for (int i = 0; i < args.length; i++) {
            boolean flog = true;
            for (Goods good : goods) {
                if (good.getName().equals(args[i])) {
                    flog = false;
                }
            }
            if (flog){
                System.out.println("名字为"+args[i]+"的货物不存在");
            }
        }
        sqlSession.close();
        return goods;
    }
    /**
     * 查询全部的对象
     * @return Goods
     */
    public static List<Goods> listGoods(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        List<Goods> goods = mapper.listGoods();
        sqlSession.close();
        return goods;
    }

    /**
     * 通过name查询对象
     * @param name
     * @return Goods
     */
    public static Goods getGoodByName(String name){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        Goods goodByName = mapper.getGoodByName(name);
        sqlSession.close();
        if(goodByName==null){
            System.out.println(name + "不存在表中");
        }
        return goodByName;
    }
    /**
     * 通过id查询对象
     * @param id
     * @return Goods
     */
    public static Goods getGoodsById(int id){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        Goods goodById = mapper.getGoodById(id);
        sqlSession.close();
        if(goodById==null){
            System.out.println("序号"+id+ "不存在表中");
        }
        return goodById;
    }

    /**
     * 通过价格实现查询商品的从小到大排序
     * @param args
     * @return
     */
    public static List<Goods> listGoodsSortByPrice(int ...args){
        List<Goods> goods = listGoodsByIds(args);
        goods.sort(new Comparator<Goods>() {
            @Override
            public int compare(Goods o1, Goods o2) {
                if(o1.getPrice()> o2.getPrice()){
                    return 1;
                } else if (o1.getPrice()== o2.getPrice()) {
                    // 如果价格相同，按照序号由小到大输出
                    if(o1.getId()> o2.getId()){
                        return 1;
                    }else{
                        return -1;
                    }
                }else {
                    return -1;
                }
            }
        });
        return goods;
    }

    /**
     * 实现分页查询
     * @param startPage
     * @param endPage
     * @return list
     */
    public static List<Goods> listGoodsByLimit(int startPage,int endPage){
        //检查首尾是否在范围内
        List<Goods> goods1 = listGoods();
        if(goods1.size()<=startPage){
            throw new IllegalParamException("开始页码超出货物范围");
        }
        if(goods1.size()<endPage-startPage){
            throw new IllegalParamException("末尾页码超出货物范围");
        }
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        GoodsMapper mapper = sqlSession.getMapper(GoodsMapper.class);
        List<Goods> goods = mapper.listGoodsByLimit(startPage, endPage);
        sqlSession.close();
        return goods;
    }
}


