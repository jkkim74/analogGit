package com.skplanet.bisportal.controller.ocb;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.math.BigDecimal;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

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
import com.skplanet.bisportal.model.ocb.*;
import com.skplanet.bisportal.service.ocb.VisitService;
import com.skplanet.bisportal.testsupport.util.TestUtil;

/**
 * The VisitControllerTest class.
 * 
 * @author cookatrice
 * 
 */
@Slf4j
@Transactional
public class VisitControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private VisitController visitController;

	@Mock
	private VisitService visitServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(visitController).build();
	}

	/**
	 * 방문개요
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetEnterForGridPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140721");
		jqGridRequest.setEndDate("20140731");
		jqGridRequest.setPocCode("01");
		jqGridRequest.setRows(-1);
		jqGridRequest.setPage(1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<ObsVstSta> obsVstStas = Lists.newArrayList();
		ObsVstSta obsVstSta = new ObsVstSta();
		obsVstSta.setMeasureValue(new BigDecimal(1234));
		obsVstStas.add(obsVstSta);
		when(visitServiceImpl.getVisitorListForGrid(jqGridRequest)).thenReturn(obsVstStas);
		this.mockMvc
				.perform(
						get(
								"/ocb/visit/outline/grid?dateType=day&startDate=20140721&endDate=20140731&pocCode=01&rows=-1&page=1&sidx=stdDt&sord=asc")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(visitServiceImpl, times(1)).getVisitorListForGrid(jqGridRequest);
		verifyNoMoreInteractions(visitServiceImpl);
	}

	/**
	 * 방문개요(성별, 연령별)
	 */
	@Test
	public void testGetVisitSexAgeForGridPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140721");
		jqGridRequest.setEndDate("20140731");
		jqGridRequest.setRows(-1);
		jqGridRequest.setPage(1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<ObsVstSexAgeSta> obsVstSexAgeStas = Lists.newArrayList();
		ObsVstSexAgeSta obsVstSexAgeSta = new ObsVstSexAgeSta();
		obsVstSexAgeSta.setMeasureValue(new BigDecimal("1234"));
		obsVstSexAgeStas.add(obsVstSexAgeSta);
		when(visitServiceImpl.getVisitSexAgeForGrid(jqGridRequest)).thenReturn(obsVstSexAgeStas);
		this.mockMvc
				.perform(
						get(
								"/ocb/visit/sexage/grid?dateType=day&startDate=20140721&endDate=20140731&rows=-1&page=1&sidx=stdDt&sord=asc")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		// verify(visitServiceImpl, times(1)).getVisitorListForGrid(jqGridRequest);
		// verifyNoMoreInteractions(visitServiceImpl);
	}

	/**
	 * 방문자(언어)
	 */
	@Test
	public void testGetVisitsLangForGridPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140721");
		jqGridRequest.setEndDate("20140731");

		List<ObsVstLngSta> obsVstLngStas = Lists.newArrayList();
		ObsVstLngSta obsVstLngSta = new ObsVstLngSta();
		obsVstLngSta.setMeasureValue(new BigDecimal("1234"));
		obsVstLngStas.add(obsVstLngSta);
		when(visitServiceImpl.getVisitsLangForGrid(jqGridRequest)).thenReturn(obsVstLngStas);
		this.mockMvc
				.perform(
						get("/ocb/visit/lang/grid?dateType=day&startDate=20140721&endDate=20140731").contentType(
                                MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(visitServiceImpl, times(1)).getVisitsLangForGrid(jqGridRequest);
		verifyNoMoreInteractions(visitServiceImpl);
	}

	/**
	 * 방문자(해상도)
	 */
	@Test
	public void testGetVisitsRsltnForGridPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140721");
		jqGridRequest.setEndDate("20140731");

		List<ObsVstRsltnSta> obsVstRsltnStas = Lists.newArrayList();
		ObsVstRsltnSta obsVstRsltnSta = new ObsVstRsltnSta();
		obsVstRsltnSta.setMeasureValue(new BigDecimal("1234"));
		obsVstRsltnStas.add(obsVstRsltnSta);
		when(visitServiceImpl.getVisitsRsltnForGrid(jqGridRequest)).thenReturn(obsVstRsltnStas);
		this.mockMvc
				.perform(
						get("/ocb/visit/rsltn/grid?dateType=day&startDate=20140721&endDate=20140731").contentType(
                                MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(visitServiceImpl, times(1)).getVisitsRsltnForGrid(jqGridRequest);
		verifyNoMoreInteractions(visitServiceImpl);

	}

	/**
	 * 방문자(단말 모델)
	 */
	@Test
	public void testGetVisitorDvcMdlForGridPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140721");
		jqGridRequest.setEndDate("20140731");

		List<ObsVstDvcMdlSta> obsVstDvcMdlStas = Lists.newArrayList();
		ObsVstDvcMdlSta obsVstDvcMdlSta = new ObsVstDvcMdlSta();
		obsVstDvcMdlSta.setMeasureValue(new BigDecimal("1234"));
		obsVstDvcMdlStas.add(obsVstDvcMdlSta);
		when(visitServiceImpl.getVisitorDvcMdlForGrid(jqGridRequest)).thenReturn(obsVstDvcMdlStas);
		this.mockMvc
				.perform(
						get("/ocb/visit/dvcmdl/grid?dateType=day&startDate=20140721&endDate=20140731").contentType(
                                MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(visitServiceImpl, times(1)).getVisitorDvcMdlForGrid(jqGridRequest);
		verifyNoMoreInteractions(visitServiceImpl);

	}

	/**
	 * 방문자(시간대)
	 */
	@Test
	public void testGetVisitTimeZoneForGridPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140721");
		jqGridRequest.setEndDate("20140731");
		jqGridRequest.setPocCode("01");

		List<ObsVstTimeSta> obsVstTimeStas = Lists.newArrayList();
		ObsVstTimeSta obsVstTimeSta = new ObsVstTimeSta();
		obsVstTimeSta.setMeasureValue(new BigDecimal("1234"));
		obsVstTimeStas.add(obsVstTimeSta);
		when(visitServiceImpl.getVisitTimeZoneForGrid(jqGridRequest)).thenReturn(obsVstTimeStas);
		this.mockMvc
				.perform(
						get("/ocb/visit/timezone/grid?dateType=day&startDate=20140721&endDate=20140731&pocCode=01")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(visitServiceImpl, times(1)).getVisitTimeZoneForGrid(jqGridRequest);
		verifyNoMoreInteractions(visitServiceImpl);

	}

	/**
	 * 방문자(Os)
	 */
	@Test
	public void testGetVisitOsForGridPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140721");
		jqGridRequest.setEndDate("20140731");

		List<ObsVstOsSta> obsVstOsStas = Lists.newArrayList();
		ObsVstOsSta obsVstOsSta = new ObsVstOsSta();
		obsVstOsSta.setMeasureValue(new BigDecimal("1234"));
		obsVstOsStas.add(obsVstOsSta);
		when(visitServiceImpl.getVisitOsForGrid(jqGridRequest)).thenReturn(obsVstOsStas);
		this.mockMvc
				.perform(
						get("/ocb/visit/os/grid?dateType=day&startDate=20140721&endDate=20140731").contentType(
                                MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(visitServiceImpl, times(1)).getVisitOsForGrid(jqGridRequest);
		verifyNoMoreInteractions(visitServiceImpl);

	}

	/**
	 * 방문페이지
	 */
	@Test
	public void testGetVisitorsPageForGridPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140721");
		jqGridRequest.setEndDate("20140731");
		jqGridRequest.setMeasureCode("LV");

		List<ObsVstPageSta> obsVstPageStas = Lists.newArrayList();
		ObsVstPageSta obsVstPageSta = new ObsVstPageSta();
		obsVstPageSta.setMeasureValue(new BigDecimal("1234"));
		obsVstPageStas.add(obsVstPageSta);
		when(visitServiceImpl.getVisitorsPageForGrid(jqGridRequest)).thenReturn(obsVstPageStas);
		this.mockMvc
				.perform(
						get("/ocb/visit/page/grid?dateType=day&startDate=20140721&endDate=20140731&measureCode=LV")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(visitServiceImpl, times(1)).getVisitorsPageForGrid(jqGridRequest);
		verifyNoMoreInteractions(visitServiceImpl);

	}

}
