package com.skplanet.bisportal.service.ocb;

import java.util.List;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.*;

/**
 * Created by cookatrice on 2014. 5. 15..
 */
public interface ContentsDetailAnalysisService {
	List<ObsFeedSta> getFeedsExposureForGrid(JqGridRequest jqGridRequest);

	List<ObsFeedSta> getFeedsExposureOrderForPivot(JqGridRequest jqGridRequest);

	List<ObsFeedSta> getFeedsExposureOrderForGrid(JqGridRequest jqGridRequest);

	List<ObsStoreOvrlSta> getStoreTotalForPivot(JqGridRequest jqGridRequest);

	List<ObsStoreDetlSta> getStoreSingleForPivot(JqGridRequest jqGridRequest);

	List<ObsStoreDetlSta> getStoreSingleForGrid(JqGridRequest jqGridRequest);

	List<ObsStoreDetlSta> getStoreSingleForGridPagination(JqGridRequest jqGridRequest);

	List<ObsPntSta> getPntsForPivot(JqGridRequest jqGridRequest);

	List<ObsCntntDscvSta> getDiscoverForPivot(JqGridRequest jqGRidRequest);

	List<ObsCntntFlyrDetlSta> getStoreFlyerInTradeAreaFlyerForPivot(JqGridRequest jqGRidRequest);

    List<ObsCntntTraraFlyrSta> getStoreFlyerForPivot(JqGridRequest jqGRidRequest);

	List<ObsCntntMbilFlyrCopnSta> getMobileFrontCouponForPivot(JqGridRequest jqGRidRequest);

	List<ObsCntntMbilFlyrPageSta> getMobileFlyerPageInquiryForPivot(JqGridRequest jqGRidRequest);

	List<ObsMktPushSend> getMarketingPush(JqGridRequest jqGridRequest);
}
