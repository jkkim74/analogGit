package com.skplanet.bisportal.repository.syrup;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwCardIssueDtl;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;

public class SmwCardIssueDtlRepositoryTest extends AbstractContextLoadingTest {
	@Autowired
	private SmwCardIssueDtlRepository smwCardIssueDtlRepository;

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetCardIssueMem() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setStartDate("20141129");
		jqGridRequest.setEndDate("20141208");
		List<SmwCardIssueDtl> smwCardIssueDtlList = smwCardIssueDtlRepository.getCardIssueMem(jqGridRequest);
		assertNotNull(smwCardIssueDtlList);
	}

	@Test
	public void testGetCardIssuePaVou() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setStartDate("20141129");
		jqGridRequest.setEndDate("20141208");
		List<SmwCardIssueDtl> smwCardIssueDtlList = smwCardIssueDtlRepository.getCardIssuePaVou(jqGridRequest);
		assertNotNull(smwCardIssueDtlList);
	}
}
