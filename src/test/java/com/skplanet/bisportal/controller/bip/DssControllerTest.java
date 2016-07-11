package com.skplanet.bisportal.controller.bip;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import javax.servlet.http.Cookie;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.skplanet.bisportal.common.resolver.BipUserArgumentResolver;
import com.skplanet.bisportal.model.acl.BipUser;
import com.skplanet.bisportal.model.bip.Dss;
import com.skplanet.bisportal.service.bip.DssService;

/**
 * Created by seoseungho on 2014. 10. 14..
 */
public class DssControllerTest {
	private MockMvc mockMvc;

	@Mock
	private DssService dssServiceImpl;

	@InjectMocks
	private DssController dssController;

	private Cookie voyagerCookie = new Cookie(
			"voyager_master",
			"lwDJtDmOpiWkgbvaRFU1anxCKyh4fRN7xIPhrw3Nl5jRqhX46HHXxGJhm4Tr45l7n3N2iq3LmHO0vKdKAPoiGJ6XORa6TWC2uLceYiGgRhObKC6nmDMyVSkPhmjP2dEWvMH4dUGT2y6N0p8dMeGhwg%3D%3D");

	private String dssJson = "{\"bmIdList\":[272],\"subject\":\"이것이 바로 주제입니다.\",\"content\":\"보고서 내용입니다.\",\"conclusion\":\"결과는 망 입니다.\",\"futureWork\":\"앞으로 뭘 해야 할까요?\",\"dataSource\":\"동네 주민센터에서 구한 자료\",\"analysisStart\":\"2014-10-16T09:47:55.896Z\",\"analysisEnd\":\"2014-10-16T09:47:55.896Z\",\"dataStart\":\"2014-10-16T09:47:55.896Z\",\"dataEnd\":\"2014-10-16T09:47:55.896Z\",\"sampleSize\":\"999999999\",\"variables\":[],\"createId\":\"PP44632\"}";
	private String notValidDssJson = "{\"bmIdList\":[272],\"content\":\"보고서 내용입니다.\",\"conclusion\":\"결과는 망 입니다.\",\"futureWork\":\"앞으로 뭘 해야 할까요?\",\"dataSource\":\"동네 주민센터에서 구한 자료\",\"analysisStart\":\"2014-10-16T09:47:55.896Z\",\"analysisEnd\":\"2014-10-16T09:47:55.896Z\",\"dataStart\":\"2014-10-16T09:47:55.896Z\",\"dataEnd\":\"2014-10-16T09:47:55.896Z\",\"sampleSize\":\"999999999\",\"variables\":[],\"createId\":\"PP44632\"}";

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(dssController).setCustomArgumentResolvers(new BipUserArgumentResolver())
				.alwaysExpect(handler().handlerType(DssController.class)).build();
	}

	@Test
	public void testGetDssList() throws Exception {
		this.mockMvc.perform(get("/dss").param("pageSize", "10").param("page", "1").accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void testGetBmList() throws Exception {
		this.mockMvc.perform(get("/dss/bmList").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	/**
	 * Test success to delete dss
	 * 
	 * @author Seo Seungho
	 */
	@Test
	public void testDeleteDssSuccess() throws Exception {
		// PP44632 사용자에 대한 쿠키 값
		Cookie cookie = new Cookie(
				"voyager_master",
				"lwDJtDmOpiWkgbvaRFU1anxCKyh4fRN7xIPhrw3Nl5jRqhX46HHXxGJhm4Tr45l7n3N2iq3LmHO0vKdKAPoiGJ6XORa6TWC2uLceYiGgRhObKC6nmDMyVSkPhmjP2dEWvMH4dUGT2y6N0p8dMeGhwg%3D%3D");

		BipUser bipUser = new BipUser();
		bipUser.setUsername("PP44632");
		Dss dss = new Dss();
		dss.setCreateUser(bipUser);
		when(dssServiceImpl.getDss(273l)).thenReturn(dss);
		when(dssServiceImpl.deleteDss(273l, bipUser.getUsername())).thenReturn(1);
		this.mockMvc.perform(delete("/dss/273").cookie(cookie).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk());
	}

	/**
	 * @author Seo Seungho Test fail while trying to delete un-exist dss.
	 */
	@Test
	public void testDeleteDssFailBecauseOfDssNotExist() throws Exception {

		this.mockMvc.perform(delete("/dss/" + Long.MAX_VALUE).cookie(voyagerCookie).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	/**
	 * @author Seo Seungho Test success adding new dss
	 */
	@Test
	public void testAddDssSuccess() throws Exception {
		// test용 dss json
		String dssJson = "{\"bmIdList\":[272],\"subject\":\"이것이 바로 주제입니다.\",\"content\":\"보고서 내용입니다.\",\"conclusion\":\"결과는 망 입니다.\",\"futureWork\":\"앞으로 뭘 해야 할까요?\",\"dataSource\":\"동네 주민센터에서 구한 자료\",\"analysisStart\":\"2014-10-16T09:47:55.896Z\",\"analysisEnd\":\"2014-10-16T09:47:55.896Z\",\"dataStart\":\"2014-10-16T09:47:55.896Z\",\"dataEnd\":\"2014-10-16T09:47:55.896Z\",\"sampleSize\":\"999999999\",\"variables\":[],\"createId\":\"PP44632\"}";
		Dss dss = new ObjectMapper().readValue(dssJson, Dss.class);
		when(dssServiceImpl.addDss(dss)).thenReturn(1);
		this.mockMvc.perform(
                post("/dss/").contentType(MediaType.APPLICATION_JSON).content(dssJson).cookie(voyagerCookie)
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
	}

	/**
	 * @author Seo Seungho Test success adding new dss
	 */
	@Test
	public void testAddDssBadRequest() throws Exception {
		// test용 dss json, subject가 없어 벨리데이션 에러.
		this.mockMvc.perform(
                post("/dss/").contentType(MediaType.APPLICATION_JSON).content(notValidDssJson).cookie(voyagerCookie)
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	/**
	 * @author Seo Seungho Test success updating new dss
	 */
	@Test
	public void testUpdateDssSuccess() throws Exception {
        when(dssServiceImpl.updateDss(any(Dss.class))).thenReturn(1);
		// test용 dss json
		this.mockMvc.perform(
                put("/dss/").contentType(MediaType.APPLICATION_JSON).content(dssJson).cookie(voyagerCookie)
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
	}
}
