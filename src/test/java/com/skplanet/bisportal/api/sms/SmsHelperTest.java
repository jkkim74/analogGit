package com.skplanet.bisportal.api.sms;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.google.gson.JsonArray;
import lombok.extern.slf4j.Slf4j;

import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;

import skp.bss.msg.rms.front.vo.MultiRequestVo;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class SmsHelperTest extends AbstractContextLoadingTest {

	@Test
	public void sendMMSTest() throws Exception {
		String rcvNums[] = { "01025430571" };
		Injector injector = Guice. createInjector(new AbstractModule() {
			@Override protected void configure() {
			}
		});
		SmsHelper smsHelper = injector.getInstance(SmsHelper.class);
		MultiRequestVo multiRequest = smsHelper.sendMMS(rcvNums, "DAU 장애 알람");
		assertThat(multiRequest.getResultCode(), is("RMS-2401"));
		MultiRequestVo multiRequest1 = smsHelper.sendMMS(rcvNums, "DAU 장애 알람1");
		assertThat(multiRequest1.getResultCode(), is("RMS-2401"));
	}
}
