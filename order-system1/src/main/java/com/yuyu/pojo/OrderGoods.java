package com.yuyu.pojo;

/**
 * @author yuyu
 * 对应数据库中order_goods表
 */
public class OrderGoods {
    private int id;
    private int gid;
    private int oid;

    public OrderGoods() {
    }

    public OrderGoods(int id, int gid, int oid) {
        this.id = id;
        this.gid = gid;
        this.oid = oid;
    }

    /**
     * 由于id会自增，所以给一个不需要传入id的构造器
     * @param gid
     * @param oid
     */
    public OrderGoods(int gid, int oid) {
        this.gid = gid;
        this.oid = oid;
    }
    @Override
    public String toString() {
        return "OrderGoods{" +
                "id=" + id +
                ", gid=" + gid +
                ", oid=" + oid +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }


}
