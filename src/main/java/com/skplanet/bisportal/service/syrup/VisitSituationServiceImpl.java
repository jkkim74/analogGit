package com.skplanet.bisportal.service.syrup;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.OlapDimensionRequest;
import com.skplanet.bisportal.model.syrup.SmwAppClickN;
import com.skplanet.bisportal.model.syrup.SmwAppExec;
import com.skplanet.bisportal.model.syrup.SmwSyrupDauFunnels;
import com.skplanet.bisportal.repository.syrup.SmwAppClickNRepository;
import com.skplanet.bisportal.repository.syrup.SmwAppExecRepository;
import com.skplanet.bisportal.repository.syrup.SmwSyrupDauFunnelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

/**
 * Created by cookatrice on 15. 1. 8..
 */
@Service
public class VisitSituationServiceImpl implements VisitSituationService {
	@Autowired
	private SmwAppExecRepository smwAppExecRepository;

	@Autowired
	private SmwAppClickNRepository smwAppClickNRepository;

	@Autowired
	private SmwSyrupDauFunnelsRepository smwSyrupDauFunnelsRepository;

	@Override
	public List<SmwAppExec> getAppVisitSituation(OlapDimensionRequest olapDimensionRequest) {
		String dimensions = olapDimensionRequest.getDimensions();
		// check dimension
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
			return smwAppExecRepository.getAppVisitSituationPerDay(olapDimensionRequest);
		} else if (isWeek(olapDimensionRequest.getDateType())) {
			return smwAppExecRepository.getAppVisitSituationPerWeek(olapDimensionRequest);
		} else {
			return smwAppExecRepository.getAppVisitSituationPerMonth(olapDimensionRequest);
		}
	}

	@Override
	public List<SmwAppClickN> getMenuVisitSituation(OlapDimensionRequest olapDimensionRequest) {
		String dimensions = olapDimensionRequest.getDimensions();
		// check dimension
		if (dimensions.indexOf("menu") > -1) {
			olapDimensionRequest.setUseMenu("Y");
		}
		if (dimensions.indexOf("menuDesc") > -1) {
			olapDimensionRequest.setUseMenuDesc("Y");
		}
		if (dimensions.indexOf("os") > -1) {
			olapDimensionRequest.setUseOs("Y");
		}
		if (dimensions.indexOf("standardDate") > -1) {
			olapDimensionRequest.setUseStandardDate("Y");
		}
		if (dimensions.indexOf("sex") > -1) {
			olapDimensionRequest.setUseSex("Y");
		}
		if (dimensions.indexOf("ageRange") > -1) {
			olapDimensionRequest.setUseAgeRange("Y");
		}
		if (dimensions.indexOf("telecom") > -1) {
			olapDimensionRequest.setUseTelecom("Y");
		}
		if (isDay(olapDimensionRequest.getDateType())) {
			return smwAppClickNRepository.getMenuVisitSituationPerDay(olapDimensionRequest);
		} else if (isWeek(olapDimensionRequest.getDateType())) {
			return smwAppClickNRepository.getMenuVisitSituationPerWeek(olapDimensionRequest);
		} else {
			return smwAppClickNRepository.getMenuVisitSituationPerMonth(olapDimensionRequest);
		}
	}

	@Override
	public List<SmwSyrupDauFunnels> getInflRtVisitSituation(JqGridRequest jqGridRequest) {
		return smwSyrupDauFunnelsRepository.getInflRtVisitSituationPerDay(jqGridRequest);
	}
}
