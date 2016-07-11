package com.skplanet.bisportal.service.syrup;

import java.util.List;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.OlapDimensionRequest;
import com.skplanet.bisportal.model.syrup.SmwAppClickN;
import com.skplanet.bisportal.model.syrup.SmwAppExec;
import com.skplanet.bisportal.model.syrup.SmwSyrupDauFunnels;

/**
 * Created by cookatrice on 15. 1. 8..
 */
public interface VisitSituationService {
	/**
	 * App 방문현황
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */
	List<SmwAppExec> getAppVisitSituation(OlapDimensionRequest olapDimensionRequest);

	/**
	 * 메뉴별 방문현황
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */
	List<SmwAppClickN> getMenuVisitSituation(OlapDimensionRequest olapDimensionRequest);

	/**
	 * Syrup DAU 기준 유입경로
	 *
	 * @param jqGRidRequest
	 * @return
	 */
	List<SmwSyrupDauFunnels> getInflRtVisitSituation(JqGridRequest jqGRidRequest);
}
