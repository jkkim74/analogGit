package com.skplanet.bisportal.common.utils;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ArrayUtilTest {
	@Test
	public void addAllTest() throws Exception {
		String[] strArray1 = { "test01", "test02" };
		String[] strArray2 = ArrayUtil.addAll(strArray1, "test03");
		assertThat(strArray2.length, is(3));

	}

	@Test
	public void cloneTest() throws Exception {
		String[] strArray1 = { "test01", "test02" };
		String[] strArray2 = ArrayUtil.clone(strArray1);
		System.out.println(strArray2[0] + ":" + strArray2[1]);
		assertThat(strArray2.length, is(2));
	}
}
