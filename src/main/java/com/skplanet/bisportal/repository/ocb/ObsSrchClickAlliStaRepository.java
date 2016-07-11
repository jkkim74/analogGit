package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsSrchClickAlliSta;

/**
 * Created by cookatrice on 2014. 5. 19..
 */
@Repository
public class ObsSrchClickAlliStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 검색결과클릭_제휴사 DAY - Pivot(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsSrchClickAlliSta> getSearchResultClickAlliForPivotPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSearchResultClickAlliForPivotPerDay", jqGridRequest);
	}

	/**
	 * 검색결과클릭_제휴사 WEEK - Pivot(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsSrchClickAlliSta> getSearchResultClickAlliForPivotPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSearchResultClickAlliForPivotPerWeek", jqGridRequest);
	}

	/**
	 * 검색결과클릭_제휴사 MONTH - Pivot(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsSrchClickAlliSta> getSearchResultClickAlliForPivotPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSearchResultClickAlliForPivotPerMonth", jqGridRequest);
	}

	/**
	 * 검색결과클릭_제휴사 DAY - Grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsSrchClickAlliSta> getSearchResultClickAlliForGridPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSearchResultClickAlliForGridPerDay", jqGridRequest);
	}

	/**
	 * 검색결과클릭_제휴사 WEEK - Grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsSrchClickAlliSta> getSearchResultClickAlliForGridPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSearchResultClickAlliForGridPerWeek", jqGridRequest);
	}

	/**
	 * 검색결과클릭_제휴사 MONTH - Grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsSrchClickAlliSta> getSearchResultClickAlliForGridPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSearchResultClickAlliForGridPerMonth", jqGridRequest);
	}

}
