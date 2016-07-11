package com.skplanet.bisportal.repository.bip;

import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmSvcCd;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.skplanet.bisportal.testsupport.builder.WhereConditionBuilder.aWhereCondition;
import static junit.framework.Assert.assertNotNull;

/**
 * The BpmSvcCdRepositoryTest class.
 * 
 * @author sjune
 */
@Slf4j
public class BpmSvcCdRepositoryTest extends AbstractContextLoadingTest {

	@Autowired
	private BpmSvcCdRepository repository;

	@Test
	public void testGetBpmSvcs() {
		// When
		List<BpmSvcCd> bpmSvcs = repository.getBpmSvcs();

		// Then
		for (BpmSvcCd bpmSvc : bpmSvcs) {
			assertNotNull(bpmSvc.getSvcCdId());
			assertNotNull(bpmSvc.getSvcCdNm());
			assertNotNull(bpmSvc.getSvcCdTitle());
		}
	}

	@Test
	public void testGetBpmCycleToGrps() {
		// Given
		WhereCondition whereCondition = aWhereCondition().svcId(25L).lnkgCyclCd("D").build();

		// When & Then
		List<BpmSvcCd> bpmCycleToGrps = repository.getBpmCycleToGrps(whereCondition);
		for (BpmSvcCd bpmSvcCd : bpmCycleToGrps) {
			assertNotNull(bpmSvcCd.getSvcCdId());
			assertNotNull(bpmSvcCd.getSvcCdNm());
			assertNotNull(bpmSvcCd.getSvcCdTitle());
		}
	}

	@Test
	public void testGetBpmGrpToCls() {
		// Given
		WhereCondition whereCondition = aWhereCondition().svcId(25L).idxClGrpCd("L001").build();

		// When & Then
		List<BpmSvcCd> bpmGrpToCls = repository.getBpmGrpToCls(whereCondition);
		for (BpmSvcCd bpmSvcCd : bpmGrpToCls) {
			assertNotNull(bpmSvcCd.getSvcCdId());
			assertNotNull(bpmSvcCd.getSvcCdNm());
			assertNotNull(bpmSvcCd.getSvcCdTitle());
		}
	}

	@Test
	public void testGetBpmWkStrds() {
		// Given & When
		List<BpmSvcCd> bpmWkStrds = repository.getBpmWkStrds("201407");

        // Then
		for (BpmSvcCd bpmSvcCd : bpmWkStrds) {
			assertNotNull(bpmSvcCd.getSvcCdId());
			assertNotNull(bpmSvcCd.getSvcCdNm());
			assertNotNull(bpmSvcCd.getSvcCdTitle());
		}
	}
}
