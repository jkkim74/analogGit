package com.skplanet.bisportal.view.admin;

import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.bip.BpmDlyPrst;
import com.skplanet.bisportal.model.bip.BpmMthStcPrst;
import com.skplanet.bisportal.model.bip.BpmWkStcPrst;
import com.skplanet.bisportal.view.AbstractDrtExcelView;
import com.skplanet.bisportal.view.CustomInput;
import com.skplanet.bisportal.view.syrup.NewCustomExcelOutput;
import net.sf.reportengine.CrossTabReport;
import net.sf.reportengine.config.DefaultCrosstabData;
import net.sf.reportengine.config.DefaultCrosstabHeaderRow;
import net.sf.reportengine.config.DefaultGroupColumn;
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
 * Created by lko on 2014-11-07.
 *
 * The BpmResultSumExcelView class.
 *
 * @author kyoungoh lee
 */
public class BpmResultSumExcelView extends AbstractDrtExcelView {
	/**
	 * 엑셀 파일 이름 지정.
	 *
	 */
	@Override
	protected void fillDefaultParam(Map<String, Object> model) throws Exception {
		super.fileName = ((WhereCondition) model.get("whereCondition")).getXlsName();
	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String menuName = ((WhereCondition) model.get("whereCondition")).getTitleName();
		String dateType = ((WhereCondition) model.get("whereCondition")).getDateType();
		String pivotFlag = ((WhereCondition) model.get("whereCondition")).getPivotFlag();
		Sheet worksheet = workbook.createSheet(menuName);
		if (pivotFlag.equals(Constants.NO)) {
			if (dateType.equals(Constants.DATE_DAY)) {
				downloadExcelForbpmResultSumDay(workbook, worksheet, menuName, model);
			} else if (dateType.equals(Constants.DATE_WEEK)) {
				downloadExcelForbpmResultSumWeek(workbook, worksheet, menuName, model);
			} else if (dateType.equals(Constants.DATE_MONTH)) {
				downloadExcelForbpmResultSumMonth(workbook, worksheet, menuName, model);
			}
		} else {
			if (dateType.equals(Constants.DATE_DAY)) {
				downloadExcelPivotForbpmResultSumDay(menuName, model, response);
			} /*else if (dateType.equals("week")) {
			} else if (dateType.equals("month")) {
			}*/
		}
	}

	private void downloadExcelPivotForbpmResultSumDay(String menuName, Map<String, Object> model,
			HttpServletResponse response) throws Exception {
		response.setContentType("application/ms-excel");
		response.setHeader("Expires:", "0");
		response.setHeader("Content-Disposition", "attachment; filename=boss-detail.xls");
		NewCustomExcelOutput output = new NewCustomExcelOutput(response.getOutputStream());
		DefaultGroupColumn defaultGroupColumn = new DefaultGroupColumn("Power Index", 4, 0);
		defaultGroupColumn.setShowDuplicates(false);
		DefaultGroupColumn defaultGroupColumn2 = new DefaultGroupColumn("Power Index", 5, 1);
		defaultGroupColumn2.setShowDuplicates(false);
		DefaultGroupColumn defaultGroupColumn3 = new DefaultGroupColumn("Power Index", 6, 2);
		defaultGroupColumn3.setShowDuplicates(false);
		DefaultCrosstabHeaderRow defaultCrosstabHeaderRow = new DefaultCrosstabHeaderRow(7);
		CrossTabReport crosstabReport = new CrossTabReport.Builder().title(menuName)
				.input(new CustomInput(model.get("bpmResultSumData"))).output(output)
				.addGroupColumn(defaultGroupColumn).addGroupColumn(defaultGroupColumn2)
				.addGroupColumn(defaultGroupColumn3).addHeaderRow(defaultCrosstabHeaderRow)
				.crosstabData(new DefaultCrosstabData(8)).build();
		crosstabReport.execute();
		response.flushBuffer();
	}

	private void downloadExcelForbpmResultSumDay(Workbook workbook, Sheet worksheet, String menuName,
			Map<String, Object> model) throws Exception {
		String headers[][] = { { "일자", "Power Index", "", "", "계" } };
		int headLength = headers[0].length;

		// 칼럼 넓이 조정.
		super.setColumnWith(worksheet, headLength);
		// 타이틀 입력.
		super.buildTitle(worksheet, menuName, headLength);
		// 헤드 입력.
		super.buildHeaders(worksheet, headers, 0, 0);

		// confirm Row offset
		int startRowIndex = 3;

		// 엑셀 바디의 셀 스타일 정의(문자 가운데 정렬).
		CellStyle styleHVCenter = ExcelUtil.getCenterStyle(worksheet);
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마).
		CellStyle styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);

		// 엑셀 바디 생성.
		List<BpmDlyPrst> bpmDlyPrstList = uncheckedCast(model.get("bpmResultSumData"));
		String[] merge = new String[4];
		int startRow = 3, lastRow = 3, startSerRow = 3, lastSerRow = 3, startIdxClCdValRow = 3, lastIdxClCdValRow = 3, startIdxCttCdValRow = 3, lastIdxCttCdValRow = 3;
		int dayMbrentStaSize = bpmDlyPrstList.size();
		for (BpmDlyPrst bpmDlyPrst : bpmDlyPrstList) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// 일자
			ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, bpmDlyPrst.getDlyStrdDt());
			// Power Index
			ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_STRING, styleHVCenter, bpmDlyPrst.getIdxClCdGrpNm());
			// Power Index
			ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_STRING, styleHVCenter,
					bpmDlyPrst.getIdxClCdVal().replaceAll("^[1-2]+", ""));
			// Power Index
			ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_STRING, styleHVCenter, bpmDlyPrst.getIdxCttCdVal());
			// 계
			ExcelUtil
					.createCell(row, 4, Cell.CELL_TYPE_NUMERIC, styleHVRight, bpmDlyPrst.getDlyRsltVal().doubleValue());

			// 셀 병합처리
			if (StringUtils.equals(merge[0], bpmDlyPrst.getDlyStrdDt())) {
				lastRow++;
				if ((startRowIndex == dayMbrentStaSize + 3) && (startRow < lastRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
				}
			} else {
				if (startRow < lastRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
				}
				startRow = startRowIndex - 1;
				lastRow = startRowIndex - 1;
			}
			if (StringUtils.equals(merge[1], bpmDlyPrst.getDlyStrdDt() + bpmDlyPrst.getIdxClCdGrpNm())) {
				lastSerRow++;
				if ((startRowIndex == dayMbrentStaSize + 3) && (startSerRow < lastSerRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startSerRow, lastSerRow, 1, 1));
				}
			} else {
				if (startSerRow < lastSerRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startSerRow, lastSerRow, 1, 1));
				}
				startSerRow = startRowIndex - 1;
				lastSerRow = startRowIndex - 1;
			}

			if (StringUtils.equals(merge[2],
					bpmDlyPrst.getDlyStrdDt() + bpmDlyPrst.getIdxClCdGrpNm() + bpmDlyPrst.getIdxClCdVal())) {
				lastIdxClCdValRow++;
				if ((startRowIndex == dayMbrentStaSize + 3) && (startIdxClCdValRow < lastIdxClCdValRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startIdxClCdValRow, lastIdxClCdValRow, 2, 2));
				}
			} else {
				if (startIdxClCdValRow < lastIdxClCdValRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startIdxClCdValRow, lastIdxClCdValRow, 2, 2));
				}
				startIdxClCdValRow = startRowIndex - 1;
				lastIdxClCdValRow = startRowIndex - 1;
			}
			if (StringUtils.equals(bpmDlyPrst.getIdxClCdVal().trim().replaceAll("^[1-2]+", StringUtils.EMPTY), bpmDlyPrst
					.getIdxCttCdVal().trim())) {
				worksheet.addMergedRegion(new CellRangeAddress(startIdxClCdValRow, startIdxClCdValRow, 2, 3));
			}
			if (StringUtils.equals(
					merge[3],
					bpmDlyPrst.getDlyStrdDt() + bpmDlyPrst.getIdxClCdGrpNm() + bpmDlyPrst.getIdxClCdVal()
							+ bpmDlyPrst.getIdxCttCdVal())) {
				lastIdxCttCdValRow++;
				if ((startRowIndex == dayMbrentStaSize + 3) && (startIdxCttCdValRow < lastIdxCttCdValRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startIdxCttCdValRow, lastIdxCttCdValRow, 3, 3));
				}
			} else {
				if (startIdxCttCdValRow < lastIdxCttCdValRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startIdxCttCdValRow, lastIdxCttCdValRow, 3, 3));
				}
				startIdxCttCdValRow = startRowIndex - 1;
				lastIdxCttCdValRow = startRowIndex - 1;
			}
			merge[0] = bpmDlyPrst.getDlyStrdDt();
			merge[1] = bpmDlyPrst.getDlyStrdDt() + bpmDlyPrst.getIdxClCdGrpNm();
			merge[2] = bpmDlyPrst.getDlyStrdDt() + bpmDlyPrst.getIdxClCdGrpNm() + bpmDlyPrst.getIdxClCdVal();
			merge[3] = bpmDlyPrst.getDlyStrdDt() + bpmDlyPrst.getIdxClCdGrpNm() + bpmDlyPrst.getIdxClCdVal()
					+ bpmDlyPrst.getIdxCttCdVal();
		}
		worksheet.addMergedRegion(new CellRangeAddress(2, // first row (0-based)
				2, // last row (0-based)
				1, // first column (0-based)
				3 // last column (0-based)
				));
	}

	private void downloadExcelForbpmResultSumWeek(Workbook workbook, Sheet worksheet, String menuName,
			Map<String, Object> model) throws Exception {
		String headers[][] = { { "주차", "Power Index", "", "", "계" } };
		int headLength = headers[0].length;

		// 칼럼 넓이 조정.
		super.setColumnWith(worksheet, headLength);
		// 타이틀 입력.
		super.buildTitle(worksheet, menuName, headLength);
		// 헤드 입력.
		super.buildHeaders(worksheet, headers, 0, 0);

		// confirm Row offset
		int startRowIndex = 3;

		// 엑셀 바디의 셀 스타일 정의(문자 가운데 정렬).
		CellStyle styleHVCenter = ExcelUtil.getCenterStyle(worksheet);
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마).
		CellStyle styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);

		// 엑셀 바디 생성.
		List<BpmWkStcPrst> bpmWkStcPrstList = uncheckedCast(model.get("bpmResultSumData"));
		String[] merge = new String[4];
		int startRow = 3, lastRow = 3, startSerRow = 3, lastSerRow = 3, startIdxClCdValRow = 3, lastIdxClCdValRow = 3, startIdxCttCdValRow = 3, lastIdxCttCdValRow = 3;
		int WkMbrentStaSize = bpmWkStcPrstList.size();
		for (BpmWkStcPrst bpmWkStcPrst : bpmWkStcPrstList) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// 주차
			ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, bpmWkStcPrst.getWkStrdVal());
			// Power Index
			ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_STRING, styleHVCenter, bpmWkStcPrst.getIdxClCdGrpNm());
			// Power Index
			ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_STRING, styleHVCenter,
					bpmWkStcPrst.getIdxClCdVal().replaceAll("^[1-2]+", StringUtils.EMPTY));
			// Power Index
			ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_STRING, styleHVCenter, bpmWkStcPrst.getIdxCttCdVal());
			// 계
			ExcelUtil.createCell(row, 4, Cell.CELL_TYPE_NUMERIC, styleHVRight, bpmWkStcPrst.getWkStcRsltVal()
					.doubleValue());
			// 셀 병합처리
			if (StringUtils.equals(merge[0], bpmWkStcPrst.getWkStrdVal())) {
				lastRow++;
				if ((startRowIndex == WkMbrentStaSize + 3) && (startRow < lastRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
				}
			} else {
				if (startRow < lastRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
				}
				startRow = startRowIndex - 1;
				lastRow = startRowIndex - 1;
			}
			if (StringUtils.equals(merge[1], bpmWkStcPrst.getWkStrdVal() + bpmWkStcPrst.getIdxClCdGrpNm())) {
				lastSerRow++;
				if ((startRowIndex == WkMbrentStaSize + 3) && (startSerRow < lastSerRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startSerRow, lastSerRow, 1, 1));
				}
			} else {
				if (startSerRow < lastSerRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startSerRow, lastSerRow, 1, 1));
				}
				startSerRow = startRowIndex - 1;
				lastSerRow = startRowIndex - 1;
			}
			if (StringUtils.equals(merge[2], bpmWkStcPrst.getWkStrdVal() + bpmWkStcPrst.getIdxClCdGrpNm()
					+ bpmWkStcPrst.getIdxClCdVal())) {
				lastIdxClCdValRow++;
				if ((startRowIndex == WkMbrentStaSize + 3) && (startIdxClCdValRow < lastIdxClCdValRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startIdxClCdValRow, lastIdxClCdValRow, 2, 2));
				}
			} else {
				if (startIdxClCdValRow < lastIdxClCdValRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startIdxClCdValRow, lastIdxClCdValRow, 2, 2));
				}
				startIdxClCdValRow = startRowIndex - 1;
				lastIdxClCdValRow = startRowIndex - 1;
			}
			if (StringUtils.equals(bpmWkStcPrst.getIdxClCdVal().trim().replaceAll("^[1-2]+", StringUtils.EMPTY), bpmWkStcPrst
					.getIdxCttCdVal().trim())) {
				worksheet.addMergedRegion(new CellRangeAddress(startIdxClCdValRow, startIdxClCdValRow, 2, 3));
			}
			if (StringUtils.equals(merge[3], bpmWkStcPrst.getWkStrdVal() + bpmWkStcPrst.getIdxClCdGrpNm()
					+ bpmWkStcPrst.getIdxClCdVal() + bpmWkStcPrst.getIdxCttCdVal())) {
				lastIdxCttCdValRow++;
				if ((startRowIndex == WkMbrentStaSize + 3) && (startIdxCttCdValRow < lastIdxCttCdValRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startIdxCttCdValRow, lastIdxCttCdValRow, 3, 3));
				}
			} else {
				if (startIdxCttCdValRow < lastIdxCttCdValRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startIdxCttCdValRow, lastIdxCttCdValRow, 3, 3));
				}
				startIdxCttCdValRow = startRowIndex - 1;
				lastIdxCttCdValRow = startRowIndex - 1;
			}
			merge[0] = bpmWkStcPrst.getWkStrdVal();
			merge[1] = bpmWkStcPrst.getWkStrdVal() + bpmWkStcPrst.getIdxClCdGrpNm();
			merge[2] = bpmWkStcPrst.getWkStrdVal() + bpmWkStcPrst.getIdxClCdGrpNm() + bpmWkStcPrst.getIdxClCdVal();
			merge[3] = bpmWkStcPrst.getWkStrdVal() + bpmWkStcPrst.getIdxClCdGrpNm() + bpmWkStcPrst.getIdxClCdVal()
					+ bpmWkStcPrst.getIdxCttCdVal();

		}
		worksheet.addMergedRegion(new CellRangeAddress(2, // first row (0-based)
				2, // last row (0-based)
				1, // first column (0-based)
				3 // last column (0-based)
				));
	}

	protected void downloadExcelForbpmResultSumMonth(Workbook workbook, Sheet worksheet, String menuName,
			Map<String, Object> model) throws Exception {
		String headers[][] = { { "월", "Power Index", "", "", "계" } };
		int headLength = headers[0].length;

		// 칼럼 넓이 조정.
		super.setColumnWith(worksheet, headLength);
		// 타이틀 입력.
		super.buildTitle(worksheet, menuName, headLength);
		// 헤드 입력.
		super.buildHeaders(worksheet, headers, 0, 0);

		// confirm Row offset
		int startRowIndex = 3;

		// 엑셀 바디의 셀 스타일 정의(문자 가운데 정렬).
		CellStyle styleHVCenter = ExcelUtil.getCenterStyle(worksheet);
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마).
		CellStyle styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);

		// 엑셀 바디 생성.
		List<BpmMthStcPrst> bpmMthStcPrstList = uncheckedCast(model.get("bpmResultSumData"));
		String[] merge = new String[4];
		int startRow = 3, lastRow = 3, startSerRow = 3, lastSerRow = 3, startIdxClCdValRow = 3, lastIdxClCdValRow = 3, startIdxCttCdValRow = 3, lastIdxCttCdValRow = 3;

		int MthMbrentStaSize = bpmMthStcPrstList.size();
		for (BpmMthStcPrst bpmMthStcPrst : bpmMthStcPrstList) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// 월
			ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, bpmMthStcPrst.getMthStcStrdYm());
			// Power Index
			ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_STRING, styleHVCenter, bpmMthStcPrst.getIdxClCdGrpNm());
			// Power Index
			ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_STRING, styleHVCenter, bpmMthStcPrst.getIdxClCdVal()
					.replaceAll("^[1-2]+", StringUtils.EMPTY));
			// Power Index
			ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_STRING, styleHVCenter, bpmMthStcPrst.getIdxCttCdVal());
			// 계
			ExcelUtil.createCell(row, 4, Cell.CELL_TYPE_NUMERIC, styleHVRight, bpmMthStcPrst.getMthStcRsltVal()
					.doubleValue());
			// 셀 병합처리
			if (StringUtils.equals(merge[0], bpmMthStcPrst.getMthStcStrdYm())) {
				lastRow++;
				if ((startRowIndex == MthMbrentStaSize + 3) && (startRow < lastRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
				}
			} else {
				if (startRow < lastRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
				}
				startRow = startRowIndex - 1;
				lastRow = startRowIndex - 1;
			}
			if (StringUtils.equals(merge[1], bpmMthStcPrst.getMthStcStrdYm() + bpmMthStcPrst.getIdxClCdGrpNm())) {
				lastSerRow++;
				if ((startRowIndex == MthMbrentStaSize + 3) && (startSerRow < lastSerRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startSerRow, lastSerRow, 1, 1));
				}
			} else {
				if (startSerRow < lastSerRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startSerRow, lastSerRow, 1, 1));
				}
				startSerRow = startRowIndex - 1;
				lastSerRow = startRowIndex - 1;
			}
			if (StringUtils.equals(merge[2], bpmMthStcPrst.getMthStcStrdYm() + bpmMthStcPrst.getIdxClCdGrpNm()
					+ bpmMthStcPrst.getIdxClCdVal())) {
				lastIdxClCdValRow++;
				if ((startRowIndex == MthMbrentStaSize + 3) && (startIdxClCdValRow < lastIdxClCdValRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startIdxClCdValRow, lastIdxClCdValRow, 2, 2));
				}
			} else {
				if (startIdxClCdValRow < lastIdxClCdValRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startIdxClCdValRow, lastIdxClCdValRow, 2, 2));
				}
				startIdxClCdValRow = startRowIndex - 1;
				lastIdxClCdValRow = startRowIndex - 1;
			}

			if (StringUtils.equals(bpmMthStcPrst.getIdxClCdVal().trim().replaceAll("^[1-2]+", StringUtils.EMPTY), bpmMthStcPrst
					.getIdxCttCdVal().trim())) {
				worksheet.addMergedRegion(new CellRangeAddress(startIdxClCdValRow, startIdxClCdValRow, 2, 3));
			}

			if (StringUtils.equals(merge[3], bpmMthStcPrst.getMthStcStrdYm() + bpmMthStcPrst.getIdxClCdGrpNm()
					+ bpmMthStcPrst.getIdxClCdVal() + bpmMthStcPrst.getIdxCttCdVal())) {
				lastIdxCttCdValRow++;
				if ((startRowIndex == MthMbrentStaSize + 3) && (startIdxCttCdValRow < lastIdxCttCdValRow)) {
					worksheet.addMergedRegion(new CellRangeAddress(startIdxCttCdValRow, lastIdxCttCdValRow, 3, 3));
				}
			} else {
				if (startIdxCttCdValRow < lastIdxCttCdValRow) {
					worksheet.addMergedRegion(new CellRangeAddress(startIdxCttCdValRow, lastIdxCttCdValRow, 3, 3));
				}
				startIdxCttCdValRow = startRowIndex - 1;
				lastIdxCttCdValRow = startRowIndex - 1;
			}
			merge[0] = bpmMthStcPrst.getMthStcStrdYm();
			merge[1] = bpmMthStcPrst.getMthStcStrdYm() + bpmMthStcPrst.getIdxClCdGrpNm();
			merge[2] = bpmMthStcPrst.getMthStcStrdYm() + bpmMthStcPrst.getIdxClCdGrpNm()
					+ bpmMthStcPrst.getIdxClCdVal();
			merge[3] = bpmMthStcPrst.getMthStcStrdYm() + bpmMthStcPrst.getIdxClCdGrpNm()
					+ bpmMthStcPrst.getIdxClCdVal() + bpmMthStcPrst.getIdxCttCdVal();
		}
		worksheet.addMergedRegion(new CellRangeAddress(2, // first row (0-based)
				2, // last row (0-based)
				1, // first column (0-based)
				3 // last column (0-based)
				));
	}
}
