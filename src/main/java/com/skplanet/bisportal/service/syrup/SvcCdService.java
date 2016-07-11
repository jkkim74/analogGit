package com.skplanet.bisportal.service.syrup;

import com.skplanet.bisportal.model.syrup.SmwCldrWk;

import java.util.List;

/**
 * Created by lko on 2014-11-21.
 */
public interface SvcCdService {
    List<SmwCldrWk> getWkStrd(SmwCldrWk smwCldrWk);
    List<SmwCldrWk> getWkStrds(SmwCldrWk smwCldrWk);
}
