package com.skplanet.bisportal.view.khub;

import com.skplanet.bisportal.common.excel.PoiHelper;
import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.ocb.ObsNewOcbSegRpt;
import com.skplanet.bisportal.view.AbstractExcelView;
import org.apache.commons.collections.CollectionUtils;
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
 * The VisitOutlineExcelView class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
public class NewOcbSeqExcelView extends AbstractExcelView {
	/**
	 * New Ocb Seq 엑셀 저장.
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
		String menuName = "New OCB Seg";
		Sheet worksheet = workbook.createSheet(menuName);
		String headers[][] = { { "행레이블", "전체TR기준", "적립TR기준", "사용TR기준" },
				{ "행레이블", "전체고객수", "전체TR", "전체P/F", "적립고객수", "적립TR", "적립포인트", "사용고객수", "사용TR", "사용포인트" } };
		int headLength = headers[1].length;

		// 칼럼 넓이 조정.
		super.setColumnWith(worksheet, headLength);
		// 타이틀 입력.
		super.buildTitle(worksheet, menuName, headLength);
		// 헤드 입력.
		PoiHelper.buildHeaderForNewOcbSeq(worksheet, headers);
		// 엑셀 바디 생성.
		List<ObsNewOcbSegRpt> obsNewOcbSegRpts = uncheckedCast(model.get("newOcbSeqData"));
		if (CollectionUtils.isNotEmpty(obsNewOcbSegRpts))
			this.setCellValue(obsNewOcbSegRpts, workbook, worksheet);
	}

	private void setCellValue(List<ObsNewOcbSegRpt> obsNewOcbSegRpts, Workbook workbook, Sheet worksheet)
			throws Exception {
		// Row offset
		int startRowIndex = 4;
		// 엑셀 바디의 셀 스타일 정의(문자 가운데 정렬).
		CellStyle styleHVCenter = ExcelUtil.getCenterStyle(worksheet);
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마).
		CellStyle styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);
		for (ObsNewOcbSegRpt obsNewOcbSegRpt : obsNewOcbSegRpts) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// Retrieve the 행레이블 value
			ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, obsNewOcbSegRpt.getRptIndNm());

			// Retrieve the 전체고객수 value
			ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsNewOcbSegRpt.getTotCustCnt()
					.doubleValue());

			// Retrieve the 전체TR value
			ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsNewOcbSegRpt.getTotTrCnt()
					.doubleValue());

			// Retrieve the 전체P/F value
			ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsNewOcbSegRpt.getTotPfSum()
					.doubleValue());

			// Retrieve the 적립고객수 value
			ExcelUtil.createCell(row, 4, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsNewOcbSegRpt.getRsrvCustCnt()
					.doubleValue());

			// Retrieve the 적립TR value
			ExcelUtil.createCell(row, 5, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsNewOcbSegRpt.getRsrvTrCnt()
					.doubleValue());

			// Retrieve the 적립포인트 value
			ExcelUtil.createCell(row, 6, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsNewOcbSegRpt.getRsrvPntSum()
					.doubleValue());

			// Retrieve the 사용고객수 value
			ExcelUtil.createCell(row, 7, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsNewOcbSegRpt.getUseCustCnt()
					.doubleValue());

			// Retrieve the 사용TR value
			ExcelUtil.createCell(row, 8, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsNewOcbSegRpt.getUseTrCnt()
					.doubleValue());

			// Retrieve the 사용포인트 value
			ExcelUtil.createCell(row, 9, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsNewOcbSegRpt.getUsePntSum()
					.doubleValue());
		}
	}
}
