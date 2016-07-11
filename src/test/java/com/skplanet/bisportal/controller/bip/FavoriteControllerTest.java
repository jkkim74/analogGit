package com.skplanet.bisportal.controller.bip;

import com.skplanet.bisportal.service.bip.FavoriteService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * The FavoriteControllerTest
 * 
 * @author sjune
 */
public class FavoriteControllerTest {

	MockMvc mockMvc;

	@InjectMocks
	FavoriteController controller;

	@Mock
	FavoriteService summaryReportService;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(controller).alwaysExpect(handler().handlerType(FavoriteController.class))
				.build();
	}

	@Test
	public void testGetFavoritesByUsername() throws Exception {
		this.mockMvc.perform(get("/favorite/list/PP39094").accept(MediaType.APPLICATION_JSON)).andExpect(
				status().isOk());
	}

	@Test
	public void testAddFavorites() throws Exception {
		this.mockMvc
				.perform(
						post("/favorite/adds")
								.contentType(MediaType.APPLICATION_JSON)
								.content(
										("[{\"serviceCode\":\"ocb\",\"categoryCode\":\"summary\",\"serviceName\":\"Ok Cashbag\",\"categoryName\":\"요약 리포트\", \"orderIdx\":\"30\", \"username\":\"PP39094\",\"commonCodeName\":\"RS\"}"
												+ ",{\"serviceCode\":\"ocb\",\"categoryCode\":\"02\",\"menuCode\":\"0201\", \"serviceName\":\"Ok Cashbag\",\"categoryName\":\"방문\",\"menuName\":\"방문개요\", \"orderIdx\":\"31\", \"username\":\"PP39094\",\"commonCodeName\":\"RS\"}]"
												+ ",{\"serviceCode\":\"ocb\",\"categoryCode\":\"02\",\"menuCode\":\"0202\",\"commonCodeName\":\"RS\", \"serviceName\":\"Ok Cashbag\",\"categoryName\":\"방문\",\"menuName\":\"방문개요(성별,연령)\", \"orderIdx\":\"31\", \"username\":\"PP39094\",\"commonCodeName\":\"RS\"}]")
												.getBytes())).andExpect(status().isOk())
				.andExpect(handler().methodName("addFavorites"));
	}

	@Test
	public void testAddFavorite() throws Exception {
		this.mockMvc
				.perform(
						post("/favorite/add")
								.contentType(MediaType.APPLICATION_JSON)
								.content(
										("{\"serviceCode\":\"ocb\",\"categoryCode\":\"summary\",\"commonCodeName\":\"RS\", \"serviceName\":\"Ok Cashbag\",\"categoryName\":\"요약 리포트\", \"orderIdx\":\"30\", \"username\":\"PP39094\"}")
												.getBytes())).andExpect(status().isOk())
				.andExpect(handler().methodName("addFavorite"));
	}

	@Test
	public void testCreateFavoriteWithBadRequest() throws Exception {
		this.mockMvc
				.perform(
						post("/favorite/add")
								.contentType(MediaType.APPLICATION_JSON)
								.content(
										"{\"badServiceCode\":\"ocb\",\"basCategoryCode\":\"02\",\"menuCode\":\"0201\",\"commonCodeName\":\"RS\",\"username\":\"PP39094\"}}"
												.getBytes())).andExpect(status().isBadRequest());
	}

	@Test
	public void testDeleteFavorite() throws Exception {
		this.mockMvc.perform(delete("/favorite/delete/1234").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(handler().methodName("deleteFavorite"));
	}

	@Test
	public void testGet() throws Exception {
		this.mockMvc
				.perform(
						get(
								"/favorite/get?username=PP39094&serviceCode=ocb&categoryCode=0201&menuCode=01&commonCodeName=RS")
								.contentType(MediaType.TEXT_PLAIN)).andExpect(status().isOk())
				.andExpect(handler().methodName("getFavorite"));
	}

}
