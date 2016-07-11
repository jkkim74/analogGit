package com.skplanet.bisportal.controller.syrup;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.ReportDateType;
import com.skplanet.bisportal.model.syrup.SmwCardIssueDtl;
import com.skplanet.bisportal.model.syrup.SmwRcmdStatDtl;
import com.skplanet.bisportal.service.syrup.MembershipService;
import com.skplanet.bisportal.testsupport.util.TestUtil;

@Slf4j
@Transactional
public class MembershipControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private MembershipController membershipController;

	@Mock
	private MembershipService membershipServiceImpl;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		this.mockMvc = standaloneSetup(membershipController).build();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetCardIssueMemGrid() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20141129");
		jqGridRequest.setEndDate("20141208");
		jqGridRequest.setRows(-1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<SmwCardIssueDtl> smwCardIssueDtlList = Lists.newArrayList();
		when(membershipServiceImpl.getCardIssueMemGrid(jqGridRequest)).thenReturn(smwCardIssueDtlList);
		this.mockMvc
				.perform(
						get(
								"/syrup/membership/cardissuemem/grid?dateType=day&startDate=20141129&endDate=20141208&sidx=stdDt&sord=asc")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
	}

	@Test
	public void testGetCardIssuePaVouGrid() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20141129");
		jqGridRequest.setEndDate("20141208");
		jqGridRequest.setRows(-1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<SmwCardIssueDtl> smwCardIssueDtlList = Lists.newArrayList();
		when(membershipServiceImpl.getCardIssuePaVouGrid(jqGridRequest)).thenReturn(smwCardIssueDtlList);
		this.mockMvc
				.perform(
						get(
								"/syrup/membership/cardissuePaVou/grid?dateType=day&startDate=20141129&endDate=20141208&sidx=stdDt&sord=asc")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
	}

	@Test
	public void testGetSmwRcmdStatDtlGrid() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20141129");
		jqGridRequest.setEndDate("20141208");
		jqGridRequest.setRows(-1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<SmwRcmdStatDtl> smwRcmdStatDtlList = Lists.newArrayList();
		when(membershipServiceImpl.getSmwRcmdStatDtlGrid(jqGridRequest)).thenReturn(smwRcmdStatDtlList);
		this.mockMvc
				.perform(
						get(
								"/syrup/membership/rcmdStat/grid?dateType=day&startDate=20141129&endDate=20141208&sidx=stdDt&sord=asc")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
	}

	@Test
	public void testDownloadExcelForMebershipIssueMem() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20141129");
		jqGridRequest.setEndDate("20141208");
		jqGridRequest.setRows(-1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<SmwCardIssueDtl> smwCardIssueDtlList = Lists.newArrayList();
		when(membershipServiceImpl.getCardIssueMemGrid(jqGridRequest)).thenReturn(smwCardIssueDtlList);
		this.mockMvc
				.perform(
						post("/syrup/membership/downloadExcelForMebershipIssueMem")
								.contentType(MediaType.APPLICATION_JSON)
								.content(
										("{\"dateType\":\"day\", \"startDate\":\"20141129\", \"endDate\":\"20141208\", \"sidx\":\"stdDt\", \"sord\":\"asc\"}")
												.getBytes())).andExpect(status().isOk()).andDo(print())
				.andExpect(handler().methodName("downloadExcelForMebershipIssueMem"));
	}

	@Test
	public void testDownloadExcelForIssuePaVou() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20141129");
		jqGridRequest.setEndDate("20141208");
		jqGridRequest.setRows(-1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<SmwCardIssueDtl> smwCardIssueDtlList = Lists.newArrayList();
		when(membershipServiceImpl.getCardIssuePaVouGrid(jqGridRequest)).thenReturn(smwCardIssueDtlList);
		this.mockMvc
				.perform(
						post("/syrup/membership/downloadExcelForIssuePaVou")
								.contentType(MediaType.APPLICATION_JSON)
								.content(
										("{\"dateType\":\"day\", \"startDate\":\"20141129\", \"endDate\":\"20141208\", \"sidx\":\"stdDt\", \"sord\":\"asc\"}")
												.getBytes())).andExpect(status().isOk()).andDo(print())
				.andExpect(handler().methodName("downloadExcelForIssuePaVou"));
	}

	@Test
	public void testDownloadExcelForRcmdStat() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20141129");
		jqGridRequest.setEndDate("20141208");
		jqGridRequest.setRows(-1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<SmwRcmdStatDtl> smwRcmdStatDtlList = Lists.newArrayList();
		when(membershipServiceImpl.getSmwRcmdStatDtlGrid(jqGridRequest)).thenReturn(smwRcmdStatDtlList);
		this.mockMvc
				.perform(
						post("/syrup/membership/downloadExcelForRcmdStat")
								.contentType(MediaType.APPLICATION_JSON)
								.content(
										("{\"dateType\":\"day\", \"startDate\":\"20141129\", \"endDate\":\"20141208\", \"sidx\":\"stdDt\", \"sord\":\"asc\"}")
												.getBytes())).andExpect(status().isOk()).andDo(print())
				.andExpect(handler().methodName("downloadExcelForRcmdStat"));
	}

	@Test
	public void testInitBinder() throws Exception {

	}
}
