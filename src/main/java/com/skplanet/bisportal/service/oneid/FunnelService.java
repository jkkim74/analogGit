package com.skplanet.bisportal.service.oneid;

import java.util.List;
import java.util.Map;

import com.skplanet.bisportal.model.oneid.FunnelRequest;
import com.skplanet.bisportal.model.oneid.OidFunnel;

/**
 * Created by mimul on 2015. 6. 11..
 */
public interface FunnelService {
	Map<String, List<OidFunnel>> getFunnelDatas(FunnelRequest funnelRequest);
	List<String> getStepCds(String procCd);
}
