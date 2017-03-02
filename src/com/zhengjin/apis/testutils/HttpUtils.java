package com.zhengjin.apis.testutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Assert;

public final class HttpUtils {

	private static final int BUFFER_SIZE = 1000;

	public static String sendHttpGetRequest(String httpUrl, String parms) {
		HttpURLConnection httpConn = null;
		BufferedReader in = null;

		String path = String.format("%s?%s", httpUrl, parms);
		try {
			URL url = new URL(path);
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.setRequestProperty("Content-Type","application/json");
			httpConn.setRequestProperty("charset", TestConstants.CHARSET_UFT8);
			httpConn.connect();

			// read response
			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				StringBuilder content = new StringBuilder(BUFFER_SIZE);
				String tempStr = null;

				in = new BufferedReader(new InputStreamReader(
						httpConn.getInputStream(), TestConstants.CHARSET_UFT8));
				while ((tempStr = in.readLine()) != null) {
					content.append(tempStr);
				}
				return content.toString();
			} else {
				Assert.assertTrue(String.format("Error, GET request return code: %d", 
						httpConn.getResponseCode()), false);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(String.format("Error, IOException(%s) in http GET request!",
					e.getMessage()), false);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			httpConn.disconnect();
		}

		return "";
	}

	public static String sendHttpPostRequest(String httpUrl, String parms) {
		HttpURLConnection httpConn = null;
		BufferedReader in = null;
		PrintWriter out = null;

		try {
			URL url = new URL(httpUrl);
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Content-Type", "application/json");
			httpConn.setRequestProperty("charset", TestConstants.CHARSET_UFT8);
			httpConn.setConnectTimeout(TestConstants.TIME_OUT);
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);

			// send post request
			out = new PrintWriter(httpConn.getOutputStream());
			out.write(parms);
			out.flush();

			// read response
			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				StringBuilder content = new StringBuilder(BUFFER_SIZE);
				String tempStr = "";

				in = new BufferedReader(new InputStreamReader(
						httpConn.getInputStream(), TestConstants.CHARSET_UFT8));
				while ((tempStr = in.readLine()) != null) {
					content.append(tempStr);
				}
				return content.toString();
			} else {
				Assert.assertTrue(String.format("Error, POST request return code %s", 
						httpConn.getResponseCode()), false);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(String.format("Error, IOException(%s) in http POST request!",
					e.getMessage()), false);
		} finally {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			httpConn.disconnect();
		}

		return "";
	}
	
}
