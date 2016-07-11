package com.skplanet.bisportal.controller.ocb;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import com.skplanet.bisportal.model.ocb.ObsActvCustRpt;
import com.skplanet.bisportal.model.ocb.ObsActvJoinsRpt;
import com.skplanet.bisportal.service.ocb.ActivityAnalysisService;
import com.skplanet.bisportal.testsupport.util.TestUtil;

/**
 * The ActivityAnalysisControllerTest class.
 * 
 * @author opheliiss
 * 
 */
@Slf4j
@Transactional
public class ActivityAnalysisControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private ActivityAnalysisController activityAnalysisController;

	@Mock
	private ActivityAnalysisService activityAnalysisServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(activityAnalysisController).build();
	}

	@Test
	public void testGetActivityCustomerReportForPivot() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140721");
		jqGridRequest.setEndDate("20140731");
		jqGridRequest.setRows(-1);
		jqGridRequest.setPage(1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<ObsActvCustRpt> obsActvCustRpts = Lists.newArrayList();
		ObsActvCustRpt obsActvCustRpt = new ObsActvCustRpt();
		obsActvCustRpt.setMeasureValue(new BigDecimal(1234));
		obsActvCustRpts.add(obsActvCustRpt);
		when(activityAnalysisServiceImpl.getActivityCustomerReportForPivot(jqGridRequest)).thenReturn(obsActvCustRpts);
		this.mockMvc.perform(get("/ocb/khub/activityAnalysis/activityCustomerReport/pivot?dateType=day&startDate=20140721&endDate=20140731&pocCode=01&rows=-1&page=1&sidx=stdDt&sord=asc")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
//				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)))
		;
//		verify(activityAnalysisServiceImpl, times(1)).getActivityCustomerReportForPivot(jqGridRequest);
//		verifyNoMoreInteractions(activityAnalysisServiceImpl);
	}

	@Test
	public void testGetActivityJoinsReportForPivot() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140721");
		jqGridRequest.setEndDate("20140731");
		jqGridRequest.setRows(-1);
		jqGridRequest.setPage(1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<ObsActvJoinsRpt> obsActvJoinsRpts = Lists.newArrayList();
		ObsActvJoinsRpt obsActvJoinsRpt = new ObsActvJoinsRpt();
		obsActvJoinsRpt.setMeasureValue(new BigDecimal("1234"));
		obsActvJoinsRpts.add(obsActvJoinsRpt);
		when(activityAnalysisServiceImpl.getActivityJoinsReportForPivot(jqGridRequest)).thenReturn(obsActvJoinsRpts);
		this.mockMvc.perform(get("/ocb/khub/activityAnalysis/activityJoinsReport/pivot?dateType=day&startDate=20140721&endDate=20140731&rows=-1&page=1&sidx=stdDt&sord=asc")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(activityAnalysisServiceImpl, times(1)).getActivityJoinsReportForPivot(jqGridRequest);
		verifyNoMoreInteractions(activityAnalysisServiceImpl);
	}
}
