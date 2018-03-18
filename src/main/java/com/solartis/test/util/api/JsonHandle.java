package com.solartis.test.util.api;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonReader;
import com.solartis.test.exception.RequestFormatException;

public class JsonHandle implements RequestResponse
{
	private JsonPath path;
	private JSONObject obj = new JSONObject();
	private JSONParser parser = new JSONParser();
	private JsonReader  doc = new JsonReader();
	private String file_location;
	private FileReader read_file = null;
	private FileWriter write_file = null;
	public JsonHandle(String file_location)
	{
		this.file_location = file_location;
		
	}
	
	
	public JsonHandle() 
	{
		// TODO Auto-generated constructor stub
	}

	public void getFilePath(String filepath) 
	{
		this.file_location = filepath;
	}

	private String enable_read() throws RequestFormatException
	{
		try 
		{
			read_file = new FileReader(file_location);
		} 
		catch (FileNotFoundException e) 
		{
			throw new RequestFormatException("ERROR OCCURS WHILE SPECIFIED JSON FILEPATH = " + file_location +" TO READ", e);
		}
		
		try 
		{
			obj = (JSONObject) parser.parse(read_file);
		} 
		catch (IOException e)
		{
			throw new RequestFormatException("ERROR OCCURS WHILE READING A JSON FILE -- I/O OPERATION FAILED", e);
		} 
		catch (ParseException e) 
		{
			throw new RequestFormatException("ERROR OCCURS WHILE READING A JSON FILE --PARSING OPERATION", e);
		}
		
		read_file = null;
		return obj.toJSONString();
		
	}
	
	private void enable_write(String json_string) throws RequestFormatException 
	{
		try 
		{
			write_file = new FileWriter(file_location);
		} 
		catch (IOException e) 
		{
			throw new RequestFormatException("ERROR OCCURS WHILE SPECIFYING JSON FILEPATH = " + file_location +" TO WRITE", e);
		}
		
		try 
		{
			write_file.write(json_string);
		} 
		catch (IOException e) 
		{
			throw new RequestFormatException("ERROR OCCURS WHILE WRITING A JSON FILE", e);
		}
		
		try 
		{
			write_file.flush();
		} 
		catch (IOException e) 
		{
			throw new RequestFormatException("ERROR OCCURS WHILE FLUSH A JSON FILE AFTER WRITING A FILE", e);
		}
		
		try 
		{
			write_file.close();
		}
		catch (IOException e) 
		{
			throw new RequestFormatException("ERROR OCCURS WHILE CLOSING A JSON FILE AFTER WRITING A FILE", e);
		}
	}
	
	
	public void StringToFile(String json_string) throws RequestFormatException
	{
		try 
		{
			enable_write(json_string);
		} 
		catch (RequestFormatException e) 
		{
			throw new RequestFormatException("ERROR OCCURS WHILE CONVERTING STRING TO FILE OPERATION", e);
		}
	}
	
	public String read(String json_path) throws RequestFormatException
	{
		try 
		{
			doc.parse(enable_read());
		} 
		catch (RequestFormatException e) 
		{
			throw new RequestFormatException("ERROR OCCURS WHILE READING STRING OPERATION", e);
		}
		
		path = JsonPath.compile(json_path);
		return doc.read(path).toString();	
	}
	
	public String FileToString() throws RequestFormatException
	{
		try
		{
			doc.parse(enable_read());
		} 
		catch (RequestFormatException e) 
		{
			throw new RequestFormatException("ERROR OCCURS WHILE CONVERTING FILE TO STRING OPERATION", e);
		}
		
		return doc.jsonString();
	}
	
	public void write(String json_path,String new_value) throws RequestFormatException
	{
		try 
		{
			doc.parse(enable_read());
		} 
		catch (RequestFormatException e) 
		{
			throw new RequestFormatException("ERROR OCCURS WHILE ENABLE WRITING OPERATION", e);
		}
		path = JsonPath.compile(json_path);
		doc.set(path, new_value);
		enable_write(doc.jsonString());
		
	}


	

	
	
}
