package com.skplanet.bisportal.service.syrup;

import java.util.List;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.OlapDimensionRequest;
import com.skplanet.bisportal.model.syrup.SmwCponLocStat;
import com.skplanet.bisportal.model.syrup.SmwCponStat;
import com.skplanet.bisportal.model.syrup.SmwCponStatDtl;

/**
 * Created by lko on 2014-11-21.
 */
public interface CouponService {
	List<SmwCponStatDtl> getCponStatForGrid(JqGridRequest jqGRidRequest);

	List<SmwCponStatDtl> getCponStatForGridForExcel(JqGridRequest jqGRidRequest);

	List<SmwCponLocStat> getCponLocStatForGrid(JqGridRequest jqGRidRequest);

	/**
	 * 쿠폰실적
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */
	List<SmwCponStat> getCouponAchieve(OlapDimensionRequest olapDimensionRequest);
}
