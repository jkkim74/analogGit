package com.skplanet.bisportal.view.syrup;

import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.syrup.SmwCardIssueDtl;
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
 * Created by lko on 2014-11-27.
 */
public class MebershipIssueMemExcelView extends AbstractExcelView {
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String menuName = "멤버십 발급 상세_일반 멤버십";
		Sheet worksheet = workbook.createSheet(menuName);
		String headers[][] = { { "일자", "카드명", "총발급건수", "기간내발급건수", "카드실행건수", "카드실행자수", "포인트조회실행건수", "포인트조회실행자수",
				"발급페이지조회건수", "발급페이지조회자수" } };

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
		List<SmwCardIssueDtl> smwDCardIssueDtlList = uncheckedCast(model.get("mebershipIssueMemExcelViewData"));
		for (SmwCardIssueDtl smwDCardIssueDtls : smwDCardIssueDtlList) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// Retrieve the 일자 value
			Cell cell1 = row.createCell(0);
			cell1.setCellValue(smwDCardIssueDtls.getOpenDt());
			cell1.setCellStyle(styleHVCenter);

			// Retrieve the 카드명 value
			Cell cell2 = row.createCell(1);
			cell2.setCellValue(smwDCardIssueDtls.getCardName());
			cell2.setCellStyle(styleHVCenter);

			// Retrieve the 총발급건수 value
			Cell cell3 = row.createCell(2);
			cell3.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell3.setCellValue(smwDCardIssueDtls.getTotIssueCnt().longValue());
			cell3.setCellStyle(styleHVRight);

			// Retrieve the 기간내발급건수value
			Cell cell4 = row.createCell(3);
			cell4.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell4.setCellValue(smwDCardIssueDtls.getDayIssueCnt().longValue());
			cell4.setCellStyle(styleHVRight);

			// Retrieve the 카드실행건수 value
			Cell cell5 = row.createCell(4);
			cell5.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell5.setCellValue(smwDCardIssueDtls.getCardExecCnt().longValue());
			cell5.setCellStyle(styleHVRight);

			// Retrieve the 카드실행자수 value
			Cell cell6 = row.createCell(5);
			cell6.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell6.setCellValue(smwDCardIssueDtls.getCardExecUserCnt().longValue());
			cell6.setCellStyle(styleHVRight);

			// Retrieve the 포인트조회실행건수 value
			Cell cell7 = row.createCell(6);
			cell7.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell7.setCellValue(smwDCardIssueDtls.getPntQryExecCnt().longValue());
			cell7.setCellStyle(styleHVRight);

			// Retrieve the 포인트조회실행자수 value
			Cell cell8 = row.createCell(7);
			cell8.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell8.setCellValue(smwDCardIssueDtls.getPntQryExecUserCnt().longValue());
			cell8.setCellStyle(styleHVRight);

			// Retrieve the 발급페이지조회건수 value
			Cell cell9 = row.createCell(8);
			cell9.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell9.setCellValue(smwDCardIssueDtls.getPageQryCnt().longValue());
			cell9.setCellStyle(styleHVRight);

			// Retrieve the 발급페이지조회자수 value
			Cell cell10 = row.createCell(9);
			cell10.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell10.setCellValue(smwDCardIssueDtls.getPageQryUserCnt().longValue());
			cell10.setCellStyle(styleHVRight);
		}
	}
}
