package com.skplanet.bisportal.service.bip;

import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmDlyPrst;
import com.skplanet.bisportal.model.bip.BpmMthStcPrst;
import com.skplanet.bisportal.model.bip.BpmWkStcPrst;

import java.util.List;

/**
 * The BpmResultSumService interface
 *
 * @author sjune
 */

public interface BpmResultSumService {
	/**
	 * 경영실적 일별 조회
	 * 
	 * @param whereCondition
	 * @return list of BpmDlyPrst
	 */
	List<BpmDlyPrst> getBpmDailyResultSums(WhereCondition whereCondition);

	/**
	 * 경영실적 주별 조회
	 *
	 * @param whereCondition
	 * @return list of BpmWkStcPrst
	 */
	List<BpmWkStcPrst> getBpmWeeklyResultSums(WhereCondition whereCondition);

	/**
	 * 경영실적 월별 조회
	 *
	 * @param whereCondition
	 * @return list of BpmMthStcPrst
	 */
	List<BpmMthStcPrst> getBpmMonthlyResultSums(WhereCondition whereCondition);
}
