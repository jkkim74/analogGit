package com.skplanet.bisportal.repository.bip;

import java.util.List;

import javax.annotation.Resource;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.DaaEwmaStatDaily;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

/**
 * DaaEwmaStatDailyRepository class.
 * 
 * @author cookatrice
 */

@Repository
public class DaaEwmaStatDailyRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	public List<DaaEwmaStatDaily> getEwmaChartData(WhereCondition whereCondition) {
		return sqlSession.selectList("getEwmaChartData", whereCondition);
	}

	public List<DaaEwmaStatDaily> getOcbEwmaChartData(WhereCondition whereCondition) {
		return sqlSession.selectList("getOcbEwmaChartData", whereCondition);
	}

	// 경영실적 메일링 증감치 체크 기능에 필요함.
	public List<DaaEwmaStatDaily> getBossEwmaData(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getBossEwmaData", jqGridRequest);
	}

	// 경영실적 메일링 증감치 체크 기능에 필요함(KID).
	public List<DaaEwmaStatDaily> getBossEwmaDataForKid(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getBossEwmaDataForKid", jqGridRequest);
	}
}
