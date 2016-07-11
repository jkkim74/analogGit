package com.skplanet.bisportal.common.utils;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class NumberUtilTest {

	@Test
	public void testCalculateGrowthTest() throws Exception {
		// System.out.println(NumberUtil.calculateGrowth(60704576L, 59876798L));
		assertThat(NumberUtil.calculateGrowth(60704576d, 59876798d), is("+1.4%")); // 1주전
		System.out.println(NumberUtil.calculateGrowth(20d, 10d));
		System.out.println(NumberUtil.calculateGrowth(48.15d, 49.26d));
		BigDecimal bg1 = new BigDecimal("17");
		BigDecimal bg2 = new BigDecimal("3");
		// divide bg1 with bg2 with 3 scale
		BigDecimal bg3 = bg1.divide(bg2, 3, RoundingMode.FLOOR);
		BigDecimal bg4 = bg1.divide(bg2, 3, RoundingMode.CEILING);
		System.out.println(bg3);
		System.out.println(bg4);
		System.out.println(new BigDecimal("28.66").setScale(0, RoundingMode.CEILING));
	}

	@Test
	public void testInRange() throws Exception {
		BigDecimal bg1 = new BigDecimal("3");
		BigDecimal bg2 = new BigDecimal("3");
		BigDecimal bg3 = new BigDecimal("24");
		System.out.println(NumberUtil.inRange(bg1, bg2, bg3));
		assertTrue(NumberUtil.inRange(bg1, bg2, bg3));
	}

	@Test
	public void testIsNumber() throws Exception {
		assertTrue(NumberUtil.isNumber("3434343"));
		assertTrue(NumberUtil.isNumber("3333.56"));
//		System.out.println(Long.parseLong("35.0"));
		System.out.println(Double.parseDouble("35.0"));
	}
}
