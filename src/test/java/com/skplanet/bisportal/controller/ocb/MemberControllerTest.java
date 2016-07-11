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
import com.skplanet.bisportal.model.ocb.ObsMbrentSta;
import com.skplanet.bisportal.service.ocb.MemberService;
import com.skplanet.bisportal.testsupport.util.TestUtil;

/**
 * The MemberControllerTest class.
 * 
 * @author ophelisis
 * 
 */
@Slf4j
@Transactional
public class MemberControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private MemberController memberController;

	@Mock
	private MemberService memberServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(memberController).build();
	}

	@Test
	public void testGetEnterForGrid() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140721");
		jqGridRequest.setEndDate("20140731");
		jqGridRequest.setRows(-1);
		jqGridRequest.setPage(1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<ObsMbrentSta> obsMbrentStas = Lists.newArrayList();
		ObsMbrentSta obsMbrentSta = new ObsMbrentSta();
		obsMbrentSta.setMeasureValue(new BigDecimal(1234));
		obsMbrentStas.add(obsMbrentSta);
		when(memberServiceImpl.getEnterForGrid(jqGridRequest)).thenReturn(obsMbrentStas);
		this.mockMvc.perform(get("/ocb/member/enter/grid?dateType=day&startDate=20140721&endDate=20140731&pocCode=01&rows=-1&page=1&sidx=stdDt&sord=asc")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
//				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)))
		;
//		verify(memberServiceImpl, times(1)).getEnterForGrid(jqGridRequest);
//		verifyNoMoreInteractions(memberServiceImpl);
	}

	@Test
	public void testGetEnterDetailForGrid() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140721");
		jqGridRequest.setEndDate("20140731");
		jqGridRequest.setRows(-1);
		jqGridRequest.setPage(1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<ObsMbrentSta> obsMbrentStas = Lists.newArrayList();
		ObsMbrentSta obsMbrentSta = new ObsMbrentSta();
		obsMbrentSta.setMeasureValue(new BigDecimal("1234"));
		obsMbrentStas.add(obsMbrentSta);
		when(memberServiceImpl.getEnterDetailForGrid(jqGridRequest)).thenReturn(obsMbrentStas);
		this.mockMvc.perform(get("/ocb/member/enter/detail/grid?dateType=day&startDate=20140721&endDate=20140731&rows=-1&page=1&sidx=stdDt&sord=asc")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(memberServiceImpl, times(1)).getEnterDetailForGrid(jqGridRequest);
		verifyNoMoreInteractions(memberServiceImpl);
	}
}
