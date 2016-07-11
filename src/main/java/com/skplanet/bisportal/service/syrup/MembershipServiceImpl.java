package com.skplanet.bisportal.service.syrup;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.OlapDimensionRequest;
import com.skplanet.bisportal.model.syrup.SmwCardIssue;
import com.skplanet.bisportal.model.syrup.SmwCardIssueDtl;
import com.skplanet.bisportal.model.syrup.SmwRcmdStatDtl;
import com.skplanet.bisportal.repository.syrup.SmwCardIssueDtlRepository;
import com.skplanet.bisportal.repository.syrup.SmwCardIssueRepository;
import com.skplanet.bisportal.repository.syrup.SmwRcmdStatDtlRepository;

/**
 * Created by lko on 2014-11-27.
 */
@Service
public class MembershipServiceImpl implements MembershipService {
	@Autowired
	private SmwCardIssueDtlRepository smwCardIssueDtlRepository;

	@Autowired
	private SmwRcmdStatDtlRepository smwRcmdStatDtlRepository;

	@Autowired
	private SmwCardIssueRepository smwCardIssueRepository;

	@Override
	public List<SmwCardIssueDtl> getCardIssueMemGrid(JqGridRequest jqGRidRequest) {
		return smwCardIssueDtlRepository.getCardIssueMem(jqGRidRequest);
	}

	@Override
	public List<SmwCardIssueDtl> getCardIssuePaVouGrid(JqGridRequest jqGRidRequest) {
		return smwCardIssueDtlRepository.getCardIssuePaVou(jqGRidRequest);
	}

	@Override
	public List<SmwRcmdStatDtl> getSmwRcmdStatDtlGrid(JqGridRequest jqGRidRequest) {
		if (isDay(jqGRidRequest.getDateType())) {
			return smwRcmdStatDtlRepository.getSmwRcmdStatDtlPerDay(jqGRidRequest);
		} else if (isWeek(jqGRidRequest.getDateType())) {
			return smwRcmdStatDtlRepository.getSmwRcmdStatDtlPerWeek(jqGRidRequest);
		} else {
			return smwRcmdStatDtlRepository.getSmwRcmdStatDtlPerMonth(jqGRidRequest);
		}
	}

	/**
	 * 멤버십발급
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */
	@Override
	public List<SmwCardIssue> getMembershipIssue(OlapDimensionRequest olapDimensionRequest) {
		String dimensions = olapDimensionRequest.getDimensions();
		// check dimension
		if (dimensions.indexOf("os") > -1) {
			olapDimensionRequest.setUseOs("Y");
		}
		if (dimensions.indexOf("standardDate") > -1) {
			olapDimensionRequest.setUseStandardDate("Y");
		}
		if (dimensions.indexOf("membership") > -1) {
			olapDimensionRequest.setUseMembership("Y");
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
			return smwCardIssueRepository.getMembershipIssuePerDay(olapDimensionRequest);
		} else if (isWeek(olapDimensionRequest.getDateType())) {
			return smwCardIssueRepository.getMembershipIssuePerWeek(olapDimensionRequest);
		} else {
			return smwCardIssueRepository.getMembershipIssuePerMonth(olapDimensionRequest);
		}
	}
}
