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
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.eframework.vo.VO;

/**
 * 엑셀다운로드 통계 합계처리 View 
 *
 * @author 패키지팀
 * @since 2014. 11. 14.
 * @version 1.0
 * 
 */

public class ExcelSumView extends AbstractView {
	
    public ExcelSumView() {
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
    	
    	String groupField = model.get("groupField").toString();			// 소계 기준 컬럼명		
    	    	
    	String preUserNm = "";
    	
    	int i = 0 ; 
		int j = 0;	
		
		int create_cnt=0;
		int read_cnt=0;
		int update_cnt= 0;
		int delete_cnt = 0;
		
		int doc_cnt = 0;
		int page_cnt = 0;
		long page_total = 0;
		
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
    		textFormat.setAlignment(Alignment.RIGHT);									// 셀 좌우 정렬    		
    		textFormat.setVerticalAlignment(VerticalAlignment.CENTRE);			// 셀 상하 정렬
    		textFormat.setBorder(Border.ALL, BorderLineStyle.THIN);			    	// 테두리
    		
    		WritableCellFormat textColorFormat = new WritableCellFormat();    		
    		textColorFormat.setAlignment(Alignment.RIGHT);									// 셀 좌우 정렬    		
    		textColorFormat.setVerticalAlignment(VerticalAlignment.CENTRE);			// 셀 상하 정렬
    		textColorFormat.setBorder(Border.ALL, BorderLineStyle.THIN);			    	// 테두리
    		textColorFormat.setBackground(jxl.format.Colour.GRAY_25);

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
					
					// 기준 컬럼명 변경된 경우 
					if(member.equals(groupField) && !preUserNm.equals("") &&
							!memberFields.get(member).toString().equals(preUserNm) )	{

						// 각 통계 페이지에 따른 별도 처리 
						if(groupField.equals(Constant.USER_DOC_STATISTICS) && cell_headers.length == 8)	{					// 사용자별 등록/활용 현황
							sheet.mergeCells(0,j,3,j);		// 셀병합처리
							sheet.addCell(new Label(0,j,"소계",textColorFormat));
							sheet.addCell(new Number(4,j,create_cnt,sumFormat));
							sheet.addCell(new Number(5,j,read_cnt,sumFormat));
							sheet.addCell(new Number(6,j,update_cnt,sumFormat));
							sheet.addCell(new Number(7,j,delete_cnt,sumFormat));
						}else if(groupField.equals(Constant.GROUP_DOC_STATISTICS))	{		// 부서별 등록/활용 현황
							sheet.mergeCells(0,j,2,j);		// 셀병합처리
							sheet.addCell(new Label(0,j,"소계",textColorFormat));
							sheet.addCell(new Number(3,j,create_cnt,sumFormat));
							sheet.addCell(new Number(4,j,read_cnt,sumFormat));
							sheet.addCell(new Number(5,j,update_cnt,sumFormat));
							sheet.addCell(new Number(6,j,delete_cnt,sumFormat));
						}else if(groupField.equals(Constant.USER_DOC_STATISTICS) && cell_headers.length == 7 )	{		// 사용자/문서함별 보유 현황
							sheet.mergeCells(0,j,3,j);		// 셀병합처리
							sheet.addCell(new Label(0,j,"소계",textColorFormat));							
							sheet.addCell(new Number(4,j,doc_cnt,sumFormat));
							sheet.addCell(new Number(5,j,page_cnt,sumFormat));
							sheet.addCell(new Label(6,j,StringUtil.fileSize(page_total),textColorFormat));
						}else if(groupField.equals(Constant.TYPE_DOC_STATISTICS) ||  groupField.equals(Constant.CODE_DOC_STATISTICS))	{		// 문서유형별 보유 현황 || 보안듭급별 보유현황
							sheet.mergeCells(0,j,1,j);		// 셀병합처리
							sheet.addCell(new Label(0,j,"소계",textColorFormat));							
							sheet.addCell(new Number(2,j,doc_cnt,sumFormat));
							sheet.addCell(new Number(3,j,page_cnt,sumFormat));
							sheet.addCell(new Label(4,j,StringUtil.fileSize(page_total),textColorFormat));
						}
						
						
						// NEXT 기준 컬럼 합계 정보 초기화
						create_cnt = 0;
						read_cnt = 0;
						update_cnt = 0;
						delete_cnt = 0;
						
						doc_cnt = 0;
						page_cnt = 0;
						page_total = 0;
												
						j++;			// NEXT DATA
					}
					
					
					// 소계처리 :: 사용자별 등록/활용 현황
					if(member.equals(Constant.SUM_CREATE_CNT))	{
						create_cnt += Integer.parseInt(memberFields.get(member).toString());
					}else if(member.equals(Constant.SUM_READ_CNT))	{
						read_cnt += Integer.parseInt(memberFields.get(member).toString());
					}else if(member.equals(Constant.SUM_UPDATE_CNT))	{
						update_cnt += Integer.parseInt(memberFields.get(member).toString());
					}else if(member.equals(Constant.SUM_DELETE_CNT))	{
						delete_cnt += Integer.parseInt(memberFields.get(member).toString());
					}
					
					// 사용자/문서함별 보유 현황
					else if(member.equals(Constant.SUM_DOC_CNT))	{
						doc_cnt += Integer.parseInt(memberFields.get(member).toString());
					}else if(member.equals(Constant.SUM_FILE_CNT))	{
						page_cnt += Integer.parseInt(memberFields.get(member).toString());
					}else if(member.equals(Constant.SUM_PAGE_TOTAL))	{
						page_total += Long.parseLong(memberFields.get(member).toString());
					}
					
					

					// 데이터값에 따라 포맷 변경 처리	
					if(member.equals(Constant.SUM_CREATE_CNT) || member.equals(Constant.SUM_READ_CNT) 
							|| member.equals(Constant.SUM_UPDATE_CNT) || member.equals(Constant.SUM_DELETE_CNT) 
							|| member.equals(Constant.SUM_DOC_CNT) || member.equals(Constant.SUM_FILE_CNT) 
							) {						
						Number label = new Number(i++, j, memberFields.get(member) != null ? Integer.parseInt(memberFields.get(member).toString()) : 0 ,integerFormat);
						sheet.addCell(label);						
					}else if(member.equals(Constant.SUM_PAGE_TOTAL)) {
						Label label = new Label(i++, j, memberFields.get(member) != null ? StringUtil.fileSize( Long.parseLong(memberFields.get(member).toString())) : "0",textFormat);
						sheet.addCell(label);						
					}else {					
						Label label = new Label(i++, j, memberFields.get(member) != null ? memberFields.get(member).toString() : "",textFormat);
						sheet.addCell(label);
					}
						
					// 이전 기준컬럼값 저장
					if(member.equals(groupField))	{						
						preUserNm = memberFields.get(member).toString();
					}

					
				}
				
			}
    		
    		// 3. 마지막 데이터 소계처리    		
    		j++;
    		
    		if(groupField.equals(Constant.USER_DOC_STATISTICS) && cell_headers.length == 8 )	{
	    		sheet.mergeCells(0,j,3,j);		// 셀병합처리
	    		sheet.addCell(new Label(0,j,"소계",textColorFormat));
				sheet.addCell(new Number(4,j,create_cnt,sumFormat));
				sheet.addCell(new Number(5,j,read_cnt,sumFormat));
				sheet.addCell(new Number(6,j,update_cnt,sumFormat));
				sheet.addCell(new Number(7,j,delete_cnt,sumFormat));
    		}else if(groupField.equals(Constant.GROUP_DOC_STATISTICS))	{
    			sheet.mergeCells(0,j,2,j);		// 셀병합처리
	    		sheet.addCell(new Label(0,j,"소계",textColorFormat));
				sheet.addCell(new Number(3,j,create_cnt,sumFormat));
				sheet.addCell(new Number(4,j,read_cnt,sumFormat));
				sheet.addCell(new Number(5,j,update_cnt,sumFormat));
				sheet.addCell(new Number(6,j,delete_cnt,sumFormat));
    		}else if(groupField.equals(Constant.USER_DOC_STATISTICS) && cell_headers.length == 7 )	{		// 사용자/문서함별 보유 현황
				sheet.mergeCells(0,j,3,j);		// 셀병합처리
				sheet.addCell(new Label(0,j,"소계",textColorFormat));							
				sheet.addCell(new Number(4,j,doc_cnt,sumFormat));
				sheet.addCell(new Number(5,j,page_cnt,sumFormat));
				sheet.addCell(new Label(6,j,StringUtil.fileSize(page_total),textColorFormat));			
			}else if(groupField.equals(Constant.TYPE_DOC_STATISTICS) || groupField.equals(Constant.CODE_DOC_STATISTICS) )	{		// 문서유형별 보유 현황  || 보안듭급별 보유현황
				sheet.mergeCells(0,j,1,j);		// 셀병합처리 
				sheet.addCell(new Label(0,j,"소계",textColorFormat));							
				sheet.addCell(new Number(2,j,doc_cnt,sumFormat));
				sheet.addCell(new Number(3,j,page_cnt,sumFormat));
				sheet.addCell(new Label(4,j,StringUtil.fileSize(page_total),textColorFormat));
			}
			
    		// 4.엑셀 생성
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
