package com.yuyu.pojo;

/**
 * @author yuyu
 * 对应表中drop_goods
 */
public class DropGood {
    int id;
    String name;
    int num;
    int oid;
    public DropGood() {
    }

    @Override
    public String toString() {
        return "DropGood{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", num=" + num +
                ", oid=" + oid +
                '}';
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getOid() {
        return oid;
    }


    public void setOid(int oid) {
        this.oid = oid;
    }

    /**
     * 由于dropgood id会自增，所以不需要传入id
     * @param name
     * @param num
     * @param oid
     */
    public DropGood(String name, int num, int oid){
        this.name = name;
        this.num = num;
        this.oid = oid;
    }
    public DropGood(int id, String name, int num, int oid) {
        this.id = id;
        this.name = name;
        this.num = num;
        this.oid = oid;
    }

}
