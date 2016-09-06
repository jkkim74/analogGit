package com.skplanet.pandora.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skplanet.pandora.model.AutoMappedMap;
import com.skplanet.pandora.model.Member;
import com.skplanet.pandora.model.UploadProgress;
import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.pandora.repository.querycache.QueryCacheRepository;
import com.skplanet.pandora.service.UploadService;

@RestController
@RequestMapping("api")
public class ApiController {

	@Autowired
	private OracleRepository oracleRepository;

	@Autowired
	private QueryCacheRepository querycacheRepository;

	@Autowired
	private UploadService uploadService;

	@RequestMapping(method = RequestMethod.GET, value = "/members")
	public List<Member> getMergedMember(@RequestParam String pageId, @RequestParam String username,
			@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "20") int limit) {

		UploadProgress uploadProgress = uploadService.getFinishedUploadProgress(pageId, username);
		return oracleRepository.selectMembers(uploadProgress, offset, limit);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/memberInfo")
	public List<AutoMappedMap> getMemberInfo(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectMemberInfo(params);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/agreementInfo")
	public List<AutoMappedMap> getAgreementInfo(@RequestParam Map<String, Object> params) {

		String mbrId = oracleRepository.selectMbrId(params);
		return querycacheRepository.selectAgreementInfo(mbrId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/joinInfoOcbapp")
	public List<AutoMappedMap> getJoinInfoOcbapp(@RequestParam Map<String, Object> params) {

		String mbrId = oracleRepository.selectMbrId(params);
		List<AutoMappedMap> result = querycacheRepository.selectJoinInfo(mbrId);

		if (result.isEmpty()) {
			AutoMappedMap m = new AutoMappedMap();
			m.put("ocbapp_Yn", "N");
			result.add(m);
		} else {
			AutoMappedMap m = result.get(0);
			m.put("ocbapp_Yn", "Y");
		}

		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/joinInfoOcbcom")
	public List<AutoMappedMap> getJoinInfoOcbcom(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectJoinInfo(params);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/lastestUsageInfo")
	public List<AutoMappedMap> getLastestUsageInfo(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectLastestUsageInfo(params);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/marketingMemberInfo")
	public List<AutoMappedMap> getMarketingMemberInfo(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectMarketingMemberInfo(params);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/marketingMemberInfoHistory")
	public List<AutoMappedMap> getMarketingMemberInfoHistory(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectMarketingMemberInfoHistory(params);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/thirdPartyProvideHistory")
	public List<AutoMappedMap> getThirdPartyProvideHistory(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectThirdPartyProvideHistory(params);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/cardList")
	public List<AutoMappedMap> getCardList(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectCardList(params);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/transactionHistory")
	public List<AutoMappedMap> getTransactionHistory(@RequestParam Map<String, Object> params) {

		String mbrId = oracleRepository.selectMbrId(params);
		return querycacheRepository.selectTransactionHistory(mbrId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/emailSendHistory")
	public List<AutoMappedMap> getEmailSendHistory(@RequestParam Map<String, Object> params) {

		String mbrId = oracleRepository.selectMbrId(params);
		return querycacheRepository.selectEmailSendHistory(mbrId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/appPushHistory")
	public List<AutoMappedMap> getAppPushHistory(@RequestParam Map<String, Object> params) {

		String mbrId = oracleRepository.selectMbrId(params);
		return querycacheRepository.selectAppPushHistory(mbrId);
	}

}
