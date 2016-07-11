package com.skplanet.bisportal.repository.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsVstPageSta;
import com.skplanet.bisportal.model.ocb.SankeyAccessLog;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 방문자 페이지 repository.
 * 
 * @author sjune
 */
@Repository
public class ObsVstPageStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 일별 방문자페이지 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstPageSta> getVisitorsPagePerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitorsPagePerDay", jqGridRequest);
	}

	/**
	 * 주별 방문자페이지 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstPageSta> getVisitorsPagePerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitorsPagePerMonth", jqGridRequest);
	}

	/**
	 * 월별 방문자페이지 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstPageSta> getVisitorsPagePerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitorsPagePerWeek", jqGridRequest);
	}

	/**
	 * 일별 트랙킹 정보 조회
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<SankeyAccessLog> getVisitorTrackingPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitorTrackingPerDay", jqGridRequest);
	}
}
