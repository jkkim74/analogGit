package com.skplanet.bisportal.view.tmap;

import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.tmap.TmapDayKpi;
import com.skplanet.bisportal.view.AbstractExcelView;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.skplanet.bisportal.common.utils.Objects.uncheckedCast;

/**
 * The DayKpiExcelView class.
 * 
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 * 
 */
public class DayKpiExcelView extends AbstractExcelView {
	/**
	 * 일별 KPI 관리 엑셀 저장.
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
		String menuName = "일별 KPI 관리";
		Sheet worksheet = workbook.createSheet(menuName);
		String headers[][] = { { "기준일자", "요일", "명절", "실적", "달성율(%)", "일증가량", "목표치" ,"목표치대비실적", "목표치대비실적비율", "예측치", "전월동일수치" ,"전월동일수치대비실적", "전월동일대비", "일UV" } };
		int headLength = headers[0].length;
		// 칼럼 넓이 조정.
		super.setColumnWith(worksheet, headLength);
		// 타이틀 입력.
		super.buildTitle(worksheet, menuName, headLength);
		// 헤드 입력.
		super.buildHeaders(worksheet, headers, 0, 0);
		// Row offset
		int startRowIndex = 3;

		// 엑셀 바디의 셀 스타일 정의(문자 가운데 정렬).
		CellStyle styleHVCenter = ExcelUtil.getCenterStyle(worksheet);
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마).
		CellStyle styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마, 소수2자리).
		CellStyle styleHVRightComma = ExcelUtil.getRightFormatStyle(workbook, worksheet);

		// 엑셀 바디 생성.
		List<TmapDayKpi> tmapDayKpis = uncheckedCast(model.get("dayKpiData"));
		for (TmapDayKpi tmapDayKpi : tmapDayKpis) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// Retrieve the 기준일자 value
			ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, tmapDayKpi.getStrdDt());

			// Retrieve the 요일 value
			ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_STRING, styleHVCenter, tmapDayKpi.getCdNm());

			// Retrieve the 명절 value
			ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_STRING, styleHVCenter, tmapDayKpi.getHdayNm());

			// Retrieve the 실적 value
			ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_NUMERIC, styleHVRight, tmapDayKpi.getRsltUv().doubleValue());

			// Retrieve the 달성율(%) value
			ExcelUtil.createCell(row, 4, Cell.CELL_TYPE_NUMERIC, styleHVRightComma, tmapDayKpi.getUvAchivRt().doubleValue());

			// Retrieve the 일증가량 value
			ExcelUtil.createCell(row, 5, Cell.CELL_TYPE_NUMERIC, styleHVRight, tmapDayKpi.getDayIncre().doubleValue());

			// Retrieve the 목표치 value
			ExcelUtil.createCell(row, 6, Cell.CELL_TYPE_NUMERIC, styleHVRight, tmapDayKpi.getTrgtUv().doubleValue());

			// Retrieve the 목표치대비실적 value
			ExcelUtil.createCell(row, 7, Cell.CELL_TYPE_NUMERIC, styleHVRight, tmapDayKpi.getDifTrgtRslt().doubleValue());

			// Retrieve the 목표치대비실적비율 value
			ExcelUtil.createCell(row, 8, Cell.CELL_TYPE_NUMERIC, styleHVRightComma, tmapDayKpi.getDifTrgtRsltRt().doubleValue());

			// Retrieve the 예측치 value
			ExcelUtil.createCell(row, 9, Cell.CELL_TYPE_NUMERIC, styleHVRight, tmapDayKpi.getEstiUv().doubleValue());

			// Retrieve the 전월동일수치 value
			ExcelUtil.createCell(row, 10, Cell.CELL_TYPE_NUMERIC, styleHVRight, tmapDayKpi.getUv().doubleValue());

			// Retrieve the 전월동일수치대비실적 value
			ExcelUtil.createCell(row, 11, Cell.CELL_TYPE_NUMERIC, styleHVRight, tmapDayKpi.getDifUv().doubleValue());

			// Retrieve the 전월동일대비 value
			ExcelUtil.createCell(row, 12, Cell.CELL_TYPE_NUMERIC, styleHVRightComma, tmapDayKpi.getDifUvRt().doubleValue());

			// Retrieve the 일UV value
			ExcelUtil.createCell(row, 13, Cell.CELL_TYPE_NUMERIC, styleHVRight, tmapDayKpi.getDayUv().doubleValue());
		}
	}
}
