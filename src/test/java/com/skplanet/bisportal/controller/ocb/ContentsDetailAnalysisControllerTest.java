package com.skplanet.bisportal.controller.ocb;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.ReportDateType;
import com.skplanet.bisportal.model.ocb.ObsCntntDscvSta;
import com.skplanet.bisportal.model.ocb.ObsCntntFlyrDetlSta;
import com.skplanet.bisportal.model.ocb.ObsCntntMbilFlyrCopnSta;
import com.skplanet.bisportal.model.ocb.ObsCntntTraraFlyrSta;
import com.skplanet.bisportal.model.ocb.ObsFeedSta;
import com.skplanet.bisportal.model.ocb.ObsPntSta;
import com.skplanet.bisportal.model.ocb.ObsStoreDetlSta;
import com.skplanet.bisportal.model.ocb.ObsStoreOvrlSta;
import com.skplanet.bisportal.service.ocb.ContentsDetailAnalysisService;
import com.skplanet.bisportal.testsupport.util.TestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * The ContentsDetailAnalysisControllerTest class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Slf4j
@Transactional
public class ContentsDetailAnalysisControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private ContentsDetailAnalysisController contentsDetailAnalysisController;

	@Mock
	private ContentsDetailAnalysisService contentsDetailAnalysisServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(contentsDetailAnalysisController).build();
	}

	// feed 20140613~
	/**
	 * Feed 노출 - grid(세로형)
	 */
	public void testGetFeedsExposureForGridPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140613");
		jqGridRequest.setEndDate("20140731");
		jqGridRequest.setRows(-1);
		jqGridRequest.setPage(1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<ObsFeedSta> obsFeedStas = Lists.newArrayList();
		ObsFeedSta obsFeedSta = new ObsFeedSta();
		obsFeedSta.setMeasureValue(new BigDecimal("1234"));
		obsFeedStas.add(obsFeedSta);
		when(contentsDetailAnalysisServiceImpl.getFeedsExposureForGrid(jqGridRequest)).thenReturn(obsFeedStas);
		this.mockMvc
				.perform(
						get(
								"/ocb/contentsDetailAnalisys/feedExposure/grid?dateType=day&startDate=20140613&endDate=20140731&rows=-1&page=1&sidx=stdDt&sord=asc")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(contentsDetailAnalysisServiceImpl, times(1)).getFeedsExposureForGrid(jqGridRequest);
		verifyNoMoreInteractions(contentsDetailAnalysisServiceImpl);
	}

	/**
	 * Feed 클릭 - pivot(세로형)
	 */
	public void testGetFeedsExposureOrderForPivotPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140613");
		jqGridRequest.setEndDate("20140731");

		List<ObsFeedSta> obsFeedStas = Lists.newArrayList();
		ObsFeedSta obsFeedSta = new ObsFeedSta();
		obsFeedSta.setMeasureValue(new BigDecimal("1234"));
		obsFeedStas.add(obsFeedSta);
		when(contentsDetailAnalysisServiceImpl.getFeedsExposureOrderForPivot(jqGridRequest)).thenReturn(obsFeedStas);
		this.mockMvc
				.perform(
						get(
								"/ocb/contentsDetailAnalisys/feedExposureOrder/pivot?dateType=day&startDate=20140613&endDate=20140731")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(contentsDetailAnalysisServiceImpl, times(1)).getFeedsExposureOrderForPivot(jqGridRequest);
		verifyNoMoreInteractions(contentsDetailAnalysisServiceImpl);
	}

	/**
	 * 매장-전체 - pivot(세로형)
	 */
	public void testGetStoreTotalForPivotPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140613");
		jqGridRequest.setEndDate("20140731");

		List<ObsStoreOvrlSta> obsStoreOvrlStas = Lists.newArrayList();
		ObsStoreOvrlSta obsStoreOvrlSta = new ObsStoreOvrlSta();
		obsStoreOvrlSta.setMeasureValue(new BigDecimal("1234"));
		obsStoreOvrlStas.add(obsStoreOvrlSta);
		when(contentsDetailAnalysisServiceImpl.getStoreTotalForPivot(jqGridRequest)).thenReturn(obsStoreOvrlStas);
		this.mockMvc
				.perform(
						get(
								"/ocb/contentsDetailAnalisys/feedExposureOrder/pivot?dateType=day&startDate=20140613&endDate=20140731")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(contentsDetailAnalysisServiceImpl, times(1)).getStoreTotalForPivot(jqGridRequest);
		verifyNoMoreInteractions(contentsDetailAnalysisServiceImpl);
	}

	/**
	 * 매장-개별 - grid(세로형)
	 */
	public void testGetStoreSingleForGridPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140613");
		jqGridRequest.setEndDate("20140731");
		jqGridRequest.setSearchString("11111-11111111");
		jqGridRequest.setRows(-1);
		jqGridRequest.setPage(1);
		jqGridRequest.setSidx("stdDt");
		jqGridRequest.setSord("asc");

		List<ObsStoreDetlSta> obsStoreDetlStas = Lists.newArrayList();
		ObsStoreDetlSta obsStoreDetlSta = new ObsStoreDetlSta();
		obsStoreDetlSta.setMeasureValue(new BigDecimal("1234"));
		obsStoreDetlStas.add(obsStoreDetlSta);
		when(contentsDetailAnalysisServiceImpl.getStoreSingleForGrid(jqGridRequest)).thenReturn(obsStoreDetlStas);
		this.mockMvc
				.perform(
						get(
								"/ocb/contentsDetailAnalisys/storeSingle/grid?dateType=day&startDate=20140721&endDate=20140731&searchString=11111-11111111&rows=-1&page=1&sidx=stdDt&sord=asc")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(contentsDetailAnalysisServiceImpl, times(1)).getStoreSingleForGrid(jqGridRequest);
		verifyNoMoreInteractions(contentsDetailAnalysisServiceImpl);
	}

	/**
	 * 포인트 - pivot
	 */
	public void testGetPntsForPivotPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140601");
		jqGridRequest.setEndDate("20140731");

		List<ObsPntSta> obsPntStas = Lists.newArrayList();
		ObsPntSta obsPntSta = new ObsPntSta();
		obsPntSta.setMeasureValue(new BigDecimal("1234"));
		obsPntStas.add(obsPntSta);
		when(contentsDetailAnalysisServiceImpl.getPntsForPivot(jqGridRequest)).thenReturn(obsPntStas);
		this.mockMvc
				.perform(
						get("ocb/contentsDetailAnalisys/point/pivot?dateType=day&startDate=20140601&endDate=20140731")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(contentsDetailAnalysisServiceImpl, times(1)).getPntsForPivot(jqGridRequest);
		verifyNoMoreInteractions(contentsDetailAnalysisServiceImpl);
	}

	/**
	 * discover - pivot
	 */
	public void testGetDiscoverForPivotPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140601");
		jqGridRequest.setEndDate("20140731");

		List<ObsCntntDscvSta> obsCntntDscvStas = Lists.newArrayList();
		ObsCntntDscvSta obsCntntDscvSta = new ObsCntntDscvSta();
		obsCntntDscvSta.setMeasureValue(new BigDecimal("1234"));
		obsCntntDscvStas.add(obsCntntDscvSta);
		when(contentsDetailAnalysisServiceImpl.getDiscoverForPivot(jqGridRequest)).thenReturn(obsCntntDscvStas);
		this.mockMvc
				.perform(
						get(
								"/ocb/contentsDetailAnalisys/discover/pivot?dateType=day&startDate=20140601&endDate=20140731")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(contentsDetailAnalysisServiceImpl, times(1)).getDiscoverForPivot(jqGridRequest);
		verifyNoMoreInteractions(contentsDetailAnalysisServiceImpl);
	}

	/**
	 * 상권전단 내 매장전단 - pivot
	 */
	public void testGetstoreFlyerInTradeAreaFlyerPivotPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140701");
		jqGridRequest.setEndDate("20140731");
		jqGridRequest.setMeasureCode("UV");

		List<ObsCntntFlyrDetlSta> obsCntntFlyrDetlStas = Lists.newArrayList();
		ObsCntntFlyrDetlSta obsCntntFlyrDetlSta = new ObsCntntFlyrDetlSta();
		obsCntntFlyrDetlSta.setMeasureValue(new BigDecimal("1234"));
		obsCntntFlyrDetlStas.add(obsCntntFlyrDetlSta);
		when(contentsDetailAnalysisServiceImpl.getStoreFlyerInTradeAreaFlyerForPivot(jqGridRequest)).thenReturn(
				obsCntntFlyrDetlStas);
		this.mockMvc
				.perform(
						get(
								"/ocb/contentsDetailAnalisys/storeFlyerInTradeAreaFlyer/pivot?dateType=day&startDate=20140701&endDate=20140731&measureCode=UV")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(contentsDetailAnalysisServiceImpl, times(1)).getStoreFlyerInTradeAreaFlyerForPivot(jqGridRequest);
		verifyNoMoreInteractions(contentsDetailAnalysisServiceImpl);
	}

	/**
	 * 상권전단 - pivot
	 */
	public void testGetStoreFlyerForPivotPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140701");
		jqGridRequest.setEndDate("20140731");
		jqGridRequest.setMeasureCode("UV");

		List<ObsCntntTraraFlyrSta> obsCntntTraraFlyrStas = Lists.newArrayList();
		ObsCntntTraraFlyrSta obsCntntTraraFlyrSta = new ObsCntntTraraFlyrSta();
		obsCntntTraraFlyrSta.setMeasureValue(new BigDecimal("1234"));
		obsCntntTraraFlyrStas.add(obsCntntTraraFlyrSta);
		when(contentsDetailAnalysisServiceImpl.getStoreFlyerForPivot(jqGridRequest)).thenReturn(obsCntntTraraFlyrStas);
		this.mockMvc
				.perform(
						get(
								"/ocb/contentsDetailAnalisys/storeFlyerInTradeAreaFlyer/pivot?dateType=day&startDate=20140701&endDate=20140731&measureCode=UV")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(contentsDetailAnalysisServiceImpl, times(1)).getStoreFlyerForPivot(jqGridRequest);
		verifyNoMoreInteractions(contentsDetailAnalysisServiceImpl);

	}

	@Test
	public void testGetMobileFrontCouponForPivotDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setStartDate("20140601");
		jqGridRequest.setEndDate("20140602");
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setMeasureCode("UV");

		List<ObsCntntMbilFlyrCopnSta> obsCntntMbilFlyrCopnStas = Lists.newArrayList();
		ObsCntntMbilFlyrCopnSta obsCntntMbilFlyrCopnSta = new ObsCntntMbilFlyrCopnSta();
		obsCntntMbilFlyrCopnSta.setMeasureValue(new BigDecimal("1234"));
		obsCntntMbilFlyrCopnStas.add(obsCntntMbilFlyrCopnSta);
		when(contentsDetailAnalysisServiceImpl.getMobileFrontCouponForPivot(jqGridRequest)).thenReturn(obsCntntMbilFlyrCopnStas);
		this.mockMvc
				.perform(get("/ocb/contentsDetailAnalisys/mobileCoupon/pivot")
						.param("dateType","day").param("startDate","20140601").param("endDate","20140602").param("measureCode","UV")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(contentsDetailAnalysisServiceImpl, times(1)).getMobileFrontCouponForPivot(jqGridRequest);
		verifyNoMoreInteractions(contentsDetailAnalysisServiceImpl);
	}

	@Test
	public void testGetMobileFrontCouponForPivotWeek() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setStartDate("20140601");
		jqGridRequest.setEndDate("20140602");
		jqGridRequest.setDateType(ReportDateType.WEEK);
		jqGridRequest.setMeasureCode("LV");

		List<ObsCntntMbilFlyrCopnSta> obsCntntMbilFlyrCopnStas = Lists.newArrayList();
		ObsCntntMbilFlyrCopnSta obsCntntMbilFlyrCopnSta = new ObsCntntMbilFlyrCopnSta();
		obsCntntMbilFlyrCopnSta.setMeasureValue(new BigDecimal("1234"));
		obsCntntMbilFlyrCopnStas.add(obsCntntMbilFlyrCopnSta);

		when(contentsDetailAnalysisServiceImpl.getMobileFrontCouponForPivot(jqGridRequest)).thenReturn(
				obsCntntMbilFlyrCopnStas);
		this.mockMvc
				.perform(
						get(
								"/ocb/contentsDetailAnalisys/mobileCoupon/pivot?startDate=20140601&endDate=20140602&dateType=week&measureCode=LV")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(contentsDetailAnalysisServiceImpl, times(1)).getMobileFrontCouponForPivot(jqGridRequest);
		verifyNoMoreInteractions(contentsDetailAnalysisServiceImpl);
	}
}
