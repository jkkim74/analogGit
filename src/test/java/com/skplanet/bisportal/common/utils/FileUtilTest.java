package com.skplanet.bisportal.common.utils;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class FileUtilTest {
	@Test
	public void renameAddSuffixTest() throws Exception {
		assertThat(FileUtil.renameAddSuffix("test"), is("test_original"));
		System.out.println(FileUtil.renameAddSuffix("test"));
	}

	@Test
	public void isLocalTest() throws Exception {
		assertThat(FileUtil.isLocal("http://localhost"), is(true));
		System.out.println(FileUtil.isLocal("http://localhost"));
	}
}
