package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import com.skplanet.bisportal.model.ocb.ObsUsePntRpt;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;

/**
 * 사용포인트보고서 repository.
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 */
@Repository
public class ObsUsePntRptRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 사용포인트보고서 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsUsePntRpt> getUsePointReport(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getUsePointReport", jqGridRequest);
	}

}
