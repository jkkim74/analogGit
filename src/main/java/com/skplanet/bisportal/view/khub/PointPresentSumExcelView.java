package com.skplanet.bisportal.view.khub;

import com.skplanet.bisportal.common.excel.PoiHelper;
import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.ocb.ObsPresntSumRpt;
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
public class PointPresentSumExcelView extends AbstractExcelView {
	/**
	 * 포인트 선물 합산하기 엑셀 저장.
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
		String menuName = "포인트 선물 합산하기";
		Sheet worksheet = workbook.createSheet(menuName);
		String headers[][] = {
				{ "기준일자", "선물하기 선물한 고객기준", "선물하기 선물받은 고객기준", "합산하기(같이쓰기)" },
				{ "기준일자", "선물하기 선물한 고객기준", "선물하기 선물받은 고객기준", "전체 고객수", "본인 point 사용실적", "타인 point 사용실적" },
				{ "기준일자", "누적고객수(Unique)", "고객수", "선물TR건수", "선물point", "선물누적포인트", "누적고객수(Unique)", "고객수", "선물받은TR건수",
						"선물받은point", "합산하기가입신청고객수", "합산하기유실적고객수", "합산하기유실적누적고객수", "합산하기사용누적고객수", "합산하기사용고객수",
						"합산하기로사용한TR건수", "합산하기로사용한Point", "누적포인트", "합산하기사용누적고객수", "합산하기사용고객수", "합산하기로사용한TR건수",
						"합산하기로사용한Point", "누적포인트" } };
		int headLength = headers[2].length;
		// 칼럼 넓이 조정.
		super.setColumnWith(worksheet, headLength);
		// 타이틀 입력.
		super.buildTitle(worksheet, menuName, headLength);
		// 헤드 입력.
		PoiHelper.buildHeaderForPointPresentSum(worksheet, headers);

		// 엑셀 바디 생성.
		List<ObsPresntSumRpt> obsPresntSumRpts = uncheckedCast(model.get("pointPresentSumData"));
		if (CollectionUtils.isNotEmpty(obsPresntSumRpts))
			this.setCellValue(obsPresntSumRpts, workbook, worksheet);
	}

	private void setCellValue(List<ObsPresntSumRpt> obsPresntSumRpts, Workbook workbook, Sheet worksheet)
			throws Exception {
		// Row offset
		int startRowIndex = 5;
		// 엑셀 바디의 셀 스타일 정의(문자 가운데 정렬).
		CellStyle styleHVCenter = ExcelUtil.getCenterStyle(worksheet);
		// 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마).
		CellStyle styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);
		for (ObsPresntSumRpt obsPresntSumRpt : obsPresntSumRpts) {
			// Create a new row
			Row row = worksheet.createRow(startRowIndex++);
			// Retrieve the 기준일자 value
			ExcelUtil.createCell(row, 0, Cell.CELL_TYPE_STRING, styleHVCenter, obsPresntSumRpt.getStndrdDt());
			// Retrieve the 누적고객수(Unique) value
			ExcelUtil.createCell(row, 1, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getPresntSndAcmCustCnt()
					.doubleValue());
			// Retrieve the 고객수 value
			ExcelUtil.createCell(row, 2, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getPresntSndCustCnt()
					.doubleValue());
			// Retrieve the 선물TR건수 value
			ExcelUtil.createCell(row, 3, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getPresntSndTrCnt()
					.doubleValue());
			// Retrieve the 선물point value
			ExcelUtil.createCell(row, 4, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getPresntSndPntSum()
					.doubleValue());
			// Retrieve the 선물누적포인트 value
			ExcelUtil.createCell(row, 5, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getPresntSndAcmPntSum()
					.doubleValue());
			// Retrieve the 누적고객수(Unique) value
			ExcelUtil.createCell(row, 6, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getPresntRcvAcmCustCnt()
					.doubleValue());
			// Retrieve the 고객수 value
			ExcelUtil.createCell(row, 7, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getPresntRcvCustCnt()
					.doubleValue());
			// Retrieve the 선물받은TR건수 value
			ExcelUtil.createCell(row, 8, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getPresntRcvTrCnt()
					.doubleValue());
			// Retrieve the 선물받은point value
			ExcelUtil.createCell(row, 9, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getPresntRcvPntSum()
					.doubleValue());
			// Retrieve the 합산하기가입신청고객수 value
			ExcelUtil.createCell(row, 10, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getEntrRqstCustCnt()
					.doubleValue());
			// Retrieve the 합산하기유실적고객수 value
			ExcelUtil.createCell(row, 11, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getYachvCustCnt()
					.doubleValue());
			// Retrieve the 합산하기유실적누적고객수 value
			ExcelUtil.createCell(row, 12, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getYachvAcmCustCnt()
					.doubleValue());
			// Retrieve the 합산하기사용누적고객수 value
			ExcelUtil.createCell(row, 13, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt
					.getOwnPntUseAcmCustCnt().doubleValue());
			// Retrieve the 합산하기사용고객수 value
			ExcelUtil.createCell(row, 14, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getOwnPntUseCustCnt()
					.doubleValue());
			// Retrieve the 합산하기로사용한TR건수 value
			ExcelUtil.createCell(row, 15, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getOwnPntUseTrCnt()
					.doubleValue());
			// Retrieve the 합산하기로사용한Point value
			ExcelUtil.createCell(row, 16, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getOwnPntUsePntSum()
					.doubleValue());
			// Retrieve the 누적포인트 value
			ExcelUtil.createCell(row, 17, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getOwnPntUseAcmPntSum()
					.doubleValue());
			// Retrieve the 합산하기사용누적고객수 value
			ExcelUtil.createCell(row, 18, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt
					.getOthPntUseAcmCustCnt().doubleValue());
			// Retrieve the 합산하기사용고객수 value
			ExcelUtil.createCell(row, 19, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getOthPntUseCustCnt()
					.doubleValue());
			// Retrieve the 합산하기로사용한TR건수 value
			ExcelUtil.createCell(row, 20, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getOthPntUseTrCnt()
					.doubleValue());
			// Retrieve the 합산하기로사용한Point value
			ExcelUtil.createCell(row, 21, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getOthPntUsePntSum()
					.doubleValue());
			// Retrieve the 누적포인트 value
			ExcelUtil.createCell(row, 22, Cell.CELL_TYPE_NUMERIC, styleHVRight, obsPresntSumRpt.getOthPntUseAcmPntSum()
					.doubleValue());
		}
	}
}
