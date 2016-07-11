package com.skplanet.bisportal.service.bip;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import com.skplanet.bisportal.common.model.BasicDateWeekNumber;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.skplanet.bisportal.model.bip.CommonCode;
import com.skplanet.bisportal.common.model.SearchCodeSection;
import com.skplanet.bisportal.model.bip.ComIntgComCd;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;

/**
 * The CommonCodeServiceTest class.
 * 
 * @author sjune
 */
@Transactional
public class CommonCodeServiceImplTest extends AbstractContextLoadingTest {
	@Autowired
	private CommonCodeService commonCodeServiceImpl;

	@Test
	public void testGetPocCodes() {
		// When
		ComIntgComCd comIntgComCd = new ComIntgComCd();
		comIntgComCd.setSysIndCd(CommonCode.SysIndCd.OBS.name());
		comIntgComCd.setComGrpCd(CommonCode.ComGrpCd.OBS001.name());
		List<SearchCodeSection> pocCodes = commonCodeServiceImpl.getPocCodes(comIntgComCd);

		// Then
		assertThat(pocCodes.size(), greaterThan(0));
	}

	@Test
	public void testGetObsVstPageStaMeasures() {
		// When
		List<SearchCodeSection> measureCodes = commonCodeServiceImpl.getObsVstPageStaMeasureCodes();
		// Then
		assertThat(measureCodes.size(), greaterThan(0));
	}

	@Test
	public void testGetSummaryReportWeekNumber() {

		// Given && When
		BasicDateWeekNumber basicDateWeekNumber = commonCodeServiceImpl.getBasicDateWeekNumber("20140723");

		// Then
		MatcherAssert.assertThat("2014074", is(basicDateWeekNumber.getBasic()));
		MatcherAssert.assertThat("2014073", is(basicDateWeekNumber.getOneWeekAgo()));
		MatcherAssert.assertThat("2014043", is(basicDateWeekNumber.getFourteenWeekAgo()));
		MatcherAssert.assertThat("2013074", is(basicDateWeekNumber.getOneYearAgo()));
	}
}
