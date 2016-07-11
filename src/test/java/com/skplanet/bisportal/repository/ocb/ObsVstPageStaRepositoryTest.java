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
import com.skplanet.bisportal.model.ocb.ObsVstPageSta;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;

/**
 * The ObsVstPageStaRepositoryTest class
 *
 * @author sjune
 */
@Transactional
public class ObsVstPageStaRepositoryTest extends AbstractContextLoadingTest {

	private JqGridRequest jqGridRequest;

	@Autowired
	private ObsVstPageStaRepository repository;

	@Before
	public void setUp() {
		jqGridRequest = new JqGridRequest();
	}

	@Test
	public void testGetVisitorsPagePerDay() {
		// Given
		jqGridRequest.setStartDate("20140101");
		jqGridRequest.setEndDate("20140501");
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setMeasureCode("LV");

		// When
		List<ObsVstPageSta> expectList = repository.getVisitorsPagePerDay(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));
	}

	@Test
	public void testGetVisitorsPagePerMonth() {
		// Given
		jqGridRequest.setStartDate("201401");
		jqGridRequest.setEndDate("201405");
		jqGridRequest.setDateType(ReportDateType.MONTH);
		jqGridRequest.setMeasureCode("LV");
		// When
		List<ObsVstPageSta> expectList = repository.getVisitorsPagePerMonth(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));

	}

	@Test
	public void testGetVisitorsPagePerWeek() {
		// Given
		jqGridRequest.setStartDate("20140101");
		jqGridRequest.setEndDate("20140501");
		jqGridRequest.setDateType(ReportDateType.WEEK);
		jqGridRequest.setMeasureCode("LV");

		// When
		List<ObsVstPageSta> expectList = repository.getVisitorsPagePerWeek(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));

	}
}
