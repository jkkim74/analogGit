package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import com.skplanet.bisportal.model.ocb.ObsPresntSumRpt;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;

/**
 * 포인트 선물/합산하기 repository.
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 */
@Repository
public class ObsPresntSumRptRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 포인트 선물/합산하기 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsPresntSumRpt> getPointPresentSum(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getPointPresentSum", jqGridRequest);
	}

}
