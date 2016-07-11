package com.skplanet.bisportal.service.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsVstExcptPushSta;

import java.util.List;

/**
 * Created by cookatrice on 2014. 5. 9..
 */
public interface VisitExceptPushService {
	List<ObsVstExcptPushSta> getVisitsExceptPushOutlineForGrid(JqGridRequest jqGRidRequest);
}
