package com.skplanet.bisportal.common.excel;

import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.model.ReportDateType;
import com.skplanet.bisportal.common.model.SummaryReportRow;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

/**
 * The SummaryCellSelector(요액페이지 셀 생성 처리) class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Singleton
public class SummaryCellSelector {
	private Map<String, CellHandler> handlerMap;

	public SummaryCellSelector() {
		this.handlerMap = Maps.newHashMap();
		this.handlerMap.put(Constants.DATE_DAY, new DayCellHandler());
		this.handlerMap.put(Constants.DATE_WEEK, new WeekCellHandler());
		this.handlerMap.put(Constants.DATE_MONTH, new MonthCellHandler());
	}

	public void getHandleCell(ReportDateType dateType, Workbook workbook, Sheet worksheet, Row row,
			SummaryReportRow summaryReportRow, int headerColSpan) throws Exception {
		this.handlerMap.get(dateType.value()).handleCell(workbook, worksheet, row, summaryReportRow, headerColSpan);
	}
}
