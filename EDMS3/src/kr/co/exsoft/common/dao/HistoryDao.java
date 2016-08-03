package kr.co.exsoft.common.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.exsoft.common.vo.DocumentHtVO;
import kr.co.exsoft.common.vo.HistoryVO;
import kr.co.exsoft.common.vo.PageHtVO;
/**
 * History 매퍼클래스
 * @author 패키지 개발팀
 * @since 2014.07.21
 * @version 3.0
 *
 */
@Repository(value = "historyDao")
public interface HistoryDao {

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더/문서유형/권한 이력을 등록한다. 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : historyWrite
	 * @param historyVO
	 * @return int
	 */
	public int historyWrite(HistoryVO historyVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서이력을 등록한다. 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentHtWrite
	 * @param documenthtVO
	 * @return int
	 */
	public int documentHtWrite(DocumentHtVO documenthtVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 첨부파일 조회이력을 등록한다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : pageHtWrite
	 * @param pageHtVO
	 * @return int
	 */
	public int pageHtWrite(PageHtVO pageHtVO);
	
	
	/**
	 * <pre>
	 * 1. 내용 : 문서 이력 목록을 조회한다. :: 삭제예정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentHtList
	 * @param map
	 * @return
	 */
	public List<DocumentHtVO> documentHtList(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : 문서 이력을 조회한다. 페이징처리
	 * </pre>
	 * @Method Name : docHtList
	 * @param map
	 * @return List<DocumentHtVO>
	 */
	public List<DocumentHtVO> docHtList(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 이력 조회수
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docHtPagingCount
	 * @param map
	 * @return int
	 */
	public int docHtPagingCount(HashMap<String, Object> map);
}
