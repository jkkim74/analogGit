package com.skplanet.bisportal.repository.ocb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.ReportDateType;
import com.skplanet.bisportal.model.ocb.ObsPushCnfgSta;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;

/**
 * The ObsPushCnfgStaRepositoryTest class
 *
 * @author cookstrice
 */
@Transactional
public class ObsPushCnfgStaRepositoryTest extends AbstractContextLoadingTest {

	private JqGridRequest jqGridRequest;

	@Autowired
	private ObsPushCnfgStaRepository repository;

	@Before
	public void setUp() {
		jqGridRequest = new JqGridRequest();
	}

	@Test
	public void testPerDay() {
		// Given
		jqGridRequest.setStartDate("20140101");
		jqGridRequest.setEndDate("20140501");
		jqGridRequest.setDateType(ReportDateType.DAY);

		// When
		List<ObsPushCnfgSta> expectList = repository.getNotificationSetForPivotPerDay(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));
	}

}
