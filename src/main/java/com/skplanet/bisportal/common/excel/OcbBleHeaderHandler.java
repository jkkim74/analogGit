package com.skplanet.bisportal.common.excel;

import com.skplanet.bisportal.common.model.MultiHeader;
import com.skplanet.bisportal.common.utils.ArrayUtil;
import org.apache.commons.lang.StringUtils;

/**
 * The OcbBleHeaderHandler(OCB BLE 동적 헤더 생성 처리) class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
public class OcbBleHeaderHandler implements BleHeaderHandler {
	@Override
	public MultiHeader buildHeader(String statContents) throws Exception {
		String header1[] = { "기준일자", "가맹점명", "MID", "BLE Triggering 항목", "BLE Triggering 항목" };
		String header2[] = { "기준일자", "가맹점명", "MID", "OCB/Syrup 서버요청", "OCB/Syrup 서버요청" };
		String header3[] = { "기준일자", "가맹점명", "MID", "PV", "UV" };
		MultiHeader multiHeader = new MultiHeader();
		if (StringUtils.indexOf(statContents, "1") > -1) {// 통계 항목 전체 선택
			multiHeader.setHeader1(ArrayUtil.addAll(header1, "BLE 수신항목", "BLE 수신항목", "BLE 수신항목", "BLE 수신항목",
					"BLE 클릭항목", "BLE 클릭항목",
					"BLE 열람항목", "BLE 열람항목", "BLE 열람항목", "BLE 열람항목"));
			multiHeader.setHeader2(ArrayUtil.addAll(header2, "OCB 체크인", "OCB 체크인", "OCB 전단", "OCB 전단",
					"OCB 전단", "OCB 전단",
					"OCB 전단", "OCB 전단", "OCB 체크인", "OCB 체크인"));
			multiHeader.setHeader3(ArrayUtil.addAll(header3, "PV", "UV", "PV", "UV",
					"PV", "UV",
					"PV", "UV", "PV", "UV"));
		} else {
			multiHeader.setHeader1(header1);
			multiHeader.setHeader2(header2);
			multiHeader.setHeader3(header3);
			if (StringUtils.indexOf(statContents, "3") > -1) {
				multiHeader.setHeader1(ArrayUtil.addAll(multiHeader.getHeader1(), "BLE 수신항목", "BLE 수신항목", "BLE 수신항목", "BLE 수신항목"));
				multiHeader.setHeader2(ArrayUtil.addAll(multiHeader.getHeader2(), "OCB 체크인", "OCB 체크인", "OCB 전단", "OCB 전단"));
				multiHeader.setHeader3(ArrayUtil.addAll(multiHeader.getHeader3(), "PV", "UV", "PV", "UV"));
			}
			if (StringUtils.indexOf(statContents, "4") > -1) {
				multiHeader.setHeader1(
						ArrayUtil.addAll(multiHeader.getHeader1(), "BLE 클릭항목", "BLE 클릭항목"));
				multiHeader.setHeader2(ArrayUtil.addAll(multiHeader.getHeader2(), "OCB 전단", "OCB 전단"));
				multiHeader.setHeader3(ArrayUtil.addAll(multiHeader.getHeader3(), "PV", "UV"));
			}
			if (StringUtils.indexOf(statContents, "5") > -1) {
				multiHeader.setHeader1(ArrayUtil.addAll(multiHeader.getHeader1(), "BLE 열람항목", "BLE 열람항목", "BLE 열람항목", "BLE 열람항목"));
				multiHeader.setHeader2(ArrayUtil.addAll(multiHeader.getHeader2(), "OCB 전단", "OCB 전단", "OCB 체크인", "OCB 체크인"));
				multiHeader.setHeader3(ArrayUtil.addAll(multiHeader.getHeader3(), "PV", "UV", "PV", "UV"));
			}
		}
		return multiHeader;
	}
}
