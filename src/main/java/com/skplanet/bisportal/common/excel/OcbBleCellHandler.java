package com.skplanet.bisportal.common.excel;

import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.ocb.BleNewTech;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * The OcbBleCellHandler(OCB BLE 동적 셀 생성 처리) class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
public class OcbBleCellHandler implements BleCellHandler {
	@Override
	public void handleBleCell(List<BleNewTech> bleNewTechs, Workbook workbook, Sheet worksheet, String statContents)
			throws Exception {
		// Row offset
		int startRowIndex = 5;
		// 엑셀 바디의 셀 스타일 정의(문자 가운데 정렬).
		CellStyle styleHVCenter = ExcelUtil.getCenterStyle(worksheet);
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마).
		CellStyle styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);
		if (StringUtils.indexOf(statContents, "1") > -1) {// 통계 항목 전체 선택
			for (BleNewTech bleNewTech : bleNewTechs) {
				// Create a new row
				Row row = worksheet.createRow(startRowIndex++);
				// Retrieve the 기준일자 value
				ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, bleNewTech.getStdDt());
				// Retrieve the 가맹점명 value
				ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_STRING, styleHVCenter, bleNewTech.getName1());
				// Retrieve the MID value
				ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_STRING, styleHVCenter, bleNewTech.getMid());
				// Retrieve the OCB/Syrup 서버요청 PV value
				ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbSrpPv()
						.doubleValue());
				// Retrieve the OCB/Syrup 서버요청 UV value
				ExcelUtil.createCell(row, 4, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbSrpUv()
						.doubleValue());
				// Retrieve the OCB 체크인 PV value
				ExcelUtil.createCell(row, 5, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbCoinPv()
						.doubleValue());
				// Retrieve the OCB 체크인 UV value
				ExcelUtil.createCell(row, 6, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbCoinUv()
						.doubleValue());
				// Retrieve the OCB 전단 PV value
				ExcelUtil.createCell(row, 7, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbLeafletPv()
						.doubleValue());
				// Retrieve the OCB 전단 UV value
				ExcelUtil.createCell(row, 8, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbLeafletUv()
						.doubleValue());
				// Retrieve the OCB 전단 PV value
				ExcelUtil.createCell(row, 9, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbMlfClkPv()
						.doubleValue());
				// Retrieve the OCB 전단 UV value
				ExcelUtil.createCell(row, 10, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbMlfClkUv()
						.doubleValue());

				// // Retrieve the OCB 체크인 PV value
				// ExcelUtil.createCell(row, 9, Cell.CELL_TYPE_NUMERIC, styleHVRight,
				// bleNewTech.getOcbChkinClkPv().doubleValue());
				// // Retrieve the OCB 체크인 UV value
				// ExcelUtil.createCell(row, 10, Cell.CELL_TYPE_NUMERIC, styleHVRight,
				// bleNewTech.getOcbChkinClkUv().doubleValue());

				// Retrieve the OCB 전단 PV value
				ExcelUtil.createCell(row, 11, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbMlfReadPv()
						.doubleValue());
				// Retrieve the OCB 전단 UV value
				ExcelUtil.createCell(row, 12, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbMlfReadUv()
						.doubleValue());
				// Retrieve the OCB 체크인 PV value
				ExcelUtil.createCell(row, 13, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbChkinReadPv()
						.doubleValue());
				// Retrieve the OCB 체크인 UV value
				ExcelUtil.createCell(row, 14, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbChkinReadUv()
						.doubleValue());
			}
		} else {
			if (StringUtils.indexOf(statContents, "3") > -1) {
				for (BleNewTech bleNewTech : bleNewTechs) {
					// Create a new row
					Row row = worksheet.createRow(startRowIndex++);
					// Retrieve the 기준일자 value
					ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, bleNewTech.getStdDt());
					// Retrieve the 가맹점명 value
					ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_STRING, styleHVCenter, bleNewTech.getName1());
					// Retrieve the MID value
					ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_STRING, styleHVCenter, bleNewTech.getMid());
					// Retrieve the OCB/Syrup 서버요청 PV value
					ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbSrpPv()
							.doubleValue());
					// Retrieve the OCB/Syrup 서버요청 UV value
					ExcelUtil.createCell(row, 4, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbSrpUv()
							.doubleValue());
					// Retrieve the OCB 동전 PV value
					ExcelUtil.createCell(row, 5, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbCoinPv()
							.doubleValue());
					// Retrieve the OCB 동전 UV value
					ExcelUtil.createCell(row, 6, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbCoinUv()
							.doubleValue());
					// Retrieve the OCB 전단 PV value
					ExcelUtil.createCell(row, 7, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbLeafletPv()
							.doubleValue());
					// Retrieve the OCB 전단 UV value
					ExcelUtil.createCell(row, 8, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbLeafletUv()
							.doubleValue());

					if (StringUtils.indexOf(statContents, "4") > -1) {
						// Retrieve the OCB 전단 PV value
						ExcelUtil.createCell(row, 9, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbMlfClkPv()
								.doubleValue());
						// Retrieve the OCB 전단 UV value
						ExcelUtil.createCell(row, 10, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbMlfClkUv()
								.doubleValue());

						// // Retrieve the OCB 체크인 PV value
						// ExcelUtil.createCell(row, 9, Cell.CELL_TYPE_NUMERIC, styleHVRight,
						// bleNewTech.getOcbChkinClkPv().doubleValue());

						// // Retrieve the OCB 체크인 UV value
						// ExcelUtil.createCell(row, 10, Cell.CELL_TYPE_NUMERIC, styleHVRight,
						// bleNewTech.getOcbChkinClkUv().doubleValue());

						if (StringUtils.indexOf(statContents, "5") > -1) {
							// Retrieve the OCB 전단 PV value
							ExcelUtil.createCell(row, 11, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech
									.getOcbMlfReadPv().doubleValue());
							// Retrieve the OCB 전단 UV value
							ExcelUtil.createCell(row, 12, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech
									.getOcbMlfReadUv().doubleValue());
							// Retrieve the OCB 체크인 PV value
							ExcelUtil.createCell(row, 13, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech
									.getOcbChkinReadPv().doubleValue());
							// Retrieve the OCB 체크인 UV value
							ExcelUtil.createCell(row, 14, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech
									.getOcbChkinReadUv().doubleValue());
						}
					} else {
						if (StringUtils.indexOf(statContents, "5") > -1) {
							// Retrieve the OCB 전단 PV value
							ExcelUtil.createCell(row, 9, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech
									.getOcbMlfReadPv().doubleValue());
							// Retrieve the OCB 전단 UV value
							ExcelUtil.createCell(row, 10, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech
									.getOcbMlfReadUv().doubleValue());
							// Retrieve the OCB 체크인 PV value
							ExcelUtil.createCell(row, 11, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech
									.getOcbChkinReadPv().doubleValue());
							// Retrieve the OCB 체크인 UV value
							ExcelUtil.createCell(row, 12, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech
									.getOcbChkinReadUv().doubleValue());
						}
					}
				}
			} else if (StringUtils.indexOf(statContents, "4") > -1) {
				for (BleNewTech bleNewTech : bleNewTechs) {
					// Create a new row
					Row row = worksheet.createRow(startRowIndex++);
					// Retrieve the 기준일자 value
					ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, bleNewTech.getStdDt());
					// Retrieve the 가맹점명 value
					ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_STRING, styleHVCenter, bleNewTech.getName1());
					// Retrieve the MID value
					ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_STRING, styleHVCenter, bleNewTech.getMid());
					// Retrieve the OCB/Syrup 서버요청 PV value
					ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbSrpPv()
							.doubleValue());
					// Retrieve the OCB/Syrup 서버요청 UV value
					ExcelUtil.createCell(row, 4, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbSrpUv()
							.doubleValue());
					// Retrieve the OCB 전단 PV value
					ExcelUtil.createCell(row, 5, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbMlfClkPv()
							.doubleValue());
					// Retrieve the OCB 전단 UV value
					ExcelUtil.createCell(row, 6, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbMlfClkUv()
							.doubleValue());

					// // Retrieve the OCB 체크인 PV value
					// ExcelUtil.createCell(row, 7, Cell.CELL_TYPE_NUMERIC, styleHVRight,
					// bleNewTech.getOcbChkinClkPv().doubleValue());

					// // Retrieve the OCB 체크인 UV value
					// ExcelUtil.createCell(row, 8, Cell.CELL_TYPE_NUMERIC, styleHVRight,
					// bleNewTech.getOcbChkinClkUv().doubleValue());

					if (StringUtils.indexOf(statContents, "5") > -1) {
						// Retrieve the OCB 전단 PV value
						ExcelUtil.createCell(row, 7, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbMlfReadPv()
								.doubleValue());
						// Retrieve the OCB 전단 UV value
						ExcelUtil.createCell(row, 8, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbMlfReadUv()
								.doubleValue());
						// Retrieve the OCB 체크인 PV value
						ExcelUtil.createCell(row, 9, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech
								.getOcbChkinReadPv().doubleValue());
						// Retrieve the OCB 체크인 UV value
						ExcelUtil.createCell(row, 10, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech
								.getOcbChkinReadUv().doubleValue());
					}
				}
			} else if (StringUtils.indexOf(statContents, "5") > -1) {
				for (BleNewTech bleNewTech : bleNewTechs) {
					// Create a new row
					Row row = worksheet.createRow(startRowIndex++);
					// Retrieve the 기준일자 value
					ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, bleNewTech.getStdDt());
					// Retrieve the 가맹점명 value
					ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_STRING, styleHVCenter, bleNewTech.getName1());
					// Retrieve the MID value
					ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_STRING, styleHVCenter, bleNewTech.getMid());
					// Retrieve the OCB/Syrup 서버요청 PV value
					ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbSrpPv()
							.doubleValue());
					// Retrieve the OCB/Syrup 서버요청 UV value
					ExcelUtil.createCell(row, 4, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbSrpUv()
							.doubleValue());
					// Retrieve the OCB 전단 PV value
					ExcelUtil.createCell(row, 5, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbMlfReadPv()
							.doubleValue());
					// Retrieve the OCB 전단 UV value
					ExcelUtil.createCell(row, 6, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbMlfReadUv()
							.doubleValue());
					// Retrieve the OCB 체크인 PV value
					ExcelUtil.createCell(row, 7, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbChkinReadPv()
							.doubleValue());
					// Retrieve the OCB 체크인 UV value
					ExcelUtil.createCell(row, 8, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbChkinReadUv()
							.doubleValue());
				}
			} else {
				for (BleNewTech bleNewTech : bleNewTechs) {
					// Create a new row
					Row row = worksheet.createRow(startRowIndex++);
					// Retrieve the 기준일자 value
					ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, bleNewTech.getStdDt());
					// Retrieve the 가맹점명 value
					ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_STRING, styleHVCenter, bleNewTech.getName1());
					// Retrieve the MID value
					ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_STRING, styleHVCenter, bleNewTech.getMid());
					// Retrieve the OCB/Syrup 서버요청 PV value
					ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbSrpPv()
							.doubleValue());
					// Retrieve the OCB/Syrup 서버요청 UV value
					ExcelUtil.createCell(row, 4, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getOcbSrpUv()
							.doubleValue());
				}
			}
		}
	}
}
