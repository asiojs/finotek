package kr.co.exsoft.common.dao;

import java.util.HashMap;
import java.util.List;

import kr.co.exsoft.common.vo.RecentlyObjectVO;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;


/**
 * 공통처리 매퍼 클래스
 * @author 패키지 개발팀
 * @since 2014.07.28
 * @version 3.0
 *
 */
@Repository(value = "commonDao")
public interface CommonDao {

	/**
	 * 
	 * <pre>
	 * 1. 개요 : NEXT_VAL FUNCTION
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : commonNextVal
	 * @param map
	 * @return int
	 */
	public int comNextVal(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : CURRENT_VAL FUNCITON 
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : commonCurrentVal
	 * @param map
	 * @return
	 */
	public int comCurrentVal(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 테이블 이용해서 카운터 증가시키기
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : comNextValInc
	 * @param map
	 * @return
	 */
	public int comNextValInc(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 테이블 이용해서 현재값 가져오기
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : comCurrvalTable
	 * @param map
	 * @return
	 */
	public int comCurrvalTable(HashMap<String,Object> map);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 다음에디터 등록처리 샘플
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : editorWrite
	 * @param map
	 * @return int
	 */
	public int editorWrite(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 다음에디터 등록 내용 보기 최신거 1개
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : editorDetailInfo
	 * @param map
	 * @return CaseInsensitiveMap
	 */
	public CaseInsensitiveMap editorDetailInfo(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 최근 등록(문서,폴더,협업)에 대한 목록 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : deleteRecently
	 * @param map
	 * @return int
	 */
	public int deleteRecently(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 첨부파일 삭제 처리
	 * 2. 처리내용 : 문서, 협업 등록 실패 시 exRep ECM에 등록된 파일은 XR_DELETEFILE_QUEUE에 삽입 후
	 *           배치 작업으로 해당 파일을 삭제 한다.
	 * </pre>
	 * @Method Name : insertDeleteFileQueue
	 * @param map
	 * @return int
	 */
	public int insertDeleteFileQueue(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 최근 등록 현황 가져오기
	 * 2. 처리내용 : 문서, 폴더, 협업 등록에 대한 사용자 등록 현황
	 * </pre>
	 * @Method Name : selectRecentlyObject
	 * @return List<RecentlyObjectVO>
	 */
	public List<RecentlyObjectVO> selectRecentlyObject(RecentlyObjectVO recentlyVo);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 최근 등록 현황 등록
	 * 2. 처리내용 : 문서, 폴더, 협업 최근 등록 현황 등록
	 * </pre>
	 * @Method Name : insertRecentlyObject
	 * @param recentlyVo
	 * @return int
	 */
	public int insertRecentlyObject(RecentlyObjectVO recentlyVo);
	
}
