package kr.co.exsoft.rgate.service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import net.sf.json.JSONArray;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.eframework.library.ExsoftAbstractServiceImpl;

import org.apache.commons.collections.map.CaseInsensitiveMap;

import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.UtilFileApp;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.rgate.vo.*;
import kr.co.exsoft.rgate.dao.RgateDao;

/**
 * Rgate 서비스 구현 부분
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Service("rgateService")
public class RgateServiceImpl extends ExsoftAbstractServiceImpl implements RgateService {

	@Autowired
	@Qualifier("sqlSession")
	private SqlSession sqlSession;
		
	@Autowired
	@Qualifier("sqlSessionBatch")
	private SqlSession sqlSessionBatch;
	
	@Override
	public Map<String, Object> rgatePageList(HashMap<String,Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<RGateMappingVO>  ret = new ArrayList<RGateMappingVO>();
		int total = 0;
		
		RgateDao rgateDao = sqlSession.getMapper(RgateDao.class);
		
		total = rgateDao.rgatePolicyPagingCount(map);
		ret = rgateDao.rgatePolicyPagingList(map);
		
		resultMap.put("page",map.get("nPage").toString());
		resultMap.put("records",total);
		resultMap.put("total",CommonUtil.getTotPageSize(total,Integer.parseInt(map.get("page_size").toString())));	
		resultMap.put("list",ret);
		
		return resultMap;
		
	}
	
	@Override
	public Map<String, Object> rgateExtProcList(HashMap<String,Object> map,SessionVO sessionVO) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<RGateListVO>  ret = new ArrayList<RGateListVO>();
		List<RGateListVO>  listVO = new ArrayList<RGateListVO>();

		List<String> strRet = new ArrayList<String>();
		String manageName = "";
		
		RgateDao rgateDao = sqlSession.getMapper(RgateDao.class);
		
		listVO = rgateDao.rgateList(map);
		resultMap.put("result",Constant.RESULT_FALSE);
		resultMap.put("records",listVO.size());
		
		if(map.get("manage_type").equals(Constant.MANAGE_EXT))	{					// 확장자인 경우 

			for(RGateListVO rGateListVO : listVO) {		
								
				String[] fileType = UtilFileApp.getFileType(rGateListVO.getManage_name());				
				// ContextRoot 적용처리
				String ext = "<img src='"+ sessionVO.getSessContextRoot() +"/img/extension/" + fileType[1] + "'/>";
				String blank = "";
				
				// 확장자 자리수에 따른 정렬처리
				switch(rGateListVO.getManage_name().length()) {
					
					case 2:
						blank = "&nbsp;&nbsp;&nbsp;";
						break;
					case 3:
						blank = "&nbsp;&nbsp;";
						break;
					default :
						blank = "&nbsp;";
						break;
				}
				
				rGateListVO.setExt_icon(ext + blank + rGateListVO.getManage_name());
				
				ret.add(rGateListVO);
			}
			
			resultMap.put("list",ret);
			
		} 
		else if(map.get("manage_type").equals(Constant.MANAGE_PROC)){			// 프로세스인 경우
			
			for(RGateListVO rGateListVO : listVO){
				rGateListVO.setManage_name(rGateListVO.getManage_name());
				ret.add(rGateListVO);
				
				manageName = rGateListVO.getManage_name() != null ? rGateListVO.getManage_name().toString() : "";
				strRet.add(manageName);				
			}
			
			manageName = "";
			
			resultMap.put("list", ret);
			resultMap.put("procDBList", strRet);
			resultMap.put("result",Constant.RESULT_TRUE);
		}
		else {		// IP주소인 경우
			
			for(RGateListVO rGateListVO : listVO) {
				
				rGateListVO.setManage_name(rGateListVO.getManage_name());
				ret.add(rGateListVO);
				
				manageName = rGateListVO.getManage_name() != null ? rGateListVO.getManage_name().toString() : "";
				strRet.add(manageName);
			}
			
			manageName = "";
			
			resultMap.put("list",ret);
			resultMap.put("ipDBList", strRet);
			resultMap.put("result", Constant.RESULT_TRUE);
		}
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> rgatePolicyManageWrite(List<RGateMappingVO> rgateMappingVO,HashMap<String,Object> map) 
			throws Exception {
		
		RgateDao rgateDao = sqlSession.getMapper(RgateDao.class);
		
		HashMap<String, Object> param = new HashMap<String, Object>();		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result",Constant.RESULT_FALSE);
		
		String workType = map.get("work_type").toString(); 
		int result = 0;
		
		for(RGateMappingVO ret : rgateMappingVO) {			
						
			param.put("ckey",ret.getCkey());
						
			// 개별 페이지 처리 항목 START
			// 1. LSC_EXTENSION : EXCEPTION
			if(workType.equals(Constant.LSC_EXTENSION))	{
				param.put("cvalue",ret.getExtension());
			}
			// 2. LSC_EPROC : PROCESS
			else if(workType.equals(Constant.LSC_EPROC)) {
				param.put("cvalue", ret.getProcess());
			}
			// 3. LSC_CONTROL : IS_UNACTIVE
			else if(workType.equals(Constant.LSC_CONTROL))	{
				param.put("cvalue",ret.getIs_unactive());
			}
			// 4. LSC_ENABLE_USB : IS_USB_ACTIVE
			else if(workType.equals(Constant.LSC_ENABLE_USB)) {
				param.put("cvalue", ret.getIs_usb_active());
			}	
			// 5. LSC_NETDRIVE_ADDR : IP_ADDRESS
			else if(workType.equals(Constant.LSC_NETDRIVE_ADDR)) {
				param.put("cvalue", ret.getIp_address());
			}
			// 6. RGC_UNINSTALL_PASS : PASSWD
			else if(workType.equals(Constant.RGC_UNINSTALL_PASS)) {
				param.put("cvalue", ret.getPasswd());
			}
						
			// 개별 페이지 처리 항목 END
						
			result = rgateDao.rgateMappingWrite(ret);
			if(result == 0)	{	throw processException("common.system.error");	}
			
			result = rgateDao.rgatePolicyWrite(param);
			if(result == 0)	{	throw processException("common.system.error");	}		
		}
					
		resultMap.put("result",Constant.RESULT_TRUE);
		return resultMap;		
	}
	
	@Override
	public Map<String, Object> rgatePolicyManageDelete(List<HashMap<String,Object>> delList,HashMap<String,Object> map) 
			throws Exception {
		
		RgateDao rgateDao = sqlSession.getMapper(RgateDao.class);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result",Constant.RESULT_FALSE);
		int result = 0;
		
		// 저장금지 삭제는 공통으로 사용한다.
		for(HashMap<String,Object> ret : delList) {			
		
			result = rgateDao.rgateMappingDelete(ret);
			if(result == 0)	{	throw processException("common.system.error");	}				
			
			result = rgateDao.rgatePolicyDelete(ret);
			if(result == 0)	{	throw processException("common.system.error");	}				
		}
		
		resultMap.put("result",Constant.RESULT_TRUE);
		return resultMap;		
	}
	
	@Override
	public Map<String, Object> rgateListManager(List<RGateListVO> rgateMappingVO,HashMap<String,Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result",Constant.RESULT_FALSE);
		
		return resultMap;
		
	}
	
	@Override
	public List<RGateMappingVO> setRgateMappingList(HashMap<String,Object> map) throws Exception{
		
		List<RGateMappingVO> ret = new ArrayList<RGateMappingVO>();
		
		// 사용자/부서=GROUP#GRP000000000003,GROUP#GRP000000000002,
		// 확장자 목록=ppt,pptx,rtf,
		
		String idList = map.get("idList") != null ? map.get("idList").toString() : "";		
		String workType = map.get("work_type") != null ? map.get("work_type").toString() : "";
		String ckey = "";
		String userType = "";
		String userId = "";
		String extList = "";
		String procList = "";
		String ipList = "";
		String pwList = "";
		
		// 공통 유효성 체크
		if(workType.equals("") ||  idList.equals(""))	{
			throw processException("common.required.error");
		}
		
		// 개별 페이지 처리 항목 START
		
		// 1. LSC_EXTENSION : EXCEPTION
		if(workType.equals(Constant.LSC_EXTENSION))	{
			extList = map.get("extList") != null ? map.get("extList").toString() : "";		
			extList = extList.replaceAll(",",";");		// EXTENSION 구분자(;)		
			if(extList.equals("") )	{
				throw processException("common.required.error");
			}
		}
		// 2. LSC_EPROC : PROCESS
		else if(workType.equals(Constant.LSC_EPROC)) {
			procList = map.get("procList") != null ? map.get("procList").toString() : "";
			procList = procList.replaceAll(",", ";");
			if(procList.equals("")){
				throw processException("common.required.error");
			}
		}
		// 3. LSC_CONTROL : IS_UNACTIVE
		else if(workType.equals(Constant.LSC_CONTROL))	{
			// NOTING
		}
		// 4. LSC_ENABLE_USB : IS_USB_ACTIVE
		else if(workType.equals(Constant.LSC_ENABLE_USB)) {
			//NOTING
		}
		// 5. LSC_NETDRIVE_ADDR : IP_ADDRESS
		else if(workType.equals(Constant.LSC_NETDRIVE_ADDR)) {
			ipList = map.get("ipList") != null ? map.get("ipList").toString() : "";
			ipList = ipList.replaceAll(",", ";");
			if(ipList.equals("")) {
				throw processException("common.required.error");
			}
		}
		// 6. RGC_UNINSTALL_PASS : PASSWD
		else if(workType.equals(Constant.RGC_UNINSTALL_PASS)) {
			pwList = map.get("pwList") != null ? map.get("pwList").toString() : "";
			if(pwList.equals("")) {
				throw processException("common.required.error");
			}
		}
				
		// 개별 페이지 처리 항목 START
		
		String[] resultId = idList.split("[,]");
		
		for(int i=0;i<resultId.length;i++) {
			
			String[] dbVal = resultId[i].split("[#]");
			
			RGateMappingVO rGateMappingVO = new RGateMappingVO();
						
			if(dbVal[0].equals(Constant.IS_ALL)) {
				ckey = workType +"_" + Constant.IS_ALL;
				userType = Constant.IS_ALL;
				userId = Constant.IS_ALL;
			}else {
				ckey = workType +"_" + resultId[i].replaceAll("#","_");
				userType = dbVal[0];
				userId = dbVal[1];
			}
			
			rGateMappingVO.setCkey(ckey.toUpperCase());		// UPPER CASE
			rGateMappingVO.setWork_type(map.get("work_type").toString());
			rGateMappingVO.setUser_type(userType);
			rGateMappingVO.setUser_id(userId);	
			
			// 개별 페이지 처리 부분 START
			// 1. LSC_EXTENSION : EXCEPTION
			if(workType.equals(Constant.LSC_EXTENSION))	{
				rGateMappingVO.setExtension(extList);
			}
			// 2. LSC_EPROC : PROCESS
			else if(workType.equals(Constant.LSC_EPROC)) {
				rGateMappingVO.setProcess(procList);
			}
			// 3. LSC_CONTROL : IS_UNACTIVE
			else if(workType.equals(Constant.LSC_CONTROL))	{
				rGateMappingVO.setIs_unactive(Constant.IS_OFF);
			}
			// 4. LSC_ENABLE_USB : IS_USB_ACTIVE
			else if(workType.equals(Constant.LSC_ENABLE_USB)) {
				rGateMappingVO.setIs_usb_active(Constant.IS_ON);
			}
			// 5. LSC_NETDRIVE_ADDR : IP_ADDRESS
			else if(workType.equals(Constant.LSC_NETDRIVE_ADDR)) {
				rGateMappingVO.setIp_address(ipList);
			}
			// 6. RGC_UNINSTALL_PASS : PASSWD	
			else if(workType.equals(Constant.RGC_UNINSTALL_PASS)) {
				rGateMappingVO.setPasswd(pwList);
			}
			
			
			
			
			// 개별 페이지 처리 부분 END
			
			ret.add(rGateMappingVO);
			
			ckey = "";
			userType = "";
		}
		
		return ret;
	}
	
	@Override
	public Map<String, Object> rgateMappingList(HashMap<String,Object> map) throws Exception {
		
		List<CaseInsensitiveMap> mappingList = new ArrayList<CaseInsensitiveMap>();
		List<String> ret = new ArrayList<String>();
		String workType = map.get("work_type") != null ? map.get("work_type").toString() : "";
		String user_type = "";
		String unqiueId = "";
		
		RgateDao rgateDao = sqlSession.getMapper(RgateDao.class);
		
		// 공통 유효성 체크
		if(workType.equals(""))	{
			throw processException("common.required.error");
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result",Constant.RESULT_FALSE);
		
		mappingList = rgateDao.rgatePolicyList(map);		
		
		for(CaseInsensitiveMap caseMap : mappingList) {
			
			user_type = caseMap.get("user_type") != null ? caseMap.get("user_type").toString() : ""; 
						
			// 파라미터 설정하기
			if(user_type.equals(Constant.IS_GROUP)) {
				unqiueId = caseMap.get("group_id") != null ? caseMap.get("group_id").toString() : ""; 
			} else if(user_type.equals(Constant.IS_USER)) {
				unqiueId = caseMap.get("user_id") != null ? caseMap.get("user_id").toString() : ""; 
			}
				
			// 클라이언트에 넘겨질 값 정의
			if(user_type.equals(Constant.IS_ALL))	{
				ret.add(user_type);
			}else {
				ret.add(user_type+"#"+unqiueId);				
			}
			
			unqiueId = "";
			user_type = "";
		}
		
		resultMap.put("mappingList",ret);
		resultMap.put("result",Constant.RESULT_TRUE);
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> rgateExceptionList(HashMap<String,Object> map) throws Exception {
	
		RgateDao rgateDao = sqlSession.getMapper(RgateDao.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<RGateProcessVO> mappingList = new ArrayList<RGateProcessVO>();
		List<String> ret = new ArrayList<String>();
		mappingList = rgateDao.rgateProcessList(map);
		
		for(RGateProcessVO vo : mappingList) {			
			ret.add(vo.getProcess());
		}
		
		resultMap.put("mappingList",ret);
		resultMap.put("result",Constant.RESULT_TRUE);
		
		return resultMap;
	}
	
	@Override
	public List<HashMap<String,Object>> setRgateMappingDelList(HashMap<String,Object> map) throws Exception {
		
		List<HashMap<String,Object>> ret = new ArrayList<HashMap<String,Object>>();
		
		
		String workType = map.get("work_type") != null ? map.get("work_type").toString() : "";
		String ckeyList = map.get("ckeyList") != null ? map.get("ckeyList").toString() : "";		
		
		// 공통 유효성 체크
		if(workType.equals("") ||  ckeyList.equals(""))	{
			throw processException("common.required.error");
		}
		
		String[] resultId = ckeyList.split("[,]");
		
		for(String ckey : resultId) {
			
			HashMap<String, Object> dMap = new HashMap<String, Object>();		
			dMap.put("ckey",ckey);
			
			ret.add(dMap);
		}
		
		return ret;
	}
	
	@Override
	public List<HashMap<String,Object>> setRgateMappingUpdateList(HashMap<String,Object> map) throws Exception {
		
		List<HashMap<String,Object>> ret = new ArrayList<HashMap<String,Object>>();
		
		String idList = map.get("idList") != null ? map.get("idList").toString() : "";		
		String workType = map.get("work_type") != null ? map.get("work_type").toString() : "";
		String extList = "";
		String procList = "";
		String ipList = "";
		String pwList = "";
		
		// 공통 유효성 체크
		if(workType.equals("") ||  idList.equals(""))	{
			throw processException("common.required.error");
		}
		
		// 개별 페이지 처리 항목 START
		// 1. LSC_EXTENSION : EXCEPTION
		if(workType.equals(Constant.LSC_EXTENSION))	{
			extList = map.get("extList") != null ? map.get("extList").toString() : "";		
			extList = extList.replaceAll(",",";");		// EXTENSION 구분자(;)		
			if(extList.equals("") )	{
				throw processException("common.required.error");
			}
		}
		// 2. LSC_EPROC : PROCESS
		if(workType.equals(Constant.LSC_EPROC)) {
			procList = map.get("procList") != null ? map.get("procList").toString() : "";
			procList = procList.replaceAll(",", ";");	// PROCESS 구분자(;)
			if(procList.equals("")) {
				throw processException("common.required.error");
			}
		}
		// 3. LSC_CONTROL : IS_UNACTIVE :: 수정처리 없음
		// 4. LSC_ENABLE_USB : IS_USB_ACTIVE :: 수정처림 없음
		// 5. LSC_NETDRIVE_ADDR : IP_ADDRESS
		if(workType.equals(Constant.LSC_NETDRIVE_ADDR)) {
			ipList = map.get("ipList") != null ? map.get("ipList").toString() : "";
			ipList = ipList.replaceAll(",", ";");	// IP 구분자(;)
			if(ipList.equals("")) {
				throw processException("common.required.error");
			}
		}
		// 6. RGC_UNINSTALL_PASS : PASSWD
		if(workType.equals(Constant.RGC_UNINSTALL_PASS)) {
			pwList = map.get("pwList") != null ? map.get("pwList").toString() : "";
			if(pwList.equals("")) {
				throw processException("common.required.error");
			}
			
		}
		
		String[] ckey = idList.split("[,]");
		
		for(int i=0;i<ckey.length;i++) {
			
			HashMap<String,Object> dbMap = new HashMap<String,Object>();
			dbMap.put("ckey",ckey[i]);
			// 개별 페이지 처리 항목 START
			// 1. LSC_EXTENSION : EXCEPTION
			if(workType.equals(Constant.LSC_EXTENSION))	{
				dbMap.put("cvalue",extList);
				dbMap.put("extension",extList);
			}
			// 2. LSC_EPROC : PROCESS
			else if(workType.equals(Constant.LSC_EPROC)) {
				dbMap.put("cvalue", procList);
				dbMap.put("process", procList);
			}
			// 3. LSC_CONTROL : IS_UNACTIVE :: 수정처리 없음
			// 4. LSC_ENABLE_USB : IS_USB_ACTIVE :: 수정처리 없음
			// 5. LSC_NETDRIVE_ADDR : IP_ADDRESS
			else if(workType.equals(Constant.LSC_NETDRIVE_ADDR)) {
				dbMap.put("cvalue", ipList);
				dbMap.put("ip_address", ipList);
			}
			// 6. RGC_UNINSTALL_PASS : PASSWD
			else if(workType.equals(Constant.RGC_UNINSTALL_PASS)) {
				dbMap.put("cvalue", pwList);
				dbMap.put("passwd", pwList);
			}
			// 개별 페이지 처리 항목 END
			
			ret.add(dbMap);
		}
		
		
		return ret;
	}
	
	@Override
	public Map<String, Object> rgatePolicyManageUpdate(List<HashMap<String,Object>> policyList,HashMap<String,Object> map) 
			throws Exception {
		
		RgateDao rgateDao = sqlSession.getMapper(RgateDao.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result",Constant.RESULT_FALSE);
		int result = 0;
		
		// 저장금지 수정은 공통으로 사용한다.
		for(HashMap<String,Object> ret : policyList) {			
					
			result = rgateDao.rgateMappingUpdate(ret);
			if(result == 0)	{	throw processException("common.system.error");	}				
			
			result = rgateDao.rgatePolicyUpdate(ret);
			if(result == 0)	{	throw processException("common.system.error");	}				
		}
		
		resultMap.put("result",Constant.RESULT_TRUE);
		return resultMap;		
	}
	
	@Override
	public Map<String, Object> exceptionPageList(HashMap<String,Object> map) throws Exception {
		
		RgateDao rgateDao = sqlSession.getMapper(RgateDao.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		List<RGateProcessVO>  ret = new ArrayList<RGateProcessVO>();		
		
		ret = rgateDao.rgateProcessList(map);
		
		resultMap.put("records",ret.size());
		resultMap.put("list",ret);
		
		return resultMap;		
	}
	
	@Override
	public List<RGateProcessVO> exceptInsertValid(HashMap<String,Object> map) throws Exception {

		RgateDao rgateDao = sqlSession.getMapper(RgateDao.class);

		List<RGateProcessVO> ret = new ArrayList<RGateProcessVO>();
		HashMap<String,Object> param = new HashMap<String,Object>(); 
				
		String programList =  map.get("programList") != null ? map.get("programList").toString() : "";
		String type = map.get(Constant.TYPE) != null ? map.get(Constant.TYPE).toString() : "";
		
		// 1.입력값 유효성 체크
		if(type.equals("") )	{
			throw processException("common.required.error");
		}

		JSONArray jsonArray = JSONArray.fromObject(programList);
		if(jsonArray.size() > 0 ) {
			
			 for(int j=0;j < jsonArray.size();j++)	{		
				 
				 RGateProcessVO vo = new RGateProcessVO();
				 vo.setWork_type(Constant.LSC_WDIRS_PROC);
				 vo.setProcess(jsonArray.getJSONObject(j).getString("process").toString());
				 if(type.equals(Constant.UPDATE))	{
					 vo.setFolder_path(jsonArray.getJSONObject(j).getString("folder_path").toString());
				 }

				 // 입력된 프로그램은 등록리스트에서 제거처리한다.
				 param.put("table_nm", Constant.LSC_RGATE_PROCESS);
				 param.put("process", vo.getProcess());

				 if(rgateDao.isProessUsing(param) == 0 
						 && type.equals(Constant.INSERT))	{		// 추가			 
					 ret.add(vo);
				 }else if(type.equals(Constant.DELETE) || type.equals(Constant.UPDATE))	{			// 삭제
					 ret.add(vo);
				 }
				  				
			 }
		}

		return ret;		
	}
	
	@Override
	public Map<String, Object> exceptProcess(List<RGateProcessVO> rgateProcess,HashMap<String,Object> map) 
			throws Exception {

		RgateDao rgateDao = sqlSession.getMapper(RgateDao.class);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String type = map.get(Constant.TYPE) != null ? map.get(Constant.TYPE).toString() : "";
		int result = 0;
		
		if(type.equals(Constant.INSERT))	{
			
			// XR_RGATE_PROCESS INSERT 처리
			if(rgateProcess != null && rgateProcess.size() > 0 )	{
				 for(RGateProcessVO vo : rgateProcess) {				 
					 result = rgateDao.rgateProcessWrite(vo);
					 if(result == 0)	{	throw processException("common.system.error");	}				 
				 }			
			}
			
		}else if(type.equals(Constant.UPDATE))	{
		
			// 프로그램별 작업폴더 수정처리
			if(rgateProcess != null && rgateProcess.size() > 0 )	{
				
				 for(RGateProcessVO vo : rgateProcess) {				
					 
					 HashMap<String,Object> param = new HashMap<String,Object>();
					 
					 param.put("process",vo.getProcess());
		 										 					 
					 if(vo.getFolder_path() != null && vo.getFolder_path().length() > 0)	{
					
						 // folder_path 마지막 라인 ; 자동추가
						 if(!vo.getFolder_path().endsWith(";")) {
							vo.setFolder_path(vo.getFolder_path()+";");
						 }
						 
						 // XR_RGATE_PROCESS UPDATE
						 param.put("folder_path",vo.getFolder_path());			
						 result = rgateDao.rgateProcessUpdate(param);
						 if(result == 0)	{	throw processException("common.system.error");	}				
						 
						 // XR_RGATE_POLICY DELETE
						 param.put("ckey",Constant.LSC_WDIRS_PROC + "_"+vo.getProcess());
						 rgateDao.rgatePolicyDelete(param);
						 
						 // XR_RGATE_POLICY INSERT
						 param.put("cvalue",vo.getFolder_path());
						 result = rgateDao.rgatePolicyWrite(param);
						 if(result == 0)	{	throw processException("common.system.error");	}
						 
					 }else {
						 
						 // 저장허용폴더 empty 처리
						 
						 // XR_RGATE_PROCESS UPDATE
						 param.put("folder_path","");
						 result = rgateDao.rgateProcessUpdate(param);
						 if(result == 0)	{	throw processException("common.system.error");	}				
						 
						 // XR_RGATE_POLICY DELETE
						 param.put("ckey",Constant.LSC_WDIRS_PROC + "_"+vo.getProcess());
						 rgateDao.rgatePolicyDelete(param);
						 
					 }
					 
				 }
				
			}
			
		}else if(type.equals(Constant.DELETE))	{
			
			// XR_RGATE_PROCESS DELETE 처리
			if(rgateProcess != null && rgateProcess.size() > 0 )	{
				
				 for(RGateProcessVO vo : rgateProcess) {				 
				
					 // XR_RGATE_PROCESS DELETE
					 HashMap<String,Object> param = new HashMap<String,Object>(); 
					 param.put("process",vo.getProcess());
					 result = rgateDao.rgateProessDelete(param);
					 if(result == 0)	{	throw processException("common.system.error");	}				 
					 
					 // XR_RGATE_POLICY DELETE
					 param.put("ckey",Constant.LSC_WDIRS_PROC + "_"+vo.getProcess());
					 rgateDao.rgatePolicyDelete(param);
					 
				 }
			}
		}
		
		resultMap.put("result",Constant.RESULT_TRUE);
		
		return resultMap;		
	}

	@Override
	public Map<String, Object> rgateListWrite(RGateListVO rGateListVO) throws Exception {
		
		RgateDao rgateDao = sqlSession.getMapper(RgateDao.class);		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int ret = 0;
		
		//RGate 리스트 등록은 공통으로 사용한다.
		ret = rgateDao.rgateListWrite(rGateListVO);
		
		if(ret == 1)
			resultMap.put("result",Constant.RESULT_TRUE);
		else
			resultMap.put("result",Constant.RESULT_FALSE);
		
		return resultMap;
		
	}

	@Override
	public List<HashMap<String, Object>> setRgateListDelList(
			HashMap<String, Object> map) throws Exception {

		List<HashMap<String,Object>> ret = new ArrayList<HashMap<String,Object>>();
		
		RgateDao rgateDao = sqlSession.getMapper(RgateDao.class);		
		
		//String workType = map.get("manage_type") != null ? map.get("manage_type").toString() : "";
		String manageType = map.get("manage_type") != null ? map.get("manage_type").toString() : "";
		String manageList = map.get("manage_name") != null ? map.get("manage_name").toString() : "";		
		
		// 공통 유효성 체크
		if(manageType.equals("") ||  manageList.equals(""))	{
			throw processException("common.required.error");
		}
		
		String[] resultId = manageList.split("[,]");
		
		for(String manageName : resultId) {
			
			HashMap<String, Object> dMap = new HashMap<String, Object>();		
			dMap.put("manage_name",manageName);
			
			// XR_RGATE_MAPPING 사용중인지 체크한다.	:: 프로그램 && IP
			HashMap<String,Object> param1 = new HashMap<String, Object>();
			if(manageType.equals(Constant.MANAGE_PROC))	{
				param1.put("work_type",Constant.LSC_EPROC);	
			}else {
				param1.put("work_type",Constant.LSC_NETDRIVE_ADDR);
			}					
			param1.put("table_nm",Constant.LSC_RGATE_MAPPING);			
			param1.put("manage_type",manageType);
			param1.put("strKeyword",manageName);
				
			if(rgateDao.isProessUsing(param1) > 0 ) {
				throw processException("rgate.proess.using.not.delete");
			}
		
						
			// XR_RGATE_PROCESS 사용중인지 체크한다. :: 프로그램(프로세스) 
			if(manageType.equals(Constant.MANAGE_PROC))	{
				HashMap<String,Object> param2 = new HashMap<String, Object>();
				param2.put("table_nm",Constant.LSC_RGATE_PROCESS);				
				param2.put("process",manageName);
				
				if(rgateDao.isProessUsing(param2) > 0 ) {
					throw processException("rgate.proess.using.not.delete");
				}
			}
			
			ret.add(dMap);
		}
		
		return ret;
	}

	@Override
	public Map<String, Object> rgateListDelete(
			List<HashMap<String, Object>> delList, HashMap<String, Object> map)
			throws Exception {
		
		RgateDao rgateDao = sqlSession.getMapper(RgateDao.class);		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", Constant.RESULT_FALSE);
		int result = 0;
		
		//RGate 리스트 삭제는 공통으로 사용한다.
		for(HashMap<String,Object> ret : delList) {
			result = rgateDao.rgateListDelete(ret);
			if(result == 0) { throw processException("common.system.error"); }
			
		}
		
		resultMap.put("result", Constant.RESULT_TRUE);
		return resultMap;
	}


}
