package com.skplanet.bisportal.repository.syrup;

import static com.skplanet.bisportal.common.utils.DateUtil.getWkFrto;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.ReportDateType;
import com.skplanet.bisportal.model.syrup.SmwCldrWk;
import com.skplanet.bisportal.model.syrup.SmwRcmdStatDtl;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;

public class SmwRcmdStatDtlRepositoryTest extends AbstractContextLoadingTest {
	@Autowired
	private SmwRcmdStatDtlRepository smwRcmdStatDtlRepository;

	@Autowired
	private SvcCdRepository svcCdRepository;

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetSmwRcmdStatDtlPerDay() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20141129");
		jqGridRequest.setEndDate("20141208");
		List<SmwRcmdStatDtl> smwRcmdStatDtlList = smwRcmdStatDtlRepository.getSmwRcmdStatDtlPerDay(jqGridRequest);
		assertNotNull(smwRcmdStatDtlList);
	}

	@Test
	public void testGetSmwRcmdStatDtlPerWeek() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.WEEK);
		jqGridRequest.setStartDate("20141028");
		jqGridRequest.setEndDate("20141208");
		List<SmwCldrWk> smwCldrWkList = svcCdRepository.getWkStrdFrto(jqGridRequest);
		jqGridRequest = getWkFrto(jqGridRequest, smwCldrWkList);
		List<SmwRcmdStatDtl> smwRcmdStatDtlLIst = smwRcmdStatDtlRepository.getSmwRcmdStatDtlPerWeek(jqGridRequest);
		assertNotNull(smwRcmdStatDtlLIst);
	}

	@Test
	public void testGetSmwRcmdStatDtlPerMonth() throws Exception {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.MONTH);
		jqGridRequest.setStartDate("201406");
		jqGridRequest.setEndDate("201212");
		List<SmwRcmdStatDtl> smwRcmdStatDtlList = smwRcmdStatDtlRepository.getSmwRcmdStatDtlPerMonth(jqGridRequest);
		assertNotNull(smwRcmdStatDtlList);
	}
}
