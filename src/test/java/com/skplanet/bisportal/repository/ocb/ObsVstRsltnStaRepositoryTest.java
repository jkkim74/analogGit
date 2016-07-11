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
import com.skplanet.bisportal.model.ocb.ObsVstRsltnSta;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;

/**
 * The ObsVstRsltnStaRepositoryTest class
 *
 * @author sjune
 */
@Transactional
public class ObsVstRsltnStaRepositoryTest extends AbstractContextLoadingTest {

	private JqGridRequest jqGridRequest;

	@Autowired
	private ObsVstRsltnStaRepository repository;

	@Before
	public void setUp() throws Exception {
		jqGridRequest = new JqGridRequest();
	}

	@Test
	public void testGetVisitorsRsltnPerDay() {
		// Given
		jqGridRequest.setStartDate("20140101");
		jqGridRequest.setEndDate("20140501");
		jqGridRequest.setDateType(ReportDateType.DAY);

		// When
		List<ObsVstRsltnSta> expectList = repository.getVisitorsRsltnPerDay(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));

	}

	@Test
	public void testGetVisitorsRsltnPerMonth() throws Exception {
		// Given
		jqGridRequest.setStartDate("201401");
		jqGridRequest.setEndDate("201405");
		jqGridRequest.setDateType(ReportDateType.MONTH);

		// When
		List<ObsVstRsltnSta> expectList = repository.getVisitorsRsltnPerMonth(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));

	}

	@Test
	public void testGetVisitorsRsltnPerWeek() throws Exception {
		// Given
		jqGridRequest.setStartDate("20140101");
		jqGridRequest.setEndDate("20140501");
		jqGridRequest.setDateType(ReportDateType.WEEK);

		// When
		List<ObsVstRsltnSta> expectList = repository.getVisitorsRsltnPerWeek(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));

	}
}
