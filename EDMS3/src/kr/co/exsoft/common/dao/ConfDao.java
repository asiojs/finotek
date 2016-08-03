package kr.co.exsoft.common.dao;

import java.util.HashMap;
import java.util.List;

import kr.co.exsoft.common.vo.ConfVO;

import org.springframework.stereotype.Repository;
import org.apache.commons.collections.map.CaseInsensitiveMap;

/**
 * 환경설정 관련 DAO
 *
 * @author 패키지팀
 * @since 2014. 9. 10.
 * @version 1.0
 * 
 */
@Repository(value = "confDao")
public interface ConfDao {
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 시스템 환경설정 정보 열람
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : sysconfigDetail
	 *  @param map
	 * @return List
	 */
	public List<ConfVO> sysConfigDetail(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 시스템 환경설정 수정 처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : sysConfigUpdate
	 * @param map
	 * @return int
	 */
	public int sysConfigUpdate(HashMap<String,Object> map);

	/**
	 * 
	 * <pre>
	 * 1. 개용 :  감사 설정 정보 수정 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : auditConfigUpdate
	 * @param map
	 * @return int
	 */
	public int auditConfigUpdate(HashMap<String,Object> map);
		
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 휴지통 관리 정책 수정 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : trashConfigUpdate
	 * @param map
	 * @return int
	 */
	public int trashConfigUpdate(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 버저관리 정책 가져오기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : versionConfigDetail
	 * @param map
	 * @return List<CaseInsensitiveMap>
	 */
	public List<CaseInsensitiveMap> versionConfigDetail();

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 버전관리 정책 수정하기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : versionConfigUpdatel
	 * @param map
	 * @return int
	 */
	public int versionConfigUpdate(HashMap<String,Object> map);

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 첨부파일 정책 수정. 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : fileConfigUpdate
	 * @param map
	 * @return int
	 */
	public int fileConfigUpdate(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : URL 유효기간 설정 가져오기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : urlConfigDetail
	 * @param map
	 * @return CaseInsensitiveMap
	 */
	public CaseInsensitiveMap urlConfigDetail();
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : URL 유효기간 설정 변경하기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : urlConfigUpdate
	 * @param map
	 * @return int
	 */
	public int urlConfigUpdate(HashMap<String,Object> map);
	
}
