package com.skplanet.bisportal.controller.bip;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmSvcCd;
import com.skplanet.bisportal.service.bip.BpmSvcCdService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.skplanet.bisportal.testsupport.builder.WhereConditionBuilder.aWhereCondition;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * The BpmSvcCdControllerTest class.
 * 
 * @author sjune
 */
public class BpmSvcCdControllerTest {

	MockMvc mockMvc;

	@InjectMocks
	BpmSvcCdController bpmSvcCdController;

	@Mock
	BpmSvcCdService bpmSvcCdServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(bpmSvcCdController).alwaysExpect(status().isOk())
				.alwaysExpect(handler().handlerType(BpmSvcCdController.class)).build();
	}

	@Test
	public void testGetBpmSvcs() throws Exception {
		List<BpmSvcCd> bpmSvcCds = Lists.newArrayList();
		BpmSvcCd bpmSvcCd = new BpmSvcCd();
		bpmSvcCd.setSvcCdId("25");
		bpmSvcCd.setSvcCdNm("OKCashbag");
		bpmSvcCd.setSvcCdTitle("YYY");
		bpmSvcCds.add(bpmSvcCd);
		when(bpmSvcCdServiceImpl.getBpmSvcs()).thenReturn(bpmSvcCds);

		this.mockMvc.perform(get("/bpmSvcCd/svcs").contentType(MediaType.APPLICATION_JSON))
				.andExpect(handler().methodName("getBpmSvcs")).andExpect(jsonPath("$[0].svcCdId").value("25"))
				.andExpect(jsonPath("$[0].svcCdNm").value("OKCashbag"))
				.andExpect(jsonPath("$[0].svcCdTitle").value("YYY"));

		verify(bpmSvcCdServiceImpl, times(1)).getBpmSvcs();
	}

	@Test
	public void testGetBpmCycleToGrps() throws Exception {
		List<BpmSvcCd> bpmSvcCds = Lists.newArrayList();
		WhereCondition whereCondition = aWhereCondition().svcId(25L).lnkgCyclCd("D").build();

		BpmSvcCd bpmSvcCd = new BpmSvcCd();
		bpmSvcCd.setSvcCdId("L001");
		bpmSvcCd.setSvcCdNm("신규 가입자수");
		bpmSvcCd.setSvcCdTitle("신규 가입자수");
		bpmSvcCds.add(bpmSvcCd);
		when(bpmSvcCdServiceImpl.getBpmCycleToGrps(whereCondition)).thenReturn(bpmSvcCds);

		this.mockMvc
				.perform(get("/bpmSvcCd/cycleToGrps?svcId=25&lnkgCyclCd=D").contentType(MediaType.APPLICATION_JSON))
				.andExpect(handler().methodName("getBpmCycleToGrps")).andDo(print())
				.andExpect(jsonPath("$[0].svcCdId").value("L001")).andExpect(jsonPath("$[0].svcCdNm").value("신규 가입자수"))
				.andExpect(jsonPath("$[0].svcCdTitle").value("신규 가입자수"));

		verify(bpmSvcCdServiceImpl, times(1)).getBpmCycleToGrps(whereCondition);
	}

	@Test
	public void testGetBpmGrpToCls() throws Exception {
		List<BpmSvcCd> bpmSvcCds = Lists.newArrayList();
		WhereCondition whereCondition = aWhereCondition().svcId(25L).idxClGrpCd("L001").build();

		BpmSvcCd bpmSvcCd = new BpmSvcCd();
		bpmSvcCd.setSvcCdId("M001");
		bpmSvcCd.setSvcCdNm("총계");
		bpmSvcCd.setSvcCdTitle("총계");
		bpmSvcCds.add(bpmSvcCd);
		when(bpmSvcCdServiceImpl.getBpmGrpToCls(whereCondition)).thenReturn(bpmSvcCds);

		this.mockMvc
				.perform(get("/bpmSvcCd/grpToCls?svcId=25&idxClGrpCd=L001").contentType(MediaType.APPLICATION_JSON))
				.andExpect(handler().methodName("getBpmGrpToCls")).andDo(print())
				.andExpect(jsonPath("$[0].svcCdId").value("M001")).andExpect(jsonPath("$[0].svcCdNm").value("총계"))
				.andExpect(jsonPath("$[0].svcCdTitle").value("총계"));

		verify(bpmSvcCdServiceImpl, times(1)).getBpmGrpToCls(whereCondition);

	}

	@Test
	public void testGetBpmWkStrds() throws Exception {
		List<BpmSvcCd> bpmSvcCds = Lists.newArrayList();
		BpmSvcCd bpmSvcCd = new BpmSvcCd();
		bpmSvcCd.setSvcCdId("2014071");
		bpmSvcCd.setSvcCdNm("07/1주");
		bpmSvcCd.setSvcCdTitle("497");
		bpmSvcCds.add(bpmSvcCd);
		when(bpmSvcCdServiceImpl.getBpmWkStrds("201407")).thenReturn(bpmSvcCds);

		this.mockMvc.perform(get("/bpmSvcCd/wkStrds/201407").contentType(MediaType.APPLICATION_JSON))
				.andExpect(handler().methodName("getBpmWkStrds")).andDo(print())
				.andExpect(jsonPath("$[0].svcCdId").value("2014071")).andExpect(jsonPath("$[0].svcCdNm").value("07/1주"))
				.andExpect(jsonPath("$[0].svcCdTitle").value("497"));

		verify(bpmSvcCdServiceImpl, times(1)).getBpmWkStrds("201407");

	}
}
