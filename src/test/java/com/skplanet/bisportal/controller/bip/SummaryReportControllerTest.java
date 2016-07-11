package com.skplanet.bisportal.controller.bip;

import com.skplanet.bisportal.service.bip.SummaryReportService;
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
 * The SummaryReportControllerTest class
 * 
 * @author sjune
 */
public class SummaryReportControllerTest {

	MockMvc mockMvc;

	@InjectMocks
	SummaryReportController controller;

	@Mock
	SummaryReportService summaryReportService;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(controller).alwaysExpect(handler().handlerType(SummaryReportController.class))
				.build();
	}

	@Test
	public void testGetSummaryReportMeasureByServiceId() throws Exception {
		this.mockMvc.perform(get("/summaryReport/measures/25").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(handler().methodName("getMeasuresByServiceId"));
	}

	@Test
	public void testGetSummaryDailyResult() throws Exception {
		this.mockMvc
				.perform(
						post("/summaryReport/result/daily")
								.contentType(MediaType.APPLICATION_JSON)
								.content(
										"{\"searchDate\":\"20131201\",\"whereConditions\":[{\"svcId\":25,\"idxClGrpCd\":\"L001\",\"idxClCd\":\"M001\",\"idxCttCd\":\"S001\",\"idxCttCdVal\":\"OCB 신규 가입자수\",\"idxCttCdDesc\":\"OCB 신규 가입자수\"}]}"
												.getBytes())).andExpect(status().isOk())
				.andExpect(handler().methodName("getSummaryDailyResult"));
	}

	@Test
	public void testGetSummaryWeeklyResult() throws Exception {
		this.mockMvc
				.perform(
						post("/summaryReport/result/weekly")
								.contentType(MediaType.APPLICATION_JSON)
								.content(
										("{\"searchDate\":\"20131223\",\"whereConditions\":[{\"svcId\":25,\"idxClGrpCd\":\"L001\",\"idxClCd\":\"M001\",\"idxCttCd\":\"S001\",\"idxCttCdVal\":\"OCB 신규 가입자수\",\"idxCttCdDesc\":\"OCB 신규 가입자수\"}]}")
												.getBytes())).andExpect(status().isOk())
				.andExpect(handler().methodName("getSummaryWeeklyResult"));
	}

	@Test
	public void testGetSummaryMonthlyResult() throws Exception {
		this.mockMvc
				.perform(
						post("/summaryReport/result/monthly")
								.contentType(MediaType.APPLICATION_JSON)
								.content(
										"{\"searchDate\":\"201312\",\"whereConditions\":[{\"svcId\":25,\"idxClGrpCd\":\"L001\",\"idxClCd\":\"M001\",\"idxCttCd\":\"S001\",\"idxCttCdVal\":\"OCB 신규 가입자수\",\"idxCttCdDesc\":\"OCB 신규 가입자수\"}]}"
												.getBytes())).andExpect(status().isOk())
				.andExpect(handler().methodName("getSummaryMonthlyResult"));
	}
}
