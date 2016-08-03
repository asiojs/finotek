package kr.co.exsoft.document.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.exsoft.document.vo.*;
import kr.co.exsoft.common.vo.SessionVO;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 문서유형 서비스 인터페이스
 * @author 패키지 개발팀
 * @since 2014.08.01
 * @version 3.0
 *
 */
@Transactional
public interface TypeService {

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typePageList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> typePageList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 정보 가져오기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typeDetailInfo
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> typeDetailInfo(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 속성 정보 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : attrList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> attrList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 속성 정보 가져오기 :: 문서등록/수정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typeAttrList
	 * @param map
	 * @return
	 * @throws Exception List<AttrVO>
	 */
	public List<AttrVO> typeAttrList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 등록 처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typeManagerInsert
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> typeManagerWrite(HashMap<String,Object> map,TypeVO typeVO,List<AttrVO> attrList,SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 삭제 처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typeManagerDelete
	 * @param map
	 * @param delList
	 * @param sessionVO
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> typeManagerDelete(HashMap<String,Object> map,List<String> delList,SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 수정 처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typeManagerUpdate
	 * @param map
	 * @param typeVO
	 * @param attrList
	 * @param sessionVO
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> typeManagerUpdate(HashMap<String,Object> map,TypeVO typeVO,List<AttrVO> attrList,SessionVO sessionVO) throws Exception;
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 삭제처리 Valid
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typeManagerDeleteValid
	 * @param map
	 * @return
	 * @throws Exception List<String>
	 */
	public List<String> typeDeleteValid(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 등록처리 Valid
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typeWriteValid
	 * @param map
	 * @return
	 * @throws Exception List<AttrVO>
	 */
	public List<AttrVO> typeWriteValid(HashMap<String,Object> map) throws Exception;
		
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 유형 목록 가져오기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typeList
	 * @param map
	 * @return
	 * @throws Exception List<TypeVO>
	 */
	public List<TypeVO> typeList(HashMap<String,Object> map) throws Exception;
}
