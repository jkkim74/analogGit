package com.skplanet.bisportal.common.model;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * The ReportDateTypeTest class.
 *
 * @author sjune
 */
public class ReportDateTypeTest {

	@Test
	public void testToString() throws Exception {
		assertThat(ReportDateType.DAY.toString(), is("day"));
		assertThat(ReportDateType.WEEK.toString(), is("week"));
		assertThat(ReportDateType.MONTH.toString(), is("month"));
	}
}
