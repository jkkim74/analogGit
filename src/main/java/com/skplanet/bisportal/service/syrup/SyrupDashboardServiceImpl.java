package com.skplanet.bisportal.service.syrup;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.common.model.OlapDimensionRequest;
import com.skplanet.bisportal.model.syrup.CustomerSituation;
import com.skplanet.bisportal.repository.syrup.SyrupDashboardRepository;

/**
 * Created by seoseungho on 2014. 11. 21..
 */
@Service
public class SyrupDashboardServiceImpl implements SyrupDashboardService {
	@Autowired
	private SyrupDashboardRepository syrupDashboardRepository;

	@Override
	public List<CustomerSituation> getCustomerSituationForPivot(OlapDimensionRequest olapDimensionRequest) {
		String dimensions = olapDimensionRequest.getDimensions();
		// check dimension
		// TODO refactoring!!!!
		if (dimensions.indexOf("os") > -1) {
			olapDimensionRequest.setUseOs("Y");
		}
		if (dimensions.indexOf("standardDate") > -1) {
			olapDimensionRequest.setUseStandardDate("Y");
		}
		if (dimensions.indexOf("sex") > -1) {
			olapDimensionRequest.setUseSex("Y");
		}
		if (dimensions.indexOf("appVersion") > -1) {
			olapDimensionRequest.setUseAppVersion("Y");
		}
		if (dimensions.indexOf("ageRange") > -1) {
			olapDimensionRequest.setUseAgeRange("Y");
		}
		if (dimensions.indexOf("telecom") > -1) {
			olapDimensionRequest.setUseTelecom("Y");
		}

		if (isDay(olapDimensionRequest.getDateType())) {
			return syrupDashboardRepository.getCustomerSituationPerDay(olapDimensionRequest);
		} else if (isWeek(olapDimensionRequest.getDateType())){
			return syrupDashboardRepository.getCustomerSituationPerWeek(olapDimensionRequest);
		} else{
			return syrupDashboardRepository.getCustomerSituationPerMonth(olapDimensionRequest);
		}
	}
}
