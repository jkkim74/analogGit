package com.skplanet.web.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.web.exception.BizException;
import com.skplanet.web.model.AutoMap;

import lombok.extern.slf4j.Slf4j;
import skp.bss.msg.rms.front.HttpMessageApi;
import skp.bss.msg.rms.front.vo.MultiRequestVo;
import skp.bss.msg.rms.front.vo.ReceiveNumVo;

@Service
@Slf4j
public class SmsService {

	@Autowired
	private HttpMessageApi smsApi;

	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private OracleRepository oracleRepository;	

	@Value("${sms.serviceId}")
	private String serviceId;

	@Value("${sms.appKey}")
	private String appKey;

	@Value("${sms.sender}")
	private String sender;

	@Value("${app.enable.sms}")
	private boolean enabled;

	public void send(List<AutoMap> receivers) {
		log.info("Send to SMS");

		for (AutoMap m : receivers) {
			String extnctObjDt = (String) m.get("extnctObjDt");

			HashMap<String, Object> model = new HashMap<>();
			model.put("mbrKorNm", m.get("mbrKorNm"));
			model.put("mm", extnctObjDt.substring(4, 6));
			model.put("dd", extnctObjDt.substring(6, 8));

			String clphnNo = (String) m.get("clphnNo");
			String mbrId = (String) m.get("mbrId");
			String baseYm = ((String) m.get("baseYm")).replaceAll("-", "");
			
			

			send(Arrays.asList(clphnNo), "pan0104.vm", model,mbrId,baseYm);
		}
	}

	@Async
	public void send(List<String> receiverPhoneNumbers, String templateName, Map<String, Object> model, String mbrId, String baseYm) {
		if (!enabled) {
			log.debug("disabled");
			return;
		}

		MultiRequestVo multiRequest = new MultiRequestVo();
		multiRequest.setOcallPhonNum(sender); /* 발신전화번호 */

		String text = templateService.mergeTemplate("/templates/sms/" + templateName, model);
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
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("mbrId", mbrId);
					params.put("baseYm", baseYm);
					params.put("smsSndYn", "1");
					params.put("smsSndFgCd", resultVo.getResultCode());
					oracleRepository.updateSmsSendStatus(params);
				}
			} else {
				log.info("Failed to send using SMS  : "+ resultVo.getResultCode() + " : " + resultVo.getResultMessage());
				//throw new BizException(resultVo.getResultCode() + " : " + resultVo.getResultMessage());
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("mbrId", mbrId);
				params.put("baseYm", baseYm);
				params.put("smsSndYn", "2");
				params.put("smsSndFgCd", resultVo.getResultCode());
				oracleRepository.updateSmsSendStatus(params);
			}
		} catch (IOException e) {
			//throw new BizException("Failed to send using SMS", e);
			log.info("Failed to send using SMS  : "+ e.toString());
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
