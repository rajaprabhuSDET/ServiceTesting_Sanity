package com.solartis.test.util.common;

import java.io.File;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class excel_operation 
{
	private Workbook workbook = null;
	private String path_str = null;
	private String sheet_name = null;
	private int row_number;
	private int column_number;
	
	private Sheet worksheet = null;
	
	public excel_operation(String path_str)
	{
		this.path_str = path_str;
		
		File workbook = new File(this.path_str);
		try {
			this.workbook = Workbook.getWorkbook(workbook);
		} catch (BiffException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getsheets(String sheet_name)
	{
		this.sheet_name = sheet_name;
		worksheet =  workbook.getSheet(this.sheet_name);
		this.row_number = 0;
		this.column_number = 0;
	}
	
	public String read_data()
	{
		if (!(this.worksheet.getCell(this.column_number,this.row_number).getContents().toString().equals("end")))
		{
			return this.worksheet.getCell(this.column_number,this.row_number).getContents().toString();
		}
		else
		{
			return null; // exception for end of line
		}
	}
	
	public String read_data(int row_number,int column_number)
	{
		if (!(this.worksheet.getCell(column_number,row_number).getContents().toString().equals("end")))
		{
			return this.worksheet.getCell(column_number,row_number).getContents().toString();
		}
		else
		{
			return null; // exception for end of line
		}
	}
	
	public String get_sheetname()
	{
		if(this.sheet_name == null)
		{
			
		}
		return this.sheet_name;
	}
	
	public int get_rownumber()
	{
		return this.row_number;
	}
	
	public int get_columnnumber()
	{
		return this.column_number;
	}
	
	public void set_rownumber(int row_number)
	{
		this.row_number = row_number;
	}
	
	public void set_columnnumber(int column_number)
	{
		this.column_number = column_number;
	}
	public void next_row()
	{
		row_number++;
	}
	
	public void next_col()
	{
		column_number++;
	}
	
	public boolean has_next_row()
	{
		boolean result;
		if (this.worksheet.getCell(0,row_number).getContents().toString().equals("end"))
		{
			result = false;
		}
		else
		{
			result = true;
		}
		return result;
	}
	
	public boolean has_next_column()
	{
		boolean result;
		if (this.worksheet.getCell(column_number,0).getContents().toString().equals("end"))
		{
			result = false;
		}
		else
		{
			result = true;
		}
		return result;
	}
	public void write_data(String strData) throws IOException, RowsExceededException, WriteException
	{
		WritableWorkbook wwbCopy = null;
		WritableSheet shSheet = null;
		
			wwbCopy = Workbook.createWorkbook(new File(path_str), this.workbook);
			shSheet = wwbCopy.getSheet(this.sheet_name);
			Label labTemp = new Label(this.column_number,this.row_number, strData);
			shSheet.addCell(labTemp);
			wwbCopy.write();
            wwbCopy.close();
	        		//wwbCopy.write();
		
			wwbCopy = null;
			shSheet = null;
			
		
		
		
	}
	
	
	
	
}
