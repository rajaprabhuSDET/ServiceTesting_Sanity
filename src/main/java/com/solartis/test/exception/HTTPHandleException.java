package com.solartis.test.exception;

public class HTTPHandleException extends Exception 
{
	private static final long serialVersionUID = 1L;
    
    public HTTPHandleException(String message)
	{
    	super (message);
	}
    
    public HTTPHandleException(Exception e) 
    {
        super(e);
    }

    public HTTPHandleException(String message, Exception e) 
    {
        super(message, e);
    }
}
