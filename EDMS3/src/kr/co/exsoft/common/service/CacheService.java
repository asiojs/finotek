package kr.co.exsoft.common.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * 메모리 캐쉬 구현부분
 *
 * @author 패키지 개발팀
 * @since 2014. 11. 4.
 * @version 1.0
 * 
 */
@Transactional
public interface CacheService {
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 메모리에 저장된 캐쉬 정보를 가져 온다. 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getCache
	 * @param cacheName : ehcache-default.xml에 설정된 cache name
	 * @param cacheKey : cacheName에 저장된 cache를 가져오기 위한 key
	 * @return
	 * @throws Exception Object
	 */
	public Object getCache(String cacheName, String cacheKey) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 기존 캐쉬값을 새로 캐쉬값으로 변경 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : replaceCache
	 * @param cacheName : ehcache-default.xml에 설정된 cache name
	 * @param cacheKey : cacheName에 저장된 cache를 가져오기 위한 key
	 * @param obj : 변경할 캐쉬 값
	 * @throws Exception void
	 */
	public void replaceCache(String cacheName, String cacheKey, Object obj) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 선택한 폴더가 관리대상 폴더인지 체크
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : menuAuthByFolderID
	 * @param folder_id
	 * @param group_id
	 * @return
	 * @throws Exception boolean
	 */
	public boolean menuAuthByFolderID(String folder_id, String group_id) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더ID 기준으로 폴더의 전체 경로명을 가져 온다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getFolderFullpathNameByFolderId
	 * @param folder_id
	 * @param isFristSlash
	 * @return
	 * @throws Exception String
	 */
	public String getFolderFullpathNameByFolderId(String folder_id, boolean isFristSlash) throws Exception;
	
}
