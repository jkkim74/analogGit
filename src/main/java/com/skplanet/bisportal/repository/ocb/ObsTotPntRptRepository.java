package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import com.skplanet.bisportal.model.ocb.ObsTotPntRpt;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;

/**
 * 전체포인트보고서 repository.
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 */
@Repository
public class ObsTotPntRptRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 전체포인트보고서 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsTotPntRpt> getTotalPointReport(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getTotalPointReport", jqGridRequest);
	}

}
