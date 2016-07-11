package com.skplanet.bisportal.service.syrup;

import com.skplanet.bisportal.model.syrup.CustomerSituation;
import com.skplanet.bisportal.common.model.OlapDimensionRequest;

import java.util.List;

/**
 * Created by seoseungho on 2014. 11. 21..
 */
public interface SyrupDashboardService {
	List<CustomerSituation> getCustomerSituationForPivot(OlapDimensionRequest olapDimensionRequest);
}
