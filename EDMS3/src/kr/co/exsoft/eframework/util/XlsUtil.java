package kr.co.exsoft.eframework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.co.exsoft.user.controller.GroupAdminController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel Paring Utls
 *
 * @author 패키지팀
 * @since 2014. 12. 2.
 * @version 1.0
 * 
 */

public class XlsUtil {
	
	protected static final Log logger = LogFactory.getLog(GroupAdminController.class);

	public List<List<String>> getExcelList(String filePath){
		
		if( filePath.substring(filePath.lastIndexOf(".")).equals(".xls")){
			return readExcelXLS(filePath);
		}else{
			return readExcelXLSX(filePath);
		}
	}
	
	private List<List<String>> readExcelXLS(String filePath){
		
        FileInputStream fis = null;
        POIFSFileSystem fs = null;

        HSSFWorkbook workbook = null;
        HSSFSheet sheet = null;
        HSSFRow row = null;
        HSSFCell cell = null;
        List<List<String>> rowList = new ArrayList<List<String>>();
        List<String> colList = new ArrayList<String>();
//        int sheets = 0;
        try {
            // Excel 파일 읽어 오기
            fis = new FileInputStream(filePath);
            fs = new POIFSFileSystem(fis);
            workbook = new HSSFWorkbook(fs);
            
            sheet = workbook.getSheetAt(0);
           
            if( sheet != null)
            {
                int nRowStartIndex = 1;                                   //기록물철의 경우 실제 데이터가 시작되는 Row지정
                int nRowEndIndex   = sheet.getLastRowNum();               //기록물철의 경우 실제 데이터가 끝 Row지정
                int nColumnStartIndex = 0;                                //기록물철의 경우 실제 데이터가 시작되는 Column지정  
                int nColumnEndIndex = sheet.getRow(0).getLastCellNum();  //기록물철의 경우 실제 데이터가 끝나는 Column지정

                String strValue = "";

                for( int i = nRowStartIndex; i <= nRowEndIndex ; i++){
                	row  = sheet.getRow( i);
                    for( int nColumn = nColumnStartIndex; nColumn <= nColumnEndIndex ; nColumn++){
                    	cell = row.getCell(( int ) nColumn);
                        if( cell == null) { continue;}
                        switch( cell.getCellType() ) {
	                        case Cell.CELL_TYPE_STRING :
	                        	strValue = cell.getRichStringCellValue().getString();
	                            break;
	
	                        case Cell.CELL_TYPE_NUMERIC :
	                            	strValue = String.valueOf(Long.toString((long)cell.getNumericCellValue()));
	                            break;
	                        case Cell.CELL_TYPE_BOOLEAN :
	                        	strValue = String.valueOf(cell.getBooleanCellValue());
	                            break;
	
	                        case Cell.CELL_TYPE_FORMULA :
	                        	strValue = String.valueOf(cell.getCellFormula());
	                            break;
	
	                        default:
	                        	strValue = cell.getRichStringCellValue().getString();
	                    }
                        colList.add(nColumn, strValue);
                        
                    }
                    rowList.add(colList);
                    colList = new  ArrayList<String>();
                }
            }
            
        } catch (FileNotFoundException e) {
        	logger.error(e);
        } catch (IOException e) {
        	logger.error(e);
        } finally {
            try {
            	
                fis.close();
            } catch (IOException e) {
            	logger.error(e);
            }
        }
        return rowList;
    }
	
	private List<List<String>> readExcelXLSX(String filePath){
        File file = new File(filePath);      
        FileInputStream fis = null;       
        XSSFWorkbook workbook = null;
        XSSFExcelExtractor extractor = null;       
//        int sheets = 0;
        
        List<List<String>> rowList = new ArrayList<List<String>>();
        List<String> colList = new ArrayList<String>();
        try {
           
            // Excel 파일 읽어 오기
            fis = new FileInputStream(file);           
            workbook = new XSSFWorkbook(filePath);           
           
            extractor = new XSSFExcelExtractor(workbook);

            extractor.setFormulasNotResults(true);
            extractor.setIncludeSheetNames(false);
            String strValue = "";
            XSSFCell cell = null;
            XSSFRow row = null;
            XSSFSheet sheet = null;
            sheet = workbook.getSheetAt(0);
            
            int nRowStartIndex = 1;                                   //기록물철의 경우 실제 데이터가 시작되는 Row지정
            int nRowEndIndex   = sheet.getLastRowNum();               //기록물철의 경우 실제 데이터가 끝 Row지정
            int nColumnStartIndex = 0;                                //기록물철의 경우 실제 데이터가 시작되는 Column지정  
            int nColumnEndIndex = sheet.getRow(0).getLastCellNum();  //기록물철의 경우 실제 데이터가 끝나는 Column지정
            
            for( int i = nRowStartIndex; i <= nRowEndIndex ; i++){
                
            	row  = sheet.getRow( i);
                for( int nColumn = nColumnStartIndex; nColumn < nColumnEndIndex ; nColumn++ ) {
                    cell = row.getCell(( int ) nColumn);
                    switch( cell.getCellType() ) {
                        case Cell.CELL_TYPE_STRING :
                        	strValue = cell.getRichStringCellValue().getString();
                            break;

                        case Cell.CELL_TYPE_NUMERIC :
                            //if(DateUtil.isCellDateFormatted(cell)) {
                            //	strValue = String.valueOf(cell.getDateCellValue());
                            //} else {
                            	//strValue = String.valueOf( cell.getNumericCellValue());
                            	strValue = String.valueOf(Long.toString((long)cell.getNumericCellValue()));


                            //}
                            break;
                        case Cell.CELL_TYPE_BOOLEAN :
                        	strValue = String.valueOf(cell.getBooleanCellValue());
                            break;

                        case Cell.CELL_TYPE_FORMULA :
                        	strValue = String.valueOf(cell.getCellFormula());
                            break;

                        default:
                        	strValue = cell.getRichStringCellValue().getString();
                    }
                    
                    colList.add(nColumn, strValue);    
                }
                rowList.add(colList);
                colList = new  ArrayList<String>();   
            }    
        } catch (FileNotFoundException e) {
        	logger.error(e);
        } catch (IOException e) {
            	logger.error(e);
        } catch (Exception e) {
        	logger.error(e);    	
        } finally {
        	try {
        		fis.close();                   
            } catch (IOException e) {
               	logger.error(e);
            }
        }
        return rowList;
  }
	
}
