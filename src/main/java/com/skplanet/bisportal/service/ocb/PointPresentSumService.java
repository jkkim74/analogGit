package com.skplanet.bisportal.service.ocb;

import java.util.List;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsPresntSumRpt;

/**
 * Created by cookatrice on 2014. 8. 11..
 */
public interface PointPresentSumService {
	List<ObsPresntSumRpt> getPointPresentSumForGrid(JqGridRequest jqGridRequest);
}
