package com.skplanet.bisportal.service.syrup;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import com.skplanet.bisportal.common.model.ReportDateType;
import com.skplanet.bisportal.repository.syrup.SmwCponLocStatRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwCponLocStat;
import com.skplanet.bisportal.model.syrup.SmwCponStatDtl;
import com.skplanet.bisportal.repository.syrup.SmwCponStatDtlRepository;

@RunWith(MockitoJUnitRunner.class)
public class CouponServiceImplTest {
	@Mock
	private SmwCponStatDtlRepository smwCponStatDtlRepository;

	@Mock
	private SmwCponLocStatRepository smwCponLocStatRepository;

	@InjectMocks
	private CouponService couponService = new CouponServiceImpl();

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetCponStatForGrid() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setStartDate("20141129");
		jqGridRequest.setEndDate("20141208");
		List<SmwCponStatDtl> smwCponStatDtlList = couponService.getCponStatForGrid(jqGridRequest);
		assertNotNull(smwCponStatDtlList);
	}

	@Test
	public void testGetCponLocStatForGrid() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setStartDate("20141121");
		jqGridRequest.setEndDate("20141208");
//		List<SmwCponLocStat> smwCponLocStatList = couponService.getCponLocStatForGrid(jqGridRequest);
		List<SmwCponLocStat> smwCponLocStatList = smwCponLocStatRepository.getCponLocStatPerDay(jqGridRequest);
		assertNotNull(smwCponLocStatList);
	}
}
