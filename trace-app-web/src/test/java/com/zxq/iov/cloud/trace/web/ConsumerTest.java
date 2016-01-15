package com.zxq.iov.cloud.trace.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ContextConfiguration(locations = { "classpath:applicationContext-dubbo-test.xml" })
public class ConsumerTest extends AbstractJUnit4SpringContextTests {

	@Test
	public void testSongSearch() {
		String url = "http://localhost:8080/trace-app-web/test/aaa";
		String result = doHttp(url, "GET");
		String hello = getJsonToObj(result, "data", String.class);
		Assert.assertNotNull(result, hello);
	}

	private static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	private <T> T getJsonToObj(String jsonContent, String nodeName, Class<T> classT) {
		JsonNode node = null;
		try {
			node = mapper.readTree(jsonContent).findValue(nodeName);
			if (node == null) {
				return null;
			} else {
				return mapper.readValue(node.toString(), classT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String doHttp(String urlAddress, String method) {
		URL url;
		HttpURLConnection con = null;
		BufferedReader br = null;
		StringBuffer outInfo = new StringBuffer();
		String input = null;
		try {
			url = new URL(urlAddress);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(method);
			con.setRequestProperty("Accept-Ranges", "qwdwe");
			con.connect();
			System.out.println("****** Content of the URL ********urlAddress=" + urlAddress);

			if (con != null) {

				System.out.println("****** getResponseCode=" + con.getResponseCode());
				for (Map.Entry<String, List<String>> entry : con.getHeaderFields().entrySet()) {
					System.out.print(String.format("****HeaderField:%s=%s", entry.getKey(), entry.getValue().get(0)));
				}
			}
			br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
			while ((input = br.readLine()) != null) {
				outInfo.append(input);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.disconnect();
				}
				if (br != null) {
					br.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
		return outInfo.toString();
	}

}
