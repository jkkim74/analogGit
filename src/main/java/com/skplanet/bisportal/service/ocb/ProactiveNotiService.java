package com.skplanet.bisportal.service.ocb;

import java.util.List;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsPushCnfgSta;

/**
 * Created by cookatrice on 2014. 5. 21..
 */
public interface ProactiveNotiService {
    List<ObsPushCnfgSta> getNotificationSetForPivot(JqGridRequest jqGridRequest);
}
