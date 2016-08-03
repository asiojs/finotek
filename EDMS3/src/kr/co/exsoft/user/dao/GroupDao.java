package kr.co.exsoft.user.dao;

import java.util.HashMap;
import java.util.List;

import kr.co.exsoft.user.vo.GroupVO;
import kr.co.exsoft.user.vo.GroupedVO;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

/**
 * Group 매퍼클래스
 * @author 패키지 개발팀
 * @since 2014.07.21
 * @version 3.0
 *
 */
@Repository(value = "groupDao")
public interface GroupDao {

	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 소속 부서 등록 처리(기본/겸직)
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupedWrite
	 * @param map
	 * @return
	 */
	public int groupedWrite(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 소속 부서 수정 처리
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupedUpdate
	 * @param map
	 * @return
	 */
	public int groupedUpdate(HashMap<String,Object> map);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 부서에 소속된 사용자 목록 얻기
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupedList
	 * @param group_id
	 * @return
	 */
	public List<GroupedVO> groupedList(String group_id);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 소속 부서 삭제 처리
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupedDelete
	 * @param groupId
	 * @return
	 */
	public int groupedDelete(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 부서 등록
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupWrite
	 * @param groupVO
	 * @return
	 */
	public int groupWrite(GroupVO groupVO);

	/**
	 * 
	 * <pre>
	 * 1. 개요 : 부서 수정
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupUpdate
	 * @param groupVO
	 * @return
	 */
	public int groupUpdate(GroupVO groupVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 최상위 부서 목록 조회
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : rootGroupList
	 * @param params
	 * @return
	 */
	public List<GroupVO> rootGroupList(HashMap<String, Object> params);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 하위 부서 목록 조회
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : childGroupList
	 * @param params
	 * @return
	 */
	public List<GroupVO> childGroupList(HashMap<String, Object> params);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 그룹 상세 정보 조회
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupDetail
	 * @param groupId
	 * @return
	 */
	public GroupVO groupDetail(String group_id);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 동일한 상위 그룹 하위에 동일한 이름의 그룹이 있는지 확인한다
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupIsExistByGroupNameAndParentIdList
	 * @param groupVO
	 * @return
	 */
	public int groupIsExistByGroupNameAndParentIdList(GroupVO groupVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 그룹 히스토리 등록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : groupHistoryWrite
	 * @param groupVO
	 * @return
	 */
	public int groupHistoryWrite(GroupVO groupVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 그룹 이동
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : groupMove
	 * @param groupVO
	 * @return
	 */
	public int groupMove(GroupVO groupVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 그룹 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : groupDelete
	 * @param groupVO
	 * @return
	 */
	public int groupDelete(GroupVO groupVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 프로젝트/겸직 대상 부서 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : dualGroupList
	 * @param params
	 * @return List<String>
	 */
	public List<String> dualGroupList(HashMap<String, Object> params);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 부서명으로 부서정보를 체크한다. NOT LIKE 검색아님 - 일괄등록시 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : chkGroupName
	 * @param map
	 * @return GroupVO
	 */
	public GroupVO chkGroupName(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 부서명으로 부서정보를 체크한다. NOT LIKE 검색아님 - 일괄등록시 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : chkGroupCnt
	 * @param map
	 * @return int
	 */
	public int chkGroupCnt(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 부서명/상위부서명으로 부서정보를 체크한다.NOT LIKE 검색아님 - 일괄등록시 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : chkUserGroupInfo
	 * @param map
	 * @return GroupVO
	 */
	public GroupVO chkUserGroupInfo(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 부서명/상위부서명으로 부서정보를 체크한다.NOT LIKE 검색아님 - 일괄등록시 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : chkUserGroupCnt
	 * @param map
	 * @return int
	 */
	public int chkUserGroupCnt(HashMap<String, Object> map);

	/**
	 * <pre>
	 * 1. 개용 : 미소속 부서 조회 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : independentGroupDetail
	 * @return
	 */
	public GroupVO independentGroupDetail();
	
	/**
	 * <pre>
	 * 1. 개용 : 그룹 매니저 카운트
	 * 2. 처리내용 : 특정 그룹을 관리부서로 설정한 사용자의 카운트
	 * </pre>
	 * @Method Name : groupManagerCnt
	 * @param map
	 * @return int
	 */
	public int groupManagerCnt(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 협업자 목록에서 필요
	 * 2. 처리내용 : 협업자의 부서명 가져오기
	 * </pre>
	 * @Method Name : groupInfoByUserId
	 * @param map
	 * @return List<CaseInsensitiveMap>
	 */
	public List<CaseInsensitiveMap> groupInfoByUserId(HashMap<String,Object> map);
}
