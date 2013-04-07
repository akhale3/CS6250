package client;

import java.io.IOException;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class VideoPublisher {
	int id;
	String serverURL;

	
	VideoPublisher(String serverURL, int id)
	{
		this.serverURL = serverURL;
		this.id = id;
	}
	
	public static void main(String args[]) throws IOException, InterruptedException {

		VideoPublisher vp = new VideoPublisher("localhost", 0);
		
		Random generator = new Random();
		
		for(int i=0;i<1000;i++)
		{
			int size = generator.nextInt(1024) + 1024;
			byte[] image = new byte[size];
			System.out.println("posting image");
			vp.postImage("first;second;third", image);
			System.out.println("image posted w/ size: " + size);
			Thread.sleep(2000);
		}

	}
	
	public void postImage(String keywords, byte[] video) throws ClientProtocolException, IOException {

		//the following parameters are automatically filled out
		double x = 1;
		double y = 1;
//		long ts = System.currentTimeMillis();
		int clientID = 100;


		String url = "http://" + serverURL + ":8080?clientID=" + clientID + "&x=" + x + "&y=" + y + "&keywords=" + keywords;

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		ByteArrayEntity byteEntity = new ByteArrayEntity(video);
		byteEntity.setContentType("binary/octet-stream");
		byteEntity.setChunked(true);
		httppost.setEntity(byteEntity);

		System.out.println("posting start");
		HttpResponse resp = httpclient.execute(httppost);
		System.out.println("posting end");
		
		int code = resp.getStatusLine().getStatusCode();
		System.out.println("return code: " + code);
		
		httppost.releaseConnection();
	}
}