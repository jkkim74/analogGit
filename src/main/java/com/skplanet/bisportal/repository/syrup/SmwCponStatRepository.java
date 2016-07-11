package com.skplanet.bisportal.repository.syrup;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.OlapDimensionRequest;
import com.skplanet.bisportal.model.syrup.SmwCponStat;

/**
 * Created by cookatrice on 15. 1. 9..
 */

@Repository
public class SmwCponStatRepository {
	@Resource(name = "syrupSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 쿠폰실적 일별조회
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */
	public List<SmwCponStat> getCouponAchievePerDay(OlapDimensionRequest olapDimensionRequest) {
		return sqlSession.selectList("getCouponAchievePerDay", olapDimensionRequest);
	}

	/**
	 * 쿠폰실적 주별조회
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */

	public List<SmwCponStat> getCouponAchievePerWeek(OlapDimensionRequest olapDimensionRequest) {
		return sqlSession.selectList("getCouponAchievePerWeek", olapDimensionRequest);
	}

	/**
	 * 쿠폰실적 월별조회
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */
	public List<SmwCponStat> getCouponAchievePerMonth(OlapDimensionRequest olapDimensionRequest) {
		return sqlSession.selectList("getCouponAchievePerMonth", olapDimensionRequest);
	}
}
