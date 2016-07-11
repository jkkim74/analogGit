package com.skplanet.bisportal.repository;

import com.skplanet.bisportal.model.ocb.OcbDayVisitStc;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by cookatrice on 2014. 5. 2..
 */

@Repository
public class OcbDayVisitStcRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 날짜 기준 방문자 현황
	 * 
	 * @param params
	 *            검색 시작일자, 종료 일자.
	 * @return list of OcbDayVisitStc
	 */
	public List<OcbDayVisitStc> getVisitorListPerDay(Map<String, Object> params) {
		return sqlSession.selectList("getVisitorListPerDay", params);
	}

	public List<OcbDayVisitStc> getVisitorList(Map<String, Object> params) {
		return sqlSession.selectList("getVisitorList", params);
	}

}
