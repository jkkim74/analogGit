package com.skplanet.bisportal.repository.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.ReportDateType;
import com.skplanet.bisportal.model.ocb.ObsStoreDetlSta;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

/**
 * The ObsStoreDetlStaRepositoryTest class
 *
 * @author cookstrice
 */
@Transactional
public class ObsStoreDetlStaRepositoryTest extends AbstractContextLoadingTest {

	private JqGridRequest jqGridRequest;

	@Autowired
	private ObsStoreDetlStaRepository repository;

	@Before
	public void setUp() {
		jqGridRequest = new JqGridRequest();
	}

	@Test
    @Ignore("쿼리 속도 체크 필요함")
	public void testPerDay() {
        // TODO 쿼리 속도 체크

		// Given
		jqGridRequest.setStartDate("20140101");
		jqGridRequest.setEndDate("20140501");
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setSidx("stdDt");

		// When
		List<ObsStoreDetlSta> expectList = repository.getStoreSingleForPivotPerDay(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));
	}

	@Test
    @Ignore("쿼리 속도 체크 필요함")
	public void testPerWeek() {
        // TODO 쿼리 속도 체크

		// Given
		jqGridRequest.setStartDate("20140101");
		jqGridRequest.setEndDate("20140501");
		jqGridRequest.setDateType(ReportDateType.WEEK);
		jqGridRequest.setSidx("stdDt");

		// When
		List<ObsStoreDetlSta> expectList = repository.getStoreSingleForPivotPerWeek(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));

	}

	@Test
    @Ignore("쿼리 속도 체크 필요함")
	public void testPerMonth() {
        // TODO 쿼리 속도 체크

		// Given
		jqGridRequest.setStartDate("201401");
		jqGridRequest.setEndDate("201405");
		jqGridRequest.setDateType(ReportDateType.MONTH);
		jqGridRequest.setSidx("stdDt");

		// When
		List<ObsStoreDetlSta> expectList = repository.getStoreSingleForPivotPerMonth(jqGridRequest);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));
	}

}
