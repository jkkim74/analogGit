package com.skplanet.bisportal.service.ocb;

import com.skplanet.bisportal.common.model.ChartRequest;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.*;
import com.skplanet.bisportal.repository.ocb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isMonth;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

/**
 * Created by cookatrice on 2014. 5. 9..
 */
@Service
public class VisitServiceImpl implements VisitService {
	@Autowired
	private ObsVstStaRepository obsVstStaRepository;

	@Autowired
	private ObsVstSexAgeStaRepository obsVstSexAgeStaRepository;

	@Autowired
	private ObsVstLngStaRepository obsVstLngStaRepository;

	@Autowired
	private ObsVstRsltnStaRepository obsVstRsltnStaRepository;

	@Autowired
	private ObsVstDvcMdlStaRepository obsVstDvcMdlStaRepository;

	@Autowired
	private ObsVstTimeStaRepository obsVstTimeStaRepository;

	@Autowired
	private ObsVstOsRepository obsVstOsRepository;

	@Autowired
	private ObsVstPageStaRepository obsVstPageStaRepository;

	@Override
	public List<ObsVstSta> getVisitorListForGrid(JqGridRequest jqGridRequest) {
		if(isDay(jqGridRequest.getDateType())) {
			return obsVstStaRepository.getVisitorListForGridPerDay(jqGridRequest);
		} else if(isWeek(jqGridRequest.getDateType())) {
			return obsVstStaRepository.getVisitorListForGridPerWeek(jqGridRequest);
		} else {
			return obsVstStaRepository.getVisitorListForGridPerMonth(jqGridRequest);
		}
	};

	@Override
	public List<ObsVstSta> getVisitorListForGrid2(JqGridRequest jqGridRequest) {
		if(isDay(jqGridRequest.getDateType())) {
			return obsVstStaRepository.getVisitorListForGridPerDay2(jqGridRequest);
		} else if(isWeek(jqGridRequest.getDateType())) {
			return obsVstStaRepository.getVisitorListForGridPerWeek2(jqGridRequest);
		} else {
			return obsVstStaRepository.getVisitorListForGridPerMonth2(jqGridRequest);
		}
	};

	@Override
	public List<ObsVstSta> getVisitorListForChart(ChartRequest chartRequest) {
		if(isDay(chartRequest.getDateType())) {
			return obsVstStaRepository.getVisitorListForChartPerDay(chartRequest);
		} else if(isWeek(chartRequest.getDateType())) {
			return obsVstStaRepository.getVisitorListForChartPerWeek(chartRequest);
		} else {
			return obsVstStaRepository.getVisitorListForChartPerMonth(chartRequest);
		}
	};

	@Override
	public List<ObsVstSexAgeSta> getVisitSexAgeForGrid(JqGridRequest jqGridRequest) {
		if(isDay(jqGridRequest.getDateType())) {
			return obsVstSexAgeStaRepository.getVisitorsSexAgePerDay(jqGridRequest);
		} else if(isWeek(jqGridRequest.getDateType())) {
			return obsVstSexAgeStaRepository.getVisitorsSexAgePerWeek(jqGridRequest);
		} else {
			return obsVstSexAgeStaRepository.getVisitorsSexAgePerMonth(jqGridRequest);
		}
	}

	@Override
	public List<ObsVstLngSta> getVisitsLangForGrid(JqGridRequest jqGridRequest) {
		if(isMonth(jqGridRequest.getDateType())) {
			return obsVstLngStaRepository.getVisitorsLangPerMonth(jqGridRequest);
		} else if(isWeek(jqGridRequest.getDateType())) {
			return obsVstLngStaRepository.getVisitorsLangPerWeek(jqGridRequest);
		}

		return obsVstLngStaRepository.getVisitorsLangPerDay(jqGridRequest);
	}

	@Override
	public List<ObsVstRsltnSta> getVisitsRsltnForGrid(JqGridRequest jqGridRequest) {
		if(isMonth(jqGridRequest.getDateType())) {
			return obsVstRsltnStaRepository.getVisitorsRsltnPerMonth(jqGridRequest);
		} else if(isWeek(jqGridRequest.getDateType())) {
			return obsVstRsltnStaRepository.getVisitorsRsltnPerWeek(jqGridRequest);
		}

		return obsVstRsltnStaRepository.getVisitorsRsltnPerDay(jqGridRequest);
	}

	@Override
	public List<ObsVstDvcMdlSta> getVisitorDvcMdlForGrid(JqGridRequest jqGridRequest) {
		if(isDay(jqGridRequest.getDateType())) {
			return obsVstDvcMdlStaRepository.getVisitorsDvcMdlPerDay(jqGridRequest);
		} else if(isWeek(jqGridRequest.getDateType())) {
			return obsVstDvcMdlStaRepository.getVisitorsDvcMdlPerWeek(jqGridRequest);
		} else {
			return obsVstDvcMdlStaRepository.getVisitorsDvcMdlPerMonth(jqGridRequest);
		}
	}

	@Override
	public List<ObsVstTimeSta> getVisitTimeZoneForGrid(JqGridRequest jqGridRequest) {
		if(isDay(jqGridRequest.getDateType())) {
			return obsVstTimeStaRepository.getVisitTimeZoneForGridPerDay(jqGridRequest);
		} else if(isWeek(jqGridRequest.getDateType())) {
			return obsVstTimeStaRepository.getVisitTimeZoneForGridPerWeek(jqGridRequest);
		} else {
			return obsVstTimeStaRepository.getVisitTimeZoneForGridPerMonth(jqGridRequest);
		}
	}

	@Override
	public List<ObsVstOsSta> getVisitOsForGrid(JqGridRequest jqGridRequest){
		if(isDay(jqGridRequest.getDateType())) {
			return obsVstOsRepository.getVisitOsForGridPerDay(jqGridRequest);
		} else if(isWeek(jqGridRequest.getDateType())) {
			return obsVstOsRepository.getVisitOsForGridPerWeek(jqGridRequest);
		} else {
			return obsVstOsRepository.getVisitOsForGridPerMonth(jqGridRequest);
		}
	}

	@Override
	public List<ObsVstPageSta> getVisitorsPageForGrid(JqGridRequest jqGridRequest) {
		if(isMonth(jqGridRequest.getDateType())) {
			return obsVstPageStaRepository.getVisitorsPagePerMonth(jqGridRequest);
		} else if(isWeek(jqGridRequest.getDateType())) {
			return obsVstPageStaRepository.getVisitorsPagePerWeek(jqGridRequest);
		}

		return obsVstPageStaRepository.getVisitorsPagePerDay(jqGridRequest);
	}

	@Override
	public List<SankeyAccessLog> getVisitorTrackingForChart(JqGridRequest jqGridRequest) {
		return obsVstPageStaRepository.getVisitorTrackingPerDay(jqGridRequest);
	}
}
