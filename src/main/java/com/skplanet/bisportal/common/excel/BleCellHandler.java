package com.skplanet.bisportal.common.excel;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.skplanet.bisportal.model.ocb.BleNewTech;

/**
 * Created by pepsi on 15. 1. 8..
 */
public interface BleCellHandler {
	void handleBleCell(List<BleNewTech> bleNewTechs, Workbook workbook, Sheet worksheet, String statContents) throws Exception;
}
