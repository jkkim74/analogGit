package com.skplanet.bisportal.repository.bip;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.common.model.Condition;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmWkStcPrst;
import com.skplanet.bisportal.model.bip.BpmWkStrdInfo;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static com.skplanet.bisportal.testsupport.builder.WhereConditionBuilder.aWhereCondition;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * The BpmWkStcPrstRepositoryTest class.
 * 
 * @author sjune
 */
@Slf4j
public class BpmWkStcPrstRepositoryTest extends AbstractContextLoadingTest {
	@Autowired
	private BpmWkStcPrstRepository repository;

	@Test
	public void testGetSummaryWeeklyResult() {
		// Given
		List<WhereCondition> whereConditions = Lists.newArrayList();
		whereConditions.add(aWhereCondition().svcId(25L).idxClGrpCd("L001").idxClCd("M001").idxCttCd("S001").build());

		Condition condition = new Condition();
		condition.setStartWeekNumber("2013091");
		condition.setEndWeekNumber("2013122");
		condition.setOneYearAgoWeekNumber("2012122");
		condition.setWhereConditions(whereConditions);

		// When
		List<BpmWkStcPrst> actual = repository.getSummaryWeeklyResult(condition);

		// Then
		for (BpmWkStcPrst bpmWkStcPrst : actual) {
			assertNotNull(bpmWkStcPrst.getWkStcStrdYmw());
			assertThat(bpmWkStcPrst.getSvcId(), is(25L));
			assertThat(bpmWkStcPrst.getIdxClGrpCd(), is("L001"));
			assertThat(bpmWkStcPrst.getIdxClCd(), is("M001"));
			assertThat(bpmWkStcPrst.getIdxCttCd(), is("S001"));
			assertNotNull(bpmWkStcPrst.getAplyStaDt());
			assertNotNull(bpmWkStcPrst.getAplyEndDt());
		}

	}

	@Test
	public void testGetSummaryWeeklyResultSize() {
		// Given
		List<WhereCondition> whereConditions = Lists.newArrayList();
		whereConditions.add(aWhereCondition().svcId(25L).idxClGrpCd("L001").idxClCd("M001").idxCttCd("S001").build());
		whereConditions.add(aWhereCondition().svcId(25L).idxClGrpCd("L003").idxClCd("M001").idxCttCd("S001").build());

		Condition condition = new Condition();
		condition.setStartWeekNumber("2013091");
		condition.setEndWeekNumber("2013122");
		condition.setOneYearAgoWeekNumber("2012122");
		condition.setWhereConditions(whereConditions);

		// When
		Set<WhereCondition> expectSet = Sets.newSet();
		List<BpmWkStcPrst> actual = repository.getSummaryWeeklyResult(condition);

		for (BpmWkStcPrst bpmWkStcPrst : actual) {
			WhereCondition wc = aWhereCondition().svcId(bpmWkStcPrst.getSvcId())
					.idxClGrpCd(bpmWkStcPrst.getIdxClGrpCd()).idxClCd(bpmWkStcPrst.getIdxClCd())
					.idxCttCd(bpmWkStcPrst.getIdxCttCd()).build();
			expectSet.add(wc);
		}

		// Then
		assertThat(expectSet.size(), is(whereConditions.size()));
	}

	@Test
	public void testGetBpmWeeklyResultSums() {
		// Given
		WhereCondition whereCondition = aWhereCondition().svcId(25L).idxClGrpCd("L001").idxClCd("M001")
				.startWeekNumber("2013121").endWeekNumber("2013124").build();
		log.debug("whereCondition:{}", whereCondition);

		// When
		List<BpmWkStcPrst> actual = repository.getBpmWeeklyResultSums(whereCondition);

		// Then
		for (BpmWkStcPrst bpmWkStcPrst : actual) {
			log.debug("bpmWkStcPrst:{}", bpmWkStcPrst);
			assertNotNull(bpmWkStcPrst.getIdxClCdGrpNm());
			assertNotNull(bpmWkStcPrst.getIdxClCdVal());
			assertNotNull(bpmWkStcPrst.getIdxCttCdVal());
			assertNotNull(bpmWkStcPrst.getWkStrdVal());
			assertNotNull(bpmWkStcPrst.getWkStcRsltVal());
		}
	}

	@Test
	public void testGetBpmWkStrdInfo() {
		// Given
		WhereCondition whereCondition = new WhereCondition();
		whereCondition.setSearchDate("20131208");

		// When
		BpmWkStrdInfo actual = repository.getBpmWkStrdInfo(whereCondition);

		// Then
		Assert.assertThat(actual.getWkStcStrdYmw(), Matchers.is("2013122")); // 주차
		Assert.assertThat(actual.getWkStaDt(), Matchers.is("20131202"));
		Assert.assertThat(actual.getWkEndDt(), Matchers.is("20131208"));
	}

	@Test
	public void testGetWeekendAgoBpmWkStrdInfo() {

		// Given
		WhereCondition whereCondition = new WhereCondition();
		whereCondition.setSearchDate("20120816");
		whereCondition.setSearchDiff(1);

		// When
		BpmWkStrdInfo actual = repository.getWeekAgoBpmWkStrdInfo(whereCondition);

		// Then
		Assert.assertThat(actual.getWkStcStrdYmw(), Matchers.is("2012082"));
		Assert.assertThat(actual.getWkStaDt(), Matchers.is("20120806"));
		Assert.assertThat(actual.getWkEndDt(), Matchers.is("20120812"));
	}

	@Test
	public void testGetOneYearAgoBpmWkStrdInfo() {
		// Given
		WhereCondition whereCondition = new WhereCondition();
		whereCondition.setSearchDate("20130816");

		// When
		BpmWkStrdInfo actual = repository.getOneYearAgoBpmWkStrdInfo(whereCondition);

		// Then
		Assert.assertThat(actual.getWkStcStrdYmw(), Matchers.is("2012083"));
		Assert.assertThat(actual.getWkStaDt(), Matchers.is("20120813"));
		Assert.assertThat(actual.getWkEndDt(), Matchers.is("20120819"));

	}
}
