package com.skplanet.bisportal.repository.bip;

import com.skplanet.bisportal.common.model.Condition;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmWkStcPrst;
import com.skplanet.bisportal.model.bip.BpmWkStrdInfo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;


/**
 *
 * @author cookatrice
 */
@Repository
public class BpmWkStcPrstRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 요약리포트 주별 지표값 목록 조회
	 *
	 * @param condition
	 * @return list of BpmWkStcPrst
	 */
	public List<BpmWkStcPrst> getSummaryWeeklyResult(Condition condition) {
		return sqlSession.selectList("getSummaryWeeklyResult", condition);
	}

	/**
	 * 경영실적 주별 조회
	 *
	 * @param whereCondition
	 * @return list of BpmWkStcPrst
	 */
	public List<BpmWkStcPrst> getBpmWeeklyResultSums(WhereCondition whereCondition) {
		return sqlSession.selectList("getBpmWeeklyResultSums", whereCondition);
	}

	/**
	 * searchDate를 기준으로 주차를 구한다.
	 *
	 * @param whereCondition
	 * @return BpmWkStrdInfo
	 */
	public BpmWkStrdInfo getBpmWkStrdInfo(WhereCondition whereCondition) {
		return sqlSession.selectOne("getBpmWkStrdInfo", whereCondition);
	}

	/**
	 * searchDate를 기준으로 searchDiff만큼의 이전 주차를 구한다.
	 *
	 * @param whereCondition
	 * @return BpmWkStrdInfo
	 */
	public BpmWkStrdInfo getWeekAgoBpmWkStrdInfo(WhereCondition whereCondition) {
		return sqlSession.selectOne("getWeekAgoBpmWkStrdInfo", whereCondition);
	}

	/**
	 * searchDate를 기준으로 14주전까지의 주차를 구한다.
	 *
	 * @param whereCondition
	 * @return BpmWkStrdInfo
	 */
	public List<BpmWkStrdInfo> getWeeksAgoBpmWkStrdInfo(WhereCondition whereCondition) {
		return sqlSession.selectList("getWeeksAgoBpmWkStrdInfo", whereCondition);
	}

	/**
	 * searchDate를 기준으로 1년 전 주차를 구한다.
	 *
	 * @param whereCondition
	 * @return BpmWkStrdInfo
	 */
	public BpmWkStrdInfo getOneYearAgoBpmWkStrdInfo(WhereCondition whereCondition) {
		return sqlSession.selectOne("getOneYearAgoBpmWkStrdInfo", whereCondition);
	}
}
