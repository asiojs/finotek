package kr.co.exsoft.eframework.util;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.lang.Math;

import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.vo.VO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

/**
 * JFreeChart Class
 *
 * @author 패키지팀
 * @since 2014. 12. 4.
 * @version 1.0
 * 
 */

public class ChartUtil {

	/**
	 * 
	 * <pre>
	 * 1. 개용 : JfreeChart 막대그래프 그리기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : barChart
	 * @param model
	 * @return
	 * @throws Exception String
	 */
	public static String barChart(HashMap<String,Object> model) throws Exception {
			
		String filePath = "";
		String mTitle = model.get("mTitle").toString();
		String xTitle = model.get("xTitle").toString();
		String yTitle = model.get("yTitle").toString();
				
  		int width = Integer.parseInt(model.get("width").toString()); 
		int height = Integer.parseInt(model.get("height").toString());
		
		DefaultCategoryDataset dataset  = getCategoryDataSet(model);		

		JFreeChart chart = ChartFactory.createBarChart(mTitle,xTitle,yTitle,dataset,PlotOrientation.VERTICAL,true,true,false);

		chart.setBackgroundPaint(Color.white);
		chart.getTitle().setPaint(Color.blue); 
		chart.getTitle().setFont(new Font("돋움", Font.BOLD, 20));
		chart.getLegend().setItemFont(new Font("돋움", Font.PLAIN, 15));
		
		// ToolTip 
		CategoryPlot p = chart.getCategoryPlot(); 
		
		p.setRangeGridlinePaint(Color.red); 
		p.setBackgroundPaint(Color.white);
		  
		Font font = p.getDomainAxis().getLabelFont();		
		p.getDomainAxis().setLabelFont(new Font("돋움", font.getStyle(), font.getSize()));			// X축 라벨
		p.getDomainAxis().setTickLabelFont(new Font("돋움", font.getStyle(), 10));					// X축 도메인
		  
		font = p.getRangeAxis().getLabelFont();
		p.getRangeAxis().setLabelFont(new Font("돋움", font.getStyle(), font.getSize()));				// Y축 라벨
		p.getRangeAxis().setTickLabelFont(new Font("돋움", font.getStyle(), 10));						// Y축 범위
		
		
		String targetPath = ConfigData.getString("FILE_UPLOAD_PATH") + UUID.randomUUID().toString();	
		UtilFileApp.createDir(targetPath);		
		String targetFile = targetPath+ "/"+UUID.randomUUID().toString();
		
		File file = new File(targetFile);
		ChartUtilities.saveChartAsJPEG(file, chart, width, height);
		
		// WebService 폴더로 File Move :: 주기적으로 배치프로그램에서 삭제처리
		filePath = UUID.randomUUID().toString() + ".jpg";
		UtilFileApp.copyfile(targetFile, ConfigData.getString("CHART_FILE_PREVIEW_PATH")+filePath);
				
		return filePath;
	}

	public static String lineChart(HashMap<String,Object> model) throws Exception {
		
		String filePath = "";
		String mTitle = model.get("mTitle").toString();
		String xTitle = model.get("xTitle").toString();
		String yTitle = model.get("yTitle").toString();
			
  		int width = Integer.parseInt(model.get("width").toString()); 
		int height = Integer.parseInt(model.get("height").toString());
		
		DefaultCategoryDataset dataset  = getCategoryDataSet(model);		

		JFreeChart chart = ChartFactory.createLineChart(mTitle, xTitle, yTitle, dataset, PlotOrientation.VERTICAL, true, true, false);

		chart.setBackgroundPaint(Color.white);
		chart.getTitle().setPaint(Color.blue); 
		chart.getTitle().setFont(new Font("돋움", Font.BOLD, 20));
		chart.getLegend().setItemFont(new Font("돋움", Font.PLAIN, 15));
		
		// ToolTip 
		CategoryPlot p = chart.getCategoryPlot(); 
		
		p.setRangeGridlinePaint(Color.red); 
		p.setBackgroundPaint(Color.white);
		  
		Font font = p.getDomainAxis().getLabelFont();		
		p.getDomainAxis().setLabelFont(new Font("돋움", font.getStyle(), font.getSize()));			// X축 라벨
		p.getDomainAxis().setTickLabelFont(new Font("돋움", font.getStyle(), 10));					// X축 도메인
		  
		font = p.getRangeAxis().getLabelFont();
		p.getRangeAxis().setLabelFont(new Font("돋움", font.getStyle(), font.getSize()));				// Y축 라벨
		p.getRangeAxis().setTickLabelFont(new Font("돋움", font.getStyle(), 10));						// Y축 범위
		
		NumberAxis rangeAxis = (NumberAxis) p.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) p.getRenderer();
		renderer.setDrawOutlines(true);
		renderer.setUseFillPaint(true);

		
		String targetPath = ConfigData.getString("FILE_UPLOAD_PATH") + UUID.randomUUID().toString();	
		UtilFileApp.createDir(targetPath);		
		String targetFile = targetPath+ "/"+UUID.randomUUID().toString();
		
		File file = new File(targetFile);
		ChartUtilities.saveChartAsJPEG(file, chart, width, height);
		
		// WebService 폴더로 File Move :: 주기적으로 배치프로그램에서 삭제처리
		filePath = UUID.randomUUID().toString() + ".jpg";
		UtilFileApp.copyfile(targetFile, ConfigData.getString("CHART_FILE_PREVIEW_PATH")+filePath);
				
		return filePath;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  JfreeChart 파이차트 그리기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : pieChart
	 * @param model
	 * @return
	 * @throws Exception String
	 */
	public static String pieChart(HashMap<String,Object> model) throws Exception {
		
		String filePath = "";
		String mTitle = model.get("mTitle").toString();
		
  		int width = Integer.parseInt(model.get("width").toString()); 
		int height = Integer.parseInt(model.get("height").toString());
		
		DefaultPieDataset dataset = getPieDataSet(model);
		
		JFreeChart chart = ChartFactory.createPieChart(mTitle, dataset, true,true, false);
		chart.setBackgroundPaint(new Color(246, 246, 246));
		
		chart.getTitle().setMargin(0, 10, 10, 25);
		chart.getTitle().setPaint(new Color(102, 102, 102));  
		chart.getTitle().setFont(new Font("돋움", Font.BOLD, 20));
		
		chart.getLegend().setMargin(10, 10, 10, 25);
		chart.getLegend().setItemFont(new Font("돋움", Font.PLAIN, 10));

		PiePlot plot = (PiePlot)chart.getPlot();
		plot.setLabelFont(new Font("돋움", Font.PLAIN, 12));		
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
			
		String targetPath = ConfigData.getString("FILE_UPLOAD_PATH") + UUID.randomUUID().toString();	
		UtilFileApp.createDir(targetPath);		
		String targetFile = targetPath+ "/"+UUID.randomUUID().toString();
		
		File file = new File(targetFile);
		ChartUtilities.saveChartAsJPEG(file, 1, chart, width, height);               
	
		
		// WebService 폴더로 File Move :: 주기적으로 배치프로그램에서 삭제처리
		filePath = UUID.randomUUID().toString() + ".jpg";
		UtilFileApp.copyfile(targetFile, ConfigData.getString("CHART_FILE_PREVIEW_PATH")+filePath);

		return filePath;
	}
		
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : Pie 데이터 설정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getPieDataSet
	 * @param model
	 * @return
	 * @throws Exception DefaultPieDataset
	 */
	public static DefaultPieDataset getPieDataSet(HashMap<String,Object> model) throws Exception {
		
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		@SuppressWarnings("unchecked")
		List<? extends VO> list = (ArrayList<VO>)model.get("list");

		String[] members = (String [])model.get("members");

		String preXvalue = "";
		String groupField = model.get("groupField").toString();
		
		// 구분항목 && Y좌표 && 통계항목명
		String statisticsType = model.get("statisticsType").toString();
		String colType = model.get("colType").toString();			
				
		int doc_cnt = 0;
		int page_cnt = 0;
		long page_total = 0;
				
		// 부서별 등록활용 현황
		int create_cnt = 0;
		int read_cnt = 0;
		int update_cnt = 0;
		int delete_cnt = 0;


		// 열 개수 만큼 반복하며 멤버들의 값을 셀에 삽입한다.				
		for (VO vo : list) {

			// VO 객체의 멤버변수들을 얻는다.
			HashMap<String, Object> memberFields = CommonUtil.getMemberFields(vo);
					
			// 열 개수 만큼 반복하며 멤버들의 값을 셀에 삽입한다.				
			for (String member : members) {

				// NEXT X좌표인 경우 
				if( member.equals(groupField) && !preXvalue.equals("") &&
						!memberFields.get(member).toString().equals(preXvalue) 
						&& !statisticsType.equals(Constant.CHART_DECADE_STATUS)			// 기간별 문서현황 집계처리 제외
						&& !statisticsType.equals(Constant.CHART_FOLDER_STATUS)			// 문서함/폴더별 보유현황 집계처리 제외
						)	{
					
					// 문서유형별 보유현황 || 보안등급별 보유현황
					if(statisticsType.equals(Constant.CHART_DOC_TYPE) || statisticsType.equals(Constant.CHART_SECURITY_STATUS) )	{	
						
						if(colType.equals(Constant.SUM_DOC_CNT)) {
							dataset.setValue(preXvalue,doc_cnt);	
						}else if(colType.equals(Constant.SUM_FILE_CNT)) {
							dataset.setValue(preXvalue,page_cnt);	
						}else if(colType.equals(Constant.SUM_PAGE_TOTAL)) {
							dataset.setValue(preXvalue,page_total);	
						}						
						
						doc_cnt = 0;
						page_cnt = 0;
						page_total = 0;
						
					}
					
					// 부서별 등록/활용 현황
					else if(statisticsType.equals(Constant.CHART_GROUP_STATUS))	{
						
						if(colType.equals(Constant.SUM_CREATE_CNT)) {
							dataset.setValue(preXvalue,create_cnt);	
						}else if(colType.equals(Constant.SUM_READ_CNT)) {
							dataset.setValue(preXvalue,read_cnt);
						}else if(colType.equals(Constant.SUM_UPDATE_CNT)) {
							dataset.setValue(preXvalue,update_cnt);
						}else if(colType.equals(Constant.SUM_DELETE_CNT)) {
							dataset.setValue(preXvalue,delete_cnt);
						}						
												
						create_cnt = 0;
						read_cnt = 0;
						update_cnt = 0;
						delete_cnt = 0;
					}
	
				}else {
					
					// 기간별 등록현황
					 if(statisticsType.equals(Constant.CHART_DECADE_STATUS))	{		
						 if(colType.equals(member)) {
							 dataset.setValue(memberFields.get("order_str").toString(),Integer.parseInt(memberFields.get(member).toString()));
						 }
					 }else if(statisticsType.equals(Constant.CHART_FOLDER_STATUS))	{
						 if(colType.equals(member)) {
							 // group_nm : 구분컬럼(개인함/부서함/프로젝트함)
							 dataset.setValue(memberFields.get("group_nm").toString(),Integer.parseInt(memberFields.get(member).toString()));
						 }
					 }
					
				}
						
				// Y좌표값 합산처리 :: 기간별 문서현황 || 문서함/폴더별 보유현황 제외처리
				if(!(statisticsType.equals(Constant.CHART_DECADE_STATUS) 
						|| statisticsType.equals(Constant.CHART_FOLDER_STATUS)))	{	
					
					// 문서유형별 보유 현황 || 보안등급별 보유현황
					if(member.equals(Constant.SUM_DOC_CNT))	{
						doc_cnt += Integer.parseInt(memberFields.get(member).toString());
					}else if(member.equals(Constant.SUM_FILE_CNT))	{
						page_cnt += Integer.parseInt(memberFields.get(member).toString());
					}else if(member.equals(Constant.SUM_PAGE_TOTAL))	{
						page_total += Long.parseLong(memberFields.get(member).toString());
					}
					
					// 부서별 등록/활용 현황
					else if(member.equals(Constant.SUM_CREATE_CNT))	{
						create_cnt += Long.parseLong(memberFields.get(member).toString());
					}else if(member.equals(Constant.SUM_READ_CNT))	{
						read_cnt += Long.parseLong(memberFields.get(member).toString());
					}else if(member.equals(Constant.SUM_UPDATE_CNT))	{
						update_cnt += Long.parseLong(memberFields.get(member).toString());
					}else if(member.equals(Constant.SUM_DELETE_CNT))	{
						delete_cnt += Long.parseLong(memberFields.get(member).toString());
					}
									
					// member 값이 X 좌표인 경우 비교를 위해 임시저장 처리
					if(member.equals(groupField))	{			
						preXvalue = memberFields.get(member).toString();
					}					
				}
			}
									
		}	// END OF LIST
				
		// Y좌표값 합산처리 :: 기간별 문서현황 || 문서함/폴더별 보유현황 제외처리
		if(!(statisticsType.equals(Constant.CHART_DECADE_STATUS) 
				|| statisticsType.equals(Constant.CHART_FOLDER_STATUS)))	{	
			
			// 문서유형별 보유 현황 || 보안등급별 보유현황
			if(statisticsType.equals(Constant.CHART_DOC_TYPE) || statisticsType.equals(Constant.CHART_SECURITY_STATUS) )	{		// 문서유형
				
				if(colType.equals(Constant.SUM_DOC_CNT)) {
					dataset.setValue(preXvalue,doc_cnt);	
				}else if(colType.equals(Constant.SUM_FILE_CNT)) {
					dataset.setValue(preXvalue,page_cnt);	
				}else if(colType.equals(Constant.SUM_PAGE_TOTAL)) {
					dataset.setValue(preXvalue,page_total);	
				}						
			}
						
			// 부서별 등록/활용 현황
			else if(statisticsType.equals(Constant.CHART_GROUP_STATUS))	{
				
				if(colType.equals(Constant.SUM_CREATE_CNT)) {
					dataset.setValue(preXvalue,create_cnt);	
				}else if(colType.equals(Constant.SUM_READ_CNT)) {
					dataset.setValue(preXvalue,read_cnt);
				}else if(colType.equals(Constant.SUM_UPDATE_CNT)) {
					dataset.setValue(preXvalue,update_cnt);
				}else if(colType.equals(Constant.SUM_DELETE_CNT)) {
					dataset.setValue(preXvalue,delete_cnt);
				}						
			}
			
		}
		return dataset;
	}
	
	
	public static List<HashMap<String,Object>>getChartDataSet(HashMap<String,Object> model) throws Exception {
		
		List<HashMap<String,Object>> retMap = new ArrayList<HashMap<String,Object>>();
		
		@SuppressWarnings("unchecked")
		List<? extends VO> list = (ArrayList<VO>)model.get("list");
		
		String[] members = (String [])model.get("members");

		String preXvalue = "";
		String groupField = model.get("groupField").toString();
		
		// 구분항목 && Y좌표 && 통계항목명
		String statisticsType = model.get("statisticsType").toString();
		String colType = model.get("colType").toString();
		String chartType = model.get("chartType").toString();			
		String yTitle = model.get("yTitle").toString();
		
		// 문서유형
		int doc_cnt = 0;
		int page_cnt = 0;
		long page_total = 0;
		
		// 부서별 등록활용 현황
		int create_cnt = 0;
		int read_cnt = 0;
		int update_cnt = 0;
		int delete_cnt = 0;
		
		int tempCnt = 0;
		
		// 열 개수 만큼 반복하며 멤버들의 값을 셀에 삽입한다.				
		for (VO vo : list) {

				// VO 객체의 멤버변수들을 얻는다.
				HashMap<String, Object> memberFields = CommonUtil.getMemberFields(vo);
						
				// 열 개수 만큼 반복하며 멤버들의 값을 셀에 삽입한다.				
				for (String member : members) {
										
					HashMap<String,Object> dataSet = new HashMap<String,Object>();
					
					// NEXT X좌표인 경우 
					if( member.equals(groupField) && !preXvalue.equals("") &&
							!memberFields.get(member).toString().equals(preXvalue) 
							&& !statisticsType.equals(Constant.CHART_DECADE_STATUS)			// 기간별 문서현황 집계처리 제외
							&& !statisticsType.equals(Constant.CHART_FOLDER_STATUS)			// 문서함/폴더별 보유현황 집계처리 제외
							)	{
						
						// 문서유형별 보유 현황 || 보안등급별 보유 현황
						if(statisticsType.equals(Constant.CHART_DOC_TYPE) || statisticsType.equals(Constant.CHART_SECURITY_STATUS))	{
							
							if(colType.equals(Constant.SUM_DOC_CNT)) {
								
								//dataset.setValue(doc_cnt, yTitle, preXvalue);
								dataSet.put("label",memberFields.get("type_name").toString());
								dataSet.put("value",doc_cnt);			
								 
								// PIE차트인 경우 색상정보를 추가한다.
								 if(chartType.equals(Constant.PIE_CHART))	{
									 String color = getRandomColor();
									 dataSet.put("color",color);
									 dataSet.put("highlight",color);									 
								 }
								 
								 retMap.add(dataSet);
								 
							}else if(colType.equals(Constant.SUM_FILE_CNT)) {
								
								dataSet.put("label",memberFields.get("type_name").toString());
								dataSet.put("value",page_cnt);
								 
								// PIE차트인 경우 색상정보를 추가한다.
								 if(chartType.equals(Constant.PIE_CHART))	{
									 String color = getRandomColor();
									 dataSet.put("color",color);
									 dataSet.put("highlight",color);							 
								 }
								 
								 retMap.add(dataSet);
								 
								//dataset.setValue(page_cnt,yTitle, preXvalue);
							}else if(colType.equals(Constant.SUM_PAGE_TOTAL)) {
								
								dataSet.put("label",memberFields.get("type_name").toString());
								dataSet.put("value",page_total);
								
								// PIE차트인 경우 색상정보를 추가한다.
								 if(chartType.equals(Constant.PIE_CHART))	{
									 String color = getRandomColor();
									 dataSet.put("color",color);
									 dataSet.put("highlight",color);							 
								 }
								 
								 retMap.add(dataSet);
								 
								//dataset.setValue(page_total,yTitle, preXvalue);
							}						
							
							doc_cnt = 0;
							page_cnt = 0;
							page_total = 0;
						}		
						
						// 부서별 등록/활용 현황 
						else if(statisticsType.equals(Constant.CHART_GROUP_STATUS))	{
							
							if(colType.equals(Constant.SUM_CREATE_CNT)) {
								//dataset.setValue(create_cnt, yTitle, preXvalue);
								dataSet.put("label",memberFields.get("group_nm").toString());
								dataSet.put("value",create_cnt);	
								
								// PIE차트인 경우 색상정보를 추가한다.
								 if(chartType.equals(Constant.PIE_CHART))	{
									 String color = getRandomColor();
									 dataSet.put("color",color);
									 dataSet.put("highlight",color);								 
								 }
								 
								 retMap.add(dataSet);
								 
							}else if(colType.equals(Constant.SUM_READ_CNT)) {
								//dataset.setValue(read_cnt,yTitle, preXvalue);
								dataSet.put("label",memberFields.get("group_nm").toString());
								dataSet.put("value",read_cnt);
								
								// PIE차트인 경우 색상정보를 추가한다.
								 if(chartType.equals(Constant.PIE_CHART))	{
									 String color = getRandomColor();
									 dataSet.put("color",color);
									 dataSet.put("highlight",color);								 
								 }
								 
								 retMap.add(dataSet);
								 
							}else if(colType.equals(Constant.SUM_UPDATE_CNT)) {
								//dataset.setValue(update_cnt,yTitle, preXvalue);
								dataSet.put("label",memberFields.get("group_nm").toString());
								dataSet.put("value",update_cnt);
								
								// PIE차트인 경우 색상정보를 추가한다.
								 if(chartType.equals(Constant.PIE_CHART))	{
									 String color = getRandomColor();
									 dataSet.put("color",color);
									 dataSet.put("highlight",color);							 
								 }
								 
								 retMap.add(dataSet);
								 
							}else if(colType.equals(Constant.SUM_DELETE_CNT)) {
								//dataset.setValue(delete_cnt,yTitle, preXvalue);
								dataSet.put("label",memberFields.get("group_nm").toString());
								dataSet.put("value",delete_cnt);
								
								// PIE차트인 경우 색상정보를 추가한다.
								 if(chartType.equals(Constant.PIE_CHART))	{
									 String color = getRandomColor();
									 dataSet.put("color",color);
									 dataSet.put("highlight",color);								 
								 }
								 
								 retMap.add(dataSet);
							}						
							
							
							create_cnt = 0;
							read_cnt = 0;
							update_cnt = 0;
							delete_cnt = 0;
						}
						
					}else {
						
						// 기간별 등록현황
						 if(statisticsType.equals(Constant.CHART_DECADE_STATUS))	{		
							 if(colType.equals(member)) {		
								 
								 //dataset.setValue(Integer.parseInt(memberFields.get(member).toString()), yTitle, memberFields.get("order_str").toString());
								 
								 dataSet.put("label",memberFields.get("order_str").toString());
								 dataSet.put("value",Integer.parseInt(memberFields.get(member).toString()));
								 
								 // PIE차트인 경우 색상정보를 추가한다.
								 if(chartType.equals(Constant.PIE_CHART))	{
									 String color = getRandomColor();
									 dataSet.put("color",color);
									 dataSet.put("highlight",color);							 
								 }
								 
								 retMap.add(dataSet);
							 }
						 }
						 
						 // 문서함/폴더별 보유현황	
						 else if(statisticsType.equals(Constant.CHART_FOLDER_STATUS))	{    	 						 
							 if(colType.equals(member)) {
								 //dataset.setValue(Integer.parseInt(memberFields.get(member).toString()), yTitle, memberFields.get("group_nm").toString());		// group_nm : 구분컬럼(개인함/부서함/프로젝트함)
								 dataSet.put("label",memberFields.get("group_nm").toString());
								 dataSet.put("value",Integer.parseInt(memberFields.get(member).toString()));
								 
								 // PIE차트인 경우 색상정보를 추가한다.
								 if(chartType.equals(Constant.PIE_CHART))	{
									 String color = getRandomColor();
									 dataSet.put("color",color);
									 dataSet.put("highlight",color);							 
								 }
								 
								 retMap.add(dataSet);
							 }
						 }
						
						
					}
							
					// Y좌표값 합산처리 :: 기간별 문서현황 || 문서함/폴더별 보유현황 제외처리
					
					if(!(statisticsType.equals(Constant.CHART_DECADE_STATUS) 
							|| statisticsType.equals(Constant.CHART_FOLDER_STATUS)))	{	
					
						// 문서유형별 보유 현황 || 보안등급별 보유 현황 
						if(member.equals(Constant.SUM_DOC_CNT))	{
							doc_cnt += Integer.parseInt(memberFields.get(member).toString());
						}else if(member.equals(Constant.SUM_FILE_CNT))	{
							page_cnt += Integer.parseInt(memberFields.get(member).toString());
						}else if(member.equals(Constant.SUM_PAGE_TOTAL))	{
							page_total += Long.parseLong(memberFields.get(member).toString());
						}
						
				
						// 부서별 등록/활용 현황
						else if(member.equals(Constant.SUM_CREATE_CNT))	{
							create_cnt += Long.parseLong(memberFields.get(member).toString());
						}else if(member.equals(Constant.SUM_READ_CNT))	{
							read_cnt += Long.parseLong(memberFields.get(member).toString());
						}else if(member.equals(Constant.SUM_UPDATE_CNT))	{
							update_cnt += Long.parseLong(memberFields.get(member).toString());
						}else if(member.equals(Constant.SUM_DELETE_CNT))	{
							delete_cnt += Long.parseLong(memberFields.get(member).toString());
						}
		
						// member 값이 X 좌표인 경우 비교를 위해 임시저장 처리
						if(member.equals(groupField) 
								&& !statisticsType.equals(Constant.CHART_DECADE_STATUS) )	{			
							preXvalue = memberFields.get(member).toString();
						}

					}
	
					
					
				}
											
		}	// END OF LIST
		
		// Y좌표값 합산처리 :: 기간별 문서현황 || 문서함/폴더별 보유현황 제외처리
	
		if(!(statisticsType.equals(Constant.CHART_DECADE_STATUS) 
				|| statisticsType.equals(Constant.CHART_FOLDER_STATUS)))	{	
				
			HashMap<String,Object> dataSet = new HashMap<String,Object>();
			
			// 문서유형별 보유 현황 || 보안등급별 보유 현황
			if(statisticsType.equals(Constant.CHART_DOC_TYPE) || statisticsType.equals(Constant.CHART_SECURITY_STATUS) )	{
				
				if(colType.equals(Constant.SUM_DOC_CNT)) {
					//dataset.setValue(doc_cnt, yTitle, preXvalue);
					dataSet.put("label",preXvalue);
					dataSet.put("value",doc_cnt);			
					 
					// PIE차트인 경우 색상정보를 추가한다.
					 if(chartType.equals(Constant.PIE_CHART))	{
						 String color = getRandomColor();
						 dataSet.put("color",color);
						 dataSet.put("highlight",color);								 
					 }
					 
					 retMap.add(dataSet);
					 
				}else if(colType.equals(Constant.SUM_FILE_CNT)) {
					//dataset.setValue(page_cnt,yTitle, preXvalue);
					dataSet.put("label",preXvalue);
					dataSet.put("value",page_cnt);			
					 
					// PIE차트인 경우 색상정보를 추가한다.
					 if(chartType.equals(Constant.PIE_CHART))	{
						 String color = getRandomColor();
						 dataSet.put("color",color);
						 dataSet.put("highlight",color);									 
					 }
					 
					 retMap.add(dataSet);
					 
				}else if(colType.equals(Constant.SUM_PAGE_TOTAL)) {
					
					//dataset.setValue(page_total,yTitle, preXvalue);
					dataSet.put("label",preXvalue);
					dataSet.put("value",page_total);			
					 
					// PIE차트인 경우 색상정보를 추가한다.
					 if(chartType.equals(Constant.PIE_CHART))	{
						 String color = getRandomColor();
						 dataSet.put("color",color);
						 dataSet.put("highlight",color);								 
					 }
					 
					 retMap.add(dataSet);
				}						
			}
			
			// 부서별 등록/활용 현황 
			else if(statisticsType.equals(Constant.CHART_GROUP_STATUS))	{
				
				if(colType.equals(Constant.SUM_CREATE_CNT)) {
					
					//dataset.setValue(create_cnt, yTitle, preXvalue);
					dataSet.put("label",preXvalue);
					dataSet.put("value",create_cnt);			
					 
					// PIE차트인 경우 색상정보를 추가한다.
					 if(chartType.equals(Constant.PIE_CHART))	{
						 String color = getRandomColor();
						 dataSet.put("color",color);
						 dataSet.put("highlight",color);								 
					 }
					 
					 retMap.add(dataSet);
					 
				}else if(colType.equals(Constant.SUM_READ_CNT)) {
					
					//dataset.setValue(read_cnt,yTitle, preXvalue);					
					dataSet.put("label",preXvalue);
					dataSet.put("value",read_cnt);			
					 
					// PIE차트인 경우 색상정보를 추가한다.
					 if(chartType.equals(Constant.PIE_CHART))	{
						 String color = getRandomColor();
						 dataSet.put("color",color);
						 dataSet.put("highlight",color);								 
					 }
					 
					 retMap.add(dataSet);					
					
				}else if(colType.equals(Constant.SUM_UPDATE_CNT)) {
					
					//dataset.setValue(update_cnt,yTitle, preXvalue);
					dataSet.put("label",preXvalue);
					dataSet.put("value",update_cnt);			
					 
					// PIE차트인 경우 색상정보를 추가한다.
					 if(chartType.equals(Constant.PIE_CHART))	{
						 String color = getRandomColor();
						 dataSet.put("color",color);
						 dataSet.put("highlight",color);								 
					 }
					 
					 retMap.add(dataSet);
					 
				}else if(colType.equals(Constant.SUM_DELETE_CNT)) {
					
					//dataset.setValue(delete_cnt,yTitle, preXvalue);
					dataSet.put("label",preXvalue);
					dataSet.put("value",delete_cnt);			
					 
					// PIE차트인 경우 색상정보를 추가한다.
					 if(chartType.equals(Constant.PIE_CHART))	{
						 String color = getRandomColor();
						 dataSet.put("color",color);
						 dataSet.put("highlight",color);								 
					 }
					 
					 retMap.add(dataSet);
					
				}						
			}
			
		}

		return retMap;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : Chart 데이터 설정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getDataSet
	 * @param model
	 * @return DefaultCategoryDataset
	 */
	public static DefaultCategoryDataset getCategoryDataSet(HashMap<String,Object> model) throws Exception {
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		@SuppressWarnings("unchecked")
		List<? extends VO> list = (ArrayList<VO>)model.get("list");

		String[] members = (String [])model.get("members");

		String preXvalue = "";
		String groupField = model.get("groupField").toString();
		
		// 구분항목 && Y좌표 && 통계항목명
		String statisticsType = model.get("statisticsType").toString();
		String colType = model.get("colType").toString();			
		String yTitle = model.get("yTitle").toString();
		
		// 문서유형
		int doc_cnt = 0;
		int page_cnt = 0;
		long page_total = 0;
		
		// 부서별 등록활용 현황
		int create_cnt = 0;
		int read_cnt = 0;
		int update_cnt = 0;
		int delete_cnt = 0;

		// 열 개수 만큼 반복하며 멤버들의 값을 셀에 삽입한다.				
		for (VO vo : list) {

			// VO 객체의 멤버변수들을 얻는다.
			HashMap<String, Object> memberFields = CommonUtil.getMemberFields(vo);
					
			// 열 개수 만큼 반복하며 멤버들의 값을 셀에 삽입한다.				
			for (String member : members) {

				// NEXT X좌표인 경우 
				if( member.equals(groupField) && !preXvalue.equals("") &&
						!memberFields.get(member).toString().equals(preXvalue) 
						&& !statisticsType.equals(Constant.CHART_DECADE_STATUS)			// 기간별 문서현황 집계처리 제외
						&& !statisticsType.equals(Constant.CHART_FOLDER_STATUS)			// 문서함/폴더별 보유현황 집계처리 제외
						)	{

					// 문서유형별 보유 현황 || 보안등급별 보유 현황
					if(statisticsType.equals(Constant.CHART_DOC_TYPE) || statisticsType.equals(Constant.CHART_SECURITY_STATUS))	{
						
						if(colType.equals(Constant.SUM_DOC_CNT)) {
							dataset.setValue(doc_cnt, yTitle, preXvalue);
						}else if(colType.equals(Constant.SUM_FILE_CNT)) {
							dataset.setValue(page_cnt,yTitle, preXvalue);
						}else if(colType.equals(Constant.SUM_PAGE_TOTAL)) {
							dataset.setValue(page_total,yTitle, preXvalue);
						}						
						
						doc_cnt = 0;
						page_cnt = 0;
						page_total = 0;
					}
					// 부서별 등록/활용 현황
					else if(statisticsType.equals(Constant.CHART_GROUP_STATUS))	{
						
						if(colType.equals(Constant.SUM_CREATE_CNT)) {
							dataset.setValue(create_cnt, yTitle, preXvalue);
						}else if(colType.equals(Constant.SUM_READ_CNT)) {
							dataset.setValue(read_cnt,yTitle, preXvalue);
						}else if(colType.equals(Constant.SUM_UPDATE_CNT)) {
							dataset.setValue(update_cnt,yTitle, preXvalue);
						}else if(colType.equals(Constant.SUM_DELETE_CNT)) {
							dataset.setValue(delete_cnt,yTitle, preXvalue);
						}						
						
						
						create_cnt = 0;
						read_cnt = 0;
						update_cnt = 0;
						delete_cnt = 0;
					}
					
				}else {
					
					// 기간별 등록현황
					 if(statisticsType.equals(Constant.CHART_DECADE_STATUS))	{		
						 if(colType.equals(member)) {
							 dataset.setValue(Integer.parseInt(memberFields.get(member).toString()), yTitle, memberFields.get("order_str").toString());
						 }
					 }else if(statisticsType.equals(Constant.CHART_FOLDER_STATUS))	{						 						 
						 if(colType.equals(member)) {
							 dataset.setValue(Integer.parseInt(memberFields.get(member).toString()), yTitle, memberFields.get("group_nm").toString());		// group_nm : 구분컬럼(개인함/부서함/프로젝트함)
						 }
					 }
					
					
				}
						
				// Y좌표값 합산처리 :: 기간별 문서현황 || 문서함/폴더별 보유현황 제외처리
				if(!(statisticsType.equals(Constant.CHART_DECADE_STATUS) 
						|| statisticsType.equals(Constant.CHART_FOLDER_STATUS)))	{	
				
					// 문서유형별 보유 현황 || 보안등급별 보유 현황 
					if(member.equals(Constant.SUM_DOC_CNT))	{
						doc_cnt += Integer.parseInt(memberFields.get(member).toString());
					}else if(member.equals(Constant.SUM_FILE_CNT))	{
						page_cnt += Integer.parseInt(memberFields.get(member).toString());
					}else if(member.equals(Constant.SUM_PAGE_TOTAL))	{
						page_total += Long.parseLong(memberFields.get(member).toString());
					}
					
					// 부서별 등록/활용 현황
					else if(member.equals(Constant.SUM_CREATE_CNT))	{
						create_cnt += Long.parseLong(memberFields.get(member).toString());
					}else if(member.equals(Constant.SUM_READ_CNT))	{
						read_cnt += Long.parseLong(memberFields.get(member).toString());
					}else if(member.equals(Constant.SUM_UPDATE_CNT))	{
						update_cnt += Long.parseLong(memberFields.get(member).toString());
					}else if(member.equals(Constant.SUM_DELETE_CNT))	{
						delete_cnt += Long.parseLong(memberFields.get(member).toString());
					}
					
	
					// member 값이 X 좌표인 경우 비교를 위해 임시저장 처리
					if(member.equals(groupField) 
							&& !statisticsType.equals(Constant.CHART_DECADE_STATUS) )	{			
						preXvalue = memberFields.get(member).toString();
					}

				}
			}
									
		}	// END OF LIST
		
		// Y좌표값 합산처리 :: 기간별 문서현황 || 문서함/폴더별 보유현황 제외처리
		if(!(statisticsType.equals(Constant.CHART_DECADE_STATUS) 
				|| statisticsType.equals(Constant.CHART_FOLDER_STATUS)))	{	
				
			// 문서유형별 보유 현황 || 보안등급별 보유 현황
			if(statisticsType.equals(Constant.CHART_DOC_TYPE) || statisticsType.equals(Constant.CHART_SECURITY_STATUS) )	{
				
				if(colType.equals(Constant.SUM_DOC_CNT)) {
					dataset.setValue(doc_cnt, yTitle, preXvalue);
				}else if(colType.equals(Constant.SUM_FILE_CNT)) {
					dataset.setValue(page_cnt,yTitle, preXvalue);
				}else if(colType.equals(Constant.SUM_PAGE_TOTAL)) {
					dataset.setValue(page_total,yTitle, preXvalue);
				}						
			}
			
			// 부서별 등록/활용 현황
			else if(statisticsType.equals(Constant.CHART_GROUP_STATUS))	{
				
				if(colType.equals(Constant.SUM_CREATE_CNT)) {
					dataset.setValue(create_cnt, yTitle, preXvalue);
				}else if(colType.equals(Constant.SUM_READ_CNT)) {
					dataset.setValue(read_cnt,yTitle, preXvalue);
				}else if(colType.equals(Constant.SUM_UPDATE_CNT)) {
					dataset.setValue(update_cnt,yTitle, preXvalue);
				}else if(colType.equals(Constant.SUM_DELETE_CNT)) {
					dataset.setValue(delete_cnt,yTitle, preXvalue);
				}						
			}
			
		}

		return dataset;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 랜덤색상값 구하기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getRandomColor
	 * @return String
	 */
	public static String getRandomColor() {
		
        String[] letters = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String color = "#";
        for (int i = 0; i < 6; i++ ) {
           color += letters[(int) Math.round(Math.random() * 15)];
        }
        
        return color;
	}	

	
}
