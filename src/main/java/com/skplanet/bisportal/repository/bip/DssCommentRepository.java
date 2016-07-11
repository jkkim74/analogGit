package com.skplanet.bisportal.repository.bip;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.skplanet.bisportal.model.bip.DssComment;

/**
 * Created by seoseungho on 2014. 10. 2..
 */
@Repository
public class DssCommentRepository {
    @Resource(name = "bipSqlSession")
    private SqlSessionTemplate sqlSession;

    public int addDssComment(DssComment dssComment) {
        return sqlSession.insert("addDssComment", dssComment);
    }

    public List<DssComment> getDssComments(long dssId) {
        return sqlSession.selectList("getDssComments", dssId);
    }

    public int updateDssComment(DssComment dssComment) {
        return sqlSession.update("updateDssComment", dssComment);
    }

    public int deleteDssComment(long dssCommentId, String updateId) {
        Map<String, Object> parameterMap = Maps.newHashMap();
        parameterMap.put("dssCommentId", dssCommentId);
        parameterMap.put("updateId", updateId);
        return sqlSession.update("deleteDssComment", parameterMap);
    }
}
