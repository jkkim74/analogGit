package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import com.skplanet.bisportal.model.ocb.ObsActvCustRpt;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;

/**
 * 활성화고객보고서 repository.
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 */
@Repository
public class ObsActvCustRptRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 활성화고객보고서 조회 (일별)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsActvCustRpt> getActivityCustomerReportPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getActivityCustomerReportPerDay", jqGridRequest);
	}

	/**
	 * 활성화고객보고서 조회 (주별)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsActvCustRpt> getActivityCustomerReportPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getActivityCustomerReportPerWeek", jqGridRequest);
	}

	/**
	 * 활성화고객보고서 조회 (월별)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsActvCustRpt> getActivityCustomerReportPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getActivityCustomerReportPerMonth", jqGridRequest);
	}

}
