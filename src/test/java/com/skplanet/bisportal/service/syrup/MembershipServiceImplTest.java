package com.skplanet.bisportal.service.syrup;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwCardIssueDtl;
import com.skplanet.bisportal.model.syrup.SmwRcmdStatDtl;
import com.skplanet.bisportal.repository.syrup.SmwCardIssueDtlRepository;
import com.skplanet.bisportal.repository.syrup.SmwRcmdStatDtlRepository;

@RunWith(MockitoJUnitRunner.class)
public class MembershipServiceImplTest {
	@Mock
	private SmwCardIssueDtlRepository smwCardIssueDtlRepository;

	@Mock
	private SmwRcmdStatDtlRepository smwRcmdStatDtlRepository;

	@InjectMocks
	private MembershipService membershipService = new MembershipServiceImpl();

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetCardIssueMemGrid() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setStartDate("20141129");
		jqGridRequest.setEndDate("20141208");
		List<SmwCardIssueDtl> smwCardIssueDtlList = membershipService.getCardIssueMemGrid(jqGridRequest);
		assertNotNull(smwCardIssueDtlList);
	}

	@Test
	public void testGetCardIssuePaVouGrid() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setStartDate("20141129");
		jqGridRequest.setEndDate("20141208");
		List<SmwCardIssueDtl> smwCardIssueDtlList = membershipService.getCardIssuePaVouGrid(jqGridRequest);
		assertNotNull(smwCardIssueDtlList);
	}

	@Test
	public void testGetSmwRcmdStatDtlGrid() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setStartDate("20141129");
		jqGridRequest.setEndDate("20141208");
		List<SmwRcmdStatDtl> smwRcmdStatDtlList = membershipService.getSmwRcmdStatDtlGrid(jqGridRequest);
		assertNotNull(smwRcmdStatDtlList);
	}
}
