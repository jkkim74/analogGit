package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import com.skplanet.bisportal.model.ocb.ObsVstOsSta;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;

/**
 * Created by cookatrice on 2014. 5. 14..
 */
@Repository
public class ObsVstOsRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 방문자(OS) DAY - grid(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstOsSta> getVisitOsForGridPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitOsForGridPerDay", jqGridRequest);
	}

	/**
	 * 방문자(OS) WEEK - grid(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstOsSta> getVisitOsForGridPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitOsForGridPerWeek", jqGridRequest);
	}

	/**
	 * 방문자(OS) MONTH - grid(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsVstOsSta> getVisitOsForGridPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getVisitOsForGridPerMonth", jqGridRequest);
	}

}
