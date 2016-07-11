package com.skplanet.bisportal.service.ocb;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.*;
import com.skplanet.bisportal.repository.ocb.*;

/**
 * Created by cookatrice on 2014. 5. 9..
 */
@Service
public class ContentsDetailAnalysisServiceImpl implements ContentsDetailAnalysisService {
	@Autowired
	private ObsFeedStaRepository obsFeedStaRepository;

	@Autowired
	private ObsStoreOvrlStaRepository obsStoreOvrlStaRepository;

	@Autowired
	private ObsStoreDetlStaRepository obsStoreDetlStaRepository;

	@Autowired
	private ObsPntStaRepository obsPntStaRepository;

	@Autowired
	private ObsCntntDscvStaRepository obsCntntDscvStaRepository;

	@Autowired
	private ObsCntntFlyrDetlStaRepository obsCntntFlyrDetlStaRepository;

	@Autowired
	private ObsCntntTraraFlyrStaRepository obsCntntTraraFlyrStaRepository;

	@Autowired
	private ObsCntntMbilFlyrCopnStaRepository obsCntntMbilFlyrCopnStaRepository;

	@Autowired
	private ObsCntntMbilFlyrPageStaRepository obsCntntMbilFlyrPageStaRepository;

	@Autowired
	private ObsMktPushSendRepository ocbMarketingPushSendRepository;

	@Override
	public List<ObsFeedSta> getFeedsExposureForGrid(JqGridRequest jqGridRequest) {
		return obsFeedStaRepository.getFeedsExposureForGridPerDay(jqGridRequest);
	}

	@Override
	public List<ObsFeedSta> getFeedsExposureOrderForPivot(JqGridRequest jqGridRequest) {
		return obsFeedStaRepository.getFeedsExposureOrderForPivotPerDay(jqGridRequest);
	}

	@Override
	public List<ObsFeedSta> getFeedsExposureOrderForGrid(JqGridRequest jqGridRequest) {
		// TODO FOR sample test page.
		return obsFeedStaRepository.getFeedsExposureOrderForGridPerDay(jqGridRequest);
	}

	@Override
	public List<ObsStoreOvrlSta> getStoreTotalForPivot(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsStoreOvrlStaRepository.getStoreTotalForPivotPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsStoreOvrlStaRepository.getStoreTotalForPivotPerWeek(jqGridRequest);
		} else {
			return obsStoreOvrlStaRepository.getStoreTotalForPivotPerMonth(jqGridRequest);
		}
	}

	@Override
	public List<ObsStoreDetlSta> getStoreSingleForPivot(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsStoreDetlStaRepository.getStoreSingleForPivotPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsStoreDetlStaRepository.getStoreSingleForPivotPerWeek(jqGridRequest);
		} else {
			return obsStoreDetlStaRepository.getStoreSingleForPivotPerMonth(jqGridRequest);
		}
	}

	@Override
	public List<ObsStoreDetlSta> getStoreSingleForGrid(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsStoreDetlStaRepository.getStoreSingleForGridPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsStoreDetlStaRepository.getStoreSingleForGridPerWeek(jqGridRequest);
		} else {
			return obsStoreDetlStaRepository.getStoreSingleForGridPerMonth(jqGridRequest);
		}
	}

	@Override
	public List<ObsStoreDetlSta> getStoreSingleForGridPagination(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsStoreDetlStaRepository.getStoreSingleForGridPaginationPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsStoreDetlStaRepository.getStoreSingleForGridPaginationPerWeek(jqGridRequest);
		} else {
			return obsStoreDetlStaRepository.getStoreSingleForGridPaginationPerMonth(jqGridRequest);
		}
	}

	@Override
	public List<ObsPntSta> getPntsForPivot(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsPntStaRepository.getPntsPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsPntStaRepository.getPntsPerWeek(jqGridRequest);
		} else {
			return obsPntStaRepository.getPntsPerMonth(jqGridRequest);
		}
	}

	@Override
	public List<ObsCntntDscvSta> getDiscoverForPivot(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsCntntDscvStaRepository.getDiscoverForPivotPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsCntntDscvStaRepository.getDiscoverForPivotPerWeek(jqGridRequest);
		} else {
			return obsCntntDscvStaRepository.getDiscoverForPivotPerMonth(jqGridRequest);
		}
	}

	@Override
	public List<ObsCntntFlyrDetlSta> getStoreFlyerInTradeAreaFlyerForPivot(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsCntntFlyrDetlStaRepository.getStoreFlyerInTradeAreaFlyerForPivotPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsCntntFlyrDetlStaRepository.getStoreFlyerInTradeAreaFlyerForPivotPerWeek(jqGridRequest);
		} else {
			return obsCntntFlyrDetlStaRepository.getStoreFlyerInTradeAreaFlyerForPivotPerMonth(jqGridRequest);
		}

	}

	public List<ObsCntntTraraFlyrSta> getStoreFlyerForPivot(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsCntntTraraFlyrStaRepository.getStoreFlyerForPivotPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsCntntTraraFlyrStaRepository.getStoreFlyerForPivotPerWeek(jqGridRequest);
		} else {
			return obsCntntTraraFlyrStaRepository.getStoreFlyerForPivotPerMonth(jqGridRequest);
		}
	}

	public List<ObsCntntMbilFlyrCopnSta> getMobileFrontCouponForPivot(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return obsCntntMbilFlyrCopnStaRepository.getMobileFrontCouponPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsCntntMbilFlyrCopnStaRepository.getMobileFrontCouponPerWeek(jqGridRequest);
		} else {
			return obsCntntMbilFlyrCopnStaRepository.getMobileFrontCouponPerMonth(jqGridRequest);
		}
	}

    @Override
    public List<ObsCntntMbilFlyrPageSta> getMobileFlyerPageInquiryForPivot(JqGridRequest jqGRidRequest) {
		if (isDay(jqGRidRequest.getDateType())) {
			return obsCntntMbilFlyrPageStaRepository.getMobileFlyerPageInquiryPerDay(jqGRidRequest);
		} else if (isWeek(jqGRidRequest.getDateType())) {
			return obsCntntMbilFlyrPageStaRepository.getMobileFlyerPageInquiryPerWeek(jqGRidRequest);
		} else {
			return obsCntntMbilFlyrPageStaRepository.getMobileFlyerPageInquiryPerMonth(jqGRidRequest);
		}
    }

	@Override
	public List<ObsMktPushSend> getMarketingPush(JqGridRequest jqGridRequest) {
		return ocbMarketingPushSendRepository.getMarketingPush(jqGridRequest);
	}
}
