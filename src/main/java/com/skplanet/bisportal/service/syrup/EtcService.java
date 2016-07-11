package com.skplanet.bisportal.service.syrup;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwPushTickerStatDtl;

import java.util.List;

/**
 * Created by lko on 2014-12-05.
 */
public interface EtcService {
    List<SmwPushTickerStatDtl> getSmwPushTickerStatDtlGrid(JqGridRequest jqGRidRequest);
}

