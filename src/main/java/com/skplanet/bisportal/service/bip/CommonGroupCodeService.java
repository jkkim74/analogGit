package com.skplanet.bisportal.service.bip;

import java.util.List;

import com.skplanet.bisportal.model.bip.CommonGroupCode;

/**
 *
 * @author cookatrice
 */
public interface CommonGroupCodeService {
//	public List<SearchCodeSection> getCodesByGroupCodeId(String groupCodeId);
	List<CommonGroupCode> getCodesByGroupCodeId(String groupCodeId);

}
