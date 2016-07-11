package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import com.skplanet.bisportal.model.ocb.ObsCntntMbilFlyrPageSta;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;

/**
 * 모바일전단 페이지조회 repository.
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 */
@Repository
public class ObsCntntMbilFlyrPageStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 일별 모바일전단 페이지조회 (일별)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsCntntMbilFlyrPageSta> getMobileFlyerPageInquiryPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getMobileFlyerPageInquiryPerDay", jqGridRequest);
	}

	/**
	 * 일별 모바일전단 페이지조회 (주별)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsCntntMbilFlyrPageSta> getMobileFlyerPageInquiryPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getMobileFlyerPageInquiryPerWeek", jqGridRequest);
	}

	/**
	 * 일별 모바일전단 페이지조회 (월별)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsCntntMbilFlyrPageSta> getMobileFlyerPageInquiryPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getMobileFlyerPageInquiryPerMonth", jqGridRequest);
	}

}
