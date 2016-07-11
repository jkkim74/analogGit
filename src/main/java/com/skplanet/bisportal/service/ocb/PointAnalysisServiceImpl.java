package com.skplanet.bisportal.service.ocb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsMbilAchvRpt;
import com.skplanet.bisportal.model.ocb.ObsRsrvPntRpt;
import com.skplanet.bisportal.model.ocb.ObsTotPntRpt;
import com.skplanet.bisportal.model.ocb.ObsUsePntRpt;
import com.skplanet.bisportal.repository.ocb.ObsMbilAchvRptRepository;
import com.skplanet.bisportal.repository.ocb.ObsRsrvPntRptRepository;
import com.skplanet.bisportal.repository.ocb.ObsTotPntRptRepository;
import com.skplanet.bisportal.repository.ocb.ObsUsePntRptRepository;

/**
 * Created by cookatrice on 2014. 8. 11..
 */
@Service
public class PointAnalysisServiceImpl implements PointAnalysisService {
	@Autowired
	private ObsTotPntRptRepository obsTotPntRptRepository;

	@Autowired
	private ObsRsrvPntRptRepository obsRsrvPntRptRepository;

	@Autowired
	private ObsUsePntRptRepository obsUsePntRptRepository;

	@Autowired
	private ObsMbilAchvRptRepository obsMbilAchvRptRepository;


    @Override
    public List<ObsTotPntRpt> getTotalPointReportForPivot(JqGridRequest jqGridRequest) {
        return obsTotPntRptRepository.getTotalPointReport(jqGridRequest);
    }

    @Override
    public List<ObsRsrvPntRpt> getReservingPointReportForPivot(JqGridRequest jqGridRequest) {
        return obsRsrvPntRptRepository.getReservingPointReport(jqGridRequest);
    }

    @Override
    public List<ObsUsePntRpt> getUsePointReportForPivot(JqGridRequest jqGridRequest) {
        return obsUsePntRptRepository.getUsePointReport(jqGridRequest);
    }

    @Override
    public List<ObsMbilAchvRpt> getMobileAchieveReportForPivot(JqGridRequest jqGridRequest) {
        return obsMbilAchvRptRepository.getMobileAchieveReport(jqGridRequest);
    }
}
