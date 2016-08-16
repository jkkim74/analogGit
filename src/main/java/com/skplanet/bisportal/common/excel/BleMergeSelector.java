package com.skplanet.bisportal.common.excel;

import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import com.google.common.collect.Maps;
//import com.google.inject.Singleton;
import com.skplanet.bisportal.common.model.MultiHeader;

/**
 * The BleMergeSelector(BLE 동정 셀 병합 생성 처리) class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
//@Singleton
public class BleMergeSelector {
	private Map<Integer, BleMergeHandler> mergeMap;

	public BleMergeSelector() {
		this.mergeMap = Maps.newHashMap();
		this.mergeMap.put(1, new AllBleMergeHandler());
		this.mergeMap.put(2, new OcbBleMergeHandler());
		this.mergeMap.put(3, new SyrupBleMergeHandler());
	}

	public void getBleCell(int serviceTypeCode, Sheet worksheet, MultiHeader headers, String statContents)
			throws Exception {
		this.mergeMap.get(serviceTypeCode).handleBleMerge(worksheet, headers, statContents);
	}
}
