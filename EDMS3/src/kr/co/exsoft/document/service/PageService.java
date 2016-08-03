package kr.co.exsoft.document.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

/**
 * Page 서비스 인터페이스
 *
 * @author 패키지팀
 * @since 2014. 9. 16.
 * @version 1.0
 * 
 */
@Transactional
public interface PageService {

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 중복파일 목록 가져오기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : duplicatePageList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> duplicatePageList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : doc_id 기준으로 파일 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : comDocPageList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> comDocPageList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : doc_id리스트로  첨부파일 목록얻기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docPageListForURLMail
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> docPageListForURLMail(HashMap<String,Object> map) throws Exception;
}
