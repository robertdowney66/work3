package com.yuyu.dao;

import com.yuyu.pojo.Goods;
import com.yuyu.pojo.Order;
import com.yuyu.utils.OrderUtils;
import com.yuyu.utils.myExcption.IllegalParamException;
import com.yuyu.utils.myExcption.NotFoundIdException;
import com.yuyu.utils.myExcption.NullParamException;
import org.junit.Test;

import java.util.*;

/**
 * 为了阅读的直观性，将所有出现的异常情况都给与了捕获
 */
public class OrderTest {
    // 增加
    @Test
    public void test5(){
        Date date = new Date();
        // 余量不充足时
        ArrayList<Goods> goods2 = new ArrayList<>();
        goods2.add(new Goods(1,1000));
        goods2.add(new Goods(3,1));
        try {
            OrderUtils.saveOrder(new Order(4,date,53,goods2));
        } catch (IllegalParamException ex) {
            System.out.println(ex.getMessage());
        }
        // 订单金额不正确时
        ArrayList<Goods> goods3 = new ArrayList<>();
        goods3.add(new Goods(2,1));
        goods3.add(new Goods(4,1));
        try {
            OrderUtils.saveOrder(new Order(4,date,700,goods3));
        } catch (IllegalParamException ex) {
            System.out.println(ex.getMessage());
        }
        // 添加正确时
        ArrayList<Goods> goods = new ArrayList<>();
        goods.add(new Goods(2,1));
        goods.add(new Goods(4,1));
        int i = OrderUtils.saveOrder(new Order(4, date, 108, goods));
        if(i!=0){
            System.out.println("增加成功");
        }
    }
    // 删除
    @Test
    public void test6(){
        // 删除订单不存在时
        try {
            OrderUtils.removeOrderById(8);
        } catch (NotFoundIdException ex){
            System.out.println(ex.getMessage());
        }
        // 删除订单正确时
        int i = OrderUtils.removeOrderById(3);
        if(i!=0) {
            System.out.println("删除成功");
        }
    }
    // 更改
    @Test
    public void test7(){
        Date date = new Date();
        ArrayList<Goods> goods = new ArrayList<>();
        goods.add(new Goods(4,1));
        goods.add(new Goods(5,1));
        // 订单不存在时
        try {
            OrderUtils.updateOrder(new Order(8, date, 10000, goods));
        } catch (NotFoundIdException ex) {
            System.out.println(ex.getMessage());
        }
        // 更改金额存在错误时
        try {
            OrderUtils.updateOrder(new Order(1, date, 100, goods));
        } catch (IllegalParamException ex) {
            System.out.println(ex.getMessage());
        }
        // 正确更改时
        int i = OrderUtils.updateOrder(new Order(1, date, 81, goods));
        if(i==1){
            System.out.println("更改成功");
        }

    }

    // 查询
    // 查询单个
    @Test
    public void test8(){

        // 存在订单时
        Order orderById = OrderUtils.getOrderById(5);
        System.out.println(orderById);

        // 不存在订单时
        Order orderById1 = OrderUtils.getOrderById(10);
        System.out.println(orderById1);
    }

    // 查询多个
    @Test
    public void test11(){

        // 正常传入时
        List<Order> orders = OrderUtils.listOrderByIds(4,5);
        for (Order order : orders) {
            System.out.println(order);
        }
        // 不传入时
        try {
            OrderUtils.listOrderByIds();
        } catch (NullParamException ex) {
            System.out.println(ex.getMessage());
        }
        // 传入表中不存在订单时
        List<Order> orders1 = OrderUtils.listOrderByIds(1, 7, 8);
        for (Order order : orders1) {
            System.out.println(order);
        }
        List<Order> orders2 = OrderUtils.listOrder();
        for (Order order : orders2) {
            System.out.println(order);
        }
    }

    // 查询结果按照时间从早到晚，按照id从小到大排序，由于该操作底层使用的就是前面的查询多个，所以异常情况同前面一样，就不再做展示了
    @Test
    public void test12(){
        List<Order> orders = OrderUtils.listOrderSortByTime(1, 2);
        for (Order order : orders) {
            System.out.println(order);
        }
        List<Order> orders1 = OrderUtils.listOrderSortById(1, 2);
        for (Order order : orders1) {
            System.out.println(order);
        }
    }

    // 实现分页查询
    @Test
    public void test13(){

        // 正确范围内
        List<Order> orders = OrderUtils.listOrderByLimit(1, 2);
        for (Order order : orders) {
            System.out.println(order);
        }
        // 开始数据超出
        try {
            OrderUtils.listOrderByLimit(4, 3);
        } catch (IllegalParamException ex) {
            System.out.println(ex.getMessage());
        }
        // 结尾数据超出
        try {
            OrderUtils.listOrderByLimit(1, 5);
        } catch (IllegalParamException ex) {
            System.out.println(ex.getMessage());
        }

    }

}
