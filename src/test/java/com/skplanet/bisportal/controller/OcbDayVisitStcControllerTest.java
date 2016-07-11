package com.skplanet.bisportal.controller;

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

import com.skplanet.bisportal.service.OcbDayVisitStcService;
import com.skplanet.bisportal.testsupport.util.TestUtil;

/**
 * The OcbDayVisitStcControllerTest class.
 * 
 * @author opheliiss
 * 
 */
@Slf4j
@Transactional
public class OcbDayVisitStcControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private OcbDayVisitStcController ocbDayVisitStcController;

	@Mock
	private OcbDayVisitStcService ocbDayVisitStcServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(ocbDayVisitStcController).build();
	}

	@Test
	public void testGetVisitorListPerDay() throws Exception {
		this.mockMvc.perform(get(
				"/ocbDayVisitStc/visitorList?startDate=201505&endDate=201511")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(handler().methodName("getVisitorListPerDay"))
		;
	}
}
