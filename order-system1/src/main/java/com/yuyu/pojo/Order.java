package com.yuyu.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author yuyu
 * 订单的实体类，用于对应数据库中的订单表
 */
public class Order {
    private int id;
    private Date time;
    private int orderPrice;

    private List<Goods> goods;

    /**
     * 实现date的格式化，使输出结果更加直观，美观
     * @param date
     * @return String
     */
    public String timeChange(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh-mm-ss");
        return simpleDateFormat.format(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;};
        if (o == null || getClass() != o.getClass()) {return false;};
        Order order = (Order) o;
        return id == order.id && orderPrice == order.orderPrice && Objects.equals(time, order.time) && Objects.equals(goods, order.goods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, orderPrice, goods);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id:" + id +
                ",time:" + timeChange(time) +
                ",orderPrice:" + orderPrice +
                ",goods:" + goods +
                '}';
    }
    public Order() {
    }

    public Order(int id, Date time, int orderPrice, List<Goods> goods) {
        this.id = id;
        this.time = time;
        this.orderPrice = orderPrice;
        this.goods = goods;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

}
