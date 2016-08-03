package kr.co.exsoft.document.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import kr.co.exsoft.document.vo.TypeVO;
import kr.co.exsoft.document.vo.AttrVO;
import kr.co.exsoft.document.vo.AttrItemVO;


/**
 * Type 매퍼클래스
 * @author 패키지 개발팀
 * @since 2014.07.21
 * @version 3.0
 *
 */
@Repository(value = "typeDao")
public interface TypeDao {

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typeList
	 * @param map
	 * @return List<TypeVO>
	 */
	public List<TypeVO> typeList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 목록 Count
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typePagingCount
	 * @param map
	 * @return int
	 */
	public int typePagingCount(HashMap<String,Object> map);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 페이지 리스트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typePagingList
	 * @param map
	 * @return List<TypeVO>
	 */
	public List<TypeVO> typePagingList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 테이블을 생성
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : createType
	 * @param typeVO
	 * @return int
	 */
	public int createType(HashMap<String,Object> map);

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 테이블 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : dropType
	 * @param tbl_name
	 * @return int
	 */
	public int dropType(String tbl_name);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형속성 테이블 수정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : alterType
	 * @param map
	 * @return int
	 */
	public int alterType(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 등록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typeWrite
	 * @param attrVO
	 * @return int
	 */
	public int typeWrite(TypeVO typeVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 수정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typeUpdate
	 * @param map
	 * @return int
	 */
	public int typeUpdate(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typeDelete
	 * @param map
	 * @return int
	 */
	public int typeDelete(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 속성을 등록한다
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : attrWrite
	 * @param attrVO
	 * @return int
	 */
	public int attrWrite(AttrVO attrVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 수정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : attrUpdate
	 * @param map
	 * @return int
	 */
	public int attrUpdate(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : attrDelete
	 * @param map
	 * @return int
	 */
	public int attrDelete(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 속성 아이템 목록 얻기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : attrItemList
	 * @param map
	 * @return List<AttrItemVO>
	 */
	public List<AttrItemVO> attrItemList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 속성 아이템 등록 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : attrItemWrite
	 * @param attrItemVO
	 * @return int
	 */
	public int attrItemWrite(AttrItemVO attrItemVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 속성 아이템 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : attrItemDelete
	 * @param map
	 * @return int
	 */
	public int attrItemDelete(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 확장속성값을 등록한다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : attrValueWrite
	 * @param map
	 * @return int
	 */
	public int attrValueWrite(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 확장속성값을 가져온다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : attrValueList
	 * @param map
	 * @return List<String>
	 */
	public String attrValueDetail(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 확장속성값을 수정한다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : attrValueUpdate
	 * @param map
	 * @return int
	 */
	public int attrValueUpdate(HashMap<String,Object> map);
			
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 확장속성값을 삭제한다. 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : attrValueDelete
	 * @param map
	 * @return int
	 */
	public int attrValueDelete(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 정보 보기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typeDetailInfo
	 * @param map
	 * @return TypeVO
	 */
	public TypeVO typeDetailInfo(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 속성 리스트 가져오기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : attrList
	 * @param map
	 * @return List<AttrVO>
	 */
	public List<AttrVO> attrList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형ID의 테이블이 존재하는 체크한다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : tableInfo
	 * @param map
	 * @return int
	 */
	public int tableInfo(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 확장문서유형 테이블에 속성값을 추가한다. 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : insertAttrValue
	 * @param map
	 * @return int
	 */
	public int insertAttrValue(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  확장문서유형 테이블에 속성값을 수정한다
	 * 2. 처리내용 : 문서유형 속성들이 동적이기때문에 이렇게 처리함
	 * </pre>
	 * @Method Name : updateAttrValue
	 * @param map
	 * @return int
	 */
	public int updateAttrValue(HashMap<String,Object> map);
}
