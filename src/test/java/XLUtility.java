

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLUtility {

	public FileInputStream fi;
	public FileOutputStream fo;
	public XSSFWorkbook workbook;
	public XSSFSheet sheet;
	public XSSFRow row;
	public XSSFCell cell;
	public CellStyle style;   
	String path;
	
	XLUtility(String path)
	{
		this.path=path;
	}
		
	

	
	public void setCellData(String sheetName,int rownum,int colnum,String data) throws IOException
	{
		File xlfile=new File(path);
		if(!xlfile.exists())    // If file not exists then create new file
		{
		workbook=new XSSFWorkbook();
		fo=new FileOutputStream(path);
		workbook.write(fo);
		}
				
		fi=new FileInputStream(path);
		workbook=new XSSFWorkbook(fi);
			
		if(workbook.getSheetIndex(sheetName)==-1) // If sheet not exists then create new Sheet
			workbook.createSheet(sheetName);
		
		sheet=workbook.getSheet(sheetName);
					
		if(sheet.getRow(rownum)==null)   // If row not exists then create new Row
				sheet.createRow(rownum);
		row=sheet.getRow(rownum);
		
		cell=row.createCell(colnum);
		cell.setCellValue(data);
		fo=new FileOutputStream(path);
		workbook.write(fo);		
		workbook.close();
		fi.close();
		fo.close();
	}
	
	

	

	
}