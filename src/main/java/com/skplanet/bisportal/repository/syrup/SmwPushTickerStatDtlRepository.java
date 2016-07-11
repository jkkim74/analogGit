package com.skplanet.bisportal.repository.syrup;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwPushTickerStatDtl;

/**
 * Created by lko on 2014-12-05.
 */
@Repository
public class SmwPushTickerStatDtlRepository {
    @Resource(name = "syrupSqlSession")
    private SqlSessionTemplate sqlSession;

    public List<SmwPushTickerStatDtl> getSmwPushTickerStatDtlPerDay(JqGridRequest jqGridRequest) {
        return sqlSession.selectList("getSmwPushTickerStatDtlPerDay", jqGridRequest);
    }

    public List<SmwPushTickerStatDtl> getSmwPushTickerStatDtlWeek(JqGridRequest jqGridRequest) {
        return sqlSession.selectList("getSmwPushTickerStatDtlWeek", jqGridRequest);
    }

    public List<SmwPushTickerStatDtl> getSmwPushTickerStatDtlPerMonth(JqGridRequest jqGridRequest) {
        return sqlSession.selectList("getSmwPushTickerStatDtlPerMonth", jqGridRequest);
    }
}
