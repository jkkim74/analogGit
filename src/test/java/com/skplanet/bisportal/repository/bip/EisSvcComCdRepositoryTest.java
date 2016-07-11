package com.skplanet.bisportal.repository.bip;

import com.skplanet.bisportal.model.bip.EisSvcComCd;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

/**
 * The EisSvcComCdRepositoryTest class.
 *
 * author sjune
 */
public class EisSvcComCdRepositoryTest extends AbstractContextLoadingTest {

	@Autowired
	private EisSvcComCdRepository repository;

	@Test
	public void testGetComCdsBySvcId() {
		// Given
		Long svcId = 25L;

		// When
		List<EisSvcComCd> expectList = repository.getComCdsBySvcId(svcId);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));
	}
}
