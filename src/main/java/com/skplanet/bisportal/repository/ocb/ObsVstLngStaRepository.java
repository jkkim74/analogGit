package com.skplanet.bisportal.repository.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsVstLngSta;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 방문 > 방문자(언어) repository
 * 
 * @author sjune
 */
@Repository
public class ObsVstLngStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 일별 방문자(언어) 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstLngSta> getVisitorsLangPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitorsLangPerDay", jqGridRequest);
	}

    /**
     * 주별 방문자(언어) 조회
     *
     * @param jqGridRequest
     * @return
     */
    public List<ObsVstLngSta> getVisitorsLangPerWeek(JqGridRequest jqGridRequest) {
        return sqlSession.selectList("getVisitorsLangPerWeek", jqGridRequest);
    }

    /**
     * 월별 방문자(언어) 조회
     *
     * @param jqGridRequest
     * @return
     */
    public List<ObsVstLngSta> getVisitorsLangPerMonth(JqGridRequest jqGridRequest) {
        return sqlSession.selectList("getVisitorsLangPerMonth", jqGridRequest);
    }
}
