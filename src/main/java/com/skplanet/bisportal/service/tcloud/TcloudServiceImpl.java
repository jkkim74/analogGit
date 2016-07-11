package com.skplanet.bisportal.service.tcloud;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.tcloud.TCloudMenuStat;
import com.skplanet.bisportal.repository.tcloud.CloudRepository;

/**
 * Created by cookatrice on 14. 12. 24..
 */
@Service
public class TcloudServiceImpl implements TcloudService {
	@Autowired
	private CloudRepository cloudRepository;

	@Override
	public List<TCloudMenuStat> getMenuStat(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return cloudRepository.getTCloudMenuStatPerDay(jqGridRequest);
		} else {
			return cloudRepository.getTCloudMenuStatPerMonth(jqGridRequest);
		}
	}
}
