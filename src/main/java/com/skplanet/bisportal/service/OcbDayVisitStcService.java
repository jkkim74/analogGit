package com.skplanet.bisportal.service;

import com.skplanet.bisportal.model.ocb.OcbDayVisitStc;

import java.util.List;
import java.util.Map;

/**
 * Created by cookatrice on 2014. 5. 2..
 */
public interface OcbDayVisitStcService {
	List<OcbDayVisitStc> getVisitorListPerDay(Map<String, Object> params);

	List<OcbDayVisitStc> getVisitorList(Map<String, Object> params);
}
