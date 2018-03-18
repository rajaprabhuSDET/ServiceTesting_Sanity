package com.solartis.test.util.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;

import com.solartis.test.exception.POIException;

public class ExcelOperationsPOI 
{
	protected String path = null;
	protected Workbook workbook = null;
	protected HSSFSheet worksheet=null;
	protected String sheet_name = null;
	protected FileInputStream inputfilestream;
	protected Cell cell=null;
	protected int row_number;
	protected int column_number;
	
	
	public ExcelOperationsPOI(String path) throws POIException
	{
		 try 
		 {
			this.path=path;
			inputfilestream= new FileInputStream(new File(path));
			workbook = new HSSFWorkbook(inputfilestream);
		 } 
		 catch (IOException e) 
		 {
			System.out.println("error in opening excel");
			 throw new POIException("ERROR OCCURS IN RATING MODEL FILE PATH -- " + path, e);
		}
	}
	public void openWorkbook() throws POIException
	{
		try 
		 {
			inputfilestream= new FileInputStream(new File(this.path));
			workbook = new HSSFWorkbook(inputfilestream);
		 } 
		 catch (IOException e) 
		 {
			 throw new POIException("ERROR OCCURS WHILE OPENING THE WORKBOOK", e);
		 }
	}
	
	public void getsheets(String sheet_name)
	{
		this.sheet_name = sheet_name;
		worksheet = (HSSFSheet) this.workbook.getSheet(sheet_name);
		this.row_number = 0;
		this.column_number = 0;
	}
	
	public void getcell(int row_number,int column_number)
	{
		 this.column_number=column_number;
		 this.row_number=row_number;
         this.cell = this.worksheet.getRow(row_number).getCell(column_number);
	}
	
	@SuppressWarnings("deprecation")
	public String read_data() throws POIException
	{
		String cellvalue=null;
		if(workbook == null)
		{
			openWorkbook();
			worksheet = (HSSFSheet) this.workbook.getSheet(this.sheet_name);
		}
		cell = this.worksheet.getRow(this.row_number).getCell(this.column_number);
		switch(this.cell.getCellType()) 
		{
			case Cell.CELL_TYPE_BOOLEAN:	cellvalue= String.valueOf(cell.getBooleanCellValue());	break;
			case Cell.CELL_TYPE_NUMERIC:	
				if(HSSFDateUtil.isCellDateFormatted(cell))
				{
					Date date5 = cell.getDateCellValue();
					DateFormat date1 = new SimpleDateFormat("MM/dd/yyyy");
				    cellvalue = date1.format(date5);
				}
				else
				{
				cellvalue= String.valueOf(cell.getNumericCellValue());
				}
				break;
			case Cell.CELL_TYPE_STRING	:		cellvalue= cell.getStringCellValue();					break;
			case Cell.CELL_TYPE_ERROR	:		cellvalue= String.valueOf(cell.getErrorCellValue());	break;
			case Cell.CELL_TYPE_BLANK	:		cellvalue="";	break;
			case 2                      :   try{
								                	cellvalue =String.valueOf(cell.getNumericCellValue()); 
								                } 
								                catch(Exception e)
								                {
								                 	cellvalue="N/A";
								                }
								                break;
			default						:	cellvalue="N/A"; break;
		}
		return cellvalue;
	}
	
	@SuppressWarnings("deprecation")
	public String read_data(int rowNumber, int columnNumber) throws POIException
	{
		String cellvalue=null;
		if(workbook == null)
		{
			openWorkbook();
			worksheet = (HSSFSheet) this.workbook.getSheet(this.sheet_name);
		}
		cell = this.worksheet.getRow(rowNumber).getCell(columnNumber);
		FormulaEvaluator evaluator = this.workbook.getCreationHelper().createFormulaEvaluator();		
		evaluator.clearAllCachedResultValues();
		//CellValue cellvalue1 =evaluator.evaluate(cell);
		this.workbook.setForceFormulaRecalculation(true);
		switch(this.cell.getCellType())
		{
			case Cell.CELL_TYPE_BOOLEAN	:	cellvalue= String.valueOf(cell.getBooleanCellValue());		break;
			case Cell.CELL_TYPE_NUMERIC	:	
											if(HSSFDateUtil.isCellDateFormatted(cell))
											{
												Date date5 = cell.getDateCellValue();
												DateFormat date1 = new SimpleDateFormat("MM/dd/yyyy");
											    cellvalue = date1.format(date5);
											}
											else
											{
												cellvalue= String.valueOf(cell.getNumericCellValue());
											}
											break;
			case Cell.CELL_TYPE_STRING	:	cellvalue= cell.getStringCellValue();						break;
			case Cell.CELL_TYPE_ERROR	:	cellvalue= String.valueOf(cell.getErrorCellValue())+"err";	break;//String.valueOf(cell.getErrorCellValue());
			case Cell.CELL_TYPE_BLANK	:	cellvalue="";break;
			case Cell.CELL_TYPE_FORMULA :   
											switch(cell.getCachedFormulaResultType())
											{
												case Cell.CELL_TYPE_NUMERIC:
													if(HSSFDateUtil.isCellDateFormatted(cell))
													{
														Date date5 = cell.getDateCellValue();
														DateFormat date1 = new SimpleDateFormat("MM/dd/yyyy");
													    cellvalue = date1.format(date5);
													}
													else
													{
														cellvalue= String.valueOf(cell.getNumericCellValue());
													}
													break;
													
									            case Cell.CELL_TYPE_STRING:
									            	cellvalue= cell.getStringCellValue(); 
									            	 break;
									            case Cell.CELL_TYPE_ERROR:
									            	cellvalue= String.valueOf(cell.getErrorCellValue())+"err";
									            	break;
									            case Cell.CELL_TYPE_BLANK	:	cellvalue="";break;
									            case Cell.CELL_TYPE_BOOLEAN	:	cellvalue= String.valueOf(cell.getBooleanCellValue());		break;
											}
				                            break;
			
			default						:	cellvalue="N/A"; 										break;
		}
		return cellvalue;
		
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
	
	@SuppressWarnings("unlikely-arg-type")
	public boolean has_next_row()
	{
		boolean result;
		if (this.worksheet.getRow(row_number).getCell(0).equals("end"))
		{
			result = false;
		}
		else
		{
			result = true;
		}
		return result;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public boolean has_next_row(int columnnumberr)
	{
		boolean result;
		if (this.worksheet.getRow(row_number).getCell(columnnumberr).equals("end"))
		{
			result = false;
		}
		else
		{
			result = true;
		}
		return result;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public boolean has_next_column()
	{
		boolean result;
		if (this.worksheet.getRow(0).getCell(column_number).equals("end"))
		{
			result = false;
		}
		else
		{
			result = true;
		}
		return result;
	}
	
	public void write_data(String strData)
	{
		
		cell = this.worksheet.getRow(this.row_number).getCell(this.column_number);
		cell.setCellValue(strData);
	
		
	}
	
	public void write_data(int strData)
	{
		
		cell = this.worksheet.getRow(this.row_number).getCell(this.column_number);
		cell.setCellValue(strData);
	
		
	}
	
	public void write_data(int rownum,int columnnum,Object strData)
	{
		 cell = this.worksheet.getRow(rownum).getCell(columnnum);
		 String s=(strData.getClass()).toString();
		 //System.out.println(s);
		if(s.equals("class java.util.Date"))
		{
			 cell.setCellValue((Date) strData);	
		}
		else if (s.equals("class java.lang.String"))
		{
			 cell.setCellValue((String) strData);
		}
		else if (s.equals("class java.lang.Integer"))
		{
			int data =(Integer) strData;
			cell.setCellValue(data);
		}
		else if (s.equals("class java.lang.Float"))
		{
			float data=(Float) strData;
			cell.setCellValue(data);
		}
		else
		{
			System.out.println("Not a Valid Format to write. Note: It must be in 'String' or 'Date' or 'Integer' ");
		}
	}
	
	public void refresh()
	{
		 HSSFFormulaEvaluator.evaluateAllFormulaCells(this.workbook);
	}
	
	public void save() throws POIException
	{
		try 
		{	
			inputfilestream.close();
			 
			FileOutputStream output_file =new FileOutputStream(new File(path));  //Open FileOutputStream to write updates
			this.workbook.write(output_file); //write changes
		    output_file.close();  //close the stream    
		} 
		catch (IOException e) 
		{
			throw new POIException("ERROR OCCURS WHILE SAVING THE WORKBOOK", e);
		}
	
		
	}

	public void saveAs(String Targetexpectedpath) throws POIException
	{
		try 
		{	
			inputfilestream.close();
			FileOutputStream output_file =new FileOutputStream(new File(Targetexpectedpath));  //Open FileOutputStream to write updates
			this.workbook.write(output_file); //write changes
		    output_file.close();  //close the stream    
		} 
		catch (IOException e) 
		{
			throw new POIException("ERROR OCCURS WHILE SAVE AS THE WORKBOOK IN DIFFERENT NAME", e);
		}
	
		
	}
	
	@SuppressWarnings("resource")
	public void Copy(String Sampleexcelpath, String Targetexpectedpath) throws POIException
	{
		FileChannel source = null;
		FileChannel destination = null;

		try 
		{

			source = new FileInputStream(Sampleexcelpath).getChannel();

			destination = new FileOutputStream(Targetexpectedpath).getChannel();

			if (destination != null && source != null) 
			{
				destination.transferFrom(source, 0, source.size());
			}

		}
		
		catch (FileNotFoundException e) 
		{
			throw new POIException("ERROR OCCURS WHILE COPYING THE WORKBOOK -- FILENOTFOUND", e);
		} 
		catch (IOException e)
		{
			throw new POIException("ERROR OCCURS WHILE COPYING THE WORKBOOK -- I/O OPERATION FAILED", e);
		
		}
		finally 
		{
			try 
			{
				if (source != null) 
				{					
					source.close();					
				}
				if (destination != null) 
				{					
					destination.close();					
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
			
	}
	
	public void RenameSheet(String Sheetnamee, String  newsheetnamee)
	{
		this.workbook.getSheet(Sheetnamee);
		int sheetindex =this.workbook.getSheetIndex(this.worksheet);
		this.workbook.setSheetName(sheetindex, newsheetnamee);
		
	}
	
}



