package kr.co.exsoft.rgate.service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.sql.SQLException;

import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.rgate.vo.RGateMappingVO;
import kr.co.exsoft.rgate.vo.RGateListVO;
import kr.co.exsoft.rgate.vo.RGateProcessVO;

/**
 * RGate 서비스 인터페이스
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Transactional
public interface RgateService {
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : RGATE 관련 페이지 목록 처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgatePageList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> rgatePageList(HashMap<String,Object> map) throws Exception;

	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 확장자/프로세스 목록 처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgateExtProcList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> rgateExtProcList(HashMap<String,Object> map,SessionVO sessionVO) throws Exception;
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  RGate 정책 등록처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgatePolicyManager
	 * @param rgateMappingVO
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> rgatePolicyManageWrite(List<RGateMappingVO> rgateMappingVO,HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :   RGate 정책 삭제처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgatePolicyManageDelete
	 * @param delList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> rgatePolicyManageDelete(List<HashMap<String,Object>> delList,HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 확장자/프로세스 등록/수정/삭제 처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgateListManager
	 * @param rgateMappingVO
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> rgateListManager(List<RGateListVO> rgateMappingVO,HashMap<String,Object> map) throws Exception;
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : rGate 관리 매핑 테이블 등록 데이터 설정 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setRgateMappingList
	 * @param map
	 * @return List<RGateMappingVO>
	 */
	public List<RGateMappingVO> setRgateMappingList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 등록된 저장정책 목록을 가져온다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgateMappingList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> rgateMappingList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 등록된 프로그램별 작업폴더 리스트를 가져온다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgateExceptionList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> rgateExceptionList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  rGate 관리 매핑 테이블 삭제 데이터 설정 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setRgateMappingDelList
	 * @param map
	 * @return
	 * @throws Exception List<HashMap<String,Object>>
	 */
	public List<HashMap<String,Object>> setRgateMappingDelList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  rGate 관리 매핑 테이블 수정 데이터 설정 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setRgateMappingUpdateList
	 * @param map
	 * @return
	 * @throws Exception List<HashMap<String,Object>>
	 */
	public List<HashMap<String,Object>> setRgateMappingUpdateList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : RGate 정책 수정처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgatePolicyManageUpdate
	 * @param policyList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> rgatePolicyManageUpdate(List<HashMap<String,Object>> policyList,HashMap<String,Object> map) throws Exception;

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 프로그램별 작업폴더 조회 목록 가져오기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : exceptionPageList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> exceptionPageList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 프로그램별 작업폴더 관리 등록 유효성 체크
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : exceptInsertValid
	 * @param map
	 * @return
	 * @throws Exception List<RGateProcessVO>
	 */
	public List<RGateProcessVO> exceptInsertValid(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 프로그램별 작업폴더 등록/수정/삭제처리 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : exceptProcess
	 * @param rgateProcess
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> exceptProcess(List<RGateProcessVO> rgateProcess,HashMap<String,Object> map) throws Exception;

	/**
	 * 
	 * <pre>
	 * 1. 개용 : RGate관리 리스트 테이블 등록 처리
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : rgateListWrite
	 * @param rGateListVO
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> rgateListWrite(RGateListVO rGateListVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : RGate 관리 리스트 테이블 삭제 데이터 설정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setRgateListDelList
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> setRgateListDelList(HashMap<String,Object> map) throws Exception;
	
	/**
	 *
	 * <pre>
	 * 1. 개용 : RGate 관리 리스트 테이블 삭제 처리
	 * 2. 처리내용 : 
	 * </pre> 
	 * @Method Name : rgateListDelete
	 * @param delList
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> rgateListDelete(List<HashMap<String,Object>> delList,HashMap<String,Object> map) throws Exception;
	
	

	
}
