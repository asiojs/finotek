package kr.co.exsoft.common.service;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.sql.SQLException;

import kr.co.exsoft.common.vo.CodeVO;
import kr.co.exsoft.common.vo.MenuAuthVO;
import kr.co.exsoft.common.vo.HistoryVO;
import kr.co.exsoft.common.vo.RecentlyObjectVO;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.vo.PageVO;


/**
 * 메뉴/코드 서비스 인터페이스
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Transactional
public interface CommonService {

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 카운터테이블 증가값 가져오기 :: nextValTable / nextVal 통합처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : commonNextVal
	 * @param counter_id
	 * @return
	 * @throws Exception int
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor ={ Exception.class,SQLException.class})
	public int commonNextVal(String counter_id) throws Exception ;
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 함수 이용해서 현재값 가져오기 
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : currentVal
	 * @param counter_id
	 * @return int
	 * @throws Exception
	 */
	public int currentVal(String counter_id) throws Exception ;
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 테이블 이용해서 다음값 가져오기
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : nextValTable
	 * @param String : counter_id
	 * @return int
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor ={ Exception.class,SQLException.class})
	public int nextValTable(String counter_id) throws Exception;
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 코드 정보 가져오기
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : codeDetail
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public CodeVO codeDetail(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 코드 목록 가져오기
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : codeList
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<CodeVO> codeList(HashMap<String,Object> map) throws Exception;
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 다음에디터 등록 처리 샘플
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : editorWrite
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int editorWrite(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 다음에디터 등록 내용 출력
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : editorDetailInfo
	 * @param map
	 * @return CaseInsensitiveMap
	 * @throws Exception
	 */
	public CaseInsensitiveMap editorDetailInfo(HashMap<String,Object> map) throws Exception;
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : EDMS 사용자 라이센스 유효성 체크
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : checkUserLicense
	 * @return boolean
	 * @throws Exception
	 */
	public boolean checkUserLicense() throws Exception;

	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 코드리스트 페이지 목록으로 가져오기.
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : codePageList
	 * @param map
	 * @return Map
	 * @throws Exception
	 */
	public Map<String, Object> codePageList(HashMap<String,Object> map) throws Exception;
	
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 메뉴권한 목록 가져오기
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : menuAuthList
	 * @param map
	 * @return List
	 * @throws Exception
	 */
	public Map<String, Object> menuAuthList(HashMap<String,Object> map) throws Exception;

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 메뉴권한 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : adminMenuAuthList
	 * @param map
	 * @return
	 * @throws Exception List<MenuAuthVO>
	 */
	public List<MenuAuthVO> adminMenuAuthList(HashMap<String,Object> map) throws Exception;

	/**
	 * 
	 * <pre>
	 * 1. 개요 :  코드 등록/수정/삭제처리 - 다국어 Base를 위해 @Transactional 선언
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : codeManager
	 * @param codeVO
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> codeManager(CodeVO codeVO,HashMap<String,Object> map) throws Exception;

	/**
	 * 
	 * <pre>
	 * 1. 개요 : 메뉴권한 등록/수정/삭제처리 
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : menuAuthManager
	 * @param menuAuthVO
	 * @param map
	 * @return Map
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> menuAuthManager(List<MenuAuthVO> menuAuthList,HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 메뉴권한 처리(수정/삭제) 공통 파라미터 처리 부분
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : setMenuAuthParam
	 * @param inputStr
	 * @return Str
	 */
	public List<MenuAuthVO> setMenuAuthParam(String[] inputStr);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 메뉴권한 처리(등록) 공통 파라미터 처리 부분
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : setMenuAuthParam
	 * @param inputStr
	 * @param map
	 * @return List
	 */
	public List<MenuAuthVO> setMenuAuthParam(String[] inputStr,HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 메뉴권한 메뉴목록 리스트 출력 처리.
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : menuList
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> menuList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 관리자 접속 메뉴 권한 정보 가져오기 -- 삭제대상
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getMenuAuth
	 * @param map
	 * @return
	 * @throws Exception MenuAuthVO
	 */
	public String getMenuAuth(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더/문서유형/권한이력 등록처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : historyWrite
	 * @param historyVO
	 * @throws Exception void
	 */
	public int historyWrite(HistoryVO historyVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 시스템 환경설정 조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : confDetail
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> confDetail(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 휴지통관리 정책 가져오기 ::  개인/시스템
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : trashConfig
	 * @param map
	 * @return
	 * @throws Exception List<HashMap<String,Object>>
	 */
	public List<HashMap<String,Object>> trashConfig(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 감사설정 정보 조회 : 배치프로그램
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : auditConfig
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> auditConfig(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 시스템 환경설정 수정 처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : confProc
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> confProc(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  페이지 네비게이션 메뉴 정보 가져오기. -- 삭제대상
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : pageMenuInfo
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> pageMenuInfo(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 권한 그룹 리스트 목록 가져오기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : authGroupList
	 * @param map
	 * @return
	 * @throws Exception List<String>
	 */
	public List<String> authGroupList(String part,SessionVO sessionVO) throws Exception;
	 
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더 FullPath 경로 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : folderFullPath
	 * @param map
	 * @return
	 * @throws Exception String
	 */
	public String folderFullPath(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  첨부파일 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setPageList
	 * @param map
	 * @return
	 * @throws Exception List<PageVO>
	 */
	public List<PageVO> setPageList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  첨부파일 목록 가져오기 Appliance
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setPageLocalList
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<PageVO> setPageLocalList(HashMap<String, Object> map) throws Exception;
		
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 첨부파일 이력처리하기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : pageHtWrite
	 * @param pageList
	 * @param sessionVO
	 * @throws Exception void
	 */
	public void pageHtWrite(List<PageVO> pageList,SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 관리자 화면단 메뉴정보 및 관리범위 공통처리 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setPageToModel
	 * @param map
	 * @param menuInfo
	 * @param partInfo
	 * @param sessionVO
	 * @throws Exception void
	 */
	public void setPageToModel(HashMap<String, Object> map,Map<String, Object> menuInfo,Map<String, Object> partInfo,SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : URL 복사 첨부파일 정보 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : urlPageInfo
	 * @param map
	 * @return
	 * @throws Exception PageVO
	 */
	public PageVO urlPageInfo(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 최근 등록(문서,협업,폴더)에 대한 목록 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : deleteRecently
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> deleteRecently(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 첨부파일 삭제 처리
	 * 2. 처리내용 : 문서, 협업 등록 실패 시 exRep ECM에 등록된 파일은 XR_DELETEFILE_QUEUE에 삽입 후
	 *           배치 작업으로 해당 파일을 삭제 한다.
	 * </pre>
	 * @Method Name : insertDeleteFileQueue
	 * @param map
	 * @return int
	 * @throws Exception
	 */
	public int insertDeleteFileQueue(HashMap<String, Object> map) throws Exception;

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 관리자 환경 설정에 설정된 파일 정보
	 * 2. 처리내용 : 등록파일제한, 첨부파일갯수제한, 첨부파일 사이즈 제한, 첨부파일 총 사이즈 제한
	 * </pre>
	 * @Method Name : configFileInfo
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> configFileInfo(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 최근 등록 현황을 개인별 등록
	 * 2. 처리내용 : 문서, 폴더, 협업에 대한 개인별 최근 등록 현황
	 * </pre>
	 * @Method Name : insertRecentlyObject
	 * @param recentlyVo
	 * @return
	 * @throws Exception int
	 */
	public int insertRecentlyObject(RecentlyObjectVO recentlyVo) throws Exception;
}
