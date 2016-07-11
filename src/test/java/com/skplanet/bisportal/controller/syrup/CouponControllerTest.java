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
import com.skplanet.bisportal.model.syrup.SmwCponLocStat;
import com.skplanet.bisportal.model.syrup.SmwCponStatDtl;
import com.skplanet.bisportal.service.syrup.CouponService;
import com.skplanet.bisportal.testsupport.util.TestUtil;

@Slf4j
@Transactional
public class CouponControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private CouponController couponController;

	@Mock
	private CouponService couponServiceImpl;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		this.mockMvc = standaloneSetup(couponController).build();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetCponStatForGrid() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20141129");
		jqGridRequest.setEndDate("20141208");
		jqGridRequest.setRows(-1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<SmwCponStatDtl> smwCponStatDtls = Lists.newArrayList();
		when(couponServiceImpl.getCponStatForGrid(jqGridRequest)).thenReturn(smwCponStatDtls);
		this.mockMvc
				.perform(
						get(
								"/syrup/coupon/cponstat/grid?dateType=day&startDate=20141129&endDate=20141208&sidx=stdDt&sord=asc")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
	}

	@Test
	public void testGetCponLocStatForGrid() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20141121");
		jqGridRequest.setEndDate("20141208");
		jqGridRequest.setRows(-1);
		jqGridRequest.setPage(1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<SmwCponLocStat> smwCponLocStats = Lists.newArrayList();
		when(couponServiceImpl.getCponLocStatForGrid(jqGridRequest)).thenReturn(smwCponLocStats);
		this.mockMvc
				.perform(
						get(
								"/syrup/coupon/cponlocstat/grid?dateType=day&startDate=20141121&endDate=20141208&sidx=stdDt&sord=asc")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
	}

	@Test
	public void testDownloadExcelForCouponStat() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20141129");
		jqGridRequest.setEndDate("20141208");
		jqGridRequest.setRows(-1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<SmwCponStatDtl> smwCponStatDtls = Lists.newArrayList();
		when(couponServiceImpl.getCponStatForGrid(jqGridRequest)).thenReturn(smwCponStatDtls);
		this.mockMvc
				.perform(
						post("/syrup/coupon/downloadExcelForCouponStat")
								.contentType(MediaType.APPLICATION_JSON)
								.content(
										("{\"dateType\":\"day\", \"startDate\":\"20141129\", \"endDate\":\"20141208\", \"sidx\":\"stdDt\", \"sord\":\"asc\"}")
												.getBytes())).andExpect(status().isOk()).andDo(print())
				.andExpect(handler().methodName("downloadExcelForCouponStat"));
	}

	@Test
	public void testDownloadExcelForCouponLocStat() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20141121");
		jqGridRequest.setEndDate("20141208");
		jqGridRequest.setRows(-1);
		jqGridRequest.setPage(1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<SmwCponLocStat> smwCponLocStats = Lists.newArrayList();
		when(couponServiceImpl.getCponLocStatForGrid(jqGridRequest)).thenReturn(smwCponLocStats);
		this.mockMvc
				.perform(
						post("/syrup/coupon/downloadExcelForCouponLocStat")
								.contentType(MediaType.APPLICATION_JSON)
								.content(
										("{\"dateType\":\"day\", \"startDate\":\"20141121\", \"endDate\":\"20141208\", \"sidx\":\"stdDt\", \"sord\":\"asc\"}")
												.getBytes())).andExpect(status().isOk()).andDo(print())
				.andExpect(handler().methodName("downloadExcelForCouponLocStat"));
	}

	@Test
	public void testInitBinder() throws Exception {

	}
}
