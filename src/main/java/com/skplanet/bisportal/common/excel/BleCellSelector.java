package com.skplanet.bisportal.common.excel;

import com.google.common.collect.Maps;
//import com.google.inject.Singleton;
import com.skplanet.bisportal.model.ocb.BleNewTech;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;

/**
 * The BleCellSelector(BLE 동적 셀 생성 처리) class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
//@Singleton
public class BleCellSelector {
	private Map<Integer, BleCellHandler> cellMap;

	public BleCellSelector() {
		this.cellMap = Maps.newHashMap();
		this.cellMap.put(1, new AllBleCellHandler());
		this.cellMap.put(2, new OcbBleCellHandler());
		this.cellMap.put(3, new SyrupBleCellHandler());
	}

	public void getBleCell(int serviceTypeCode, List<BleNewTech> bleNewTechs, Workbook workbook, Sheet worksheet, String statContents) throws Exception {
		this.cellMap.get(serviceTypeCode).handleBleCell(bleNewTechs, workbook, worksheet, statContents);
	}
}
