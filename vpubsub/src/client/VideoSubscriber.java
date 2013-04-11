package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import datatype.VideoInfo;

public class VideoSubscriber {
	
	String serverURL;
	int clientID;
	
	VideoSubscriber(String serverURL, int clientID)
	{
		this.serverURL = serverURL;
		this.clientID = clientID;
	}
	
	public static void main(String args[]) throws IOException, InterruptedException
	{
		VideoSubscriber vs = new VideoSubscriber("localhost", 200);
		int streamID;
		int option;
		String choice = new String();
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		
		do
		{
			System.out.println("Menu");
			System.out.println("1. Request thumbnails");
			System.out.println("2. Request stream ");
			System.out.print("Enter option: ");
			option = Integer.parseInt(input.readLine());
			
			switch(option)
			{
			case 1: 
				break;
				
			case 2: for(int i=0;i<1000;i++)
				{
					streamID = 100;	//Specifies stream number to pull
					byte[] image = vs.GetImage(streamID);
					
					if(image != null)
						System.out.println("retreived image: " + image.length);
					else
					{
						System.out.println("no image retrieved");
						break;
					}
		
					Thread.sleep(2000);
				}
			break;
			
			default: System.out.println("Invalid option");
			}
			
			System.out.print("End of stream. Do you wish to continue (Y/n)? ");
			choice = input.readLine();
		}
		while(choice.equalsIgnoreCase("y") == true);
		
			
	}
	
	@SuppressWarnings("unused")
	byte[] GetImage(int streamID) throws ClientProtocolException, IOException, NullPointerException
	{
		byte[] video = null;
		//contact the server and send the query
		//		String url = "http://" + serverURL + ":8080?mode=query&xStart="	+ vq.getxStart() + "&xEnd=" + vq.getxEnd() +
		//				"&yStart=" + vq.getyStart() + "&yEnd=" + vq.getyEnd() + "&keywords=" + keywordString;
		String url = "http://" + serverURL + ":8080?mode=query&clientID=" + clientID + "&streamID=" + streamID;
		HttpClient httpclient = new DefaultHttpClient();

		HttpGet httpget = new HttpGet(url);
		
		ResponseHandler<byte[]> handler = new ResponseHandler<byte[]>() {
		    public byte[] handleResponse(
		            HttpResponse response) throws ClientProtocolException, IOException {
		        HttpEntity entity = response.getEntity();
		        if (entity != null) {
		            return EntityUtils.toByteArray(entity);
		        } else {
		            return null;
		        }
		    }
		};


		//TBD: post a query with Filter object 

		System.out.println("start");
		//HttpResponse response = httpclient.execute(httpget);
		byte[] image = httpclient.execute(httpget, handler);
		System.out.println("end");
		httpget.releaseConnection();
		httpclient.getConnectionManager().shutdown();

		
		return image;
	}

	//this interface is called when a new query is arrived
	void onVideoInfoReceive(VideoInfo vi)
	{
		
	}
}
