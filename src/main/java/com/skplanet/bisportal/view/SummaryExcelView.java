package com.skplanet.bisportal.view;

import com.skplanet.bisportal.common.excel.SummaryCellSelector;
import com.skplanet.bisportal.common.excel.SummaryHeaderSelector;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.ReportDateType;
import com.skplanet.bisportal.common.model.SummaryReportRow;
import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.bip.SummaryHeader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.skplanet.bisportal.common.utils.Objects.uncheckedCast;

/**
 * The SummaryExcelView class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Slf4j
public class SummaryExcelView extends AbstractExcelView {
	/**
	 * 요약페이지 엑셀 저장.
	 * 
	 * @param model
	 *            요청 파라미터 객체들.
	 * @param workbook
	 *            엑셀 객체.
	 * @param request
	 *            HttpServletRequest.
	 * @param response
	 *            HttpServletResponse.
	 * @return void
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String[][] headers;
		int headerColSpan;
		JqGridRequest jqGridRequest = null;
		SummaryHeader summaryHeader;
		try {
			jqGridRequest = uncheckedCast(model.get("jqGridRequest"));
			Sheet worksheet = workbook.createSheet(jqGridRequest.getTitleName());

			summaryHeader = uncheckedCast(model.get("periodData"));
			SummaryHeaderSelector summarySelector = injector.getInstance(SummaryHeaderSelector.class);
			headers = summarySelector.getHandleHeader(jqGridRequest, summaryHeader);
			headerColSpan = summaryHeader.getPeriods().size();

			int headLength = headers[0].length;
			// 칼럼 넓이 조정.
			super.setColumnWith(worksheet, headLength);
			// 타이틀 입력.
			super.buildTitle(worksheet, jqGridRequest.getTitleName(), headLength);
			// 헤드 입력.
			super.buildHeaders(worksheet, headers, 2, headerColSpan);

			// 엑셀 바디 생성.
			this.appendData(workbook, worksheet, headers, headerColSpan,
					(List<SummaryReportRow>) model.get("summaryData"), jqGridRequest.getDateType());
		} catch (Exception e) {
			log.error("buildExcelDocument()", e);
			String titleName = "Unknown";
			if (jqGridRequest != null && StringUtils.isNotEmpty(jqGridRequest.getTitleName())) {
				titleName = jqGridRequest.getTitleName();
			}
			Sheet sheet = workbook.getSheet(titleName);
			if (sheet == null)
				sheet = workbook.createSheet(jqGridRequest.getTitleName());
			// 칼럼 넓이 조정.
			super.setColumnWith(sheet, 5);
			// 타이틀 입력.
			super.buildTitle(sheet, titleName, 5);
		}
	}

	/**
	 * 엑셀 통계 데이터 저장.
	 * 
	 * @param workbook
	 *            엑셀 객체.
	 * @param worksheet
	 *            엑셀 시트 객체.
	 * @param headers
	 *            엑셀 헤더 정보.
	 * @param headerColSpan
	 *            지표이하 좌표정보.
	 * @param summaryDatas
	 *            지표별 요약 데이터.
	 * @param dateType
	 *            일간/주간/월간 구분.
	 * @return void
	 */
	private void appendData(Workbook workbook, Sheet worksheet, String[][] headers, int headerColSpan,
			List<SummaryReportRow> summaryDatas, ReportDateType dateType) throws Exception {
		try {
			// Row offset
			int startRowIndex = 4;
			CellStyle styleHVCenter = ExcelUtil.getCenterStyle(worksheet);
			CellStyle styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);
			for (SummaryReportRow summaryReportRow : summaryDatas) {
				Row row = worksheet.createRow(startRowIndex++);
				for (int i = 0; i < headerColSpan; i++) {
					Cell cell0 = row.createCell(i);

					String key = StringUtils.replace(headers[1][i], ".", StringUtils.EMPTY);
					if (summaryReportRow.getPeriodMeasureMap().get(key) != null) {
						cell0.setCellValue(summaryReportRow.getPeriodMeasureMap().get(key).doubleValue());
					}
					cell0.setCellStyle(styleHVRight);
				}
				// 지표
				Cell cell1 = row.createCell(headerColSpan);
				cell1.setCellValue(summaryReportRow.getMeasure());
				cell1.setCellStyle(styleHVCenter);
				// 기준일
				Cell cell2 = row.createCell(headerColSpan + 1);
				if (summaryReportRow.getBasicMeasureValue() != null)
					cell2.setCellValue(summaryReportRow.getBasicMeasureValue().doubleValue());
				cell2.setCellStyle(styleHVRight);
				SummaryCellSelector cellSelector = injector.getInstance(SummaryCellSelector.class);
				cellSelector.getHandleCell(dateType, workbook, worksheet, row, summaryReportRow, headerColSpan);
			}
		} catch (Exception e) {
			log.error("appendData()", e);
			throw new Exception("excel append error!");
		}
	}
}
