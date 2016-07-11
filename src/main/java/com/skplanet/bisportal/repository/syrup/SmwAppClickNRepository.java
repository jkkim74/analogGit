package com.skplanet.bisportal.repository.syrup;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.OlapDimensionRequest;
import com.skplanet.bisportal.model.syrup.SmwAppClickN;

/**
 * Created by cookatrice on 15. 1. 9..
 */

@Repository
public class SmwAppClickNRepository {
	@Resource(name = "syrupSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 메뉴별 방문현황 일별조회
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */
	public List<SmwAppClickN> getMenuVisitSituationPerDay(OlapDimensionRequest olapDimensionRequest) {
		return sqlSession.selectList("getMenuVisitSituationPerDay", olapDimensionRequest);
	}

	/**
	 * 메뉴별 방문현황 주별조회
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */

	public List<SmwAppClickN> getMenuVisitSituationPerWeek(OlapDimensionRequest olapDimensionRequest) {
		return sqlSession.selectList("getMenuVisitSituationPerWeek", olapDimensionRequest);
	}

	/**
	 * 메뉴별 방문현황 월별조회
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */
	public List<SmwAppClickN> getMenuVisitSituationPerMonth(OlapDimensionRequest olapDimensionRequest) {
		return sqlSession.selectList("getMenuVisitSituationPerMonth", olapDimensionRequest);
	}
}
