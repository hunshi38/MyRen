package io.renren.modules.generator.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.Region;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class POIUtils {  

    /** 
     * 复制一个单元格样式到目的单元格样式 
     * @param fromStyle 
     * @param toStyle 
     */  
    public static void copyCellStyle(HSSFCellStyle fromStyle,  
            HSSFCellStyle toStyle) {  
        toStyle.setAlignment(fromStyle.getAlignment());  
        //边框和边框颜色  
        toStyle.setBorderBottom(fromStyle.getBorderBottom());  
        toStyle.setBorderLeft(fromStyle.getBorderLeft());  
        toStyle.setBorderRight(fromStyle.getBorderRight());  
        toStyle.setBorderTop(fromStyle.getBorderTop());  
        toStyle.setTopBorderColor(fromStyle.getTopBorderColor());  
        toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());  
        toStyle.setRightBorderColor(fromStyle.getRightBorderColor());  
        toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());  
          
        //背景和前景  
        toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());  
        toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());  
          
        toStyle.setDataFormat(fromStyle.getDataFormat());  
        toStyle.setFillPattern(fromStyle.getFillPattern());  
//      toStyle.setFont(fromStyle.getFont(null));  
        toStyle.setHidden(fromStyle.getHidden());  
        toStyle.setIndention(fromStyle.getIndention());//首行缩进  
        toStyle.setLocked(fromStyle.getLocked());  
        toStyle.setRotation(fromStyle.getRotation());//旋转  
        toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());  
        toStyle.setWrapText(fromStyle.getWrapText());  
          
    }  
 // 取Sheet对象
 	public Sheet getSheetByIndex(String fileName, int iSheetIndex) {
 		File inputFile = new File(fileName);
 		HSSFSheet sheet = null;
 		try {
 			InputStream is = new FileInputStream(inputFile);
 			HSSFWorkbook wb = null;
 			String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
 			if (fileType.equals("xls")) {
 				wb = new HSSFWorkbook(is);
 			} else if (fileType.equals("xlsx")) {
 				//wb = new XSSFWorkbook(is);
 			}
 			sheet = wb.getSheetAt(iSheetIndex);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}

 		return sheet;
 	}
    /** 
     * Sheet复制 
     * @param fromSheet 
     * @param toSheet 
     * @param copyValueFlag 
     */  
    public static void copySheet(HSSFWorkbook wb,HSSFSheet fromSheet, HSSFSheet toSheet,  
            boolean copyValueFlag) {  
        //合并区域处理  
        mergerRegion(fromSheet, toSheet);  
        for (Iterator rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {  
            HSSFRow tmpRow = (HSSFRow) rowIt.next();  
            HSSFRow newRow = toSheet.createRow(tmpRow.getRowNum());  
            //行复制  
            copyRow(wb,tmpRow,newRow,copyValueFlag);  
        }  
    }  
    /** 
     * 行复制功能 
     * @param fromRow 
     * @param toRow 
     */  
    public static void copyRow(HSSFWorkbook wb,HSSFRow fromRow,HSSFRow toRow,boolean copyValueFlag){  
        for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {  
            HSSFCell tmpCell = (HSSFCell) cellIt.next();  
            HSSFCell newCell = toRow.createCell(tmpCell.getCellNum());  
            copyCell(wb,tmpCell, newCell, copyValueFlag);  
        }  
    }  
    /** 
    * 复制原有sheet的合并单元格到新创建的sheet 
    *  
    * @param sheetCreat 新创建sheet 
    * @param sheet      原有的sheet 
    */  
    public static void mergerRegion(HSSFSheet fromSheet, HSSFSheet toSheet) {  
       int sheetMergerCount = fromSheet.getNumMergedRegions();  
       for (int i = 0; i < sheetMergerCount; i++) {  
        Region mergedRegionAt = fromSheet.getMergedRegionAt(i);  
        toSheet.addMergedRegion(mergedRegionAt);  
       }  
    }  
    /** 
     * 复制单元格 
     *  
     * @param srcCell 
     * @param distCell 
     * @param copyValueFlag 
     *            true则连同cell的内容一起复制 
     */  
    public static void copyCell(HSSFWorkbook wb,HSSFCell srcCell, HSSFCell distCell,  
            boolean copyValueFlag) {  
        HSSFCellStyle newstyle=wb.createCellStyle();  
        copyCellStyle(srcCell.getCellStyle(), newstyle);  
         
        //样式  
        distCell.setCellStyle(newstyle);  
        //评论  
        if (srcCell.getCellComment() != null) {  
            distCell.setCellComment(srcCell.getCellComment());  
        }  
        // 不同数据类型处理  
        int srcCellType = srcCell.getCellType();  
        distCell.setCellType(srcCellType);  
        if (copyValueFlag) {  
            if (srcCellType == HSSFCell.CELL_TYPE_NUMERIC) {  
                if (HSSFDateUtil.isCellDateFormatted(srcCell)) {  
                    distCell.setCellValue(srcCell.getDateCellValue());  
                } else {  
                    distCell.setCellValue(srcCell.getNumericCellValue());  
                }  
            } else if (srcCellType == HSSFCell.CELL_TYPE_STRING) {  
                distCell.setCellValue(srcCell.getRichStringCellValue());  
            } else if (srcCellType == HSSFCell.CELL_TYPE_BLANK) {  
                // nothing21  
            } else if (srcCellType == HSSFCell.CELL_TYPE_BOOLEAN) {  
                distCell.setCellValue(srcCell.getBooleanCellValue());  
            } else if (srcCellType == HSSFCell.CELL_TYPE_ERROR) {  
                distCell.setCellErrorValue(srcCell.getErrorCellValue());  
            } else if (srcCellType == HSSFCell.CELL_TYPE_FORMULA) {  
                distCell.setCellFormula(srcCell.getCellFormula());  
            } else { // nothing29  
            }  
        }  
    }  
}  