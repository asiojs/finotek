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
@Repository(value = "workDocumentDao")
public interface WorkDocumentDao {

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 업무문서/개인문서 목록 수
	 * 2. 처리내용 : acl 필터링
	 * </pre>
	 * @Method Name : workDocumentListCnt
	 * @param map
	 * @return int
	 */
	public int workDocumentListCnt(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 업무문서/개인문서 목록
	 * 2. 처리내용 : acl 필터링
	 * </pre>
	 * @Method Name : workDocumentList
	 * @param map
	 * @return List<DocumentVO>
	 */
	public List<DocumentVO> workDocumentList(HashMap<String, Object> map);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 메인페이지 문서목록 리스트 구하기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : mainDocumentList
	 * @param map
	 * @return List<DocumentVO>
	 */
	public List<DocumentVO> mainDocumentList(HashMap<String, Object> map);
}
