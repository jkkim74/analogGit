package com.skplanet.bisportal.service.ocb;

import com.skplanet.bisportal.common.model.ChartRequest;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.*;

import java.util.List;

/**
 * Created by cookatrice on 2014. 5. 9..
 */
public interface VisitService {
	List<ObsVstSta> getVisitorListForGrid(JqGridRequest jqGRidRequest);

	List<ObsVstSta> getVisitorListForGrid2(JqGridRequest jqGridRequest);

	List<ObsVstSta> getVisitorListForChart(ChartRequest chartRequest);

	List<ObsVstSexAgeSta> getVisitSexAgeForGrid(JqGridRequest jqGRidRequest);

	List<ObsVstLngSta> getVisitsLangForGrid(JqGridRequest jqGridRequest);

	List<ObsVstRsltnSta> getVisitsRsltnForGrid(JqGridRequest jqGridRequest);

	List<ObsVstDvcMdlSta> getVisitorDvcMdlForGrid(JqGridRequest jqGridRequest);

	List<ObsVstTimeSta> getVisitTimeZoneForGrid(JqGridRequest jqGridRequest);

    List<ObsVstOsSta> getVisitOsForGrid(JqGridRequest jqGridRequest);

	List<ObsVstPageSta> getVisitorsPageForGrid(JqGridRequest jqGridRequest);

	List<SankeyAccessLog> getVisitorTrackingForChart(JqGridRequest jqGridRequest);
}
