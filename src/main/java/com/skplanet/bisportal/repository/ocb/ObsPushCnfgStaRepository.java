package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import com.skplanet.bisportal.model.ocb.ObsPushCnfgSta;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;

/**
 * Created by cookatrice on 2014. 5. 14..
 */
@Repository
public class ObsPushCnfgStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 알림설정 DAY - grid(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsPushCnfgSta> getNotificationSetForPivotPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getNotificationSetForPivotPerDay", jqGridRequest);
	}

	/**
	 * 알림설정 WEEK - grid(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsPushCnfgSta> getNotificationSetForPivotPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getNotificationSetForPivotPerWeek", jqGridRequest);
	}

	/**
	 * 알림설정 MONTH - grid(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsPushCnfgSta> getNotificationSetForPivotPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getNotificationSetForPivotPerMonth", jqGridRequest);
	}

}
