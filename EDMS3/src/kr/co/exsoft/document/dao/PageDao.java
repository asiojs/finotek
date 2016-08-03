package kr.co.exsoft.document.dao;

import java.util.HashMap;
import java.util.List;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import kr.co.exsoft.document.vo.PageVO;

/**
 * Page 매퍼클래스
 * @author 패키지 개발팀
 * @since 2014.07.29
 * @version 3.0
 *
 */
@Repository(value = "pageDao")
public interface PageDao {

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 중복파일 목록 카운트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : duplicatePageCount
	 * @param map
	 * @return int
	 */
	public int duplicatePageCount(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  중복파일 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : duplicatePageList
	 * @param map
	 * @return List<PageVO>
	 */
	public List<PageVO> duplicatePageList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  페이지 정보 수정하기 :: 컬럼확정해서 사용하세요.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : pageInfoUpdate
	 * @param map
	 * @return int
	 */
	public int pageInfoUpdate(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : XR_FILED 페이지 리스트 가져오기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : xrPageList
	 * @param map
	 * @return List<CaseInsensitiveMap>
	 */
	public List<CaseInsensitiveMap> xrPageList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : XR_FILED 정보 삭제처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : xrFiledDelete
	 * @param map
	 * @return int
	 */
	public int xrFiledDelete(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 공통 문서 첨부파일 목록 리스트 가져오기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : comDocPageList
	 * @param map
	 * @return List<PageVO>
	 */
	public List<PageVO> comDocPageList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 파일 정보 삭제처리 :: PAGE_ID/IS_DELETED
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : xrPageDelete
	 * @param map
	 * @return int
	 */
	public int xrPageDelete(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 파일 정보 등록처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : writePage
	 * @param pageVO
	 * @return int
	 */
	public int writePage(PageVO pageVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서-파일 정보 등록처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : writeXrFiled
	 * @param map
	 * @return int
	 */
	public int writeXrFiled(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 다운로드 대상 목록 리스트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : downPageList
	 * @param map
	 * @return List<PageVO>
	 */
	public List<PageVO> downPageList(HashMap<String,Object> map); 
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : URL 링크복사 첨부파일 정보
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : pageDetailInfo
	 * @param map
	 * @return PageVO
	 */
	public PageVO pageDetailInfo(HashMap<String,Object> map);
	
	/**
	 * <pre>
	 * 1. 개용 : doc_id리스트로  첨부파일 목록얻기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docPageListForURLMail
	 * @param map
	 * @return List<PageVO>
	 */
	public List<PageVO> docPageListForURLMail(HashMap<String,Object> map);
}
