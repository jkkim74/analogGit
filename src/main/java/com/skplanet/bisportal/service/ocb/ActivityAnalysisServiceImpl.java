package com.skplanet.bisportal.service.ocb;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsActvCustRpt;
import com.skplanet.bisportal.model.ocb.ObsActvJoinsRpt;
import com.skplanet.bisportal.repository.ocb.ObsActvCustRptRepository;
import com.skplanet.bisportal.repository.ocb.ObsActvJoinsRptRepository;

/**
 * Created by cookatrice on 2014. 8. 11..
 */
@Service
public class ActivityAnalysisServiceImpl implements ActivityAnalysisService {
	@Autowired
	private ObsActvCustRptRepository obsActvCustRptRepository;

	@Autowired
	private ObsActvJoinsRptRepository obsActvJoinsRptRepository;

	@Override
	public List<ObsActvCustRpt> getActivityCustomerReportForPivot(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsActvCustRptRepository.getActivityCustomerReportPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsActvCustRptRepository.getActivityCustomerReportPerWeek(jqGridRequest);
		} else {
			return obsActvCustRptRepository.getActivityCustomerReportPerMonth(jqGridRequest);
		}
	}

	@Override
	public List<ObsActvJoinsRpt> getActivityJoinsReportForPivot(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsActvJoinsRptRepository.getActivityJoinsReportPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsActvJoinsRptRepository.getActivityJoinsReportPerWeek(jqGridRequest);
		} else {
			return obsActvJoinsRptRepository.getActivityJoinsReportPerMonth(jqGridRequest);
		}
	}
}
