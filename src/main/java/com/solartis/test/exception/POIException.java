package com.solartis.test.exception;

public class POIException extends Exception 
{
    private static final long serialVersionUID = 1L;
    
    public POIException(String message)
	{
    	super (message);
	}
    
    public POIException(Exception e) 
    {
        super(e);
    }

    public POIException(String message, Exception e) 
    {
        super(message, e);
    }
}
