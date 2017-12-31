package io.renren.modules.generator.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.Region;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	public List<String[]> readByPoi(String fileName,int sheetnum,int startrow, int datacol,int colNum,boolean isReadNullCell) {
		org.apache.poi.ss.usermodel.Workbook workbook = null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
			if (fileType.equals("xls")) {
				workbook = new HSSFWorkbook(new FileInputStream(fileName));
			} else if (fileType.equals("xlsx")) {
				workbook = new XSSFWorkbook(new FileInputStream(fileName));
			}
			
				// 创建sheet对象
				org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(sheetnum);
				int firstRowIndex = sheet.getFirstRowNum();
				int lastRowIndex = sheet.getLastRowNum();
				int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();// 获得总列数
				for (int rIndex = startrow; rIndex <= lastRowIndex; rIndex++) {
					Row row = sheet.getRow(rIndex);
					if (row != null) {
						int firstCellIndex = row.getFirstCellNum();
						int lastCellIndex = colNum;
						//int lastCellIndex = row.getLastCellNum();
						String[] s = new String[lastCellIndex];
						for (int cIndex = 0; cIndex < lastCellIndex; cIndex++) {
							Cell cell = row.getCell(cIndex);
								if (datacol != 0) {
									if (cIndex == datacol) {
										try {
											Date date = new Date();
											String brithday = null;
											cell.setCellType(Cell.CELL_TYPE_NUMERIC);
											if (fileType.equals("xls")) {
												date = HSSFDateUtil.getJavaDate(new Double(cell.getNumericCellValue()));
											} else if (fileType.equals("xlsx")) {
												date = XSSFDateUtil.getJavaDate(new Double(cell.getNumericCellValue()));
											}
											SimpleDateFormat formatter;
											formatter = new SimpleDateFormat("yyyy-MM-dd");
											brithday = formatter.format(date);
											s[cIndex] = brithday;
										} catch (Exception e) {
											e.printStackTrace();
											s[cIndex] = "0000-00-00";
										}
									}else {
										String value="";
										try{
											cell.setCellType(Cell.CELL_TYPE_STRING);
											value = cell.getStringCellValue().trim();
										}catch(Exception e){
											
										}

										
										if(!isReadNullCell){
											if(value.equals("")||value==null){
												return list;
											}
										}
										if(cIndex==2){
											if(value.equals("")||value==null){
												return list;
											}
										}
										s[cIndex] = value;
									}
								}
								else {
									String value="";
									try{
										cell.setCellType(Cell.CELL_TYPE_STRING);
										value = cell.getStringCellValue().trim();
									}catch(Exception e){
										
									}
										if(!isReadNullCell){
											if(cell.getStringCellValue().trim().equals("")||cell.getStringCellValue()==null){
												return list;
											}
										}
										if(cIndex==2){
											if(cell.getStringCellValue().trim().equals("")||cell.getStringCellValue()==null){
												return list;
											}
										}
										s[cIndex] = value;
									}
								
							}
							for(int i=0;i<s.length;i++){
								System.out.print(s[i]+":");
							}
							System.out.println();
						list.add(s);
						}else{
							return list;
						}
						
					}
					
				
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	//取合并单元格的值
	public List<String> execelpoi(String fileName,int sheetnum,int datacol) {
		List<String> list = new ArrayList<String>();
		File inputFile = new File(fileName);
		String value=null;
		try {
			InputStream is = new FileInputStream(inputFile);
			Workbook wb = null;
			String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
			if (fileType.equals("xls")) {
				wb = new HSSFWorkbook(is);
			} else if (fileType.equals("xlsx")) {
				wb = new XSSFWorkbook(is);
			}
				Sheet sheet = wb.getSheetAt(sheetnum);
				// 遍历合并区域CellRangeAddress
				sheet.getRow(0).getCell(0).setCellType(Cell.CELL_TYPE_STRING);
				String value1=sheet.getRow(0).getCell(0).getStringCellValue().trim();//2017嘉兴市青少年跆拳道锦标赛报名表
				sheet.getRow(1).getCell(0).setCellType(Cell.CELL_TYPE_STRING);
				String value2=sheet.getRow(1).getCell(0).getStringCellValue().trim();//比赛名称：2017嘉兴市青少年跆拳道锦标赛
				sheet.getRow(2).getCell(0).setCellType(Cell.CELL_TYPE_STRING);
				String value3=sheet.getRow(2).getCell(0).getStringCellValue().trim();//报名单位（盖章）：
				sheet.getRow(2).getCell(3).setCellType(Cell.CELL_TYPE_STRING);
				String value4=sheet.getRow(2).getCell(3).getStringCellValue().trim();//海宁市
				sheet.getRow(3).getCell(1).setCellType(Cell.CELL_TYPE_STRING);
				String value5=sheet.getRow(3).getCell(1).getStringCellValue().trim();//领队
				sheet.getRow(3).getCell(4).setCellType(Cell.CELL_TYPE_STRING);
				String value6=sheet.getRow(3).getCell(4).getStringCellValue().trim();//联系人
				sheet.getRow(3).getCell(7).setCellType(Cell.CELL_TYPE_STRING);
				String value7=sheet.getRow(3).getCell(7).getStringCellValue().trim();//联系电话
				sheet.getRow(4).getCell(1).setCellType(Cell.CELL_TYPE_STRING);
				String value8=sheet.getRow(4).getCell(1).getStringCellValue().trim();//教练1
				sheet.getRow(4).getCell(4).setCellType(Cell.CELL_TYPE_STRING);
				String value9=sheet.getRow(4).getCell(4).getStringCellValue().trim();//教练2
				sheet.getRow(4).getCell(7).setCellType(Cell.CELL_TYPE_STRING);
				String value10=sheet.getRow(4).getCell(7).getStringCellValue().trim();//教练3
				list.add(value1);
				list.add(value2);
				list.add(value3);
				list.add(value4);
				list.add(value5);
				list.add(value6);
				list.add(value7);
				list.add(value8);
				list.add(value9);
				list.add(value10);

				is.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	//取某个单元格的值
	public String onecell(String fileName, int sheetNumber, int row,int col,Boolean isdata){
		File inputFile = new File(fileName);
		Date date = new Date();
		String cellvalue = null;
		try {
			InputStream is = new FileInputStream(inputFile);
			Workbook wb = null;
			String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
			if (fileType.equals("xls")) {
				wb = new HSSFWorkbook(is);
			} else if (fileType.equals("xlsx")) {
				wb = new XSSFWorkbook(is);
			}
			Sheet sheet = wb.getSheetAt(sheetNumber);
		Cell cell=sheet.getRow(row).getCell(col);
		
		if(isdata){
			try {
				if (fileType.equals("xls")) {
					date = HSSFDateUtil.getJavaDate(new Double(cell.getNumericCellValue()));
				} else if (fileType.equals("xlsx")) {
					date = XSSFDateUtil.getJavaDate(new Double(cell.getNumericCellValue()));
				}

				SimpleDateFormat formatter;
				formatter = new SimpleDateFormat("yyyy-MM-dd");
				cellvalue = formatter.format(date);
			} catch (Exception e) {
				e.printStackTrace();
				cellvalue = "0000-00-00";
			}
		}else{
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cellvalue=cell.getStringCellValue().trim();
		}
		is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cellvalue;
	}

	// 取Sheet数量
	public int getSheetQuantity(String fileName){
		Workbook workbook = null;
		
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		try {
			if (fileType.equals("xls")) {
				workbook = new HSSFWorkbook(new FileInputStream(fileName));
			} else if (fileType.equals("xlsx")) {
				workbook = new XSSFWorkbook(new FileInputStream(fileName));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return workbook.getNumberOfSheets();
	}
	public String getSheetNames(String fileName){
		Workbook workbook = null;
		StringBuilder names = new StringBuilder();
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		try {
			if (fileType.equals("xls")) {
				workbook = new HSSFWorkbook(new FileInputStream(fileName));
			} else if (fileType.equals("xlsx")) {
				workbook = new XSSFWorkbook(new FileInputStream(fileName));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		int total = workbook.getNumberOfSheets();
		
		for(int i=0;i<total;i++){
			org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(i);
			names.append(sheet.getSheetName()+";");
		}
		return names.toString();
	}
	
	// 取Sheet名
	public String getSheetNameByIndex(String fileName,int iSheetIndex) {
		
		Sheet sheet = getSheetByIndex(fileName,iSheetIndex);

		return sheet.getSheetName().trim();
	}

	// 取Sheet对象
	public Sheet getSheetByIndex(String fileName, int iSheetIndex) {
		File inputFile = new File(fileName);
		Sheet sheet = null;
		try {
			InputStream is = new FileInputStream(inputFile);
			Workbook wb = null;
			String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
			if (fileType.equals("xls")) {
				wb = new HSSFWorkbook(is);
			} else if (fileType.equals("xlsx")) {
				wb = new XSSFWorkbook(is);
			}
			sheet = wb.getSheetAt(iSheetIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sheet;
	}
	public void getSubExcel(String fileName, int iSheetIndex,String newFileName){
		Sheet sheet = getSheetByIndex(fileName,iSheetIndex);
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		System.out.println("firstrownum"+firstRowNum+"last"+lastRowNum);
		String sheetName = sheet.getSheetName();
		 Workbook wb = new HSSFWorkbook();  
	         //创建工作表  
		  Sheet tmpSheet =  wb.createSheet(sheetName);  
		  for(int i= firstRowNum;i<=lastRowNum;i++){
			  Row  sr = sheet.getRow(i);
			 Row tr= tmpSheet.createRow(i);
			 copyRow(sr,tr);
		  }
	      
	        FileOutputStream fileOut;
			try {
				fileOut = new FileOutputStream(newFileName);
				
				 wb.write(fileOut);  
				 fileOut.flush();
			     fileOut.close(); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	       
	}
	public void copyRow(Row fromRow,Row toRow){
		
	
		for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {  
            Cell tmpCell = (Cell) cellIt.next();  
            Cell newCell = toRow.createCell(tmpCell.getColumnIndex());  
              copyCell(tmpCell,newCell);
        } 
	}
	public void copyCell(Cell srcCell,Cell distCell){
	  int srcCellType =srcCell.getCellType();
	  if (srcCellType == Cell.CELL_TYPE_NUMERIC) {  
		  distCell.setCellValue(srcCell.getNumericCellValue());
         
      } else if (srcCellType == Cell.CELL_TYPE_STRING) {  
          distCell.setCellValue(srcCell.getStringCellValue());  
      } else if (srcCellType == Cell.CELL_TYPE_BLANK) {  
          // nothing21  
      } else if (srcCellType == Cell.CELL_TYPE_BOOLEAN) {  
          distCell.setCellValue(srcCell.getBooleanCellValue());  
      } else if (srcCellType == Cell.CELL_TYPE_ERROR) {  
          distCell.setCellErrorValue(srcCell.getErrorCellValue());  
      } else if (srcCellType == Cell.CELL_TYPE_FORMULA) {  
          distCell.setCellFormula(srcCell.getCellFormula());  
      } 
	  
	}
	
	
	
	
	
}

class XSSFDateUtil extends DateUtil {
	protected static int absoluteDay(Calendar cal, boolean use1904windowing) {
		return DateUtil.absoluteDay(cal, use1904windowing);
	}
}
