package com.solartis.test.apiPackage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import com.jayway.jsonpath.PathNotFoundException;
import com.solartis.test.Configuration.PropertiesHandle;
import com.solartis.test.exception.APIException;
import com.solartis.test.exception.DatabaseException;
import com.solartis.test.exception.HTTPHandleException;
import com.solartis.test.exception.RequestFormatException;
import com.solartis.test.util.api.*;

public class BaseClass implements API
{
	protected RequestResponse sampleInput = null;
	protected RequestResponse request = null;
	protected RequestResponse response = null;
	protected LinkedHashMap<String, String> XmlElements = null;
	protected LinkedHashMap<String, String> jsonElements = null;
	protected PropertiesHandle config = null;
	protected LinkedHashMap<String, String> input = null;
	protected LinkedHashMap<String, String> output = null;
	protected HttpHandle http = null;
	protected DBColoumnVerify InputColVerify = null;
	protected DBColoumnVerify OutputColVerify = null;
	protected DBColoumnVerify StatusColVerify = null;
	protected ArrayList<String> errorParentname = new ArrayList<String>();
	protected ArrayList<String> errorMessage=new ArrayList<String>();
	protected String Location;

//---------------------------------------------------------------LOAD SAMPLE REQUEST--------------------------------------------------------------------	
	public void LoadSampleRequest(LinkedHashMap<String, String> InputData,String Location) throws APIException
	{
		this.input = InputData;
		this.Location = Location;
		sampleInput = new JsonHandle(this.Location+"/sample_request/" +input.get("RequestFileName")+ ".json");
	}

//-----------------------------------------------------------PUMPING TEST DATA TO REQUEST--------------------------------------------------------------- 	
	public void PumpDataToRequest(LinkedHashMap<Integer, LinkedHashMap<String, String>> outputTable,LinkedHashMap<String, String> output) throws APIException 
	{
		try
		{
			request = new JsonHandle(this.Location+"/Request/" +input.get("RequestFileName")+ ".json");
			request.StringToFile(sampleInput.FileToString());
			for (Entry<Integer, LinkedHashMap<String, String>> entry : outputTable.entrySet())	
			{
				LinkedHashMap<String, String> Outputrow = entry.getValue();
				if(Outputrow.get("Product").equalsIgnoreCase(input.get("Product")) && (Outputrow.get("API").equalsIgnoreCase(input.get("Api")))&&(Outputrow.get("NatureOfData").equalsIgnoreCase("input")))
				{
					if(Outputrow.get("Product").equalsIgnoreCase("DailyDate"))
					{
						request.write(Outputrow.get("JsonPath"), dateManipulation(Outputrow.get("Format")));
					}
					else
					{
						request.write(Outputrow.get("JsonPath"), output.get(Outputrow.get("InputFeedFrom")));
					}
				}	
			}
		}
			
		catch(RequestFormatException | ParseException  e)
		{
			throw new APIException("ERROR OCCURS IN PUMPDATATOREQUEST FUNCTION -- BASE CLASS", e);
		}
	}

//------------------------------------------------------------CONVERTING REQUEST TO STRING--------------------------------------------------------------	
	public String RequestToString() throws APIException
	{
	  try 
	  {
		  return request.FileToString();
	  } 
	  catch (RequestFormatException e)
	  {
		  throw new APIException("ERROR OCCURS IN REQUEST TO STRING FUNCTION -- BASE CLASS", e);
	   }
	}
	
//-------------------------------------------------------------ADDING HEADER || TOKENS------------------------------------------------------------------	
	public void AddHeaders(String URL,String ContentType,String Token,String EventVersion,String EventName) throws APIException
	{
		try
		{
			http = new HttpHandle(URL,"POST");
			http.AddHeader("Content-Type", ContentType);
			http.AddHeader("Token", Token);
			http.AddHeader("EventVersion", EventVersion);
			http.AddHeader("EventName", EventName);
		}
		catch(HTTPHandleException e)
		{
			throw new APIException("ERROR ADD HEADER FUNCTION -- BASE CLASS", e);
		}
	}

//------------------------------------------------------------STORING RESPONSE TO FOLDER----------------------------------------------------------------	
	public void SendAndReceiveData() throws APIException 
	{
		try
		{
			String input_data= null;
			input_data = request.FileToString();
		    http.SendData(input_data);
			String response_string = http.ReceiveData();	
			response = new JsonHandle(this.Location+"/Response/" +input.get("RequestFileName")+ ".json");
			response.StringToFile(response_string);
		}
		catch(RequestFormatException | HTTPHandleException e)
		{
			throw new APIException("ERROR IN SEND AND RECIEVE DATA FUNCTION -- BASE CLASS", e);
		}
	}
	
//-------------------------------------------------------------CONVERTING RESPONSE TO STRING------------------------------------------------------------
	public String ResponseToString() throws APIException 
	{
		try
		{
			return response.FileToString();
		}
		catch(RequestFormatException e)
		{
			throw new APIException("ERROR IN RESPONSE TO STRING FUNCTION -- BASE CLASS", e);
		}
	}
	
//-----------------------------------------------------------UPDATING RESPONSE DATA TO DATABASE---------------------------------------------------------	
	public LinkedHashMap<String, String> SendResponseDataToFile(LinkedHashMap<Integer, LinkedHashMap<String, String>> outputTable,LinkedHashMap<String, String> output) throws APIException
	{
		try
		{
			for (Entry<Integer, LinkedHashMap<String, String>> entry : outputTable.entrySet())	
			{
				LinkedHashMap<String, String> Outputrow = entry.getValue();
				if(Outputrow.get("Product").equalsIgnoreCase(input.get("Product")) && (Outputrow.get("API").equalsIgnoreCase(input.get("Api")))&&(Outputrow.get("NatureOfData").equalsIgnoreCase("output")))
				{
					try
					{
						System.out.println("Writing Response to Map");
						output.put(Outputrow.get("Product")+"_"+Outputrow.get("API")+"_"+Outputrow.get("Output"), response.read(Outputrow.get("JsonPath")));
					}
					catch(PathNotFoundException e)
					{
						output.put(Outputrow.get("Product")+"_"+Outputrow.get("API")+"_"+Outputrow.get("Output"), "Response Json Doesnot have "+Outputrow.get("Output")+" Attribute");
					}
				}
			}
			
			return output;
		}
		catch(RequestFormatException e)
		{
			throw new APIException("ERROR IN SEND RESPONSE TO FILE FUNCTION -- BASE CLASS", e);
		}
	}

//---------------------------------------------------------------COMAPRISION FUNCTION-------------------------------------------------------------------	
	public LinkedHashMap<String, String> CompareFunction(LinkedHashMap<String, String> outputrow) throws APIException
	{		
	    try
	    {
	    	LinkedHashMap<Integer, LinkedHashMap<String, String>> tableStatusColVerify = StatusColVerify.GetDataObjects(config.getProperty("OutputColQuery"));
	    	for (Entry<Integer, LinkedHashMap<String, String>> entry : tableStatusColVerify.entrySet()) 	
			{	
			    LinkedHashMap<String, String> rowStatusColVerify = entry.getValue();	
			    if(StatusColVerify.DbCol(rowStatusColVerify) && (rowStatusColVerify.get("Comaparision_Flag").equalsIgnoreCase("Y")))
				{
					String ExpectedColumn = rowStatusColVerify.get(config.getProperty("ExpectedColumn"));
					String ActualColumn = rowStatusColVerify.get(config.getProperty("OutputColumn"));
					String StatusColumn = rowStatusColVerify.get(config.getProperty("StatusColumn"));
					if(!(StatusColumn.equals("")) && !(ExpectedColumn.equals("")))
					{
						if(premium_comp(outputrow.get(ExpectedColumn),outputrow.get(ActualColumn)))
						{
							outputrow.put(StatusColumn, "Pass");
						}
						else
						{
							outputrow.put(StatusColumn, "Fail");
							//outputrow.UpdateRow();
							analyse(rowStatusColVerify,outputrow);
						}
					}
				}
			}
			 			
			String message = "";
			for(int i=0;i<errorMessage.size();i++)
			{
				message=message+errorMessage.get(i)+"; ";
			}
			outputrow.put("AnalyserResult", message);
			errorMessage.clear();
			errorParentname.clear();
			return outputrow;

	    }	
	    catch(DatabaseException e)
	    {
	    	System.out.println(e);
	    	throw new APIException("ERROR IN DB COMPARISON FUNCTION -- BASE CLASS", e);
	    }
	}
	
//-----------------------------------------------------PRIVATE FUNCTION FOR SUPPORTING COMPARISON FUNCTION---------------------------------------------------	
	protected static boolean premium_comp(String expected,String actual)
	{
		
		boolean status = false;
		if(actual == null||actual.equals(""))
		{
			if((expected == null || expected.equals("")||expected.equals("0") || expected.equals("0.0")))
			{
				status = true;
			}
		}
		if(expected == null||expected.equals(""))
		{
			if(actual == null|| actual.equals("")||actual.equals("0") || actual.equals("0.0"))
			{
				status = true;
			}
		}
		if(actual!=null && expected!=null)
		{
			expected = expected.replaceAll("\\[\"", "");
    		actual = actual.replaceAll("\\[\"", "");	
    		expected = expected.replaceAll("\"\\]", "");
    		actual = actual.replaceAll("\"\\]", "");
    		expected = expected.replaceAll("\\.[0-9]*", "");
    		actual = actual.replaceAll("\\.[0-9]*", "");
    		if(expected.equals(actual))
    		{
    			status = true;
    		}
		}

		return status;	
	}

	protected void analyse(LinkedHashMap<String, String> Conditiontablerow,LinkedHashMap<String, String> outputrow ) throws DatabaseException 
	{		
		boolean flag = false;
		
		if(outputrow.get(Conditiontablerow.get("StatusColumn")).equals("Pass"))
		{		

		}

		else if(outputrow.get(Conditiontablerow.get("StatusColumn")).equals("Fail"))
		{	
			String[] Parentname =Conditiontablerow.get("ParentName").split(";");
			int noOfParentname=Parentname.length;
			for(int i=0;i<noOfParentname;i++)
			{								
				if(!this.ifexist(Conditiontablerow.get("NodeName")))
				{
					errorParentname.add(Parentname[i]);
					if(flag == false)
					{
						errorMessage.add(Conditiontablerow.get("Message"));
						flag = true;
					}
				}
			}
						
		}

	}

	protected boolean ifexist (String NodeName)
	{
		boolean exist = false;
		int arraylength =errorParentname.size();
		for(int i = 0; i<arraylength;i++)
		{
			String existParentName =errorParentname.get(i);
			if(existParentName.equals(NodeName))
			{
				exist = true;
				break;
			}
		}
		return exist;	

	}
	
	protected String dateManipulation(String ExpectedFormat) throws ParseException
	{
		 Calendar cal = Calendar.getInstance();
	     cal.setTime(new Date());
	     cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
	     cal.getTime();
	     SimpleDateFormat sdfmt1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	     SimpleDateFormat sdfmt2= new SimpleDateFormat(ExpectedFormat);
	     Date dDate = sdfmt1.parse( cal.getTime().toString() );
	     String strOutput = sdfmt2.format( dDate );		
		return strOutput;
	}
	
}
