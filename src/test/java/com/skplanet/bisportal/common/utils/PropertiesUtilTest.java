package com.skplanet.bisportal.common.utils;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class PropertiesUtilTest {
  @Test
  public void testGetProperty() throws Exception {
    String test = PropertiesUtil.getProperty("ldap.url");
    assertThat(test, is("ldap://10.40.29.172:389"));
  }
}
