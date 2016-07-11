package com.skplanet.bisportal.repository.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsVstRsltnSta;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 방문자(해상도) repository.
 * 
 * @author sjune
 */
@Repository
public class ObsVstRsltnStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 일별 방문자(해상도) 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstRsltnSta> getVisitorsRsltnPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitorsRsltnPerDay", jqGridRequest);
	}

	/**
	 * 주별 방문자(해상도) 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstRsltnSta> getVisitorsRsltnPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitorsRsltnPerMonth", jqGridRequest);
	}

	/**
	 * 월별 방문자(해상도) 조회
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstRsltnSta> getVisitorsRsltnPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitorsRsltnPerWeek", jqGridRequest);
	}
}
