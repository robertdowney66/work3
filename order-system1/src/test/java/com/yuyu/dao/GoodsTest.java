package com.yuyu.dao;

import com.yuyu.pojo.Goods;
import com.yuyu.utils.GoodsUtils;
import com.yuyu.utils.myExcption.GoodsExistedException;
import com.yuyu.utils.myExcption.IllegalParamException;
import com.yuyu.utils.myExcption.NotFoundIdException;
import org.junit.Test;

import java.util.List;

/**
 * 为了便于观阅，这里把会出现相应异常的情况都进行了捕获,同时对操作正确给予输出
 */
public class GoodsTest {
    // 增加
    @Test
    public void test() {
        // 货物不存在
        int i = GoodsUtils.saveGood(new Goods(9, "candy", 3, 101));
        if (i != 0) {
            System.out.println("加入成功");
        }
        // 货物已存在
        try {
            GoodsUtils.saveGood(new Goods(6, "apple", 3, 101));
        } catch (GoodsExistedException ex) {
            System.out.println(ex.getMessage());
        }
        // 货物参数非法
        try {
            GoodsUtils.saveGood(new Goods(7, "banana", -89, 0));
        } catch (IllegalParamException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // 删除
    @Test
    public void test1() {
        // 货物存在
        int i = GoodsUtils.removeGood(9);
        if (i != 0) {
            System.out.println("删除成功");
        }

        // 货物不存在
        try {
            GoodsUtils.removeGood(10);
        } catch (NotFoundIdException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // 更改
    @Test
    public void test3() {
        // 货物存在

        int i = GoodsUtils.updateGood(new Goods(1, "potato", 3, 100));
        if (i != 0) {
            System.out.println("修改成功");
        }

        // 货物不存在
        try {
            GoodsUtils.updateGood(new Goods(10, "potato", 3, 100));
        } catch (NotFoundIdException ex) {
            System.out.println(ex.getMessage());
        }
        // 货物参数非法
        try {
            GoodsUtils.updateGood(new Goods(1, "potato", 3, -100));
        } catch (IllegalParamException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // 查询多个
    @Test
    public void test2() {
        // names未传参情况
        try {
            List<Goods> goods = GoodsUtils.listGoodsByNames();
            for (Goods good : goods) {
                System.out.println(good);
            }
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
        // names传入非表内货物时
        try {
            List<Goods> goods = GoodsUtils.listGoodsByNames("chip", "beef");
            for (Goods good : goods) {
                System.out.println(good);
            }
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
        // names正常传入时
        try {
            List<Goods> goods = GoodsUtils.listGoodsByNames("cake", "pork");
            for (Goods good : goods) {
                System.out.println(good);
            }
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Test
    public void test4() {
        // ids空时
        try {
            List<Goods> goods = GoodsUtils.listGoodsByIds();
            for (Goods good : goods) {
                System.out.println(good);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // 含有表中没有货物的id
        try {
            List<Goods> goods = GoodsUtils.listGoodsByIds(1, 7, 8);
            for (Goods good : goods) {
                System.out.println(good);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // 表中货物id
        try {
            List<Goods> goods = GoodsUtils.listGoodsByIds(1, 2, 3);
            for (Goods good : goods) {
                System.out.println(good);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // 查询表中所有货物
        List<Goods> goods = GoodsUtils.listGoods();
        for (Goods good : goods) {
            System.out.println(good);
        }
    }

    // 查询单个时
    @Test
    public void test10() {
        // id:传入表中货物的序号
        try {
            Goods goodsById = GoodsUtils.getGoodsById(1);
            System.out.println(goodsById);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // id:传入非表中货物id
        try {
            Goods goodsById = GoodsUtils.getGoodsById(8);
            System.out.println(goodsById);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // name:传入已有货物时
        try {
            Goods beef = GoodsUtils.getGoodByName("beef");
            System.out.println(beef);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // name:传入没有货物时
        try {
            Goods beef = GoodsUtils.getGoodByName("chip");
            System.out.println(beef);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 查询多个货物，并按照价格排序，由于该操作底层使用的就是前面的查询多个，所以异常情况同前面一样，就不再做展示了
    @Test
    public void test12(){
        List<Goods> goods = GoodsUtils.listGoodsSortByPrice(1, 2, 3, 4, 5);
        for (Goods good : goods) {
            System.out.println(good);
        }
    }

    // 实现分页查询
    @Test
    public void test13(){
        // 正确时
        List<Goods> goods = GoodsUtils.listGoodsByLimit(0, 3);
        for (Goods good : goods) {
            System.out.println(good);
        }
        // 开始超出
        try {
            GoodsUtils.listGoodsByLimit(8,99);
        } catch (IllegalParamException ex) {
            System.out.println(ex.getMessage());
        }
        // 末尾超出
        try {
            GoodsUtils.listGoodsByLimit(0,99);
        } catch (IllegalParamException ex) {
            System.out.println(ex.getMessage());
        }
    }
}