package com.skplanet.bisportal.service.ocb;

import java.util.List;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.BleNewTech;

/**
 * The BleService class(Ble NewTech 통계 정보).
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 * @see
 */
public interface BleService {
	List<BleNewTech> getMerchantDetail(JqGridRequest jqGRidRequest) throws Exception;
	List<BleNewTech> getServiceForGrid(JqGridRequest jqGRidRequest) throws Exception;
	List<BleNewTech> getServiceForExcel(JqGridRequest jqGridRequest) throws Exception;
}
