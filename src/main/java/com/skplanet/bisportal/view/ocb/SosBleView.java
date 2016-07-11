package com.skplanet.bisportal.view.ocb;

import com.skplanet.bisportal.common.excel.PoiHelper;
import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.ocb.ObsSosBleSta;
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
 * The SosBleView class.
 *
 * @author cookatrice
 *
 */
public class SosBleView extends AbstractExcelView {
	/**
	 * sosBle 엑셀 저장.
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
		String menuName = "sos BLE 고객모수";
		Sheet worksheet = workbook.createSheet(menuName);
		String headers[][] = {
				{ "기준일자", "구분", "Syrup 모수", "OCB 모수", "Syrup ∩ OCB 모수" },
				{ "기준일자", "구분",
						"BLE 모수", "BT on", "BLE 진단", "Smart Beacon",
						"BLE 전단", "BLE 체크인", "BT on","Smart Beacon", "매장전단", "상권전단",
						"(S)BLE ∩ (O)BLE 전단+체크인", "(S)BLE ∩ (O)BLE 전단","(S)BLE ∩ (O)BLE 체크인", "(S)BT on ∩ (O) BT on" } };

		int headLength = headers[1].length;
		// 칼럼 넓이 조정.
		super.setColumnWith(worksheet, headLength);
		// 타이틀 입력.
		super.buildTitle(worksheet, menuName, headLength);
		// 헤드 입력.
		PoiHelper.buildHeaderForSosBle(worksheet, headers);
		// 엑셀 바디 생성.
		List<ObsSosBleSta> obsSosBleStas = uncheckedCast(model.get("sosBleData"));
		if (CollectionUtils.isNotEmpty(obsSosBleStas))
			this.setCellValue(obsSosBleStas, workbook, worksheet);
	}

	private void setCellValue(List<ObsSosBleSta> obsSosBleStas, Workbook workbook, Sheet worksheet) throws Exception {
		// Row offset
		int startRowIndex = 4;
		// 엑셀 바디의 셀 스타일 정의(문자 가운데 정렬).
		CellStyle styleHVCenter = ExcelUtil.getCenterStyle(worksheet);
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마).
		CellStyle styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);

		String merge = StringUtils.EMPTY;
		int startRow = 3, lastRow = 3;
		int obsSosBlestaSize = obsSosBleStas.size();
		for (ObsSosBleSta item : obsSosBleStas) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// Retrieve the 기준일자 value
			Cell cell1 = row.createCell(0);
			cell1.setCellType(Cell.CELL_TYPE_STRING);
			cell1.setCellValue(item.getStdDt());
			cell1.setCellStyle(styleHVCenter);

			// Retrieve the 구분 value
			Cell cell2 = row.createCell(1);
			cell2.setCellType(Cell.CELL_TYPE_STRING);
			cell2.setCellValue(item.getComCdNm());
			cell2.setCellStyle(styleHVRight);

			// Retrieve the BLE모수 value
			Cell cell3 = row.createCell(2);
			cell3.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell3.setCellValue(item.getSyrupBleCnt().doubleValue());
			cell3.setCellStyle(styleHVRight);

			// Retrieve the BTon value
			Cell cell4 = row.createCell(3);
			cell4.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell4.setCellValue(item.getSyrupBtOnCnt().doubleValue());
			cell4.setCellStyle(styleHVRight);

			// Retrieve the BLE진단 value
			Cell cell5 = row.createCell(4);
			cell5.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell5.setCellValue(item.getSyrupBleFlyrCnt().doubleValue());
			cell5.setCellStyle(styleHVRight);

			// Retrieve the SmartBeacon value
			Cell cell6 = row.createCell(5);
			cell6.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell6.setCellValue(item.getSyrupSmtBcnCnt().doubleValue());
			cell6.setCellStyle(styleHVRight);

			// Retrieve the BLE전단 value
			Cell cell7 = row.createCell(6);
			cell7.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell7.setCellValue(item.getOcbBleFlyrCnt().doubleValue());
			cell7.setCellStyle(styleHVRight);

			// Retrieve the BLE체크인 value
			Cell cell8 = row.createCell(7);
			cell8.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell8.setCellValue(item.getOcbBleChkinCnt().doubleValue());
			cell8.setCellStyle(styleHVRight);

			// Retrieve the BTon value
			Cell cell9 = row.createCell(8);
			cell9.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell9.setCellValue(item.getOcbBtOnCnt().doubleValue());
			cell9.setCellStyle(styleHVRight);

			// Retrieve the SmartBeacon value
			Cell cell10 = row.createCell(9);
			cell10.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell10.setCellValue(item.getOcbSmtBcnCnt().doubleValue());
			cell10.setCellStyle(styleHVRight);

			// Retrieve the 매장전단 value
			Cell cell11 = row.createCell(10);
			cell11.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell11.setCellValue(item.getOcbStorFlyrCnt().doubleValue());
			cell11.setCellStyle(styleHVRight);

			// Retrieve the 상권전단 value
			Cell cell12 = row.createCell(11);
			cell12.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell12.setCellValue(item.getOcbTraraFlyrCnt().doubleValue());
			cell12.setCellStyle(styleHVRight);

			// Retrieve the (S)BLE ∩ (O)BLE 전단+체크인 value
			Cell cell13 = row.createCell(12);
			cell13.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell13.setCellValue(item.getBleFlyrChkinCnt().doubleValue());
			cell13.setCellStyle(styleHVRight);

			// Retrieve the (S)BLE ∩ (O)BLE 전단 value
			Cell cell14 = row.createCell(13);
			cell14.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell14.setCellValue(item.getBleFlyrCnt().doubleValue());
			cell14.setCellStyle(styleHVRight);

			// Retrieve the (S)BLE ∩ (O)BLE 체크인 value
			Cell cell15 = row.createCell(14);
			cell15.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell15.setCellValue(item.getBleChkinCnt().doubleValue());
			cell15.setCellStyle(styleHVRight);

			// Retrieve the (S)BT on ∩ (O) BT on value
			Cell cell16 = row.createCell(15);
			cell16.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell16.setCellValue(item.getBleBtonCnt().doubleValue());
			cell16.setCellStyle(styleHVRight);

			// 셀 병합처리
			if (StringUtils.equals(merge, item.getStdDt())) {
				lastRow++;
				if ((startRowIndex == obsSosBlestaSize + 4) && (startRow < lastRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
				}
			} else {
				if (startRow < lastRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
				}
				startRow = startRowIndex - 1;
				lastRow = startRowIndex - 1;
			}
			merge = item.getStdDt();
		}
	}
}
