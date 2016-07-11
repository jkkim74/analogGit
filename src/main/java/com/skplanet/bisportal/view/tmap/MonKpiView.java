package com.skplanet.bisportal.view.tmap;

import com.skplanet.bisportal.common.excel.PoiHelper;
import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.tmap.TmapMonKpiStc;
import com.skplanet.bisportal.view.AbstractExcelView;
import org.apache.commons.collections.CollectionUtils;
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
 * The monKpiView class.
 *
 * @author cookatrice
 *
 */
public class MonKpiView extends AbstractExcelView {
	/**
	 * Tmap KPI > 월별 KPI 관리 엑셀 저장.
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
		String menuName = "월별 KPI 관리";
		Sheet worksheet = workbook.createSheet(menuName);
		String headers[][] = { { "기준일자", "KPI", "UV", "인당월평균사용일수" },
				{ "기준일자", "KPI", "Active UV", "T map", "T map 대중교통", "Active UV", "T map", "T map 대중교통" } };

		int headLength = headers[1].length;
		// 칼럼 넓이 조정.
		super.setColumnWith(worksheet, headLength);
		// 타이틀 입력.
		super.buildTitle(worksheet, menuName, headLength);
		// 헤드 입력.
		PoiHelper.buildHeaderForMonKpi(worksheet, headers);

		// 엑셀 바디 생성.
		List<TmapMonKpiStc> tmapMonKpiStcs = uncheckedCast(model.get("monKpiData"));
		if (CollectionUtils.isNotEmpty(tmapMonKpiStcs))
			this.setCellValue(tmapMonKpiStcs, workbook, worksheet);
	}

	private void setCellValue(List<TmapMonKpiStc> tmapMonKpiStcs, Workbook workbook, Sheet worksheet) throws Exception {
		// Row offset
		int startRowIndex = 4;
		// 엑셀 바디의 셀 스타일 정의(문자 가운데 정렬).
		CellStyle styleHVCenter = ExcelUtil.getCenterStyle(worksheet);
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마).
		CellStyle styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마, 소수2자리).
		CellStyle styleHVRightComma = ExcelUtil.getRightFormatStyle2(workbook, worksheet);

		String merge = StringUtils.EMPTY;
		int startRow = 3, lastRow = 3;
		int monKpiSize = tmapMonKpiStcs.size();
		for (TmapMonKpiStc item : tmapMonKpiStcs) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// Retrieve the 기준일자 value
			Cell cell1 = row.createCell(0);
			cell1.setCellType(Cell.CELL_TYPE_STRING);
			cell1.setCellValue(item.getStrdYm());
			cell1.setCellStyle(styleHVCenter);

			// Retrieve the KPI value
			Cell cell2 = row.createCell(1);
			cell2.setCellType(Cell.CELL_TYPE_STRING);
			cell2.setCellValue(item.getKpi());
			cell2.setCellStyle(styleHVRight);

			// Retrieve the UV - activeUv value
			Cell cell3 = row.createCell(2);
			cell3.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell3.setCellValue(item.getUvActiveUv().doubleValue());
			cell3.setCellStyle(styleHVRight);

			// Retrieve the UV - Tmap value
			Cell cell4 = row.createCell(3);
			cell4.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell4.setCellValue(item.getUvTmap().doubleValue());
			cell4.setCellStyle(styleHVRight);

			// Retrieve the UV - Tmap 대중교통 value
			Cell cell5 = row.createCell(4);
			cell5.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell5.setCellValue(item.getUvTmapPublic().doubleValue());
			cell5.setCellStyle(styleHVRight);

			// Retrieve the 인당월평균사용일수 - activeUv value
			Cell cell6 = row.createCell(5);
			cell6.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell6.setCellValue(item.getAvgActiveUv().doubleValue());
			cell6.setCellStyle(styleHVRightComma);

			// Retrieve the 인당월평균사용일수 - Tmap value
			Cell cell7 = row.createCell(6);
			cell7.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell7.setCellValue(item.getAvgTmap().doubleValue());
			cell7.setCellStyle(styleHVRightComma);

			// Retrieve the 인당월평균사용일수 - Tmap 대중교통 value
			Cell cell8 = row.createCell(7);
			cell8.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell8.setCellValue(item.getAvgTmapPublic().doubleValue());
			cell8.setCellStyle(styleHVRightComma);

			// 셀 병합처리
			if (StringUtils.equals(merge, item.getStrdYm())) {
				lastRow++;
				if ((startRowIndex == monKpiSize + 4) && (startRow < lastRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
				}
			} else {
				if (startRow < lastRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
				}
				startRow = startRowIndex - 1;
				lastRow = startRowIndex - 1;
			}

			merge = item.getStrdYm();
		}
	}
}
