package client;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class VideoHttpClient {

	/**
	 * @param serverURL
	 * @param videoID
	 * @return
	 */
	public static byte[] getVideo(String serverURL, String videoID) {
		String url = "http://" + serverURL + ":80?mode=retrieve&videoID="
				+ videoID;

		byte[] data = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();

			HttpGet httpget = new HttpGet(url);

			HttpResponse response = httpclient.execute(httpget);
			InputStream is = response.getEntity().getContent();

			data = IOUtils.toByteArray(is);
		} catch (Exception e) {
			// show error
		}

		return data;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static void main(String args[]) throws IOException {
		String serverURL = "localhost";

		String location = "klaus";
		String keywords = "first;second;third";
		long timestamp = System.currentTimeMillis();
	}

	/**
	 * @param serverURL
	 * @param timestamp
	 * @param location
	 * @param keywords
	 * @param video
	 */
	public static void putVideo(String serverURL, long timestamp,
			String location, String keywords, byte[] video) {
		String url = "http://" + serverURL + ":80?timestamp=" + timestamp
				+ "&location=" + location + "&keywords=" + keywords;

		try {
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(url);

			ByteArrayEntity byteEntity = new ByteArrayEntity(video);
			byteEntity.setContentType("binary/octet-stream");
			byteEntity.setChunked(true); // Send in multiple parts if needed
			httppost.setEntity(byteEntity);

			httpclient.execute(httppost);
		} catch (Exception e) {
			// show error
		}
	}

	/**
	 * @param serverURL
	 * @param tsBegin
	 * @param tsEnd
	 * @param location
	 * @param keywords
	 * @return
	 */
	public static byte[] queryVideo(String serverURL, long tsBegin, long tsEnd,
			String location, String keywords) {
		String url = "http://" + serverURL + ":80?mode=query&tsBegin="
				+ tsBegin + "&tsEnd=" + tsEnd + "&location=" + location
				+ "&keywords=" + keywords;

		byte[] data = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			InputStream is = response.getEntity().getContent();

			data = IOUtils.toByteArray(is);
		} catch (Exception e) {
			// show error
		}

		return data;
	}
}