package kr.co.exsoft.note.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.library.ExsoftAbstractServiceImpl;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.PagingAjaxUtil;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.note.dao.NoteDao;
import kr.co.exsoft.note.vo.NoteManageVO;
import kr.co.exsoft.note.vo.NoteVO;
import kr.co.exsoft.user.dao.GroupDao;
import kr.co.exsoft.user.vo.GroupVO;
import kr.co.exsoft.user.vo.GroupedVO;
import net.sf.json.JSONArray;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * Note 서비스 구현 부분
 * @author 패키지 개발팀
 * @since 2015.03.02
 * @version 3.0
 *
 */	
@Service("noteService")
public class NoteServiceImpl extends ExsoftAbstractServiceImpl implements NoteService {

	@Autowired
	@Qualifier("sqlSession")
	private SqlSession sqlSession;
	
	@Autowired
	private CommonService commonService;
		

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteListForInserting
	 * @param map
	 * @return List<NoteVO>
	 */
	public Map<String, Object> noteListForInserting(List<HashMap<String, Object>> noteList, HashMap<String, Object> map, SessionVO sessionVO) throws Exception {
		// TODO Auto-generated method stub
		
		int result=0;
		
		HashMap<String,Object> resultMap = new  HashMap<String, Object>();
		
		NoteDao noteDao = sqlSession.getMapper(NoteDao.class);
		//GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		//
		int noteid_seq = commonService.commonNextVal(Constant.COUNTER_ID_NOTE);
		String newNoteID = CommonUtil.getStringID(Constant.ID_PREFIX_NOTE, noteid_seq);		
/*
		System.out.println("=======================================");
		System.out.println("newNoteID = " + newNoteID);
		System.out.println("=======================================");*/
		String newRootID = newNoteID;
		
		String gid = "";
		
		
		
		//TEST ========================================================================

		String ntlist ="";// "accessor_isgroup=accessor_id:"", accessor_isgroup:""";
		
		//resultList = getNoteVOFromReciverList(noteList,groupDao);
			/*
		String gid = (String) resultList.get("from_group_id");			
		String pid = (String) resultList.get("from_pgroup_id");
		String uid = (String) resultList.get("from_user_id");*/
		
		//XR_NOTE INSERT
		HashMap<String,Object> param = new  HashMap<String, Object>();
		param.put("note_id", newNoteID);
		param.put("root_id", newRootID);
		param.put("creator_id", sessionVO.getSessId());
		param.put("content", map.get("content"));
		param.put("note_from",  map.get("note_from"));		
		param.put("note_from_userid", "");
		param.put("note_from_groupid",  "");
		param.put("note_from_pgroupid",  "");
		
		//XR_NOTE INSERT
		result = noteDao.noteWrite(param);
		if(result == 0)	{	throw processException("common.system.error");	}
	
		//XR_NOTEMANAGE INSERT
		// 수신자 Insert
		List<NoteManageVO> manageVOList =  new ArrayList<NoteManageVO>();
		NoteManageVO manageVO = new NoteManageVO();
		// 발신자 Insert
		//============================================
		int notemid_seq = commonService.commonNextVal(Constant.COUNTER_ID_NOTEMANAGE);
		String newManageID = CommonUtil.getStringID(Constant.ID_PREFIX_NOTEMANAGE, notemid_seq);
/*
		System.out.println("=======================================");
		System.out.println("newManageID = " + newManageID);
		System.out.println("=======================================");*/
		
		manageVO.setManage_id(newManageID);
		manageVO.setNote_id(newNoteID);
		manageVO.setRoot_id(newRootID);
		manageVO.setNote_target_id(sessionVO.getSessId());
		manageVO.setNote_type("S");
		manageVO.setNote_save("N");
		manageVO.setNote_read("N");

		result = noteDao.noteManageWrite(manageVO);				
		if(result == 0)	{	throw processException("common.system.error");	}
		//=============================================
		for(HashMap<String,Object> nMap : noteList) {
			
			// 수신자 리스트 가져오기
			//accessor_isgroup			
			String accessor_id = nMap.get("accessor_id").toString();
			String accessor_isgroup = nMap.get("accessor_isgroup").toString();
			
			
			manageVO = new NoteManageVO();

			notemid_seq = commonService.commonNextVal(Constant.COUNTER_ID_NOTEMANAGE);
			newManageID = CommonUtil.getStringID(Constant.ID_PREFIX_NOTEMANAGE, notemid_seq);
			
			manageVO.setManage_id(newManageID);
			manageVO.setNote_id(newNoteID);
			manageVO.setRoot_id(newRootID);
			manageVO.setNote_target_id(accessor_id);
			manageVO.setNote_type("R");
			manageVO.setNote_save("N");
			manageVO.setNote_read("N");
			
			result = noteDao.noteManageWrite(manageVO);				
			if(result == 0)	{	throw processException("common.system.error");	}

			
			//Rgate 연계를 위해 
			//XR_NOTE_QUEUE INSERT
			result = noteDao.noteQueueWrite(manageVO);
			if(result == 0)	{	throw processException("common.system.error");	}
			
			
		}
		
		resultMap.put("result",Constant.RESULT_TRUE);
		return resultMap;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteListForInserting
	 * @param map
	 * @return List<NoteVO>
	 */
	public Map<String, Object> noteListForReInserting(HashMap<String, Object> map, SessionVO sessionVO) throws Exception {
		// TODO Auto-generated method stub
		
		int result=0;
		
		HashMap<String,Object> resultMap = new  HashMap<String, Object>();
		
		NoteDao noteDao = sqlSession.getMapper(NoteDao.class);
		//GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		//
		int noteid_seq = commonService.commonNextVal(Constant.COUNTER_ID_NOTE);
		String newNoteID = CommonUtil.getStringID(Constant.ID_PREFIX_NOTE, noteid_seq);		
		
		
		//XR_NOTE INSERT
		HashMap<String,Object> param = new  HashMap<String, Object>();
		param.put("note_id", newNoteID);
		param.put("root_id", map.get("root_id"));
		param.put("creator_id", sessionVO.getSessId());
		param.put("content", map.get("content"));
		param.put("note_from",  map.get("note_from"));		
		param.put("note_from_userid",map.get("note_from_userid"));
		param.put("note_from_groupid",  "");
		param.put("note_from_pgroupid",  "");
		
		//XR_NOTE INSERT
		result = noteDao.noteWrite(param);
		if(result == 0)	{	throw processException("common.system.error");	}
	
		//XR_NOTEMANAGE INSERT
		// 수신자 Insert
		List<NoteManageVO> manageVOList =  new ArrayList<NoteManageVO>();
		NoteManageVO manageVO = new NoteManageVO();
		// 발신자 Insert
		//============================================
		int notemid_seq = commonService.commonNextVal(Constant.COUNTER_ID_NOTEMANAGE);
		String newManageID = CommonUtil.getStringID(Constant.ID_PREFIX_NOTEMANAGE, notemid_seq);
/*
		System.out.println("=======================================");
		System.out.println("newManageID = " + newManageID);
		System.out.println("=======================================");*/
		
		manageVO.setManage_id(newManageID);
		manageVO.setNote_id(newNoteID);
		manageVO.setRoot_id(map.get("root_id").toString());
		manageVO.setNote_target_id(sessionVO.getSessId());
		manageVO.setNote_type("S");
		manageVO.setNote_save("N");
		manageVO.setNote_read("N");

		result = noteDao.noteManageWrite(manageVO);				
		if(result == 0)	{	throw processException("common.system.error");	}
		//=============================================

			
		manageVO = new NoteManageVO();

		notemid_seq = commonService.commonNextVal(Constant.COUNTER_ID_NOTEMANAGE);
		newManageID = CommonUtil.getStringID(Constant.ID_PREFIX_NOTEMANAGE, notemid_seq);

		manageVO.setManage_id(newManageID);
		manageVO.setNote_id(newNoteID);
		manageVO.setRoot_id(map.get("root_id").toString());
		manageVO.setNote_target_id(map.get("note_from_userid").toString());
		manageVO.setNote_type("R");
		manageVO.setNote_save("N");
		manageVO.setNote_read("N");

		result = noteDao.noteManageWrite(manageVO);				
		if(result == 0)	{	throw processException("common.system.error");	}


		//Rgate 연계를 위해 
		//XR_NOTE_QUEUE INSERT
		result = noteDao.noteQueueWrite(manageVO);
		if(result == 0)	{	throw processException("common.system.error");	}
			
		
		
		resultMap.put("result",Constant.RESULT_TRUE);
		return resultMap;
	}
	

	/**
	 * 
	 * <pre>
	 * 1. 개용 : AclItemListVO를 이용하여 NoteVO를 만듬
	 * </pre>
	 * @Method Name : aclWriteValid
	 * @param map
	 * @return
	 * @throws Exception List<AclItemVO>
	 */
	public static HashMap<String,Object> getNoteVOFromReciverList(String reciverListArrayList,GroupDao groupDao) throws Exception{
				
		/*reciverListArrayList = "[{\"accessor_id\" : \"user011\", \"accessor_isgroup\":\"F\"},"
               + "{\"accessor_id\" : \"GRP000000000004\", \"accessor_isgroup\":\"GT\"},"
               + "{\"accessor_id\":\"GRP000000000005\", \"accessor_isgroup\":\"GT\"}]";
		*/
		if(StringUtil.isEmpty(reciverListArrayList)){
			throw new Exception("common.required.error");
		}
		// JsonArray 객체 생성하기
		JSONArray jsonArray = JSONArray.fromObject(reciverListArrayList);
		String from_user_id="";
		String from_group_id="";
		String from_pgroup_id="";
		
		HashMap<String,Object> reciver = new HashMap<String,Object>();
		
		List<GroupedVO> groupUserList = new ArrayList<GroupedVO>();

		GroupedVO userid= new GroupedVO();
		List list = new ArrayList();

		if(jsonArray.size() > 0 ) {
			for(int j=0; j<jsonArray.size(); j++){
				
				
				if(jsonArray.getJSONObject(j).getString("accessor_isgroup").equals("GT")){
					reciver = new HashMap<String,Object>();
					//그룹아이디로 user_id 추출
					groupUserList = groupDao.groupedList(jsonArray.getJSONObject(j).getString("accessor_id"));
					reciver.put("USERID", groupUserList);
					list.add(reciver);
					from_group_id += jsonArray.getJSONObject(j).getString("accessor_id")+"#";			
					
				}else if(jsonArray.getJSONObject(j).getString("accessor_isgroup").equals("PT")){
					reciver = new HashMap<String,Object>();
					//프로젝트아이디로 user_id 추출
					groupUserList = groupDao.groupedList(jsonArray.getJSONObject(j).getString("accessor_id")); 
					reciver.put("USERID", groupUserList);
					list.add(reciver);
					
					from_pgroup_id += jsonArray.getJSONObject(j).getString("accessor_id")+"#";					
				}else{
					reciver = new HashMap<String,Object>();
					from_user_id += jsonArray.getJSONObject(j).getString("accessor_id")+"#";
					userid.setUser_id(jsonArray.getJSONObject(j).getString("accessor_id"));
					groupUserList.add(userid);
					reciver.put("USERID", groupUserList);
					list.add(reciver);
				}
			}
		}/*
		System.out.println("==================================");
		System.out.println("LIST_SIZE = " +list.size());
		System.out.println("from_group_id = " +from_group_id);
		System.out.println("from_pgroup_id = " +from_pgroup_id);
		System.out.println("from_user_id = " +from_user_id);
		System.out.println("==================================");*/
		
		groupUserList = new ArrayList<GroupedVO>();
		HashMap<String,Object> reciver01 = new HashMap<String,Object>();
		List list2 = new ArrayList();
		for(int i=0;i<list.size(); i++) {
			reciver01 = (HashMap<String, Object>) list.get(i);
			list2.add((List<GroupedVO>) reciver01.get("USERID"));
		}

		HashMap<String,Object> reciver02 = new HashMap<String,Object>();
		reciver02.put("note_target_id", list2);
		reciver02.put("from_user_id", from_user_id);
		reciver02.put("from_group_id", from_group_id);
		reciver02.put("from_pgroup_id", from_pgroup_id);
		return reciver02;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteNewTopNInfoList
	 * @param map
	 * @return List<NoteVO>
	 */
	public Map<String, Object> noteNewTopNInfoList(HashMap<String, Object> map) {
		HashMap<String,Object> resultMap = new  HashMap<String, Object>();
		
		List<NoteVO> noteList = new ArrayList<NoteVO>();
		NoteDao noteDao = sqlSession.getMapper(NoteDao.class);
		
		// 1.user_id로 읽지 않은 쪽지 목록 리스트 가져오기		
		noteList = noteDao.noteNewTopNInfoList(map);
		resultMap.put("list",noteList);	
		resultMap.put("count",noteList.size());			// TOP & MENU 쪽지 개수 표시	
		
		//TEST ========================================================================

		/*GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		HashMap<String,Object> resultList = new  HashMap<String, Object>();
		String ntlist ="";// "accessor_isgroup=accessor_id:"", accessor_isgroup:""";
		
		try{
			resultList = getNoteVOFromReciverList(ntlist,groupDao);
			
			String gid = (String) resultList.get("from_group_id");			
			String pid = (String) resultList.get("from_pgroup_id");
			String uid = (String) resultList.get("from_user_id");
			
			List<GroupedVO> groupUserList = new ArrayList<GroupedVO>();
			List list = (List) resultList.get("note_target_id");
			
			for(int i=0;i<list.size(); i++) {
				
				groupUserList = (List<GroupedVO>) list.get(i);
				for(int j=0;j<groupUserList.size(); j++) {
					
					System.out.println("==================================");				
					System.out.println("GROUP_ID = " +groupUserList.get(j).getGroup_id());
					System.out.println("USER_ID = " +groupUserList.get(j).getUser_id());
					System.out.println("==================================");
										
				 }
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}*/
		//========================================================================
		
		
		
		/*
		
		System.out.println("==========================" );
		System.out.println("NOTE RESULT SIZE : " + noteList.size());
		//System.out.println("sender_name = " +noteList.get(0).getSender_name());
		//System.out.println("newnote_cnt = " +noteList.get(0).getNewnote_cnt());
		System.out.println("==========================" );*/
				
		return resultMap;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteAllReceiveSendInfoList
	 * @param map
	 * @return List<NoteVO>
	 */
	public Map<String, Object> noteAllReceiveSendInfoList(HashMap<String, Object> map) {
		HashMap<String,Object> resultMap = new  HashMap<String, Object>();
		
		List<NoteVO> noteList = new ArrayList<NoteVO>();
		NoteDao noteDao = sqlSession.getMapper(NoteDao.class);
		
		// 1.user_id로 모든 쪽지 목록 리스트를 최신순으로 가져오기		
		noteList = noteDao.noteAllReceiveSendInfoList(map);
		resultMap.put("list",noteList);
		resultMap.put("result",Constant.RESULT_TRUE);
		return resultMap;
	}

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : 보낸쪽지 받은 쪽지의 보관 상태 update
	 * </pre>
	 * @Method Name : noteSaveUpdate
	 * @param map
	 * @return List<NoteVO>
	 */
	public int noteSaveUpdate(HashMap<String, Object> map, SessionVO sessionVO) throws Exception {
		NoteDao noteDao = sqlSession.getMapper(NoteDao.class);
		int result = 0;
		//HashMap<String, Object> resultMap = new HashMap<String, Object>();

		
		String manageId = StringUtil.getMapString(map, "manage_id");
		/*
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("mangeID = "+ manageId);
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
*/
		// 보관한 저장 상태값 update
		HashMap<String,Object> param = new  HashMap<String, Object>();
		param.put("user_id", sessionVO.getSessId());
		param.put("manage_id", manageId);
				
		result = noteDao.noteSaveUpdate(param);
		if(result == 0)	{	throw processException("common.system.error");	}
		
		return result;
	}


	/**
	 * 
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : 받은 쪽지의 읽음 상태 update
	 * </pre>
	 * @Method Name : noteReadUpdate
	 * @param map
	 * @return List<NoteVO>
	 */
	public int noteReadUpdate(HashMap<String, Object> map, SessionVO sessionVO) throws Exception {
		NoteDao noteDao = sqlSession.getMapper(NoteDao.class);
		int result = 0;
		//HashMap<String, Object> resultMap = new HashMap<String, Object>();

		
		String manageId = StringUtil.getMapString(map, "manage_id");
		
		// 보관한 저장 상태값 update
		HashMap<String,Object> param = new  HashMap<String, Object>();
		param.put("user_id", sessionVO.getSessId());
		param.put("manage_id", manageId);
				
		result = noteDao.noteReadUpdate(param);
		if(result == 0)	{	throw processException("common.system.error");	}
		
		return result;
	}
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : 보낸쪽지 받은 쪽지의 보관 상태 update
	 * </pre>
	 * @Method Name : noteSaveUpdate
	 * @param map
	 * @return int
	 */
	public int noteDelete(HashMap<String, Object> map, SessionVO sessionVO) throws Exception {
		NoteDao noteDao = sqlSession.getMapper(NoteDao.class);
		
		//HashMap<String, Object> resultMap = new HashMap<String, Object>();
		int result = 0;

		String noteId = StringUtil.getMapString(map, "note_id");		
		String manageId = StringUtil.getMapString(map, "manage_id");	
		String rootId = StringUtil.getMapString(map, "root_id");
		/*
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("noteId = "+ noteId);
		System.out.println("manageId = "+ manageId);
		System.out.println("rootId = "+ rootId);
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");*/
		// parameter setting
		HashMap<String,Object> param = new  HashMap<String, Object>();
		param.put("user_id", sessionVO.getSessId());
		param.put("manage_id", manageId);
		param.put("root_id", rootId);
		param.put("note_id", noteId);
		param.put("del_kbn", map.get("del_kbn"));
				
		//noteManage 테이블의 보관데이터가 아닌 쪽지 삭제
		result = noteDao.noteManageDelete(param);

		//root_id로 보관데이터의 갯수를 가져옴 
		int selectresult = noteDao.noteRootCount(param);
		
		//보관 데이터가 없으면 XR_NOTE의 데이터 삭제		
		if(result>0 && selectresult==0){
			result = noteDao.noteDelete(param);
		}
		
		if(result == 0)	{	throw processException("common.system.error");	}
		
		return result;
	}
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : 대화함 리스트 가져오기
	 * </pre>
	 * @Method Name : noteTalkList
	 * @param map
	 * @return List<NoteVO>
	 */
	public Map<String, Object> noteTalkList(HashMap<String, Object> map) {
		HashMap<String,Object> resultMap = new  HashMap<String, Object>();		

		List<NoteVO> noteList = new ArrayList<NoteVO>();
		NoteDao noteDao = sqlSession.getMapper(NoteDao.class);
		NoteVO nvo= new NoteVO();
		// 1.user_id로 대화함 목록 리스트 가져오기		
		noteList = noteDao.noteTalkList(map);
		
		for(int i=0; i<noteList.size(); i++){
			nvo= new NoteVO();
			nvo = noteList.get(i);
			String note_id= noteList.get(i).getNote_id();//.substring(3);
			String root_id= noteList.get(i).getRoot_id();//.substring(3);
			/*
			System.out.println("==================================================================");
			System.out.println("root_id = "+ root_id+":note_id =" + note_id);
			System.out.println("==================================================================");*/
			//if(note_id.equals(root_id) && noteList.get(i).getCreator_id().equals(noteList.get(i).getNote_target_id())){
			if(note_id.equals(root_id)){
				nvo.setNote_kbn(0);
			}else{
				nvo.setNote_kbn(1);
			}/*
			System.out.println("==================================================================");
			System.out.println(noteList.get(i).getCreator_id()+":" + noteList.get(i).getNote_kbn());
			System.out.println("==================================================================");*/
			
			noteList.set(i, nvo);
		}
		
		
		resultMap.put("list",noteList);		
		
		return resultMap;
	}

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : 대화함 리스트 가져오기
	 * </pre>
	 * @Method Name : noteTalkList
	 * @param map
	 * @return List<NoteVO>
	 */
	public Map<String, Object> noteTalkDetailList(HashMap<String, Object> map) {
		HashMap<String,Object> resultMap = new  HashMap<String, Object>();		

		List<NoteVO> noteList = new ArrayList<NoteVO>();
		NoteDao noteDao = sqlSession.getMapper(NoteDao.class);
		
		// 1.user_id, root_id 로 대화함 목록 상세 리스트 가져오기		
		noteList = noteDao.noteTalkDetailList(map);
		
		
		resultMap.put("list",noteList);		
		
		return resultMap;
	}

	
	
	public List<HashMap<String, Object>> noteValidList(HashMap<String,Object> map) throws Exception {
		
		List<HashMap<String, Object>> ret = new ArrayList<HashMap<String, Object>>();
		
		NoteDao noteDao = sqlSession.getMapper(NoteDao.class);

		/***********************************************************************************************************
		 * 주) 쪽지 받는사람 처리 파라미터정의 JsonArry 형식이며 아래의 필수값 포함해야됨.
		 * delDocList=[
		 * {"accessor_isgroup":"T","accessor_id":"GRP0000000001"},
		 * {"accessor_isgroup":"F","accessor_id":"user021"},
		 * {"accessor_isgroup":"F","accessor_id":"user034"}]
		 ***********************************************************************************************************/
		String reciverList =  map.get("reciveList") != null ? map.get("reciveList").toString() : "";
		
		// 1.입력값 유효성 체크
		if(reciverList.equals("") ||  reciverList.equals(""))	{
			throw processException("common.required.error");
		}
			
		// 2.JsonArray To List
		JSONArray jsonArray = JSONArray.fromObject(reciverList);
		if(jsonArray.size() > 0 ) {		
			 for(int j=0;j < jsonArray.size();j++)	{				 
				 
				 HashMap<String, Object> docInfo = new HashMap<String, Object>();
				 
				 docInfo.put("accessor_isgroup",jsonArray.getJSONObject(j).getString("accessor_isgroup").toString());
				 docInfo.put("accessor_id",jsonArray.getJSONObject(j).getString("accessor_id").toString());
				// docInfo.put("accessor_name",jsonArray.getJSONObject(j).getString("accessor_name").toString());
/*
					System.out.println("================================================");
					System.out.println(jsonArray.getJSONObject(j).getString("accessor_id").toString());
					System.out.println("================================================");*/
					
				 ret.add(docInfo);
			 }
		}

		return ret;
	}
	
	
	
	
	
	
}
