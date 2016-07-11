package com.skplanet.bisportal.service.bip;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.bip.DayVisitor;

import java.util.List;

/**
 * Created by lko on 2014-09-30.
 */
public interface StatisticsService {
    /*
    일변방문자
     */
    List<DayVisitor> getDayVisitor(JqGridRequest jqGridRequest) throws Exception;
}
