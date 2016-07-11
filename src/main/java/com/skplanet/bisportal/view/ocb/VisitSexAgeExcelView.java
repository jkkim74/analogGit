package com.skplanet.bisportal.view.ocb;

import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.ocb.ObsVstSexAgeSta;
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
 * The VisitSexAgeExcelView class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
public class VisitSexAgeExcelView extends AbstractExcelView {
	/**
	 * 방문개요(셩별,연령별) 엑셀 저장.
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
		String menuName = "방문개요(성별,연령)";
		Sheet worksheet = workbook.createSheet(menuName);
		String headers[][] = { { "기준일자", "성별", "연령대", "UV", "LV(Login Visitor)", "RV(Returning Visitor)", "방문횟수",
				"체류시간", "PV", "Bounce Rate" } };
		int headLength = headers[0].length;
		// 칼럼 넓이 조정.
		super.setColumnWith(worksheet, headLength);
		// 타이틀 입력.
		super.buildTitle(worksheet, menuName, headLength);
		// 헤드 입력.
		super.buildHeaders(worksheet, headers, 0, 0);
		// Row offset
		int startRowIndex = 3;

		// 엑셀 바디의 셀 스타일 정의(문자 가운데 정렬).
		CellStyle styleHVCenter = ExcelUtil.getCenterStyle(worksheet);
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마).
		CellStyle styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마, 소수2자리).
		CellStyle styleHVRightComma = ExcelUtil.getRightFormatStyle(workbook, worksheet);

		// 엑셀 바디 생성.
		List<ObsVstSexAgeSta> obsVstSexAgeStas = uncheckedCast(model.get("visitSexAgeData"));
		String[] merge = new String[3];
		int startRow = 3, lastRow = 3, startSexRow = 3, lastSexRow = 3;
		int obsMbrentStaSize = obsVstSexAgeStas.size();
		for (ObsVstSexAgeSta obsVstSexAgeSta : obsVstSexAgeStas) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// Retrieve the 기준일자 value
			ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, obsVstSexAgeSta.getStdDt());

			// Retrieve the 성별 value
			ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_STRING, styleHVCenter, obsVstSexAgeSta.getSexIndNm());

			// Retrieve the 연령대 value
			ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_STRING, styleHVCenter, obsVstSexAgeSta.getAgeLgrpNm());

			// Retrieve the UV value
			ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsVstSexAgeSta.getUv().doubleValue());

			// Retrieve the LV(Login Visitor) value
			ExcelUtil.createCell(row, 4, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsVstSexAgeSta.getLv().doubleValue());

			// Retrieve the RV(Returning Visitor) value
			ExcelUtil.createCell(row, 5, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsVstSexAgeSta.getRv().doubleValue());

			// Retrieve the 방문횟수 value
			ExcelUtil.createCell(row, 6, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsVstSexAgeSta.getVstCnt()
					.doubleValue());

			// Retrieve the 체류시간 value
			ExcelUtil.createCell(row, 7, Cell.CELL_TYPE_NUMERIC, styleHVRightComma, obsVstSexAgeSta.getTimeSptFVst()
					.doubleValue());

			// Retrieve the PV value
			ExcelUtil.createCell(row, 8, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsVstSexAgeSta.getPv().doubleValue());

			// Retrieve the Bounce Rate value
			ExcelUtil.createCell(row, 9, Cell.CELL_TYPE_NUMERIC, styleHVRightComma, obsVstSexAgeSta.getBuncRate()
					.doubleValue());

			// 셀 병합처리
			if (StringUtils.equals(merge[0], obsVstSexAgeSta.getStdDt())) {
				lastRow++;
				if ((startRowIndex == obsMbrentStaSize + 3) && (startRow < lastRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
				}
			} else {
				if (startRow < lastRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
				}
				startRow = startRowIndex - 1;
				lastRow = startRowIndex - 1;
			}
			if (StringUtils.equals(merge[1], obsVstSexAgeSta.getSexIndNm())) {
				lastSexRow++;
				if ((startRowIndex == obsMbrentStaSize + 3) && (startSexRow < lastSexRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startSexRow, lastSexRow, 1, 1));
				}
			} else {
				if (startSexRow < lastSexRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startSexRow, lastSexRow, 1, 1));
				}
				startSexRow = startRowIndex - 1;
				lastSexRow = startRowIndex - 1;
			}
			merge[0] = obsVstSexAgeSta.getStdDt();
			merge[1] = obsVstSexAgeSta.getSexIndNm();
		}
	}
}
