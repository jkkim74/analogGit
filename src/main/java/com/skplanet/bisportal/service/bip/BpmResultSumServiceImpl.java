package com.skplanet.bisportal.service.bip;

import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmDlyPrst;
import com.skplanet.bisportal.model.bip.BpmMthStcPrst;
import com.skplanet.bisportal.model.bip.BpmWkStcPrst;
import com.skplanet.bisportal.repository.bip.BpmDlyPrstRepository;
import com.skplanet.bisportal.repository.bip.BpmMthStcPrstRepository;
import com.skplanet.bisportal.repository.bip.BpmWkStcPrstRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The BpmResultSumServiceImpl class
 *
 * @author sjune
 */

@Service
public class BpmResultSumServiceImpl implements BpmResultSumService {
	@Autowired
	private BpmDlyPrstRepository bpmDlyPrstRepository;
	@Autowired
	private BpmWkStcPrstRepository bpmWkStcPrstRepository;
	@Autowired
	private BpmMthStcPrstRepository bpmMthStcPrstRepository;

	@Override
	public List<BpmDlyPrst> getBpmDailyResultSums(WhereCondition whereCondition) {
		return bpmDlyPrstRepository.getBpmDailyResultSums(whereCondition);
	}

	@Override
	public List<BpmWkStcPrst> getBpmWeeklyResultSums(WhereCondition whereCondition) {
		return bpmWkStcPrstRepository.getBpmWeeklyResultSums(whereCondition);
	}

	@Override
	public List<BpmMthStcPrst> getBpmMonthlyResultSums(WhereCondition whereCondition) {
		return bpmMthStcPrstRepository.getBpmMonthlyResultSums(whereCondition);
	}
}
