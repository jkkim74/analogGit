package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsStoreOvrlSta;

/**
 * Created by cookatrice on 2014. 5. 13..
 */
@Repository
public class ObsStoreOvrlStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 매장전체 DAY - grid(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsStoreOvrlSta> getStoreTotalForPivotPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getStoreTotalForPivotPerDay", jqGridRequest);
	}

	/**
	 * 매장전체 WEEK - grid(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsStoreOvrlSta> getStoreTotalForPivotPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getStoreTotalForPivotPerWeek", jqGridRequest);
	}

	/**
	 * 매장전체 MONTH - grid(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsStoreOvrlSta> getStoreTotalForPivotPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getStoreTotalForPivotPerMonth", jqGridRequest);
	}

}
