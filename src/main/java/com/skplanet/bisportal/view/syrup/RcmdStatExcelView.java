package com.skplanet.bisportal.view.syrup;

import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.syrup.SmwRcmdStatDtl;
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
 * Created by lko on 2014-12-05.
 */
public class RcmdStatExcelView extends AbstractExcelView {
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String menuName = "추천멤버십 발급현황";
		Sheet worksheet = workbook.createSheet(menuName);
		String headers[][] = { { "일자", "카드명", "기간내 노출 건수", "추천카드발급건수" } };

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

		// 엑셀 바디 생성.
		List<SmwRcmdStatDtl> smwRcmdStatDtlList = uncheckedCast(model.get("rcmdStatExcelViewData"));
		for (SmwRcmdStatDtl smwRcmdStatDtls : smwRcmdStatDtlList) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// Retrieve the 일자 value
			Cell cell1 = row.createCell(0);
			cell1.setCellValue(smwRcmdStatDtls.getStrdDt());
			cell1.setCellStyle(styleHVCenter);

			// Retrieve the 카드명 value
			Cell cell2 = row.createCell(1);
			cell2.setCellValue(smwRcmdStatDtls.getRecNm());
			cell2.setCellStyle(styleHVCenter);

			// Retrieve the 기간내 노출 건수 value
			Cell cell3 = row.createCell(2);
			cell3.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell3.setCellValue(smwRcmdStatDtls.getTgtCnt().longValue());
			cell3.setCellStyle(styleHVRight);

			// Retrieve the 기간내 노출 건수 value
			Cell cell4 = row.createCell(3);
			cell4.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell4.setCellValue(smwRcmdStatDtls.getIssueCnt().longValue());
			cell4.setCellStyle(styleHVRight);
		}
	}
}
