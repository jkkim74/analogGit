package com.skplanet.bisportal.service.tmap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.tmap.TmapDayKpi;
import com.skplanet.bisportal.model.tmap.TmapMonKpiStc;
import com.skplanet.bisportal.repository.tmap.TmapDayKpiRepository;
import com.skplanet.bisportal.repository.tmap.TmapMonKpiStcRepository;

/**
 * Created by ophelisis on 2015. 6. 11..
 */
@Service
public class KpiServiceImpl implements KpiService {
	@Autowired
	private TmapDayKpiRepository tmapDayKpiRepository;

	@Autowired
	private TmapMonKpiStcRepository tmapMonKpiStcRepository;

	@Override
	public List<TmapDayKpi> getDayKpiForChart(JqGridRequest jqGridRequest) {
		// re-setting endDate
		jqGridRequest.setEndDate(jqGridRequest.getEndDate().substring(0, 6));
		return tmapDayKpiRepository.getDayKpiPerDayForChart(jqGridRequest);
	}

	@Override
	public List<TmapDayKpi> getDayKpiForGrid(JqGridRequest jqGridRequest) {
		// re-setting endDate
		jqGridRequest.setEndDate(jqGridRequest.getEndDate().substring(0, 6));
		return tmapDayKpiRepository.getDayKpiPerDayForGrid(jqGridRequest);
	}

	@Override
	public List<TmapMonKpiStc> getMonthKpiForGrid(JqGridRequest jqGridRequest) {
		return tmapMonKpiStcRepository.getMonthKpiManagement(jqGridRequest);
	}
}
