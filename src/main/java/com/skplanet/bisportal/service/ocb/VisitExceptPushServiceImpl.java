package com.skplanet.bisportal.service.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsVstExcptPushSta;
import com.skplanet.bisportal.repository.ocb.ObsVstExceptPushStaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

/**
 * Created by cookatrice on 2014. 5. 9..
 */
@Service
public class VisitExceptPushServiceImpl implements VisitExceptPushService {

	@Autowired
	private ObsVstExceptPushStaRepository obsVstExceptPushStaRepository;

	@Override
	public List<ObsVstExcptPushSta> getVisitsExceptPushOutlineForGrid(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsVstExceptPushStaRepository.getVisitsExceptPushOutlineForGridPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsVstExceptPushStaRepository.getVisitsExceptPushOutlineForGridPerWeek(jqGridRequest);
		} else {
			return obsVstExceptPushStaRepository.getVisitsExceptPushOutlineForGridPerMonth(jqGridRequest);
		}
	};

}
