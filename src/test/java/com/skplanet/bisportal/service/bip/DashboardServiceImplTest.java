package com.skplanet.bisportal.service.bip;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import com.skplanet.bisportal.model.bip.DaaEwmaStatDaily;
import com.skplanet.bisportal.repository.bip.DaaEwmaStatDailyRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmDlyPrst;
import com.skplanet.bisportal.repository.bip.BpmDlyPrstRepository;

/**
 * The DashboardServiceImplTest class.
 * 
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class DashboardServiceImplTest {
	@Mock
	private BpmDlyPrstRepository bpmDlyPrstRepository;

	@Mock
	private DaaEwmaStatDailyRepository daaEwmaStatDailyRepository;

	@InjectMocks
	private DashboardService dashboardService = new DashboardServiceImpl();

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetDashboardData() throws Exception {
		WhereCondition whereCondition = new WhereCondition();
		whereCondition.setStartDate("20140101");
		whereCondition.setEndDate("20141231");
		whereCondition.setIdxClGrpCd("L091");
		whereCondition.setSvcId(25l);
		whereCondition.setIdxClCd("M001");
		whereCondition.setIdxCttCd("S001");

		// When
		List<BpmDlyPrst> bpmDlyPrstList = bpmDlyPrstRepository.getDashboardData(whereCondition);

		// Then
		assertThat(bpmDlyPrstList.size(), greaterThanOrEqualTo(0));
	}

	@Test
	public void testGetEwmaChartData() throws Exception {
		WhereCondition whereCondition = new WhereCondition();
		whereCondition.setSearchDate("20140101");
		whereCondition.setIdxClGrpCd("L091");
		whereCondition.setSvcId(25l);

		// When
		List<DaaEwmaStatDaily> daaEwmaStatDailyList = daaEwmaStatDailyRepository.getEwmaChartData(whereCondition);

		// Then
		assertThat(daaEwmaStatDailyList.size(), greaterThanOrEqualTo(0));
	}

	@Test
	public void testGetOcbEwmaChartData() throws Exception {
		WhereCondition whereCondition = new WhereCondition();
		whereCondition.setSearchDate("20140101");
		whereCondition.setKpiId("10000");
		whereCondition.setSvcId(11l);

		// When
		List<DaaEwmaStatDaily> daaEwmaStatDailyList = daaEwmaStatDailyRepository.getOcbEwmaChartData(whereCondition);

		// Then
		assertThat(daaEwmaStatDailyList.size(), greaterThanOrEqualTo(0));
	}
}
