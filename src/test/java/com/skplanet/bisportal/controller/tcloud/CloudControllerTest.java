package com.skplanet.bisportal.controller.tcloud;

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

import com.skplanet.bisportal.service.tcloud.TcloudService;
import com.skplanet.bisportal.testsupport.util.TestUtil;

/**
 * The CloudControllerTest class.
 * 
 * @author ophelisis
 * 
 */
@Slf4j
@Transactional
public class CloudControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private CloudController cloudController;

	@Mock
	private TcloudService cloudServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(cloudController).build();
	}

	@Test
	public void testGetMenuStat() throws Exception {
		this.mockMvc.perform(get(
				"/tcloud/cloud/getMenuStat?commonGroupCode=TCD5010&dateType=day&endDate=20151109&startDate=20151031")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(handler().methodName("getMenuStat"))
		;
	}
}
