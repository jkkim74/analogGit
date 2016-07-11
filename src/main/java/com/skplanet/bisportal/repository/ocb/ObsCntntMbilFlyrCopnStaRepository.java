package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsCntntMbilFlyrCopnSta;

/**
 * 모바일전단 쿠폰다운 repository.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 */
@Repository
public class ObsCntntMbilFlyrCopnStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 일별 모바일전단 쿠폰다운 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsCntntMbilFlyrCopnSta> getMobileFrontCouponPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getMobileFrontCouponPerDay", jqGridRequest);
	}

	/**
	 * 주별 모바일전단 쿠폰다운 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsCntntMbilFlyrCopnSta> getMobileFrontCouponPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getMobileFrontCouponPerMonth", jqGridRequest);
	}

	/**
	 * 월별 모바일전단 쿠폰다운 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsCntntMbilFlyrCopnSta> getMobileFrontCouponPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getMobileFrontCouponPerWeek", jqGridRequest);
	}
}
