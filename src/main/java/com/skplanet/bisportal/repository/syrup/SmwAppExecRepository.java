package com.skplanet.bisportal.repository.syrup;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.OlapDimensionRequest;
import com.skplanet.bisportal.model.syrup.SmwAppExec;

/**
 * Created by cookatrice on 15. 1. 8..
 */

@Repository
public class SmwAppExecRepository {
	@Resource(name = "syrupSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * App 방문현황 일별조회
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */
	public List<SmwAppExec> getAppVisitSituationPerDay(OlapDimensionRequest olapDimensionRequest) {
		return sqlSession.selectList("getAppVisitSituationPerDay", olapDimensionRequest);
	}

	/**
	 * App 방문현황 주별조회
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */

	public List<SmwAppExec> getAppVisitSituationPerWeek(OlapDimensionRequest olapDimensionRequest) {
		return sqlSession.selectList("getAppVisitSituationPerWeek", olapDimensionRequest);
	}

	/**
	 * App 방문현황 월별조회
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */
	public List<SmwAppExec> getAppVisitSituationPerMonth(OlapDimensionRequest olapDimensionRequest) {
		return sqlSession.selectList("getAppVisitSituationPerMonth", olapDimensionRequest);
	}
}
