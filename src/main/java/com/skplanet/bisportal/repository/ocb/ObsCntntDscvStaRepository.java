package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsCntntDscvSta;

/**
 * Created by cookatrice
 */
@Repository
public class ObsCntntDscvStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * discover DAY - pivot
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsCntntDscvSta> getDiscoverForPivotPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getDiscoverForPivotPerDay", jqGridRequest);
	}

	/**
	 * discover WEEK - pivot
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsCntntDscvSta> getDiscoverForPivotPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getDiscoverForPivotPerWeek", jqGridRequest);
	}

	/**
	 * discover MONTH - pivot
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsCntntDscvSta> getDiscoverForPivotPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getDiscoverForPivotPerMonth", jqGridRequest);
	}
}
