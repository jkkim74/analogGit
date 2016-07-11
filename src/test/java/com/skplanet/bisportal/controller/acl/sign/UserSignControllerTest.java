package com.skplanet.bisportal.controller.acl.sign;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.skplanet.bisportal.service.acl.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * The UserSignControllerTest class.
 *
 * @author kyoungoh lee
 */
public class UserSignControllerTest {
	MockMvc mockMvc;
	@InjectMocks
	UserSignController controlller;

	@Mock
	private UserService userServiceImpl;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		this.mockMvc = standaloneSetup(controlller).alwaysExpect(handler().handlerType(UserSignController.class))
				.build();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testCountSign() throws Exception {
		this.mockMvc.perform(get("/sign/cnt/?loginId=PP44651").contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(handler().methodName("countSign"));
	}

	@Test
	public void testSign() throws Exception {
		this.mockMvc
				.perform(
						post("/sign/create").contentType(MediaType.APPLICATION_JSON).content(
								("{\"loginId\":\"PP44651\"}").getBytes())).andExpect(status().isOk()).andDo(print())
				.andExpect(handler().methodName("sign"));
	}

	@Test
	public void testGetSign() throws Exception {
		this.mockMvc.perform(get("/sign/get/?loginId=PP44651").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andExpect(handler().methodName("getSign"));

	}
}
