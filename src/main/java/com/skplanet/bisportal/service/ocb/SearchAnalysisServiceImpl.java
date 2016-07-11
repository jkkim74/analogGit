package com.skplanet.bisportal.service.ocb;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsSrchClickAlliSta;
import com.skplanet.bisportal.model.ocb.ObsSrchClickBnftSta;
import com.skplanet.bisportal.model.ocb.ObsSrchClickStoreSta;
import com.skplanet.bisportal.repository.ocb.ObsSrchClickAlliStaRepository;
import com.skplanet.bisportal.repository.ocb.ObsSrchClickBnftStaRepository;
import com.skplanet.bisportal.repository.ocb.ObsSrchClickStoreStaRepository;

/**
 * Created by cookatrice on 2014. 5. 19..
 */
@Service
public class SearchAnalysisServiceImpl implements SearchAnalysisService {
	@Autowired
	private ObsSrchClickStoreStaRepository obsSrchClickStoreStaRepository;

	@Autowired
	private ObsSrchClickAlliStaRepository obsSrchClickAlliStaRepository;

	@Autowired
	private ObsSrchClickBnftStaRepository obsSrchClickBnftStaRepository;

	@Override
	public List<ObsSrchClickStoreSta> getSearchResultClickStoreForPivot(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsSrchClickStoreStaRepository.getSearchResultClickStoreForPivotPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsSrchClickStoreStaRepository.getSearchResultClickStoreForPivotPerWeek(jqGridRequest);
		} else {
			return obsSrchClickStoreStaRepository.getSearchResultClickStoreForPivotPerMonth(jqGridRequest);
		}
	}

	@Override
	public List<ObsSrchClickStoreSta> getSearchResultClickStoreForGrid(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsSrchClickStoreStaRepository.getSearchResultClickStoreForGridPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsSrchClickStoreStaRepository.getSearchResultClickStoreForGridPerWeek(jqGridRequest);
		} else {
			return obsSrchClickStoreStaRepository.getSearchResultClickStoreForGridPerMonth(jqGridRequest);
		}
	}

	@Override
	public List<ObsSrchClickAlliSta> getSearchResultClickAlliForPivot(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsSrchClickAlliStaRepository.getSearchResultClickAlliForPivotPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsSrchClickAlliStaRepository.getSearchResultClickAlliForPivotPerWeek(jqGridRequest);
		} else {
			return obsSrchClickAlliStaRepository.getSearchResultClickAlliForPivotPerMonth(jqGridRequest);
		}
	}

	@Override
	public List<ObsSrchClickAlliSta> getSearchResultClickAlliForGrid(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsSrchClickAlliStaRepository.getSearchResultClickAlliForGridPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsSrchClickAlliStaRepository.getSearchResultClickAlliForGridPerWeek(jqGridRequest);
		} else {
			return obsSrchClickAlliStaRepository.getSearchResultClickAlliForGridPerMonth(jqGridRequest);
		}
	}

	@Override
	public List<ObsSrchClickBnftSta> getSearchResultClickBnftForGrid(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsSrchClickBnftStaRepository.getSearchResultClickBnftPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsSrchClickBnftStaRepository.getSearchResultClickBnftPerWeek(jqGridRequest);
		} else {
			return obsSrchClickBnftStaRepository.getSearchResultClickBnftPerMonth(jqGridRequest);
		}
	}
}
