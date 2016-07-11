package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsSrchClickStoreSta;

/**
 * Created by cookatrice on 2014. 5. 19..
 */
@Repository
public class ObsSrchClickStoreStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 검색결과클릭_매장 DAY - Pivot(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsSrchClickStoreSta> getSearchResultClickStoreForPivotPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSearchResultClickStoreForPivotPerDay", jqGridRequest);
	}

	/**
	 * 검색결과클릭_매장 WEEK - Pivot(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsSrchClickStoreSta> getSearchResultClickStoreForPivotPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSearchResultClickStoreForPivotPerWeek", jqGridRequest);
	}

	/**
	 * 검색결과클릭_매장 MONTH - Pivot(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsSrchClickStoreSta> getSearchResultClickStoreForPivotPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSearchResultClickStoreForPivotPerMonth", jqGridRequest);
	}

	/**
	 * 검색결과클릭_매장 DAY - Grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsSrchClickStoreSta> getSearchResultClickStoreForGridPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSearchResultClickStoreForGridPerDay", jqGridRequest);
	}

	/**
	 * 검색결과클릭_매장 WEEK - Grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsSrchClickStoreSta> getSearchResultClickStoreForGridPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSearchResultClickStoreForGridPerWeek", jqGridRequest);
	}

	/**
	 * 검색결과클릭_매장 MONTH - Grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsSrchClickStoreSta> getSearchResultClickStoreForGridPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSearchResultClickStoreForGridPerMonth", jqGridRequest);
	}

}
