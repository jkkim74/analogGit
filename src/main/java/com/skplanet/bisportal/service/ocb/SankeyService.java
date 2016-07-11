package com.skplanet.bisportal.service.ocb;

import java.util.List;

import com.skplanet.bisportal.model.sankey.SankeyRequest;
import com.skplanet.bisportal.model.sankey.SankeyResponse;

/**
 * Created by cookatrice on 2015. 8. 11..
 */
public interface SankeyService {
	List<SankeyResponse> getSankeyDataSet(SankeyRequest sankeyRequest);
}
