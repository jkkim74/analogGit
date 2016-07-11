package com.skplanet.bisportal.repository;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.repository.ocb.ObsMbrentStaRepository;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;

@Transactional
public class ObsDMbrentStaRepositoryTest extends AbstractContextLoadingTest {

	@Autowired
	private ObsMbrentStaRepository obsDMbrentStaRepository;

	@Test
	public void getSumNewEnterCntPerDay() {
		JqGridRequest jqGRidRequest = new JqGridRequest();
		jqGRidRequest.setPocCode("0");
		jqGRidRequest.setStartDate("20100101");
		jqGRidRequest.setEndDate("20140431");
		jqGRidRequest.setSidx("stdDt");
		jqGRidRequest.setSord("asc");
		assertNotNull(obsDMbrentStaRepository.getEnterForGridPerDay(jqGRidRequest));
	}
}
