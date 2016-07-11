package com.skplanet.bisportal.view.syrup;

import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.syrup.SmwCponLocStat;
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
 * Created by lko on 2014-11-28.
 */
public class CouponLocStatExcelView extends AbstractExcelView {
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String menuName = "쿠폰메뉴 위치별 실적";
		Sheet worksheet = workbook.createSheet(menuName);
		String headers[][] = { { "기간", "구분", "위치", "브랜드명", "쿠폰명(배너명)", "클릭수", "클릭고객수" } };

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
		List<SmwCponLocStat> swmDCponLocStatList = uncheckedCast(model.get("couponLocStatData"));

		String[] merge = new String[4];
		int startRow = 3, lastRow = 3, startCdNm3Row = 3, lastCdNm3Row = 3;
		int startTgtOrderRow = 3, lastTgtOrderRow = 3;
		int startBrandNmRow = 3, lastBrandNmRow = 3;
		int syrupMbrentStaSize = swmDCponLocStatList.size();
		for (SmwCponLocStat swmDCponLocStats : swmDCponLocStatList) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// Retrieve the 기간 value
			Cell cell1 = row.createCell(0);
			cell1.setCellValue(swmDCponLocStats.getDispDt());
			cell1.setCellStyle(styleHVCenter);

			// Retrieve the 구분 value
			Cell cell2 = row.createCell(1);
			cell2.setCellValue(swmDCponLocStats.getCdNm3());
			cell2.setCellStyle(styleHVCenter);

			// Retrieve the 위치 value
			Cell cell3 = row.createCell(2);
			cell3.setCellValue(swmDCponLocStats.getTgtOrder());
			cell3.setCellStyle(styleHVCenter);

			// Retrieve the 브랜드명
			Cell cell4 = row.createCell(3);
			cell4.setCellValue(swmDCponLocStats.getBrandNm());
			cell4.setCellStyle(styleHVCenter);

			// Retrieve the 쿠폰명(배너명) value
			Cell cell5 = row.createCell(4);
			cell5.setCellValue(swmDCponLocStats.getCpNm());
			cell5.setCellStyle(styleHVCenter);

			// Retrieve the 클릭수 value
			Cell cell6 = row.createCell(5);
			cell6.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell6.setCellValue(swmDCponLocStats.getPageCnt().longValue());
			cell6.setCellStyle(styleHVRight);

			// Retrieve the 클릭고객수 value
			Cell cell7 = row.createCell(6);
			cell7.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell7.setCellValue(swmDCponLocStats.getPageUserCnt().longValue());
			cell7.setCellStyle(styleHVRight);

			// 셀 병합처리
			if (StringUtils.equals(merge[0], swmDCponLocStats.getDispDt())) {
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
			if (StringUtils.equals(merge[1], swmDCponLocStats.getCdNm3())) {
				lastCdNm3Row++;
				if ((startRowIndex == syrupMbrentStaSize + 3) && (startCdNm3Row < lastCdNm3Row)) {
					worksheet.addMergedRegion(new CellRangeAddress(startCdNm3Row, lastCdNm3Row, 1, 1));
				}
			} else {
				if (startCdNm3Row < lastCdNm3Row) {
					worksheet.addMergedRegion(new CellRangeAddress(startCdNm3Row, lastCdNm3Row, 1, 1));
				}
				startCdNm3Row = startRowIndex - 1;
				lastCdNm3Row = startRowIndex - 1;
			}
			// 셀 병합처리
			if (StringUtils.equals(merge[2], swmDCponLocStats.getCdNm3() + swmDCponLocStats.getTgtOrder())) {
				lastTgtOrderRow++;
				if ((startRowIndex == syrupMbrentStaSize + 3) && (startTgtOrderRow < lastTgtOrderRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startTgtOrderRow, lastTgtOrderRow, 2, 2));
				}
			} else {
				if (startTgtOrderRow < lastTgtOrderRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startTgtOrderRow, lastTgtOrderRow, 2, 2));
				}
				startTgtOrderRow = startRowIndex - 1;
				lastTgtOrderRow = startRowIndex - 1;
			}
			// 셀 병합처리
			if (StringUtils.equals(merge[3], swmDCponLocStats.getTgtOrder() + swmDCponLocStats.getBrandNm())) {
				lastBrandNmRow++;
				if ((startRowIndex == syrupMbrentStaSize + 3) && (startBrandNmRow < lastBrandNmRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startBrandNmRow, lastBrandNmRow, 3, 3));
				}
			} else {
				if (startBrandNmRow < lastBrandNmRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startBrandNmRow, lastBrandNmRow, 3, 3));
				}
				startBrandNmRow = startRowIndex - 1;
				lastBrandNmRow = startRowIndex - 1;
			}
			merge[0] = swmDCponLocStats.getDispDt();
			merge[1] = swmDCponLocStats.getCdNm3();
			merge[2] = swmDCponLocStats.getCdNm3() + swmDCponLocStats.getTgtOrder();
			merge[3] = swmDCponLocStats.getTgtOrder() + swmDCponLocStats.getBrandNm();
		}
	}
}
