package kr.co.exsoft.common.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import kr.co.exsoft.common.vo.CodeVO;

/**
 * Code 매퍼클래스
 * @author 패키지 개발팀
 * @since 2014.07.21
 * @version 3.0
 *
 */
@Repository(value = "codeDao")
public interface CodeDao {

	/**
	 * 
	 * <pre>
	 * 1. 개요 : 코드 정보 가져오기
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : codeDetail
	 * @param map
	 * @return CodeVO
	 */
	public CodeVO codeDetail(HashMap<String,Object> map); 
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 코드 목록 가져오기 SELECTBOX
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : codeSelectList
	 * @param map
	 * @return List
	 */
	public List<CodeVO> codeSelectList(HashMap<String,Object> map);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 코드 목록 가져오기 페이징
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : codePagingList
	 * @param map
	 * @return List
	 */
	public List<CodeVO> codePagingList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 코드 목록 가져오기 카운트
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : codePagingCount
	 * @param map
	 * @return int
	 */
	public int codePagingCount(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 공통코드 등록처리 :: ROLE만 사용됨
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : codeWrite
	 * @param codeVO
	 * @return int
	 */
	public int codeWrite(CodeVO codeVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : ROLE만 사용됨
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : codeDelete
	 * @param map
	 * @return int
	 */
	public int codeDelete(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 공통코드 수정처리 :  ROLE만 사용됨
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : codeUpdate
	 * @param codeVO
	 * @return
	 */
	public int codeUpdate(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 그룹코드의 Max Sort Index 값 가져오기
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : codeMaxSortIndex
	 * @param map
	 * @return
	 */
	public int codeMaxSortIndex(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 코드가 사용중인 체크한다.
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : isRoleUsing
	 * @param map
	 * @return
	 */
	public int isRoleUsing(HashMap<String,Object> map);
	
}
