package com.skplanet.bisportal.service.tcloud;

import java.util.List;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.tcloud.TCloudMenuStat;

/**
 * Created by cookatrice on 14. 12. 24..
 */
public interface TcloudService {
	List<TCloudMenuStat> getMenuStat(JqGridRequest jqGRidRequest);
}
