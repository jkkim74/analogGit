package com.skplanet.bisportal.repository.bip;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.skplanet.bisportal.model.bip.Dss;
import com.skplanet.bisportal.model.bip.DssRequest;

/**
 * Created by seoseungho on 2014. 9. 17..
 */
@Repository
public class DssRepository {
    @Resource(name = "bipSqlSession")
    private SqlSessionTemplate sqlSession;

    public Dss getDss(long dssId) {
        return sqlSession.selectOne("getDss", dssId);
    }
    public Dss getLatestDss() {
        return sqlSession.selectOne("getLatestDss");
    }
    public Dss getNextDss(long dssId) {
        return sqlSession.selectOne("getNextDss", dssId);
    }
    public Dss getPreviousDss(long dssId) {
        return sqlSession.selectOne("getPreviousDss", dssId);
    }
    public int addDss(Dss dss) {
        return sqlSession.insert("addDss", dss);
    }
    public int updateDss(Dss dss) {
        return sqlSession.update("updateDss", dss);
    }
    public int deleteDss(long dssId, String deleteId) {
        Map<String, Object> parameterMap = Maps.newHashMap();
        parameterMap.put("dssId", dssId);
        parameterMap.put("deleteId", deleteId);
        return sqlSession.update("deleteDss", parameterMap);
    }
    public List<Dss> getDssList(DssRequest dssRequest) {

        return sqlSession.selectList("getDssList", dssRequest);
    }
    public int increaseViewCount(long dssId) {
        return sqlSession.update("increaseViewCount", dssId);
    }

    public int hasNext(DssRequest dssRequest) {
        return sqlSession.selectOne("hasNext", dssRequest);
    }
}
