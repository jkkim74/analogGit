package com.skplanet.ocb.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.skplanet.ocb.exception.BizException;
import com.skplanet.ocb.util.AutoMappedMap;

import lombok.extern.slf4j.Slf4j;
import skp.bss.msg.rms.front.HttpMessageApi;
import skp.bss.msg.rms.front.vo.MultiRequestVo;
import skp.bss.msg.rms.front.vo.ReceiveNumVo;

@Service
@SuppressWarnings("deprecation")
@Slf4j
public class SmsService {

	@Autowired
	private HttpMessageApi smsApi;

	@Autowired
	private VelocityEngine velocityEngine;

	@Value("${sms.serviceId}")
	private String serviceId;

	@Value("${sms.appKey}")
	private String appKey;

	@Value("${sms.sender}")
	private String sender;

	public void send(List<AutoMappedMap> receivers) {
		log.info("Send to SMS");
		
		for (AutoMappedMap m : receivers) {
			String extnctObjDt = (String) m.get("extnctObjDt");

			HashMap<String, Object> model = new HashMap<>();
			model.put("mbrKorNm", m.get("mbrKorNm"));
			model.put("mm", extnctObjDt.substring(4, 6));
			model.put("dd", extnctObjDt.substring(6, 8));

			String clphnNo = (String) m.get("clphnNo");

			send(Arrays.asList(clphnNo), "pan0104.vm", model);
		}
	}

	@Async
	public void send(List<String> receiverPhoneNumbers, String templateName, Map<String, Object> model) {
		MultiRequestVo multiRequest = new MultiRequestVo();
		multiRequest.setOcallPhonNum(sender); /* 발신전화번호 */

		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "/templates/sms/" + templateName,
				model);
		multiRequest.setMsgPhrs(text); /* 메시지문구 */

		List<ReceiveNumVo> receiveNumList = new ArrayList<>();

		for (int i = 0, n = receiverPhoneNumbers.size(); i < n; i++) {
			ReceiveNumVo receiveNumVo = new ReceiveNumVo();
			receiveNumVo.setReqNum(String.valueOf(i));
			receiveNumVo.setRcvPhonNum(receiverPhoneNumbers.get(i));
			receiveNumList.add(receiveNumVo);
		}

		multiRequest.setRcvPhonNumList(receiveNumList);

		try {
			MultiRequestVo resultVo = smsApi.sendMmsMultiRequest(serviceId, appKey, multiRequest, null);

			if ("0".equals(resultVo.getResultCode())) {
				for (ReceiveNumVo rcvNum : resultVo.getRcvPhonNumList()) {
					log.debug("##### ReqNum={}, RcvPhonNum={}, TransactionId={}", rcvNum.getReqNum(),
							rcvNum.getRcvPhonNum(), rcvNum.getTransactionId());
				}
			} else {
				throw new BizException(resultVo.getResultCode() + " : " + resultVo.getResultMessage());
			}
		} catch (IOException e) {
			throw new BizException("Failed to send SMS", e);
		}
	}

	public void getResult(String transactionId) {
		MultiRequestVo vo = new MultiRequestVo();
		vo.setTransactionId(transactionId);

		try {
			MultiRequestVo resultVo = smsApi.sendSmsResultRequest(serviceId, appKey, vo);

			log.debug("##### getTransactionId()={}, getResultCode()={}, getResultMessage()={}",
					resultVo.getTransactionId(), resultVo.getResultCode(), resultVo.getResultMessage());

		} catch (IOException e) {
			throw new BizException("Failed to get the result of SMS", e);
		}

	}
}
