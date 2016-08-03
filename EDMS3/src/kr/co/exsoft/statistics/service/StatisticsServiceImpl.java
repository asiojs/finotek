package kr.co.exsoft.statistics.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;

import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.library.ExsoftAbstractServiceImpl;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.PagingAjaxUtil;
import kr.co.exsoft.folder.service.FolderService;
import kr.co.exsoft.statistics.dao.StatisticsDao;
import kr.co.exsoft.statistics.vo.DocumentDecadeVO;
import kr.co.exsoft.statistics.vo.DocumentGroupHtVO;
import kr.co.exsoft.statistics.vo.DocumentStatusVO;
import kr.co.exsoft.statistics.vo.DocumentUserHtVO;
import kr.co.exsoft.user.vo.ConnectLogVO;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;



/**
 * Statistics 서비스 구현 부분
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Service("statisticsService")
public class StatisticsServiceImpl extends ExsoftAbstractServiceImpl implements StatisticsService {

	@Autowired
	@Qualifier("sqlSession")
	private SqlSession sqlSession;
	
	@Autowired
	private FolderService folderService;
	
	@Override
	public Map<String, Object> userDocStatisticsList(HashMap<String,Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<DocumentUserHtVO> ret = new ArrayList<DocumentUserHtVO>();
		int total = 0;
		
		StatisticsDao statisticsDao = sqlSession.getMapper(StatisticsDao.class);
		
		total = statisticsDao.userDocStatisticsCnt(map);
		ret = statisticsDao.userDocStatisticsList(map);

		resultMap.put("page",map.get("nPage").toString());
		resultMap.put("records",total);
		resultMap.put("total",CommonUtil.getTotPageSize(total,Integer.parseInt(map.get("page_size").toString())));	
		resultMap.put("list",ret);		
		
		// Ajax Paging 
		String strLink = "javascript:exsoftStatisticsFunc.event.gridPage";
		String contextRoot = map.get("contextRoot") != null ? map.get("contextRoot").toString() : "";
		PagingAjaxUtil pagingInfo = new PagingAjaxUtil(Integer.parseInt(map.get("nPage").toString()),total,Integer.parseInt(map.get("page_size").toString()),10,strLink,contextRoot);		
		resultMap.put("pagingInfo",pagingInfo);		

		return resultMap;	
	}
	
	@Override
	public Map<String, Object> groupDocStatisticsList(HashMap<String,Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<DocumentGroupHtVO> ret = new ArrayList<DocumentGroupHtVO>();
		int total = 0;
		
		StatisticsDao statisticsDao = sqlSession.getMapper(StatisticsDao.class);
		
		total = statisticsDao.groupDocStatisticsCnt(map);
		ret = statisticsDao.groupDocStatisticsList(map);

		resultMap.put("page",map.get("nPage").toString());
		resultMap.put("records",total);
		resultMap.put("total",CommonUtil.getTotPageSize(total,Integer.parseInt(map.get("page_size").toString())));	
		resultMap.put("list",ret);		
		
		// Ajax Paging 
		String strLink = "javascript:exsoftStatisticsFunc.event.gridPage";
		String contextRoot = map.get("contextRoot") != null ? map.get("contextRoot").toString() : "";
		PagingAjaxUtil pagingInfo = new PagingAjaxUtil(Integer.parseInt(map.get("nPage").toString()),total,Integer.parseInt(map.get("page_size").toString()),10,strLink,contextRoot);		
		resultMap.put("pagingInfo",pagingInfo);		

		return resultMap;	
	}
	
	@Override
	public Map<String, Object> decadeUserDocStatisticsList(HashMap<String,Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<DocumentDecadeVO> ret = new ArrayList<DocumentDecadeVO>();
		
		StatisticsDao statisticsDao = sqlSession.getMapper(StatisticsDao.class);

		ret = statisticsDao.decadeUserDocStatisticsList(map);

		resultMap.put("records",ret.size());
		resultMap.put("list",ret);		

		return resultMap;	
	}
	
	@Override
	public Map<String, Object> decadeGroupDocStatisticsList(HashMap<String,Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<DocumentDecadeVO> ret = new ArrayList<DocumentDecadeVO>();
		
		StatisticsDao statisticsDao = sqlSession.getMapper(StatisticsDao.class);

		ret = statisticsDao.decadeGroupDocStatisticsList(map);

		resultMap.put("records",ret.size());
		resultMap.put("list",ret);		

		return resultMap;	
	}
	
	@Override
	public Map<String, Object> userFolderStatisticsList(HashMap<String,Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<DocumentStatusVO> ret = new ArrayList<DocumentStatusVO>();
		int total = 0;
		
		StatisticsDao statisticsDao = sqlSession.getMapper(StatisticsDao.class);
		
		total = statisticsDao.userFoldertatisticsCnt(map);
		ret = statisticsDao.userFolderStatisticsList(map);

		resultMap.put("page",map.get("nPage").toString());
		resultMap.put("records",total);
		resultMap.put("total",CommonUtil.getTotPageSize(total,Integer.parseInt(map.get("page_size").toString())));	
		resultMap.put("list",ret);		
		
		// Ajax Paging 
		String strLink = "javascript:exsoftStatisticsFunc.event.gridPage";
		String contextRoot = map.get("contextRoot") != null ? map.get("contextRoot").toString() : "";
		PagingAjaxUtil pagingInfo = new PagingAjaxUtil(Integer.parseInt(map.get("nPage").toString()),total,Integer.parseInt(map.get("page_size").toString()),10,strLink,contextRoot);		
		resultMap.put("pagingInfo",pagingInfo);		

		return resultMap;	
		
	}
	
	@Override
	public Map<String, Object> folderStatisticsList(HashMap<String,Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<DocumentStatusVO> statusList = new ArrayList<DocumentStatusVO>();
		List<CaseInsensitiveMap> folderList = new ArrayList<CaseInsensitiveMap>();
		List<DocumentStatusVO> ret = new ArrayList<DocumentStatusVO>();
		List<String> subFolders = new ArrayList<String>();
				
		int total = 0;
		
		StatisticsDao statisticsDao = sqlSession.getMapper(StatisticsDao.class);
				
		
		// 1.1 프로젝트 그룹인 경우 1 DEPTH
		if(map.get("strFolderIdx").toString().equals(Constant.MAP_ID_PROJECT))	{			
			map.put("project_root_id",statisticsDao.statisticsProjectRootId(map));
		}
		
		// 1.2 문서함/폴더별 보유현황
		statusList = statisticsDao.userFolderStatisticsList(map);

		// 2. 기준 폴더 리스트 :: 화면 정렬 및 리스트 수 기준
		total = statisticsDao.statisticsFolderListCnt(map);
		folderList = statisticsDao.statisticsFolderList(map);
		
		if(folderList != null && folderList.size() > 0)	{
			
			for(CaseInsensitiveMap caseMap : folderList)			{
				
				int page_cnt = 0;
				long page_total = 0;
				int doc_cnt = 0;
				
				String folder_id = caseMap.get("folder_id").toString();
				subFolders = new ArrayList<String>(new HashSet<String>(folderService.childFolderIdsByfolderId(folder_id, null)));			
									
				// 개인함/프로젝트폴더/부서함내에 문서가 없는 경우도 리스트에 보이게 처리
				if(statusList != null && statusList.size() > 0)	{
					for(DocumentStatusVO documentStatusVO : statusList) {
	
						String subFolder = documentStatusVO.getFolder_id();
						
						if(subFolders != null && subFolders.size() > 0) {
							
							for(String subFolderInfo : subFolders)	{
								
								// 기준 폴더 하위에 포함이 된 경우
								if(subFolder.equals(subFolderInfo)) {							
									page_cnt += documentStatusVO.getPage_cnt();
									page_total += documentStatusVO.getPage_total();								
									doc_cnt += documentStatusVO.getDoc_cnt();
								}
								
							}
						}						
					}
				}
				
				// return value add
				DocumentStatusVO docStVO = new DocumentStatusVO();
				docStVO.setGroup_nm(caseMap.get("folder_nm").toString());
				docStVO.setDoc_cnt(doc_cnt);
				docStVO.setPage_cnt(page_cnt);			
				docStVO.setPage_total(page_total);

				ret.add(docStVO);
				
			}	// END OF FOR folderList
		}	// END OF IF folderList

		resultMap.put("page",map.get("nPage").toString());
		resultMap.put("records",total);
		resultMap.put("total",CommonUtil.getTotPageSize(total,Integer.parseInt(map.get("page_size").toString())));	
		resultMap.put("list",ret);		
		
		// Ajax Paging 
		String strLink = "javascript:exsoftStatisticsFunc.event.gridPage";
		String contextRoot = map.get("contextRoot") != null ? map.get("contextRoot").toString() : "";
		PagingAjaxUtil pagingInfo = new PagingAjaxUtil(Integer.parseInt(map.get("nPage").toString()),total,Integer.parseInt(map.get("page_size").toString()),10,strLink,contextRoot);		
		resultMap.put("pagingInfo",pagingInfo);		

		return resultMap;	
		
	}
	
	@Override
	public Map<String, Object> statisticsTypeList(HashMap<String,Object> map) throws Exception {
		
		StatisticsDao statisticsDao = sqlSession.getMapper(StatisticsDao.class);
	
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<DocumentStatusVO> ret = new ArrayList<DocumentStatusVO>();
		
		ret = statisticsDao.statisticsTypeList(map);
		
		resultMap.put("records",ret.size());
		resultMap.put("list",ret);
		
		return resultMap;		
	}
	
	@Override
	public Map<String, Object> statisticsSecurityList(HashMap<String,Object> map) throws Exception{
		
		StatisticsDao statisticsDao = sqlSession.getMapper(StatisticsDao.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<DocumentStatusVO> ret = new ArrayList<DocumentStatusVO>();
		int total = 0;

		total = statisticsDao.statisticsSecurityCnt(map);
		ret = statisticsDao.statisticsSecurityList(map);

		resultMap.put("page",map.get("nPage").toString());
		resultMap.put("records",total);
		resultMap.put("total",CommonUtil.getTotPageSize(total,Integer.parseInt(map.get("page_size").toString())));	
		resultMap.put("list",ret);		

		// Ajax Paging 
		String strLink = "javascript:exsoftStatisticsFunc.event.gridPage";
		String contextRoot = map.get("contextRoot") != null ? map.get("contextRoot").toString() : "";
		PagingAjaxUtil pagingInfo = new PagingAjaxUtil(Integer.parseInt(map.get("nPage").toString()),total,Integer.parseInt(map.get("page_size").toString()),10,strLink,contextRoot);		
		resultMap.put("pagingInfo",pagingInfo);		
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> statisticsQuotaList(HashMap<String,Object> map) throws Exception {
		
		StatisticsDao statisticsDao = sqlSession.getMapper(StatisticsDao.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<CaseInsensitiveMap> gridList = new ArrayList<CaseInsensitiveMap>();
		List<DocumentStatusVO> quotaList = new ArrayList<DocumentStatusVO>();
		List<DocumentStatusVO> ret = new ArrayList<DocumentStatusVO>();
		List<String> subFolders = new ArrayList<String>();
		
		int total = 0;
		
		// 1. 개인/부서별 현 사용량 리스트
		//
		quotaList =statisticsDao.statisticsQuotaList(map);		
		
		// 2. 기준(개인/부서) 목록 리스트
		total = statisticsDao.statisticsQuotaPageCnt(map);
		gridList = statisticsDao.statisticsQuotaPageList(map);
		
		// 해당 조건이 만족된 경우에만 통계 데이터를 화면에 출력할 수 있다.
		if(gridList != null && gridList.size() > 0 && quotaList != null && quotaList.size() > 0)	{
			
			// PART_ID,PART_NM,STORAGE_QUOTA
			for(CaseInsensitiveMap caseMap : gridList)	{
				
				long page_total = 0;		// 현재 사용량
				
				String folder_id = caseMap.get("part_id").toString();			// 사용자ID나 부서ID			
				subFolders = new ArrayList<String>(new HashSet<String>(folderService.childFolderIdsByfolderId(folder_id, null)));
				
				for(DocumentStatusVO documentStatusVO : quotaList) {
					
					String subFolder = documentStatusVO.getFolder_id();

					
					if(subFolders != null && subFolders.size() > 0) {
						
						for(String subFolderInfo : subFolders)	{							
							// 기준 폴더 하위에 포함이 된 경우
							if(subFolder.equals(subFolderInfo)) {	
								page_total += documentStatusVO.getPage_total();		
							}							
						}
						
					}
					
				}	// END OF FOR quotaList

				// return value add
				DocumentStatusVO docStVO = new DocumentStatusVO();
				docStVO.setPart_id(folder_id);
				docStVO.setPart_nm(caseMap.get("part_nm").toString());
				docStVO.setStorage_quota(Long.parseLong(caseMap.get("storage_quota").toString()));				
				docStVO.setPage_total(page_total);
				
				ret.add(docStVO);
				
			}	// END OF FOR gridList
				
		}		// END OF IF gridList
		
		resultMap.put("page",map.get("nPage").toString());
		resultMap.put("records",total);
		resultMap.put("total",CommonUtil.getTotPageSize(total,Integer.parseInt(map.get("page_size").toString())));	
		resultMap.put("list",ret);		
		
		// Ajax Paging 
		String strLink = "javascript:exsoftStatisticsFunc.event.gridPage";
		String contextRoot = map.get("contextRoot") != null ? map.get("contextRoot").toString() : "";
		PagingAjaxUtil pagingInfo = new PagingAjaxUtil(Integer.parseInt(map.get("nPage").toString()),total,Integer.parseInt(map.get("page_size").toString()),10,strLink,contextRoot);		
		resultMap.put("pagingInfo",pagingInfo);		
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> loginLogList(HashMap<String,Object> map) throws Exception {
		
		StatisticsDao statisticsDao = sqlSession.getMapper(StatisticsDao.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<ConnectLogVO> ret = new ArrayList<ConnectLogVO>();
		
		int total = 0;
		
		total = statisticsDao.loginLogPageCnt(map);
		ret = statisticsDao.loginLogPageList(map);
		
		resultMap.put("page",map.get("nPage").toString());
		resultMap.put("records",total);
		resultMap.put("total",CommonUtil.getTotPageSize(total,Integer.parseInt(map.get("page_size").toString())));	
		resultMap.put("list",ret);		
		
		// Ajax Paging 
		String strLink = "javascript:exsoftStatisticsFunc.event.gridPage";
		String contextRoot = map.get("contextRoot") != null ? map.get("contextRoot").toString() : "";
		PagingAjaxUtil pagingInfo = new PagingAjaxUtil(Integer.parseInt(map.get("nPage").toString()),total,Integer.parseInt(map.get("page_size").toString()),10,strLink,contextRoot);		
		resultMap.put("pagingInfo",pagingInfo);		
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> myStatisticsList(HashMap<String,Object> map) throws Exception {
		
		StatisticsDao statisticsDao = sqlSession.getMapper(StatisticsDao.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<DocumentUserHtVO> ret = new ArrayList<DocumentUserHtVO>();
		
		ret = statisticsDao.myStatisticsList(map);
		
		resultMap.put("records",ret.size());
		resultMap.put("list",ret);
		
		return resultMap;
	}
}
