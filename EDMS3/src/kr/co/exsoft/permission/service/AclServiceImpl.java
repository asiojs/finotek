package kr.co.exsoft.permission.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.dao.DocumentDao;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.library.ExsoftAbstractServiceImpl;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.PagingAjaxUtil;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.folder.dao.FolderDao;
import kr.co.exsoft.permission.dao.AclDao;
import kr.co.exsoft.permission.vo.AclExItemVO;
import kr.co.exsoft.permission.vo.AclItemListVO;
import kr.co.exsoft.permission.vo.AclItemVO;
import kr.co.exsoft.permission.vo.AclVO;

/**
 * Acl 서비스 구현 부분
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Service("aclService")
public class AclServiceImpl extends ExsoftAbstractServiceImpl implements AclService {

	@Autowired
	@Qualifier("sqlSession")
	private SqlSession sqlSession;

	@Autowired
	private CommonService commonService;
	
	@Override
	public Map<String, Object> aclList(HashMap<String, Object> map) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<AclVO> ret = new ArrayList<AclVO>();
		int total = 0;
		
		AclDao aclDao = sqlSession.getMapper(AclDao.class);
		
		total = aclDao.aclPagingCount(map);
		ret = aclDao.aclList(map);
		
		resultMap.put("page",map.get("nPage").toString());
		resultMap.put("records",total);
		resultMap.put("total",CommonUtil.getTotPageSize(total,Integer.parseInt(map.get("page_size").toString())));	
		resultMap.put("list",ret);
		resultMap.put("result",Constant.RESULT_TRUE);
		
		// 3. Ajax Paging 
		String strLink = "javascript:aclManager.grid.paging";
		String contextRoot = map.get("contextRoot") != null ? map.get("contextRoot").toString() : "";
		PagingAjaxUtil pagingInfo = new PagingAjaxUtil(Integer.parseInt(map.get("nPage").toString()),total,Integer.parseInt(map.get("page_size").toString()),10,strLink,contextRoot);		
		resultMap.put("pagingInfo",pagingInfo);
		
		return resultMap;
	}


	@Override
	public Map<String, Object>  aclDetail(HashMap<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		AclVO aclVO = new AclVO();
		AclDao aclDao = sqlSession.getMapper(AclDao.class);
		
		//권한 상세 정보를 가져 온다
		aclVO = aclDao.aclDetail(map);
		resultMap.put("aclDetail", aclVO);
		resultMap.put("result",Constant.RESULT_TRUE);
		
		return resultMap;
	}

	@Override
	public List<AclItemListVO> aclItemList(HashMap<String, Object> map) throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		
		List<AclItemVO> listAclItemVO = new ArrayList<AclItemVO>();
		
		//AclItemVO에서 폴더와 문서에 대하 권한을 1row로 재조합 한다.
		List<AclItemListVO> listAclItemListVO = new ArrayList<AclItemListVO>();
		
		AclDao aclDao = sqlSession.getMapper(AclDao.class);
		
		//권한 상세 정보를 가져 온다
		// 주의 : 이 메서드는 반드시 조건에 is_type을 주면 안된다.
		String is_type = map.get("is_type") != null ? map.get("is_type").toString() : null;
		listAclItemVO = aclDao.aclDetailItems(map);
		
		//폴더 및 문서 권한 합친다.
		for(AclItemVO aclItemVO : listAclItemVO) {
			
			if(!StringUtil.isEmpty(is_type) && is_type.equals(Constant.D)) {
				//VO는 문서 권한, map은 폴더 권한//
				listAclItemListVO.add(CommonUtil.getAclItemListVOFromAclItemVO(new AclItemVO(), aclItemVO));
			} else if(!StringUtil.isEmpty(is_type) && is_type.equals(Constant.F)) {
				// VO는 폴더 권한, map은 문서 권한
				listAclItemListVO.add(CommonUtil.getAclItemListVOFromAclItemVO(aclItemVO, new AclItemVO()));
			} else {
				//1. tempMap에 담기전 ACCESSOR_ID 존재여부 확인
				if(tempMap.containsKey(aclItemVO.getAccessor_id())){
					//존재하면 listAclItemListVO에 폴더와 문서 권한을 조합 추가
					if(aclItemVO.getIs_type().equals(Constant.ACL_IS_TYPE_DOCUMENT)) {
						//VO는 문서 권한, map은 폴더 권한//
						listAclItemListVO.add(CommonUtil.getAclItemListVOFromAclItemVO((AclItemVO)tempMap.get(aclItemVO.getAccessor_id()), aclItemVO));
					}else{
						// VO는 폴더 권한, map은 문서 권한
						listAclItemListVO.add(CommonUtil.getAclItemListVOFromAclItemVO(aclItemVO, (AclItemVO)tempMap.get(aclItemVO.getAccessor_id())));
					}
				}else{
					// 존재하지 아는면 맵에 추가
					tempMap.put(aclItemVO.getAccessor_id(), aclItemVO);
				}
			}
			
		}
		
		return listAclItemListVO;
	}

	@Override
	public Map<String, Object> aclUpdate(HashMap<String, Object> map, AclVO aclVO, List<AclItemVO> aclItemList, SessionVO sessionVO)
			throws Exception {
		AclDao aclDao = sqlSession.getMapper(AclDao.class);		
	
		HashMap<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		param.put("acl_id", aclVO.getAcl_id());
		param.put("isOwnerDelete", "false");
		
		int result = 0;	
		
		// 동일명의 권한이 있는지 체크 한다.
		String src_acl_name = map.get("src_acl_name") != null ? map.get("src_acl_name").toString() : "";;
		String acl_name = aclVO.getAcl_name();
		param.put("acl_name", acl_name);
		
		if( !src_acl_name.equals(acl_name)){
			result = aclDao.aclCountByAclName(param);
			if(result > 0)	{	throw processException("acl.fail.acl.name.duplication");	}
		}
				
		// 1. aclVO update
		result = aclDao.aclUpdate(aclVO);
		if(result == 0)	{	throw processException("common.system.error");	}
		
		// 2. aclItem delete 단, OWNER는 제외
		result = aclDao.aclItemDelete(param);
		//if(result == 0)	{	throw processException("common.system.error");	}
		
		// 3. aclItemList insert
		for(AclItemVO aclItemVO : aclItemList) {
			if(!aclItemVO.getAccessor_id().equals(Constant.OWNER)) {
				result = aclDao.aclItemWrite(aclItemVO);
				if(result == 0)	{	throw processException("common.system.error");	}
			}
		}
		
		resultMap.put("acl_name", acl_name);
		resultMap.put("result",Constant.RESULT_TRUE);
		
		return resultMap;
	}

	@Override
	public Map<String, Object> aclWrite(HashMap<String, Object> map, AclVO aclVO, List<AclItemVO> aclItemList, SessionVO sessionVO)
			throws Exception {
		AclDao aclDao = sqlSession.getMapper(AclDao.class);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		int result = 0;	
		
		String[] dept_id_list = sessionVO.getSessParentGroup().toArray(new String[sessionVO.getSessParentGroup().size()+1]);
		dept_id_list[sessionVO.getSessParentGroup().size()] = sessionVO.getSessGroup_id();  // 배열 시작은 0부터임
		
		String[] team_id_list = sessionVO.getSessProjectGroup().toArray(new String[sessionVO.getSessProjectGroup().size()+1]);
		team_id_list[sessionVO.getSessProjectGroup().size()] = sessionVO.getSessGroup_id();
		
		param.put("dept_id_list", dept_id_list);
		param.put("team_id_list", team_id_list);
		param.put("user_id", sessionVO.getSessId());
		param.put("acl_name", aclVO.getAcl_name());
		
		// 동일명의 권한이 있는지 체크 한다.
		result = aclDao.aclCountByAclName(param);
		if(result > 0)	{	throw processException("acl.fail.acl.name.duplication");	}
		
		// 1. aclVO insert
		if(!StringUtil.isEmpty(aclVO.getAcl_type()) && aclVO.getAcl_type().equals(Constant.ACL_ACL_TYPE_ALL)){
			aclVO.setOpen_name(Constant.ACL_ACL_TYPE_ALL_NAME);
		}
		
		result = aclDao.aclWrite(aclVO);
		if(result == 0)	{	throw processException("common.system.error");	}
		
		// 2. aclItem insert
		for(AclItemVO aclItemVO : aclItemList) {
			result = aclDao.aclItemWrite(aclItemVO);
			if(result == 0)	{	throw processException("common.system.error");	}
		}
		
		resultMap.put("acl_id", aclVO.getAcl_id());
		resultMap.put("acl_name", aclVO.getAcl_name());
		resultMap.put("result",Constant.RESULT_TRUE);
		
		return resultMap;
	}

	@Override
	public List<String> aclDeleteValid(HashMap<String, Object> map) throws Exception {
		
		DocumentDao documentDao = sqlSession.getMapper(DocumentDao.class);
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		List<String> ret = new ArrayList<String>();
		
		String aclList =  map.get("acl_idList") != null ? map.get("acl_idList").toString() : "";
		
		// 1.입력값 유효성 체크
		if(aclList == null ||  aclList.equals(""))	{
			throw processException("common.required.error");
		}
		
		JSONArray jsonArray = JSONArray.fromObject(aclList);
		if(jsonArray.size() > 0 ) {		
			 for(int j=0;j < jsonArray.size();j++)	{				 
				 ret.add(jsonArray.getJSONObject(j).getString("acl_id").toString());
			 }
		}
		
		// 2.XR_TYPE  사용여부 (XR_DOCUMENT/XR_DOCUMENT_DEL)
		if(ret != null && ret.size() > 0) {
			
			param.put("acl_list", ret);
			param.put("table_nm",Constant.DOC_TABLE);
			if(documentDao.isUsingAcl(param) > 0 )	{
				throw processException("acl.fail.acl.id.use.doc");	
			}			
			
			param.put("table_nm",Constant.DOC_DEL_TABLE);
			if(documentDao.isUsingAcl(param) > 0 )	{
				throw processException("acl.fail.acl.id.use.docdel");	
			}
			
			param.put("table_nm",Constant.FOLDER_TABLE);
			if(folderDao.isUsingAcl(param) > 0 )	{
				throw processException("acl.fail.acl.id.use.folder");	
			}
		}
		
		return ret;
	}

	@Override
	public Map<String, Object> aclDelete(HashMap<String, Object> map, List<String> delList, SessionVO sessionVO) throws Exception {
		AclDao aclDao = sqlSession.getMapper(AclDao.class);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
//		long history_seq = 0;
		int result = 0;
		
		for(String acl_id : delList){
			param.put("acl_id", acl_id);
			param.put("isOwnerDelete", "true");
			
			// 1. XR_ACL 삭제
			result = aclDao.aclDelete(param);
			if(result == 0)	{	throw processException("common.system.error");	}
			
			// 2. XR_ACLITEM 삭제
			result = aclDao.aclItemDelete(param);
			if(result == 0)	{	throw processException("common.system.error");	}
			
//			// 3.XR_HISTORY 등록처리
//			history_seq = commonService.commonNextVal(Constant.COUNTER_ID_HISTORY);
//			HistoryVO historyVO = CommonUtil.setHistoryVO(history_seq,acl_id, Constant.ACTION_DELETE,  Constant.TARGET_ACL, sessionVO);
//			result = commonService.historyWrite(historyVO);
//			if(result == 0)	{	throw processException("common.system.error");	}	
		}
		
		resultMap.put("result",Constant.RESULT_TRUE);
		return resultMap;
	}
	
	@Override
	public Map<String, Object> aclItem(HashMap<String,Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<AclItemVO> aclItemList = new ArrayList<AclItemVO>();
		
		AclDao aclDao = sqlSession.getMapper(AclDao.class);
		
		// 파라미터 : acl_id / is_type=D/F
		AclVO aclVO = aclDao.aclDetail(map);
		
		aclItemList = aclDao.aclDetailItems(map);
		
		resultMap.put("acl_name",aclVO.getAcl_name());
		resultMap.put("aclItemList",aclItemList);
		resultMap.put("result",Constant.RESULT_TRUE);
		
		return resultMap;
	}

	@Override
	public Map<String, Object> aclInheritDetail(HashMap<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		AclDao aclDao = sqlSession.getMapper(AclDao.class);
		
		String type =  map.get("type") != null ? map.get("type").toString() : "";
		String folder_id = map.get("folder_id") != null ? map.get("folder_id").toString() : "";
		String parent_folder_id = map.get("parent_folder_id") != null ? map.get("parent_folder_id").toString() : "";
		
		
		if(type.equals("folder")){
			param.put("folder_id", parent_folder_id);
		} else if(type.equals("document")){
			param.put("folder_id", folder_id);
		}
		
		AclVO aclVO = aclDao.aclInheritDetail(param);
		resultMap.put("aclDetail",aclVO);		
		resultMap.put("result",Constant.RESULT_TRUE);
		return resultMap;
	}
	
	@Override
	public List<AclExItemVO> exAclItemList(HashMap<String, Object> map) throws Exception {
		AclDao aclDao = sqlSession.getMapper(AclDao.class);

		List<AclExItemVO> listAclExItemVO = new ArrayList<AclExItemVO>();
		listAclExItemVO = aclDao.aclExDetailItems(map);
		
		for(int i=0; i<listAclExItemVO.size(); i++) {
			AclExItemVO tempVO = listAclExItemVO.get(i);
			
			if(tempVO.getAct_delete().equals(Constant.T))
				tempVO.setDoc_default_acl(Constant.ACL_DELETE);
			else if(tempVO.getAct_update().equals(Constant.T))
				tempVO.setDoc_default_acl(Constant.ACL_UPDATE);
			else if(tempVO.getAct_read().equals(Constant.T))
				tempVO.setDoc_default_acl(Constant.ACL_READ);
			else if(tempVO.getAct_browse().equals(Constant.T))
				tempVO.setDoc_default_acl(Constant.ACL_BROWSE);
			else
				tempVO.setDoc_default_acl(Constant.ACL_NONE);
			
			listAclExItemVO.set(i, tempVO);
		}
		
		return listAclExItemVO;
	}

	@Override
	public Map<String, Object> aclExItemWrite(HashMap<String, Object> map, AclExItemVO aclExItemVO, SessionVO sessionVO)	throws Exception {
		AclDao aclDao = sqlSession.getMapper(AclDao.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		aclDao.aclExItemWrite(aclExItemVO);
		resultMap.put("result",Constant.RESULT_TRUE);
		
		return resultMap;
	}

	@Override
	public Map<String, Object> aclExItemDelete(HashMap<String, Object> map,	String delDoc_id, SessionVO sessionVO) throws Exception {
		AclDao aclDao = sqlSession.getMapper(AclDao.class);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 삭제할 내용이 없어도 성공
		param.put("doc_id", delDoc_id);
		aclDao.aclExItemDelete(param);
		resultMap.put("result",Constant.RESULT_TRUE);
		return resultMap;
	}

}
