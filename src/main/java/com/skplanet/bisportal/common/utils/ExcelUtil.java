package com.skplanet.bisportal.common.utils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

/**
 * The ExcelUtil class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Slf4j
public class ExcelUtil {
	/**
	 * 셀 스타일 가운데 정렬.
	 * 
	 * @param worksheet
	 *            Sheet 겍체.
	 * @return CellStyle 셀 스타일(가운데 정렬).
	 */
	public static CellStyle getCenterStyle(Sheet worksheet) throws Exception {
		// 엑셀 바디의 셀 스타일 정의(문자 가운데 정렬).
		CellStyle styleHVCenter = worksheet.getWorkbook().createCellStyle();
		styleHVCenter.setAlignment(CellStyle.ALIGN_CENTER);
		styleHVCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		styleHVCenter.setWrapText(true);
		return styleHVCenter;
	}

    /**
     * 셀 스타일 좌측 정렬.
     *
     * @param worksheet
     *            Sheet 겍체.
     * @return CellStyle 셀 스타일(좌측 정렬).
     */
    public static CellStyle getLeftStyle(Sheet worksheet) throws Exception {
        // 엑셀 바디의 셀 스타일 정의(문자 죄측 정렬).
        CellStyle styleHVCenter = worksheet.getWorkbook().createCellStyle();
        styleHVCenter.setAlignment(CellStyle.ALIGN_LEFT);
        styleHVCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        styleHVCenter.setWrapText(true);
        return styleHVCenter;
    }

	/**
	 * 셀 스타일 우측 정렬.
	 * 
	 * @param workbook
	 *            Workbook 겍체.
	 * @param worksheet
	 *            Sheet 겍체.
	 * @return CellStyle 셀 스타일(우측 정렬).
	 */
	public static CellStyle getRightStyle(Workbook workbook, Sheet worksheet) throws Exception {
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마).
		DataFormat format = workbook.createDataFormat();
		CellStyle styleHVRight = worksheet.getWorkbook().createCellStyle();
		styleHVRight.setDataFormat(format.getFormat("#,##"));
		styleHVRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		styleHVRight.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return styleHVRight;
	}

	/**
	 * 우측 정렬 및 소수 2째자리 포멧팅.
	 * 
	 * @param workbook
	 *            Workbook 겍체.
	 * @param worksheet
	 *            Sheet 겍체.
	 * @return CellStyle 셀 스타일(우측 정렬 및 소수 2째자리 포멧팅).
	 */
	public static CellStyle getRightFormatStyle(Workbook workbook, Sheet worksheet) throws Exception {
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마, 소수2자리).
		DataFormat format = workbook.createDataFormat();
		CellStyle styleHVRightComma = worksheet.getWorkbook().createCellStyle();
		styleHVRightComma.setDataFormat(format.getFormat("#,##0.00"));
		styleHVRightComma.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		styleHVRightComma.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return styleHVRightComma;
	}

	/**
	 * 우측 정렬 및 소수 1째자리 포멧팅.
	 *
	 * @param workbook
	 *            Workbook 겍체.
	 * @param worksheet
	 *            Sheet 겍체.
	 * @return CellStyle 셀 스타일(우측 정렬 및 소수 1째자리 포멧팅).
	 */
	public static CellStyle getRightFormatStyle2(Workbook workbook, Sheet worksheet) throws Exception {
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마, 소수1자리).
		DataFormat format = workbook.createDataFormat();
		CellStyle styleHVRightComma = worksheet.getWorkbook().createCellStyle();
		styleHVRightComma.setDataFormat(format.getFormat("#,##0.0"));
		styleHVRightComma.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		styleHVRightComma.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return styleHVRightComma;
	}

	/**
	 * 엑셀 헤더 스타일.
	 * 
	 * @param worksheet
	 *            Sheet 겍체.
	 * @return CellStyle 헤더 스타일.
	 */
	public static CellStyle getHeaderCellStyle(Sheet worksheet) throws Exception {
		Font font = worksheet.getWorkbook().createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);

		CellStyle headerCellStyle = worksheet.getWorkbook().createCellStyle();
		headerCellStyle.setFillBackgroundColor(HSSFColor.LIGHT_GREEN.index);
		headerCellStyle.setFillPattern(CellStyle.FINE_DOTS);
		headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		headerCellStyle.setWrapText(true);
		headerCellStyle.setFont(font);
		headerCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headerCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		return headerCellStyle;
	}

	public static void createCell(Row row, int cellNo, int cellType, CellStyle cellStyle, Object value) throws Exception {
		Cell cell = row.createCell(cellNo);
		if (cellType == Cell.CELL_TYPE_NUMERIC) {
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue((Double) ObjectUtils.defaultIfNull(value, 0d));
		} else {
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue((String) ObjectUtils.defaultIfNull(value, StringUtils.EMPTY));
		}
		cell.setCellStyle(cellStyle);
	}
}
