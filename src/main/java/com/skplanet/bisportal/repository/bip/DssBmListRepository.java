package com.skplanet.bisportal.repository.bip;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.skplanet.bisportal.model.bip.DssBmList;

/**
 * Created by seoseungho on 2014. 9. 25..
 */
@Repository
public class DssBmListRepository {
    @Resource(name = "bipSqlSession")
    private SqlSessionTemplate sqlSession;

    public int addDssBmList(DssBmList dssBmList) {
        return sqlSession.insert("addDssBmList", dssBmList);
    }

    public List<DssBmList> getDssBmList(long dssId) {
        return sqlSession.selectList("getDssBmList", dssId);
    }

    public int deleteDssBmListByDssId(long dssId, String updateId) {
        Map<String, Object> requestMap = Maps.newHashMap();
        requestMap.put("dssId", dssId);
        requestMap.put("updateId", updateId);
        return sqlSession.update("deleteDssBmListByDssId", requestMap);
    }
}
