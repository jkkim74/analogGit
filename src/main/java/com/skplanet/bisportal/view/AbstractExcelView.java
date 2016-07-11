package com.skplanet.bisportal.view;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.common.utils.FileUtil;
import org.apache.commons.codec.CharEncoding;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

/**
 * The AbstractExcelView class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
public abstract class AbstractExcelView extends AbstractView {
	private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	private String fileName = "Excel_donwload.xlsx";
	protected static final Injector injector = Guice.createInjector();

	public AbstractExcelView() {
		setContentType(CONTENT_TYPE);
	}

	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}

	/**
	 * Excel view 렌더링 함수 재정의.
	 */
	@Override
	protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		fillDefaultParam(model);
		ByteArrayOutputStream baos = createTemporaryOutputStream();
		Workbook workbook = new XSSFWorkbook();

		this.fileName = this.fileName.replaceAll(".xlsx|.xls", ".xlsx");
		buildExcelDocument(model, workbook, request, response);
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"",
				URLEncoder.encode(this.fileName, CharEncoding.UTF_8)));
		response.setHeader("Content-Transfer-Encoding", "binary");

		if (!FileUtil.isLocal(request.getRequestURL().toString())) {
			// 문서보안 적용 ------ start
			response.setContentType(getContentType());
			response.setContentLength(baos.size());
			this.fileName = FileUtil.renameAddSuffix(this.fileName);
			FileUtil.makeExcelToServer(workbook, this.fileName);
			this.fileName = FileUtil.encoding(this.fileName);
			FileUtil.download(response, this.fileName);
			// 문서보안 적용 ------ end
		} else {
			// 문서보안 미적용 ------ start
			workbook.write(baos);
			writeToHttpResponse(response, baos);
			// 문서보안 미적용 ------ end
		}
	}

	/**
	 * 리포트용 타이틀 정의.
	 *
	 * @param worksheet
	 *            엑셀 시트
	 * @param menuName
	 *            서비스 메뉴 이름
	 * @param headLength
	 *            헤더 길이
	 */
	protected void buildTitle(Sheet worksheet, String menuName, int headLength) throws Exception {
		// Create font style for the report title
		Font fontTitle = worksheet.getWorkbook().createFont();
		fontTitle.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontTitle.setFontHeight((short) 280);

		// Create cell style for the report title
		CellStyle cellStyleTitle = ExcelUtil.getCenterStyle(worksheet);
		cellStyleTitle.setFont(fontTitle);

		CellStyle cellStyleDate = worksheet.getWorkbook().createCellStyle();
		cellStyleDate.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleDate.setWrapText(true);

		// Create report title
		Row rowTitle = worksheet.createRow((short) 0);
		rowTitle.setHeight((short) 500);
		Cell cellTitle = rowTitle.createCell(0);
		cellTitle.setCellValue(menuName + " 리포트");
		cellTitle.setCellStyle(cellStyleTitle);

		// Create merged region for the report title
		worksheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headLength - 1));
		worksheet.addMergedRegion(new CellRangeAddress(1, 1, 0, headLength - 1));

		// Create date header
		Row dateTitle = worksheet.createRow((short) 1);
		Cell cellDate = dateTitle.createCell(0);
		cellDate.setCellValue("This report was generated at " + new Date());
		cellDate.setCellStyle(cellStyleDate);
	}

	/**
	 * 칼럼 헤더 정의.
	 *
	 * @param worksheet
	 *            엑셀 시트
	 * @param headers
	 *            Excel Header 정보
	 */
	protected void buildHeaders(Sheet worksheet, String[][] headers, int pivotFlag, int headerColSpan) throws Exception {

		// Create cell style for the headers
		CellStyle headerCellStyle = ExcelUtil.getHeaderCellStyle(worksheet);

		if (pivotFlag == 1) {// pivotTable
			buildHeaderForPivot(worksheet, headers, headerColSpan, headerCellStyle);
		} else if (pivotFlag == 2) { // 경영실적 Summary
			buildHeaderForSummary(worksheet, headers, headerColSpan, headerCellStyle);
		} else if (pivotFlag == 3) { // 경영실적 상세조회
			buildHeaderForBoss(worksheet, headers, headerColSpan, headerCellStyle);
		} else {
			buildHeaderForDefault(worksheet, headers, headerCellStyle);
		}
	}

	/**
	 * 칼럼 길이 지정.
	 *
	 */
	protected void setColumnWith(Sheet worksheet, int headersLength) throws Exception {
		// Set column widths
		_setColumnWidths(worksheet, headersLength, 5000);
	}

	/**
	 * 칼럼 길이 지정.
	 *
	 */
	protected void setColumnWith(Sheet worksheet, int headersLength, int headerSize) throws Exception {
		// Set column widths
		_setColumnWidths(worksheet, headersLength, headerSize);
	}

	private void _setColumnWidths(Sheet worksheet, int headersLength, int headerSize) {
		for (int i = 0; i < headersLength; i++) {
			worksheet.autoSizeColumn(i);
			worksheet.setColumnWidth(i, headerSize);
		}
	}

	/**
	 * 서비스별로 엑셀 그리기용 추상 함수.
	 *
	 */
	protected abstract void buildExcelDocument(Map<String, Object> model, Workbook workbook,
			HttpServletRequest request, HttpServletResponse response) throws Exception;

	/**
	 * 엑셀 파일 이름 지정.
	 *
	 */
	private void fillDefaultParam(Map<String, Object> model) throws Exception {
		this.fileName = ((JqGridRequest) model.get("jqGridRequest")).getXlsName();
	}

	/**
	 * pivotTable용 헤더 정의.
	 *
	 * @param worksheet
	 *            엑셀 시트
	 * @param headers
	 *            Excel Header 정보
	 * @param headerColSpan
	 *            헤더 길이
	 * @param headerCellStyle
	 *            Excel 쎌 스타일
	 */
	private void buildHeaderForPivot(Sheet worksheet, String[][] headers, int headerColSpan, CellStyle headerCellStyle)
			throws Exception {
		int headersLength = headers[0].length;

		Row rowHeader = worksheet.createRow((short) (2));
		rowHeader.setHeight((short) 500);
		for (int j = 0; j < headersLength; j++) {
			Cell cell = rowHeader.createCell(j);
			cell.setCellValue(headers[0][j]);
			cell.setCellStyle(headerCellStyle);
		}
	}

	/**
	 * 경영 실적 요약용 헤더 정의.
	 *
	 * @param worksheet
	 *            엑셀 시트
	 * @param headers
	 *            Excel Header 정보
	 * @param headerColSpan
	 *            헤더 길이
	 * @param headerCellStyle
	 *            Excel 쎌 스타일
	 */
	private void buildHeaderForSummary(Sheet worksheet, String[][] headers, int headerColSpan, CellStyle headerCellStyle)
			throws Exception {
		int headersLength = headers[0].length;
		for (int i = 0; i < 2; i++) {
			Row rowHeader = worksheet.createRow((short) (i + 2));
			rowHeader.setHeight((short) 500);
			for (int j = 0; j < headersLength; j++) {
				Cell cell = rowHeader.createCell(j);
				cell.setCellValue(headers[i][j]);
				cell.setCellStyle(headerCellStyle);
			}
		}
		worksheet.addMergedRegion(new CellRangeAddress(2, 2, 0, headerColSpan - 1));
		for (int i = headerColSpan; i < headersLength; i++) {
			worksheet.addMergedRegion(new CellRangeAddress(2, 3, i, i));
		}
	}

	/**
	 * 경영실적 상세조회용 헤더 정의.
	 *
	 * @param worksheet
	 *            엑셀 시트
	 * @param headers
	 *            Excel Header 정보
	 * @param headerColSpan
	 *            헤더 길이
	 * @param headerCellStyle
	 *            Excel 쎌 스타일
	 */
	private void buildHeaderForBoss(Sheet worksheet, String[][] headers, int headerColSpan, CellStyle headerCellStyle)
			throws Exception {
		int headersLength = headers[0].length;
		Row rowHeader = worksheet.createRow((short) 2);
		rowHeader.setHeight((short) 500);
		for (int i = 0; i < headersLength; i++) {
			Cell cell = rowHeader.createCell(i);
			cell.setCellValue(headers[0][i]);
			cell.setCellStyle(headerCellStyle);
		}
		worksheet.addMergedRegion(new CellRangeAddress(2, 2, 0, headerColSpan - 1));
	}

	/**
	 * 기본 헤더 정의.
	 *
	 * @param worksheet
	 *            엑셀 시트
	 * @param headers
	 *            Excel Header 정보
	 * @param headerCellStyle
	 *            Excel 쎌 스타일
	 */
	private void buildHeaderForDefault(Sheet worksheet, String[][] headers, CellStyle headerCellStyle) throws Exception {
		int headersLength = headers[0].length;
		Row rowHeader = worksheet.createRow((short) 2);
		rowHeader.setHeight((short) 500);
		for (int i = 0; i < headersLength; i++) {
			Cell cell = rowHeader.createCell(i);
			cell.setCellValue(headers[0][i]);
			cell.setCellStyle(headerCellStyle);
		}
	}

	private void writeToHttpResponse(HttpServletResponse response, ByteArrayOutputStream baos) throws IOException {
		response.setContentType(this.getContentType());
		response.setContentLength(baos.size());
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
		//if (out != null)
		out.close();
		//if (baos != null)
		baos.close();
	}
}
