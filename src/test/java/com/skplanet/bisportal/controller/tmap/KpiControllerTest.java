package com.skplanet.bisportal.controller.tmap;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.skplanet.bisportal.service.tmap.KpiService;
import com.skplanet.bisportal.testsupport.util.TestUtil;

/**
 * The KpiControllerTest class.
 * 
 * @author ophelisis
 * 
 */
@Slf4j
@Transactional
public class KpiControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private KpiController kpiController;

	@Mock
	private KpiService kpiServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(kpiController).build();
	}

	@Test
	public void testGetDayKpiForGrid() throws Exception {
		this.mockMvc.perform(get(
				"/tmap/kpi/day/grid?endDate=20151109&kpiCode=04")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(handler().methodName("getDayKpiForGrid"))
		;
	}

	@Test
	public void testGetMonKpiForGrid() throws Exception {
		this.mockMvc.perform(get(
				"/tmap/kpi/mon/grid?startDate=201505&endDate=201511")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(handler().methodName("getMonKpiForGrid"))
		;
	}
}
