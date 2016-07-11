package com.skplanet.bisportal.controller.bip;

import com.skplanet.bisportal.service.bip.CommonCodeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * The CommonCodeControllerTest class.
 *
 * @author sjune
 */
public class CommonCodeControllerTest {

	MockMvc mockMvc;

	@InjectMocks
	CommonCodeController controller;

	@Mock
	CommonCodeService CommonCodeServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(controller).build();
	}

	@Test
	public void getPocCodes() throws Exception {
		this.mockMvc.perform(get("/commonCode/poc/OBS/OBS001").accept(MediaType.APPLICATION_JSON)).andExpect(
				status().isOk());
	}

	@Test
	public void testGetObsVstPageStaMeasureCodes() throws Exception {
		this.mockMvc.perform(get("/commonCode/measure/ObsVstPage").accept(MediaType.APPLICATION_JSON)).andExpect(
				status().isOk());
	}
}
