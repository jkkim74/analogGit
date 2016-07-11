package com.skplanet.bisportal.view.syrup;

import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.syrup.SmwSyrupDauFunnels;
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

import static com.skplanet.bisportal.common.utils.Objects.uncheckedCast;

/**
 * The InflRtVisitSituationExcelView class.
 * 
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 * 
 */
public class InflRtVisitSituationExcelView extends AbstractExcelView {
	/**
	 * Syrup DAU 기준 유입경로 엑셀 저장.
	 * 
	 * @param model 요청 파라미터 객체들.
	 * @param workbook 엑셀 객체.
	 * @param request HttpServletRequest.
	 * @param response HttpServletResponse.
	 * @return void
	 */
	@Override
	@SuppressWarnings({ "unchecked" })
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String menuName = "Syrup DAU 기준 유입경로(Page_id 기준)";
		Sheet worksheet = workbook.createSheet(menuName);
		String headers[][] = { { "기준일자", "CATEGORY", "PAGE_ID", "고객수" } };
		int headLength = headers[0].length;
		// 칼럼 넓이 조정.
		super.setColumnWith(worksheet, headLength);
		// 타이틀 입력.
		super.buildTitle(worksheet, menuName, headLength);
		// 헤드 입력.
		super.buildHeaders(worksheet, headers, 0, 0);
		// Row offset
		int startRowIndex = 3;

		CellStyle styleHVLeft = ExcelUtil.getLeftStyle(worksheet);
		// 엑셀 바디의 셀 스타일 정의(문자 가운데 정렬).
		CellStyle styleHVCenter = ExcelUtil.getCenterStyle(worksheet);
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마).
		CellStyle styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마, 소수2자리).
		//CellStyle styleHVRightComma = ExcelUtil.getRightFormatStyle(workbook, worksheet);

		// 엑셀 바디 생성.
		List<SmwSyrupDauFunnels> smwSyrupDauFunnelss = uncheckedCast(model.get("inflRtVisitSituationData"));

		String[] merge = new String[2];
		int startRow = 3, lastRow = 3, startCatRow = 3, lastCatRow = 3;
		int smwSyrupDauFunnelsSize = smwSyrupDauFunnelss.size();
		for (SmwSyrupDauFunnels smwSyrupDauFunnels : smwSyrupDauFunnelss) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// Retrieve the 기준일자 value
			ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, smwSyrupDauFunnels.getStrdDt());

			// Retrieve the CATEGORY value
			ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_STRING, styleHVLeft, smwSyrupDauFunnels.getCategory());

			// Retrieve the PAGE_ID value
			ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_STRING, styleHVLeft, smwSyrupDauFunnels.getPageId());

			// Retrieve the 고객수 value
			ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_NUMERIC, styleHVRight, smwSyrupDauFunnels.getMbrCnt().doubleValue());

			// 셀 병합처리
			if (StringUtils.equals(merge[0], smwSyrupDauFunnels.getStrdDt())) {
				lastRow++;
				if ((startRowIndex == smwSyrupDauFunnelsSize + 3) && (startRow < lastRow)) {
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
			if (StringUtils.equals(merge[0], smwSyrupDauFunnels.getStrdDt()) && StringUtils.equals(merge[1], smwSyrupDauFunnels.getCategory())) {
				lastCatRow++;
				if ((startRowIndex == smwSyrupDauFunnelsSize + 3) && (startCatRow < lastCatRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startCatRow, lastCatRow, 1, 1));
				}
			} else {
				if (startCatRow < lastCatRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startCatRow, lastCatRow, 1, 1));
				}
				startCatRow = startRowIndex - 1;
				lastCatRow = startRowIndex - 1;
			}
			merge[0] = smwSyrupDauFunnels.getStrdDt();
			merge[1] = smwSyrupDauFunnels.getCategory();
		}
	}
}
