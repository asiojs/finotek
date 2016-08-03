package kr.co.exsoft.rgate.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import kr.co.exsoft.rgate.vo.*;

/**
 * Rgate 매퍼클래스
 * @author 패키지 개발팀
 * @since 2014.07.21
 * @version 3.0
 *
 */
@Repository(value = "rgateDao")
public interface RgateDao {

	/**
	 * 
	 * <pre>
	 * 1. 개용 :  rGate 정책 등록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgatePolicyWrite
	 * @param map
	 * @return int
	 */
	public int rgatePolicyWrite(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : rGate 정책 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgatePolicyDelete
	 * @param map
	 * @return int
	 */
	public int rgatePolicyDelete(HashMap<String,Object> map);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  rGate 정책 조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgatePolicyDetail
	 * @param map
	 * @return String
	 */
	public String rgatePolicyDetail(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 관리 대상 확장자나 프로세스를 등록 한다
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgateListWrite
	 * @param rgateListVO
	 * @return int
	 */
	public int rgateListWrite(RGateListVO rgateListVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 관리 대상 확장자나 프로세스 목록 조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgateList
	 * @param map
	 * @return List<RGateListVO>
	 */
	public List<RGateListVO> rgateList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 관리 대상 확장자나 프로세스 수정 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgatePolicyUpdate
	 * @param map
	 * @return int
	 */
	public int rgatePolicyUpdate(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  관리 대상 확장자나 프로세스를 삭제 한다
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgateListDelete
	 * @param map
	 * @return int
	 */
	public int rgateListDelete(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 프로세스 예외 폴더  수정처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgateProcessUpdate
	 * @param map
	 * @return int
	 */
	public int rgateProcessUpdate(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 프로세스 예외 폴더 리스트를 얻는다 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgateProcessList
	 * @param map
	 * @return List<RGateProcessVO>
	 */
	public List<RGateProcessVO> rgateProcessList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 프로세스 예외 폴더 리스트를 등록 한다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgateProcessWrite
	 * @param map
	 * @return int
	 */
	public int rgateProcessWrite(RGateProcessVO vo );
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 프로세스 예외 폴더 리스트를 삭제 한다  
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgateProessDelete
	 * @param map
	 * @return int
	 */
	public int rgateProessDelete(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 프로세스가 사용중인지 체크한다
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : isProessUsing
	 * @param map
	 * @return int
	 */
	public int isProessUsing(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 로컬저장금지정책 목록을 등록 한다. 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgateMappingWrite
	 * @param map
	 * @return int
	 */
	public int rgateMappingWrite(RGateMappingVO rgateMappingVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  로컬저장금지정책 목록을 수정 한다. 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgateMappingUpdate
	 * @param map
	 * @return int
	 */
	public int rgateMappingUpdate(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 로컬저장금지정책 목록을 삭제 한다. 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgateMappingDelete
	 * @param map
	 * @return int
	 */
	public int rgateMappingDelete(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 로컬저장금지정책 목록 카운트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgatePolicyPagingCount
	 * @param map
	 * @return int
	 */
	public int rgatePolicyPagingCount(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 로컬저장금지정책 페이지 목록을 얻는다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgatePolicyPagingList
	 * @param map
	 * @return List<RGateMappingVO>
	 */
	public List<RGateMappingVO> rgatePolicyPagingList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 로컬저장금지정책 목록을 얻는다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgatePolicyList
	 * @param map
	 * @return List<CaseInsensitiveMap>
	 */
	public List<CaseInsensitiveMap> rgatePolicyList(HashMap<String,Object> map);
	
}
