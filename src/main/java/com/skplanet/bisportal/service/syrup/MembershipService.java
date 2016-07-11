package com.skplanet.bisportal.service.syrup;

import java.util.List;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.OlapDimensionRequest;
import com.skplanet.bisportal.model.syrup.SmwCardIssue;
import com.skplanet.bisportal.model.syrup.SmwCardIssueDtl;
import com.skplanet.bisportal.model.syrup.SmwRcmdStatDtl;

/**
 * Created by lko on 2014-11-27.
 */
public interface MembershipService {
	List<SmwCardIssueDtl> getCardIssueMemGrid(JqGridRequest jqGRidRequest);

	List<SmwCardIssueDtl> getCardIssuePaVouGrid(JqGridRequest jqGRidRequest);

	List<SmwRcmdStatDtl> getSmwRcmdStatDtlGrid(JqGridRequest jqGRidRequest);

	/**
	 * 멤버십발급
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */
	List<SmwCardIssue> getMembershipIssue(OlapDimensionRequest olapDimensionRequest);
}
