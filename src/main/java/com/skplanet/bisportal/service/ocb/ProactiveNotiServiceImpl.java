package com.skplanet.bisportal.service.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsPushCnfgSta;
import com.skplanet.bisportal.repository.ocb.ObsPushCnfgStaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

/**
 * Created by cookatrice on 2014. 5. 19..
 */
@Service
public class ProactiveNotiServiceImpl implements ProactiveNotiService {
	@Autowired
	private ObsPushCnfgStaRepository obsPushCnfgStaRepository;

	@Override
	public List<ObsPushCnfgSta> getNotificationSetForPivot(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsPushCnfgStaRepository.getNotificationSetForPivotPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsPushCnfgStaRepository.getNotificationSetForPivotPerWeek(jqGridRequest);
		} else {
			return obsPushCnfgStaRepository.getNotificationSetForPivotPerMonth(jqGridRequest);
		}
	}
}
