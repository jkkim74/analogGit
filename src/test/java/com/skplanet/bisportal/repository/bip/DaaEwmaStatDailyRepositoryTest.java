package com.skplanet.bisportal.repository.bip;

import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.DaaEwmaStatDaily;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;

/**
 * The DaaEwmaStatDailyRepositoryTest class
 * 
 * @author cookatrice
 */
@Transactional
public class DaaEwmaStatDailyRepositoryTest extends AbstractContextLoadingTest {

	private WhereCondition whereCondition;

	@Autowired
	private DaaEwmaStatDailyRepository repository;

	@Before
	public void setUp() {
		whereCondition = new WhereCondition();
	}

	@Test
	public void testGetEwmaChartData() {
		// Given
		whereCondition.setSearchDate("20140711");
		whereCondition.setSvcId((long) 1);
		whereCondition.setIdxClGrpCd("L013");

		// When
		List<DaaEwmaStatDaily> expectList = repository.getEwmaChartData(whereCondition);

		// Then
		MatcherAssert.assertThat(expectList.size(), greaterThanOrEqualTo(0));
	}

	@Test
	public void testGetOcbEwmaChartData() {
		// Given
		whereCondition.setSearchDate("20140711");
		whereCondition.setSvcId((long) 25);
		whereCondition.setKpiId("10004");

		// When
		List<DaaEwmaStatDaily> expectList = repository.getOcbEwmaChartData(whereCondition);

		// Then
		MatcherAssert.assertThat(expectList.size(), greaterThanOrEqualTo(0));
	}

}
