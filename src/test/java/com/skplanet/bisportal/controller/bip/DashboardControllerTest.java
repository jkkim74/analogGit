package com.skplanet.bisportal.controller.bip;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmDlyPrst;
import com.skplanet.bisportal.model.bip.DaaEwmaStatDaily;
import com.skplanet.bisportal.service.bip.DashboardService;
import com.skplanet.bisportal.testsupport.util.TestUtil;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * The DashboardControllerTest
 * 
 * @author cookatrice
 */
public class DashboardControllerTest {

	MockMvc mockMvc;

	@InjectMocks
	DashboardController dashboardController;

	@Mock
	DashboardService dashboardServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(dashboardController).build();
	}

	/**
	 * 대시보드 데이터 로드
	 */
	@Test
	public void testGetDashboardData() throws Exception {
		WhereCondition whereCondition = new WhereCondition();
		whereCondition.setStartDate("20140701");
		whereCondition.setEndDate("20140731");
		whereCondition.setIdxClGrpCd("L064");
		whereCondition.setSvcId(25l);
		whereCondition.setIdxClCd("M001");
		whereCondition.setIdxCttCd("S001");

		List<BpmDlyPrst> bpmDlyPrsts = Lists.newArrayList();
		BpmDlyPrst bpmDlyPrst = new BpmDlyPrst();
		bpmDlyPrst.setSvcId(25l);
		bpmDlyPrsts.add(bpmDlyPrst);
		when(dashboardServiceImpl.getDashboardData(whereCondition)).thenReturn(bpmDlyPrsts);
		this.mockMvc
				.perform(
						get(
								"/dashboard/data?startDate=20140701&endDate=20140731&idxClGrpCd=L064&svcId=25&idxClCd=M001&idxCttCd=S001")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$[0].svcId", is(25)));
		verify(dashboardServiceImpl, times(1)).getDashboardData(whereCondition);
		verifyNoMoreInteractions(dashboardServiceImpl);
	}

	/**
	 * ewma chart data 로드
	 */
	@Test
	public void testGetEwmaChartData() throws Exception {
		WhereCondition whereCondition = new WhereCondition();
		whereCondition.setSearchDate("20140711");
		whereCondition.setIdxClGrpCd("L019");
		whereCondition.setSvcId(1l);
		whereCondition.setIdxClCd("M001");
		whereCondition.setIdxCttCd("S001");

		List<DaaEwmaStatDaily> daaEwmaStatDailies = Lists.newArrayList();
		DaaEwmaStatDaily daaEwmaStatDaily = new DaaEwmaStatDaily();
		daaEwmaStatDaily.setEwmaValue(new BigDecimal("915173"));
		daaEwmaStatDaily.setEwmaAvg(new BigDecimal("833462"));
		daaEwmaStatDaily.setLclValue(new BigDecimal("633437"));
		daaEwmaStatDaily.setUclValue(new BigDecimal("1033486"));
		daaEwmaStatDailies.add(daaEwmaStatDaily);
		when(dashboardServiceImpl.getEwmaChartData(whereCondition)).thenReturn(daaEwmaStatDailies);
		this.mockMvc
				.perform(
						get(
								"/dashboard/ewmaChart/data?searchDate=20140711&idxClGrpCd=L019&svcId=1&idxClCd=M001&idxCttCd=S001")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				// .andDo(print());
				.andExpect(jsonPath("$[0].ewmaValue", is(915173))).andExpect(jsonPath("$[0].ewmaAvg", is(833462)))
				.andExpect(jsonPath("$[0].lclValue", is(633437))).andExpect(jsonPath("$[0].uclValue", is(1033486)));
		verify(dashboardServiceImpl, times(1)).getEwmaChartData(whereCondition);
		verifyNoMoreInteractions(dashboardServiceImpl);
	}

	/**
	 * OCB ewma chart data 로드 (임시)
	 */
	@Test
	public void testGetOcbEwmaChartData() throws Exception {
		WhereCondition whereCondition = new WhereCondition();
		whereCondition.setSearchDate("20140711");
		whereCondition.setSvcId(25l);
		whereCondition.setKpiId("10003");

		List<DaaEwmaStatDaily> daaEwmaStatDailies = Lists.newArrayList();
		DaaEwmaStatDaily daaEwmaStatDaily = new DaaEwmaStatDaily();
		daaEwmaStatDaily.setEwmaValue(new BigDecimal("74165"));
		daaEwmaStatDailies.add(daaEwmaStatDaily);
		when(dashboardServiceImpl.getOcbEwmaChartData(whereCondition)).thenReturn(daaEwmaStatDailies);
		this.mockMvc
				.perform(get("/dashboard/OcbEwmaChart/data?searchDate=20140711&svcId=25&kpiId=10003")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$[0].ewmaValue", is(74165)));
		verify(dashboardServiceImpl, times(1)).getOcbEwmaChartData(whereCondition);
		verifyNoMoreInteractions(dashboardServiceImpl);

	}

	//@Test
	public void testDashboard() throws Exception {
		String url = "http://172.22.242.209/dashboard/data/add";
		try {
			URL object = new URL(url);
			HttpURLConnection con = (HttpURLConnection) object.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "*/*");
			con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			con.setRequestMethod("POST");
			String data = "{\"dateType\" : \"week\", \"svcId\" : 29, \"strdDt\" : \"20150719\", \"dataSet\" : [{\"dlyStrdDt\":\"20150719\", \"svcId\":29, \"idxClGrpCd\":\"L040\", \"idxClCdGrpNm\":\"주 UV\", \"idxClCd\":\"M001\", \"idxClCdVal\":\"총계\", \"idxCttCd\":\"S001\", \"idxCttCdVal\":\"총계\", \"dlyRsltVal\":64404440},  {\"dlyStrdDt\":\"20150719\", \"svcId\":29, \"idxClGrpCd\":\"L115\", \"idxClCdGrpNm\":\"주 다운로드건수\", \"idxClCd\":\"M001\", \"idxClCdVal\":\"총계\", \"idxCttCd\":\"S001\", \"idxCttCdVal\":\"총계\", \"dlyRsltVal\":768320}]}";
			JSONObject json = new JSONObject(data);
			System.out.println(json);
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(json.toString());
			wr.flush();

			//display what returns the POST request
			StringBuilder sb = new StringBuilder();
			int HttpResult = con.getResponseCode();
			if (HttpResult == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				System.out.println("" + sb.toString());

			} else {
				System.out.println(con.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}
}
