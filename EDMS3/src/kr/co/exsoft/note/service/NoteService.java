package kr.co.exsoft.note.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.note.vo.NoteVO;

import org.springframework.transaction.annotation.Transactional;

	/**
	 * Note 서비스 인터페이스
	 * @author 패키지 개발팀
	 * @since 2015.03.02
	 * @version 3.0
	 *
	 */
	@Transactional
	public interface NoteService {

		/**
		 * 
		 * <pre>
		 * 1. 개용 : 쪽지 등록
		 * 2. 처리내용 : 
		 * </pre>
		 * @Method Name : noteListForInserting
		 * @param map
		 * @return map
		 */
		public Map<String, Object> noteListForInserting(List<HashMap<String, Object>>  noteList,HashMap<String, Object> map, SessionVO sessionVO) throws Exception;
		

		/**
		 * 
		 * <pre>
		 * 1. 개용 : 쪽지 답장 등록
		 * 2. 처리내용 : 
		 * </pre>
		 * @Method Name : noteListForInserting
		 * @param map
		 * @return map
		 */
		public Map<String, Object> noteListForReInserting(HashMap<String, Object> map, SessionVO sessionVO) throws Exception;
		
	
		/**
		 * 
		 * <pre>
		 * 1. 개용 : 읽지 않은 최신쪽지 가져오기
		 * 2. 처리내용 : 
		 * </pre>
		 * @Method Name : noteNewTopNInfoList
		 * @param map
		 * @return map
		 */
		public Map<String, Object> noteNewTopNInfoList(HashMap<String, Object> map);
		
		
		
		/**
		 * 
		 * <pre>
		 * 1. 개용 : 쪽지목록 가져오기
		 * 2. 처리내용 : 
		 * </pre>
		 * @Method Name : noteAllReceiveInfoList
		 * @param map
		 * @return map
		 */
		public Map<String, Object> noteAllReceiveSendInfoList(HashMap<String, Object> map);
		
		/**
		 * 
		 * <pre>
		 * 1. 개용 : 쪽지 보관함에 저장
		 * 2. 처리내용 : 
		 * </pre>
		 * @Method Name : noteSaveUpdate
		 * @param map
		 * @return map
		 */
		public int noteSaveUpdate(HashMap<String, Object> map, SessionVO sessionVO) throws Exception;
		/**
		 * 
		 * <pre>
		 * 1. 개용 : 쪽지 보관함에 저장
		 * 2. 처리내용 : 
		 * </pre>
		 * @Method Name : noteSaveUpdate
		 * @param map
		 * @return map
		 */
		public int noteReadUpdate(HashMap<String, Object> map, SessionVO sessionVO) throws Exception;

		/**
		 * 
		 * <pre>
		 * 1. 개용 : 쪽지 삭제
		 * 2. 처리내용 : 
		 * </pre>
		 * @Method Name : noteDelete
		 * @param map
		 * @return int
		 */
		public int noteDelete(HashMap<String, Object> map, SessionVO sessionVO) throws Exception;
		
		/**
		 * 
		 * <pre>
		 * 1. 개용 : 대화함목록 가져오기
		 * 2. 처리내용 : 
		 * </pre>
		 * @Method Name : noteTalkList
		 * @param map
		 * @return map
		 */
		public Map<String, Object> noteTalkList(HashMap<String, Object> map);
		
		
		/**
		 * 
		 * <pre>
		 * 1. 개용 : 대화함목록 상세 가져오기
		 * 2. 처리내용 : 
		 * </pre>
		 * @Method Name : noteTalkDetailList
		 * @param map
		 * @return map
		 */
		public Map<String, Object> noteTalkDetailList(HashMap<String, Object> map);
		/**
		 * 
		 * <pre>
		 * 1. 개용 : 문서처리(삭제/완전삭제/휴지통비우기) 대상 목록 구하기.
		 * 2. 처리내용 : 
		 * </pre>
		 * @Method Name : noteValidList
		 * @param map
		 * @return
		 * @throws Exception List<HashMap<String,Object>>
		 */
		public List<HashMap<String, Object>>  noteValidList(HashMap<String,Object> map) throws Exception;
			
		
}
