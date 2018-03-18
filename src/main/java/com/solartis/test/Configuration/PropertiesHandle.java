package com.solartis.test.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Map.Entry;

import com.solartis.test.exception.DatabaseException;
import com.solartis.test.exception.PropertiesHandleException;
import com.solartis.test.util.common.DatabaseOperation;

public class PropertiesHandle extends Properties

{
	private static final long serialVersionUID = 1L;
	protected String path = null;
	protected String Project;
	protected String Api;
	protected String Env;
	protected String OutputChioce;
	protected String UserName;
	protected String JDBC_DRIVER;
	protected String DB_URL;
	protected String USER;
	protected String password;
	protected String priority;
	protected String queryresult;

	static DatabaseOperation ConfigQuery = new DatabaseOperation();
			
	    public PropertiesHandle(String Project,String Api, String Env ,String OutputChioce, String UserName, String JDBC_DRIVER, String DB_URL, String USER, String password, String priority) throws DatabaseException, PropertiesHandleException
		{
			this.Project = Project;
			this.Api=Api;
			this.Env=Env;
			this.OutputChioce=OutputChioce;
			this.UserName=UserName;
			this.JDBC_DRIVER=JDBC_DRIVER;
			this.DB_URL=DB_URL;
			this.USER=USER;
			this.password=password;
			this.priority=priority;
			
			WriteProperty(UserName);
			
		}
		
		protected void WriteProperty(String UserName) throws DatabaseException, PropertiesHandleException
		{
			DatabaseOperation.ConnectionSetup(JDBC_DRIVER, DB_URL, USER, password);

			this.OutputInSameTable();
						
            if(OutputChioce.equalsIgnoreCase("Output_Saved_in_DB"))
            {
				 this.ActualAndStatus("Y", "N");    
            }
		    else if(OutputChioce.equalsIgnoreCase("Get_Response_Only"))
		    {
		    	this.ActualAndStatus("N", "N");    
		    }
			if(OutputChioce.equalsIgnoreCase("Compared_Results"))
			{
				this.ActualAndStatus("Y", "Y");    
			}
			
			this.SampleRequest();
			this.RequestLocation();
			this.ResponseLocation();
		   
			this.SampleRatingModelLocation();
			this.ExpectedRatingModelPath();
			this.MacroMappingQuery();
			this.MacroTranslationQuery();		
			
			this.URL();
			this.ContentType();
			this.Token();
			this.EventName();
			this.EventVersion();

			this.InputQuery();
			this.OutputQuery();
			
			this.InputConditionQuery();
			this.OutputConiditionQuery();
			
			this.ClassName();
			
			this.InputJsonPath();
			this.OutputJsonPath();
			
		    this.InputColumn();
		    this.OutputColumn();
		    		    
			this.InputConditionColumn();
			this.OutputConditionColumn();
			
		    this.ExpectedColumn();
		    this.StatusColumn();
		    		    
		    this.DBdetails();
		    
		    DatabaseOperation.CloseConn();
		 
		}
		
		protected String RdmsQuery(String OutputColoumn) throws PropertiesHandleException
		{
			try
			{
				LinkedHashMap<Integer, LinkedHashMap<String, String>> tableRdmsQuery =  ConfigQuery.GetDataObjects("SELECT UserFolder_CONFIG.RootFolder,UserFolder_CONFIG.JDCDriver,UserFolder_CONFIG.DB_URL,UserFolder_CONFIG.DB_UserName,UserFolder_CONFIG.DB_Password,UserFolder_CONFIG.UserDBName,Version_CONFIG.Version,Project_CONFIG.ProjectDBName,Project_CONFIG.ServiceType,Environment_CONFIG.URL,Project_CONFIG.Token,VersionDetail_CONFIG.EventName,VersionDetail_CONFIG.EventVersion,API_CONFIG.OutputInInputTable,VersionDetail_CONFIG.ClassName,VersionDetail_CONFIG.InputConditonTable,VersionDetail_CONFIG.InputTable,VersionDetail_CONFIG.OutputConditionTable,VersionDetail_CONFIG.OutputTable,VersionDetail_CONFIG.MacroMappingTable,VersionDetail_CONFIG.MacroTranslationTable FROM Project_CONFIG INNER JOIN UserFolder_CONFIG INNER JOIN API_CONFIG ON Project_CONFIG.ProjectID = API_CONFIG.ProjectID INNER JOIN Environment_CONFIG ON API_CONFIG.APIID = Environment_CONFIG.APIID INNER JOIN Version_CONFIG ON Environment_CONFIG.Env_ID = Version_CONFIG.Env_ID INNER JOIN VersionDetail_CONFIG ON (VersionDetail_CONFIG.Verision = Version_CONFIG.Version and VersionDetail_CONFIG.APIID = API_CONFIG.APIID)  WHERE Project_CONFIG.ProjectName ='" + Project +"' AND API_CONFIG.APIName = '" + Api + "' AND Environment_CONFIG.Env_Name = '" + Env + "' AND UserFolder_CONFIG.User_ID = '" + UserName + "' ORDER BY Version_CONFIG.Version DESC LIMIT 1");
				for (Entry<Integer, LinkedHashMap<String, String>> entry : tableRdmsQuery.entrySet())	
				{
					LinkedHashMap<String, String> rowRdmsQuery = entry.getValue();
					queryresult =  rowRdmsQuery.get(OutputColoumn);
				}
				return "SELECT * FROM " + queryresult;		
			}
			catch(DatabaseException e)
			{
				throw new PropertiesHandleException("ERROR IN SQL - QUERY -- " + OutputColoumn, e);
			}
		}
		
		protected String RdmsQueryWithPriority(String OutputColoumn, String priority) throws PropertiesHandleException
		{
			try 
			{
				LinkedHashMap<Integer, LinkedHashMap<String, String>> tableRdmsQueryWithPriority =  ConfigQuery.GetDataObjects("SELECT UserFolder_CONFIG.RootFolder,UserFolder_CONFIG.JDCDriver,UserFolder_CONFIG.DB_URL,UserFolder_CONFIG.DB_UserName,UserFolder_CONFIG.DB_Password,UserFolder_CONFIG.UserDBName,Version_CONFIG.Version,Project_CONFIG.ProjectDBName,Project_CONFIG.ServiceType,Environment_CONFIG.URL,Project_CONFIG.Token,VersionDetail_CONFIG.EventName,VersionDetail_CONFIG.EventVersion,API_CONFIG.OutputInInputTable,VersionDetail_CONFIG.ClassName,VersionDetail_CONFIG.InputConditonTable,VersionDetail_CONFIG.InputTable,VersionDetail_CONFIG.OutputConditionTable,VersionDetail_CONFIG.OutputTable,VersionDetail_CONFIG.MacroMappingTable,VersionDetail_CONFIG.MacroTranslationTable FROM Project_CONFIG INNER JOIN UserFolder_CONFIG INNER JOIN API_CONFIG ON Project_CONFIG.ProjectID = API_CONFIG.ProjectID INNER JOIN Environment_CONFIG ON API_CONFIG.APIID = Environment_CONFIG.APIID INNER JOIN Version_CONFIG ON Environment_CONFIG.Env_ID = Version_CONFIG.Env_ID INNER JOIN VersionDetail_CONFIG ON (VersionDetail_CONFIG.Verision = Version_CONFIG.Version and VersionDetail_CONFIG.APIID = API_CONFIG.APIID)  WHERE Project_CONFIG.ProjectName ='" + Project +"' AND API_CONFIG.APIName = '" + Api + "' AND Environment_CONFIG.Env_Name = '" + Env + "' AND UserFolder_CONFIG.User_ID = '" + UserName + "' ORDER BY Version_CONFIG.Version DESC LIMIT 1");
				for (Entry<Integer, LinkedHashMap<String, String>> entry : tableRdmsQueryWithPriority.entrySet())	
				{
					LinkedHashMap<String, String> rowRdmsQueryWithPriority = entry.getValue();
					queryresult = rowRdmsQueryWithPriority.get(OutputColoumn);
				}	
				return "SELECT * FROM " + queryresult + " WHERE " + queryresult + ".Priority = '" + priority + "'";	
			} 
			catch (DatabaseException e)
			{
				throw new PropertiesHandleException("ERROR IN SQL - PRIORITY QUERY -- " + OutputColoumn, e);
			}
		}
		
		protected String RdmsQueryWithCustomSortPriority(String OutputColoumn) throws PropertiesHandleException
		{
			try 
			{
				LinkedHashMap<Integer, LinkedHashMap<String, String>> tableRdmsQueryWithCustomSortPriority = ConfigQuery.GetDataObjects("SELECT UserFolder_CONFIG.RootFolder,UserFolder_CONFIG.JDCDriver,UserFolder_CONFIG.DB_URL,UserFolder_CONFIG.DB_UserName,UserFolder_CONFIG.DB_Password,UserFolder_CONFIG.UserDBName,Version_CONFIG.Version,Project_CONFIG.ProjectDBName,Project_CONFIG.ServiceType,Environment_CONFIG.URL,Project_CONFIG.Token,VersionDetail_CONFIG.EventName,VersionDetail_CONFIG.EventVersion,API_CONFIG.OutputInInputTable,VersionDetail_CONFIG.ClassName,VersionDetail_CONFIG.InputConditonTable,VersionDetail_CONFIG.InputTable,VersionDetail_CONFIG.OutputConditionTable,VersionDetail_CONFIG.OutputTable,VersionDetail_CONFIG.MacroMappingTable,VersionDetail_CONFIG.MacroTranslationTable FROM Project_CONFIG INNER JOIN UserFolder_CONFIG INNER JOIN API_CONFIG ON Project_CONFIG.ProjectID = API_CONFIG.ProjectID INNER JOIN Environment_CONFIG ON API_CONFIG.APIID = Environment_CONFIG.APIID INNER JOIN Version_CONFIG ON Environment_CONFIG.Env_ID = Version_CONFIG.Env_ID INNER JOIN VersionDetail_CONFIG ON (VersionDetail_CONFIG.Verision = Version_CONFIG.Version and VersionDetail_CONFIG.APIID = API_CONFIG.APIID)  WHERE Project_CONFIG.ProjectName ='" + Project +"' AND API_CONFIG.APIName = '" + Api + "' AND Environment_CONFIG.Env_Name = '" + Env + "' AND UserFolder_CONFIG.User_ID = '" + UserName + "' ORDER BY Version_CONFIG.Version DESC LIMIT 1");
			    for (Entry<Integer, LinkedHashMap<String, String>> entry : tableRdmsQueryWithCustomSortPriority.entrySet())	
				{
					LinkedHashMap<String, String> rowRdmsQueryWithCustomSortPriority = entry.getValue();
					queryresult = rowRdmsQueryWithCustomSortPriority.get(OutputColoumn);
				}
				return "SELECT * FROM " + queryresult + " WHERE (" + queryresult + ".Priority = 'high' OR " + queryresult + ".Priority = 'low' or " + queryresult + ".Priority = 'medium') ORDER BY FIELD( " + queryresult + ".Priority ,'high','medium','low')";	
			} 
			catch (DatabaseException e)
			{
				throw new PropertiesHandleException("ERROR IN SQL - CUSTOM SORT PRIORITY QUERY -- " + OutputColoumn, e);
			}
		}
		
		protected String RdmsValue(String OutputColoumn) throws PropertiesHandleException
		{
			try
			{
				LinkedHashMap<Integer, LinkedHashMap<String, String>> tableRdmsValue = ConfigQuery.GetDataObjects("SELECT UserFolder_CONFIG.RootFolder,UserFolder_CONFIG.JDCDriver,UserFolder_CONFIG.DB_URL,UserFolder_CONFIG.DB_UserName,UserFolder_CONFIG.DB_Password,UserFolder_CONFIG.UserDBName,Version_CONFIG.Version,Project_CONFIG.ProjectDBName,Project_CONFIG.ServiceType,Environment_CONFIG.URL,Project_CONFIG.Token,VersionDetail_CONFIG.EventName,VersionDetail_CONFIG.EventVersion,API_CONFIG.OutputInInputTable,VersionDetail_CONFIG.ClassName,VersionDetail_CONFIG.InputConditonTable,VersionDetail_CONFIG.InputTable,VersionDetail_CONFIG.OutputConditionTable,VersionDetail_CONFIG.OutputTable,VersionDetail_CONFIG.MacroMappingTable,VersionDetail_CONFIG.MacroTranslationTable FROM Project_CONFIG INNER JOIN UserFolder_CONFIG INNER JOIN API_CONFIG ON Project_CONFIG.ProjectID = API_CONFIG.ProjectID INNER JOIN Environment_CONFIG ON API_CONFIG.APIID = Environment_CONFIG.APIID INNER JOIN Version_CONFIG ON Environment_CONFIG.Env_ID = Version_CONFIG.Env_ID INNER JOIN VersionDetail_CONFIG ON (VersionDetail_CONFIG.Verision = Version_CONFIG.Version and VersionDetail_CONFIG.APIID = API_CONFIG.APIID)  WHERE Project_CONFIG.ProjectName ='" + Project +"' AND API_CONFIG.APIName = '" + Api + "' AND Environment_CONFIG.Env_Name = '" + Env + "' AND UserFolder_CONFIG.User_ID = '" + UserName + "' ORDER BY Version_CONFIG.Version DESC LIMIT 1");
				for (Entry<Integer, LinkedHashMap<String, String>> entry : tableRdmsValue.entrySet())	
				{
					LinkedHashMap<String, String> rowRdmsValue = entry.getValue();
					queryresult = rowRdmsValue.get(OutputColoumn);
				}
				return queryresult;
			}
			catch(DatabaseException e)
			{
				throw new PropertiesHandleException("ERROR IN RETRIVING DATA FROM -- " + OutputColoumn, e);
			}
		}

	
		protected void OutputInSameTable() throws PropertiesHandleException // FUNCTION CHECK OUTPUT IN SAME TABLE ARE NOT
		{
			this.put("output_in_same_table", this.RdmsValue("OutputInInputTable"));
		}	
		
		protected void ActualAndStatus(String Actual, String Status)// FUNCTION FOR ACTUAL AND STATUS OCCURANCE
		{
			this.put("actual", Actual);
			this.put("status", Status);
		}	
		
		protected void SampleRequest() throws PropertiesHandleException// FUNCTION FOR SAMPLEREQUEST PATH
		{
			this.put("sample_request", this.RdmsValue("RootFolder") + "/" + Project + "/" + Api + "/SampleRequest/SampleRequest" + this.RdmsValue("Version") + "/");
		}
		
		protected void RequestLocation() throws PropertiesHandleException// FUNCTION FOR REQUEST TO SAVE PATH
		{
			this.put("request_location", this.RdmsValue("RootFolder") + "/" + Project + "/" + Api + "/Request/");
		}
		
		protected void ResponseLocation() throws PropertiesHandleException// FUNCTION FOR RESPONSE TO SAVE PATH
		{
			this.put("response_location", this.RdmsValue("RootFolder") + "/" + Project + "/" + Api + "/Response/");
		}
		
		protected void SampleRatingModelLocation() throws PropertiesHandleException// FUNCTION FOR SAMPLE RATING MODEL PATH
		{
			this.put("Samplepath", this.RdmsValue("RootFolder") + "/" + Project + "/" + Api + "/SampleRatingModel/SampleRating" + this.RdmsValue("Version") + "/");
		}
		
		protected void ExpectedRatingModelPath() throws PropertiesHandleException// FUNCTION FOR EXPECTED RATING MODEL PATH
		{
			this.put("TargetPath", this.RdmsValue("RootFolder") + "/" + Project + "/" + Api +  "/RatingModelResult/");
		}
		
		protected void MacroMappingQuery() throws PropertiesHandleException// FUNCTION TO GET RESULT SET FROM MACRO MAPPING TABLE 
		{
			this.put("config_query", this.RdmsQuery("MacroMappingTable"));
		}
		
		protected void MacroTranslationQuery() throws PropertiesHandleException// FUNCTION TO GET RESULT SET FROM MACRO TRNSLATION TABLE 
		{
			this.put("lookup_query", this.RdmsQuery("MacroTranslationTable"));
		}

		protected void URL() throws PropertiesHandleException// FUNCTION TO GET URL
		{
			this.put("test_url", this.RdmsValue("URL"));	
		}
		
		protected void ContentType() throws PropertiesHandleException// FUNCTION TO GET CONTENTTYPE
		{
			this.put("content_type", "application/"+this.RdmsValue("ServiceType"));
	    }
	
		protected void Token() throws PropertiesHandleException// FUNCTION TO GET TOKEN
		{
			this.put("token", this.RdmsValue("Token"));
		}
		
		protected void EventName() throws PropertiesHandleException// FUNCTION TO GET EVENT-NAME
		{
		    this.put("EventName", this.RdmsValue("EventName"));
		}
		
		protected void EventVersion() throws PropertiesHandleException// FUNCTION TO GET EVENT-VERSION
		{
		    this.put("EventVersion", this.RdmsValue("EventVersion"));
		}
	
		protected void InputQuery() throws PropertiesHandleException// FUNCTION FOR INPUTQUERY
		{
				if(priority.equalsIgnoreCase("all"))
				{
					this.put("input_query",  this.RdmsQueryWithCustomSortPriority("InputTable") );
				}
				else if(priority.equalsIgnoreCase("high") || priority.equalsIgnoreCase("low"))
				{
					this.put("input_query",  this.RdmsQueryWithPriority("InputTable", priority));
				}		
		}
		
		protected void OutputQuery() throws PropertiesHandleException// FUNCTION FOR OUTPUTQUERY
		{
			this.put("output_query", this.RdmsQuery("OutputTable"));
		}
		
		protected void InputConditionQuery() throws PropertiesHandleException// FUNCTION FOR INPUT-CONDITION-QUERY
		{
			this.put("InputColQuery",this.RdmsQuery("InputConditonTable"));
		}
		
		protected void OutputConiditionQuery() throws PropertiesHandleException// FUNCTION FOR OUTPUT-CONDITION-QUERY
		{
			this.put("OutputColQuery",this.RdmsQuery("OutputConditionTable"));
		}
		
		protected void ClassName() throws PropertiesHandleException // FUNCTION TOS GET CLASS-NAME
		{
			this.put("ClassName", this.RdmsValue("ClassName"));
		}	
		
		protected void InputJsonPath() // FUNCTION TO GET INPUT-JSON-PATH
		{
			this.put("InputJsonPath", "InputJsonPath");
		}
		
		protected void OutputJsonPath() // FUNCTION TO GET OUTPUT-JSON-PATH
		{
		    this.put("OutputJsonPath", "OutputJsonPath");
		}
		
		protected void InputColumn() // FUNCTION TO GET INPUT-COLUMN
		{
			this.put("InputColumn", "InputColumn");
		}
		
		protected void OutputColumn() // FUNCTION TO GET OUTPUT-COLUMN
		{
			this.put("OutputColumn", "OutputColumn");
		}
		
	    protected void InputConditionColumn() // FUNCTION TO GET INPUT-CONDITION-COLUMN
		{
			this.put("InputCondColumn", "InputColumnCondtn");
		}
		
		protected void OutputConditionColumn() // FUNCTION TO GET OUTPUT-CONDITION-COLUMN
		{
			this.put("OutputCondColumn", "OutputColumnCondtn");
		}
		
	    protected void ExpectedColumn() // FUNCTION TO GET EXPECTED-COLUMN
		{
			this.put("ExpectedColumn", "ExpectedColumn");
		}
		
		protected void StatusColumn() // FUNCTION TO GET STATUS-COLUMN
		{
			this.put("StatusColumn", "StatusColumn");
		}
		
	    protected void DBdetails() throws PropertiesHandleException// FUNCTION FOR DB-DETAILS
		{
			this.put("jdbc_driver", this.RdmsValue("JDCDriver"));
			this.put("db_url", this.RdmsValue("DB_URL") + "/" + this.RdmsValue("ProjectDBName") + "_" + this.RdmsValue("UserDBName"));
			this.put("db_username", this.RdmsValue("DB_UserName"));
			this.put("db_password", this.RdmsValue("DB_Password"));
		}	
		
		public PropertiesHandle(String path) throws PropertiesHandleException
		{
			this.path = path;
			
			FileInputStream configuration = null;
			
			try 
			{
				configuration = new FileInputStream(path);
			} 
			catch (FileNotFoundException e) 
			{
				throw new PropertiesHandleException("CONFIGURATION FILE PATH DOES NOT CONTAINS CONFIG FILE", e);
			}
			try 
			{
				this.load(configuration);
			} 
			catch (IOException e) 
			{
				throw new PropertiesHandleException("ERROR IN LOADING A CONFIG FILE", e);
			}
		}
		
		public void store(String newpath) throws PropertiesHandleException
		{
			Writer writer = null;
			try 
			{
				 writer = new FileWriter(newpath);
			} 
			catch (IOException e) 
			{
				throw new PropertiesHandleException("ERROR IN WRITING A CONFIG FILE", e);
			}
			try 
			{
				this.store(writer, "File saved");
			} 
			catch (IOException e) 
			{
				throw new PropertiesHandleException("ERROR IN STORING A CONFIG FILE", e);
			};
		}
		
		public void store()
		{
			Writer writer = null;
			try 
			{
				 writer = new FileWriter(this.path);
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try 
			{
				this.store(writer, "File saved");
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
}