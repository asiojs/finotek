package kr.co.exsoft.process.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kr.co.exsoft.common.vo.SessionVO;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

/**
 * Process 서비스 인터페이스
 * @author 패키지 개발팀
 * @since 2015.03.12
 * @version 3.0
 *
 */
@Transactional
public interface ProcessService {
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 좌측 메뉴 상단엣 문서에 대한 count를 가져오기 
	 * 2. 처리내용 : 사용자 ID기준으로 협업 관련 문서 count를 가져오기
	 * </pre>
	 * @Method Name : processCount
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> processCount(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 협업 메뉴별 목록 가져오기 
	 * 2. 처리내용 : type기준으로 협업 메뉴별 목록 가져오기
	 * </pre>
	 * @Method Name : processList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> processList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 최근 협업 등록 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : processRecentlyList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> processRecentlyList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 최근 협업 등록 목록에서 기본 정보 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : selectProcessRecently
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> selectProcessRecently(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 협업 신규 등록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : processWrite
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @param request
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> processWrite(SessionVO sessionVO, Model model, HashMap<String, Object> map, HttpServletRequest request) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 협업 상세 정보
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : processDetail
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> processDetail(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 협업 승인 요청
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : approveAction
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> approveAction(HashMap<String, Object> map, SessionVO sessionVO) throws Exception;
	

}
