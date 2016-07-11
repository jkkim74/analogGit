package com.skplanet.bisportal.view.admin;

import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.bip.DayVisitor;
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
 * Created by lko on 2014-10-08.
 * 
 * The DayVisitorExcelView class.
 * 
 * @author kyoungoh lee
 */
public class DayVisitorExcelView extends AbstractExcelView {
	/**
	 * voyager 통계 일별 방문자 엑셀 저장.
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
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String menuName = "일별 방문자";
		Sheet worksheet = workbook.createSheet(menuName);
		String headers[][] = { { "일자", "서비스", "카테고리", "menu", "방문횟수" } };
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
		List<DayVisitor> dayVisitor = uncheckedCast(model.get("dayVisitorData"));
		String[] merge = new String[4];
		int startRow = 3, lastRow = 3, startSerRow = 3, lastSerRow = 3;
		int startCateRow = 3, lastCateRow = 3;
		int adminMbrentStaSize = dayVisitor.size();
		for (DayVisitor dayVisitors : dayVisitor) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// Retrieve the 기준일자 value
			ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, dayVisitors.getVisitDate());
			// Retrieve the 서비스 value
			ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_STRING, styleHVCenter, dayVisitors.getServiceCode());
			// Retrieve the 카테고리 value
			ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_STRING, styleHVCenter, dayVisitors.getCategoryNm());
			// Retrieve the 메뉴 value
			ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_STRING, styleHVCenter, dayVisitors.getMenuNm());
			// Retrieve the 방문자수 value
			ExcelUtil.createCell(row, 4, Cell.CELL_TYPE_NUMERIC, styleHVRight, dayVisitors.getVisitCount()
					.doubleValue());
			// 셀 병합처리
			if (StringUtils.equals(merge[0], dayVisitors.getVisitDate())) {
				lastRow++;
				if ((startRowIndex == adminMbrentStaSize + 3) && (startRow < lastRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
				}
			} else {
				if (startRow < lastRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
				}
				startRow = startRowIndex - 1;
				lastRow = startRowIndex - 1;
			}
			if (StringUtils.equals(merge[1], dayVisitors.getVisitDate() + dayVisitors.getServiceCode())) {
				lastSerRow++;
				if ((startRowIndex == adminMbrentStaSize + 3) && (startSerRow < lastSerRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startSerRow, lastSerRow, 1, 1));
				}
			} else {
				if (startSerRow < lastSerRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startSerRow, lastSerRow, 1, 1));
				}
				startSerRow = startRowIndex - 1;
				lastSerRow = startRowIndex - 1;
			}
			if (StringUtils.equals(merge[2], dayVisitors.getServiceCode() + dayVisitors.getCategoryNm())) {
				lastCateRow++;
				if ((startRowIndex == adminMbrentStaSize + 3) && (startCateRow < lastCateRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startCateRow, lastCateRow, 2, 2));
				}
			} else {
				if (startCateRow < lastCateRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startCateRow, lastCateRow, 2, 2));
				}
				startCateRow = startRowIndex - 1;
				lastCateRow = startRowIndex - 1;
			}
			merge[0] = dayVisitors.getVisitDate();
			merge[1] = dayVisitors.getVisitDate() + dayVisitors.getServiceCode();
			merge[2] = dayVisitors.getServiceCode() + dayVisitors.getCategoryNm();
		}
	}
}
