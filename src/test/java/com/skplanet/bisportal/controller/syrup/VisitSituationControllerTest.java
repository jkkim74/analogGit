package com.skplanet.bisportal.controller.syrup;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.skplanet.bisportal.service.syrup.VisitSituationService;
import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.skplanet.bisportal.service.ocb.BleService;
import com.skplanet.bisportal.testsupport.util.TestUtil;

/**
 * The VisitSituationControllerTest class.
 * 
 * @author ophelisis
 * 
 */
@Slf4j
@Transactional
public class VisitSituationControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private VisitSituationController visitSituationController;

	@Mock
	private VisitSituationService visitSituationServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(visitSituationController).build();
	}

	@Test
	public void testGetAppVisitSituation() throws Exception {
		this.mockMvc.perform(get(
				"/syrup/visitSituation/app?dateType=day&dimensions=standardDate%7Cmeasure&endDate=20151109&startDate=20151103")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(handler().methodName("getAppVisitSituation"))
		;
	}

	@Test
	public void testGetMenuVisitSituation() throws Exception {
		this.mockMvc.perform(get("/syrup/visitSituation/menu?dateType=day&dimensions=standardDate%7Cmeasure%7Cmenu%7CmenuDesc&endDate=20151109&startDate=20151109")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(handler().methodName("getMenuVisitSituation"))
		;
	}

	@Test
	public void testGetSyrupDauForPivot() throws Exception {
		this.mockMvc.perform(get("/syrup/visitSituation/inflRt/pivot?dateType=day&endDate=20151109&startDate=20151031")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(handler().methodName("getSyrupDauForPivot"))
		;
	}
}
