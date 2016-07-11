package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsStoreDetlSta;

/**
 * Created by cookatrice on 2014. 5. 13..
 */
@Repository
public class ObsStoreDetlStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 매장전체 DAY - Pivot(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsStoreDetlSta> getStoreSingleForPivotPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getStoreSingleForPivotPerDay", jqGridRequest);
	}

	/**
	 * 매장전체 WEEK - Pivot(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsStoreDetlSta> getStoreSingleForPivotPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getStoreSingleForPivotPerWeek", jqGridRequest);
	}

	/**
	 * 매장전체 MONTH - Pivot(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsStoreDetlSta> getStoreSingleForPivotPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getStoreSingleForPivotPerMonth", jqGridRequest);
	}

	/**
	 * 매장전체 DAY - Grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsStoreDetlSta> getStoreSingleForGridPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getStoreSingleForGridPerDay", jqGridRequest);
	}
	public List<ObsStoreDetlSta> getStoreSingleForGridPaginationPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getStoreSingleForGridPaginationPerDay", jqGridRequest);
	}

	/**
	 * 매장전체 WEEK - Grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsStoreDetlSta> getStoreSingleForGridPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getStoreSingleForGridPerWeek", jqGridRequest);
	}
	public List<ObsStoreDetlSta> getStoreSingleForGridPaginationPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getStoreSingleForGridPaginationPerWeek", jqGridRequest);
	}

	/**
	 * 매장전체 MONTH - Grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsStoreDetlSta> getStoreSingleForGridPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getStoreSingleForGridPerMonth", jqGridRequest);
	}
	public List<ObsStoreDetlSta> getStoreSingleForGridPaginationPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getStoreSingleForGridPaginationPerMonth", jqGridRequest);
	}

}
