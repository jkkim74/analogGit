package com.skplanet.bisportal.controller.ocb;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.ReportDateType;
import com.skplanet.bisportal.model.ocb.ObsVstExcptPushSta;
import com.skplanet.bisportal.service.ocb.VisitExceptPushService;
import com.skplanet.bisportal.testsupport.util.TestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * The VisitExceptPushControllerTest class.
 * 
 * @author cookatrice
 * 
 */
@Slf4j
@Transactional
public class VisitExceptPushControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private VisitExceptPushController visitExceptPushController;

	@Mock
	private VisitExceptPushService visitExceptPushServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(visitExceptPushController).build();
	}

	/**
	 * 방문개요
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetVisitsExceptPushOutlineForGridPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140701");
		jqGridRequest.setEndDate("20140731");
		jqGridRequest.setRows(-1);
		jqGridRequest.setPage(1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<ObsVstExcptPushSta> obsVstExcptPushStas = Lists.newArrayList();
		ObsVstExcptPushSta obsVstExcptPushSta = new ObsVstExcptPushSta();
		obsVstExcptPushSta.setMeasureValue(new BigDecimal("1234"));
		obsVstExcptPushStas.add(obsVstExcptPushSta);
		when(visitExceptPushServiceImpl.getVisitsExceptPushOutlineForGrid(jqGridRequest)).thenReturn(
				obsVstExcptPushStas);
		this.mockMvc
				.perform(
						get(
								"/ocb/visitExceptPush/outline/grid?dateType=day&startDate=20140701&endDate=20140731&rows=-1&page=1&sidx=stdDt&sord=asc")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(visitExceptPushServiceImpl, times(1)).getVisitsExceptPushOutlineForGrid(jqGridRequest);
		verifyNoMoreInteractions(visitExceptPushServiceImpl);
	}

}
