package com.solartis.test.apiPackage;

import java.util.LinkedHashMap;
import com.solartis.test.exception.APIException;

public interface API 
{
	public void LoadSampleRequest(LinkedHashMap<String, String> InputData,String Location) throws APIException;
	public void PumpDataToRequest(LinkedHashMap<Integer, LinkedHashMap<String, String>> outputTable,LinkedHashMap<String, String> output) throws APIException;
	public void AddHeaders(String URL,String ContentType,String Token,String EventVersion,String EventName) throws APIException;
	public void SendAndReceiveData() throws APIException;
	public LinkedHashMap<String, String> SendResponseDataToFile(LinkedHashMap<Integer, LinkedHashMap<String, String>> outputTable,LinkedHashMap<String, String> output) throws APIException;
	public LinkedHashMap<String, String> CompareFunction(LinkedHashMap<String, String> output) throws APIException;
	public String RequestToString() throws APIException;
	public String ResponseToString() throws APIException;	
}
