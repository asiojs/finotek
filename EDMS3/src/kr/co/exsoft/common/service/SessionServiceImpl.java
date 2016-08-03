package kr.co.exsoft.common.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import kr.co.exsoft.eframework.library.ExsoftAbstractServiceImpl;
import kr.co.exsoft.user.dao.GroupDao;
import kr.co.exsoft.user.dao.UserDao;
import kr.co.exsoft.user.vo.UserVO;
import kr.co.exsoft.common.vo.MenuAuthVO;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.user.vo.ConnectLogVO;
import kr.co.exsoft.user.vo.GroupVO;
import kr.co.exsoft.user.vo.LoginLogVO;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.handler.SessionManager;
import kr.co.exsoft.eframework.library.LocaleLibrary;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.util.StringUtil;

/**
 * 세션처리 구현부분
 * @author 패키지 개발팀
 * @since 2014.08.05
 * @version 3.0
 *
 */
@Service("sessionService")
public class SessionServiceImpl  extends ExsoftAbstractServiceImpl implements SessionService {
	
	@Autowired
	@Qualifier("sqlSession")
	private SqlSession sqlSession;

	@Autowired
	private CommonService commonService;
	
	@Override
	public SessionVO setSessionVO(UserVO userVO,HttpServletRequest request,HashMap<String,Object> param) throws Exception {
		
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		
		SessionVO sessionVO = new SessionVO();
		
		// 1.세션 사용자 정보 구하기.
		HttpSession session = request.getSession();
		sessionVO = setSessionInfo(session,userVO,param,request); 
		
		// 2.XR_CONNECT_LOG INSERT	
		connectLog(userDao,sessionVO,request,param);
		
		// 3.중복로그인 처리관련
		if(!ConfigData.getBoolean("MULTI.LOGIN.DENY"))	{			
			
			// 3-1. XR_LOGIN_LOG DELETE
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("user_id",sessionVO.getSessId());
			userDao.loginLogDelete(map);									
			
			// 3-2. XR_LOGIN_LOG INSERT
			LoginLogVO loginLogVO = new LoginLogVO();
			loginLogVO.setUser_id(sessionVO.getSessId());
			loginLogVO.setSession_id(sessionVO.getSessionId());
			loginLogVO.setConnect_ip(request.getRemoteAddr());
			userDao.loginLogWrite(loginLogVO);							
		}
		
		// session listener 추가		
		session.setAttribute("sessionVO",sessionVO);					
		session.setAttribute("listener", new SessionManager());
		
		// default locale session -- 영/한 버전일 경우 변경 작업
		LocaleLibrary.setLocaleInfo(request,ConfigData.getString("LANGUAGE"));
		
		return sessionVO;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 세션 객체 생성하기.
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : setSessionInfo
	 * @param session
	 * @param userVO
	 * @param param
	 * @return SessionVO
	 */
	@SuppressWarnings("unchecked")
	public SessionVO setSessionInfo(HttpSession session,UserVO userVO,HashMap<String,Object> param,HttpServletRequest request)
	throws Exception {
		
		SessionVO sessionVO = new SessionVO();
		
		// 1. 세션기본정보 
		sessionVO.setSessionId(session.getId());								// 새션 객체ID
		sessionVO.setSessId(userVO.getUser_id());
		sessionVO.setSessName(userVO.getUser_name_ko());
		sessionVO.setSessJobtitle(userVO.getJobtitle());
		sessionVO.setSessRole_id(userVO.getRole_id());
		sessionVO.setSessGroup_id(userVO.getGroup_id());
		sessionVO.setSessGroup_nm(userVO.getGroup_nm());
		sessionVO.setSessTheme(userVO.getTheme());
		sessionVO.setSessPage_size(userVO.getPage_size());
		sessionVO.setSessContent(param.get("content").toString());		
		sessionVO.setSessLanguage(userVO.getLanguage());
		sessionVO.setSessContextRoot(param.get("contextRoot").toString());
		sessionVO.setSessRemoteIp(request.getRemoteAddr());
		sessionVO.setSessSearchYear(userVO.getDoc_search());
		sessionVO.setSessManage_group(userVO.getManage_group());
		sessionVO.setSessManage_group_nm(userVO.getManage_group_nm());		
		sessionVO.setSessRole_nm(userVO.getRole_nm());
		sessionVO.setSessDocSearch(userVO.getDoc_search());
		
		// 1차고도화 추가항목(미리보기,첨부파일 이미지 미리보기,사용자Email)
		sessionVO.setSessViewType(userVO.getView_type());
		sessionVO.setSessIconPrewiew(userVO.getIcon_preview());
		sessionVO.setSessEmail(userVO.getEmail());
		
		// 2. 로그인 접속 위치 구분 :: U-사용자화면 A-관리자화면
		if(param.get("content").toString().equals(Constant.SESSION_USER)) {
			sessionVO.setSessLocation(Constant.LOCATION_USER);	
		}else {
			sessionVO.setSessLocation(Constant.LOCATION_ADMIN);
		}		
		
		// 3. 문서목록 기본검색기간		
		Calendar eday = Calendar.getInstance();
		Calendar sday = (Calendar)eday.clone();				
		
		sday.add(Calendar.YEAR,-(Integer.parseInt(userVO.getDoc_search())));
		sessionVO.setSessStartDt(StringUtil.print(sday));
		sessionVO.setSessEndDt(StringUtil.print(eday));
		
		// 4. 사용자 ROLE 권한 폴더 적용 (폴더/문서/권한)
		HashMap<String,Object> map = new HashMap<String,Object>();		
		map.put("role_id",sessionVO.getSessRole_id());
		
		// 5. role_id에 해당하는 menu_auth 정보를 가져 온다.
		sessionVO.setSessMenuAuth((List<MenuAuthVO>)commonService.menuAuthList(map).get("list"));

		// 6. 사용자 소속 프로젝트 부서 목록 리스트 
		map.clear();
		map.put("is_default",Constant.F);
		map.put("map_id",Constant.MAP_ID_PROJECT);
		map.put("user_id",sessionVO.getSessId());
		sessionVO.setSessProjectGroup(dualGroupList(map));
		
		// 7. 사용자 소속 상위 부서 리스트
		map.clear();
		map.put("group_id", sessionVO.getSessGroup_id());
		sessionVO.setSessParentGroup(parentGroupList(map));
		
		return sessionVO;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 프로젝트/겸직 대상 부서 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : dualGroupList
	 * @param map
	 * @return
	 * @throws Exception List<String>
	 */
	public List<String> dualGroupList(HashMap<String,Object> map) throws Exception{
	
		List<String> ret = new ArrayList<String>();
		
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		ret = groupDao.dualGroupList(map);
		
		return ret;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자의 모든 상위 부서 목록 가져오기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : parentGroupList
	 * @param map
	 * @return
	 * @throws Exception List<String>
	 */
	public List<String> parentGroupList(HashMap<String,Object> map) throws Exception {
		
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		List<String> ret = new ArrayList<String>();
		
		//groupDetail
		String group_id = map.get("group_id") != null ? map.get("group_id").toString() : "";
		int loopCnt = 0;
		while(!StringUtil.isEmpty(group_id) && !group_id.equals(Constant.GROUP_TOP_ID)){
			GroupVO groupVO = new GroupVO();
			groupVO = groupDao.groupDetail(group_id);
			ret.add(groupVO.getParent_id());
			group_id = groupVO.getParent_id();
			
			//무한 loop 방지 :: 부서 depth 최대한 10000개
			loopCnt++;
			if( loopCnt == 10000)
				break;
		}
		
		return ret;
	}
	
	
	@Override
	public void sessionOut(HttpSession session,SessionVO sessionVO,HttpServletRequest request,HashMap<String,Object> param) throws Exception {
		
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		// 1.XR_CONNECT_LOG INSERT
		connectLog(userDao,sessionVO,request,param);
		
		// 2.XR_LOGIN_LOG 테이블 초기화
		map.put("session_id",session.getId());
		LoginLogVO loginLogVO = userDao.loginLogDetail(map);
		
		if(loginLogVO != null) {			
			// 3. XR_LOGIN_LOG DELETE
			userDao.loginLogDelete(map);
		}
		        
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 접속 로그 공통 처리(로그인/로그아웃)
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : connectLog
	 * @param userDao
	 * @param sessionVO
	 * @param request
	 * @param param
	 * @throws Exception
	 */
	public void connectLog(UserDao userDao,SessionVO sessionVO,HttpServletRequest request,HashMap<String,Object> param) 
			throws Exception {

		ConnectLogVO connectLogVO = new ConnectLogVO();
				
		// XR_CONNECT_LOG INSERT		
		connectLogVO.setConnect_log_seq(commonService.commonNextVal(Constant.COUNTER_ID_CONNECT_LOG));
		connectLogVO.setUser_id(sessionVO.getSessId());
		connectLogVO.setUser_nm(sessionVO.getSessName());
		connectLogVO.setGroup_id(sessionVO.getSessGroup_id());
		connectLogVO.setGroup_nm(sessionVO.getSessGroup_nm());
		connectLogVO.setLogin_type(param.get("login_type").toString());
		connectLogVO.setConnect_type(param.get("connect_type").toString());		
		connectLogVO.setConnect_ip(request.getRemoteAddr());
		connectLogVO.setCert_yn(param.get("cert_yn").toString());
		userDao.connectLogWrite(connectLogVO);						

	}

}
