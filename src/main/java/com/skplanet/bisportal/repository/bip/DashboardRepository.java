package com.skplanet.bisportal.repository.bip;

import java.util.List;

import javax.annotation.Resource;

import com.skplanet.bisportal.common.model.Condition;
import com.skplanet.bisportal.model.bip.BpmDlyPrst;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.WhereCondition;

/**
 * 
 * @author cookatrice
 */
@Repository
public class DashboardRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 날짜 -> 주차 변환
	 * @param curDate
	 * @return
	 */
	public List<String> makeDayToWeek(String curDate){
		return sqlSession.selectList("makeDayToWeek", curDate);
	}

	/**
	 * 대시보드 카테고리 리스트 로드
	 * @param whereCondition
	 * @return
	 */
	public List<WhereCondition> getDashboardCategory(WhereCondition whereCondition) {
		return sqlSession.selectList("getDashboardCategory", whereCondition);
	}

	/**
	 * 대시보드 sparkline chart 데이터셋 로드
	 * @param condition
	 * @return
	 */
	public List<BpmDlyPrst> getDashboardSparklineDataset(Condition condition) {
		return sqlSession.selectList("getDashboardSparklineDataset", condition);
	}

	/**
	 * 대시보드 sparkline chart 데이터셋 로드 (직전7일)
	 * @param whereCondition
	 * @return
	 */
	public List<BpmDlyPrst> getDashboardSparklinePast7DayDataset(WhereCondition whereCondition) {
		return sqlSession.selectList("getDashboardSparklinePast7DayDataset", whereCondition);
	}

}
