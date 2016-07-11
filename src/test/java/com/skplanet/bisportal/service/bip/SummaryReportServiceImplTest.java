package com.skplanet.bisportal.service.bip;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.common.exception.BizException;
import com.skplanet.bisportal.common.model.Condition;
import com.skplanet.bisportal.common.model.SummaryReportRow;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static com.skplanet.bisportal.testsupport.builder.WhereConditionBuilder.aWhereCondition;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * The SummaryReportServiceTest class.
 * 
 * <pre>
 *  테스트DB에 데이터가 들어있다고 가정한다.
 * </pre>
 * 
 * @author sjune
 */
@Slf4j
public class SummaryReportServiceImplTest extends AbstractContextLoadingTest {

	@Autowired
	private SummaryReportService serviceImpl;

	@Test(expected = BizException.class)
	public void shouldThrowBizExceptionWhenWhereConditionsIsNull() throws Exception {
		Condition condition = new Condition();
		condition.setWhereConditions(Collections.<WhereCondition> emptyList());
		serviceImpl.getSummaryDailyResult(condition);
	}

	@Test
	public void testGetSummaryDailyResult() throws Exception {

		// Given
		List<WhereCondition> whereConditions = Lists.newArrayList();
		whereConditions.add(aWhereCondition().svcId(25L).idxClGrpCd("L001").idxClCd("M001").idxCttCd("S001").build());
		whereConditions.add(aWhereCondition().svcId(25L).idxClGrpCd("L003").idxClCd("M001").idxCttCd("S001").build());

		Condition condition = new Condition();
		condition.setSearchDate("20131201");
		condition.setWhereConditions(whereConditions);

		// When
		List<SummaryReportRow> actual = serviceImpl.getSummaryDailyResult(condition);

		// Then
		assertEquals(2, actual.size());
		for (SummaryReportRow summaryReportRow : actual) {
			assertNotNull(summaryReportRow.getMeasure());
			assertNotNull(summaryReportRow.getBasicMeasureValue());
			assertNotNull(summaryReportRow.getPeriodMeasureValues());
			assertNotNull(summaryReportRow.getOneDayAgoMeasureValue());
			assertNotNull(summaryReportRow.getOneWeekAgoMeasureValue());
			assertNotNull(summaryReportRow.getOneMonthAgoMeasureValue());
		}
	}

	@Test
	public void testGetSummaryWeeklyResult() throws Exception {

		// Given
		List<WhereCondition> whereConditions = Lists.newArrayList();
		whereConditions.add(aWhereCondition().svcId(25L).idxClGrpCd("L001").idxClCd("M001").idxCttCd("S001").build());

		Condition condition = new Condition();
		condition.setSearchDate("20131205");
		condition.setWhereConditions(whereConditions);

		// When
		List<SummaryReportRow> actual = serviceImpl.getSummaryWeeklyResult(condition);
		log.debug("actual:{}", actual);

		// Then
		assertEquals(1, actual.size());

		for (SummaryReportRow summaryReportRow : actual) {
			assertNotNull(summaryReportRow.getMeasure());
			assertNotNull(summaryReportRow.getBasicMeasureValue());
			assertNotNull(summaryReportRow.getPeriodMeasureValues());
			assertNotNull(summaryReportRow.getOneWeekAgoMeasureValue());
			assertNotNull(summaryReportRow.getOneYearAgoMeasureValue());
		}
	}

	@Test
	public void testGetSummaryMonthlyResult() throws Exception {
		// Given
		List<WhereCondition> whereConditions = Lists.newArrayList();
		whereConditions.add(aWhereCondition().svcId(25L).idxClGrpCd("L001").idxClCd("M001").idxCttCd("S001").build());
		whereConditions.add(aWhereCondition().svcId(25L).idxClGrpCd("L003").idxClCd("M001").idxCttCd("S001").build());

		Condition condition = new Condition();
		condition.setSearchDate("201312");
		condition.setWhereConditions(whereConditions);

		// When
		List<SummaryReportRow> actual = serviceImpl.getSummaryMonthlyResult(condition);

		// Then
		assertEquals(2, actual.size());

		for (SummaryReportRow summaryReportRow : actual) {
			assertNotNull(summaryReportRow.getMeasure());
			assertNotNull(summaryReportRow.getBasicMeasureValue());
			assertNotNull(summaryReportRow.getPeriodMeasureValues());
			assertNotNull(summaryReportRow.getOneMonthAgoMeasureValue());
			assertNotNull(summaryReportRow.getOneYearAgoMeasureValue());
		}
	}

}
