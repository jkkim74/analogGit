package com.skplanet.web.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import com.skplanet.web.model.AutoMap;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExcelService {

	public static void main(String[] args) {
		ExcelService excelService = new ExcelService();
		List<AutoMap> list = null;
		excelService.create(null, "test", list);
	}

	public final void create(Path filePath, String sheetName, List<AutoMap> list) {
		// Workbook 생성
		Workbook xlsWb = new HSSFWorkbook(); // Excel 2007 이전 버전
		// Workbook xlsxWb = new XSSFWorkbook(); // Excel 2007 이상

		// *** Sheet-------------------------------------------------
		// Sheet 생성
		Sheet sheet1 = xlsWb.createSheet(sheetName);
		CellStyle center = xlsWb.createCellStyle();

		center.setBorderTop(CellStyle.BORDER_THIN);
		center.setBorderLeft(CellStyle.BORDER_THIN);
		center.setBorderRight(CellStyle.BORDER_THIN);
		center.setBorderBottom(CellStyle.BORDER_THIN);

		Row row = null;
		Cell cell = null;
		// ----------------------------------------------------------

		for (int i = 0; i < list.size(); i++) {
			// 첫 번째 줄
			row = sheet1.createRow(i);

			// 첫 번째 줄에 Cell 설정하기-------------

			Map map = list.get(i);

			Iterator<String> keys = map.keySet().iterator();
			int j = 0;
			while (keys.hasNext()) {
				String key = keys.next();
				if (map.get(key) != null) {
					if (!(map.get(key).toString().trim()).equals("")) {
						j++;
					}
				}
			}

			if (j != 0) {

				map = list.get(i);

				keys = map.keySet().iterator();
				int k = 0;
				while (keys.hasNext()) {
					String key = keys.next();
					cell = row.createCell(k);
					cell.setCellStyle(center);

					if (map.get(key) != null) {
						cell.setCellValue(map.get(key).toString());
					}

					k++;
				}
			}
		}
		// ---------------------------------

		// excel 파일 저장
		try {
			File xlsFile = filePath.toFile();
			FileOutputStream fileOut = new FileOutputStream(xlsFile);
			xlsWb.write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
