package com.skplanet.bisportal.service.ocb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsPresntSumRpt;
import com.skplanet.bisportal.repository.ocb.ObsPresntSumRptRepository;

/**
 * Created by cookatrice on 2014. 8. 11..
 */
@Service
public class PointPresentSumServiceImpl implements PointPresentSumService {
	@Autowired
	private ObsPresntSumRptRepository obsPresntSumRptRepository;

	@Override
	public List<ObsPresntSumRpt> getPointPresentSumForGrid(JqGridRequest jqGridRequest) {
        return obsPresntSumRptRepository.getPointPresentSum(jqGridRequest);
	}

}
