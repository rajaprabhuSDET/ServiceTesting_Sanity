package com.solartis.test.util.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.solartis.test.exception.HTTPHandleException;

public class HttpHandle 
{
	private URL url;
	private HttpURLConnection conn;
	
	public HttpHandle(String url,String type) throws HTTPHandleException
	{
		
			try 
			{
				this.url = new URL(url);
			} 
			catch (MalformedURLException e) 
			{
				throw new HTTPHandleException("ERROR IN URL TO HIT HTTP -- REQUESTOR", e);
			}
			
			try 
			{
				conn = (HttpURLConnection) this.url.openConnection();
			} 
			catch (IOException e) 
			{
				throw new HTTPHandleException("ERROR IN URL TO OPEN CONNECTION IN HTTP REQUESTOR", e);
			}
			
			conn.setDoOutput(true);
			
			try 
			{
				conn.setRequestMethod(type);
			} 
			catch (ProtocolException e) 
			{
				throw new HTTPHandleException("ERROR WHILE SENDING REQUEST TO HIT -- HTTP REQUESTOR", e);
			}
		
	}
	
	public void AddHeader(String attr,String value)
	{
		conn.setRequestProperty(attr, value);
		
	}
	
	public void SendData(String data) throws HTTPHandleException
	{
		OutputStream os;
		
			try 
			{
				os = conn.getOutputStream();
				os.write(data.getBytes());
				os.flush();
			} 
			catch (IOException e) 
			{
				throw new HTTPHandleException("ERROR WHILE SENDING JSON REQUEST", e);
			}
		
		
	}
	
	public String ReceiveData() throws HTTPHandleException 
	{
		String output,output2 = null;
		BufferedReader br;
		
			try 
			{
				br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			} 
			catch (IOException e)
			{
				throw new HTTPHandleException("ERROR WHILE RECEIVING JSON RESPONSE", e);
			}
					
			try 
			{
				while ((output = br.readLine()) != null) 
				{
					if(output2 == null)
					{
						output2 = output;
					}
					else
					{
						output2 += output;
					}
				}
			} 
			catch (IOException e) 
			{
				throw new HTTPHandleException("ERROR WHILE RECEIVING JSON RESPONSE", e);
			}
		
		
		if (output2 == null)
		{
			throw new HTTPHandleException("RETURNS EMPTY RESPONSE");
		}
		
		return output2;
	}
	
}
