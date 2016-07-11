package com.skplanet.bisportal.controller.bip;

import com.skplanet.bisportal.service.acl.MenuService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * MenuControllerTest class.
 * 
 * @author sjune
 */
public class MenuControllerTest {

	MockMvc mockMvc;

	@InjectMocks
	MenuController controller;

	@Mock
	MenuService menuServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(controller).alwaysExpect(handler().handlerType(MenuController.class)).build();
	}

	@Test
	public void testGetReportServices() throws Exception {
		this.mockMvc.perform(get("/menu/reportServices/PP39093").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetBMDashboardService() throws Exception {
		this.mockMvc.perform(get("/menu/dashboardService/PP39093").accept(MediaType.APPLICATION_JSON)).andExpect(
				status().isOk());
	}

	@Test
	public void testGetAdminService() throws Exception {
		this.mockMvc.perform(get("/menu/adminService/PP39093").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetHelpdeskService() throws Exception {
		this.mockMvc.perform(get("/menu/helpDeskService/PP39093").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testUpdateMenus() throws Exception {
		this.mockMvc
				.perform(
						post("/menu/updateMenus").contentType(MediaType.APPLICATION_JSON).content(
								("[{\"id\":\"292\",\"orderIdx\":\"30\"}, {\"id\":\"823\",\"orderIdx\":\"31\"}]")
										.getBytes())).andExpect(status().isOk())
				.andExpect(handler().methodName("updateMenus"));
	}

	@Test
	public void testUpdateMenu() throws Exception {
		this.mockMvc
				.perform(
						post("/menu/update").contentType(MediaType.APPLICATION_JSON).content(
								("{\"id\":\"292\",\"name\":\"방문개요\",\"menuSearchOptionYn\":\"N\"}").getBytes()))
				.andExpect(status().isOk()).andExpect(handler().methodName("update"));
	}
}
