package com.skplanet.bisportal.controller.bip;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.ReportDateType;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.*;
import com.skplanet.bisportal.model.ocb.ObsVstSta;
import com.skplanet.bisportal.service.bip.AdminService;
import com.skplanet.bisportal.service.bip.BpmResultSumService;
import com.skplanet.bisportal.testsupport.util.TestUtil;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static com.skplanet.bisportal.testsupport.builder.WhereConditionBuilder.aWhereCondition;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class AdminControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private AdminController adminController;

	@Mock
	private AdminService adminServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(adminController).alwaysExpect(status().isOk())
				.alwaysExpect(handler().handlerType(AdminController.class)).build();
	}

	@Test
	public void testGetAllBpmMgntRsltBatInfoRgst() throws Exception {
		List<BpmMgntRsltBatInfoRgst> bpmMgntRsltBatInfoRgsts = Lists.newArrayList();
		BpmMgntRsltBatInfoRgst bpmMgntRsltBatInfoRgst = new BpmMgntRsltBatInfoRgst();
		bpmMgntRsltBatInfoRgst.setSvcNm("OKCashbag");
		bpmMgntRsltBatInfoRgst.setDayRcvItmCnt(12l);
		bpmMgntRsltBatInfoRgsts.add(bpmMgntRsltBatInfoRgst);
		when(adminServiceImpl.getAllBpmMgntRsltBatInfoRgst()).thenReturn(bpmMgntRsltBatInfoRgsts);
		this.mockMvc
				.perform(
						get("/admin/boss/batchInfo?_search=false&nd=1411958338985&rows=-1&page=1&sidx=svcNm&sord=asc")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].svcNm", is("OKCashbag")));
		verify(adminServiceImpl, times(1)).getAllBpmMgntRsltBatInfoRgst();
		verifyNoMoreInteractions(adminServiceImpl);
	}

	@Test
	public void testGetBpmMgntRsltBatOpPrst() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setBasicDate("20140929");

		List<BpmMgntRsltBatOpPrst> bpmMgntRsltBatOpPrsts = Lists.newArrayList();
		BpmMgntRsltBatOpPrst bpmMgntRsltBatOpPrst = new BpmMgntRsltBatOpPrst();
		bpmMgntRsltBatOpPrst.setSvcNm("OKCashbag");
		bpmMgntRsltBatOpPrst.setSvcId(25);
		bpmMgntRsltBatOpPrsts.add(bpmMgntRsltBatOpPrst);
		when(adminServiceImpl.getBpmMgntRsltBatOpPrst(jqGridRequest.getBasicDate())).thenReturn(bpmMgntRsltBatOpPrsts);
		this.mockMvc
				.perform(
						get("/admin/boss/batchMonitoring?basicDate=20140929&_search=false&nd=1411958704264&page=1&sidx=svcNm&sord=asc")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))//.andDo(print());
				.andExpect(jsonPath("$.rows[0].svcNm", is("OKCashbag")));
		verify(adminServiceImpl, times(1)).getBpmMgntRsltBatOpPrst(jqGridRequest.getBasicDate());
		verifyNoMoreInteractions(adminServiceImpl);
	}

	@Test
	public void testGetHandInputTmap() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setBasicDate("20140929");
		jqGridRequest.setPage(1);
		jqGridRequest.setSidx("dispDt");
		jqGridRequest.setSord("asc");

		List<HandInput> handInputs = Lists.newArrayList();
		HandInput handInput = new HandInput();
		handInput.setDispDt("2014-09-27");
		handInput.setRsltVal1(new BigDecimal("232323"));
		handInputs.add(handInput);
		when(adminServiceImpl.getTMapCttMappInfo(jqGridRequest)).thenReturn(handInputs);
		this.mockMvc
				.perform(
						get("/admin/boss/handInputTmap?dateType=day&basicDate=20140929&reloadable=1&_search=false&nd=1411974231949&page=1&sidx=dispDt&sord=asc")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))//.andDo(print());
				.andExpect(jsonPath("$.rows[0].rsltVal1", is(232323)));
		verify(adminServiceImpl, times(1)).getTMapCttMappInfo(jqGridRequest);
		verifyNoMoreInteractions(adminServiceImpl);
	}

	@Test
	public void testGetHandInputSyrup() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setBasicDate("20140929");
		jqGridRequest.setPage(1);
		jqGridRequest.setSidx("dispDt");
		jqGridRequest.setSord("asc");
		jqGridRequest.setRows(1000);

		List<HandInput> handInputs = Lists.newArrayList();
		HandInput handInput = new HandInput();
		handInput.setDispDt("2014-09-27");
		handInput.setRsltVal2(new BigDecimal("363639"));
		handInputs.add(handInput);
		when(adminServiceImpl.getSyrupCttMappInfo(jqGridRequest)).thenReturn(handInputs);
		this.mockMvc
				.perform(
						get("/admin/boss/handInputSyrup?dateType=day&basicDate=20140929&reloadable=1&_search=false&nd=1411975766890&rows=1000&page=1&sidx=dispDt&sord=asc")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))//.andDo(print());
				.andExpect(jsonPath("$.rows[0].rsltVal2", is(363639)));
		verify(adminServiceImpl, times(1)).getSyrupCttMappInfo(jqGridRequest);
		verifyNoMoreInteractions(adminServiceImpl);
	}

	@Test
	public void testcheckBatchProcessing() throws Exception {
		when(adminServiceImpl.getBatchJobCheck(25)).thenReturn(0);
		this.mockMvc
				.perform(
						get("/admin/boss/checkBatchProcessing?svcId=25")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))//.andDo(print());
				.andExpect(jsonPath("$.code", is(200)));
		verify(adminServiceImpl, times(1)).getBatchJobCheck(25);
		verifyNoMoreInteractions(adminServiceImpl);
	}

	@Test
	public void testgetOrgTrees() throws Exception {
		OrgUser orgUser1 = new OrgUser();
		orgUser1.setOrgId("1");

		List<OrgUser> orgUsers = Lists.newArrayList();
		OrgUser orgUser2 = new OrgUser();
		orgUser2.setOrgId("1");
		orgUser2.setOrgNm("와이즈에코");
		orgUsers.add(orgUser2);
		when(adminServiceImpl.getOrgUserTree(orgUser1)).thenReturn(orgUsers);
		this.mockMvc.perform(get("/admin/boss/orgUserTrees?dateType=day&basicDate=20140929&reloadable=1&_search=false&nd=1411975766890&rows=1000&page=1&sidx=dispDt&sord=asc")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
//				.andExpect(jsonPath("$.rows[0].orgNm", is("와이즈에코")))
		;
//		verify(adminServiceImpl, times(1)).getOrgUserTree(orgUser1);
//		verifyNoMoreInteractions(adminServiceImpl);
	}
}
