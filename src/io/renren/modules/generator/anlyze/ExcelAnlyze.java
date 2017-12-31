package io.renren.modules.generator.anlyze;

import io.renren.modules.generator.entity.AthleteEntity;
import io.renren.modules.generator.entity.BallReportTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelAnlyze {
	
	/*public static <T> T getAnlyzeEntity(T t){
		
		return t;
	}*/
	public static BallReportTable getAnlyzeEntity(String filepath){
		
		 File file=new File(filepath);

		 Workbook wb = null;
			BallReportTable ballReportTable=new BallReportTable();
			try {
				/*InputStream in = new FileInputStream(file);
				wb = new XSSFWorkbook(in);*/
				String fileType = filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length());
				System.out.println(fileType);
				if (fileType.equals("xls")) {
					wb = new HSSFWorkbook(new FileInputStream(filepath));
				} else if (fileType.equals("xlsx")) {
					wb = new XSSFWorkbook(new FileInputStream(filepath));
				}
				//Sheet sheet = wb.getSheetAt(0);
				// 获取合并的单元格
				int sheetNum=wb.getNumberOfSheets();		
				String[] name=new String[2];
				String[] company=new String[2];
				String[] date=new String[2];
				String[] leader=new String[2];
				String[] contact=new String[2];
				String[] phoneNum=new String[2];
				String[] coach1=new String[2];
				String[] coach2=new String[2];
				String[] coach3=new String[2];
				List<AthleteEntity> list=new ArrayList<AthleteEntity>();
				List<AthleteEntity> list2=new ArrayList<>();
				Sheet sheet0 = wb.getSheetAt(0);
				String title = sheet0.getRow(0).getCell(0).getStringCellValue();
				ballReportTable.setTitle(title);
				for(int i=0;i<sheetNum;i++){
					Sheet sheet = wb.getSheetAt(i);					
					name[i] = sheet.getRow(1).getCell(0).getStringCellValue();
					company[i] = sheet.getRow(2).getCell(3).getStringCellValue();
					date[i] = sheet.getRow(2).getCell(5).getStringCellValue();
					leader[i] = sheet.getRow(3).getCell(1).getStringCellValue();
					contact[i] = sheet.getRow(3).getCell(4).getStringCellValue();
					sheet.getRow(3).getCell(7).setCellType(Cell.CELL_TYPE_STRING);
					phoneNum[i] = sheet.getRow(3).getCell(7).getStringCellValue();
					coach1[i] = sheet.getRow(4).getCell(1).getStringCellValue();
					coach2[i] = sheet.getRow(4).getCell(4).getStringCellValue();
					sheet.getRow(4).getCell(7).setCellType(Cell.CELL_TYPE_STRING);
					coach3[i]=sheet.getRow(4).getCell(7).getStringCellValue();
					int firstRowIndex = sheet.getFirstRowNum();// 第一行
					int lastRowIndex = sheet.getLastRowNum();// 最后一行
					int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();// 获得总列数
				
					for (int startIndex = 6; startIndex <= lastRowIndex; startIndex++) {
						Row row = sheet.getRow(startIndex);// 得到指定行
						if (row != null) {
							int firstCellIndex = row.getFirstCellNum();
							int lastCellIndex = row.getLastCellNum();
							String[] message=new String[10];
							AthleteEntity athlete=new AthleteEntity();
							for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {
								Cell cell = row.getCell(cIndex);
								switch (cell.getCellType()) {
								case HSSFCell.CELL_TYPE_NUMERIC: // 数字
									if (HSSFDateUtil.isCellDateFormatted(cell)) {
										Date d = cell.getDateCellValue();
										DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
										message[cIndex]=df.format(d);
									}else
										message[cIndex]=cell.getNumericCellValue()+" ";
									break;
								case HSSFCell.CELL_TYPE_STRING: // 字符串
									message[cIndex]=cell.getStringCellValue();
									break;						

								}

							}
							if(i==0){
								if(message[0]!=null)
									athlete.setId(Integer.parseInt(message[0].trim().substring(0,message[0].indexOf(".") )));
								athlete.setRegisterCode(message[1]);
								athlete.setIdCard(message[2]);
								athlete.setName(message[3]);
								if(message[5]!=null){
									athlete.setGender(message[4].equals("男")?1:0);
									DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
									
									if(message[2]!=null){
										String birthday = message[2].substring(6,10) 
												+ "-" + message[2].substring(10,12) 
												+ "-" + message[2].substring(12,14);
										athlete.setBirthday(df.parse(birthday));
									}
									
									
								}
								
								if(message[6]!=null){
									try{
										athlete.setJerseyNumber(Integer.parseInt(message[6].trim().substring(0,message[6].indexOf(".") )));
									}catch(Exception e){
										athlete.setJerseyNumber(startIndex-1);
									}
								}
									
								athlete.setCoach(message[7]);
								athlete.setNote(message[8]);
								if(athlete.getIdCard()!=null&&!(athlete.getIdCard().trim().equals(""))){
									list.add(athlete);
								}
							}else{
								if(message[0]!=null)
									athlete.setId(Integer.parseInt(message[0].trim().substring(0,message[0].indexOf(".") )));
								athlete.setRegisterCode(message[1]);
								athlete.setIdCard(message[2]);
								athlete.setName(message[3]);
								if(message[5]!=null){
									athlete.setGender(message[4].equals("男")?1:0);
									DateFormat df = new SimpleDateFormat("yyyy-MM-dd");	
									if(message[2]!=null){
										String birthday = message[2].substring(6,10) 
												+ "-" + message[2].substring(10,12) 
												+ "-" + message[2].substring(12,14);
										athlete.setBirthday(df.parse(birthday));
									}
									
								}
								
								if(message[6]!=null){
									try{
										athlete.setJerseyNumber(Integer.parseInt(message[6].trim().substring(0,message[6].indexOf(".") )));
									}catch(Exception e){
										athlete.setJerseyNumber(startIndex-1);
									}
								}
									
								athlete.setCoach(message[7]);
								athlete.setNote(message[8]);
								if(athlete.getIdCard()!=null&&!(athlete.getIdCard().trim().equals(""))){
									list2.add(athlete);
								}
								
							}
						
						}
							
					}
				}
				ballReportTable.setName(name);
				ballReportTable.setCompany(company);
				ballReportTable.setDate(date);
				ballReportTable.setLeader(leader);
				ballReportTable.setContact(contact);
				ballReportTable.setPhoneNum(phoneNum);
				ballReportTable.setCoach1(coach1);
				ballReportTable.setCoach2(coach2);
				ballReportTable.setCoach3(coach3);
				System.out.println(list.size());
				ballReportTable.setAthletes(list);
				ballReportTable.setAthletesOfWoman(list2);
				return ballReportTable;
				// int totalColumn=sheet.getp
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException();
			}

	}

}
