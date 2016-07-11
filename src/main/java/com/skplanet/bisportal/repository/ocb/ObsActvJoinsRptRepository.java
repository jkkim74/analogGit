package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import com.skplanet.bisportal.model.ocb.ObsActvJoinsRpt;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;

/**
 * 활성화가맹점보고서 repository.
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 */
@Repository
public class ObsActvJoinsRptRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 활성화가맹점보고서 조회 (일별)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsActvJoinsRpt> getActivityJoinsReportPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getActivityJoinsReportPerDay", jqGridRequest);
	}

	/**
	 * 활성화가맹점보고서 조회 (주별)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsActvJoinsRpt> getActivityJoinsReportPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getActivityJoinsReportPerWeek", jqGridRequest);
	}

	/**
	 * 활성화가맹점보고서 조회 (월별)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsActvJoinsRpt> getActivityJoinsReportPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getActivityJoinsReportPerMonth", jqGridRequest);
	}

}
