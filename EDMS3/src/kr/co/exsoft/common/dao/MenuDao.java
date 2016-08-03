package kr.co.exsoft.common.dao;

import java.util.HashMap;
import java.util.List;

import kr.co.exsoft.common.vo.MenuVO;
import kr.co.exsoft.common.vo.MenuAuthVO;

import org.springframework.stereotype.Repository;

/**
 * Menu 매퍼클래스
 * @author 패키지 개발팀
 * @since 2014.07.21
 * @version 3.0
 *
 */
@Repository(value = "menuDao")
public interface MenuDao {
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 메뉴권한 목록 가져오기.
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : menuAuthList
	 * @param map
	 * @return List
	 */
	public List<MenuAuthVO> menuAuthList(HashMap<String,Object> map);

	/**
	 * 
	 * <pre>
	 * 1. 개요 : 메뉴권한 삭제 처리
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : menuAuthDelete
	 * @param menuAuthVO
	 * @return int
	 */
	public int menuAuthDelete(MenuAuthVO menuAuthVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 :하위메뉴권한 삭제 처리
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : subMenuAuthDelete
	 * @param menuAuthVO
	 * @return int
	 */
	public int subMenuAuthDelete(MenuAuthVO menuAuthVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 메뉴권한 등록처리
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : menuAuthWrite
	 * @param menuAuthVO
	 * @return int
	 */
	public int menuAuthWrite(MenuAuthVO menuAuthVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 메뉴권한 목록 등록 여부
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : menuAuthDetail
	 * @param menuAuthVO
	 * @return MenuAuthVO
	 */
	public MenuAuthVO menuAuthDetail(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 메뉴목록 가져오기.
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : menuList
	 * @param map
	 * @return List
	 */
	public List<MenuVO> menuList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 메뉴정보 가져오기
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : menuDetail
	 * @param map
	 * @return MenuVO
	 */
	public MenuVO menuDetail(HashMap<String,Object> map);
	
}
