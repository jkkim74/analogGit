package com.skplanet.bisportal.view.dashboard;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.SummaryReportRow;
import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.common.utils.NumberUtil;
import com.skplanet.bisportal.view.AbstractExcelView;
import lombok.extern.slf4j.Slf4j;
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
 * The PlatformExcelView class.
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 *
 */
@Slf4j
public class PlatformExcelView extends AbstractExcelView {
	/**
	 * 대시보드 리포트 엑셀 저장.
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
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			JqGridRequest jqGridRequest = uncheckedCast(model.get("jqGridRequest"));
			String titleName = "플랫폼사업부문";
			Sheet worksheet = workbook.createSheet(titleName);
			String[][] headers = this.appendHeader(jqGridRequest);
			int headLength = headers[0].length;
			// 칼럼 넓이 조정.
			super.setColumnWith(worksheet, headLength);
			// 타이틀 입력.
			super.buildTitle(worksheet, titleName, headLength);
			// 엑셀 헤더 생성.
			super.buildHeaders(worksheet, headers, 0, 0);
			// 엑셀 바디 생성.
			this.appendData(workbook, worksheet, 0, model);
		} catch (Exception e) {
			log.error("buildExcelDocument {}", e);
			String titleName = "Unknown";
			Sheet sheet = workbook.getSheet(titleName);
			if (sheet == null)
				sheet = workbook.createSheet(titleName);
			// 칼럼 넓이 조정.
			super.setColumnWith(sheet, 5);
			// 타이틀 입력.
			super.buildTitle(sheet, titleName, 5);
		}
	}

	/**
	 * 엑셀 헤더 데이.
	 *
	 * @param jqGridRequest
	 *            요청 파라미터.
	 * @return String[][]
	 */
	private String[][] appendHeader(JqGridRequest jqGridRequest) throws Exception {
		String[][] headers = new String[1][9];
		try {
			String changeFormat = jqGridRequest.getBasicDate().substring(0, 4) + "." + jqGridRequest.getBasicDate().substring(4, 6) + "." + jqGridRequest.getBasicDate().substring(6);
			headers[0][0] = "BM";
			headers[0][1] = "지표";
			headers[0][2] = "기준일(" + changeFormat + ")";
			String oneDayAgo = com.skplanet.bisportal.common.utils.DateUtil.addDays(jqGridRequest.getBasicDate(), -1);
			headers[0][3] = "1일전(" + oneDayAgo.substring(0, 4) + "." + oneDayAgo.substring(4, 6)
					+ "." + oneDayAgo.substring(6) + ")";
			headers[0][4] = "1일전 비교증감치";
			String oneWeekAgo = com.skplanet.bisportal.common.utils.DateUtil.addDays(jqGridRequest.getBasicDate(), -7);
			headers[0][5] = "1주전(" + oneWeekAgo.substring(0, 4) + "."
					+ oneWeekAgo.substring(4, 6) + "." + oneWeekAgo.substring(6) + ")";
			headers[0][6] = "1주전 비교증감치";
			String oneYearAgo = com.skplanet.bisportal.common.utils.DateUtil.addMonths(jqGridRequest.getBasicDate(), -1);
			headers[0][7] = "1달전(" + oneYearAgo.substring(0, 4) + "."
					+ oneYearAgo.substring(4, 6) + "." + oneYearAgo.substring(6) + ")";
			headers[0][8] = "1달전 비교증감치";
		} catch (Exception e) {
			log.error("handleHeader {}", e);
			throw new Exception("day header added error!");
		}
		return headers;
	}

	/**
	 * 엑셀 바디 데이터.
	 *
	 * @param workbook
	 *            엑셀 객체.
	 * @param worksheet
	 *            엑셀 시트 객체.
	 * @param headerColSpan
	 *            지표이하 좌표정보.
	 * @return void
	 */
	private void appendData(Workbook workbook, Sheet worksheet, int headerColSpan, Map<String, Object> model) throws Exception {
		try {
			// Row offset
			int startRowIndex = 3;
			// 엑셀 바디의 셀 스타일 정의(문자 가운데 정렬).
			CellStyle styleHVCenter = ExcelUtil.getCenterStyle(worksheet);
			// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마).
			CellStyle styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);
			List<SummaryReportRow> summaryDatas = uncheckedCast(model.get("summaryData"));

			for (SummaryReportRow summaryReportRow : summaryDatas) {
				Row row = worksheet.createRow(startRowIndex++);
				// BM
				Cell cell0 = row.createCell(headerColSpan);
				cell0.setCellValue(summaryReportRow.getSvcNm());
				cell0.setCellStyle(styleHVCenter);
				// 지표
				Cell cell1 = row.createCell(headerColSpan + 1);
				cell1.setCellValue(summaryReportRow.getMeasure());
				cell1.setCellStyle(styleHVCenter);
				// 기준일
				Cell cell2 = row.createCell(headerColSpan + 2);
				if (summaryReportRow.getBasicMeasureValue() != null)
					cell2.setCellValue(summaryReportRow.getBasicMeasureValue().doubleValue());
				cell2.setCellStyle(styleHVRight);
				// 1일전
				Cell cell3 = row.createCell(headerColSpan + 3);
				if (summaryReportRow.getOneDayAgoMeasureValue() != null)
					cell3.setCellValue(summaryReportRow.getOneDayAgoMeasureValue().longValue());
				cell3.setCellStyle(styleHVRight);
				// 1일전 증감치
				Cell cell4 = row.createCell(headerColSpan + 4);
				if (summaryReportRow.getBasicMeasureValue() != null && summaryReportRow.getOneDayAgoMeasureValue() != null) {
					cell4.setCellValue(NumberUtil.calculateGrowth(summaryReportRow.getBasicMeasureValue().doubleValue(),
							summaryReportRow.getOneDayAgoMeasureValue().doubleValue()));
				}
				cell4.setCellStyle(styleHVCenter);
				// 1주전
				Cell cell5 = row.createCell(headerColSpan + 5);
				if (summaryReportRow.getOneWeekAgoMeasureValue() != null)
					cell5.setCellValue(summaryReportRow.getOneWeekAgoMeasureValue().longValue());
				cell5.setCellStyle(styleHVRight);
				// 1주전 증감치
				Cell cell6 = row.createCell(headerColSpan + 6);
				if (summaryReportRow.getBasicMeasureValue() != null && summaryReportRow.getOneWeekAgoMeasureValue() != null) {
					cell6.setCellValue(NumberUtil.calculateGrowth(summaryReportRow.getBasicMeasureValue().doubleValue(),
							summaryReportRow.getOneWeekAgoMeasureValue().doubleValue()));
				}
				cell6.setCellStyle(styleHVCenter);
				// 1달전
				Cell cell7 = row.createCell(headerColSpan + 7);
				if (summaryReportRow.getOneMonthAgoMeasureValue() != null)
					cell7.setCellValue(summaryReportRow.getOneMonthAgoMeasureValue().longValue());
				cell7.setCellStyle(styleHVRight);
				// 1달전 증감치
				Cell cell8 = row.createCell(headerColSpan + 8);
				if (summaryReportRow.getBasicMeasureValue() != null && summaryReportRow.getOneMonthAgoMeasureValue() != null) {
					cell8.setCellValue(NumberUtil.calculateGrowth(summaryReportRow.getBasicMeasureValue().doubleValue(),
							summaryReportRow.getOneMonthAgoMeasureValue().doubleValue()));
				}
				cell8.setCellStyle(styleHVCenter);
			}
		} catch (Exception e) {
			log.error("appendData {}", e);
			throw new Exception("excel append error!");
		}

	}
}
