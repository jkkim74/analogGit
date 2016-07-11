package com.skplanet.bisportal.repository.bip;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.common.model.Condition;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmMthStcPrst;
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
 * The BpmMthStcPrstRepositoryTest class.
 * 
 * @author sjune
 */
@Slf4j
public class BpmMthStcPrstRepositoryTest extends AbstractContextLoadingTest {

	@Autowired
	private BpmMthStcPrstRepository repository;

	@Test
	public void testGetSummaryMonthlyResult() {
		// Given
		List<WhereCondition> whereConditions = Lists.newArrayList();
		whereConditions.add(aWhereCondition().svcId(25L).idxClGrpCd("L001").idxClCd("M001").idxCttCd("S001").build());

		Condition condition = new Condition();
		condition.setSearchDate("201312");
		condition.setWhereConditions(whereConditions);

		// When
		List<BpmMthStcPrst> actual = repository.getSummaryMonthlyResult(condition);

		// Then
		for (BpmMthStcPrst bpmMthStcPrst : actual) {
			assertThat(bpmMthStcPrst.getSvcId(), is(25L));
			assertThat(bpmMthStcPrst.getIdxClGrpCd(), is("L001"));
			assertThat(bpmMthStcPrst.getIdxClCd(), is("M001"));
			assertThat(bpmMthStcPrst.getIdxCttCd(), is("S001"));
			assertNotNull(bpmMthStcPrst.getMthStcStrdYm());
		}
	}

	@Test
	public void testGetSummaryMonthlyResultSize() {
		// Given
		List<WhereCondition> whereConditions = Lists.newArrayList();
		whereConditions.add(aWhereCondition().svcId(25L).idxClGrpCd("L001").idxClCd("M001").idxCttCd("S001").build());
		whereConditions.add(aWhereCondition().svcId(25L).idxClGrpCd("L003").idxClCd("M001").idxCttCd("S001").build());

		Condition condition = new Condition();
		condition.setSearchDate("201312");
		condition.setWhereConditions(whereConditions);

		// When
		Set<WhereCondition> expectSet = Sets.newSet();
		List<BpmMthStcPrst> actual = repository.getSummaryMonthlyResult(condition);

		for (BpmMthStcPrst bpmMthStcPrst : actual) {
			WhereCondition wc = aWhereCondition().svcId(bpmMthStcPrst.getSvcId())
					.idxClGrpCd(bpmMthStcPrst.getIdxClGrpCd()).idxClCd(bpmMthStcPrst.getIdxClCd())
					.idxCttCd(bpmMthStcPrst.getIdxCttCd()).build();
			expectSet.add(wc);
		}

		// Then
		assertThat(expectSet.size(), is(whereConditions.size()));
	}

	@Test
	public void testGetBpmMonthlyResultSums() {
		// Given
		WhereCondition whereCondition = aWhereCondition().svcId(25L).idxClGrpCd("L001").idxClCd("M001")
				.startDate("201311").endDate("201312").build();
		log.debug("whereCondition:{}", whereCondition);

		// When
		List<BpmMthStcPrst> actual = repository.getBpmMonthlyResultSums(whereCondition);

		// Then
		for (BpmMthStcPrst bpmMthStcPrst : actual) {
			log.debug("actual:{}", actual);
			assertNotNull(bpmMthStcPrst.getIdxClCdGrpNm());
			assertNotNull(bpmMthStcPrst.getIdxClCdVal());
			assertNotNull(bpmMthStcPrst.getIdxCttCdVal());
			assertNotNull(bpmMthStcPrst.getMthStcStrdYm());
			assertNotNull(bpmMthStcPrst.getMthStcRsltVal());
		}
	}
}
