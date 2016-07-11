package com.skplanet.bisportal.service.bip;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmSvcCd;
import com.skplanet.bisportal.repository.bip.BpmSvcCdRepository;

/**
 * Created by sjune on 2014-08-05.
 *
 * @author sjune
 */
@Service
public class BpmSvcCdServiceImpl implements BpmSvcCdService {
	@Autowired
	private BpmSvcCdRepository bpmSvcCdRepository;

	@Override
	public List<BpmSvcCd> getBpmSvcs() {
		return bpmSvcCdRepository.getBpmSvcs();
	}

	@Override
	public List<BpmSvcCd> getBpmCycleToGrps(WhereCondition whereCondition) {
		return bpmSvcCdRepository.getBpmCycleToGrps(whereCondition);
	}

	@Override
	public List<BpmSvcCd> getBpmGrpToCls(WhereCondition whereCondition) {
		return bpmSvcCdRepository.getBpmGrpToCls(whereCondition);
	}

	@Override
	public List<BpmSvcCd> getBpmWkStrds(String wkStcStrdYmw) {
		return bpmSvcCdRepository.getBpmWkStrds(wkStcStrdYmw);
	}
}
