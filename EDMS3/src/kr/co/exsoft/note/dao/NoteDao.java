package kr.co.exsoft.note.dao;

import java.util.HashMap;
import java.util.List;

import kr.co.exsoft.document.vo.DocumentVO;
import kr.co.exsoft.note.vo.NoteManageVO;
import kr.co.exsoft.note.vo.NoteVO;

import org.springframework.stereotype.Repository;

/**
 * Note 매퍼클래스
 * @author 패키지 개발팀
 * @since 2015.03.02
 * @version 3.0
 *
*/
@Repository(value = "noteDao")
public interface NoteDao {
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 쪽지등록처리 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteWrite
	 * @param map
	 * @return int
	 */
	public int noteWrite(HashMap<String,Object> map);
	

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 쪽지등록처리 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : writeNote
	 * @param NoteVo
	 * @return int
	 */

	public int noteManageWrite(NoteManageVO manageVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 쪽지등록처리 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : writeNote
	 * @param NoteVo
	 * @return int
	 */

	public int noteQueueWrite(NoteManageVO manageVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : user_id로 최신 쪽지  목록을 가져온다 : XR_NOTE
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteNewTopNInfoList
	 * @param map
	 * @return List<NoteVO>
	 */
	public List<NoteVO> noteNewTopNInfoList(HashMap<String,Object> map);


	/**
	 * 
	 * <pre>
	 * 1. 개용 : user_id로  받은 쪽지  목록을 가져온다 : XR_NOTE
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteAllReceiveInfoList
	 * @param map
	 * @return List<NoteVO>
	 */
	public List<NoteVO> noteAllReceiveSendInfoList(HashMap<String,Object> map);

	/**
	 * 
	 * <pre>
	 * 1. 개용 : manage_id로  받은 쪽지&보낸쪽지를 보관상태 update
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteSaveUpdate
	 * @param map
	 * @return int
	 */
	public int noteSaveUpdate(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : manage_id로  받은 쪽지 읽음상태 update
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteReadUpdate
	 * @param map
	 * @return int
	 */
	public int noteReadUpdate(HashMap<String,Object> map);
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteManageDelete
	 * @param map
	 * @return int
	 */
	public int noteManageDelete(HashMap<String,Object> map);

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteDelete
	 * @param map
	 * @return List<NoteVO>
	 */
	public int noteDelete(HashMap<String,Object> map);

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : root_id 로 
	 * </pre>
	 * @Method Name : noteDelete
	 * @param map
	 * @return List<NoteVO>
	 */
	public int noteRootCount(HashMap<String,Object> map);

	/**
	 * 
	 * <pre>
	 * 1. 개용 : user_id로  대화함  목록을 가져온다 : XR_NOTE
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteTalkList
	 * @param map
	 * @return List<NoteVO>
	 */
	public List<NoteVO> noteTalkList(HashMap<String,Object> map);

	/**
	 * 
	 * <pre>
	 * 1. 개용 : user_id로  대화함  목록 상세를 가져온다 : XR_NOTE
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteTalkDetailList
	 * @param map
	 * @return List<NoteVO>
	 */
	public List<NoteVO> noteTalkDetailList(HashMap<String,Object> map);

		
}
