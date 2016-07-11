package com.skplanet.bisportal.common.utils;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class UtilsTest {
  @Test
  public void getQueryParamTest() throws Exception {
    String queryString = "a=234&b=456&c=567";
    Map<String, Object> parsMap = Utils.getQueryParam(queryString);
    assertThat((String) parsMap.get("c"), is("567"));
  }

  @Test
  public void splitTest() throws Exception {
    String value = "SKP_MGT_DAY_1_20140811.dat";
    String date = StringUtils.split(value, "_")[4].substring(0, 8);
    assertThat(date, is("20140811"));
	BigDecimal a = new BigDecimal("20403922368");
	BigDecimal b = a.divide(new BigDecimal("1000"));
	System.out.println(b);
    int score = 80;
    System.out.println(60 < score);
  }
}
