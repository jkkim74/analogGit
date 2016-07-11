package com.skplanet.bisportal.controller.ocb;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
import com.skplanet.bisportal.model.ocb.ObsTotPntRpt;
import com.skplanet.bisportal.service.ocb.PointAnalysisService;
import com.skplanet.bisportal.testsupport.util.TestUtil;

/**
 * The PointAnalysisControllerTest class.
 * 
 * @author ophelisis
 * 
 */
@Slf4j
@Transactional
public class PointAnalysisControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private PointAnalysisController pointAnalysisController;

	@Mock
	private PointAnalysisService pointAnalysisServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(pointAnalysisController).build();
	}

	@Test
	public void testGetTotalPointReportForPivot() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140721");
		jqGridRequest.setEndDate("20140731");
		jqGridRequest.setRows(-1);
		jqGridRequest.setPage(1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<ObsTotPntRpt> obsTotPntRpts = Lists.newArrayList();
		ObsTotPntRpt obsTotPntRpt = new ObsTotPntRpt();
		obsTotPntRpt.setMeasureValue(new BigDecimal(1234));
		obsTotPntRpts.add(obsTotPntRpt);
		when(pointAnalysisServiceImpl.getTotalPointReportForPivot(jqGridRequest)).thenReturn(obsTotPntRpts);
		this.mockMvc.perform(get("/ocb/khub/pointAnalysis/totalPointReport/pivot?dateType=day&startDate=20140721&endDate=20140731&pocCode=01&rows=-1&page=1&sidx=stdDt&sord=asc")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8)).andDo(print())
//				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)))
		;
//		verify(pointAnalysisServiceImpl, times(1)).getTotalPointReportForPivot(jqGridRequest);
//		verifyNoMoreInteractions(pointAnalysisServiceImpl);
	}
}
