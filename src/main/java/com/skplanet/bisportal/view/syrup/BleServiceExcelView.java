package com.skplanet.bisportal.view.syrup;

import com.skplanet.bisportal.common.excel.BleCellSelector;
import com.skplanet.bisportal.common.excel.BleHeaderSelector;
import com.skplanet.bisportal.common.excel.BleMergeSelector;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.MultiHeader;
import com.skplanet.bisportal.model.ocb.BleNewTech;
import com.skplanet.bisportal.view.AbstractExcelView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.skplanet.bisportal.common.utils.Objects.uncheckedCast;

/**
 * The BleServiceExcelView class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Slf4j
public class BleServiceExcelView extends AbstractExcelView {

	/**
	 * 포인트 선물 합산하기 엑셀 저장.
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
		String menuName = "Ble 서비스 통계";
		Sheet worksheet = workbook.createSheet(menuName);
		JqGridRequest jqGridRequest = (JqGridRequest) model.get("jqGridRequest");
		BleHeaderSelector headerSelector = injector.getInstance(BleHeaderSelector.class);
		MultiHeader multiHeader = headerSelector.getBleHeader(jqGridRequest.getServiceTypeCode(),
				jqGridRequest.getStatContents());
		int headLength = multiHeader.getHeader3().length;

		// 칼럼 넓이 조정.
		super.setColumnWith(worksheet, headLength);

		// 타이틀 입력.
		super.buildTitle(worksheet, menuName, headLength);

		// 헤드 입력.
		BleMergeSelector mergeSelector = injector.getInstance(BleMergeSelector.class);
		mergeSelector.getBleCell(jqGridRequest.getServiceTypeCode(), worksheet, multiHeader,
				jqGridRequest.getStatContents());

		// 엑셀 바디 생성.
		List<BleNewTech> bleNewTechs = uncheckedCast(model.get("bleServiceData"));

		if (CollectionUtils.isNotEmpty(bleNewTechs)) {
			BleCellSelector cellSelector = injector.getInstance(BleCellSelector.class);
			cellSelector.getBleCell(jqGridRequest.getServiceTypeCode(), bleNewTechs, workbook, worksheet,
					jqGridRequest.getStatContents());
		}
	}
}
