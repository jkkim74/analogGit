package com.skplanet.bisportal.repository.bip;

import com.skplanet.bisportal.common.model.Condition;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmDayStrdInfo;
import com.skplanet.bisportal.model.bip.BpmDlyPrst;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 
 * @author cookatrice
 */
@Repository
public class BpmDlyPrstRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * searchDate를 기준으로 7일전까지의 일자를 구한다.
	 *
	 * @param whereCondition
	 * @return BpmWkStrdInfo
	 */
	public List<BpmDayStrdInfo> getBpmDayStrdInfos(WhereCondition whereCondition) {
		return sqlSession.selectList("getBpmDayStrdInfos", whereCondition);
	}

	/**
	 * Ocb dashboard Data
     * OCB 거래액
     * OCB 유실적고객수
     * OCB 적립건수
     * OCB 사용건수
     * OCB 적립포인트
     * OCB 사용포인트
     * OCB 신규가입자수
     * OCB 총회원수
	 * 
	 * @param whereCondition
	 * @return
	 */
	public List<BpmDlyPrst> getDashboardData(WhereCondition whereCondition) {
		return sqlSession.selectList("getDashboardData", whereCondition);
	}

	/**
	 * 요약리포트 일자별 지표값 목록 조회
	 * 
	 * @param condition
	 * @return list of BpmDlyPrst
	 */
	public List<BpmDlyPrst> getSummaryDailyResult(Condition condition) {
		return sqlSession.selectList("getSummaryDailyResult", condition);
	}

	/**
	 * 경영실적 일별 조회
	 * 
	 * @param whereCondition
	 * @return list of BpmDlyPrst
	 */
	public List<BpmDlyPrst> getBpmDailyResultSums(WhereCondition whereCondition) {
		return sqlSession.selectList("getBpmDailyResultSums", whereCondition);
	}
}
