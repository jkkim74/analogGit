package com.skplanet.bisportal.service.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsNewOcbSegRpt;

import java.util.List;

/**
 * Created by cookatrice on 2014. 8. 11..
 */
public interface NewOcbSegService {
	List<ObsNewOcbSegRpt> getNewOcbSegForGrid(JqGridRequest jqGridRequest);
}
