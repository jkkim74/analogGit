package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import com.skplanet.bisportal.model.ocb.ObsMbilAchvRpt;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;

/**
 * 모바일실적보고서 repository.
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 */
@Repository
public class ObsMbilAchvRptRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 모바일실적보고서
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsMbilAchvRpt> getMobileAchieveReport(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getMobileAchieveReport", jqGridRequest);
	}

}
