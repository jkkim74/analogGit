package com.skplanet.pandora.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.skplanet.pandora.common.BizException;
import com.skplanet.pandora.repository.mysql.MysqlRepository;

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

	@Autowired
	private MysqlRepository mysqlRepository;

	@Value("${sms.serviceId}")
	private String serviceId;

	@Value("${sms.appKey}")
	private String appKey;

	@Value("${sms.sender}")
	private String sender;

	@Async
	public void send(String[] receivers, String templateName, Map<String, Object> model) {
		MultiRequestVo multiRequest = new MultiRequestVo();
		multiRequest.setOcallPhonNum(sender); /* 발신전화번호 */

		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "/templates/sms/" + templateName,
				model);
		multiRequest.setMsgPhrs(text); /* 메시지문구 */

		List<ReceiveNumVo> receiveNumList = new ArrayList<>();

		for (int i = 0, n = receivers.length; i < n; i++) {
			ReceiveNumVo receiveNumVo = new ReceiveNumVo();
			receiveNumVo.setReqNum(String.valueOf(i));
			receiveNumVo.setRcvPhonNum(receivers[i]);
			receiveNumList.add(receiveNumVo);
		}

		multiRequest.setRcvPhonNumList(receiveNumList);

		try {
			MultiRequestVo resultVo = smsApi.sendMmsMultiRequest(serviceId, appKey, multiRequest, null);

			if ("0".equals(resultVo.getResultCode())) {
				for (ReceiveNumVo rcvNum : resultVo.getRcvPhonNumList()) {
					log.debug("##### ReqNum={}, RcvPhonNum={}, TransactionId={}", rcvNum.getReqNum(),
							rcvNum.getRcvPhonNum(), rcvNum.getTransactionId());

					// mysqlRepository.upsertSubmissionResult(SubmissionType.SMS);
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
