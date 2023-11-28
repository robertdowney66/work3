package com.yuyu.dao;

import com.yuyu.pojo.DropGood;
import com.yuyu.pojo.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yuyu
 */
public interface DropGoodMapper {
    /**
     * 实现drop_goods的增加
     * @param dropGood
     * @return
     */
    int saveDropGood(DropGood dropGood);

    /**
     * 实现通过订单id查询废弃货物
     * @param id
     * @return
     */
    List<DropGood> listDropGoodByOid(@Param("oid") int id);
}
