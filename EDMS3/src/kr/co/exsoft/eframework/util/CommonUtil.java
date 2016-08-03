package kr.co.exsoft.eframework.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.Model;
import org.apache.commons.collections.map.CaseInsensitiveMap;

import kr.co.exsoft.document.service.DocumentService;
import kr.co.exsoft.document.vo.AttrVO;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.vo.VO;
import kr.co.exsoft.folder.service.FolderService;
import kr.co.exsoft.permission.vo.AclExItemVO;
import kr.co.exsoft.permission.vo.AclItemListVO;
import kr.co.exsoft.permission.vo.AclItemVO;
import kr.co.exsoft.process.vo.ProcessExecutorVO;
import kr.co.exsoft.common.service.CacheService;
import kr.co.exsoft.common.vo.DocumentHtVO;
import kr.co.exsoft.common.vo.MenuAuthVO;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.common.vo.HistoryVO;

/**
 * Common Utility
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public class CommonUtil {

	protected static final Log logger = LogFactory.getLog(CommonUtil.class);

	/**
	 * StackTrace 반환.
	 * @param e
	 * @return String
	 */
	public static String getPrintStackTrace(Exception e) {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(out));
		
		return out.toString();
	}
	
	/**
	 * 오늘 날짜로 폴더 경로를 생성한다.
	 * @param rootFolder
	 * @return String
	 */
	public static String getContentPathByDate(String rootFolder) {
		
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		int date = Calendar.getInstance().get(Calendar.DATE);   		   
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

		String dirPath = String.format("%04d%s%02d%s%02d%s%02d", year, "/", month, "/", date, "/", hour);	
		String folder = rootFolder + "/" + dirPath + "/";
		
		return folder;
	}
	
	/**
	 * 새 버전 값을 얻는다.
	 * @param oldVersion
	 * @param version_type
	 * @return String
	 */
	public static String getUpVersion(String oldVersion, String version_type) {

    	logger.debug("oldVersion = " + oldVersion);
    	logger.debug("version_type = " + version_type);
		
		String[] versions = StringUtil.split2Array(oldVersion, ".", false);
		
		String majorVersion = versions[versions.length - 2];
		String minorVersion = versions[versions.length - 1];
		String stemVersion = oldVersion.substring(0, oldVersion.length() - majorVersion.length() - minorVersion.length() - 1);
		String newVersion = "";

		if (version_type.equals(Constant.VERSION_MAJOR_VERSION)) {
			newVersion = stemVersion + (Integer.parseInt(majorVersion) + Integer.parseInt(Constant.MAJOR_VERSION_UP)) + ".0";
		} else if (version_type.equals(Constant.VERSION_MINOR_VERSION)) {
			newVersion = stemVersion + majorVersion + "." + (Integer.parseInt(minorVersion) + Integer.parseInt(Constant.MINOR_VERSION_UP));
		}
		
		logger.debug("Return value=" + newVersion);
		
		return newVersion;
	}
	
	
	/**
	 * 기간 구하기 (날짜 기준)
	 * @param term
	 * @param search_conditions
	 * @return HashMap
	 */
	public static HashMap<String, Object> getRecentTerm(int term, HashMap<String, Object> search_conditions) {	
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		
		// start date setting
   	 	cal.add ( Calendar.DATE, term ); 
        Date currentDate = cal.getTime();
        
        // end date setting
        cal = Calendar.getInstance();
        cal.add ( Calendar.DATE, +1 );
        Date toDate =  cal.getTime();
		
        search_conditions.put(Constant.DATE_START, sf.format(currentDate));
		search_conditions.put(Constant.DATE_END, sf.format(toDate));
		
		return search_conditions;
	}
			
	/**
	 * Response Header 세팅.
	 * @param fileName
	 * @param fileSize
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static void setResponseHeader(String fileName, long fileSize, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		// 1. Content Type 세팅.
		String mimetype = request.getSession().getServletContext().getMimeType(fileName);

		if (mimetype == null || mimetype.length() == 0) {
			response.setContentType("application/octet-stream; charset=utf-8");
		}else {
    		response.setContentType(mimetype + "; charset=utf-8");
    	}
				
		// 2. Content Disposition 세팅. 
		response.setHeader("Content-Disposition", "attachment; filename="+ CharConversion.K2E(fileName) + ";");

		// 3. Content Length 세팅.
		if (fileSize > 0) {
			response.setHeader("Content-Length", "" + fileSize);
		}		
		
		response.flushBuffer();
	}
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 :총 페이지수 구하기
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : getTotPageSize
	 * @param nTotLineNum 총 라인수
	 * @param nMaxListLine 한페이지에 보여지는 최대 라인수
	 * @return
	 */
	public static int getTotPageSize(int nTotLineNum,int nMaxListLine) {
		
		int nTotPageSize = 0;
		
		if (nTotLineNum == 0)			// 총라인수
			nTotPageSize = 1;			
		else if ((nTotLineNum % nMaxListLine) != 0)
			nTotPageSize = nTotLineNum / nMaxListLine + 1;
		else
			nTotPageSize = nTotLineNum / nMaxListLine;
		
		return nTotPageSize;
	}
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : Grid 페이지번호 구하기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getPage
	 * @param map
	 * @return int
	 */
	public static int getPage(HashMap<String,Object> map) {
		
		String is_search = "";
		String page_init = "";
		int ret = 0;
		
		is_search = map.get("is_search") != null ? map.get("is_search").toString() : Constant.RESULT_FALSE;
		page_init = map.get("page_init") != null ? map.get("page_init").toString() : Constant.RESULT_FALSE;
		
		if(is_search.equals(Constant.RESULT_TRUE) || page_init.equals(Constant.RESULT_TRUE))	{
			ret = 1;
		}else {
			ret = map.get("page") != null ? Integer.parseInt(map.get("page").toString()) : 1 ;
		}
		
		return ret;
	}
	
	
	public static String getPageSize(HashMap<String,Object> map,SessionVO sessionVO) {
		
		String ret = "";
		
		if(map.get("rows") != null)		{
			
			if(map.get("rows").toString().equals(sessionVO.getSessPage_size()))	{
				// 세션 변경없을 경우
				ret = map.get("rows").toString();
			}else {
				// 세션 변경시 
				ret =sessionVO.getSessPage_size();
			}
			
		}else {
			ret = sessionVO.getSessPage_size();
		}
		
		
		return ret;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서, 파일, 폴더 pk값 구하기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getStringID
	 * @param prefix
	 * @param id
	 * @return String
	 */
	public static String getStringID(String prefix, int id) {
		return prefix + String.format("%012d", id); 
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더/권한/문서유형 객체 생성하기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setHistoryVO
	 * @param history_seq
	 * @param target_id
	 * @param action_id
	 * @param target_type
	 * @param sessionVO
	 * @return HistoryVO
	 */
	public static HistoryVO setHistoryVO(long history_seq,String target_id,String action_id,String target_type,SessionVO sessionVO) {
		
		HistoryVO historyVO = new HistoryVO();
		
		historyVO.setHistory_seq(history_seq);
		historyVO.setActor_id(sessionVO.getSessId());
		historyVO.setActor_nm(sessionVO.getSessName());
		historyVO.setGroup_id(sessionVO.getSessGroup_id());
		historyVO.setGroup_nm(sessionVO.getSessGroup_nm());
		historyVO.setAction_id(action_id);
		historyVO.setTarget_id(target_id);
		historyVO.setTarget_type(target_type);
		historyVO.setAction_place(Constant.ACTION_PLACE);
		
		return historyVO;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 이력 객체 생성하기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setDocumentHistory
	 * @param doc_seq
	 * @param root_id
	 * @param target_id
	 * @param action_id
	 * @param type_id
	 * @param doc_name
	 * @param version_no
	 * @param sessionVO
	 * @return DocumentHtVO
	 */
	public static DocumentHtVO setDocumentHistory(long doc_seq,String root_id,String target_id,String action_id,
			String type_id,String doc_name,String version_no,SessionVO sessionVO) {
		
		DocumentHtVO vo = new DocumentHtVO();
		
		vo.setDoc_seq(doc_seq);
		vo.setRoot_id(root_id);
		vo.setAction_id(action_id);
		vo.setTarget_id(target_id);
		vo.setType_id(type_id);
		vo.setDoc_name(doc_name);
		vo.setVersion_no(version_no);
		vo.setActor_id(sessionVO.getSessId());
		vo.setActor_nm(sessionVO.getSessName());
		vo.setGroup_id(sessionVO.getSessGroup_id());
		vo.setGroup_nm(sessionVO.getSessGroup_nm());
		vo.setConnect_ip(sessionVO.getSessRemoteIp());
		vo.setAction_place(Constant.ACTION_PLACE);
		
		return vo;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 이력 객체 생성하기 : 배치프로그램
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setDocumentHistory
	 * @param doc_seq
	 * @param root_id
	 * @param target_id
	 * @param action_id
	 * @param type_id
	 * @param doc_name
	 * @param version_no
	 * @param systemUser : 시스템관리자 정보
	 * @return DocumentHtVO
	 */
	public static DocumentHtVO setDocumentHistory(long doc_seq,String root_id,String target_id,String action_id,
			String type_id,String doc_name,String version_no,HashMap<String,Object> systemUser) {
		
		DocumentHtVO vo = new DocumentHtVO();
		
		vo.setDoc_seq(doc_seq);
		vo.setRoot_id(root_id);
		vo.setAction_id(action_id);
		vo.setTarget_id(target_id);
		vo.setType_id(type_id);
		vo.setDoc_name(doc_name);
		vo.setVersion_no(version_no);
		vo.setActor_id(systemUser.get("user_id").toString());
		vo.setActor_nm(systemUser.get("user_name").toString());
		vo.setGroup_id(systemUser.get("group_id").toString());
		vo.setGroup_nm(systemUser.get("group_name").toString());
		vo.setConnect_ip(Constant.BATCH_IP);
		vo.setAction_place(Constant.ACTION_PLACE);
		
		return vo;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 이력 객체 생성하기 :: 문서이동/소유권변경에 해당함
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setDocumentHistory
	 * @param doc_seq
	 * @param root_id
	 * @param target_id
	 * @param action_id
	 * @param type_id
	 * @param doc_name
	 * @param version_no
	 * @param targetMap
	 * @param sessionVO
	 * @return DocumentHtVO
	 */
	public static DocumentHtVO setDocumentHistory(long doc_seq,String root_id,String target_id,String action_id,
			String type_id,String doc_name,String version_no,HashMap<String,Object> targetMap,SessionVO sessionVO) {
		
		DocumentHtVO vo = new DocumentHtVO();
		
		vo.setDoc_seq(doc_seq);
		vo.setRoot_id(root_id);
		vo.setAction_id(action_id);
		vo.setTarget_id(target_id);
		vo.setType_id(type_id);
		vo.setDoc_name(doc_name);
		vo.setVersion_no(version_no);
		vo.setActor_id(sessionVO.getSessId());
		vo.setActor_nm(sessionVO.getSessName());
		vo.setGroup_id(sessionVO.getSessGroup_id());
		vo.setGroup_nm(sessionVO.getSessGroup_nm());
		vo.setConnect_ip(sessionVO.getSessRemoteIp());
		vo.setAction_place(Constant.ACTION_PLACE);
		
		if(targetMap != null && ( 
				action_id.equals(Constant.ACTION_MOVE) ||  action_id.equals(Constant.ACTION_CHANGE_CREATOR) ))	{
			
			vo.setBefore_id(targetMap.get("before_id").toString());
			vo.setBefore_nm(targetMap.get("before_nm").toString());
			vo.setAfter_id(targetMap.get("after_id").toString());
			vo.setAfter_nm(targetMap.get("after_nm").toString());
		}
		
		return vo;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 리소스 ID의 Prefix를 주어진 Prefix로 바꾸어 반환한다.
	 * 2. 처리내용 :
	 * </pre>
	 * 
	 * @Method Name : getChangedResourceIDByPrefix
	 * @param resource_id
	 * @param prefix
	 * @return
	 */
	public static String getChangedResourceIDByPrefix(String resource_id, String prefix) {
		if (resource_id == null)
			return null;
		else
			return prefix + resource_id.substring(3);
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : AclItemVO을 이용하여 AclItemListVO객체 조합
	 * 2. 처리내용 : XR_ACLITEM에서 존재하는 폴더 및 문서에 대한 2개의 권한을 하나로 합칩
	 * </pre>
	 * @Method Name : getAclItemListVOFromAclItemVO
	 * @param folderACLItemVO
	 * @param documentAclItemVO
	 * @return AclItemListVO
	 */
	public static AclItemListVO getAclItemListVOFromAclItemVO(AclItemVO folderACLItemVO, AclItemVO documentAclItemVO) {
		AclItemListVO aclItemListVO = new AclItemListVO();
		
		//공통권한 셋팅
		aclItemListVO.setAcl_id(!StringUtil.isEmpty(folderACLItemVO.getAcl_id()) ? folderACLItemVO.getAcl_id() : documentAclItemVO.getAcl_id());		
		aclItemListVO.setAccessor_id(!StringUtil.isEmpty(folderACLItemVO.getAccessor_id()) ? folderACLItemVO.getAccessor_id() : documentAclItemVO.getAccessor_id());
		aclItemListVO.setAccessor_name(!StringUtil.isEmpty(folderACLItemVO.getAccessor_name()) ? folderACLItemVO.getAccessor_name() : documentAclItemVO.getAccessor_name());
		aclItemListVO.setAccessor_isgroup(!StringUtil.isEmpty(folderACLItemVO.getAccessor_isgroup()) ? folderACLItemVO.getAccessor_isgroup() : documentAclItemVO.getAccessor_isgroup() );
		aclItemListVO.setAccessor_isalias(!StringUtil.isEmpty(folderACLItemVO.getAccessor_isalias()) ? folderACLItemVO.getAccessor_isalias() : documentAclItemVO.getAccessor_isalias());
		
		//폴더권한 셋팅
		aclItemListVO.setFol_act_browse(folderACLItemVO.getAct_browse());
		aclItemListVO.setFol_act_read(folderACLItemVO.getAct_read());
		aclItemListVO.setFol_act_update(folderACLItemVO.getAct_update());
		aclItemListVO.setFol_act_delete(folderACLItemVO.getAct_delete());
		aclItemListVO.setFol_act_create(folderACLItemVO.getAct_create());
		aclItemListVO.setFol_act_change_permission(folderACLItemVO.getAct_change_permission());
		//문서권한 셋팅
		aclItemListVO.setDoc_act_browse(documentAclItemVO.getAct_browse());
		aclItemListVO.setDoc_act_read(documentAclItemVO.getAct_read());
		aclItemListVO.setDoc_act_update(documentAclItemVO.getAct_update());
		aclItemListVO.setDoc_act_delete(documentAclItemVO.getAct_delete());
		aclItemListVO.setDoc_act_create(documentAclItemVO.getAct_create());
		aclItemListVO.setDoc_act_cancel_checkout(documentAclItemVO.getAct_cancel_checkout());
		aclItemListVO.setDoc_act_change_permission(documentAclItemVO.getAct_change_permission());
		
		//문서 및 폴더 기본권한 값 셋팅
		if(aclItemListVO.getFol_act_delete().equals(Constant.T))
			aclItemListVO.setFol_default_acl(Constant.ACL_DELETE);
		else if(aclItemListVO.getFol_act_update().equals(Constant.T))
			aclItemListVO.setFol_default_acl(Constant.ACL_UPDATE);
		else if(aclItemListVO.getFol_act_read().equals(Constant.T))
			aclItemListVO.setFol_default_acl(Constant.ACL_READ);
		else if(aclItemListVO.getFol_act_browse().equals(Constant.T))
			aclItemListVO.setFol_default_acl(Constant.ACL_BROWSE);
		
		if(aclItemListVO.getDoc_act_delete().equals(Constant.T))
			aclItemListVO.setDoc_default_acl(Constant.ACL_DELETE);
		else if(aclItemListVO.getDoc_act_update().equals(Constant.T))
			aclItemListVO.setDoc_default_acl(Constant.ACL_UPDATE);
		else if(aclItemListVO.getDoc_act_read().equals(Constant.T))
			aclItemListVO.setDoc_default_acl(Constant.ACL_READ);
		else if(aclItemListVO.getDoc_act_browse().equals(Constant.T))
			aclItemListVO.setDoc_default_acl(Constant.ACL_BROWSE);
		else
			aclItemListVO.setDoc_default_acl(Constant.ACL_NONE);
		
		return aclItemListVO;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : AclItemListVO를 이용하여 AclItemVO를 만듬
	 * 2. 처리내용 : 즉, AclItemListVO에 폴더, 문서권한을 AclItemVO로 분리하는 작업
	 * </pre>
	 * @Method Name : aclWriteValid
	 * @param map
	 * @return
	 * @throws Exception List<AclItemVO>
	 */
	public static List<AclItemVO> getAclItemVOFromAclItemListVO(String aclItemListArrayList, String acl_id) throws Exception{
		
		List<AclItemVO> aclItemList = new ArrayList<AclItemVO>();
		
		if(StringUtil.isEmpty(aclItemListArrayList)){
			throw new Exception("common.required.error");
		}
		
		// JsonArray 객체 생성하기
		JSONArray jsonArray = JSONArray.fromObject(aclItemListArrayList);
		
		if(jsonArray.size() > 0 ) {
			for(int j=0; j<jsonArray.size(); j++){
				AclItemVO folderAclItemVo = new AclItemVO();
				AclItemVO documentAclItemVo = new AclItemVO();
				
				//folder 권한을 매핑
				folderAclItemVo.setAcl_id(acl_id);					// PK
				folderAclItemVo.setIs_type("F");				// PK
				folderAclItemVo.setAccessor_id(jsonArray.getJSONObject(j).getString("accessor_id"));		// PK
				folderAclItemVo.setAccessor_isgroup(jsonArray.getJSONObject(j).getString("accessor_isgroup"));
				folderAclItemVo.setAccessor_isalias(jsonArray.getJSONObject(j).getString("accessor_isalias"));
				folderAclItemVo.setAct_create(jsonArray.getJSONObject(j).getString("fol_act_create"));
				folderAclItemVo.setAct_cancel_checkout(Constant.F);
				folderAclItemVo.setAct_change_permission(jsonArray.getJSONObject(j).getString("fol_act_change_permission"));
				folderAclItemVo = setAclItemBRCD(folderAclItemVo, jsonArray.getJSONObject(j).getString("fol_default_acl")); 
								
				//document 권한을 매핑
				documentAclItemVo.setAcl_id(acl_id);														// PK
				documentAclItemVo.setIs_type("D");															// PK
				documentAclItemVo.setAccessor_id(jsonArray.getJSONObject(j).getString("accessor_id"));		// PK
				documentAclItemVo.setAccessor_isgroup(jsonArray.getJSONObject(j).getString("accessor_isgroup"));
				documentAclItemVo.setAccessor_isalias(jsonArray.getJSONObject(j).getString("accessor_isalias"));
				documentAclItemVo.setAct_create(jsonArray.getJSONObject(j).getString("doc_act_create"));
				documentAclItemVo.setAct_cancel_checkout(jsonArray.getJSONObject(j).getString("doc_act_cancel_checkout"));
				documentAclItemVo.setAct_change_permission(jsonArray.getJSONObject(j).getString("doc_act_change_permission"));
				documentAclItemVo = setAclItemBRCD(documentAclItemVo, jsonArray.getJSONObject(j).getString("doc_default_acl"));
				
				aclItemList.add(folderAclItemVo);
				aclItemList.add(documentAclItemVo);
				
			}
		}
		
		return aclItemList;
	}
	
	public static AclItemVO setAclItemBRCD(AclItemVO aclItemVO, String acl_brcd){
		
		// only JDK1.7
		switch (acl_brcd) {
		case Constant.ACL_DELETE:
			aclItemVO.setAct_browse(Constant.T);aclItemVO.setAct_read(Constant.T);
			aclItemVO.setAct_update(Constant.T);aclItemVO.setAct_delete(Constant.T);
			break;
		case Constant.ACL_UPDATE:
			aclItemVO.setAct_browse(Constant.T);aclItemVO.setAct_read(Constant.T);
			aclItemVO.setAct_update(Constant.T);aclItemVO.setAct_delete(Constant.F);
			break;
		case Constant.ACL_READ:
			aclItemVO.setAct_browse(Constant.T);aclItemVO.setAct_read(Constant.T);
			aclItemVO.setAct_update(Constant.F);aclItemVO.setAct_delete(Constant.F);
			break;
		case Constant.ACL_BROWSE:
			aclItemVO.setAct_browse(Constant.T);aclItemVO.setAct_read(Constant.F);
			aclItemVO.setAct_update(Constant.F);aclItemVO.setAct_delete(Constant.F);
			break;

		default:
			break;
		}
		
		return aclItemVO;
		
	}
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 감사정책 기준초과자 메일 본문 생성 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getAuditReportMessage
	 * @param auditDate
	 * @param auditConfig
	 * @param auditList
	 * @return
	 * @throws Exception StringBuffer
	 */
	public static StringBuffer getAuditReportMessage(String auditDate, Map<String,Object> auditConfig, List<HashMap<String,Object>> auditList) throws Exception {

    	StringBuffer message = new StringBuffer();
		
    	message.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
    	message.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
    	message.append("<head>");
    	message.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
    	message.append("<title>Audit Report, " + auditDate + "</title>");
    	message.append("<style type=\"text/css\">");
    	message.append("html, body {");
    	message.append("	height:100%;");
    	message.append("	background-color:#ffffff;");
    	message.append("}");
    	message.append("body {");
    	message.append("	margin:0;");
    	message.append("	padding:0;");
    	message.append("	font-size: 12px;");
    	message.append("	color:#666666;");
    	message.append("	font-family:Gulim, GulimChe;");
    	message.append("	text-decoration: none;");
    	message.append("	scrollbar-highlight-color: #e7e7e7;");
    	message.append("	scrollbar-shadow-color: #e7e7e7;");
    	message.append("	scrollbar-arrow-color: #6591c2;");
    	message.append("	scrollbar-face-color: #FFFFFF;");
    	message.append("	scrollbar-3dlight-color: #FFFFFF;");
    	message.append("	scrollbar-darkshadow-color: #FFFFFF;");
    	message.append("	scrollbar-track-color: #FFFFFF;");
    	message.append("}");
    	message.append("h1, h2, h3 {");
    	message.append("	color:white;");
    	message.append("}");
    	message.append("h4 {");
    	message.append("	font-size:14px;");
    	message.append("	margin:10px 0 10px 0;");
    	message.append("}");
    	message.append("</style>");
    	message.append("</head>");
    	message.append("<body>");
    	message.append("<table width=\"500\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"border:1px solid #6C97C8;\">");
    	message.append("    <tr>");
    	message.append("        <td colspan=\"2\" style=\"background-color:#6C97C8; border-top:10px solid #333;\"><table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin-top:5px;\">");
    	message.append("                <tr>");
    	message.append("                    <td>&nbsp;</td>");
    	message.append("                    <td style=\"padding:10px\" <h3>[LG Chem EDMS] Audit Report</h3></td>");
    	message.append("                </tr>");
    	message.append("            </table></td>");
    	message.append("    </tr>");
    	message.append("    <tr>");
    	message.append("        <td style=\"padding:15px;background:#EBF4FF;\" colspan=\"2\" align=\"center\" valign=\"middle\"><table width=\"500\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"background:#fff; border:5px solid #fff;\">");
    	message.append("                <tr>");
    	message.append("                    <td>&nbsp;</td>");
    	message.append("                    <td align=\"left\"><h4>Audit Trail</h4></td>");
    	message.append("                </tr>");
    	message.append("                <tr>");
    	message.append("                    <td>&nbsp;</td>");
    	message.append("                    <td align=\"left\">Audit date : " + auditDate + "</td>");
    	message.append("                </tr>");
    	message.append("                <tr>");
    	message.append("                    <td>&nbsp;</td>");
    	message.append("                    <td align=\"left\">Audit condition : Read count (" + auditConfig.get("read_count_threshold") + " counts / 1 day)</td>");
    	message.append("                </tr>");
    	message.append("            </table></td>");
    	message.append("    </tr>");
    	
    	int i = 0;
    	
    	for (HashMap<String,Object> audit : auditList) {
    		// 새로운 행의 시작의 경우.
    		if (i % 2 == 0) {
    	    	message.append("    <tr>");
    	    	message.append("        <td colspan=\"2\" height=\"30\">&nbsp;</td>");
    	    	message.append("    </tr>");
    	    	message.append("    <tr>");
    			message.append("        <td align=\"left\" valign=\"top\" style=\"padding-left:15;\"><table border=\"1\" cellpadding=\"4\" cellspacing=\"0\" style=\"border-collapse: collapse; border:1px solid #b6c0df; margin-left:20px;\">");
    			message.append("                <tr>");
    			message.append("                    <td width=\"15\" rowspan=\"4\" align=\"center\" style=\"background:#6C97C8; color:#fff;\" >&nbsp;" + (i + 1) + "&nbsp;</td>");
    			message.append("                    <td width=\"70\" align=\"left\" style=\"border-right-style:dashed;\" >Name</td>");
    			message.append("                    <td width=\"240\" align=\"left\">" + audit.get("user_name") + "</td>");
    			message.append("                </tr>");
    			message.append("                <tr>");
    			message.append("                    <td align=\"left\" style=\"border-right-style:dashed;\" >ID</td>");
    			message.append("                    <td align=\"left\" >" + audit.get("user_id") + "</td>");
    			message.append("                </tr>");
    			message.append("                <tr>");
    			message.append("                    <td align=\"left\" style=\"border-right-style:dashed;\" >Department</td>");
    			message.append("                    <td align=\"left\" >" + audit.get("group_name") + "</td>");
    			message.append("                </tr>");
    			message.append("                <tr>");
    			message.append("                    <td align=\"left\" style=\"border-right-style:dashed;\" >Read count</td>");
    			message.append("                    <td align=\"left\" >" + audit.get("read_count") + "</td>");
    			message.append("                </tr>");
    			message.append("            </table></td>");
    		} 
    		// 두번째 열의 경우.
    		else {
    			message.append("        <td align=\"left\" valign=\"top\" style=\"padding-left:30;\"><table border=\"1\" cellpadding=\"4\" cellspacing=\"0\" style=\"border-collapse:collapse; border:1px solid #b6c0df;\">");
    			message.append("                <tr>");
    			message.append("                    <td width=\"15\" rowspan=\"4\" align=\"center\" style=\"background:#6C97C8; color:#fff;\"  >&nbsp;" + (i + 1) + "&nbsp;</td>");
    			message.append("                    <td width=\"70\" align=\"left\" style=\"border-right-style:dashed;\" >Name</td>");
    			message.append("                    <td width=\"240\" align=\"left\">" + audit.get("user_name") + "</td>");
    			message.append("                </tr>");
    			message.append("                <tr>");
    			message.append("                    <td align=\"left\" style=\"border-right-style:dashed;\" >ID</td>");
    			message.append("                    <td align=\"left\" >" + audit.get("user_id")  + "</td>");
    			message.append("                </tr>");
    			message.append("                <tr>");
    			message.append("                    <td align=\"left\" style=\"border-right-style:dashed;\" >Department</td>");
    			message.append("                    <td align=\"left\" >" + audit.get("group_name")+ "</td>");
    			message.append("                </tr>");
    			message.append("                <tr>");
    			message.append("                    <td align=\"left\" style=\"border-right-style:dashed;\" >Read count</td>");
    			message.append("                    <td align=\"left\" >" + audit.get("read_count") + "</td>");
    			message.append("                </tr>");
    			message.append("            </table></td>");
    			message.append("    </tr>");    			
    		}
        	
        	// 마지막 아이템이면서 첫번째 열의 경우.
        	if (i + 1 == auditList.size() && i % 2 != 0) {
        		message.append("        <td></td>");
        	}
        	
        	i++;
    	}

    	message.append("    </tr>");
    	message.append("    <tr>");
    	message.append("        <td colspan=\"2\" height=\"20\">&nbsp;</td>");
    	message.append("    </tr>");
    	message.append("</table>");
    	message.append("</body>");
    	message.append("</html>");

		return message;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : jsonArrayToList
	 * @param map
	 * @param param1
	 * @param param2
	 * @return List<String>
	 */
	public static List<String> jsonArrayToList(HashMap<String,Object> map,String key,String value){
		
		List<String> ret = new ArrayList<String>();
		
		if(map.get(key) != null && !map.get(key).toString().equals(""))	{
			
			JSONArray jsonArray = JSONArray.fromObject(map.get(key));
			
			if(jsonArray.size() > 0 ) {		
				
				 for(int j=0;j < jsonArray.size();j++)	{		
					 ret.add(jsonArray.getJSONObject(j).getString(value).toString());
				 }
			}
		}
		return ret;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 등록/수정 공통 :: 신규등록 파일 목록 생성하기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : jsonArrayToFileList
	 * @param jsonArray
	 * @return List<HashMap<String,Object>>
	 */
	public static List<HashMap<String,Object>> jsonArrayToFileList(HashMap<String,Object> map){
		
		List<HashMap<String,Object>> ret = new ArrayList<HashMap<String,Object>>();

		if(map.get("fileList") != null && !map.get("fileList").toString().equals(""))	{
			
			JSONArray jsonArray = JSONArray.fromObject(map.get("fileList") );
			
			if(jsonArray.size() > 0 ) {
//				try{
//					
//				}catch(Exception e){
//					throw e;
//				}
				
				for(int j=0;j < jsonArray.size();j++)	{		
					HashMap<String, Object> param = new HashMap<String, Object>();
					param.put("orgFile",jsonArray.getJSONObject(j).getString("orgFile").toString());
					param.put("contentPath",jsonArray.getJSONObject(j).getString("contentPath").toString());
					param.put("fileSize",jsonArray.getJSONObject(j).getString("fileSize").toString());
					param.put("volumeId",jsonArray.getJSONObject(j).getString("volumeId").toString());
					
					ret.add(param);
				}
				
			}
		}
		
		return ret;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서수정시 삭제될 파일목록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : jsonArrayToDelFileList
	 * @param map
	 * @return List<HashMap<String,Object>>
	 */
	public static List<HashMap<String,Object>> jsonArrayToDelFileList(HashMap<String,Object> map){
		
		List<HashMap<String,Object>> ret = new ArrayList<HashMap<String,Object>>();

		if(map.get("dFileList") != null && !map.get("dFileList").toString().equals(""))	{
			
			JSONArray jsonArray = JSONArray.fromObject(map.get("dFileList") );
			
			if(jsonArray.size() > 0 ) {		
				
				 for(int j=0;j < jsonArray.size();j++)	{		
					 HashMap<String, Object> param = new HashMap<String, Object>();
					 param.put("page_id",jsonArray.getJSONObject(j).getString("page_id").toString());					 
					 ret.add(param);
				 }
			}
		}
		
		return ret;
	}
	
	public static List<ProcessExecutorVO> jsonArrayToProcessExecutorList(HashMap<String,Object> map){
		
		List<ProcessExecutorVO> ret = new ArrayList<ProcessExecutorVO>();

		// 대표 작성자
		if(map.get("authorList") != null && !map.get("authorList").toString().equals(""))	{
			
			JSONArray jsonArray = JSONArray.fromObject(map.get("authorList") );
			
			if(jsonArray.size() > 0 ) {		
				
				 for(int j=0;j < jsonArray.size();j++)	{		
					 ProcessExecutorVO author = new ProcessExecutorVO();
					 String sort_index = StringUtil.isEmpty(jsonArray.getJSONObject(j).getString("sort_index")) ? "0" : jsonArray.getJSONObject(j).getString("sort_index");
					 author.setType(Constant.PROCESS_TYPE_AUTHOR);
					 author.setExecutor_id(jsonArray.getJSONObject(j).getString("user_id"));
					 author.setExecutor_name(jsonArray.getJSONObject(j).getString("user_nm"));
					 author.setSort_index(Integer.parseInt(sort_index));
					 ret.add(author);
				 }
			}
		}
		
		// 공동 작성자
		if(map.get("coauthorList") != null && !map.get("coauthorList").toString().equals(""))	{
			
			JSONArray jsonArray = JSONArray.fromObject(map.get("coauthorList") );
			
			if(jsonArray.size() > 0 ) {		
				
				 for(int j=0;j < jsonArray.size();j++)	{		
					 ProcessExecutorVO coAuthor = new ProcessExecutorVO();
					 String sort_index = StringUtil.isEmpty(jsonArray.getJSONObject(j).getString("sort_index")) ? "0" : jsonArray.getJSONObject(j).getString("sort_index");
					 coAuthor.setType(Constant.PROCESS_TYPE_COAUTHOR);
					 coAuthor.setExecutor_id(jsonArray.getJSONObject(j).getString("user_id"));
					 coAuthor.setExecutor_name(jsonArray.getJSONObject(j).getString("user_nm"));
					 coAuthor.setSort_index(Integer.parseInt(sort_index));
					 
					 ret.add(coAuthor);
				 }
			}
		}
		
		// 승인자
		if(map.get("approverList") != null && !map.get("approverList").toString().equals(""))	{
			
			JSONArray jsonArray = JSONArray.fromObject(map.get("approverList") );
			
			if(jsonArray.size() > 0 ) {		
				
				 for(int j=0;j < jsonArray.size();j++)	{		
					 ProcessExecutorVO applover = new ProcessExecutorVO();
					 String sort_index = StringUtil.isEmpty(jsonArray.getJSONObject(j).getString("sort_index")) ? "0" : jsonArray.getJSONObject(j).getString("sort_index");
					 applover.setType(Constant.PROCESS_TYPE_APPROVER);
					 applover.setExecutor_id(jsonArray.getJSONObject(j).getString("user_id"));
					 applover.setExecutor_name(jsonArray.getJSONObject(j).getString("user_nm"));
					 applover.setSort_index(Integer.parseInt(sort_index));
					 
					 ret.add(applover);
				 }
			}
		}
		
		// 수신자
		if(map.get("receiverList") != null && !map.get("receiverList").toString().equals(""))	{
			
			JSONArray jsonArray = JSONArray.fromObject(map.get("receiverList") );
			
			if(jsonArray.size() > 0 ) {		
				
				 for(int j=0;j < jsonArray.size();j++)	{		
					 ProcessExecutorVO receiver = new ProcessExecutorVO();
					 String sort_index = StringUtil.isEmpty(jsonArray.getJSONObject(j).getString("sort_index")) ? "0" : jsonArray.getJSONObject(j).getString("sort_index");
					 receiver.setType(Constant.PROCESS_TYPE_RECEIVER);
					 receiver.setExecutor_id(jsonArray.getJSONObject(j).getString("user_id"));
					 receiver.setExecutor_name(jsonArray.getJSONObject(j).getString("user_nm"));
					 receiver.setSort_index(Integer.parseInt(sort_index));
					
					 ret.add(receiver);
				 }
			}
		}
		
		return ret;
	}
	
	
	public static List<AclExItemVO> jsonArrayToExAclItemList(HashMap<String,Object> map){
		
		List<AclExItemVO> ret = new ArrayList<AclExItemVO>();

		if(map.get("aclExItem_list") != null && !map.get("aclExItem_list").toString().equals(""))	{
			
			JSONArray jsonArray = JSONArray.fromObject(map.get("aclExItem_list") );
			
			if(jsonArray.size() > 0 ) {		
				
				 for(int j=0;j < jsonArray.size();j++)	{		
					 AclExItemVO tempAclExIemVO = new AclExItemVO();
					 tempAclExIemVO.setAccessor_id(jsonArray.getJSONObject(j).getString("accessor_id").toString());
					 tempAclExIemVO.setAccessor_isgroup(jsonArray.getJSONObject(j).getString("accessor_isgroup").toString());
					 tempAclExIemVO.setAccessor_isalias(jsonArray.getJSONObject(j).getString("accessor_isalias").toString());
					 // only JDK1.7
					 switch (jsonArray.getJSONObject(j).getString("doc_default_acl").toString()) {
					 case Constant.ACL_DELETE:
						tempAclExIemVO.setAct_browse(Constant.T);tempAclExIemVO.setAct_read(Constant.T);
						tempAclExIemVO.setAct_update(Constant.T);tempAclExIemVO.setAct_delete(Constant.T);
						break;
					 case Constant.ACL_UPDATE:
						tempAclExIemVO.setAct_browse(Constant.T);tempAclExIemVO.setAct_read(Constant.T);
						tempAclExIemVO.setAct_update(Constant.T);tempAclExIemVO.setAct_delete(Constant.F);
						break;
					 case Constant.ACL_READ:
						tempAclExIemVO.setAct_browse(Constant.T);tempAclExIemVO.setAct_read(Constant.T);
						tempAclExIemVO.setAct_update(Constant.F);tempAclExIemVO.setAct_delete(Constant.F);
						break;
					 case Constant.ACL_BROWSE:
						tempAclExIemVO.setAct_browse(Constant.T);tempAclExIemVO.setAct_read(Constant.F);
						tempAclExIemVO.setAct_update(Constant.F);tempAclExIemVO.setAct_delete(Constant.F);
						break;
					 default:
						tempAclExIemVO.setAct_browse(Constant.F);tempAclExIemVO.setAct_read(Constant.F);
						tempAclExIemVO.setAct_update(Constant.F);tempAclExIemVO.setAct_delete(Constant.F);
						break;
					 }
					 
					 tempAclExIemVO.setAct_create(jsonArray.getJSONObject(j).getString("doc_act_create").toString());
					 tempAclExIemVO.setAct_cancel_checkout(jsonArray.getJSONObject(j).getString("doc_act_cancel_checkout").toString());
					 tempAclExIemVO.setAct_change_permission(jsonArray.getJSONObject(j).getString("doc_act_change_permission").toString());
					 
					 ret.add(tempAclExIemVO);
				 }
			}
		}
		
		return ret;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 파일명의 확장자 구하기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getFileExtension
	 * @param file_name
	 * @return String
	 */
	public static String getFileExtension(String file_name) {
		
		String ext = "";
		
		int ext_index = file_name.lastIndexOf(".");
		
		if (ext_index > 0) {
			ext = file_name.substring(ext_index + 1, (file_name.length()));
			
			if(ext.length() > 6){
				ext = "";
			}
		}
		
		return ext;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : Client 에 넘겨줄 공통 세션객체 리스트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setSessionToModel
	 * @param model
	 * @param sessionVO void
	 */
	public static void setSessionToModel(Model model, SessionVO sessionVO) {
		
		model.addAttribute("contextRoot",sessionVO.getSessContextRoot());
		model.addAttribute("user_id",sessionVO.getSessId());
		model.addAttribute("user_name",sessionVO.getSessName());
		model.addAttribute("group_id",sessionVO.getSessGroup_id());
		model.addAttribute("theme",sessionVO.getSessTheme());				// 테마=SKIN
		model.addAttribute("pageSize",sessionVO.getSessPage_size());
		
		// 권한 레벨 체크(시스템관리자,전사,하위부서포함,부서,작성자)
		String aclMenuPart;
		if(sessionVO.getSessRole_id().equals(Constant.SYSTEM_ROLE)) {
			aclMenuPart = Constant.SYSTEM_ROLE;
		} else if(sessionVO.getSessRole_id().equals(Constant.USER_ROLE)) {
			aclMenuPart = Constant.USER_ROLE;
		} else {
			aclMenuPart = CommonUtil.getMenuPart(sessionVO, Constant.USER_ACL_MENU_CODE);
		}			
		model.addAttribute("acl_menu_part", aclMenuPart);
		model.addAttribute("manage_group_id", sessionVO.getSessManage_group());
		model.addAttribute("manage_group_nm", sessionVO.getSessManage_group_nm());
		model.addAttribute("user_email", sessionVO.getSessEmail());
		
		// File upload 관련
		// 파일첨부 기본값 :: 환경설정에서 사용안함 옵션 적용시
		model.addAttribute("defaultFileCnt",ConfigData.getInt("DOC.DEFAULT.FILECNT"));
		model.addAttribute("defaultFileSize",ConfigData.getInt("DOC.DEFAULT.FILESIZE"));
		model.addAttribute("defaultFileTotal",ConfigData.getInt("DOC.DEFAULT.TOTAL"));
		
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  에러페이지에 넘겨줄 메세지 정의
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setErrorMsg
	 * @param model
	 * @param errorCode
	 * @param errorMessage void
	 */
	public static void setErrorMsg(Model model,String errorCode,String errorMessage,String contextRoot) {
		
		model.addAttribute("errorCode",errorCode);
		model.addAttribute("errorMessage",errorMessage);
		model.addAttribute("contextRoot",contextRoot);
	}
	
	/**
	 * 1. 개요 : Client 에 넘겨줄 문서 객체 리스트
	 * 2. 처리내용 :  
	 * @param model
	 * @param resultMap
	 * @param sessionVO
	 */
	public static void docSessionToModel(Model model, Map<String, Object> resultMap,SessionVO sessionVO) {
	
		@SuppressWarnings("rawtypes")
		Set set = resultMap.keySet(); 
		Object[] argArray = set.toArray(); 
		
		//[position, FILETOTAL, FILESIZE, sercurity, FILECNT, typeList, EXT, preservation_year]
		for( int i = 0; i < argArray.length; i++ ){				
			
			String key = (String)argArray[i];
			
			// 파일확장자인경우 문자열 가공 처리 후 전달함
			if(key.equals(Constant.FILE_EXT))	{
				CaseInsensitiveMap caseMap = (CaseInsensitiveMap)resultMap.get(key);				
				String value = caseMap.get("fval").toString();			
				caseMap.put("fval",value.substring(0,value.length()-1).replaceAll(";",","));		
				model.addAttribute(key,caseMap);
			}else {
				Object value = resultMap.get(key);
				model.addAttribute(key,value);
			}
		}        
		
		// setSessionToModel로 이동
/*		// 파일첨부 기본값 :: 환경설정에서 사용안함 옵션 적용시
		model.addAttribute("defaultFileCnt",ConfigData.getInt("DOC.DEFAULT.FILECNT"));
		model.addAttribute("defaultFileSize",ConfigData.getInt("DOC.DEFAULT.FILESIZE"));
		model.addAttribute("defaultFileTotal",ConfigData.getInt("DOC.DEFAULT.TOTAL"));*/
		model.addAttribute("defaultRefDocCnt",ConfigData.getInt("DOC.REF.FILECNT"));
		
		// 문서검색기간 기본설정
		model.addAttribute("startDt",sessionVO.getSessStartDt());
		model.addAttribute("endDt",sessionVO.getSessEndDt());
		model.addAttribute("pageSize",sessionVO.getSessPage_size()); // 환경설정 기본 리스트 사이즈
		
		model.addAttribute("versionInfo",ConfigData.getString("VERSION_INFO"));		// 제품타입
		
	}
	
	/**
	 * 문서상세검색 조건 CallBack 함수
	 * @param sessionVO
	 * @param req
	 * @param map
	 * @param cacheService
	 * @param folderService
	 * @param documentService
	 * @param param
	 */
	public static void docDetailSearch(SessionVO sessionVO,HttpServletRequest req,HashMap<String,Object> map,CacheService cacheService,
			FolderService folderService,DocumentService documentService,HashMap<String,Object> param) {
		
		List<String> folder_id_list = new ArrayList<String>(); 						// 하위폴더리스트(하위폴더 포함)
		List<AttrVO>  attrList = new ArrayList<AttrVO>();							// 확장문서유형 속성리스트
		
		String[] group_id_list = sessionVO.getSessProjectGroup().toArray(new String[sessionVO.getSessProjectGroup().size()+1]);		// 그룹+프로젝트 그룹 ID
		String folder_menu_part = CommonUtil.getMenuPart(sessionVO, Constant.USER_FOLDER_MENU_CODE);	// 폴더관리 권한(ALL/GROUP/TEAM) - CREATOR가 아닌경우
		String document_menu_part = CommonUtil.getMenuPart(sessionVO, Constant.USER_DOC_MENU_CODE);	// 문서관리 권한 (ALL/GROUP/TEAM) - CREATOR가 아닌경우
		
		String folder_id =  map.get("folder_id") != null ? map.get("folder_id").toString() : "";									// 선택폴더ID (내소유문서/내수정중인문서/내만기문서/휴지통 해당 사항없음)
		String is_extended =  map.get("is_extended") != null ? map.get("is_extended").toString() : "";						// 확장문서유형여부(T/F)
		String doc_type = map.get("doc_type") != null ?map.get("doc_type").toString() : "";									// 선택한 문서유형명
		String child_include = map.get("child_include") != null ?map.get("child_include").toString() : "";					// 하위폴더포함 여부	
		String acl_check = Constant.RESULT_TRUE;																							// ACL 권한 체크 여부 false 이면 skipped	
		
		boolean isFolderMenuPart = false;																									// 하위폴더권한 여부
		
		try {
			
			// 0.그룹(부서그룹/프로젝트그룹)
			group_id_list[sessionVO.getSessProjectGroup().size()] = sessionVO.getSessGroup_id();
			param.put("group_id_list", group_id_list);
			
			// 1. 문서권한관리자인 경우 && 하위폴더 체크된 경우
			if(!StringUtil.isEmpty(child_include) && child_include.equals("on")) {
				
				if(document_menu_part.equals(Constant.MENU_ALL)){
					// 전체문서 관리자인 경우 권한체크 안함
					acl_check =  Constant.RESULT_FALSE;
					isFolderMenuPart = true;
				}else if(document_menu_part.equals(Constant.MENU_GROUP) || document_menu_part.equals(Constant.MENU_TEAM)){
					// 하위부서포함, 소속부서 관리는 관리부서 ID일 경우 acl_check 안함				
					if(cacheService.menuAuthByFolderID(folder_id, sessionVO.getSessManage_group())){
						acl_check = Constant.RESULT_FALSE;
						isFolderMenuPart = true;
					}				
				}
				
				folder_id_list = folderService.childFolderIdsByfolderId(folder_id, isFolderMenuPart ? folder_menu_part : "");
			}
			
			// 2.확장문서 상세 검색 처리
			if(is_extended.equals(Constant.T) && !StringUtil.isEmpty(doc_type)) {
				
				attrList = documentService.extendedAttrListByDocType(req, doc_type);
				param.put("attrList", attrList);																// 확장문서유형 속성리스트			
				param.put("tbl_name", Constant.TABLE_PREFIX + doc_type); 					// 확장문서유형 테이블명
				
			}			
			
			// 3.선택폴더ID (내소유문서/내수정중인문서/내만기문서/휴지통 해당 사항없음)
			if(!StringUtil.isEmpty(folder_id))	{
				folder_id_list.add(folder_id);		
			}
			
			param.put("folder_id_list", folder_id_list);
			param.put("document_menu_part",document_menu_part);
			param.put("manage_group_id", sessionVO.getSessManage_group());
			param.put("acl_check",acl_check);
			param.put("is_extended",is_extended);
			
		}catch(Exception e){			
			logger.error(e);
		}
		
	}
	
	
	public static String getMenuPart(SessionVO sessionVO, String menu_code) {
		List<MenuAuthVO> menu_list = sessionVO.getSessMenuAuth();
		
		for(MenuAuthVO menu_auth : menu_list){
			if(!StringUtil.isEmpty(menu_auth.getMenu_cd()) && menu_auth.getMenu_cd().equals(menu_code)){
				return menu_auth.getPart();
			}
		}
		return "";
	}
		

	/**
	 * 
	 * <pre>
	 * 1. 개용 : Excel Download 목록 리스트 가져오기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getExcelList
	 * @param resultMap
	 * @return Object
	 */
	public static void getExcelList(Map<String, Object> resultMap,Model model) {
		
		Object value = null;
		
		@SuppressWarnings("rawtypes")
		Set set = resultMap.keySet(); 
		Object[] argArray = set.toArray(); 
		
		for( int i = 0; i < argArray.length; i++ ){			
			String key = (String)argArray[i];
			if(key.equals(Constant.EXCEL_LIST))	{
				value = resultMap.get(key);
				model.addAttribute(key,value);
			}
		}

	}

	/**
	 * 
	 * <pre>
	 * 1. 개용 : JfreeChart 그래프 목록 리스트 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getChartList
	 * @param resultMap
	 * @param model void
	 */
	public static void getChartList(Map<String, Object> resultMap,HashMap<String,Object> model) {
		
		Object value = null;
		
		@SuppressWarnings("rawtypes")
		Set set = resultMap.keySet(); 
		Object[] argArray = set.toArray(); 
		
		for( int i = 0; i < argArray.length; i++ ){			
			String key = (String)argArray[i];
			if(key.equals(Constant.EXCEL_LIST))	{
				value = resultMap.get(key);
				model.put(key,value);
			}
		}

	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  주어진 VO 객체의 멤버들을 Key, Value의 형식의 HashMap으로 얻는다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getMemberFields
	 * @param vo
	 * @return
	 * @throws Exception HashMap<String,Object>
	 */
	public static HashMap<String, Object> getMemberFields(VO vo) throws Exception {
		
		HashMap<String, Object> members = new HashMap<String, Object>();
		
		if(vo != null) {
			
			Class<? extends Object> c = vo.getClass();
			Method [] methods = c.getMethods();
			
			for (int i = 0; i < methods.length; i++) {
				
				String methodName = methods[i].getName();
			
				if (!methodName.equals("getClass") && methodName.subSequence(0, 3).equals("get")) {
					
					String fieldName = methodName.substring(3);
					Object fieldValue = methods[i].invoke(vo, new Object[]{});

					members.put(fieldName.toLowerCase(), fieldValue);
				}
			}
		}

		return members;		
	}

	/**
	 * 선택조건에 따른 검색대상 컬럼명값 설정처리
	 * @param inMap
	 * @param param
	 */
	public static void setColumNm(HashMap<String,Object> inMap,HashMap<String, Object> param) {
		
		// strIndex에 따른 검색조건 추가 strKeyword1
		switch(StringUtil.getMapString(inMap, "strIndex")) {
		
			case "doc_name":
				param.put("strIndexColumn","D.DOC_NAME");
				break;
			case "doc_description":
				param.put("strIndexColumn","D.DOC_DESCRIPTION");
				break;
			case "creator_name":
				param.put("strIndexColumn","D.CREATOR_NAME");
				break;
			case "author_list":
				param.put("strIndexColumn","D.AUTHOR_LIST");
				break;
			case "keyword":
				param.put("strIndexColumn","D.KEYWORD");
				break;							
		}
		
	}
	
	
	/**
	 * 문서목록 엑셀다운로드 설정값 처리
	 * @param model
	 * @param listType
	 */
	public static void setExcelFormat(Model model,String listType) {
		
		String[] members = null;
		String[] cell_headers = null;
		int[] cell_widths = null;
		
		if(Constant.DOCUMENT_LIST_TYPE_EXPIRED.equals(listType)){
			// 내 만기 문서
			members = new String[]{"doc_name", "type_name", "version_no", "owner_name", "create_date", "expired_date"};
			cell_headers = new String[]{"문서명", "문서유형", "버전", "소유자", "등록일", "만기일"};
			cell_widths = new int[]{50, 20, 20, 20, 30, 30};
		} else if(Constant.DOCUMENT_LIST_TYPE_TRASHCAN.equals(listType)) {
			// 개인 휴지통 문서
			members = new String[]{"doc_name","type_name","version_no","creator_name","create_date","deleter_name","delete_date","owner_name"};
			cell_headers = new String[]{"문서명", "문서유형", "버전", "등록자", "등록일", "삭제자", "삭제일", "소유자"};
			cell_widths = new int[]{50, 20, 20, 20, 20, 20, 20, 20};
		} else {
			// 그외 문서리스트(업무문서함/개인문서함)
			members = new String[]{"doc_name","type_name","version_no","creator_name","create_date"};
			cell_headers = new String[]{"문서명", "문서유형", "버전", "등록자", "등록일"};
			cell_widths = new int[]{50, 20, 20, 20, 20};
		}
		
		model.addAttribute("members",members);
		model.addAttribute("cell_headers",cell_headers);
		model.addAttribute("cell_widths",cell_widths);
		model.addAttribute("fileName","downLoad.xls");	
		
	}
	
	/**
	 * 나의문서 목록 검색조건 설정값 처리
	 * @param listType
	 * @param param
	 */
	public static void setSearchColumn(String listType,HashMap<String, Object> param) {
		
		switch(listType) {
		
			case Constant.DOCUMENT_LIST_TYPE_TRASHCAN :		// 개인휴지통(권한체크 필요없음)
				param.put("dateColumn","D.DELETE_DATE");
				param.put("userColumn","D.DELETER_ID");
				param.put("acl_check",Constant.RESULT_FALSE);
				param.put("doc_status",Constant.DOC_STATUS_DELETE);
				param.put("is_expired",Constant.F);
				break;
			case Constant.DOCUMENT_LIST_TYPE_CHECKOUT :		// 내수정중문서(권한체크 필요없음)
				param.put("dateColumn","D.LOCK_DATE");
				param.put("userColumn","D.LOCK_OWNER");
				param.put("acl_check",Constant.RESULT_FALSE);
				param.put("doc_status",Constant.DOC_STATUS_CREATE);
				param.put("is_expired",Constant.F);
				param.put("is_locked",Constant.T);
				break;	
			case Constant.DOCUMENT_LIST_TYPE_EXPIRED :			// 내만기문서(권한체크 필요없음)
				param.put("dateColumn","D.EXPIRED_DATE");
				param.put("userColumn","D.CREATOR_ID");
				param.put("acl_check",Constant.RESULT_FALSE);
				param.put("doc_status",Constant.DOC_STATUS_CREATE);
				param.put("is_expired",Constant.T);
				break;	
			case Constant.DOCUMENT_LIST_TYPE_OWNER :				// 내소유문서(권한체크 필요없음)
				param.put("dateColumn","D.CREATE_DATE");
				param.put("userColumn","D.OWNER_ID");
				param.put("acl_check",Constant.RESULT_FALSE);
				param.put("doc_status",Constant.DOC_STATUS_CREATE);
				param.put("is_expired",Constant.F);
				break;	
			case Constant.DOCUMENT_LIST_TYPE_SHARE :				// 공유문서(권한체크함)
				param.put("dateColumn","D.CREATE_DATE");
				param.put("doc_status",Constant.DOC_STATUS_CREATE);
				param.put("is_expired",Constant.F);
				param.put("is_share",Constant.T);
				break;		
			case Constant.DOCUMENT_LIST_TYPE_TEMPDOC :			// 임시문서(권한체크함)
				param.put("dateColumn","D.CREATE_DATE");
				param.put("userColumn","TD.USER_ID");
				param.put("doc_status",Constant.DOC_STATUS_CREATE);
				param.put("is_expired",Constant.F);					
				break;
			case Constant.DOCUMENT_LIST_TYPE_RECENTLYDOC :    // 최신문서함(권한체크함)
				param.put("dateColumn","D.CREATE_DATE");
				//param.put("userColumn","D.OWNER_ID");
				param.put("doc_status",Constant.DOC_STATUS_CREATE);
				param.put("is_expired",Constant.F);					
				break;
			default :
				param.put("dateColumn","D.CREATE_DATE");
				param.put("doc_status",Constant.DOC_STATUS_CREATE);
				param.put("is_expired",Constant.F);
				break;
			}
		
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : DB에 저장할 date type 
	 * 2. 처리내용 : 현재 시간
	 * </pre>
	 * @Method Name : getCurruentTime
	 * @return java.sql.Date
	 */
	public static java.sql.Date getCurruentTime(){
		return new java.sql.Date(new java.util.Date().getTime());
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : DB에 저장할 date type 
	 * 2. 처리내용 : 입력된 시간
	 * </pre>
	 * @Method Name : getCurruentTimeByDate
	 * @param date
	 * @return
	 * @throws Exception java.sql.Date
	 */
	public static java.sql.Date getCurruentTimeByDate(String date) throws Exception{
		DateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = dateFormat.parse(date);
		return new java.sql.Date(currentDate.getTime());
	}
}

