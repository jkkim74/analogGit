package com.skplanet.bisportal.repository.tmap;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.tmap.TmapMonKpiStc;

/**
 * Created by cookatrice on 15. 6. 12..
 */
@Repository
public class TmapMonKpiStcRepository {
	@Resource(name = "tmapSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 월별 KPI 관리 조회
	 * @param jqGridRequest
	 * @return
	 */
	public List<TmapMonKpiStc> getMonthKpiManagement(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getMonthKpiManagement", jqGridRequest);
	}
}
