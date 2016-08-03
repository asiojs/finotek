package kr.co.exsoft.document.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.exsoft.document.vo.DocumentVO;

/**
 * 
 * MyDocument 매퍼클래스
 * @author 패키지 개발팀
 * @since 2015.01.05
 * @version 3.0
 *
 */
@Repository(value = "myDocumentDao")
public interface MyDocumentDao {

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 나의문서 목록 수
	 * 2. 처리내용 : acl 필터링
	 * </pre>
	 * @Method Name : myDocumentListCnt
	 * @param map
	 * @return int
	 */
	public int myDocumentListCnt(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 나의문서 목록
	 * 2. 처리내용 : acl 필터링
	 * </pre>
	 * @Method Name : myDocumentList
	 * @param map
	 * @return List<DocumentVO>
	 */
	public List<DocumentVO> myDocumentList(HashMap<String, Object> map);
}
