package com.skplanet.bisportal.common.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.skplanet.bisportal.common.model.SummaryReportRow;
import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.common.utils.NumberUtil;

/**
 * The DayCellHandler class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
public class DayCellHandler implements CellHandler {
	@Override
	public void handleCell(Workbook workbook, Sheet worksheet, Row row, SummaryReportRow summaryReportRow,
			int headerColSpan) throws Exception {
		CellStyle styleHVCenter = ExcelUtil.getCenterStyle(worksheet);
		CellStyle styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);

		// 1일전
		Cell cell3 = row.createCell(headerColSpan + 2);
		if (summaryReportRow.getOneDayAgoMeasureValue() != null)
			cell3.setCellValue(summaryReportRow.getOneDayAgoMeasureValue().longValue());
		cell3.setCellStyle(styleHVRight);
		// 1일전 증감치
		Cell cell4 = row.createCell(headerColSpan + 3);
		if (summaryReportRow.getBasicMeasureValue() != null && summaryReportRow.getOneDayAgoMeasureValue() != null) {
			cell4.setCellValue(NumberUtil.calculateGrowth(summaryReportRow.getBasicMeasureValue().doubleValue(),
					summaryReportRow.getOneDayAgoMeasureValue().doubleValue()));
		}
		cell4.setCellStyle(styleHVCenter);
		// 1주전
		Cell cell5 = row.createCell(headerColSpan + 4);
		if (summaryReportRow.getOneWeekAgoMeasureValue() != null)
			cell5.setCellValue(summaryReportRow.getOneWeekAgoMeasureValue().longValue());
		cell5.setCellStyle(styleHVRight);
		// 1주전 증감치
		Cell cell6 = row.createCell(headerColSpan + 5);
		if (summaryReportRow.getBasicMeasureValue() != null && summaryReportRow.getOneWeekAgoMeasureValue() != null) {
			cell6.setCellValue(NumberUtil.calculateGrowth(summaryReportRow.getBasicMeasureValue().doubleValue(),
					summaryReportRow.getOneWeekAgoMeasureValue().doubleValue()));
		}
		cell6.setCellStyle(styleHVCenter);
		// 1달전
		Cell cell7 = row.createCell(headerColSpan + 6);
		if (summaryReportRow.getOneMonthAgoMeasureValue() != null)
			cell7.setCellValue(summaryReportRow.getOneMonthAgoMeasureValue().longValue());
		cell7.setCellStyle(styleHVRight);
		// 1달전 증감치
		Cell cell8 = row.createCell(headerColSpan + 7);
		if (summaryReportRow.getBasicMeasureValue() != null && summaryReportRow.getOneMonthAgoMeasureValue() != null) {
			cell8.setCellValue(NumberUtil.calculateGrowth(summaryReportRow.getBasicMeasureValue().doubleValue(),
					summaryReportRow.getOneMonthAgoMeasureValue().doubleValue()));
		}
		cell8.setCellStyle(styleHVCenter);
	}
}
