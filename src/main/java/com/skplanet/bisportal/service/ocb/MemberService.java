package com.skplanet.bisportal.service.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsMbrentSta;

import java.util.List;

/**
 * Created by cookatrice on 2014. 5. 7..
 */
public interface MemberService {
	List<ObsMbrentSta> getEnterForGrid(JqGridRequest jqGRidRequest);

	List<ObsMbrentSta> getEnterDetailForGrid(JqGridRequest jqGridRequest);
}
