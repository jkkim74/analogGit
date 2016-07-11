package com.skplanet.bisportal.view.ocb;

import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.ocb.ObsMbrentSta;
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
 * The MemberEnterExcelView class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
public class MemberEnterExcelView extends AbstractExcelView {
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
	@SuppressWarnings({"unchecked"})
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String menuName = "App 인증";
		Sheet worksheet = workbook.createSheet(menuName);
		String headers[][] = {{"기준일자", "신규가입자수", "누적가입자수"}};
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

		// 엑셀 바디 생성.
		List<ObsMbrentSta> obsMbrentStas = uncheckedCast(model.get("memberEnterData"));
		for (ObsMbrentSta obsMbrentSta: obsMbrentStas) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// Retrieve the 기준일자 value
			Cell cell1 = row.createCell(0);
			cell1.setCellType(Cell.CELL_TYPE_STRING);
			cell1.setCellValue(obsMbrentSta.getStdDt());
			cell1.setCellStyle(styleHVCenter);

			// Retrieve the 신규가입자수 value
			Cell cell2 = row.createCell(1);
			cell2.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell2.setCellValue(obsMbrentSta.getNewEntrCnt());
			cell2.setCellStyle(styleHVRight);

			// Retrieve the 누적가입자수 value
			Cell cell3 = row.createCell(2);
			cell3.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell3.setCellValue(obsMbrentSta.getAcmEntrCnt());
			cell3.setCellStyle(styleHVRight);
		}
	}
}
