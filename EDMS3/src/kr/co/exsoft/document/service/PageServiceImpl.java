package kr.co.exsoft.document.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.exsoft.document.dao.PageDao;
import kr.co.exsoft.document.vo.PageVO;
import kr.co.exsoft.eframework.library.ExsoftAbstractServiceImpl;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.PagingAjaxUtil;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Page 서비스 구현 부분
 *
 * @author 패키지팀
 * @since 2014. 9. 16.
 * @version 1.0
 * 
 */
@Service("pageService")
public class PageServiceImpl extends ExsoftAbstractServiceImpl implements PageService{
	
	@Autowired
	@Qualifier("sqlSession")
	private SqlSession sqlSession;
	
	@Override
	public Map<String, Object> duplicatePageList(HashMap<String,Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<PageVO> ret = new ArrayList<PageVO>();
		int total = 0;
		
		PageDao pageDao = sqlSession.getMapper(PageDao.class);
		
		total = pageDao.duplicatePageCount(map);
		ret = pageDao.duplicatePageList(map);
		
		resultMap.put("page",map.get("nPage").toString());
		resultMap.put("records",total);
		resultMap.put("total",CommonUtil.getTotPageSize(total,Integer.parseInt(map.get("page_size").toString())));	
		resultMap.put("list",ret);		
				
		// Ajax Paging 
		String strLink = "javascript:exsoftAdminDupFunc.event.gridMainPage";
		String contextRoot = map.get("contextRoot") != null ? map.get("contextRoot").toString() : "";
		PagingAjaxUtil pagingInfo = new PagingAjaxUtil(Integer.parseInt(map.get("nPage").toString()),total,Integer.parseInt(map.get("page_size").toString()),10,strLink,contextRoot);		
		resultMap.put("pagingInfo",pagingInfo);		
		
		return resultMap;
	}

	@Override
	public Map<String, Object> comDocPageList(HashMap<String, Object> map) throws Exception {
		PageDao pageDao = sqlSession.getMapper(PageDao.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		param.put("doc_id", map.get("doc_id") != null ? map.get("doc_id") : "" );
		
		List<PageVO> pageList = pageDao.comDocPageList(map);
		resultMap.put("pageList",pageList);
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> docPageListForURLMail(HashMap<String, Object> map) throws Exception {
		PageDao pageDao = sqlSession.getMapper(PageDao.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		String docList = map.get("doc_id_list") != null ? map.get("doc_id_list").toString() : "";
		
		param.put("doc_id_list", docList);
		
		List<PageVO> pageList = pageDao.docPageListForURLMail(map);
		resultMap.put("pageList",pageList);
		
		return resultMap;
	}
}
