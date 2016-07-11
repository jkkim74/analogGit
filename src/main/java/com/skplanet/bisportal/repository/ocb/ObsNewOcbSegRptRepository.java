package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import com.skplanet.bisportal.model.ocb.ObsNewOcbSegRpt;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;

/**
 * New OCB Seg repository.
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 */
@Repository
public class ObsNewOcbSegRptRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * New OCB Seg 조회 (일별)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsNewOcbSegRpt> getNewOcbSegPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getNewOcbSegPerDay", jqGridRequest);
	}

	/**
	 * New OCB Seg 조회 (주별)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsNewOcbSegRpt> getNewOcbSegPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getNewOcbSegPerWeek", jqGridRequest);
	}

	/**
	 * New OCB Seg 조회 (월별)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsNewOcbSegRpt> getNewOcbSegPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getNewOcbSegPerMonth", jqGridRequest);
	}

}
