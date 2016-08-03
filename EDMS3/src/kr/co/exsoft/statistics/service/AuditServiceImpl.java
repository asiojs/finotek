package kr.co.exsoft.statistics.service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import kr.co.exsoft.statistics.dao.AuditDao;

import org.apache.commons.collections.map.CaseInsensitiveMap;

import kr.co.exsoft.eframework.library.ExsoftAbstractServiceImpl;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.PagingAjaxUtil;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.quartz.vo.AuditTrailVO;

/**
 * 대량열람감사 서비스 구현 부분
 *
 * @author 패키지팀
 * @since 2014. 9. 15.
 * @version 1.0
 * 
 */
@Service("auditService")
public class AuditServiceImpl extends ExsoftAbstractServiceImpl implements AuditService {

	@Autowired
	@Qualifier("sqlSession")
	private SqlSession sqlSession;

	@Override
	public Map<String, Object> auditPageList(HashMap<String,Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<AuditTrailVO> ret = new ArrayList<AuditTrailVO>();
		int total = 0;
		
		AuditDao auditDao = sqlSession.getMapper(AuditDao.class);
		
		total = auditDao.auditPagingCount(map);
		ret = auditDao.auditPagingList(map);
				
		resultMap.put("page",map.get("nPage").toString());
		resultMap.put("records",total);
		resultMap.put("total",CommonUtil.getTotPageSize(total,Integer.parseInt(map.get("page_size").toString())));	
		resultMap.put("list",ret);
		
		// Ajax Paging 
		String strLink = "javascript:auditManager.event.gridPage";
		String contextRoot = map.get("contextRoot") != null ? map.get("contextRoot").toString() : "";
		PagingAjaxUtil pagingInfo = new PagingAjaxUtil(Integer.parseInt(map.get("nPage").toString()),total,Integer.parseInt(map.get("page_size").toString()),10,strLink,contextRoot);		
		resultMap.put("pagingInfo",pagingInfo);

		return resultMap;
	}
	
	@Override
	public Map<String, Object> auditDetailPageList(HashMap<String,Object> map) throws Exception {
	
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<CaseInsensitiveMap> ret = new ArrayList<CaseInsensitiveMap>();
		List<CaseInsensitiveMap> detailList = new ArrayList<CaseInsensitiveMap>();
		int total = 0;
		
		AuditDao auditDao = sqlSession.getMapper(AuditDao.class);
		
		total = auditDao.auditDetailCount(map);
		detailList = auditDao.auditDetailList(map);
		
		for(CaseInsensitiveMap caseMap : detailList)	{
			
			String page_size = StringUtil.fileSize(Long.parseLong(caseMap.get("page_size").toString()));
			caseMap.put("page_size", page_size);			
			ret.add(caseMap);
		}
		
		resultMap.put("page",map.get("nPage").toString());
		resultMap.put("records",total);
		resultMap.put("total",CommonUtil.getTotPageSize(total,Integer.parseInt(map.get("page_size").toString())));	
		resultMap.put("list",ret);
		
		// Ajax Paging 
		String strLink = "javascript:auditManager.event.gridPageForDetail";
		String contextRoot = map.get("contextRoot") != null ? map.get("contextRoot").toString() : "";
		PagingAjaxUtil pagingInfo = new PagingAjaxUtil(Integer.parseInt(map.get("nPage").toString()),total,Integer.parseInt(map.get("page_size").toString()),10,strLink,contextRoot);		
		resultMap.put("pagingInfo",pagingInfo);
		
		return resultMap;
	}
}
