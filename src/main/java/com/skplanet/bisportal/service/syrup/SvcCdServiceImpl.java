package com.skplanet.bisportal.service.syrup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.model.syrup.SmwCldrWk;
import com.skplanet.bisportal.repository.syrup.SvcCdRepository;

/**
 * Created by lko on 2014-11-21.
 */
@Service
public class SvcCdServiceImpl implements SvcCdService {
	@Autowired
	private SvcCdRepository svcCdRepository;

	@Override
	public List<SmwCldrWk> getWkStrd(SmwCldrWk smwCldrWk) {
		return svcCdRepository.getWkStrd(smwCldrWk);
	}

	@Override
	public List<SmwCldrWk> getWkStrds(SmwCldrWk smwCldrWk) {
		return svcCdRepository.getWkStrds(smwCldrWk);
	}
}
