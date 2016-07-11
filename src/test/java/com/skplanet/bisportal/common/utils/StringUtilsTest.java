package com.skplanet.bisportal.common.utils;

import org.junit.Test;
import org.springframework.util.StringUtils;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by pepsi on 15. 3. 30..
 */
public class StringUtilsTest {
	@Test
	public void replaceTest() throws Exception {
		assertThat(StringUtils.replace("010-2543-0571", "-", ""), is("01025430571"));
	}
}
