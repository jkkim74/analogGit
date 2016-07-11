package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import com.skplanet.bisportal.model.ocb.ObsRsrvPntRpt;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;

/**
 * 적립포인트보고서 repository.
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 */
@Repository
public class ObsRsrvPntRptRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 적립포인트보고서 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsRsrvPntRpt> getReservingPointReport(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getReservingPointReport", jqGridRequest);
	}

}
