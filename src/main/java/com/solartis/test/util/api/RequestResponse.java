package com.solartis.test.util.api;

import com.solartis.test.exception.RequestFormatException;

public interface RequestResponse 
{
	
	public String FileToString() throws RequestFormatException;
	public void getFilePath(String filepath);
	public String read(String Path) throws RequestFormatException;
	public void StringToFile(String String) throws RequestFormatException;
	public void write(String Path,String Value) throws RequestFormatException;
}
