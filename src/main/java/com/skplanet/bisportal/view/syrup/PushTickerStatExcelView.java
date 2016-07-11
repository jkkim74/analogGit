package com.skplanet.bisportal.view.syrup;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.syrup.SmwPushTickerStatDtl;
import com.skplanet.bisportal.view.AbstractExcelView;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;
import static com.skplanet.bisportal.common.utils.Objects.uncheckedCast;

/**
 * Created by lko on 2014-12-05.
 */
public class PushTickerStatExcelView extends AbstractExcelView {
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String menuName = "푸쉬 티커 실적";
		Sheet worksheet = workbook.createSheet(menuName);
		JqGridRequest jqGridRequest = (JqGridRequest) model.get("jqgridRequest");
		String dateLabel;
		if (isDay(jqGridRequest.getDateType())) {
			dateLabel = "기준일자";
		} else if (isWeek(jqGridRequest.getDateType())) {
			dateLabel = "표시년주차";
		} else {
			dateLabel = "기준년월";
		}
		String headers[][] = { { "유형", dateLabel, "푸시메시지", "대상건수", "발송완료건수", "발송성공률", "UV" } };

		int headLength = headers[0].length;
		// 칼럼 넓이 조정.
		super.setColumnWith(worksheet, headLength);
		// 타이틀 입력.
		super.buildTitle(worksheet, menuName, headLength);
		// 헤드 입력.
		super.buildHeaders(worksheet, headers, 0, 0);

		// confirm Row offset
		int startRowIndex = 3;

		// 엑셀 바디의 셀 스타일 정의(문자 가운데 정렬).
		CellStyle styleHVCenter = ExcelUtil.getCenterStyle(worksheet);
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마).
		CellStyle styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마, 소수2자리).
		CellStyle styleHVRightComma = ExcelUtil.getRightFormatStyle(workbook, worksheet);
		// 엑셀 바디 생성.
		List<SmwPushTickerStatDtl> smwPushTickerStatDtlList = uncheckedCast(model.get("pushTickerStatExcelViewData"));

		String[] merge = new String[2];
		int startRow = 3, lastRow = 3, startDispDtRow = 3, lastDispDtRow = 3;
		int syrupMbrentStaSize = smwPushTickerStatDtlList.size();
		for (SmwPushTickerStatDtl smwPushTickerStatDtls : smwPushTickerStatDtlList) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// Retrieve the 일자 value
			ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, smwPushTickerStatDtls.getCdNm());
			ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_STRING, styleHVCenter, smwPushTickerStatDtls.getDispDt());
			ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_STRING, styleHVCenter, smwPushTickerStatDtls.getPushMsg());
			ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_NUMERIC, styleHVRight, smwPushTickerStatDtls.getTgtCnt().doubleValue());
			ExcelUtil.createCell(row, 4, Cell.CELL_TYPE_NUMERIC, styleHVRight, smwPushTickerStatDtls.getSendCnt().doubleValue());
			ExcelUtil.createCell(row, 5, Cell.CELL_TYPE_NUMERIC, styleHVRightComma, smwPushTickerStatDtls.getSendSucCnt().doubleValue());
			ExcelUtil.createCell(row, 6, Cell.CELL_TYPE_NUMERIC, styleHVRight, smwPushTickerStatDtls.getUv().doubleValue());

			// 셀 병합처리
			if (StringUtils.equals(merge[0], smwPushTickerStatDtls.getCdNm())) {
				lastRow++;
				if ((startRowIndex == syrupMbrentStaSize + 3) && (startRow < lastRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
				}
			} else {
				if (startRow < lastRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
				}
				startRow = startRowIndex - 1;
				lastRow = startRowIndex - 1;
			}
			// 셀 병합처리
			if (StringUtils.equals(merge[1], smwPushTickerStatDtls.getCdNm() + smwPushTickerStatDtls.getDispDt())) {
				lastDispDtRow++;
				if ((startRowIndex == syrupMbrentStaSize + 3) && (startDispDtRow < lastDispDtRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startDispDtRow, lastDispDtRow, 1, 1));
				}
			} else {
				if (startDispDtRow < lastDispDtRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startDispDtRow, lastDispDtRow, 1, 1));
				}
				startDispDtRow = startRowIndex - 1;
				lastDispDtRow = startRowIndex - 1;
			}
			merge[0] = smwPushTickerStatDtls.getCdNm();
			merge[1] = smwPushTickerStatDtls.getCdNm() + smwPushTickerStatDtls.getDispDt();
		}
	}
}
