package com.skplanet.bisportal.common.excel;

import com.skplanet.bisportal.common.model.MultiHeader;
import com.skplanet.bisportal.common.utils.ExcelUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * The SyrupBleMergeHandler(Syrup BLE 동적 헤더 병합 처리) class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
public class SyrupBleMergeHandler implements BleMergeHandler {
	@Override
	public void handleBleMerge(Sheet worksheet, MultiHeader headers, String statContents) throws Exception {
		CellStyle headerCellStyle = ExcelUtil.getHeaderCellStyle(worksheet);
		int headerLength = headers.getHeader3().length;
		Row rowHeader1 = worksheet.createRow((short) 2);
		rowHeader1.setHeight((short) 550);
		Row rowHeader2 = worksheet.createRow((short) 3);
		rowHeader2.setHeight((short) 550);
		Row rowHeader3 = worksheet.createRow((short) 4);
		rowHeader3.setHeight((short) 550);
		// Header 처리
		for (int i = 0; i < headerLength; i++) {
			Cell cell = rowHeader1.createCell(i);
			cell.setCellValue(headers.getHeader1()[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for (int i = 0; i < headerLength; i++) {
			Cell cell = rowHeader2.createCell(i);
			cell.setCellValue(headers.getHeader2()[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for (int i = 0; i < headerLength; i++) {
			Cell cell = rowHeader3.createCell(i);
			cell.setCellValue(headers.getHeader3()[i]);
			cell.setCellStyle(headerCellStyle);
		}
		worksheet.addMergedRegion(new CellRangeAddress(2, 4, 0, 0));// 기간
		worksheet.addMergedRegion(new CellRangeAddress(2, 4, 1, 1));// 가맹점명
		worksheet.addMergedRegion(new CellRangeAddress(2, 4, 2, 2));// MID
		worksheet.addMergedRegion(new CellRangeAddress(2, 2, 3, 4));
		// 병합 처리
		if (StringUtils.indexOf(statContents, "1") > -1) {// 통계 항목 전체 선택
			worksheet.addMergedRegion(new CellRangeAddress(2, 2, 5, 10));
			worksheet.addMergedRegion(new CellRangeAddress(2, 2, 11, 18));
			worksheet.addMergedRegion(new CellRangeAddress(2, 2, 19, 20));

			worksheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 4));

			worksheet.addMergedRegion(new CellRangeAddress(3, 3, 5, 6));
			worksheet.addMergedRegion(new CellRangeAddress(3, 3, 7, 8));
			worksheet.addMergedRegion(new CellRangeAddress(3, 3, 9, 10));

			worksheet.addMergedRegion(new CellRangeAddress(3, 3, 11, 12));
			worksheet.addMergedRegion(new CellRangeAddress(3, 3, 13, 14));
			worksheet.addMergedRegion(new CellRangeAddress(3, 3, 15, 16));
			worksheet.addMergedRegion(new CellRangeAddress(3, 3, 17, 18));

			worksheet.addMergedRegion(new CellRangeAddress(3, 3, 19, 20));
		} else {
			if (StringUtils.indexOf(statContents, "3") > -1) {
				worksheet.addMergedRegion(new CellRangeAddress(2, 2, 5, 10));

				worksheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 4));

				worksheet.addMergedRegion(new CellRangeAddress(3, 3, 5, 6));
				worksheet.addMergedRegion(new CellRangeAddress(3, 3, 7, 8));
				worksheet.addMergedRegion(new CellRangeAddress(3, 3, 9, 10));
				if (StringUtils.indexOf(statContents, "4") > -1) {
					worksheet.addMergedRegion(new CellRangeAddress(2, 2, 11, 18));
					worksheet.addMergedRegion(new CellRangeAddress(3, 3, 11, 12));
					worksheet.addMergedRegion(new CellRangeAddress(3, 3, 13, 14));
					worksheet.addMergedRegion(new CellRangeAddress(3, 3, 15, 16));
					worksheet.addMergedRegion(new CellRangeAddress(3, 3, 17, 18));
					if (StringUtils.indexOf(statContents, "5") > -1) {
						worksheet.addMergedRegion(new CellRangeAddress(2, 2, 19, 20));
						worksheet.addMergedRegion(new CellRangeAddress(3, 3, 19, 20));
					}
				} else {
					if (StringUtils.indexOf(statContents, "5") > -1) {
						worksheet.addMergedRegion(new CellRangeAddress(2, 2, 11, 12));
						worksheet.addMergedRegion(new CellRangeAddress(3, 3, 11, 12));
					}
				}
			} else if (StringUtils.indexOf(statContents, "4") > -1) {
				worksheet.addMergedRegion(new CellRangeAddress(2, 2, 5, 12));

				worksheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 4));

				worksheet.addMergedRegion(new CellRangeAddress(3, 3, 5, 6));
				worksheet.addMergedRegion(new CellRangeAddress(3, 3, 7, 8));
				worksheet.addMergedRegion(new CellRangeAddress(3, 3, 9, 10));
				worksheet.addMergedRegion(new CellRangeAddress(3, 3, 11, 12));
				if (StringUtils.indexOf(statContents, "5") > -1) {
					worksheet.addMergedRegion(new CellRangeAddress(2, 2, 13, 14));
					worksheet.addMergedRegion(new CellRangeAddress(3, 3, 13, 14));
				}
			} else if (StringUtils.indexOf(statContents, "5") > -1) {
				worksheet.addMergedRegion(new CellRangeAddress(2, 2, 5, 6));

				worksheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 4));

				worksheet.addMergedRegion(new CellRangeAddress(3, 3, 5, 6));
			} else {
				worksheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 4));
			}
		}
	}
}
