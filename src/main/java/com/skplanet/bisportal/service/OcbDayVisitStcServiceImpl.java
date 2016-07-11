package com.skplanet.bisportal.service;

import com.skplanet.bisportal.model.ocb.OcbDayVisitStc;
import com.skplanet.bisportal.repository.OcbDayVisitStcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by cookatrice on 2014. 5. 2..
 */

@Service
public class OcbDayVisitStcServiceImpl implements OcbDayVisitStcService {
	@Autowired
	private OcbDayVisitStcRepository ocbDayVisitStcRepository;

	@Override
	public List<OcbDayVisitStc> getVisitorListPerDay(Map<String, Object> params) {
		return ocbDayVisitStcRepository.getVisitorListPerDay(params);
	}

	@Override
	public List<OcbDayVisitStc> getVisitorList(Map<String, Object> params) {
		return ocbDayVisitStcRepository.getVisitorList(params);
	}
}
