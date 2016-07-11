package com.skplanet.bisportal.common.excel;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jsoup.nodes.Element;

import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.common.utils.NumberUtil;

/**
 * Created by pepsi on 2014. 8. 14..
 */
@Slf4j
public class PoiHelper {
	public static void firstMerge(Row row, Element children, int[][] mergeRow, String[] mergeValue,
			String[][] cellValue, int seq, int i, CellStyle styleHVCenter, CellStyle styleHVRight) throws Exception {
		if (seq == 0) {
			mergeRow[0][i] = Integer.parseInt(StringUtils.defaultIfEmpty(children.attr("rowspan"), "0"));
			mergeValue[0] = children.ownText();
		} else if (seq == 1) {
			mergeRow[1][i] = Integer.parseInt(StringUtils.defaultIfEmpty(children.attr("rowspan"), "0"));
			mergeValue[1] = children.ownText();
		} else if (seq == 2) {
			mergeRow[2][i] = Integer.parseInt(StringUtils.defaultIfEmpty(children.attr("rowspan"), "0"));
			mergeValue[2] = children.ownText();
		} else if (seq == 3) {
			mergeRow[3][i] = Integer.parseInt(StringUtils.defaultIfEmpty(children.attr("rowspan"), "0"));
			mergeValue[3] = children.ownText();
		} else if (seq == 4) {
			mergeRow[4][i] = Integer.parseInt(StringUtils.defaultIfEmpty(children.attr("rowspan"), "0"));
			mergeValue[4] = children.ownText();
		}
		Cell cell = row.createCell(seq);
		//if (seq == 1 && cellValue != null && StringUtils.equals(cellValue[i][0], cellValue[i][1])) {
		//	cell.setCellValue("");
		//	cell.setCellStyle(styleHVCenter);
		//} else {
		if (StringUtils.equals(children.tagName(), "td")) {
			if (StringUtils.isEmpty(children.ownText())) {
				cell.setCellValue(0);
			} else {
				String childrenValue = StringUtils.replace(children.ownText(), ",", StringUtils.EMPTY);
				if (NumberUtil.isNumber(childrenValue)) {
					if (NumberUtil.isDigit(childrenValue)) {
						cell.setCellValue(Long.parseLong(childrenValue));
					} else {
						cell.setCellValue(Double.parseDouble(childrenValue));
					}
				} else {
					cell.setCellValue(children.ownText());
				}
			}
			cell.setCellStyle(styleHVRight);
		} else {
			cell.setCellValue(children.ownText());
			cell.setCellStyle(styleHVCenter);
		}
		//}
	}

	public static void secondMerge(Row row, Element children, int[][] mergeRow, String[] mergeValue, int seq, int i,
			CellStyle styleHVCenter, CellStyle styleHVRight) throws Exception {
		if (seq == 0) {
			mergeRow[1][i] = Integer.parseInt(StringUtils.defaultIfEmpty(children.attr("rowspan"), "0"));
			mergeValue[1] = children.ownText();
		} else if (seq == 1) {
			mergeRow[2][i] = Integer.parseInt(StringUtils.defaultIfEmpty(children.attr("rowspan"), "0"));
			mergeValue[2] = children.ownText();
		} else if (seq == 2) {
			mergeRow[3][i] = Integer.parseInt(StringUtils.defaultIfEmpty(children.attr("rowspan"), "0"));
			mergeValue[3] = children.ownText();
		} else if (seq == 3) {
			mergeRow[4][i] = Integer.parseInt(StringUtils.defaultIfEmpty(children.attr("rowspan"), "0"));
			mergeValue[4] = children.ownText();
		}
		if (seq == 0) {
			ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, mergeValue[0]);
			ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_STRING, styleHVCenter, children.ownText());
		} else {
			Cell cell2 = row.createCell(seq + 1);
			if (StringUtils.equals(children.tagName(), "td")) {
				if (StringUtils.isEmpty(children.ownText())) {
					cell2.setCellValue(0);
				} else {
					String childrenValue = StringUtils.replace(children.ownText(), ",", StringUtils.EMPTY);
					if (NumberUtil.isNumber(childrenValue)) {
						if (NumberUtil.isDigit(childrenValue)) {
							cell2.setCellValue(Long.parseLong(childrenValue));
						} else {
							cell2.setCellValue(Double.parseDouble(childrenValue));
						}
					} else {
						cell2.setCellValue(children.ownText());
					}
				}
				cell2.setCellStyle(styleHVRight);
			} else {
				cell2.setCellValue(children.ownText());
				cell2.setCellStyle(styleHVCenter);
			}
		}
	}

	public static void thirdMerge(Row row, Element children, int[][] mergeRow, String[] mergeValue, int seq, int i,
			CellStyle styleHVCenter, CellStyle styleHVRight) throws Exception {
		if (seq == 0) {
			mergeRow[2][i] = Integer.parseInt(StringUtils.defaultIfEmpty(children.attr("rowspan"), "0"));
			mergeValue[2] = children.ownText();
		} else if (seq == 1) {
			mergeRow[3][i] = Integer.parseInt(StringUtils.defaultIfEmpty(children.attr("rowspan"), "0"));
			mergeValue[3] = children.ownText();
		} else if (seq == 2) {
			mergeRow[4][i] = Integer.parseInt(StringUtils.defaultIfEmpty(children.attr("rowspan"), "0"));
			mergeValue[4] = children.ownText();
		}

		if (seq == 0) {
			ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, mergeValue[0]);
			ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_STRING, styleHVCenter, mergeValue[1]);
			ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_STRING, styleHVCenter, children.ownText());
		} else {
			Cell cell3 = row.createCell(seq + 2);
			if (StringUtils.equals(children.tagName(), "td")) {
				if (StringUtils.isEmpty(children.ownText())) {
					cell3.setCellValue(0);
				} else {
					String childrenValue = StringUtils.replace(children.ownText(), ",", StringUtils.EMPTY);
					if (NumberUtil.isNumber(childrenValue)) {
						if (NumberUtil.isDigit(childrenValue)) {
							cell3.setCellValue(Long.parseLong(childrenValue));
						} else {
							cell3.setCellValue(Double.parseDouble(childrenValue));
						}
					} else {
						cell3.setCellValue(children.ownText());
					}
				}
				cell3.setCellStyle(styleHVRight);
			} else {
				cell3.setCellValue(children.ownText());
				cell3.setCellStyle(styleHVCenter);
			}
		}
	}

	public static void fourthMerge(Row row, Element children, int[][] mergeRow, String[] mergeValue, int seq, int i,
			CellStyle styleHVCenter, CellStyle styleHVRight) throws Exception {
		if (seq == 0) {
			mergeRow[3][i] = Integer.parseInt(StringUtils.defaultIfEmpty(children.attr("rowspan"), "0"));
			mergeValue[3] = children.ownText();
		} else if (seq == 1) {
			mergeRow[4][i] = Integer.parseInt(StringUtils.defaultIfEmpty(children.attr("rowspan"), "0"));
			mergeValue[4] = children.ownText();
		}
		if (seq == 0) {
			ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, mergeValue[0]);
			ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_STRING, styleHVCenter, mergeValue[1]);
			ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_STRING, styleHVCenter, mergeValue[2]);
			ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_STRING, styleHVCenter, children.ownText());
		} else {
			Cell cell4 = row.createCell(seq + 3);
			if (StringUtils.equals(children.tagName(), "td")) {
				if (StringUtils.isEmpty(children.ownText())) {
					cell4.setCellValue(0);
				} else {
					String childrenValue = StringUtils.replace(children.ownText(), ",", StringUtils.EMPTY);
					if (NumberUtil.isNumber(childrenValue)) {
						if (NumberUtil.isDigit(childrenValue)) {
							cell4.setCellValue(Long.parseLong(childrenValue));
						} else {
							cell4.setCellValue(Double.parseDouble(childrenValue));
						}
					} else {
						cell4.setCellValue(children.ownText());
					}
				}
				cell4.setCellStyle(styleHVRight);
			} else {
				cell4.setCellValue(children.ownText());
				cell4.setCellStyle(styleHVCenter);
			}
		}
	}

	public static void fifthMerge(Row row, Element children, int[][] mergeRow, String[] mergeValue, int seq, int i,
			CellStyle styleHVCenter, CellStyle styleHVRight) throws Exception {
		if (seq == 0) {
			mergeRow[4][i] = Integer.parseInt(StringUtils.defaultIfEmpty(children.attr("rowspan"), "0"));
			mergeValue[4] = children.ownText();
		}
		if (seq == 0) {
			ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, mergeValue[0]);
			ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_STRING, styleHVCenter, mergeValue[1]);
			ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_STRING, styleHVCenter, mergeValue[2]);
			ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_STRING, styleHVCenter, mergeValue[3]);
			ExcelUtil.createCell(row, 4, Cell.CELL_TYPE_STRING, styleHVCenter, children.ownText());
		} else {
			Cell cell5 = row.createCell(seq + 4);
			if (StringUtils.equals(children.tagName(), "td")) {
				if (StringUtils.isEmpty(children.ownText())) {
					cell5.setCellValue(0);
				} else {
					String childrenValue = StringUtils.replace(children.ownText(), ",", StringUtils.EMPTY);
					if (NumberUtil.isNumber(childrenValue)) {
						if (NumberUtil.isDigit(childrenValue)) {
							cell5.setCellValue(Long.parseLong(childrenValue));
						} else {
							cell5.setCellValue(Double.parseDouble(childrenValue));
						}
					} else {
						cell5.setCellValue(children.ownText());
					}
				}
				cell5.setCellStyle(styleHVRight);
			} else {
				cell5.setCellValue(children.ownText());
				cell5.setCellStyle(styleHVCenter);
			}
		}
	}

	public static void sixthMerge(Row row, Element children, String[] mergeValue, int seq, int i,
			CellStyle styleHVCenter, CellStyle styleHVRight) throws Exception {
		if (seq == 0) {
			ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, mergeValue[0]);
			ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_STRING, styleHVCenter, mergeValue[1]);
			ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_STRING, styleHVCenter, mergeValue[2]);
			ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_STRING, styleHVCenter, mergeValue[3]);
			ExcelUtil.createCell(row, 4, Cell.CELL_TYPE_STRING, styleHVCenter, mergeValue[4]);
			ExcelUtil.createCell(row, 5, Cell.CELL_TYPE_STRING, styleHVCenter, children.ownText());
		} else {
			Cell cell6 = row.createCell(seq + 5);
			if (StringUtils.equals(children.tagName(), "td")) {
				if (StringUtils.isEmpty(children.ownText())) {
					cell6.setCellValue(0);
				} else {
					String childrenValue = StringUtils.replace(children.ownText(), ",", StringUtils.EMPTY);
					if (NumberUtil.isNumber(childrenValue)) {
						if (NumberUtil.isDigit(childrenValue)) {
							cell6.setCellValue(Long.parseLong(childrenValue));
						} else {
							cell6.setCellValue(Double.parseDouble(childrenValue));
						}
					} else {
						cell6.setCellValue(children.ownText());
					}
				}
				cell6.setCellStyle(styleHVRight);
			} else {
				cell6.setCellValue(children.ownText());
				cell6.setCellStyle(styleHVCenter);
			}
		}
	}

	public static void mergeRowBody(Sheet worksheet, int[][] mergeRow, String[][] cellValue, int dataNumRows,
			int startRow, int initValue) throws Exception {
		int start1Row = startRow, last1Row, start2Row = startRow, last2Row, start3Row = startRow, last3Row, start4Row = startRow, last4Row, start5Row = startRow, last5Row, startCell = 0;
		boolean mergeFlag = false;
		for (int k = initValue; k < dataNumRows + initValue; k++) {
			if (mergeRow[0][k] > 1) {// 첫번째 칼럼 병합 처리.
				last1Row = start1Row + mergeRow[0][k] - 1;
				if (cellValue != null && StringUtils.equals(cellValue[k][0], cellValue[k][1])) {
					if (mergeRow[0][k] > 5) {
						startCell = 0;
						mergeFlag = false;
					} else {
						startCell = 1;
						mergeFlag = true;
					}
				} else {
					startCell = 0;
					mergeFlag = false;
				}
				worksheet.addMergedRegion(new CellRangeAddress(start1Row, last1Row, 0, startCell));
				start1Row = last1Row + 1;
			} else if (mergeRow[0][k] == 1) {
				if (cellValue != null && StringUtils.equals(cellValue[k][0], cellValue[k][1])) {
					worksheet.addMergedRegion(new CellRangeAddress(start1Row, start1Row, 0, 1));
				}
				start1Row += 1;
			}

			if (mergeRow[1][k] > 1) {// 두번째 칼럼 병합 처리.
				last2Row = (start2Row + mergeRow[1][k]) - 1;
				if (!mergeFlag)
					worksheet.addMergedRegion(new CellRangeAddress(start2Row, last2Row, 1, 1));
				start2Row = last2Row + 1;
			} else if (mergeRow[1][k] == 1) {
				start2Row += 1;
			}

			if (mergeRow.length > 2 && mergeRow[2][k] > 1) { // 세번째 칼럼 병합 처리.
				last3Row = (start3Row + mergeRow[2][k]) - 1;
				worksheet.addMergedRegion(new CellRangeAddress(start3Row, last3Row, 2, 2));
				start3Row = last3Row + 1;
			} else if (mergeRow.length > 2 && mergeRow[2][k] == 1) {
				start3Row += 1;
			}
			if (mergeRow.length > 3 && mergeRow[3][k] > 1) { // 네번째 칼럼 병합 처리.
				last4Row = (start4Row + mergeRow[3][k]) - 1;
				worksheet.addMergedRegion(new CellRangeAddress(start4Row, last4Row, 3, 3));
				start4Row = last4Row + 1;
			} else if (mergeRow.length > 3 && mergeRow[3][k] == 1) {
				start4Row += 1;
			}
			if (mergeRow.length > 4 && mergeRow[4][k] > 1) { // 다섯번째 칼럼 병합 처리.
				last5Row = (start5Row + mergeRow[4][k]) - 1;
				worksheet.addMergedRegion(new CellRangeAddress(start5Row, last5Row, 4, 4));
				start5Row = last5Row + 1;
			} else if (mergeRow.length > 4 && mergeRow[4][k] == 1) {
				start5Row += 1;
			}
		}
	}

	/**
	 * New Ocb Seq 헤더 정의.
	 * 
	 * @param worksheet
	 *            엑셀 시트
	 * @param headers
	 *            Excel Header 정보
	 * 
	 */
	public static void buildHeaderForNewOcbSeq(Sheet worksheet, String[][] headers) throws Exception {
		CellStyle headerCellStyle = ExcelUtil.getHeaderCellStyle(worksheet);
		int headersLength = headers[1].length;
		Row rowHeader1 = worksheet.createRow((short) 2);
		rowHeader1.setHeight((short) 500);
		for (int i = 0; i < headersLength; i++) {
			Cell cell = rowHeader1.createCell(i);
			if (i < 2) {
				cell.setCellValue(headers[0][i]);
			} else if (i == 4) {
				cell.setCellValue(headers[0][2]);
			} else if (i == 7) {
				cell.setCellValue(headers[0][3]);
			} else {
				cell.setCellValue(StringUtils.EMPTY);
			}
			cell.setCellStyle(headerCellStyle);
		}

		Row rowHeader2 = worksheet.createRow((short) 3);
		rowHeader2.setHeight((short) 500);
		for (int i = 0; i < headersLength; i++) {
			Cell cell = rowHeader2.createCell(i);
			cell.setCellValue(headers[1][i]);
			cell.setCellStyle(headerCellStyle);
		}
		// 행레이블 셀 병합
		worksheet.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
		// 전체TR기준
		worksheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 3));
		// 적립TR기준
		worksheet.addMergedRegion(new CellRangeAddress(2, 2, 4, 6));
		// 사용TR기준
		worksheet.addMergedRegion(new CellRangeAddress(2, 2, 7, 9));
	}

	/**
	 * 포인트 선물/합산하기 헤더 정의.
	 * 
	 * @param worksheet
	 *            엑셀 시트
	 * @param headers
	 *            Excel Header 정보
	 * 
	 */
	public static void buildHeaderForPointPresentSum(Sheet worksheet, String[][] headers) throws Exception {
		CellStyle headerCellStyle = ExcelUtil.getHeaderCellStyle(worksheet);
		int headersLength = headers[2].length;
		Row rowHeader1 = worksheet.createRow((short) 2);
		rowHeader1.setHeight((short) 550);
		for (int i = 0; i < headersLength; i++) {
			Cell cell = rowHeader1.createCell(i);
			if (i < 2) {
				cell.setCellValue(headers[0][i]);
			} else if (i == 6) {
				cell.setCellValue(headers[0][2]);
			} else if (i == 10) {
				cell.setCellValue(headers[0][3]);
			} else {
				cell.setCellValue(StringUtils.EMPTY);
			}
			cell.setCellStyle(headerCellStyle);
		}

		Row rowHeader2 = worksheet.createRow((short) 3);
		rowHeader2.setHeight((short) 550);
		for (int i = 0; i < headersLength; i++) {
			Cell cell = rowHeader2.createCell(i);
			if (i < 2) {
				cell.setCellValue(headers[1][i]);
			} else if (i == 6) {
				cell.setCellValue(headers[1][2]);
			} else if (i == 10) {
				cell.setCellValue(headers[1][3]);
			} else if (i == 13) {
				cell.setCellValue(headers[1][4]);
			} else if (i == 18) {
				cell.setCellValue(headers[1][5]);
			} else {
				cell.setCellValue(StringUtils.EMPTY);
			}
			cell.setCellStyle(headerCellStyle);
		}

		Row rowHeader3 = worksheet.createRow((short) 4);
		rowHeader3.setHeight((short) 550);
		for (int i = 0; i < headersLength; i++) {
			Cell cell = rowHeader3.createCell(i);
			cell.setCellValue(headers[2][i]);
			cell.setCellStyle(headerCellStyle);
		}
		// 기준일자 셀병합
		worksheet.addMergedRegion(new CellRangeAddress(2, 4, 0, 0));
		// 합산하기(같이쓰기)
		worksheet.addMergedRegion(new CellRangeAddress(2, 2, 10, 22));
		// 선물하기 선물한 고객기준
		worksheet.addMergedRegion(new CellRangeAddress(2, 3, 1, 5));
		// 선물하기 선물받은 고객기준
		worksheet.addMergedRegion(new CellRangeAddress(2, 3, 6, 9));
		// 전체 고객수
		worksheet.addMergedRegion(new CellRangeAddress(3, 3, 10, 12));
		// 본인 point 사용실적
		worksheet.addMergedRegion(new CellRangeAddress(3, 3, 13, 17));
		// 타인 point 사용실적
		worksheet.addMergedRegion(new CellRangeAddress(3, 3, 18, 22));
	}

	/**
	 * sos BLE 헤더 정의.
	 * 
	 * @param worksheet
	 *            엑셀 시트
	 * @param headers
	 *            Excel Header 정보
	 * 
	 */
	public static void buildHeaderForSosBle(Sheet worksheet, String[][] headers) throws Exception {
		CellStyle headerCellStyle = ExcelUtil.getHeaderCellStyle(worksheet);
		int headersLength = headers[1].length;
		Row rowHeader1 = worksheet.createRow((short) 2);
		rowHeader1.setHeight((short) 500);
		for (int i = 0; i < headersLength; i++) {
			Cell cell = rowHeader1.createCell(i);
			if (i < 3) {
				cell.setCellValue(headers[0][i]);
			} else if (i == 6) {
				cell.setCellValue(headers[0][2]);
			} else if (i == 12) {
				cell.setCellValue(headers[0][3]);
			} else {
				cell.setCellValue(StringUtils.EMPTY);
			}
			cell.setCellStyle(headerCellStyle);
		}

		Row rowHeader2 = worksheet.createRow((short) 3);
		rowHeader2.setHeight((short) 500);
		for (int i = 0; i < headersLength; i++) {
			Cell cell = rowHeader2.createCell(i);
			cell.setCellValue(headers[1][i]);
			cell.setCellStyle(headerCellStyle);
		}
		// 행레이블 셀 병합
		worksheet.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
		worksheet.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));
		// Syrup 모수
		worksheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 5));
		// OCB 모수
		worksheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 11));
		// Syrup ∩ OCB 모수
		worksheet.addMergedRegion(new CellRangeAddress(2, 2, 12, 15));
	}

	/**
	 * monKpi 헤더 정의.
	 * 
	 * @param worksheet
	 *            엑셀 시트
	 * @param headers
	 *            Excel Header 정보
	 * 
	 */
	public static void buildHeaderForMonKpi(Sheet worksheet, String[][] headers) throws Exception {
		CellStyle headerCellStyle = ExcelUtil.getHeaderCellStyle(worksheet);
		int headersLength = headers[1].length;
		Row rowHeader1 = worksheet.createRow((short) 2);
		rowHeader1.setHeight((short) 500);
		for (int i = 0; i < headersLength; i++) {
			Cell cell = rowHeader1.createCell(i);
			if (i < 3) {
				cell.setCellValue(headers[0][i]);
			} else if (i == 5) {
				cell.setCellValue(headers[0][3]);
			} else {
				cell.setCellValue(StringUtils.EMPTY);
			}
			cell.setCellStyle(headerCellStyle);
		}

		Row rowHeader2 = worksheet.createRow((short) 3);
		rowHeader2.setHeight((short) 500);
		for (int i = 0; i < headersLength; i++) {
			Cell cell = rowHeader2.createCell(i);
			cell.setCellValue(headers[1][i]);
			cell.setCellStyle(headerCellStyle);
		}
		// 행레이블 셀 병합
		worksheet.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
		worksheet.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));
		// UV 모수
		worksheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 4));
		// 인당월평균사용일수
		worksheet.addMergedRegion(new CellRangeAddress(2, 2, 5, 7));
	}
}
