package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsVstExcptPushSta;

/**
 * Created by cookatrice
 */
@Repository
public class ObsVstExceptPushStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 방문개요 DAY - grid(세로형)
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstExcptPushSta> getVisitsExceptPushOutlineForGridPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitsExceptPushOutlineForGridPerDay", jqGridRequest);
	}
	/**
	 * 방문개요 WEEK - grid(세로형)
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstExcptPushSta> getVisitsExceptPushOutlineForGridPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitsExceptPushOutlineForGridPerWeek", jqGridRequest);
	}
	/**
	 * 방문개요 MONTH - grid(세로형)
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstExcptPushSta> getVisitsExceptPushOutlineForGridPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitsExceptPushOutlineForGridPerMonth", jqGridRequest);
	}
}
