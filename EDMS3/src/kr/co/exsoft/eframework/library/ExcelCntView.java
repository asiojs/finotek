package kr.co.exsoft.eframework.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.write.Number;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.util.CharConversion;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.vo.VO;

/**
 * 엑셀다운로드 통계 처리 View 
 *
 * @author 패키지팀
 * @since 2014. 11. 14.
 * @version 1.0
 * 
 */

public class ExcelCntView extends AbstractView {
	
    public ExcelCntView() {
    	setContentType("application/octet-stream; charset=UTF-8");
    }
    
    @Override
    protected void renderMergedOutputModel(@SuppressWarnings("rawtypes") Map model, HttpServletRequest request,
        HttpServletResponse response) throws Exception {

    	InputStream is = null;
    	 
    	@SuppressWarnings("unchecked")
		List<? extends VO> list = (ArrayList<VO>)model.get("list");

    	String fileName = model.get("fileName").toString();

    	File file = new File(ConfigData.getString("FILE_DOWN_PATH")+ "/" + UUID.randomUUID().toString());
    	WritableWorkbook workbook = Workbook.createWorkbook(file);

    	String[] members = (String [])model.get("members");
    	String[] cell_headers = (String [])model.get("cell_headers");
    	int[] cell_widths = (int [])model.get("cell_widths");
    	
    	int i = 0 ; 
		int j = 0;	

    	try {
    		
    		// 0. 폰트 및 환경설정 
    		
    		// 폰트 포맷
    		WritableFont heard14font = new WritableFont(WritableFont.ARIAL, // 폰트이름
    				10, // 폰트 크기
    				WritableFont.BOLD, // 폰트 두께
    				false, // 이탤릭 사용 유무
    				UnderlineStyle.NO_UNDERLINE, // 밑줄 스타일
    				Colour.BLACK);  // 폰트색

    		// 헤더 형식
    		WritableCellFormat headFormat = new WritableCellFormat(heard14font);			    		
    		headFormat.setAlignment(Alignment.CENTRE);    							// 헤더 좌우 정렬
    		headFormat.setVerticalAlignment(VerticalAlignment.CENTRE);			// 헤더 상하 정렬							 
    		headFormat.setBorder(Border.ALL, BorderLineStyle.THIN);				// 헤더 테두리					
    		headFormat.setBackground(jxl.format.Colour.GRAY_50);					// 백그라운드 색
    		
    		// 셀형식 - 문자
    		WritableCellFormat textFormat = new WritableCellFormat();    		
    		textFormat.setAlignment(Alignment.CENTRE);									// 셀 좌우 정렬    		
    		textFormat.setVerticalAlignment(VerticalAlignment.CENTRE);			// 셀 상하 정렬
    		textFormat.setBorder(Border.ALL, BorderLineStyle.THIN);			    	// 테두리

    		// 셀형식 - 숫자	
    		NumberFormat numberFormat = new NumberFormat("###,##0");
    		WritableCellFormat integerFormat = new WritableCellFormat(numberFormat);
    		integerFormat.setAlignment(Alignment.CENTRE);
    		integerFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
    		integerFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

    		// 소계 셀형식
    		WritableCellFormat sumFormat = new WritableCellFormat(numberFormat);
    		sumFormat.setAlignment(Alignment.CENTRE);
    		sumFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
    		sumFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
    		sumFormat.setBackground(jxl.format.Colour.GRAY_25);
    		
    		// 1.시트 생성
    		WritableSheet sheet = workbook.createSheet("Sheet1", 0);

    		for (String header : cell_headers) {
				sheet.setColumnView(i, cell_widths[i]);
				Label label = new Label(i++, 0, header,headFormat);
				sheet.addCell(label);
			}

    		// 2. 행별로 각 셀 데이터 세팅.
    		for (VO vo : list) {
    			
				i = 0; j++;

				// VO 객체의 멤버변수들을 얻는다.
				HashMap<String, Object> memberFields = CommonUtil.getMemberFields(vo);
				
				// 열 개수 만큼 반복하며 멤버들의 값을 셀에 삽입한다.				
				for (String member : members) {

					// 데이터값에 따라 포맷 변경 처리	
					if(member.equals(Constant.SUM_CREATE_CNT) || member.equals(Constant.SUM_READ_CNT) 
							|| member.equals(Constant.SUM_UPDATE_CNT) || member.equals(Constant.SUM_DELETE_CNT) 
							|| member.equals(Constant.SUM_DOC_CNT) || member.equals(Constant.SUM_FILE_CNT) 
							) {						
						Number label = new Number(i++, j, memberFields.get(member) != null ? Integer.parseInt(memberFields.get(member).toString()) : 0 ,integerFormat);
						sheet.addCell(label);						
					}else {					
						Label label = new Label(i++, j, memberFields.get(member) != null ? memberFields.get(member).toString() : "",textFormat);
						sheet.addCell(label);
					}
						
				}
				
			}

    		// 3.엑셀 생성
    		workbook.write();
			workbook.close();
	        
			response.setContentType(getContentType());
	        response.setContentLength((int)file.length());      			
	        response.setHeader("Content-Disposition", "attachment; fileName=\"" + CharConversion.K2E(fileName) + "\";");	        
	        response.setHeader("Content-Transfer-Encoding", "binary");
	        
	        OutputStream out = response.getOutputStream();        
	        FileInputStream fis = null;
	        
	        fis = new FileInputStream(file);
            FileCopyUtils.copy(fis, out);
	        
    	}catch(Exception e) {
    		e.printStackTrace();
    	}finally {
        
            if (is != null) {
            	try {
                    is.close();
                } catch(IOException ex) {	}
			}
        }

    }

}
