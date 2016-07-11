package com.skplanet.bisportal.repository.syrup;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.CustomerSituation;

/**
 * Created by seoseungho on 2014. 11. 21..
 */
@Repository
public class SyrupDashboardRepository {
	@Resource(name = "syrupSqlSession")
	private SqlSessionTemplate sqlSession;

	public List<CustomerSituation> getCustomerSituationPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getCustomerSituationPerDay", jqGridRequest);
	}

	public List<CustomerSituation> getCustomerSituationPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getCustomerSituationPerWeek", jqGridRequest);
	}

	public List<CustomerSituation> getCustomerSituationPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getCustomerSituationPerMonth", jqGridRequest);
	}
}
