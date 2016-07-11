package com.skplanet.bisportal.service.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsMbrentSta;
import com.skplanet.bisportal.repository.ocb.ObsMbrentStaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

/**
 * Created by cookatrice on 2014. 5. 7..
 */
@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	private ObsMbrentStaRepository obsMbrentStaRepository;

	@Override
	public List<ObsMbrentSta> getEnterForGrid(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsMbrentStaRepository.getEnterForGridPerDay(jqGridRequest);
		} else if(isWeek(jqGridRequest.getDateType())) {
			return obsMbrentStaRepository.getEnterForGridPerWeek(jqGridRequest);
		} else {
			return obsMbrentStaRepository.getEnterForGridPerMonth(jqGridRequest);
		}
	}

	@Override
	public List<ObsMbrentSta> getEnterDetailForGrid(JqGridRequest jqGridRequest) {
		if(isDay(jqGridRequest.getDateType())) {
			return obsMbrentStaRepository.getEnterDetailForGridPerDay(jqGridRequest);
		} else if(isWeek(jqGridRequest.getDateType())) {
			return obsMbrentStaRepository.getEnterDetailForGridPerWeek(jqGridRequest);
		} else {
			return obsMbrentStaRepository.getEnterDetailForGridPerMonth(jqGridRequest);
		}
	}
}
