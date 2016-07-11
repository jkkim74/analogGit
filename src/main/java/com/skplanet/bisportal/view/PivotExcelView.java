package com.skplanet.bisportal.view;

import com.skplanet.bisportal.common.excel.PoiHelper;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.utils.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
public class PivotExcelView extends AbstractExcelView {
  /**
   * pivotTable로 구성된 리포트 엑셀 저장.
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
		JqGridRequest jqGridRequest = null;
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
			Elements pvtTables = docs.select("table.pvtTable");

			// 피벗 디멘전 일자(혹은 시간) 칼럼 수.
			//int dataNumcols = Integer.parseInt(StringUtils.defaultIfEmpty(pvtTables.attr("data-numcols"), "0"));
			// 엑셀 헤더 그리기 위한 헤더 칼럼들의 갯수.(search the class 'pivotHeaderCategory')
            int headerLength = docs.select("table tr.head-group th").size();

			// 표시되는 데이터의 전체 갯수.
			int dataNumRows = Integer.parseInt(StringUtils.defaultIfEmpty(pvtTables.attr("data-numrows"), "0"));

			String[][] headers = new String[1][headerLength];
			Elements headerText = docs.select("table tr.head-group").first().getElementsByTag("th");

			// 헤더 병합을 위한 데이터 저장.
			for (int i = 0; i < headerLength; i++) {
				headers[0][i] = headerText.get(i).ownText();
			}

			// 칼럼 넓이 조정.
			super.setColumnWith(worksheet, headerLength);
			// 타이틀 입력.
			super.buildTitle(worksheet, titleName, headerLength);
			// 헤드 입력.
			super.buildHeaders(worksheet, headers, 1, headerLength);

			Elements dataRows = docs.select("table.pvtTable tr");

			// 엑셀의 바디를 그린다.
			this.buildBody(workbook, worksheet, headerLength, dataNumRows, dataRows);
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

  /**
   * 엑셀의 바디 구성.
   *
   * @param workbook 엑셀 객체.
   * @param worksheet 엑셀 시트 객체.
   * @param headerLength 헤더의 갯수.
   * @param dataNumRows 저장된 데이터의 갯수.
   * @param dataRows 리포트 데이터가 저장된 elements.
   * @return void
   */
	private void buildBody(Workbook workbook, Sheet worksheet, int headerLength, int dataNumRows, Elements dataRows)
			throws Exception {

		// 헤더 이후 엑셀 바디 첫 시작 로우.
		int startRowIndex = 3;
		try {
			CellStyle styleHVCenter = ExcelUtil.getCenterStyle(worksheet);
			CellStyle styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);
			int[][] mergeRow = new int[5][dataNumRows + 5];
			String[] mergeValue = new String[10];
			String[][] cellValue = new String[dataNumRows + 5][headerLength + 1];
			for (int i = 1; i < dataNumRows + 1; i++) { //variable i is start 1. the 2-line header is changed 1-line header.
				int childrenSize = dataRows.get(i).children().size();
				Row row = worksheet.createRow(startRowIndex++);
				Elements childrenElements = dataRows.get(i).children();
				int seq = 0;
				for (Element children : childrenElements) {
					cellValue[i][seq] = children.ownText();
					// 병합 칼럼의 수가 현재 5개까지 지원함.
					// TODO 병합 칼럼이 5개 넘을 경우 Array의 사이즈 늘려주고 if case 추가해 줘야 함.
					if (headerLength == childrenSize) {// 병합 대상이 없는 첫번째 로우의 병합 갯수 및 병합 대상 칼럼값 저장.
						PoiHelper.firstMerge(row, children, mergeRow, mergeValue, cellValue, seq, i, styleHVCenter,
								styleHVRight);
					} else if (headerLength == (childrenSize + 1)) { // 첫번째 칼럼이 병합 대상인 로우의 병합 갯수 및 병합 대상 칼럼값 저장.
						PoiHelper.secondMerge(row, children, mergeRow, mergeValue, seq, i, styleHVCenter, styleHVRight);
					} else if (headerLength == (childrenSize + 2)) { // 두번째 칼럼이 병합대상인 로우의 병합 갯수 및 병합 대상 칼럼값 저장.
						PoiHelper.thirdMerge(row, children, mergeRow, mergeValue, seq, i, styleHVCenter, styleHVRight);
					} else if (headerLength == (childrenSize + 3)) {// 세번째 칼럼이 병합 대상인 로우의 병합 갯수 및 병합 대상 칼럼값 저장.
						PoiHelper.fourthMerge(row, children, mergeRow, mergeValue, seq, i, styleHVCenter, styleHVRight);
					} else if (headerLength == (childrenSize + 4)) {// 네번째 칼럼이 병합 대상인 로우의 병합 갯수 및 병합 대상 칼럼값 저장.
						PoiHelper.fifthMerge(row, children, mergeRow, mergeValue, seq, i, styleHVCenter, styleHVRight);
					} else if (headerLength == (childrenSize + 5)) {// 다섯번째 칼럼이 병합 대상인 로우 처리.
						PoiHelper.sixthMerge(row, children, mergeValue, seq, i, styleHVCenter, styleHVRight);
					}
					seq++;
				}

			}
			// 엑셀 데이터 병합 처리 mergeRowBody(Sheet worksheet, int[][] mergeRow, String[][] cellValue, int dataNumRows, int startRow, int initValue)
			PoiHelper.mergeRowBody(worksheet, mergeRow, cellValue, dataNumRows, 3, 1);
			// Cell 병합
			// PoiHelper.mergeCellBody(worksheet, cellValue, dataNumRows, 2);
		} catch (Exception e) {
			log.error("buildBody()", e);
			throw new Exception("build excel body error!");
		}
	}
}
