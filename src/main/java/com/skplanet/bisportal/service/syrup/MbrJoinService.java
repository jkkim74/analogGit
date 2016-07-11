package com.skplanet.bisportal.service.syrup;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwMbrJoin;

import java.util.List;

/**
 * Created by lko on 2014-11-20.
 */
public interface MbrJoinService {
    List<SmwMbrJoin> getMbrJoinForGrid(JqGridRequest jqGRidRequest);
}
