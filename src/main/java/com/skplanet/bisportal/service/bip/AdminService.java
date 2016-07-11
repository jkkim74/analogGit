package com.skplanet.bisportal.service.bip;

import com.skplanet.bisportal.common.model.AjaxResult;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.bip.*;

import java.util.List;
import java.util.Map;

/**
 * Admin
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 */
public interface AdminService {

	List<HandInput> getTMapCttMappInfo(JqGridRequest jqGridRequest) throws Exception;

    List<HandInput> getSyrupCttMappInfo(JqGridRequest jqGridRequest) throws Exception;

	Integer getBatchJobCheck(Integer svcId) throws Exception;

	String createTmapCttMappInfoPerDay(JqGridRequest jqGridRequest) throws Exception;

	String createTmapCttMappInfoPerWeek(JqGridRequest jqGridRequest) throws Exception;

	String createTmapCttMappInfoPerMonth(JqGridRequest jqGridRequest) throws Exception;

	String createSyrupCttMappInfoPerDay(JqGridRequest jqGridRequest) throws Exception;

	String createSyrupCttMappInfoPerMonth(JqGridRequest jqGridRequest) throws Exception;

	void runBatch(String trmsFlNm) throws Exception;

	List<OrgUser> getOrgUserTree(OrgUser orgUser) throws Exception;

	List<OrgUser> getOrgUsers(OrgUser orgUser) throws Exception;

	List<OrgUser> getOrgTree() throws Exception;

	List<OrgUser> getEmailOrgUser(Long sndObjId) throws Exception;

	void createBpmMgntRsltScrnCmnt(BpmMgntRsltScrnCmnt bpmMgntRsltScrnCmnt) throws Exception;

	int deleteBpmMgntEmailSndUser(JqGridRequest jqGridRequest) throws Exception;

	int createBpmMgntEmailSndUser(BpmMgntEmailSndUser pmMgntEmailSndUser) throws Exception;

	BpmMgntEmailSndCtt getMailContentForKid(JqGridRequest jqGridRequest) throws Exception;

	List<BpmMgntRsltBatInfoRgst> getAllBpmMgntRsltBatInfoRgst() throws Exception;

	List<BpmMgntRsltBatOpPrst> getBpmMgntRsltBatOpPrst(String basicDate) throws Exception;

	AjaxResult sendMailsForKid(JqGridRequest jqGridRequest) throws Exception;

	MailReports getBusinessDatasForKid(JqGridRequest jqGridRequest) throws Exception;

	Map<String, Object> getVelocityMapForKid(MailReports mailReports) throws Exception;
}
