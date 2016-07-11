package com.skplanet.bisportal.repository.syrup;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwMbrJoin;

/**
 * Created by lko on 2014-11-20.
 */
@Repository
public class SmwMbrJoinRepository {
    @Resource(name = "syrupSqlSession")
    private SqlSessionTemplate sqlSession;

    public List<SmwMbrJoin> getMbrJoinPerDay(JqGridRequest jqGridRequest) {
        return sqlSession.selectList("getMbrJoinPerDay", jqGridRequest);
    }

    public List<SmwMbrJoin> getMbrJoinPerWeek(JqGridRequest jqGridRequest) {
        return sqlSession.selectList("getMbrJoinPerWeek", jqGridRequest);
    }
    public List<SmwMbrJoin> getMbrJoinPerMonth(JqGridRequest jqGridRequest) {
        return sqlSession.selectList("getMbrJoinPerMonth", jqGridRequest);
    }
}
