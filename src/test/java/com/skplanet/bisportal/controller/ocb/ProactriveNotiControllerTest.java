package com.skplanet.bisportal.controller.ocb;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.math.BigDecimal;
import java.util.List;

import com.skplanet.bisportal.service.ocb.OcbMktPushSndRsltService;
import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.ReportDateType;
import com.skplanet.bisportal.model.ocb.ObsPushCnfgSta;
import com.skplanet.bisportal.service.ocb.ProactiveNotiService;
import com.skplanet.bisportal.testsupport.util.TestUtil;

/**
 * The ProactriveNotiControllerTest class.
 * 
 * @author cookatrice
 * 
 */
@Slf4j
@Transactional
public class ProactriveNotiControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private ProactriveNotiController proactriveNotiController;

	@Mock
	private ProactiveNotiService proactiveNotiServiceImpl;

    @Mock
    private OcbMktPushSndRsltService ObsDPushRespStaServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(proactriveNotiController).build();
	}

	/**
	 * 푸쉬 알림설정
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetNotificationSetForPivotPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140721");
		jqGridRequest.setEndDate("20140731");

		List<ObsPushCnfgSta> obsPushCnfgStas = Lists.newArrayList();
		ObsPushCnfgSta obsPushCnfgSta = new ObsPushCnfgSta();
		obsPushCnfgSta.setMeasureValue(new BigDecimal("1234"));
		obsPushCnfgStas.add(obsPushCnfgSta);
		when(proactiveNotiServiceImpl.getNotificationSetForPivot(jqGridRequest)).thenReturn(obsPushCnfgStas);
		this.mockMvc
				.perform(
						get("/ocb/proactiveNoti/notificationSet/pivot?dateType=day&startDate=20140721&endDate=20140731")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(proactiveNotiServiceImpl, times(1)).getNotificationSetForPivot(jqGridRequest);
		verifyNoMoreInteractions(proactiveNotiServiceImpl);
	}

    /**
     * 마케팅 Push별 발송 결과
     *
     * @throws Exception
     */
    @Test
    public void testGetMktPushSndRslt() throws Exception {
        this.mockMvc.perform(
                get("/ocb/proactiveNoti/mktPushSndRslt/get")
						.param("startDate","20141006").param("endDate","20141015")
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
                .andExpect(status().isOk())
				.andExpect(handler().methodName("getMktPushSndRslt"));
    }
    /**
     * 마케팅 Push별 발송 결과 엑셀 출력
     *
     * @throws Exception
     */
    @Test
    public void testDownloadExcelForMktPushSndRslt() throws Exception {
        this.mockMvc.perform(
                post("/ocb/proactiveNoti/downloadExcelForMktPushSndRslt").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("downloadExcelForMktPushSndRslt")
                );
    }
}
