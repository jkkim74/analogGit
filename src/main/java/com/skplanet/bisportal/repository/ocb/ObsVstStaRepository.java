package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.ChartRequest;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsVstSta;

/**
 * Created by cookatrice on 2014. 5. 9..
 */
@Repository
public class ObsVstStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 방문개요 DAY - grid(세로형)
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstSta> getVisitorListForGridPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitorListForGridPerDay", jqGridRequest);
	}
	/**
	 * 방문개요 WEEK - grid(세로형)
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstSta> getVisitorListForGridPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitorListForGridPerWeek", jqGridRequest);
	}
	/**
	 * 방문개요 MONTH - grid(세로형)
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstSta> getVisitorListForGridPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitorListForGridPerMonth", jqGridRequest);
	}

    /**
     * 방문개요 DAY - grid(가로형)
     * @param jqGridRequest
     * @return
     */
	public List<ObsVstSta> getVisitorListForGridPerDay2(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitorListForGridPerDay2", jqGridRequest);
	}
    /**
     * 방문개요 WEEK - grid(가로형)
     * @param jqGridRequest
     * @return
     */
	public List<ObsVstSta> getVisitorListForGridPerWeek2(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitorListForGridPerWeek2", jqGridRequest);
	}
    /**
     * 방문개요 MONTH - grid(가로형)
     * @param jqGridRequest
     * @return
     */
	public List<ObsVstSta> getVisitorListForGridPerMonth2(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitorListForGridPerMonth2", jqGridRequest);
	}

	/**
	 * 방문개요 DAY - chart
	 * @param chartRequest dateType, startDate, endDate, pocCode
	 * @return
	 */
	public List<ObsVstSta> getVisitorListForChartPerDay(ChartRequest chartRequest) {
		return sqlSession.selectList("getVisitorListForChartPerDay", chartRequest);
	}
	/**
	 * 방문개요 WEEK - chart
	 * @param chartRequest dateType, startDate, endDate, pocCode
	 * @return
	 */
	public List<ObsVstSta> getVisitorListForChartPerWeek(ChartRequest chartRequest) {
		return sqlSession.selectList("getVisitorListForChartPerWeek", chartRequest);
	}
	/**
	 * 방문개요 MONTH - chart
	 * @param chartRequest dateType, startDate, endDate, pocCode
	 * @return
	 */
	public List<ObsVstSta> getVisitorListForChartPerMonth(ChartRequest chartRequest) {
		return sqlSession.selectList("getVisitorListForChartPerMonth", chartRequest);
	}
}
