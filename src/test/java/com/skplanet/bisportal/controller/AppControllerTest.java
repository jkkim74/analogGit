package com.skplanet.bisportal.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Created by sjune on 2014-04-18.
 * 
 * @author sjune
 */
public class AppControllerTest {

	MockMvc mockMvc;

	@InjectMocks
	AppController appController;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(appController).build();
	}

	@Test
	public void testAccessIndex() throws Exception {
		this.mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("app"));
	}

}
