package com.yuyu.utils;

import com.yuyu.dao.OrderGoodsMapper;
import com.yuyu.pojo.Goods;
import com.yuyu.pojo.OrderGoods;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuyu
 * 实现对关联表操作的简化
 */
public class OrderGoodsUtils {
    /**
     * 实现关联数据的保存
     * @param orderGoods
     * @param num
     * @return int
     */
    public static int savaOrderGoods(OrderGoods orderGoods,int num){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        OrderGoodsMapper mapper = sqlSession.getMapper(OrderGoodsMapper.class);

        Map<String,Integer> map = new HashMap<>(6);
        map.put("oid",orderGoods.getOid());
        map.put("gid",orderGoods.getGid());
        map.put("num",num);
        int i = mapper.saveOrderGoods(map);
        sqlSession.commit();
        sqlSession.close();
        return i;
    }

    /**
     * 实现关联数据的去除
     * @param id
     * @return int
     */
    public static int removeOrderGoods(int id){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        OrderGoodsMapper mapper = sqlSession.getMapper(OrderGoodsMapper.class);
        int i = mapper.removeOrderGoodsById(id);
        sqlSession.commit();
        sqlSession.close();
        return i;
    }

    /**
     * 实现通过oid和gid获得num
     * @param oid
     * @param gid
     * @return Goods
     */
    public static Goods getNumByOidAndGid(int oid,int gid){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        OrderGoodsMapper mapper = sqlSession.getMapper(OrderGoodsMapper.class);
        Goods numByOidAndGid = mapper.getNumByOidAndGid(oid, gid);
        sqlSession.close();
        return numByOidAndGid;
    }

    /**
     * 实现通过oid查询关联数据
     * @param oid
     * @return List
     */
    public static List<OrderGoods> listOrderGoodsByOid(int oid){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        OrderGoodsMapper mapper = sqlSession.getMapper(OrderGoodsMapper.class);
        List<OrderGoods> orderGoodsByOid = mapper.listOrderGoodsByOid(oid);
        sqlSession.close();
        return orderGoodsByOid;
    }

    /**
     * 实现通过gid查询关联数据
     * @param gid
     * @return List
     */
    public static List<OrderGoods> listOrderGoodsByGid(int gid){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        OrderGoodsMapper mapper = sqlSession.getMapper(OrderGoodsMapper.class);
        List<OrderGoods> orderGoodsByOid = mapper.listOrderGoodsByGid(gid);
        sqlSession.close();
        return orderGoodsByOid;
    }
}
