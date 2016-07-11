package com.skplanet.bisportal.service.mstr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.microstrategy.web.objects.*;
import com.microstrategy.webapi.EnumDSSXMLApplicationType;
import com.microstrategy.webapi.EnumDSSXMLAuthModes;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.microstrategy.webapi.EnumDSSXMLProjectStatus;
import com.skplanet.bisportal.common.acl.CookieUtils;
import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.mstr.ExpressionPromptHandler;
import com.skplanet.bisportal.common.mstr.PromptSelector;
import com.skplanet.bisportal.common.utils.ObjectMapperFactory;
import com.skplanet.bisportal.common.utils.Utils;
import com.skplanet.bisportal.model.acl.Login;
import com.skplanet.bisportal.model.bip.ComRoleUser;
import com.skplanet.bisportal.model.mstr.*;
import com.skplanet.bisportal.repository.acl.UserRepository;
import com.skplanet.bisportal.repository.mstr.MstrBipRepository;
import com.skplanet.bisportal.repository.mstr.MstrSyrupRepository;

/**
 * The MstrServiceImpl class.
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 *
 */
@Slf4j
@Service
public class MstrServiceImpl implements MstrService {
	private final ObjectMapper mapper = ObjectMapperFactory.get();
	protected static final Injector injector = Guice.createInjector();
	@Value("#{common['mstr.base.url']}")
	private String BASE_URL;
	@Value("#{common['mstr.server.name.01']}")
	private String SERVER_NAME_01;
	@Value("#{common['mstr.server.name.02']}")
	private String SERVER_NAME_02;
	@Value("#{common['mstr.server.port']}")
	private String SERVER_PORT;
	@Value("#{common['mstr.trust.token.01']}")
	private String TRUST_TOKEN_01;
	@Value("#{common['mstr.trust.token.02']}")
	private String TRUST_TOKEN_02;
	@Value("#{common['mstr.hidden.sections']}")
	private String HIDDEN_SECTIONS;
	@Value("#{common['mstr.current.view.media']}")
	private String CURRENT_VIEW_MEDIA;
	@Value("#{common['mstr.vis.mode']}")
	private String VIS_MODE;
	@Value("#{common['mstr.report.user.id']}")
	private String REPORT_USER_ID;
	@Value("#{common['mstr.report.user.password']}")
	private String REPORT_USER_PASSWORD;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MstrBipRepository mstrBipRepository;

	@Autowired
	private MstrSyrupRepository mstrSyrupRepository;

	/**
	 * MSTR 세션 생성(비동기 처리)
	 *
	 * @param request
	 * @param response
	 */
	@Async
	@Override
	@Transactional
	public void createSession(HttpServletRequest request, HttpServletResponse response, Login loginUser) {
		Map<String, String> serverInfos = getServerInfo(loginUser.getUsername().toUpperCase());
		try {
			WebObjectsFactory objFactory = WebObjectsFactory.getInstance();
			WebIServerSession serverSession = objFactory.getIServerSession();

			List<Map<String, Object>> projectList = getProjectList(serverInfos, objFactory, serverSession);
			if (projectList != null) {
				int projectListSize = projectList.size();
				JsonArray projects = new JsonArray();
				for (int i = 0; i < projectListSize; i++) {
					String projectId = projectList.get(i).get("id").toString();
					String projectName = projectList.get(i).get("name").toString();

					JsonObject tmpObj = new JsonObject();
					tmpObj.addProperty("id", projectId);
					tmpObj.addProperty("name", projectName);
					projects.add(tmpObj);

//					serverSession.setServerName(serverInfos.get("serverName"));
//					serverSession.setServerPort(Integer.parseInt(SERVER_PORT));
//					serverSession.setLogin(serverInfos.get("userId"));
//					serverSession.setTrustToken(serverInfos.get("trustToken"));
//					serverSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthTrusted);
//					serverSession.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationCustomApp);
//					serverSession.setProjectID(projectId);
					serverSession = setServerSession(serverSession, serverInfos, projectId);
					serverSession.getSessionID();

					MstrSession mstrSession = new MstrSession();
					mstrSession.setUsername(loginUser.getUsername().toUpperCase());
					mstrSession.setIp(CookieUtils.getIpAddress(request));
					mstrSession.setProjectId(projectId);
					mstrSession.setSessionId(serverSession.saveState());
					userRepository.createMstrSession(mstrSession);
//					CookieUtils.setMstrCookie(response, projectId, serverSession.saveState(), loginUser.isLoginCheck());
				}
				CookieUtils.setMstrCookie(response, "mstr_projects", projects.toString(), loginUser.isLoginCheck());
			}

			log.debug("++ createSession ++++++++++++++++++++++++++++++++");
			log.debug("loginId : {}", loginUser.getUsername());
			log.debug("serverSession : {}", serverSession);
			log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++");
		} catch (WebObjectsException e) {
			log.error("WebObjectsException {}", e);
		} catch (Exception e) {
			log.error("createSession {}", e);
		}
	}

	@Async
	@Override
	@Transactional
	public void closeSession(HttpServletRequest request, HttpServletResponse response) {
		try {
			WebObjectsFactory objFactory = WebObjectsFactory.getInstance();
			WebIServerSession serverSession = objFactory.getIServerSession();

			String mstrProjects = CookieUtils.getMstrCookie(request, "mstr_projects");
			if (StringUtils.isNotEmpty(mstrProjects)) {
				mstrProjects = "{\"mstrs\":" + mstrProjects + "}";
				List<Mstr> mstrs = mapper.readValue(mstrProjects, MstrMenu.class).getMstrs();
				for (Mstr mstr : mstrs) {
					MstrSession mstrSession = new MstrSession();
					mstrSession.setUsername(CookieUtils.getVoyagerCookie(request).getUsername().toUpperCase());
					mstrSession.setIp(CookieUtils.getIpAddress(request));
					mstrSession.setProjectId(mstr.getId());
					String sessionId = userRepository.getMstrSession(mstrSession);
//					String sessionId = CookieUtils.getMstrCookie(request, mstr.getId());
					if (sessionId != null && serverSession.restoreState(sessionId)) {
						serverSession.closeSession();
					}
					userRepository.deleteMstrSession(mstrSession);
//					CookieUtils.deleteCookie(response, mstr.getId());
				}
			}
			CookieUtils.deleteCookie(response, "mstr_projects");
		} catch (Exception e) {
			log.error("Exception {}", e);
		}
	}

	/**
	 * MSTR 세션 체크
	 *
	 * @param request
	 * @param response
	 */
	@Override
	@Transactional
	public MstrResponse getCheckSession(HttpServletRequest request, HttpServletResponse response, MstrRequest mstrRequest) throws WebObjectsException {
		MstrResponse mstrResponse = null;
		String username = CookieUtils.getVoyagerCookie(request).getUsername().toUpperCase();
		Map<String, String> serverInfos = getServerInfo(username);
		WebObjectsFactory objFactory = WebObjectsFactory.getInstance();
		WebIServerSession serverSession = objFactory.getIServerSession();

		MstrSession mstrSession = new MstrSession();
		mstrSession.setUsername(username);
		mstrSession.setIp(CookieUtils.getIpAddress(request));
		mstrSession.setProjectId(mstrRequest.getProjectId());
		String sessionId = userRepository.getMstrSession(mstrSession);
//		String sessionId = CookieUtils.getMstrCookie(request, mstrRequest.getProjectId());

		boolean isAlive = false;
		//TODO: (ophelisis) 현재 MSTR 서버가 off 상태에서도 true 값을 반환하는 문제 있음.
		if (sessionId != null && serverSession.restoreState(sessionId) && serverSession.isAlive()) {
			isAlive = true;
		}
		if (!isAlive) {
//			serverSession.setServerName(serverInfos.get("serverName"));
//			serverSession.setServerPort(Integer.parseInt(SERVER_PORT));
//			serverSession.setLogin(serverInfos.get("userId"));
//			serverSession.setTrustToken(serverInfos.get("trustToken"));
//			serverSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthTrusted);
//			serverSession.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationCustomApp);
//			serverSession.setProjectID(mstrRequest.getProjectId());
			serverSession = setServerSession(serverSession, serverInfos, mstrRequest.getProjectId());
			serverSession.getSessionID();
			sessionId = serverSession.saveState();

			mstrSession = new MstrSession();
			mstrSession.setUsername(username);
			mstrSession.setIp(CookieUtils.getIpAddress(request));
			mstrSession.setProjectId(mstrRequest.getProjectId());
			mstrSession.setSessionId(sessionId);
			userRepository.createMstrSession(mstrSession);
//			CookieUtils.setMstrCookie(response, mstrRequest.getProjectId(), sessionId, true);
		}
		if (StringUtils.isNotEmpty(sessionId)) {
			mstrResponse = new MstrResponse();
			mstrResponse.setBaseUrl(BASE_URL);
			mstrResponse.setSessionId(sessionId);

			if (!StringUtils.equals(mstrRequest.getSaveServerInfo(), Constants.OFF)) {
				mstrResponse.setObjFactory(objFactory);
				mstrResponse.setServerSession(serverSession);
			}
		}
		log.debug("++ getCheckSession ++++++++++++++++++++++++++++++");
		log.debug("projectId : {}", mstrRequest.getProjectId());
		log.debug("saveServerInfo : {}", mstrRequest.getSaveServerInfo());
		log.debug("sessionId : {}", sessionId);
		log.debug("isAlive : {}", isAlive);
		log.debug("serverSession : {}", serverSession);
		log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++");
		return mstrResponse;
	}

	/**
	 * MSTR 프롬프트 정보 생성
	 *
	 * @param request
	 * @param mstrRequest
	 * @return
	 * @throws Exception
	 */
	@Override
	public MstrResponse getPrompts(HttpServletRequest request, HttpServletResponse response, MstrRequest mstrRequest) throws Exception {
		if (mstrRequest == null) {
			log.warn("mstrRequest is null");
			return null;
		}
		MstrResponse serverInfo = getCheckSession(request, response, mstrRequest);
		MstrResponse mstrResponse = setMstrResponse();
		mstrResponse.setUsrSmgr(serverInfo.getSessionId());
		WebObjectsFactory objFactory = serverInfo.getObjFactory();
		WebPrompts prompts = null;

		if (mstrRequest.getObjectType() == EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition) {
			WebReportSource reptSrc = objFactory.getReportSource();
			WebReportInstance reptIns = reptSrc.getNewInstance(mstrRequest.getObjectId());
			try {
				prompts = reptIns.getPrompts();
			} catch (Exception e) {
				;
			}
			mstrResponse.setEvt("4001");
			mstrResponse.setSrc("mstrWeb.4001");
			mstrResponse.setReportId(mstrRequest.getObjectId());
		} else if (mstrRequest.getObjectType() == EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition) {
			WebDocumentSource docSrc = objFactory.getDocumentSource();
			WebDocumentInstance wdb = docSrc.getNewInstance(mstrRequest.getObjectId());
			try {
				prompts = wdb.getPrompts();
			} catch (Exception e) {
				;
			}
			mstrResponse.setEvt("2048001");
			mstrResponse.setSrc("mstrWeb.2048001");
			mstrResponse.setDocumentId(mstrRequest.getObjectId());
		} else if (mstrRequest.getObjectType() == 99) {
			WebDocumentSource docSrc = objFactory.getDocumentSource();
			WebDocumentInstance wdb = docSrc.getNewInstance(mstrRequest.getObjectId());
			try {
				prompts = wdb.getPrompts();
			} catch (Exception e) {
				;
			}
			mstrResponse.setEvt("3140");
			mstrResponse.setSrc("mstrWeb.3140");
			mstrResponse.setDocumentId(mstrRequest.getObjectId());
		}
		if (prompts != null) {
			mstrResponse.setPrompts(setPromptsCustomizing(prompts, mstrRequest));
		}
		log.debug("++ getPrompts +++++++++++++++++++++++++++++++++++");
		log.debug("{}", mstrResponse.toString());
		log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++");
		return mstrResponse;
	}

	/**
	 * MSTR 권한 정보
	 *
	 * @param comRoleUser
	 * @return
	 * @throws Exception
	 */
	@Override
	public ComRoleUser getMstrRole(ComRoleUser comRoleUser) throws Exception {
		comRoleUser.setRoleId(10l);
		return userRepository.getComRoleUser(comRoleUser);
	}

	/**
	 * MSTR 리포트 날짜 조건 정보
	 *
	 * @param request
	 * @param response
	 * @param mstrRequest
	 * @return
	 * @throws Exception
	 */
	@Override
	public MstrResponse getDateTypes(HttpServletRequest request, HttpServletResponse response, MstrRequest mstrRequest) throws Exception {
		MstrResponse serverInfo = getCheckSession(request, response, mstrRequest);
		WebObjectsFactory objFactory = serverInfo.getObjFactory();
		WebReportSource reptSrc = objFactory.getReportSource();
		WebReportInstance reptIns = reptSrc.getNewInstance(mstrRequest.getObjectId());
		WebPrompts prompts = reptIns.getPrompts();

		StringBuffer promptStr = new StringBuffer(256);
		int promptsSize = prompts.size();
		for (int x = 0; x < promptsSize; x++) {
			if (prompts.get(x).getPromptType() == EnumWebPromptType.WebPromptTypeExpression) {
				ExpressionPromptHandler expressionPromptHandler = injector.getInstance(ExpressionPromptHandler.class);
				promptStr.append(expressionPromptHandler.getDateTypes(prompts.get(x)));
			}
		}
		log.debug("++ getDateTypes +++++++++++++++++++++++++++++++++");
		log.debug("projectId : {}", mstrRequest.getProjectId());
		log.debug("objectId : {}", mstrRequest.getObjectId());
		log.debug("dateTypes : {}", promptStr.toString());
		log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++");
		MstrResponse mstrResponse = new MstrResponse();
		mstrResponse.setDateTypes(promptStr.toString());
		return mstrResponse;
	}

	/**
	 * 기준일에 대한 주차 정보
	 *
	 * @param mstrRequest
	 * @return
	 * @throws Exception
	 */
	@Override
	public MstrResponse getWeeks(MstrRequest mstrRequest) throws Exception {
		log.debug("++ getWeeks +++++++++++++++++++++++++++++++++++++");
		log.debug("weekType : {}", mstrRequest.getWeekType());
		log.debug("searchStartDate : {}", mstrRequest.getSearchStartDate());
		log.debug("searchEndDate : {}", mstrRequest.getSearchEndDate());

		String startDate = null;
		String endDate = null;
		List<MstrWeek> startDates;
		List<MstrWeek> endDates;
		if (StringUtils.equals(mstrRequest.getWeekType().toUpperCase(), Constants.WEEK_TYPE_1)) {
			startDates = mstrBipRepository.getWeeks(mstrRequest.getSearchStartDate());
			endDates = mstrBipRepository.getWeeks(mstrRequest.getSearchEndDate());
			if (startDates.size() > 0 && endDates.size() > 0) {
				startDate = startDates.get(0).getWkPerdCd();
				endDate = endDates.get(0).getWkPerdCd();
			}
		} else if (StringUtils.equals(mstrRequest.getWeekType().toUpperCase(), Constants.WEEK_TYPE_2)) {
			startDates = mstrBipRepository.getWeeks(mstrRequest.getSearchStartDate());
			endDates = mstrBipRepository.getWeeks(mstrRequest.getSearchEndDate());
			if (startDates.size() > 0 && endDates.size() > 0) {
				startDate = startDates.get(0).getWkDt();
				endDate = endDates.get(0).getWkDt();
			}
		} else {
			startDates = mstrSyrupRepository.getWeeks(mstrRequest.getSearchStartDate());
			endDates = mstrSyrupRepository.getWeeks(mstrRequest.getSearchEndDate());
			if (startDates.size() > 0 && endDates.size() > 0) {
				startDate = startDates.get(0).getWkDt();
				endDate = endDates.get(0).getWkDt();
			}
		}
		MstrResponse mstrResponse = new MstrResponse();
		mstrResponse.setSearchStartDate(startDate);
		mstrResponse.setSearchEndDate(endDate);
		log.debug("resultStartDate : {}", startDate);
		log.debug("resultEndDate : {}", endDate);
		log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++");
		return mstrResponse;
	}

	/**
	 * MSTR 프로젝트 목록 생성
	 *
	 * @return
	 */
	private List<Map<String, Object>> getProjectList(Map<String, String> serverInfos, WebObjectsFactory objFactory, WebIServerSession serverSession) {
		List<Map<String, Object>> projectList = null;
		try {
			if (objFactory == null || serverSession == null) {
				objFactory = WebObjectsFactory.getInstance();
				serverSession = objFactory.getIServerSession();
			}
			serverSession.setServerName(serverInfos.get("serverName"));
			serverSession.setServerPort(Integer.parseInt(SERVER_PORT));
			serverSession.setLogin(serverInfos.get("userId"));
			serverSession.setPassword(serverInfos.get("userPassword"));
			WebProjectSource oProjectSource = objFactory.getProjectSource();
			WebProjectInstances oPInstanceList = oProjectSource.getAccessibleProjectsInCluster();
			int oPInstanceListSize = oPInstanceList.size();

			WebProjectInstance oPInstance;
			projectList = new ArrayList<>();
			for (int i = 0; i < oPInstanceListSize; i++) {
				oPInstance = oPInstanceList.get(i);
				if (oPInstance.getStatus() == EnumDSSXMLProjectStatus.DssXmlProjectStatusActive && !StringUtils.equals(oPInstance.getProjectDSSID(), Constants.EXCLUDE_PROJECT)) {
					Map<String, Object> project = Maps.newHashMap();
					project.put("id",oPInstance.getProjectDSSID());
					project.put("name",oPInstance.getProjectName());
					projectList.add(project);
				}
			}
		} catch(WebObjectsException e) {
			log.error("WebObjectsException {}", e);
		}
		return projectList;
	}

	private WebIServerSession setServerSession(WebIServerSession serverSession, Map<String, String> serverInfos, String projectId) {
		serverSession.setServerName(serverInfos.get("serverName"));
		serverSession.setServerPort(Integer.parseInt(SERVER_PORT));
		serverSession.setLogin(serverInfos.get("userId"));
		serverSession.setTrustToken(serverInfos.get("trustToken"));
		serverSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthTrusted);
		serverSession.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationCustomApp);
		serverSession.setProjectID(projectId);
		return serverSession;
	}

	/**
	 * MSTR Response 객체 설정
	 *
	 * @return
	 * @throws Exception
	 */
	private MstrResponse setMstrResponse() throws Exception {
		MstrResponse mstrResponse = new MstrResponse();
		mstrResponse.setBaseUrl(BASE_URL);
		mstrResponse.setHiddenSections(HIDDEN_SECTIONS);
		mstrResponse.setCurrentViewMedia(CURRENT_VIEW_MEDIA);
		mstrResponse.setVisMode(VIS_MODE);
		return mstrResponse;
	}

	/**
	 * MSTR 프롬프트 커스터마이징
	 *
	 * @param prompts
	 * @return
	 * @throws Exception
	 */
	private String setPromptsCustomizing(WebPrompts prompts, MstrRequest mstrRequest) throws Exception {
		StringBuffer promptStr = new StringBuffer(256);
		int promptsSize = prompts.size();
		if (promptsSize > 0) {
			promptStr.append("<table class='table form-type'>");
			promptStr.append("<colgroup><col style='width:10%;'><col style='width:90%;'></colgroup>");
			promptStr.append("<tbody>");
			for (int x = 0; x < promptsSize; x++) {
				PromptSelector promptSelector = injector.getInstance(PromptSelector.class);
				promptStr.append(promptSelector
						.getHandlePrompt(prompts.get(x).getPromptType(), prompts.get(x), mstrRequest));
			}
			promptStr.append("</tbody>");
			promptStr.append("</table>");
		}
		return promptStr.toString();
	}

	/**
	 * MSTR 접속 정보 설정
	 *
	 * @param loginId
	 * @return
	 */
	private Map<String, String> getServerInfo(String loginId) {
		Map<String, String> serverInfos = null;
		try {
			ComRoleUser comRoleUser = new ComRoleUser();
			comRoleUser.setRoleId(10l);
			comRoleUser.setLoginId(loginId);
			ComRoleUser ssbiUser = userRepository.getComRoleUser(comRoleUser);

			String loginPassword = "mstr" + loginId.substring(loginId.length() - 4, loginId.length()) + "!";
			serverInfos = Maps.newHashMap();
			serverInfos.put("userId", ssbiUser != null ? loginId : REPORT_USER_ID);
			serverInfos.put("userPassword", ssbiUser != null ? loginPassword : REPORT_USER_PASSWORD);
			String hostName = Utils.getHostName();
			if (StringUtils.isNotEmpty(hostName) && StringUtils.equals(hostName, Constants.HOST_WAS02)) {
				serverInfos.put("serverName", SERVER_NAME_02);
				serverInfos.put("trustToken", TRUST_TOKEN_02);
			} else {
				serverInfos.put("serverName", SERVER_NAME_01);
				serverInfos.put("trustToken", TRUST_TOKEN_01);
			}
			log.debug("++ getServerInfo ++++++++++++++++++++++++++++++++");
			log.debug("ssbiUser : {}", ssbiUser);
			log.debug("{}", serverInfos.toString());
			log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++");
		} catch (Exception e) {
			log.error("Exception {}", e);
		}
		return serverInfos;
	}
}
