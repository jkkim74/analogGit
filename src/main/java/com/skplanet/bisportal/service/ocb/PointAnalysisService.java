package com.skplanet.bisportal.service.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsMbilAchvRpt;
import com.skplanet.bisportal.model.ocb.ObsRsrvPntRpt;
import com.skplanet.bisportal.model.ocb.ObsTotPntRpt;
import com.skplanet.bisportal.model.ocb.ObsUsePntRpt;

import java.util.List;

/**
 * Created by cookatrice on 2014. 8. 11..
 */
public interface PointAnalysisService {
	List<ObsTotPntRpt> getTotalPointReportForPivot(JqGridRequest jqGridRequest);

	List<ObsRsrvPntRpt> getReservingPointReportForPivot(JqGridRequest jqGridRequest);

	List<ObsUsePntRpt> getUsePointReportForPivot(JqGridRequest jqGridRequest);

	List<ObsMbilAchvRpt> getMobileAchieveReportForPivot(JqGridRequest jqGridRequest);
}
