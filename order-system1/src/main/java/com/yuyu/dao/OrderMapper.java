package com.yuyu.dao;

import com.yuyu.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author yuyu
 */
public interface OrderMapper {
    /**
     * 保存订单数据
     * @param order
     * @return int
     */
    int saveOrder(Order order);

    /**
     * 删除订单数据
     * @param id
     * @return int
     */
    int removeOrder(@Param("id") int id);

    /**
     * 更新订单数据
     * @param order
     * @return int
     */
    int updateOrder(Order order);

    /**
     * 通过id查询订单除了货物外的信息
     * @param id
     * @return Order
     */
    Order getOrderByIdSimple(@Param("id") int id);

    /**
     * 通过id查询订单全部信息
     * @param id
     * @return Order
     */
    Order getOrderById(@Param("id") int id);

    /**
     * 通过id查询订单有关废弃货物信息
     * @param id
     * @return Order
     */
    Order getOrderByIdWithDropGood(@Param("id") int id);

    /**
     * 查询全部订单除了商品外的全部信息
     * @return List
     */
    List<Order> listOrderSimple();

    /**
     * 通过多个id查询多个订单
     * @param map
     * @return List
     */
    List<Order> listOrderByIds(Map map);

    /**
     * 查询全部的订单信息
     * @return List
     */
    List<Order> listOrder();


}
