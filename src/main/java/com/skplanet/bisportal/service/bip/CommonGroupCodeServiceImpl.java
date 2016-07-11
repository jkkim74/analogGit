package com.skplanet.bisportal.service.bip;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.model.bip.CommonGroupCode;
import com.skplanet.bisportal.repository.bip.CommonGroupCodeRepository;

/**
 * 
 * @author cookatrice
 */
@Service
public class CommonGroupCodeServiceImpl implements CommonGroupCodeService {
	@Autowired
	private CommonGroupCodeRepository commonGroupCodeRepository;

    @Override
    public List<CommonGroupCode> getCodesByGroupCodeId(String groupCodeId) {
        return commonGroupCodeRepository.getCodesByGroupCodeId(groupCodeId);
    }
}
