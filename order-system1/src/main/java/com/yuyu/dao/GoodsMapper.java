package com.yuyu.dao;

import com.yuyu.pojo.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author yuyu
 */
public interface GoodsMapper {
    /**
     * 增加货物
     * @param good
     * @return int
     */
    int saveGood(Goods good);

    /**
     * 去除货物
     * @param id
     * @return int
     */
    int removeGood(@Param("id") int id);

    /**
     * 更新货物
     * @param good
     * @return int
     */
    int updateGood(Goods good);

    /**
     * 通过id查询单个货物
     * @param id
     * @return Goods
     */
    Goods getGoodById(@Param("id") int id);

    /**
     * 通过name查询单个货物
     * @param name
     * @return Goods
     */
    Goods getGoodByName(@Param("name") String name);


    /**
     * 通过map实现动态sql进行id查询多个对象
     * @param map
     * @return List
     */
    List<Goods> listGoodsByIds(Map map);

    /**
     * 通过map实现动态sql进行name查询多个对象
     * @param map
     * @return List
     */
    List<Goods> listGoodsByNames(Map map);

    /**
     * 查询所有对象
     * @return List
     */
    List<Goods> listGoods();

    /**
     * 实现了分页查询
     * @param startpage
     * @param endpage
     * @return List
     */
    List<Goods> listGoodsByLimit(@Param("StartPage")int startpage,@Param("EndPage")int endpage);

}

