package kr.co.exsoft.process.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kr.co.exsoft.common.dao.CommonDao;
import kr.co.exsoft.common.service.CacheService;
import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.common.vo.CommentVO;
import kr.co.exsoft.common.vo.RecentlyObjectVO;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.dao.DocumentDao;
import kr.co.exsoft.document.service.DocumentService;
import kr.co.exsoft.document.vo.DocumentVO;
import kr.co.exsoft.document.vo.PageVO;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.handler.ExsoftServiceExceptionHandler;
import kr.co.exsoft.eframework.library.ExsoftAbstractServiceImpl;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.PagingAjaxUtil;
import kr.co.exsoft.folder.dao.FolderDao;
import kr.co.exsoft.folder.vo.FolderVO;
import kr.co.exsoft.permission.dao.AclDao;
import kr.co.exsoft.process.dao.ProcessDao;
import kr.co.exsoft.process.vo.ProcessExecutorVO;
import kr.co.exsoft.process.vo.ProcessVO;
import kr.co.exsoft.statistics.dao.StatisticsDao;
import kr.co.exsoft.statistics.vo.DocumentUserHtVO;
import kr.co.exsoft.user.dao.GroupDao;
import oracle.jdbc.Const;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * Process 서비스 구현 부분
 * @author 패키지 개발팀
 * @since 2015.03.12
 * @version 3.0
 *
 */	
@Service("processService")
public class ProcessServiceImpl extends ExsoftAbstractServiceImpl implements ProcessService {
	
	@Autowired
	@Qualifier("sqlSession")
	private SqlSession sqlSession;
	
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private DocumentService documentService;
	
	/**
	 * public method 구현
	 */	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 협업 목록에서 사용할 tooltip set 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setProcessTooltip
	 * @param processList
	 * @param map
	 * @throws Exception void
	 */
	public void setProcessTooltip(List<ProcessVO> processList, HashMap<String, Object> map) throws Exception{
		ProcessDao processDao = sqlSession.getMapper(ProcessDao.class);
		
		List<String> processIdList = new ArrayList<String>();
		for(ProcessVO processVo : processList){
			processIdList.add(processVo.getProcess_id());
		}
		
		if( processIdList.size() == 0){
			//throw new Exception("[ProcessServiceImpl.setProcessTooltip] process_id not found Exception!!");
			return; // 데이터가 없을 경우
		}
			
		map.put("processIdList", processIdList);
		List<ProcessExecutorVO> peVoList = processDao.processExcutorList(map);
		
		// 협업 리스트, 협업 단계조회에서 사용됨
		setExecutorInfo(processList, peVoList);
		
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 협업 목록, 협업단계 조회, 협업 상세조회에서 사용할 승인, 열람 정보를 set
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setExecutorInfo
	 * @param processList
	 * @param peVoList
	 * @throws Exception void
	 */
	public void setExecutorInfo(List<ProcessVO> processList, List<ProcessExecutorVO> peVoList) throws Exception{
		int wirteCntIng=0, approveCntIng=0, receiveCntIng=0;
		int wirteCntEnd=0, approveCntEnd=0, receiveCntEnd=0;
		
		List<ProcessExecutorVO> tempList = new ArrayList<ProcessExecutorVO>();
		tempList.addAll(peVoList);
		
		for(int i=0; i < processList.size(); i++) {
			ProcessVO tempProcessVo = new ProcessVO(); 
			tempProcessVo =	processList.get(i);
		
			for(Iterator<ProcessExecutorVO> iter = tempList.iterator(); iter.hasNext();){
				ProcessExecutorVO tempVo = iter.next();
				if(tempProcessVo.getProcess_id().equals(tempVo.getProcess_id())){
					switch (tempVo.getType()) {
						case Constant.PROCESS_TYPE_AUTHOR: tempProcessVo.setAuthor_nm(tempVo.getExecutor_name()); //공동작성자와 같이 처리 한다.
						case Constant.PROCESS_TYPE_COAUTHOR:{
							if(tempVo.getStatus().equals(Constant.PROCESS_EXECUTOR_END)){
								wirteCntEnd++; //완료
								tempProcessVo.getWrite_list().add(tempVo.getExecutor_name()+"|작성완료");
								tempProcessVo.setWrite_list(tempProcessVo.getWrite_list());
							}else{
								wirteCntIng++; //미완료
								tempProcessVo.getWrite_list().add(tempVo.getExecutor_name()+"|작성중");
								tempProcessVo.setWrite_list(tempProcessVo.getWrite_list());
							}
						};break;
						case Constant.PROCESS_TYPE_APPROVER:{
							if(tempVo.getStatus().equals(Constant.PROCESS_EXECUTOR_END)){
								approveCntEnd++; //완료
								tempProcessVo.getApproval_list().add(tempVo.getExecutor_name()+"|승인완료");
								tempProcessVo.setApproval_list(tempProcessVo.getApproval_list());
							}else{
								approveCntIng++; //미완료
								tempProcessVo.getApproval_list().add(tempVo.getExecutor_name()+"|승인대기");
								tempProcessVo.setApproval_list(tempProcessVo.getApproval_list());
							}
						};break;
						case Constant.PROCESS_TYPE_RECEIVER:{
							if(tempVo.getStatus().equals(Constant.PROCESS_EXECUTOR_END)){
								receiveCntEnd++; //완료
								tempProcessVo.getReceiver_list().add(tempVo.getExecutor_name()+"|열람완료");
								tempProcessVo.setReceiver_list(tempProcessVo.getReceiver_list());
							}else{
								receiveCntIng++; //미완료
								tempProcessVo.getReceiver_list().add(tempVo.getExecutor_name()+"|열람대기");
								tempProcessVo.setReceiver_list(tempProcessVo.getReceiver_list());
							}
							
						};break;
	
						default:
							break;
					}
			
					iter.remove(); // 사용한 peVoList 값은 remove 시킨다.
				}
			} // 실행자 for end...
			
			tempProcessVo.setWrite_count(wirteCntEnd+"/"+(wirteCntIng+wirteCntEnd));
			tempProcessVo.setApproval_count(approveCntEnd+"/"+(approveCntIng+approveCntEnd));
			tempProcessVo.setReceiver_count(receiveCntEnd+"/"+(receiveCntIng+receiveCntEnd));
			// 기존 내용 변경 시 add가 아닌 set을 이용한다.
			processList.set(i, tempProcessVo);
		} // 협업 for end..		
	}
	
	

	/**
	 * Interface override 
	 */	
	@Override
	public Map<String, Object> processCount(HashMap<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		ProcessDao processDao = sqlSession.getMapper(ProcessDao.class);
		
		int count = processDao.processCount(map);
		
		resultMap.put("count",count);
		resultMap.put("result",Constant.RESULT_TRUE);
		return resultMap;
	}

	@Override
	public Map<String, Object> processList(HashMap<String, Object> map) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<ProcessVO> processList = new ArrayList<ProcessVO>();
		int total = 0;
		
		ProcessDao processDao = sqlSession.getMapper(ProcessDao.class);
		
		// 1. 문서 목록을 가져 온다
		total = processDao.processListCount(map);
		processList = processDao.processList(map);
		
		// 2. 승인, 열람 tooltip을 set. 엑셀저장은 매핑 안함
		if(processList.size() > 0) {
			setProcessTooltip(processList, map);
		}
		
		resultMap.put("result",Constant.RESULT_TRUE);
		resultMap.put("page",map.get("nPage").toString());
		resultMap.put("records",total);
		resultMap.put("total",CommonUtil.getTotPageSize(total,Integer.parseInt(map.get("page_size").toString())));	
		resultMap.put("list",processList);		
		
		// Ajax Paging 
		String strLink = "javascript:exsoftProcessFunc.event.gridPage";
		String contextRoot = map.get("contextRoot") != null ? map.get("contextRoot").toString() : "";
		PagingAjaxUtil pagingInfo = new PagingAjaxUtil(Integer.parseInt(map.get("nPage").toString()),total,Integer.parseInt(map.get("page_size").toString()),10,strLink,contextRoot);		
		resultMap.put("pagingInfo",pagingInfo);		

		return resultMap;
	}
	
	@Override
	public Map<String, Object> processRecentlyList(HashMap<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<ProcessVO> processList = new ArrayList<ProcessVO>();
		ProcessDao processDao = sqlSession.getMapper(ProcessDao.class);
		
		processList = processDao.processRecentlyRegistList(map);
		
		resultMap.put("result",Constant.RESULT_TRUE);
		resultMap.put("list",processList);		
		

		return resultMap;
	}
	
	@Override
	public Map<String, Object> selectProcessRecently(HashMap<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<ProcessExecutorVO> processExecutorList = new ArrayList<ProcessExecutorVO>();
		ProcessDao processDao = sqlSession.getMapper(ProcessDao.class);
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		// 1. 업무명 가져오기
		ProcessVO processInfo = processDao.processInfo(map);
		
		// 1-1 폴더_id 가져 오기
		map.put("doc_id", processInfo.getDoc_root_id());
		String processName = processInfo.getName();
		String folderId = processDao.processFolderIdByDocId(map);
		map.put("folder_id", folderId);
		FolderVO folderVo = folderDao.folderDetail(map);
		
		// 2. 협업자 정보 가져오기
		processExecutorList = processDao.processExcutorList(map);
		
		// 3. 협업자 정보를 이용하여 협업자의 부서명 가져오기
		List<String> userIdList = new ArrayList<String>();
		for(ProcessExecutorVO processExecutorVO : processExecutorList){
			userIdList.add(processExecutorVO.getExecutor_id());
		}
		
		map.put("userIdList", userIdList);
		List<CaseInsensitiveMap> groupList = groupDao.groupInfoByUserId(map);
		HashMap<String, String> groupInfoMap = new HashMap<String, String>();
		for(CaseInsensitiveMap tempMap : groupList){
			groupInfoMap.put((String)tempMap.get("user_id"), (String)tempMap.get("group_nm"));
		}
		
		for(int i=0; i<processExecutorList.size(); i++){
			processExecutorList.get(i).setGroup_nm(groupInfoMap.get((processExecutorList.get(i).getExecutor_id())));
		}
		
		resultMap.put("result",Constant.RESULT_TRUE);
		resultMap.put("processName",processName);
		resultMap.put("full_path",cacheService.getFolderFullpathNameByFolderId(folderId, true));		
		resultMap.put("folder_id",folderId);
		resultMap.put("map_id",folderVo.getMap_id());
		resultMap.put("acl_id",folderVo.getAcl_id());
		resultMap.put("list",processExecutorList);		

		return resultMap;
	}

	@Override
	public Map<String, Object> processWrite(SessionVO sessionVO, Model model, HashMap<String, Object> map, HttpServletRequest request) throws Exception {
		
		ProcessDao processDao = sqlSession.getMapper(ProcessDao.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		int process_id = commonService.commonNextVal(Constant.COUNTER_ID_PROCESS);
		int recently_id = commonService.commonNextVal(Constant.COUNTER_ID_RECENTLY);

		String strProcessId = CommonUtil.getStringID(Constant.ID_PREFIX_PROCESS, process_id);
		String strContent = map.get("content") != null ? map.get("content").toString() : "";
		
		// 1. documentVo set
		DocumentVO documentVo = new DocumentVO();
		//documentVo.setDoc_id(strDocumentId);
		documentVo.setDoc_name(map.get("name").toString());
		documentVo.setAcl_id(map.get("acl_id").toString());
		documentVo.setDoc_type(map.get("doc_type").toString());
		documentVo.setFolder_id(map.get("folder_id").toString());
		documentVo.setAccess_grade(Constant.DOCUMENT_DEFALUT_ACCESSGRADE);
		documentVo.setSecurity_level(Constant.DOCUMENT_DEFALUT_SECURITY_LEVEL);
		documentVo.setDoc_status(Constant.DOCUMENT_STATUS_PROCESS_ING);			//process 진행단계는 P, 완료 후 C
		 
		// vailidation check를 위한 추가
		map.put("isType", Constant.INSERT);
		map.put("version_type", Constant.VERSION_NEW_DOCUMENT);
		
		// 1-1. 확장속성이 존재하는 경우	
		List<HashMap<String,Object>> attrList = new ArrayList<HashMap<String,Object>>();
		if(map.get("is_extended") != null && map.get("is_extended").toString().equals(Constant.T))	{	
			attrList = documentService.docExtendedAttrList(request,documentVo.getDoc_type()); 				
		}
		
		// 1-2 doc_id, 확장 권한(aclExItem_list), 첨부파일(fileList) 등 set
		documentService.writeDocValid(map, documentVo, sessionVO);
		// 1-3 documentService.writeDocProc(); 호출
		documentService.writeDocProc(map, documentVo, attrList, sessionVO);
		
		// 2. processVo set
		ProcessVO processVo = new ProcessVO();
		processVo.setProcess_id(strProcessId);
		processVo.setDoc_root_id(documentVo.getDoc_id());
		processVo.setCreator_id(sessionVO.getSessId());
		processVo.setCreator_name(sessionVO.getSessName());
		processVo.setName(map.get("name").toString()); 						// 업무명
		processVo.setStatus(Constant.PROCESS_STATUS_WRITE);
		//processVo.setExpect_date(map.get("expect_date").toString());		// 완료 예정일
		processVo.setExpect_dateDB(CommonUtil.getCurruentTimeByDate(map.get("expect_date").toString()));
		processVo.setContent(strContent);
		// 2-1 process Dao 처리
		processDao.insertProcess(processVo);
		
		// 3. processExecutorVo set
		// 3-1 작성자, 공동작성자, 승인자, 수신자
		List<ProcessExecutorVO> proExecutorList = CommonUtil.jsonArrayToProcessExecutorList(map);
		// 3-2 요청자
		ProcessExecutorVO processExecutorVo = new ProcessExecutorVO();
		processExecutorVo.setType(Constant.PROCESS_TYPE_REQUESTOR);
		processExecutorVo.setExecutor_id(map.get("requestorId").toString());
		processExecutorVo.setExecutor_name(map.get("requestorName").toString());
		processExecutorVo.setStatus(Constant.PROCESS_EXECUTOR_END);
		processExecutorVo.setSort_index(0);
		
		proExecutorList.add(processExecutorVo);
		for(ProcessExecutorVO tempVo : proExecutorList){
			String execute_id = CommonUtil.getStringID(Constant.ID_PREFIX_PROCESS_EXECUTOR, commonService.commonNextVal(Constant.COUNTER_ID_PROCESS_EXECUTOR));
			tempVo.setExecute_id(execute_id);
			tempVo.setProcess_id(strProcessId);
			tempVo.setDoc_root_id(documentVo.getDoc_id());
			
			// 날짜 및 상태값
			if(tempVo.getType().equals(Constant.PROCESS_TYPE_AUTHOR) || tempVo.getType().equals(Constant.PROCESS_TYPE_COAUTHOR)){
				if(tempVo.getType().equals(Constant.PROCESS_TYPE_AUTHOR)){
					documentVo.setOwner_id(tempVo.getExecutor_id());
				}
				
				tempVo.setStatus(Constant.PROCESS_EXECUTOR_START);
				tempVo.setStart_dateDB(CommonUtil.getCurruentTime());	// 오늘 날짜
			}else{
				tempVo.setStatus(Constant.PROCESS_EXECUTOR_WAIT);
				tempVo.setStart_dateDB(CommonUtil.getCurruentTimeByDate("9999-01-01"));  // 임시 날짜
			}
			
			// processExecutor Dao 처리
			processDao.insertProcessExecutor(tempVo);
		}
		
		// 4. recently set
		RecentlyObjectVO recentlyObjectVo = new RecentlyObjectVO();
		recentlyObjectVo.setIdx(CommonUtil.getStringID(Constant.ID_PREFIX_RECENTLY, recently_id));
		recentlyObjectVo.setUser_id(sessionVO.getSessId());
		recentlyObjectVo.setTarget_id(processVo.getProcess_id());
		recentlyObjectVo.setTarget_type(Constant.RECENTLY_TYPE_PROCESS);
		
		// 4-1 recently Dao 처리 
		commonService.insertRecentlyObject(recentlyObjectVo);
		
		resultMap.put("result",Constant.RESULT_TRUE);		
		return resultMap;
	}

	@Override
	public Map<String, Object> processDetail(HashMap<String, Object> map) throws Exception {
		ProcessDao processDao = sqlSession.getMapper(ProcessDao.class);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		ProcessVO processVo = new ProcessVO();
		List<ProcessVO> tempProcessList = new ArrayList<ProcessVO>();
		List<ProcessExecutorVO> executorList = new ArrayList<ProcessExecutorVO>();
		
		processVo = processDao.processInfo(map);
		executorList = processDao.processExcutorList(map);
		
		tempProcessList.add(processVo);
		setExecutorInfo(tempProcessList, executorList);
		
		resultMap.put("processVo",tempProcessList.get(0));		
		resultMap.put("processExecutorList",executorList);		
		resultMap.put("result",Constant.RESULT_TRUE);		
		return resultMap;
	}

	@Override
	public Map<String, Object> approveAction(HashMap<String, Object> map, SessionVO sessionVO) throws Exception {
		ProcessDao processDao = sqlSession.getMapper(ProcessDao.class);
		DocumentDao documentDao = sqlSession.getMapper(DocumentDao.class);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String actionType = map.get("actionType").toString();	// null check 안함.(null point exception)
		String process_id = map.get("process_id").toString();  
		boolean isNextExecutor = true;
		
		// 1. xr_comment 추가 :: com_step은 증가, com_order는 default(0) 즉, 답글 형태로 구성 안함
		map.put("doc_root_id", process_id);
		CommentVO commentVo = new CommentVO();
		commentVo.setCom_id( CommonUtil.getStringID(Constant.ID_PREFIX_COMMENT, commonService.commonNextVal(Constant.COUNTER_ID_COMMENT)));
		commentVo.setDoc_root_id(process_id);  
		commentVo.setCom_step(String.valueOf(documentDao.checkMaxStep(map)+1));  // doc_root_id => process_id
		commentVo.setCreator_id(sessionVO.getSessId());
		commentVo.setCreator_name(sessionVO.getSessName());
		commentVo.setParent_creator_name(sessionVO.getSessName());
		commentVo.setContent(map.get("content").toString());
		
		documentDao.docCommentWrite(commentVo);	
		
		// 2. xr_process 승인 단계로 변경
		ProcessVO processVo = new ProcessVO(); // process_id, status를 제외한 DB값은 빈값
		processVo.setProcess_id(process_id);
		processVo.setComplete_dateDB(CommonUtil.getCurruentTime());
		
		// 3. xr_process_executor 요청한 실행자 상태값 변경
		ProcessExecutorVO processExecutorVo = new ProcessExecutorVO();
		processExecutorVo.setUpdateDBType(actionType);
		processExecutorVo.setProcess_id(process_id);		
		
		if(actionType.equals(Constant.PROCESS_ACTION_APPROVEREJECT)){
			// 반려
			isNextExecutor = false;
			processVo.setStatus(Constant.PROCESS_STATUS_MODIFY);
			processExecutorVo.setStatus(Constant.PROCESS_EXECUTOR_WAIT);
			processExecutorVo.setEnd_dateDB(CommonUtil.getCurruentTimeByDate("8888-01-01"));
			
		}else{
			// 승인요청, 승인
			processExecutorVo.setStatus(Constant.PROCESS_EXECUTOR_END);
			processExecutorVo.setEnd_dateDB(CommonUtil.getCurruentTime());
			
		}
		
		// [3]action 요청한 실행자 정보 update
		processDao.updateProcessExecutor(processExecutorVo);
		
		// 4. xr_process_executor 첫번째 승인자 승인 시작으로 변경 :: status(S), start_date(sysdate)
		if(isNextExecutor){
			ProcessExecutorVO currentApprover = processDao.currentApproverInfo(map);
			if(currentApprover != null){
				processVo.setStatus(Constant.PROCESS_STATUS_APPROVAL);
				processExecutorVo = new ProcessExecutorVO();
				processExecutorVo.setExecute_id(currentApprover.getExecute_id());
				processExecutorVo.setStatus(Constant.PROCESS_EXECUTOR_START);
				processExecutorVo.setStart_dateDB(CommonUtil.getCurruentTime());
				// 다음 승인자 update
				processDao.updateProcessExecutor(processExecutorVo);
				
			}else{
				processVo.setStatus(Constant.PROCESS_STATUS_END);
			}
		}else if(processVo.getStatus().equals(Constant.PROCESS_STATUS_MODIFY)){
			processExecutorVo = new ProcessExecutorVO();
			processExecutorVo.setUpdateDBType(Constant.PROCESS_ACTION_APPROVEREQUEST);
			processExecutorVo.setStatus(Constant.PROCESS_EXECUTOR_START);
			processExecutorVo.setStart_dateDB(CommonUtil.getCurruentTime());
			processExecutorVo.setEnd_dateDB(CommonUtil.getCurruentTimeByDate("8888-01-01"));
			// 다음 승인자 update
			processDao.updateProcessExecutor(processExecutorVo);
		}
		
		// [2]업무(협업) 단계 update
		processDao.updateProcess(processVo);
		
//		public static final String PROCESS_ACTION_APPROVEREQUEST	= "APPROVEREQUEST";		// 승인요청	
//		public static final String PROCESS_ACTION_APPROVE			= "APPROVE";			// 승인	
//		public static final String PROCESS_ACTION_APPROVEREJECT		= "APPROVEREJECT";		// 반려
		
		///////
		//// 승인요청 저리의 조건
		//  0. 승인요청 내용 xr_comment 등록
		//  1. 작성자, 공동작성자 상태값 E, 완료일 지정
		//  2. 프로세스 단계 승인(A)으로 변경
		//  3. 다음 승인 처리자 처리
		//////
		
		////////
		//// 승인 처리의 조건
		// 0. 승인 내용 xr_comment 등록
		// 1. 승인처리한 승인자의 상태값 E, 완료일 지정
		// 2. 다음 승인 처리자가 있는지 확인
		       // 2-1 : 다음 승인자가 존재할 경우 상태값 S, 시작일 지정
		       // 2-2 : 다음 승인자가 없을 경우 프로세스 단계 완료(E) 처리
		////////
		
		///////
		//// 반려 처리의 조건
		// 0. 반려 내용 xr_comment 등록
		// 1. 모든 승인자 상태값 N으로 변경
		// 2. 프로세스 단계 보완(M) 단계로 변경
		// 3. 작성자, 공동작성자 상태값 S로 변경
		///////
				
		resultMap.put("result",Constant.RESULT_TRUE);		
		return resultMap;
	}

}
