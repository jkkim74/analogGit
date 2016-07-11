package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsVstDvcMdlSta;

/**
 * The ObsVstDvcMdlStaRepository class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Repository
public class ObsVstDvcMdlStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 날짜기준 방문자(단말 모델) - 일자별
	 *
	 * @param jqGridRequest
	 * @return Collection of ObsVstDvcMdlSta
	 */
	public List<ObsVstDvcMdlSta> getVisitorsDvcMdlPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitorsDvcMdlPerDay", jqGridRequest);
	}

	/**
	 * 날짜기준 방문자(단말 모델) - 주별
	 *
	 * @param jqGridRequest
	 * @return Collection of ObsVstDvcMdlSta
	 */
	public List<ObsVstDvcMdlSta> getVisitorsDvcMdlPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitorsDvcMdlPerWeek", jqGridRequest);
	}

	/**
	 * 날짜기준 방문자(단말 모델) - 월별
	 *
	 * @param jqGridRequest
	 * @return Collection of ObsVstDvcMdlSta
	 */
	public List<ObsVstDvcMdlSta> getVisitorsDvcMdlPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitorsDvcMdlPerMonth", jqGridRequest);
	}

}
