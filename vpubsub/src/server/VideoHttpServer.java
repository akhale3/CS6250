package server;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;


public class VideoHttpServer extends HttpServlet {
	/**
	 * Required by the Serializable interface
	 */
	private static final long serialVersionUID = -1838508097138721463L;
	@SuppressWarnings("rawtypes")
//	ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();
	ConcurrentLinkedQueue[] queue;
	int i, j, rowlen, gx, gy;
	int reqIndex, reqLimit;
//	HttpServletRequest reqList[];
	int reqList[];

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		Server server = new Server(8080);
		Context root = new Context(server, "/", Context.SESSIONS);
		root.addServlet(new ServletHolder(new VideoHttpServer()), "/*");
		server.start();
	}

	@SuppressWarnings("unused")
	private BigInteger lastVideoID = null;

	/**
	 * @throws IOException
	 */
	public VideoHttpServer() throws IOException {
		super();
		rowlen = 5;		//Maximum number of columns in a row of thumbnail grid
		gx = 0;			//X coordinate for grid
		gy = 0;			//Y coordinate for grid
//		reqList = new HttpServletRequest[5];	//A HttpServletRequest array to maintain list of unique HTTP streams
		reqLimit = 5;	//Maximum number of streams handled by the server
		reqList = new int[reqLimit];	//An integer array to maintain list of unique HTTP streams based on clientID
		reqIndex = 0;	//Index for reqList[] 
//		for(i = 0; i < reqLimit; i++)
//			reqList[i] = null;
		i = 0;
		queue = new ConcurrentLinkedQueue[reqLimit];
	}
	
	/***
	 * 
	 * @param req
	 */
	
	@SuppressWarnings("rawtypes")
	protected void getReqIndex(int clientID, String mode)//HttpServletRequest req)
	{
		System.out.println(clientID);
		boolean flag = false;
		if(i < reqLimit)	//Check whether number of concurrent streams exceed max stream limit (currently 5)
		{
			for(j = 0; j < reqLimit; j++)
				if(clientID == reqList[j])	//Check whether incoming stream exists in stream list
				{
					reqIndex = j;
					flag = true;
				}
			if(flag == false && mode.equals("post"))	//New stream
			{
				for(j = 0; j < reqLimit; j++)
				{
					if(reqList[j] == 0)	//Assign first available index to new incoming stream
					{
						reqIndex = j;
						reqList[j] = clientID;
						queue[reqIndex] = new ConcurrentLinkedQueue();
						i++;
						break;
					}
				}
			}
			else
				flag = false;
		}
		else
			System.out.println("Stream limit reached. Please try again later.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
//		reqIndex = getReqIndex(req);
//		queue[reqIndex] = new ConcurrentLinkedQueue();
		byte[] image = null;
		ServletOutputStream os = resp.getOutputStream();
		int streamID = Integer.parseInt(req.getParameter("streamID"));
		String mode = "get";
		getReqIndex(streamID, mode);	//Invoke method on clientID
		
		if(queue[reqIndex].size() > 0)
		{
			resp.setContentType("binary/octet-stream");
			image = (byte[]) queue[reqIndex].remove();
			System.out.println("image returned: size(" + image.length + "), queue size(" + queue[reqIndex].size() + ")");				
			resp.setContentLength(image.length);
			os.write(image);
			os.close();
		}
		else
		{
			System.out.println("returning no video");
		}
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			ServletInputStream is = req.getInputStream();

			byte[] image = IOUtils.toByteArray(is);
//			long timestamp = Long.parseLong(req.getParameter("ts"));
			double x = Double.parseDouble(req.getParameter("x"));
			double y = Double.parseDouble(req.getParameter("y"));
			//String location = req.getParameter("x");
			int clientID = Integer.parseInt(req.getParameter("clientID"));
			String keywords = req.getParameter("keywords");
			String mode = "post";
			getReqIndex(clientID, mode);	//Invoke method on clientID
			
			ArrayList<String> keywordList = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer(keywords, ";");
			while (st.hasMoreTokens())
				keywordList.add(st.nextToken());
			
			if(queue[reqIndex].size() < 1000)
			{
				queue[reqIndex].add(image);
				System.out.println("image posted : size(" + image.length + "), queue size(" + queue[reqIndex].size() + "), grid coordinates( " + gx + " , " + gy + " ), ( x , y ) = ( " + x + " , " + y + " ), Stream number = " + reqIndex);
				gy = (gy+1) % rowlen;
			}
			is.close();
	}
}