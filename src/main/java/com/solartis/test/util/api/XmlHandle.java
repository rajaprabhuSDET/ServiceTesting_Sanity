package com.solartis.test.util.api;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.solartis.test.exception.RequestFormatException;


public class XmlHandle implements RequestResponse{
	private String file_location;
	
	
	//***************constructor to load file location***************************************************************
	public XmlHandle(String file_location)
	{
		this.file_location = file_location;
		
	}
	
	//*****************method for reading the XML input file by passing the xpath value******************************
	 
	public void getFilePath(String filepath) 
	{
		// TODO Auto-generated method stub
		this.file_location = filepath;
	}
	
	public XmlHandle() 
	{
		// TODO Auto-generated constructor stub
	}
	
	

	public String read(String xpath) throws RequestFormatException
	{
		File inputFile = new File(file_location);
        SAXReader reader = new SAXReader();
        Document document;
		
        try 
		{
			document = reader.read(inputFile);
		} 
		catch (DocumentException e) 
		{
			throw new RequestFormatException("ERROR OCCURS WHILE READING XML FILE", e);
		}
		
        Node element = document.selectSingleNode(xpath);	                                                                                                                                                  
		return element.getText();	
	}
	

	//*********************method to convert a string into XML************************************************************
	
	@SuppressWarnings("resource")
	public void StringToFile(String xml_data) throws RequestFormatException
	{
		Writer writer;
		
		try 
		{
			writer = new FileWriter(file_location);
		} 
		catch (IOException e) 
		{
			throw new RequestFormatException("ERROR OCCURS WHILE SPECIFIED XML FILEPATH = " + file_location, e);
		}
		
        Document document;
		
        try 
        {
			document = DocumentHelper.parseText(xml_data);
		} 
        catch (DocumentException e) 
        {
        	throw new RequestFormatException("ERROR OCCURS WHILE PARSING XML FILE", e);
		}
        
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter xmlwriter;
        xmlwriter = new XMLWriter( writer, format );
        
        try 
        {
			xmlwriter.write( document );
		} 
        catch (IOException e) 
        {
        	throw new RequestFormatException("ERROR OCCURS WHILE WRITING XML FILE", e);
		}
        
        try 
        {
			xmlwriter.close();
		} 
        catch (IOException e) 
        {
        	throw new RequestFormatException("ERROR OCCURS WHILE CLOSING XMLWRITER", e);
		}
	}
	
	
	 //******************************method to modify values in existing XML by Passing XPath************************************************
	
	public void write(String xpath,String value) throws RequestFormatException
	{
		File inputFile = new File(file_location);
		SAXReader reader = new SAXReader();
		
        Document document;
		try 
		{
			document = reader.read(inputFile);
		} 
		catch (DocumentException e) 
		{
			throw new RequestFormatException("ERROR OCCURS WHILE WRITING XML FILE", e);
		}
        
        Node node = document.selectSingleNode(xpath);
        Element element = (Element)node;
        element.setText(value);
        OutputFormat format = OutputFormat.createPrettyPrint();
        
        Writer writer;
		try 
		{
			writer = new FileWriter(file_location);
		} 
		catch (IOException e) 
		{
			throw new RequestFormatException("ERROR OCCURS WHILE WRITING XML FILE IN FILE LOCATION-- I/O OPERATION FAILED", e);
		}
        XMLWriter xmlwriter;
        
        xmlwriter = new XMLWriter( writer, format );
        
        try 
        {
			xmlwriter.write( document );
		} 
        catch (IOException e) 
        {
        	throw new RequestFormatException("ERROR OCCURS WHILE WRITING XML FILE IN DOCUMENT-- I/O OPERATION FAILED", e);
		}
        
        try 
        {
			writer.close();
		} 
        catch (IOException e)
        {
        	throw new RequestFormatException("ERROR OCCURS WHILE CLOSING XML FILE WRITER", e);
		}
		
		
	}
		
   //*********************************method to Convert xml to string		
		
	public String FileToString() throws RequestFormatException
	{
		File inputFile = new File(file_location);
		SAXReader reader = new SAXReader();
        Document document;
		
        try
		{
			document = reader.read(inputFile);
		} 
		catch (DocumentException e) 
		{
			throw new RequestFormatException("ERROR OCCURS WHILE CONVERTING FILE TO STRING", e);
		}
        
		return document.asXML();
		
	}
	
	
	/*private String getxpath(String xpath) throws UnsupportedEncodingException, IOException,DocumentException
	{
		if(!(xpath.contains("=")))
		{
			return xpath;
		}
		else
		{
			String path[] = xpath.split("=");
			File inputFile = new File(file_location);
	        SAXReader reader = new SAXReader();
	        Document document = reader.read(inputFile);
	        String output = null;
	        output = path[0];
	        Node node = document.selectSingleNode(output);
	        
			
		}
		return null;
	}
	*/
	
	
	
	 
	 
	 




}



