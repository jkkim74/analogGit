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
import com.skplanet.bisportal.model.ocb.ObsSrchClickStoreSta;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;

/**
 * The ObsSrchClickStoreStaRepositoryTest class
 *
 * @author cookstrice
 */
@Transactional
public class ObsSrchClickStoreStaRepositoryTest extends AbstractContextLoadingTest {

	private JqGridRequest jqGridRequest;

	@Autowired
	private ObsSrchClickStoreStaRepository repository;

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
		List<ObsSrchClickStoreSta> expectList = repository.getSearchResultClickStoreForPivotPerDay(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));
	}

	@Test
	public void testPerWeek() {
		// Given
		jqGridRequest.setStartDate("20140101");
		jqGridRequest.setEndDate("20140501");
		jqGridRequest.setDateType(ReportDateType.WEEK);

		// When
		List<ObsSrchClickStoreSta> expectList = repository.getSearchResultClickStoreForPivotPerWeek(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));

	}

	@Test
	public void testPerMonth() {
		// Given
		jqGridRequest.setStartDate("201401");
		jqGridRequest.setEndDate("201405");
		jqGridRequest.setDateType(ReportDateType.MONTH);

		// When
		List<ObsSrchClickStoreSta> expectList = repository.getSearchResultClickStoreForPivotPerMonth(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));
	}

	@Test
	public void testPerDay2() {
		// Given
		jqGridRequest.setStartDate("20140101");
		jqGridRequest.setEndDate("20140501");
		jqGridRequest.setDateType(ReportDateType.DAY);

		// When
		List<ObsSrchClickStoreSta> expectList = repository.getSearchResultClickStoreForGridPerDay(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));
	}

	@Test
	public void testPerWeek2() {
		// Given
		jqGridRequest.setStartDate("20140101");
		jqGridRequest.setEndDate("20140501");
		jqGridRequest.setDateType(ReportDateType.WEEK);

		// When
		List<ObsSrchClickStoreSta> expectList = repository.getSearchResultClickStoreForGridPerWeek(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));

	}

	@Test
	public void testPerMonth2() {
		// Given
		jqGridRequest.setStartDate("201401");
		jqGridRequest.setEndDate("201405");
		jqGridRequest.setDateType(ReportDateType.MONTH);

		// When
		List<ObsSrchClickStoreSta> expectList = repository.getSearchResultClickStoreForGridPerMonth(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));
	}

}
