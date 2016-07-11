package com.skplanet.bisportal.view.ocb;

import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.ocb.ObsVstSta;
import com.skplanet.bisportal.view.AbstractExcelView;
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
 * The VisitOutlineExcelView class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
public class VisitOutlineExcelView extends AbstractExcelView {
	/**
	 * 방문개요 엑셀 저장.
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
		String menuName = "방문개요";
		Sheet worksheet = workbook.createSheet(menuName);
		String headers[][] = { { "기준일자", "UV", "LV(Login Visitor)", "RV(Returning Visitor)", "방문횟수", "체류시간", "PV",
				"Bounce Rate" } };
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
		List<ObsVstSta> obsVstStas = uncheckedCast(model.get("visitOutlineData"));
		for (ObsVstSta obsVstSta : obsVstStas) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// Retrieve the 기준일자 value
			ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, obsVstSta.getStdDt());

			// Retrieve the UV value
			ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsVstSta.getUv().doubleValue());

			// Retrieve the LV(Login Visitor) value
			ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsVstSta.getLv().doubleValue());

			// Retrieve the RV(Returning Visitor) value
			ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsVstSta.getRv().doubleValue());

			// Retrieve the 방문횟수 value
			ExcelUtil.createCell(row, 4, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsVstSta.getVstCnt().doubleValue());

			// Retrieve the 체류시간 value
			ExcelUtil.createCell(row, 5, Cell.CELL_TYPE_NUMERIC, styleHVRightComma, obsVstSta.getTimeSptFVst().doubleValue());

			// Retrieve the PV value
			ExcelUtil.createCell(row, 6, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsVstSta.getPv().doubleValue());

			// Retrieve the Bounce Rate value
			ExcelUtil.createCell(row, 7, Cell.CELL_TYPE_NUMERIC, styleHVRightComma, obsVstSta.getBuncRate().doubleValue());
		}
	}
}
