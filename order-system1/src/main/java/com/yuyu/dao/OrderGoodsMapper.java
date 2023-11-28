package com.yuyu.dao;

import com.yuyu.pojo.Goods;
import com.yuyu.pojo.OrderGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author yuyu
 */
public interface OrderGoodsMapper {
    /**
     * 保存关联数据
     * @param map
     * @return int
     */
    int saveOrderGoods(Map map);

    /**
     * 删除关联数据
     * @param id
     * @return int
     */
    int removeOrderGoodsById(@Param("id") int id);

    /**
     * 通过gid和oid查询货物num
     * @param oid
     * @param gid
     * @return Goods
     */
    Goods getNumByOidAndGid(@Param("oid") int oid, @Param("gid") int gid);

    /**
     * 通过oid查询关联数据
     * @param oid
     * @return List
     */
    List<OrderGoods> listOrderGoodsByOid(@Param("oid") int oid);

    /**
     * 通过gid查询关联数据
     * @param gid
     * @return List
     */
    List<OrderGoods> listOrderGoodsByGid(@Param("gid") int gid);
}
