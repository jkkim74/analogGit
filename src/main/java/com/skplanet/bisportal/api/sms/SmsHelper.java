package com.skplanet.bisportal.api.sms;

import java.net.URL;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.ArrayUtils;

import org.apache.commons.lang.StringUtils;
import skp.bss.msg.rms.front.HttpConnectionManager;
import skp.bss.msg.rms.front.HttpMessageApi;
import skp.bss.msg.rms.front.vo.MultiRequestVo;
import skp.bss.msg.rms.front.vo.ReceiveNumVo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import com.skplanet.bisportal.common.exception.RemoteAccessException;
import com.skplanet.bisportal.common.utils.PropertiesUtil;

/**
 * SmsHelper SMS/MMS 발송 클래스.
 *
 * @author HO-JIN, HA (mimul@wiseeco.com)
 */
@Singleton
@Slf4j
public class SmsHelper {
	private URL MSG_URL;
	private String API_MMS = "MMS";
	private String MMS_SERVICE_ID;
	private String MMS_APP_KEY;
	private Map<String, HttpMessageApi> apiPool = Maps.newHashMap();
	private HttpMessageApi msgApi = new HttpMessageApi();

	public SmsHelper() {
		try {
			MSG_URL = new URL(PropertiesUtil.getProperty("mms.msg.url"));
			MMS_SERVICE_ID = PropertiesUtil.getProperty("mms.service.id");
			MMS_APP_KEY = PropertiesUtil.getProperty("mms.app.key");
		} catch (Exception e) {
			log.error("SmsHelper {}", e);
			throw new RemoteAccessException("SmsHelper Initialization Failed", e);
		}
	}

	public MultiRequestVo sendMMS(String[] receives, String msg) throws Exception {
		if (ArrayUtils.isEmpty(receives))
			return null;

		MultiRequestVo multiRequest = new MultiRequestVo();
		multiRequest.setMsgTypCd(API_MMS); /* 메시지유형코드 SMS, MMS */
		multiRequest.setOcallPhonNum("01050042015"); /* 발신전화번호 */
		multiRequest.setMsgPhrs(msg); /* 메시지문구 */

		List<ReceiveNumVo> receiveNums = Lists.newArrayList();
		int receiveLength = receives.length;
		for (int i = 0; i < receiveLength; i++) {
			ReceiveNumVo receiveNum = new ReceiveNumVo();
			receiveNum.setReqNum(StringUtils.EMPTY + i);
			receiveNum.setRcvPhonNum(receives[i]);
			receiveNums.add(receiveNum);
		}
		multiRequest.setRcvPhonNumList(receiveNums);
		msgApi.setConnectionManager(new HttpConnectionManager(MSG_URL, MMS_SERVICE_ID, MMS_APP_KEY));
		apiPool.put(API_MMS, msgApi);
		log.info("multiRequest {} {} {}", MSG_URL, MMS_SERVICE_ID, MMS_APP_KEY);
		multiRequest = apiPool.get(API_MMS).sendMmsMultiRequest(MMS_SERVICE_ID, MMS_APP_KEY, multiRequest, null);
		return multiRequest;
	}
}
