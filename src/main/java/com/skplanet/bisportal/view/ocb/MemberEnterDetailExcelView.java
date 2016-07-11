package com.skplanet.bisportal.view.ocb;

import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.ocb.ObsMbrentSta;
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
 * The MemberEnterDetailExcelView class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
public class MemberEnterDetailExcelView extends AbstractExcelView {
	/**
	 * APP인증(성별, 연령별) 엑셀 저장.
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
	@SuppressWarnings({ "unchecked" })
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String menuName = "App 인증(성별,연령)";
		Sheet worksheet = workbook.createSheet(menuName);
		String headers[][] = { { "기준일자", "성별", "연령대", "신규가입자수", "누적가입자수" } };
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
		List<ObsMbrentSta> obsMbrentStas = uncheckedCast(model.get("memberEnterDetailData"));
		String[] merge = new String[3];
		int startRow = 3, lastRow = 3, startSexRow = 3, lastSexRow = 3;
		int obsMbrentStaSize = obsMbrentStas.size();
		for (ObsMbrentSta obsMbrentSta : obsMbrentStas) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// Retrieve the 기준일자 value
			Cell cell1 = row.createCell(0);
			cell1.setCellValue(obsMbrentSta.getStdDt());
			cell1.setCellStyle(styleHVCenter);

			// Retrieve the 성별 value
			Cell cell2 = row.createCell(1);
			cell2.setCellValue(obsMbrentSta.getSexIndNm());
			cell2.setCellStyle(styleHVCenter);

			// Retrieve the 연령대 value
			Cell cell3 = row.createCell(2);
			cell3.setCellValue(obsMbrentSta.getAgeLgrpNm());
			cell3.setCellStyle(styleHVCenter);

			// Retrieve the 신규가입자수 value
			Cell cell4 = row.createCell(3);
			cell4.setCellValue(obsMbrentSta.getNewEntrCnt());
			cell4.setCellStyle(styleHVRight);

			// Retrieve the 누적가입자수 value
			Cell cell5 = row.createCell(4);
			cell5.setCellValue(obsMbrentSta.getAcmEntrCnt());
			cell5.setCellStyle(styleHVRight);

			if (StringUtils.equals(merge[0], obsMbrentSta.getStdDt())) {
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

			if (StringUtils.equals(merge[1], obsMbrentSta.getSexIndNm())) {
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
			merge[0] = obsMbrentSta.getStdDt();
			merge[1] = obsMbrentSta.getSexIndNm();
		}
	}
}
