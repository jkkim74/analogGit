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
 * The SyrupBleCellHandler(Syrup BLE 동적 셀 처리) class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
public class SyrupBleCellHandler implements BleCellHandler {
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
				// Retrieve the Syrup Push 팝업 PV value
				ExcelUtil.createCell(row, 5, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpPushPopPv()
						.doubleValue());
				// Retrieve the Syrup Push 팝업 UV value
				ExcelUtil.createCell(row, 6, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpPushPopUv()
						.doubleValue());
				// Retrieve the Syrup Push Noti PV value
				ExcelUtil.createCell(row, 7, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpPushNotiPv()
						.doubleValue());
				// Retrieve the Syrup Push Noti UV value
				ExcelUtil.createCell(row, 8, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpPushNotiUv()
						.doubleValue());
				// Retrieve the Syrup 여기(버튼) PV value
				ExcelUtil.createCell(row, 9, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpIconBtnPv()
						.doubleValue());
				// Retrieve the Syrup 여기(버튼) UV value
				ExcelUtil.createCell(row, 10, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpIconBtnUv()
						.doubleValue());
				// Retrieve the Syrup 확인 PV value
				ExcelUtil.createCell(row, 11, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpOkClkPv()
						.doubleValue());
				// Retrieve the Syrup 확인 UV value
				ExcelUtil.createCell(row, 12, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpOkClkUv()
						.doubleValue());
				// Retrieve the Syrup 취소 PV value
				ExcelUtil.createCell(row, 13, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpCnclClkPv()
						.doubleValue());
				// Retrieve the Syrup 취소 UV value
				ExcelUtil.createCell(row, 14, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpCnclClkUv()
						.doubleValue());
				// Retrieve the Noti 조회 PV value
				ExcelUtil.createCell(row, 15, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpIconBtnClkPv()
						.doubleValue());
				// Retrieve the Noti 조회 UV value
				ExcelUtil.createCell(row, 16, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpIconBtnClkUv()
						.doubleValue());
				// Retrieve the Syrup 여기(버튼) PV value
				ExcelUtil.createCell(row, 17, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpIconPv()
						.doubleValue());
				// Retrieve the Syrup 여기(버튼) UV value
				ExcelUtil.createCell(row, 18, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpIconUv()
						.doubleValue());
				// Retrieve the Syrup 체크인/URL 확인 PV value
				ExcelUtil.createCell(row, 19, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpOkReadPv()
						.doubleValue());
				// Retrieve the Syrup 체크인/URL 확인 UV value
				ExcelUtil.createCell(row, 20, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpOkReadUv()
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
					// Retrieve the Syrup Push 팝업 PV value
					ExcelUtil.createCell(row, 5, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpPushPopPv()
							.doubleValue());
					// Retrieve the Syrup Push 팝업 UV value
					ExcelUtil.createCell(row, 6, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpPushPopUv()
							.doubleValue());
					// Retrieve the Syrup Push Noti PV value
					ExcelUtil.createCell(row, 7, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpPushNotiPv()
							.doubleValue());
					// Retrieve the Syrup Push Noti UV value
					ExcelUtil.createCell(row, 8, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpPushNotiUv()
							.doubleValue());
					// Retrieve the Syrup 여기(버튼) PV value
					ExcelUtil.createCell(row, 9, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpIconBtnPv()
							.doubleValue());
					// Retrieve the Syrup 여기(버튼) UV value
					ExcelUtil.createCell(row, 10, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpIconBtnUv()
							.doubleValue());

					if (StringUtils.indexOf(statContents, "4") > -1) {
						// Retrieve the Syrup 확인 PV value
						ExcelUtil.createCell(row, 11, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpOkClkPv()
								.doubleValue());
						// Retrieve the Syrup 확인 UV value
						ExcelUtil.createCell(row, 12, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpOkClkUv()
								.doubleValue());
						// Retrieve the Syrup 취소 PV value
						ExcelUtil.createCell(row, 13, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech
								.getSrpCnclClkPv().doubleValue());
						// Retrieve the Syrup 취소 UV value
						ExcelUtil.createCell(row, 14, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech
								.getSrpCnclClkUv().doubleValue());
						// Retrieve the Noti 조회 PV value
						ExcelUtil.createCell(row, 15, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech
								.getSrpIconBtnClkPv().doubleValue());
						// Retrieve the Noti 조회 UV value
						ExcelUtil.createCell(row, 16, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech
								.getSrpIconBtnClkUv().doubleValue());
						// Retrieve the Syrup 여기(버튼) PV value
						ExcelUtil.createCell(row, 17, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpIconPv()
								.doubleValue());
						// Retrieve the Syrup 여기(버튼) UV value
						ExcelUtil.createCell(row, 18, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpIconUv()
								.doubleValue());

						if (StringUtils.indexOf(statContents, "5") > -1) {
							// Retrieve the Syrup 체크인/URL 확인 PV value
							ExcelUtil.createCell(row, 19, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech
									.getSrpOkReadPv().doubleValue());
							// Retrieve the Syrup 체크인/URL 확인 UV value
							ExcelUtil.createCell(row, 20, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech
									.getSrpOkReadUv().doubleValue());
						}
					} else {
						if (StringUtils.indexOf(statContents, "5") > -1) {
							// Retrieve the Syrup 체크인/URL 확인 PV value
							ExcelUtil.createCell(row, 11, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech
									.getSrpOkReadPv().doubleValue());
							// Retrieve the Syrup 체크인/URL 확인 UV value
							ExcelUtil.createCell(row, 12, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech
									.getSrpOkReadUv().doubleValue());
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
					// Retrieve the Syrup 확인 PV value
					ExcelUtil.createCell(row, 5, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpOkClkPv()
							.doubleValue());
					// Retrieve the Syrup 확인 UV value
					ExcelUtil.createCell(row, 6, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpOkClkUv()
							.doubleValue());
					// Retrieve the Syrup 취소 PV value
					ExcelUtil.createCell(row, 7, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpCnclClkPv()
							.doubleValue());
					// Retrieve the Syrup 취소 UV value
					ExcelUtil.createCell(row, 8, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpCnclClkUv()
							.doubleValue());
					// Retrieve the Noti 조회 PV value
					ExcelUtil.createCell(row, 9, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpIconBtnClkPv()
							.doubleValue());
					// Retrieve the Noti 조회 UV value
					ExcelUtil.createCell(row, 10, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpIconBtnClkUv()
							.doubleValue());
					// Retrieve the Syrup 여기(버튼) PV value
					ExcelUtil.createCell(row, 11, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpIconPv()
							.doubleValue());
					// Retrieve the Syrup 여기(버튼) UV value
					ExcelUtil.createCell(row, 12, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpIconUv()
							.doubleValue());

					if (StringUtils.indexOf(statContents, "5") > -1) {
						// Retrieve the Syrup 체크인/URL 확인 PV value
						ExcelUtil.createCell(row, 13, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpOkReadPv()
								.doubleValue());
						// Retrieve the Syrup 체크인/URL 확인 UV value
						ExcelUtil.createCell(row, 14, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpOkReadUv()
								.doubleValue());
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
					// Retrieve the Syrup 체크인/URL 확인 PV value
					ExcelUtil.createCell(row, 5, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpOkReadPv()
							.doubleValue());
					// Retrieve the Syrup 체크인/URL 확인 UV value
					ExcelUtil.createCell(row, 6, Cell.CELL_TYPE_NUMERIC, styleHVRight, bleNewTech.getSrpOkReadUv()
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
