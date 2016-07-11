package com.skplanet.bisportal.view;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.utils.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Map;

import static com.skplanet.bisportal.common.utils.Objects.uncheckedCast;

/**
 * The PivotExcelView class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Slf4j
public class DynamicHeaderPivotExcelView extends AbstractExcelView {

	/**
	 * pivotTable로 구성된 리포트 엑셀 저장.
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
		JqGridRequest jqGridRequest = null;
		// rowNum 0,1 은 리포트 타이틀로 쓰인다.
		int rowNum = 2, colNum, rowspan, colspan, totalColNum;
		CellStyle styleHVCenter, styleHVRight, headerCellStyle;
		Row row;
		Cell cell;

		try {
			jqGridRequest = uncheckedCast(model.get("jqGridRequest"));
			String titleName = StringUtils.defaultIfEmpty(jqGridRequest.getTitleName(), "Unknown");
			Sheet worksheet = workbook.createSheet(titleName);
			if (StringUtils.isEmpty(jqGridRequest.getHtmlData())) {
				// 칼럼 넓이 조정.
				super.setColumnWith(worksheet, 5);
				// 타이틀 입력.
				super.buildTitle(worksheet, titleName, 5);
				log.debug("Data Not Found");
				return;
			}

			Document docs = Jsoup.parseBodyFragment(URLDecoder.decode(jqGridRequest.getHtmlData(), CharEncoding.UTF_8));
			Elements pivotTable = docs.select("table");
			totalColNum = Integer.parseInt(StringUtils.defaultIfEmpty(pivotTable.attr("data-totalcol"), "0"));

			// 타이틀 입력.
			super.buildTitle(worksheet, titleName, totalColNum);
			// 헤더 영역 (리포트 데이터가 포함되지 않은 로우)
			super.setColumnWith(worksheet, totalColNum, 3000);

			styleHVCenter = ExcelUtil.getCenterStyle(worksheet);
			styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);
			headerCellStyle = ExcelUtil.getHeaderCellStyle(worksheet);

			Elements trs = docs.select("table tr");
			for (Element tr : trs) {
				row = worksheet.createRow(rowNum);
				Elements ths = tr.select("th");
				for (Element th : ths) {
					colNum = Integer.parseInt(StringUtils.defaultIfEmpty(th.attr("data-col"), "0"));
					rowspan = Integer.parseInt(StringUtils.defaultIfEmpty(th.attr("rowspan"), "1"));
					colspan = Integer.parseInt(StringUtils.defaultIfEmpty(th.attr("colspan"), "1"));
					cell = row.createCell(colNum);
					if(rowNum == 2){//only header row's background is green. :)
						cell.setCellStyle(headerCellStyle);
					}else{
						cell.setCellStyle(styleHVCenter);
					}
					cell.setCellValue(th.ownText());
					// cell merge
					if (rowspan > 1 || colspan > 1) {
						worksheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + rowspan - 1, colNum, colNum
								+ colspan - 1));
					}
				}

				Elements tds = tr.select("td");
				for (Element td : tds) {
					colNum = Integer.parseInt(StringUtils.defaultIfEmpty(td.attr("data-col"), "0"));
					rowspan = Integer.parseInt(StringUtils.defaultIfEmpty(td.attr("rowspan"), "1"));
					colspan = Integer.parseInt(StringUtils.defaultIfEmpty(td.attr("colspan"), "1"));
					cell = row.createCell(colNum);
					cell.setCellStyle(styleHVRight);
					cell.setCellValue(td.ownText());
					if (rowspan > 1 || colspan > 1) {
						// cell merge
						worksheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + rowspan - 1, colNum, colNum
								+ colspan - 1));
					}
				}
				rowNum++;
			}
		} catch (Exception e) {
			log.error("buildExcelDocument() {}", e);
			String titleName = "Unknown";
			if (jqGridRequest != null && StringUtils.isNotEmpty(jqGridRequest.getTitleName())) {
				titleName = jqGridRequest.getTitleName();
			}
			Sheet sheet = workbook.getSheet(titleName);
			if (sheet == null)
				sheet = workbook.createSheet(jqGridRequest.getTitleName());
			// 칼럼 넓이 조정.
			super.setColumnWith(sheet, 5);
			// 타이틀 입력.
			super.buildTitle(sheet, titleName, 5);
		}
	}
}
