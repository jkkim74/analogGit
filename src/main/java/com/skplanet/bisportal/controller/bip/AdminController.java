package com.skplanet.bisportal.controller.bip;

import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.common.model.AjaxResult;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.model.acl.BipUser;
import com.skplanet.bisportal.model.bip.*;
import com.skplanet.bisportal.service.bip.AdminService;
import com.skplanet.bisportal.service.ocb.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

/**
 * The AdminController class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController extends ReportController {
	@Autowired
	private AdminService adminServiceImpl;

	@Autowired
	private CustomerService customerServiceImpl;

	/**
	 * 배치 관리자 정보 조회
	 * 
	 * @return
	 */
	@RequestMapping(value = "/boss/batchInfo", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<BpmMgntRsltBatInfoRgst> getAllBpmMgntRsltBatInfoRgst() throws Exception {
		JqGridResponse<BpmMgntRsltBatInfoRgst> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(adminServiceImpl.getAllBpmMgntRsltBatInfoRgst());
		return jqGridResponse;
	}

	/**
	 * 배치 모니터링 일별 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/boss/batchMonitoring", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<BpmMgntRsltBatOpPrst> getBpmMgntRsltBatOpPrst(JqGridRequest jqGridRequest) throws Exception {
		JqGridResponse<BpmMgntRsltBatOpPrst> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(adminServiceImpl.getBpmMgntRsltBatOpPrst(jqGridRequest.getBasicDate()));
		return jqGridResponse;
	}

	/**
	 * Tmap 일/주/월 실적 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/boss/handInputTmap", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<HandInput> getHandInputTmap(JqGridRequest jqGridRequest) throws Exception {
		JqGridResponse<HandInput> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(adminServiceImpl.getTMapCttMappInfo(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * Tmap 일/주/월 실적 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/boss/handInputSyrup", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<HandInput> getHandInputSyrup(JqGridRequest jqGridRequest) throws Exception {
		JqGridResponse<HandInput> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(adminServiceImpl.getSyrupCttMappInfo(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * Tmap 배치처리중인지 체크.
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/boss/checkBatchProcessing", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult checkBatchProcessing(JqGridRequest jqGridRequest) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			Integer count = adminServiceImpl.getBatchJobCheck(Integer.parseInt(jqGridRequest.getSvcId()));
			if (count > 0) {
				ajaxResult.setCode(999);
				ajaxResult.setMessage("fail");
			} else {
				ajaxResult.setCode(200);
				ajaxResult.setMessage("success");
			}
		} catch (Exception e) {
			log.error("checkBatchProcessing {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("fail");
		}
		return ajaxResult;
	}

	/**
	 * Tmap 실적 수기 입력 처리.
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/boss/createTmapGridData", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult createTmapGridData(@RequestBody JqGridRequest jqGridRequest) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		String trmsFlNm;
		try {
			if (isDay(jqGridRequest.getDateType())) {
				trmsFlNm = adminServiceImpl.createTmapCttMappInfoPerDay(jqGridRequest);
			} else if (isWeek(jqGridRequest.getDateType())) {
				trmsFlNm = adminServiceImpl.createTmapCttMappInfoPerWeek(jqGridRequest);
			} else {
				trmsFlNm = adminServiceImpl.createTmapCttMappInfoPerMonth(jqGridRequest);
			}
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
			adminServiceImpl.runBatch(trmsFlNm);
		} catch (Exception e) {
			log.error("createTmapGridData {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * Syrup 실적 수기 입력 처리.
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/boss/createSyrupGridData", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult createSyrupGridData(@RequestBody JqGridRequest jqGridRequest) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		String trmsFlNm;
		try {
			if (isDay(jqGridRequest.getDateType())) {
				trmsFlNm = adminServiceImpl.createSyrupCttMappInfoPerDay(jqGridRequest);
			} else {
				trmsFlNm = adminServiceImpl.createSyrupCttMappInfoPerMonth(jqGridRequest);
			}
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
			adminServiceImpl.runBatch(trmsFlNm);
		} catch (Exception e) {
			log.error("createSyrupGridData {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * 조직 목록을 조회.
	 * 
	 * @return List<OrgUser>
	 */
	@RequestMapping(value = "/boss/orgTrees", method = RequestMethod.POST)
	@ResponseBody
	public List<OrgUser> getOrgTrees() throws Exception {
		return adminServiceImpl.getOrgTree();
	}

	/**
	 * 조직에 속한 사용자를 조회.
	 * 
	 * @return List<OrgUser>
	 */
	@RequestMapping(value = "/boss/orgUserTrees", method = RequestMethod.GET)
	@ResponseBody
	public List<OrgUser> getOrgUserTrees(OrgUser orgUser) throws Exception {
		return adminServiceImpl.getOrgUserTree(orgUser);
	}

	/**
	 * 사용자 이름으로 조직 및 사용자 정보 조회.
	 *
	 * @return List<OrgUser>
	 */
	@RequestMapping(value = "/boss/orgUsers", method = RequestMethod.POST)
	@ResponseBody
	public List<OrgUser> getOrgUsers(@RequestBody OrgUser orgUser) throws Exception {
		if (StringUtils.isNotEmpty(orgUser.getSearchCondition()) && StringUtils.isNotEmpty(orgUser.getSearchKeyword()))
			return adminServiceImpl.getOrgUserTree(orgUser);
		else
			return null;
	}

	/**
	 * 경영실적 이메일 발송 대상자 조회.
	 * 
	 * @return JqGridResponse<OrgUser>
	 */
	@RequestMapping(value = "/boss/emailUsers", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<OrgUser> getEmailOrgUsers(BpmMgntEmailSndUser bpmMgntEmailSndUser) throws Exception {
		JqGridResponse<OrgUser> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(adminServiceImpl.getEmailOrgUser(bpmMgntEmailSndUser.getSndObjId()));
		return jqGridResponse;
	}

	/**
	 * 경영실적 이메일 데이터 조회(KID 지표).
	 *
	 * @return List<MailReport>
	 */
	@RequestMapping(value = "/boss/businessDatas", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getBusinessDatasForKid(JqGridRequest jqGridRequest) throws Exception {
		MailReports mailReports = adminServiceImpl.getBusinessDatasForKid(jqGridRequest);
		mailReports.setYesterday(jqGridRequest.getBasicDate());
		return adminServiceImpl.getVelocityMapForKid(mailReports);
	}

	/**
	 * 경영실적 메일 코멘트 등록
	 */
	@RequestMapping(value = "/boss/createMailComments", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult createMailComments(@RequestBody BpmMgntRsltScrnCmnt bpmMgntRsltScrnCmnt) {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			adminServiceImpl.createBpmMgntRsltScrnCmnt(bpmMgntRsltScrnCmnt);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * 경영실적 메일링 대상자 삭제 처리.
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/boss/deleteMailUsers", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult delMailUsers(@RequestBody JqGridRequest jqGridRequest) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			adminServiceImpl.deleteBpmMgntEmailSndUser(jqGridRequest);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("delMailUsers {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * 경영실적 메일링 대상자 추가 처리.
	 * 
	 * @param bpmMgntEmailSndUser
	 * @return
	 */
	@RequestMapping(value = "/boss/createMailUsers", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult createMailUsers(@RequestBody BpmMgntEmailSndUser bpmMgntEmailSndUser) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			adminServiceImpl.createBpmMgntEmailSndUser(bpmMgntEmailSndUser);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("createMailUsers {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * 경영실적 메일 발송 내용 등록(KID 지표).
	 */
	@RequestMapping(value = "/boss/mailContentsForKid", method = RequestMethod.POST)
	@ResponseBody
	public BpmMgntEmailSndCtt getMailContentsForKid(@RequestBody JqGridRequest jqGridRequest) throws Exception {
		return adminServiceImpl.getMailContentForKid(jqGridRequest);
	}

	/**
	 * 경영실적 메일링 대상자 발송 처리.
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/boss/sendMailsForKid", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult sendMailsForKid(@RequestBody JqGridRequest jqGridRequest, BipUser bipUser) throws Exception {
		AjaxResult ajaxResult = null;
		try {
			jqGridRequest.setUsername(bipUser.getUsername());
			ajaxResult = adminServiceImpl.sendMailsForKid(jqGridRequest);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("sendMailsForKid {}", e);
			if (ajaxResult == null)
				ajaxResult = new AjaxResult();
			ajaxResult.setCode(998);
			ajaxResult.setMessage("mail_fail");
		}
		return ajaxResult;
	}

	/**
	 * SMS 발송 테스트 API
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/boss/sms", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult sendMMS() throws Exception {
		return customerServiceImpl.sendMMS("OCB");
	}
}
