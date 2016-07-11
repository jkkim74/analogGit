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
import com.skplanet.bisportal.model.ocb.ObsCntntTraraFlyrSta;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;

/**
 * The ObsCntntTraraFlyrStaRepositoryTest class
 * 
 * @author sjune
 */
@Transactional
public class ObsCntntTraraFlyrStaRepositoryTest extends AbstractContextLoadingTest {

	private JqGridRequest jqGridRequest;

	@Autowired
	private ObsCntntTraraFlyrStaRepository repository;

	@Before
	public void setUp() {
		jqGridRequest = new JqGridRequest();
	}

	@Test
	public void testGetStoreFlyerForPivotPerDay() {
		// Given
		jqGridRequest.setStartDate("20140701");
		jqGridRequest.setEndDate("20140707");
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setMeasureCode("UV");

		// When
		List<ObsCntntTraraFlyrSta> expectList = repository.getStoreFlyerForPivotPerDay(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));

	}

	@Test
	public void testGetStoreFlyerForPivotPerWeek() {
		// Given
		jqGridRequest.setStartDate("20140601");
		jqGridRequest.setEndDate("20140630");
		jqGridRequest.setDateType(ReportDateType.WEEK);
		jqGridRequest.setMeasureCode("PV");

		// When
		List<ObsCntntTraraFlyrSta> expectList = repository.getStoreFlyerForPivotPerWeek(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));

	}

	@Test
	public void testGetStoreFlyerForPivotPerMonth() {
		// Given
		jqGridRequest.setStartDate("201401");
		jqGridRequest.setEndDate("201406");
		jqGridRequest.setDateType(ReportDateType.MONTH);
		jqGridRequest.setMeasureCode("LV");

		// When
		List<ObsCntntTraraFlyrSta> expectList = repository.getStoreFlyerForPivotPerMonth(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));

	}
}
