package com.skplanet.bisportal.repository.bip;

import com.skplanet.bisportal.common.model.Condition;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmMthStcPrst;
import com.skplanet.bisportal.model.bip.BpmMthStrdInfo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;


/**
 *
 * @author cookatrice
 */
@Repository
public class BpmMthStcPrstRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 요약리포트 월별 지표값 목록 조회
	 *
	 * @param condition
	 * @return list of BpmMthStcPrst
	 */
	public List<BpmMthStcPrst> getSummaryMonthlyResult(Condition condition) {
		return sqlSession.selectList("getSummaryMonthlyResult", condition);
	}

	/**
	 * 경영실적 월별 조회
	 *
	 * @param whereCondition
	 * @return list of BpmDlyPrst
	 */
	public List<BpmMthStcPrst> getBpmMonthlyResultSums(WhereCondition whereCondition) {
		return sqlSession.selectList("getBpmMonthlyResultSums", whereCondition);
	}

	/**
	 * startDate, endDate를 기준으로 12개월전까지의 월을 구한다.
	 *
	 * @param whereCondition
	 * @return BpmWkStrdInfo
	 */
	public List<BpmMthStrdInfo> getBpmMthStrdInfos(WhereCondition whereCondition) {
		return sqlSession.selectList("getBpmMthStrdInfos", whereCondition);
	}
}
