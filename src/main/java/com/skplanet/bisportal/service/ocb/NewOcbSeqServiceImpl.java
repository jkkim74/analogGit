package com.skplanet.bisportal.service.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsNewOcbSegRpt;
import com.skplanet.bisportal.repository.ocb.ObsNewOcbSegRptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

/**
 * Created by cookatrice on 2014. 8. 11..
 */
@Service
public class NewOcbSeqServiceImpl implements NewOcbSegService {
	@Autowired
	private ObsNewOcbSegRptRepository obsNewOcbSegRptRepository;

	@Override
	public List<ObsNewOcbSegRpt> getNewOcbSegForGrid(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsNewOcbSegRptRepository.getNewOcbSegPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsNewOcbSegRptRepository.getNewOcbSegPerWeek(jqGridRequest);
		} else {
			return obsNewOcbSegRptRepository.getNewOcbSegPerMonth(jqGridRequest);
		}
	}
}
