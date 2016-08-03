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
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import kr.co.exsoft.eframework.util.CharConversion;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.vo.VO;

/**
 * 엑셀다운로드 View 
 *
 * @author 패키지팀
 * @since 2014. 11. 14.
 * @version 1.0
 * 
 */

public class ExcelView extends AbstractView {
	
    public ExcelView() {
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
    				11, // 폰트 크기
    				WritableFont.BOLD, // 폰트 두께
    				false, // 이탤릭 사용 유무
    				UnderlineStyle.NO_UNDERLINE, // 밑줄 스타일
    				Colour.BLACK);  // 폰트색

    		// 헤더 형식
    		WritableCellFormat headFormat = new WritableCellFormat(heard14font);
    		// 헤더 좌우 정렬
    		headFormat.setAlignment(Alignment.CENTRE);
    		// 헤더 상하 정렬
    		headFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
    		// 헤더 테두리
    		headFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
    		// 백그라운드 색
    		headFormat.setBackground(jxl.format.Colour.GRAY_25);
    		
    		// 셀형식
    		WritableCellFormat textFormat = new WritableCellFormat();
    		// 셀 좌우 정렬
    		textFormat.setAlignment(Alignment.CENTRE);
    		// 셀 상하 정렬
    		textFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
    		// 테두리
    		textFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

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
					Label label = new Label(i++, j, memberFields.get(member) != null ? memberFields.get(member).toString() : "",textFormat);
					sheet.addCell(label);
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
