package com.yuyu.pojo;

import java.util.Objects;

/**
 * @author yuyu
 * 商品的实体类，用于对应数据库中的商品表
 */
public class Goods {
    private int id;
    private String name;
    private int price;

    private int remain;
    /**
     * 加入一个num参数，这个值虽然存储在关联表中，但是为了便于查询，所以放入order类中
     */
    private int num;
    /**
     * 由于订单联表查询不需要返回id，因此如果只需判断id是否为0，就能知道是订单查询还是货物查询
     * @return
     */
    @Override
    public String toString() {
        if(id==0){
            return "{name:"+ name +",num:" + num +"}";
        }else{
            return "[id:"+ id +",name:"+ name + ",price:"+ price + ",remain:" + remain +"]";
        }
    }
    /**
     * 由于添加商品和添加订单所需参数不一样，下面该构造器传入货物姓名和购买数量
     * @param name
     */
    public Goods(String name,int num){
        this.name=name;
        this.num=num;
    }

    /**
     * 添加构造器理由同上,该构造器传入货物的id和购买数量
     * @param id
     */
    public Goods(int id,int num){
        this.id=id;
        this.num=num;
    }
    public Goods() {
    }

    /**
     * 由于全参构造只用于添加商品，所以不需要num，这个参数只在添加订单时需要
     * @param id
     * @param name
     * @param price
     * @param remain
     */
    public Goods(int id, String name, int price, int remain) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.remain = remain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        Goods goods = (Goods) o;
        return id == goods.id && price == goods.price && remain == goods.remain && num == goods.num && Objects.equals(name, goods.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, remain, num);
    }

    public Goods(int id, String name, int price, int remain, int num) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.remain = remain;
        this.num = num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}
