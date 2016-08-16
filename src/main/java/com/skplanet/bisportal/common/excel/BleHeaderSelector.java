package com.skplanet.bisportal.common.excel;

import java.util.Map;

import com.google.common.collect.Maps;
//import com.google.inject.Singleton;
import com.skplanet.bisportal.common.model.MultiHeader;

/**
 * The BleHeaderSelector(BLE 동적 헤더 생성 처리) class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
//@Singleton
public class BleHeaderSelector {
	private Map<Integer, BleHeaderHandler> headerMap;

	public BleHeaderSelector() {
		headerMap = Maps.newHashMap();
		headerMap.put(1, new AllBleHeaderHandler());
		headerMap.put(2, new OcbBleHeaderHandler());
		headerMap.put(3, new SyrupBleHeaderHandler());
	}

	public MultiHeader getBleHeader(int serviceTypeCode, String statContents) throws Exception {
		return this.headerMap.get(serviceTypeCode).buildHeader(statContents);
	}
}
