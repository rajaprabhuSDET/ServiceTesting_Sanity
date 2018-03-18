package com.solartis.test.exception;

public class MacroException extends Exception 
{
	private static final long serialVersionUID = 1L;
    
    public MacroException(String message)
	{
    	super (message);
	}
    
    public MacroException(Exception e) 
    {
        super(e);
    }

    public MacroException(String message, Exception e) 
    {
        super(message, e);
    }

}