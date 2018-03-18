package com.solartis.test.exception;

public class RequestFormatException extends Exception 
{
    private static final long serialVersionUID = 1L;
    
    public RequestFormatException(String message)
	{
    	super (message);
	}
    
    public RequestFormatException(Exception e) 
    {
        super(e);
    }

    public RequestFormatException(String message, Exception e) 
    {
        super(message, e);
    }
}
