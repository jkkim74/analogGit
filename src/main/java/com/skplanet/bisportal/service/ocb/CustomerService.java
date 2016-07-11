package com.skplanet.bisportal.service.ocb;

import com.skplanet.bisportal.common.model.AjaxResult;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.DauResponse;
import com.skplanet.bisportal.model.ocb.ObsActnSta;
import com.skplanet.bisportal.model.ocb.ObsSosBleSta;
import com.skplanet.bisportal.model.ocb.ObsVstFstUvSta;
import com.skplanet.bisportal.model.ocb.ObsVstInflowUvSta;
import com.skplanet.bisportal.model.ocb.RtdDau;

import java.util.List;

/**
 * The CustomerService class(고객 상세 분석).
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
public interface CustomerService {
	List<ObsActnSta> getCustomersActionForGrid(JqGridRequest jqGridRequest);

	List<ObsSosBleSta> getBleDiffData(JqGridRequest jqGridRequest);

	DauResponse getDau() throws Exception;

	List<RtdDau> getOcbDauForPivot(JqGridRequest jqGridRequest);

	List<ObsVstInflowUvSta> getVisitInflRtForPivot(JqGridRequest jqGridRequest);

	List<ObsVstFstUvSta> getVisitFstForPivot(JqGridRequest jqGridRequest);

	List<RtdDau> getSyrupDauForPivot(JqGridRequest jqGridRequest);

	List<RtdDau> getAppSticknessForPivot(JqGridRequest jqGridRequest);

	AjaxResult sendMMS(String serviceName);
}
