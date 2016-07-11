package com.skplanet.bisportal.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.skplanet.bisportal.common.utils.Objects.uncheckedCast;

/**
 * Created by cookatrice on 14. 11. 19..
 */

@Slf4j
@Deprecated
public class JiraApiUtil {
	public static void main(String[] args) {
		// getTest();
		// postTest();

	}

	public static void getTest() {
		InputStream content = null;
		BufferedReader in = null;
		try {
			// URL url = new URL("http://jira.skplanet.com/rest/api/2/issue/VOYOP-11");
			// URL url = new
			// URL("http://jira.skplanet.com/rest/api/2/issue/createmeta?projectKeys=VOYOP&issuetypeNames=BI 데이터 오류 문의&expand=projects.issuetypes.fields");
			URL url = new URL("http://jira.skplanet.com/rest/api/2/issue/createmeta?projectKeys=VOYOP");
			// URL url = new URL("http://jira.skplanet.com/rest/api/2/search?jql=assignee=pp39095");
			String myInfo = "pp39095:Wn664483";
			String encoding = Base64.encodeBase64String(myInfo.getBytes("UTF-8"));
			System.out.println(encoding);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// connection.setRequestMethod("POST");
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", "Basic " + encoding);
			content = uncheckedCast(connection.getInputStream());
			in = new BufferedReader(new InputStreamReader(content, "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				log.debug("{}", line);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (content != null) {
					content.close();
				}
			} catch (IOException e) {
				log.error("{}", e);
			}
		}
	}

	private static void postTest() {
		HttpClient httpClient = new DefaultHttpClient();

		try {
			String url = "http://jira.skplanet.com/rest/api/2/issue/";
			String myInfo = "pp39095:Wn664483";
			String encoding = Base64.encodeBase64String(myInfo.getBytes("UTF-8"));
			System.out.println(encoding);

			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Authorization", "BASIC " + encoding);

			System.out.println("executing request " + httpPost.getRequestLine());
			HttpResponse response = httpClient.execute(httpPost);
			// HttpEntity entity = response.getEntity();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void restTemplateTest() throws UnsupportedEncodingException {
		String uri = "http://jira.skplanet.com/rest/api/2/issue/";
		String myInfo = "pp39095:Wn664483";
		String encoding = Base64.encodeBase64String(myInfo.getBytes("UTF-8"));
		System.out.println(encoding);

	}
}
