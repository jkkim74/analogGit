package com.skplanet.bisportal.repository.syrup;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwCldrWk;

/**
 * Created by lko on 2014-11-21.
 */
@Repository
public class SvcCdRepository {
    @Resource(name = "syrupSqlSession")
    private SqlSessionTemplate sqlSession;

    /**
     * 주차 코드를 조회한다.
     *
     * @param smwCldrWk
     * @return SvcCd
     */
    public List<SmwCldrWk> getWkStrd(SmwCldrWk smwCldrWk) {
        return sqlSession.selectList("getWkStrd", smwCldrWk);
    }

    /**
     * 주차 코드를 조회한다.
     *
     * @param smwCldrWk
     * @return SvcCd
     */
    public List<SmwCldrWk> getWkStrds(SmwCldrWk smwCldrWk) {
        return sqlSession.selectList("getWkStrds", smwCldrWk);
    }

    /**
     * 주차 코드를 조회한다.
     *
     * @param jqGRidRequest
     * @return SvcCd
     */
    public List<SmwCldrWk> getWkStrdFrto(JqGridRequest jqGRidRequest) {
        return sqlSession.selectList("getWkStrdFrto", jqGRidRequest);
    }
}
