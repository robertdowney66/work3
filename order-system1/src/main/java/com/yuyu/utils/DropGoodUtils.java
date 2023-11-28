package com.yuyu.utils;

import com.yuyu.dao.DropGoodMapper;
import com.yuyu.pojo.DropGood;
import com.yuyu.pojo.Goods;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @author yuyu
 * 用于实现废弃货物的增加和查询
 */
public class DropGoodUtils {
    /**
     * 实现了保存废物货物
     * @param goods
     * @param oid
     * @return int
     */
    public static int saveDropGood(Goods goods,int oid){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        DropGoodMapper mapper = sqlSession.getMapper(DropGoodMapper.class);
        int i = mapper.saveDropGood(new DropGood(goods.getName(), goods.getNum(), oid));
        sqlSession.commit();
        sqlSession.close();
        return i;
    }

    /**
     * 实现了通过oid查询多个废弃货物
     * @param oid
     * @return List
     */
    public static List<DropGood> listDropGoodByOid(int oid){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        DropGoodMapper mapper = sqlSession.getMapper(DropGoodMapper.class);
        List<DropGood> dropGoods = mapper.listDropGoodByOid(oid);
        sqlSession.close();
        return dropGoods;
    }
}
