package com.skplanet.bisportal.service.tmap;

import java.util.List;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.tmap.TmapDayKpi;
import com.skplanet.bisportal.model.tmap.TmapMonKpiStc;

/**
 * Created by ophelisis on 2015. 6. 11..
 */
public interface KpiService {
	List<TmapDayKpi> getDayKpiForChart(JqGridRequest jqGRidRequest);

	List<TmapDayKpi> getDayKpiForGrid(JqGridRequest jqGRidRequest);

	List<TmapMonKpiStc> getMonthKpiForGrid(JqGridRequest jqGridRequest);
}
