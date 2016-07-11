package com.skplanet.bisportal.controller.ocb;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.math.BigDecimal;
import java.util.List;

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
import com.skplanet.bisportal.model.ocb.ObsActnSta;
import com.skplanet.bisportal.service.ocb.CustomerService;
import com.skplanet.bisportal.testsupport.util.TestUtil;

/**
 * The CustomerControllerTest class.
 * 
 * @author cookatrice
 * 
 */
@Slf4j
@Transactional
public class CustomerControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private CustomerController customerController;

	@Mock
	private CustomerService customerServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(customerController).build();
	}

	/**
	 * 고객별 액션
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetCustomersActionForGridPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20140701");
		jqGridRequest.setEndDate("20140731");

		List<ObsActnSta> obsActnStas = Lists.newArrayList();
		ObsActnSta obsActnSta = new ObsActnSta();
		obsActnSta.setMeasureValue(new BigDecimal(1234));
		obsActnStas.add(obsActnSta);
		when(customerServiceImpl.getCustomersActionForGrid(jqGridRequest)).thenReturn(obsActnStas);
		this.mockMvc
				.perform(
						get("/ocb/customer/customerAction/grid?dateType=day&startDate=20140701&endDate=20140731")
								.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))// .andDo(print());
				.andExpect(jsonPath("$.rows[0].measureValue", is(1234)));
		verify(customerServiceImpl, times(1)).getCustomersActionForGrid(jqGridRequest);
		verifyNoMoreInteractions(customerServiceImpl);
	}

}
