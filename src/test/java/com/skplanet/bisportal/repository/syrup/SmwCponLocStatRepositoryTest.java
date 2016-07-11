package com.skplanet.bisportal.repository.syrup;

import static com.skplanet.bisportal.common.utils.DateUtil.getWkFrto;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwCldrWk;
import com.skplanet.bisportal.model.syrup.SmwCponLocStat;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;

public class SmwCponLocStatRepositoryTest extends AbstractContextLoadingTest {
	@Autowired
	private SmwCponLocStatRepository smwCponLocStatRepository;

	@Autowired
	private SvcCdRepository svcCdRepository;

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetCponLocStatPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setStartDate("20141121");
		jqGridRequest.setEndDate("20141208");
		List<SmwCponLocStat> smwCponLocStatList = smwCponLocStatRepository.getCponLocStatPerDay(jqGridRequest);
		assertNotNull(smwCponLocStatList);
	}

	@Test
	public void testGetCponLocStatPerWeek() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setStartDate("20141028");
		jqGridRequest.setEndDate("20141208");
		List<SmwCldrWk> smwCldrWkList = svcCdRepository.getWkStrdFrto(jqGridRequest);
		jqGridRequest = getWkFrto(jqGridRequest, smwCldrWkList);
		List<SmwCponLocStat> smwDCponLocStatList = smwCponLocStatRepository.getCponLocStatPerWeek(jqGridRequest);
		assertNotNull(smwDCponLocStatList);
	}

	@Test
	public void testGetCponLocStatPerMonth() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setStartDate("201406");
		jqGridRequest.setEndDate("201412");
		List<SmwCponLocStat> smwCponLocStatList = smwCponLocStatRepository.getCponLocStatPerMonth(jqGridRequest);
		assertNotNull(smwCponLocStatList);
	}
}
