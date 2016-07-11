package com.skplanet.bisportal.controller.oneid;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.skplanet.bisportal.service.oneid.FunnelService;
import com.skplanet.bisportal.testsupport.util.TestUtil;

/**
 * The FunnelControllerTest class.
 * 
 * @author ophelisis
 * 
 */
@Slf4j
@Transactional
public class FunnelControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private FunnelController funnelController;

	@Mock
	private FunnelService funnelServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(funnelController).build();
	}

	@Test
	public void testGetFunnelDatas() throws Exception {
		this.mockMvc.perform(get("/oneid/funnel/chart?svcId=23&dateType=day&startDate=20150531&endDate=20151109&procCd=p1&poc=p&reqSstCd=00000")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(handler().methodName("getFunnelDatas"))
		;
	}
}
