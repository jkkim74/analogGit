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
import com.skplanet.bisportal.model.syrup.SmwCponStatDtl;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;

public class SmwCponStatDtlRepositoryTest extends AbstractContextLoadingTest {
	@Autowired
	private SmwCponStatDtlRepository smwCponStatDtlRepository;

	@Autowired
	private SvcCdRepository svcCdRepository;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetCponStatPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setStartDate("20141129");
		jqGridRequest.setEndDate("20141208");
		List<SmwCponStatDtl> smwCponStatDtlList = smwCponStatDtlRepository.getCponStatDtlPerDay(jqGridRequest);
		assertNotNull(smwCponStatDtlList);
	}

	@Test
	public void testGetCponStatPerWeek() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setStartDate("20141028");
		jqGridRequest.setEndDate("20141208");
		List<SmwCldrWk> smwCldrWkList = svcCdRepository.getWkStrdFrto(jqGridRequest);
		jqGridRequest = getWkFrto(jqGridRequest, smwCldrWkList);
		List<SmwCponStatDtl> smwCponStatDtlList = smwCponStatDtlRepository.getCponStatDtlPerWeek(jqGridRequest);
		assertNotNull(smwCponStatDtlList);
	}

	@Test
	public void testGetCponStatPerMonth() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setStartDate("201406");
		jqGridRequest.setEndDate("201412");
		List<SmwCponStatDtl> smwCponStatDtlList = smwCponStatDtlRepository.getCponStatDtlPerMonth(jqGridRequest);
		assertNotNull(smwCponStatDtlList);
	}
}
