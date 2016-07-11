package com.skplanet.bisportal.controller.syrup;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.skplanet.bisportal.service.ocb.BleService;
import com.skplanet.bisportal.testsupport.util.TestUtil;

/**
 * The BleControllerTest class.
 * 
 * @author ophelisis
 * 
 */
@Slf4j
@Transactional
public class BleControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private BleController bleController;

	@Mock
	private BleService bleServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(bleController).build();
	}

	@Test
	public void testGetServiceForGrid() throws Exception {
		this.mockMvc.perform(post("/syrup/ble/service/grid?startDate=20150531&endDate=20151109")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(handler().methodName("getServiceForGrid"))
		;
	}

	@Test
	public void testGetServiceForSubGrid() throws Exception {
		this.mockMvc.perform(get("/syrup/ble/service/subGrid?startDate=20150531&endDate=20151109")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(handler().methodName("getServiceForSubGrid"))
		;
	}
}
