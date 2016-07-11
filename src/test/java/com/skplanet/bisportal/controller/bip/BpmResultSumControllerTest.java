package com.skplanet.bisportal.controller.bip;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmDlyPrst;
import com.skplanet.bisportal.model.bip.BpmMthStcPrst;
import com.skplanet.bisportal.model.bip.BpmWkStcPrst;
import com.skplanet.bisportal.service.bip.BpmResultSumService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static com.skplanet.bisportal.testsupport.builder.WhereConditionBuilder.aWhereCondition;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * The BpmResultSumControllerTest class.
 * 
 * @author sjune
 */
public class BpmResultSumControllerTest {

	MockMvc mockMvc;

	@InjectMocks
	BpmResultSumController bpmResultSumController;

	@Mock
	BpmResultSumService bpmResultSumServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(bpmResultSumController).alwaysExpect(status().isOk())
				.alwaysExpect(handler().handlerType(BpmResultSumController.class)).build();
	}

	@Test
	public void testGetBpmDailyResultSums() throws Exception {

		WhereCondition whereCondition = aWhereCondition().svcId(25L).idxClGrpCd("L001").idxClCd("M001")
				.startDate("20140728").endDate("20140807").build();

		List<BpmDlyPrst> bpmDlyPrsts = Lists.newArrayList();
		BpmDlyPrst bpmDlyPrst = new BpmDlyPrst();
		bpmDlyPrst.setSvcId(25L);
		bpmDlyPrst.setIdxClGrpCd("L001");
		bpmDlyPrst.setIdxClCd("M001");
		bpmDlyPrst.setIdxClCdGrpNm("신규 가입자수");
		bpmDlyPrst.setIdxClCdVal("1총계");
		bpmDlyPrst.setIdxCttCdVal("총계");
		bpmDlyPrst.setDlyRsltVal(new BigDecimal("1234"));
		bpmDlyPrsts.add(bpmDlyPrst);

		when(bpmResultSumServiceImpl.getBpmDailyResultSums(whereCondition)).thenReturn(bpmDlyPrsts);

		this.mockMvc
				.perform(
						get("/bpmResultSum/daily?svcId=25&idxClGrpCd=L001&idxClCd=M001&startDate=20140728&endDate=20140807"))
				.andExpect(handler().methodName("getBpmDailyResultSums"))
				.andExpect(jsonPath("$rows[0].svcId").value(25))
				.andExpect(jsonPath("$rows[0].idxClGrpCd").value("L001"))
				.andExpect(jsonPath("$rows[0].idxClCd").value("M001"))
				.andExpect(jsonPath("$rows[0].idxClCdGrpNm").value("신규 가입자수"))
				.andExpect(jsonPath("$rows[0].idxClCdVal").value("1총계"))
				.andExpect(jsonPath("$rows[0].idxCttCdVal").value("총계"))
				.andExpect(jsonPath("$rows[0].dlyRsltVal").value(1234));

		verify(bpmResultSumServiceImpl, times(1)).getBpmDailyResultSums(whereCondition);
	}

	@Test
	public void testGetBpmWeeklyResultSums() throws Exception {
		WhereCondition whereCondition = aWhereCondition().svcId(25L).idxClGrpCd("L001").idxClCd("M001")
				.startWeekNumber("2013121").endWeekNumber("2013124").build();

		List<BpmWkStcPrst> bpmWkStcPrsts = Lists.newArrayList();
		BpmWkStcPrst bpmWkStcPrst = new BpmWkStcPrst();
		bpmWkStcPrst.setSvcId(25L);
		bpmWkStcPrst.setIdxClGrpCd("L001");
		bpmWkStcPrst.setIdxClCd("M001");
		bpmWkStcPrst.setIdxClCdGrpNm("신규 가입자수");
		bpmWkStcPrst.setIdxClCdVal("1총계");
		bpmWkStcPrst.setIdxCttCdVal("총계");
		bpmWkStcPrst.setWkStrdVal("12/1주");
		bpmWkStcPrst.setWkStcRsltVal(new BigDecimal(1234));
		bpmWkStcPrsts.add(bpmWkStcPrst);

		when(bpmResultSumServiceImpl.getBpmWeeklyResultSums(whereCondition)).thenReturn(bpmWkStcPrsts);

		this.mockMvc
				.perform(
						get("/bpmResultSum/weekly?svcId=25&idxClGrpCd=L001&idxClCd=M001&startWeekNumber=2013121&endWeekNumber=2013124"))
				.andExpect(handler().methodName("getBpmWeeklyResultSums"))
				.andExpect(jsonPath("$rows[0].svcId").value(25))
				.andExpect(jsonPath("$rows[0].idxClGrpCd").value("L001"))
				.andExpect(jsonPath("$rows[0].idxClCd").value("M001"))
				.andExpect(jsonPath("$rows[0].idxClCdGrpNm").value("신규 가입자수"))
				.andExpect(jsonPath("$rows[0].idxClCdVal").value("1총계"))
				.andExpect(jsonPath("$rows[0].idxCttCdVal").value("총계"))
				.andExpect(jsonPath("$rows[0].wkStrdVal").value("12/1주"))
				.andExpect(jsonPath("$rows[0].wkStcRsltVal").value(1234));

		verify(bpmResultSumServiceImpl, times(1)).getBpmWeeklyResultSums(whereCondition);

	}

	@Test
	public void testGetBpmMonthlyResultSums() throws Exception {

		WhereCondition whereCondition = aWhereCondition().svcId(25L).idxClGrpCd("L001").idxClCd("M001")
				.startDate("201311").endDate("201312").build();

		List<BpmMthStcPrst> bpmMthStcPrsts = Lists.newArrayList();
		BpmMthStcPrst bpmMthStcPrst = new BpmMthStcPrst();
		bpmMthStcPrst.setSvcId(25L);
		bpmMthStcPrst.setIdxClGrpCd("L001");
		bpmMthStcPrst.setIdxClCd("M001");
		bpmMthStcPrst.setIdxClCdGrpNm("신규 가입자수");
		bpmMthStcPrst.setIdxClCdVal("1총계");
		bpmMthStcPrst.setIdxCttCdVal("총계");
		bpmMthStcPrst.setMthStcRsltVal(new BigDecimal(1234));
		bpmMthStcPrsts.add(bpmMthStcPrst);

		when(bpmResultSumServiceImpl.getBpmMonthlyResultSums(whereCondition)).thenReturn(bpmMthStcPrsts);

		this.mockMvc
				.perform(
						get("/bpmResultSum/monthly?svcId=25&idxClGrpCd=L001&idxClCd=M001&startDate=201311&endDate=201312"))
				.andExpect(handler().methodName("getBpmMonthlyResultSums"))
				.andExpect(jsonPath("$rows[0].svcId").value(25))
				.andExpect(jsonPath("$rows[0].idxClGrpCd").value("L001"))
				.andExpect(jsonPath("$rows[0].idxClCd").value("M001"))
				.andExpect(jsonPath("$rows[0].idxClCdGrpNm").value("신규 가입자수"))
				.andExpect(jsonPath("$rows[0].idxClCdVal").value("1총계"))
				.andExpect(jsonPath("$rows[0].idxCttCdVal").value("총계"))
				.andExpect(jsonPath("$rows[0].mthStcRsltVal").value(1234));

		verify(bpmResultSumServiceImpl, times(1)).getBpmMonthlyResultSums(whereCondition);

	}
}
