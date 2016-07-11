package com.skplanet.bisportal.repository.bip;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.skplanet.bisportal.model.bip.ComIntgComCd;
import com.skplanet.bisportal.model.bip.CommonCode;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;

/**
 * The ComIntgComCdRepositoryTest class.
 * 
 * @author sjune
 */
@Transactional
public class ComIntgComCdRepositoryTest extends AbstractContextLoadingTest {

	@Autowired
	private ComIntgComCdRepository repository;

	@Test
	public void testGetComIntgComCd() {
		// Given
		ComIntgComCd comIntgComCd = new ComIntgComCd();
		comIntgComCd.setSysIndCd(CommonCode.SysIndCd.OBS.name());
		comIntgComCd.setComGrpCd(CommonCode.ComGrpCd.OBS001.name());

		// When
		List<ComIntgComCd> expectList = repository.getComIntgComCds(comIntgComCd);

		// Then
		assertThat(expectList.size(), greaterThanOrEqualTo(0));
	}
}
