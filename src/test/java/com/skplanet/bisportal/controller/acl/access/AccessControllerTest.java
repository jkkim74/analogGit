package com.skplanet.bisportal.controller.acl.access;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * The AccessControllerTest class.
 *
 * @author kyoungoh lee
 */
public class AccessControllerTest {
	MockMvc mockMvc;
	@InjectMocks
	AccessController controlller;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		this.mockMvc = standaloneSetup(controlller).alwaysExpect(handler().handlerType(AccessController.class))
				.build();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testAccessLog() throws Exception {
		this.mockMvc.perform(get("/log/access").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(handler().methodName("accessLog"));
	}
}
