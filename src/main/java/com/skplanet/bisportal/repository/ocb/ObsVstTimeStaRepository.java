package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsVstTimeSta;

/**
 * Created by cookatrice on 2014. 5. 13..
 */
@Repository
public class ObsVstTimeStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 방문자(시간대) DAY - grid(세로형)
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstTimeSta> getVisitTimeZoneForGridPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitTimeZoneForGridPerDay", jqGridRequest);
	}
	/**
	 * 방문자(시간대) DAY - grid(세로형)
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstTimeSta> getVisitTimeZoneForGridPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitTimeZoneForGridPerWeek", jqGridRequest);
	}
	/**
	 * 방문자(시간대) DAY - grid(세로형)
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstTimeSta> getVisitTimeZoneForGridPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitTimeZoneForGridPerMonth", jqGridRequest);
	}

}
