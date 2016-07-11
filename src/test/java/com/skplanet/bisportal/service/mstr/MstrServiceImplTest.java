package com.skplanet.bisportal.service.mstr;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.microstrategy.utils.StringUtils;
import com.microstrategy.web.objects.*;
import com.microstrategy.webapi.EnumDSSXMLApplicationType;
import com.microstrategy.webapi.EnumDSSXMLAuthModes;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.microstrategy.webapi.EnumDSSXMLProjectStatus;
import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.mstr.PromptSelector;
import com.skplanet.bisportal.model.mstr.MstrRequest;
import com.skplanet.bisportal.repository.acl.UserRepository;

/**
 * The MstrServiceImplTest class.
 *
 * @author ophelisis
 *
 */
@Slf4j
public class MstrServiceImplTest {
	MockMvc mockMvc;

	@Mock
	private UserRepository userRepository;

	protected static final Injector injector = Guice.createInjector();
	private static final String SERVER_NAME = "WIN-VTC3SBJLDQA";
	private static final int SERVER_PORT = 0;
	private static final String PROJECT_ID = "B19DEDCC11D4E0EFC000EB9495D0F44F";
	private static final String USER_ID = "test";
	private static final String USER_PASSWORD = "test";
	private static final String TRUST_TOKEN = "TokenC26D6B844B06CB83E8E2938FC7294EAC"; //dev
	private static final String OBJECT_ID = "E271825943AC632AA416E0B36EB72BD3";
	private static final String FOLDER_ID = "4776E7AA43EE5DF2429411A1723718F4";

	@Before
	public void init() {
	}

	private WebIServerSession setWebIServerSession(WebObjectsFactory objFactory) {
		WebIServerSession serverSession = objFactory.getIServerSession();
		serverSession.setServerName(SERVER_NAME);
		serverSession.setServerPort(SERVER_PORT);
		serverSession.setLogin(USER_ID);
		serverSession.setTrustToken(TRUST_TOKEN);
		serverSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthTrusted);
		serverSession.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationCustomApp);
		serverSession.setProjectID(PROJECT_ID);
		return serverSession;
	}

	@Test
	public void testGetCheckSession() throws Exception {
		WebObjectsFactory objFactory = WebObjectsFactory.getInstance();
		WebIServerSession serverSession = setWebIServerSession(objFactory);
		serverSession.getSessionID();
		String sessionId = serverSession.saveState();
		log.debug(">>> sessionId : {}", sessionId);
		assertNotNull(sessionId);
	}

	@Test
	public void testGetPrompts() throws Exception {
		MstrRequest mstrRequest = new MstrRequest();
		mstrRequest.setMenuCode("testMenu");
		WebObjectsFactory objFactory = WebObjectsFactory.getInstance();
		WebIServerSession serverSession = setWebIServerSession(objFactory);
		serverSession.getSessionID();
		String sessionId = serverSession.saveState();
		if (StringUtils.isNotEmpty(sessionId)) {
			WebReportSource reptSrc = objFactory.getReportSource();
			WebReportInstance reptIns = reptSrc.getNewInstance(OBJECT_ID);
			WebPrompts prompts = reptIns.getPrompts();
			if (prompts == null) {
				log.warn(">>> prompts is null");
			} else {
				StringBuffer promptStr = new StringBuffer(256);
				promptStr.append("<table class='table form-type'>");
				promptStr.append("<colgroup><col style='width:10%;'><col style='width:90%;'></colgroup>");
				promptStr.append("<tbody>");

				int promptsSize = prompts.size();
				for (int x = 0; x < promptsSize; x++) {
					PromptSelector promptSelector = injector.getInstance(PromptSelector.class);
					promptStr.append(promptSelector
							.getHandlePrompt(prompts.get(x).getPromptType(), prompts.get(x), mstrRequest));
				}
				promptStr.append("</tbody>");
				promptStr.append("</table>");
				log.debug(">>> promptStr {}", promptStr.toString());
			}
		}
	}

	@Test
	public void testGetProjectList() throws Exception {
		WebObjectsFactory objFactory = WebObjectsFactory.getInstance();
		WebIServerSession serverSession = setWebIServerSession(objFactory);
		serverSession.setPassword(USER_PASSWORD);
		WebProjectSource oProjectSource = objFactory.getProjectSource();
		WebProjectInstances oPInstanceList = oProjectSource.getAccessibleProjectsInCluster();
		int oPInstanceListSize = oPInstanceList.size();
		WebProjectInstance oPInstance;
		List<Map<String, Object>> projectList = new ArrayList<>();
		for (int i = 0; i < oPInstanceListSize; i++) {
			oPInstance = oPInstanceList.get(i);
			if (oPInstance.getStatus() == EnumDSSXMLProjectStatus.DssXmlProjectStatusActive && !org.apache.commons.lang.StringUtils
					.equals(oPInstance.getProjectDSSID(), Constants.EXCLUDE_PROJECT)) {
				Map<String, Object> project = Maps.newHashMap();
				project.put("id",oPInstance.getProjectDSSID());
				project.put("name",oPInstance.getProjectName());
				projectList.add(project);
				log.debug(">>> project : {}", project);
			}
		}
		assertThat(projectList.size(), greaterThanOrEqualTo(0));
	}

	@Test
	public void testGetMenus() throws Exception {
		WebObjectsFactory objFactory = WebObjectsFactory.getInstance();
		WebIServerSession serverSession = setWebIServerSession(objFactory);
		serverSession.getSessionID();
		WebObjectSource objSource = objFactory.getObjectSource();
		Map<String, Object> map = getSubFolder(objSource, FOLDER_ID);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("children");
		assertThat(list.size(), greaterThanOrEqualTo(0));
	}

	private Map<String, Object> getSubFolder(WebObjectSource objSource, String folderId) throws Exception {
		Map<String, Object> menus = new TreeMap<>();
		WebObjectInfo obj = objSource.getObject(folderId, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
		WebFolder rootFolder = (WebFolder) obj;
		rootFolder.populate();
		menus.put("id", rootFolder.getID());
		menus.put("title", rootFolder.getDisplayName());
		menus.put("isFolder", 1);
		menus.put("type", rootFolder.getType());
		menus.put("subType", rootFolder.getSubType());

		if (rootFolder.size() > 0) {
			WebDisplayUnits units = rootFolder.getChildUnits();
			List<Object> childList = new ArrayList<>();
			WebFolder childFolder;
			WebObjectInfo childObj;
			Map<String, Object> childMap;

			for (int i = 0; i < units.size(); i++) {
				WebDisplayUnit child = units.get(i);
				if (child.getDisplayUnitType() == EnumDSSXMLObjectTypes.DssXmlTypeFolder) {
					childObj = objSource.getObject(child.getID(), EnumDSSXMLObjectTypes.DssXmlTypeFolder);
					childFolder = (WebFolder) childObj;
					childFolder.populate();
					childMap = new TreeMap<>();
					childMap.put("id", childFolder.getID());
					childMap.put("title", childFolder.getDisplayName());
					childMap.put("type", childFolder.getType());
					childMap.put("subType", childFolder.getSubType());
					childMap.put("unitType", child.getDisplayUnitType());
					childMap.put("isFolder", 1);
					childList.add(childMap);
					log.debug("************** Folder {}", child.getDisplayUnitType());
					log.debug("id: {}", childFolder.getID());
					log.debug("title: {}", childFolder.getDisplayName());
					log.debug("type: {}", childFolder.getType());
					log.debug("subType: {}", childFolder.getType());
				} else if (child.getDisplayUnitType() == EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition || child.getDisplayUnitType() == EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition) {
					childObj = objSource.getObject(child.getID(), child.getDisplayUnitType());
					childObj.populate();
					childMap = new TreeMap<>();
					childMap.put("id", childObj.getID());
					childMap.put("title", childObj.getDisplayName());
					childMap.put("type", childObj.getType());
					childMap.put("subType", childObj.getSubType());
					childMap.put("unitType", child.getDisplayUnitType());
					childMap.put("isFolder", 0);
					childList.add(childMap);
					log.debug("************** Report (" + child.getDisplayUnitType() + ")");
					log.debug("id: {}", childObj.getID());
					log.debug("title: {}", childObj.getDisplayName());
					log.debug("type: {}", childObj.getType());
					log.debug("subType: {}", childObj.getType());
				} else {
				}
			}
			menus.put("children", childList);
		}

		return menus;
	}
}
