package com.skplanet.bisportal.service.ocb;

import java.util.List;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsActvCustRpt;
import com.skplanet.bisportal.model.ocb.ObsActvJoinsRpt;

/**
 * Created by cookatrice on 2014. 8. 11..
 */
public interface ActivityAnalysisService {
	List<ObsActvCustRpt> getActivityCustomerReportForPivot(JqGridRequest jqGridRequest);

	List<ObsActvJoinsRpt> getActivityJoinsReportForPivot(JqGridRequest jqGridRequest);

}
