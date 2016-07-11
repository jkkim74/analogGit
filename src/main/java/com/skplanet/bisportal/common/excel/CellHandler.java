package com.skplanet.bisportal.common.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.skplanet.bisportal.common.model.SummaryReportRow;

/**
 * The CellHandler class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
public interface CellHandler {
	void handleCell(Workbook workbook, Sheet worksheet, Row row, SummaryReportRow summaryReportRow,
			int headerColSpan) throws Exception;
}
