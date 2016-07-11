package com.skplanet.bisportal.view.syrup;

import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.syrup.SmwCponStatDtl;
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
 * Created by lko on 2014-11-26.
 *
 * The MbrJoinExcelView class.
 *
 * @author kyoungoh lee
 *
 */
public class MbrJoinExcelView extends AbstractExcelView {
	/**
	 * syrup 쿠폰 실적 상세 엑셀 저장.
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
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String menuName = "쿠폰 실적 상세";
		Sheet worksheet = workbook.createSheet(menuName);
		String headers[][] = { { "쿠폰ID", "유형", "브랜드명(제휴사명)", "쿠폰명", "발급건수", "누적발급건수", "사용건수", "누적사용건수", "노출건수",
				"누적노출건수", "발급페이지조회수", "누적발급페이지조회수", "상세발급페이지조회수", "누적상세발급페이지조회수" } };
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
		List<SmwCponStatDtl> smwDCponStatDtlList = uncheckedCast(model.get("couponStatData"));
		for (SmwCponStatDtl smwDCponStatDtls : smwDCponStatDtlList) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// Retrieve the 쿠폰ID value
			Cell cell1 = row.createCell(0);
			cell1.setCellValue(smwDCponStatDtls.getCpProdCd());
			cell1.setCellStyle(styleHVCenter);

			// Retrieve the 브랜드명(제휴사명) value
			Cell cell2 = row.createCell(2);
			cell2.setCellValue(smwDCponStatDtls.getPtNm());
			cell2.setCellStyle(styleHVCenter);

			// Retrieve the 유형 value
			Cell cell3 = row.createCell(1);
			cell3.setCellValue(smwDCponStatDtls.getRegPocNm());
			cell3.setCellStyle(styleHVCenter);

			// Retrieve the 쿠폰명 value
			Cell cell4 = row.createCell(3);
			cell4.setCellValue(smwDCponStatDtls.getCponNm());
			cell4.setCellStyle(styleHVCenter);

			// Retrieve the 발급건수 value
			Cell cell5 = row.createCell(4);
			cell5.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell5.setCellValue(smwDCponStatDtls.getIssueCnt().longValue());
			cell5.setCellStyle(styleHVRight);

			// Retrieve the 누적발급건수 value
			Cell cell6 = row.createCell(5);
			cell6.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell6.setCellValue(smwDCponStatDtls.getAccumIssueCnt().longValue());
			cell6.setCellStyle(styleHVRight);

			// Retrieve the 사용건수 value
			Cell cell7 = row.createCell(6);
			cell7.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell7.setCellValue(smwDCponStatDtls.getUseCnt().longValue());
			cell7.setCellStyle(styleHVRight);

			// Retrieve the 누적사용건수 value
			Cell cell8 = row.createCell(7);
			cell8.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell8.setCellValue(smwDCponStatDtls.getAccumUseCnt().longValue());
			cell8.setCellStyle(styleHVRight);

			// Retrieve the 노출건수 value
			Cell cell9 = row.createCell(8);
			cell9.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell9.setCellValue(smwDCponStatDtls.getShowCnt().longValue());
			cell9.setCellStyle(styleHVRight);

			// Retrieve the 누적노출건수 value
			Cell cell10 = row.createCell(9);
			cell10.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell10.setCellValue(smwDCponStatDtls.getAccumShowCnt().longValue());
			cell10.setCellStyle(styleHVRight);

			// Retrieve the 발급페이지조회수 value
			Cell cell11 = row.createCell(10);
			cell11.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell11.setCellValue(smwDCponStatDtls.getIssuePageCnt().longValue());
			cell11.setCellStyle(styleHVRight);

			// Retrieve the 누적발급페이지조회수 value
			Cell cell12 = row.createCell(11);
			cell12.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell12.setCellValue(smwDCponStatDtls.getAccumIssuePageCnt().longValue());
			cell12.setCellStyle(styleHVRight);

			// Retrieve the 상세발급페이지조회수 value
			Cell cell13 = row.createCell(12);
			cell13.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell13.setCellValue(smwDCponStatDtls.getIssuePageDtlCnt().longValue());
			cell13.setCellStyle(styleHVRight);

			// Retrieve the 누적상세발급페이지조회수 value
			Cell cell14 = row.createCell(13);
			cell14.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell14.setCellValue(smwDCponStatDtls.getAccumIssuePageDtlCnt().longValue());
			cell14.setCellStyle(styleHVRight);
		}
	}
}
