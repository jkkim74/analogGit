package com.skplanet.bisportal.repository.bip;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.common.model.Condition;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmDlyPrst;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import lombok.extern.slf4j.Slf4j;
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
 * The BpmDlyPrstRepositoryTest class.
 * 
 * @author sjune
 */
@Slf4j
public class BpmDlyPrstRepositoryTest extends AbstractContextLoadingTest {

	@Autowired
	private BpmDlyPrstRepository repository;

	@Test
	public void testGetSummaryDailyResult() {
		// Given
		List<WhereCondition> whereConditions = Lists.newArrayList();
		whereConditions.add(aWhereCondition().svcId(25L).idxClGrpCd("L001").idxClCd("M001").idxCttCd("S001").build());

		Condition condition = new Condition();
		condition.setSearchDate("20131201");
		condition.setWhereConditions(whereConditions);

		// When
		List<BpmDlyPrst> actual = repository.getSummaryDailyResult(condition);

		// Then
		for (BpmDlyPrst bpmDlyPrst : actual) {
			assertThat(bpmDlyPrst.getSvcId(), is(25L));
			assertThat(bpmDlyPrst.getIdxClGrpCd(), is("L001"));
			assertThat(bpmDlyPrst.getIdxClCd(), is("M001"));
			assertThat(bpmDlyPrst.getIdxCttCd(), is("S001"));
			assertNotNull(bpmDlyPrst.getDlyStrdDt());
			assertNotNull(bpmDlyPrst.getDlyRsltVal());
		}
	}

	@Test
	public void testGetSummaryDailyResultSize() {
		// Given
		List<WhereCondition> whereConditions = Lists.newArrayList();
		whereConditions.add(aWhereCondition().svcId(25L).idxClGrpCd("L001").idxClCd("M001").idxCttCd("S001").build());
		whereConditions.add(aWhereCondition().svcId(25L).idxClGrpCd("L003").idxClCd("M001").idxCttCd("S001").build());

		Condition condition = new Condition();
		condition.setSearchDate("20131201");
		condition.setWhereConditions(whereConditions);

		// When
		Set<WhereCondition> expectSet = Sets.newSet();
		List<BpmDlyPrst> actual = repository.getSummaryDailyResult(condition);

		for (BpmDlyPrst bpmDlyPrst : actual) {
			WhereCondition wc = aWhereCondition().svcId(bpmDlyPrst.getSvcId()).idxClGrpCd(bpmDlyPrst.getIdxClGrpCd())
					.idxClCd(bpmDlyPrst.getIdxClCd()).idxCttCd(bpmDlyPrst.getIdxCttCd()).build();
			expectSet.add(wc);
		}

		// Then
		assertThat(expectSet.size(), is(whereConditions.size()));
	}

	@Test
	public void testGetBpmDailyResultSums() {
		// Given
		WhereCondition whereCondition = aWhereCondition().svcId(25L).idxClGrpCd("L001").idxClCd("M001")
				.startDate("20140728").endDate("20140807").build();

		// When
		List<BpmDlyPrst> actual = repository.getBpmDailyResultSums(whereCondition);

		// Then
		for (BpmDlyPrst bpmDlyPrst : actual) {
			assertNotNull(bpmDlyPrst.getIdxClCdGrpNm());
			assertNotNull(bpmDlyPrst.getIdxClCdVal());
			assertNotNull(bpmDlyPrst.getIdxCttCdVal());
			assertNotNull(bpmDlyPrst.getDlyStrdDt());
			assertNotNull(bpmDlyPrst.getDlyRsltVal());
		}
	}
}
