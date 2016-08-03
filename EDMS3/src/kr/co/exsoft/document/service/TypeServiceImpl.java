package kr.co.exsoft.document.service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import kr.co.exsoft.eframework.library.ExsoftAbstractServiceImpl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.PagingAjaxUtil;
import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.document.vo.*;
import kr.co.exsoft.common.vo.HistoryVO;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.dao.TypeDao;
import kr.co.exsoft.document.dao.DocumentDao;
import net.sf.json.JSONArray;


/**
 * 문서유형 서비스 구현 부분
 * @author 패키지 개발팀
 * @since 2014.08.01
 * @version 3.0
 *
 */
@Service("typeService")
public class TypeServiceImpl extends ExsoftAbstractServiceImpl implements TypeService {

	@Autowired
	@Qualifier("sqlSession")
	private SqlSession sqlSession;
	
	@Autowired
	private CommonService commonService;
	
	// 오라클 예약어 리스트
	protected static final String[] oracle_reversed = {
		"ACCESS","ADD","ALL","ALTER","AND","ANY","AS","ASC","AUDIT","BETWEEN","BY","CHAR","CHECK","CLUSTER","COLUMN",
		"COMMENT","COMPRESS","CONNECT","CREATE","CURRENT","DATE","DECIMAL","DEFAULT","DELETE","DESC","DISTINCT","DROP",
		"ELSE","EXCLUSIVE","EXISTS","FILE","FLOAT","FOR","FROM","GRANT","GROUP","HAVING","IDENTIFIED","IMMEDIATE","IN","INCREMENT",
		"INDEX","INITIAL","INSERT","INTEGER","INTERSECT","INTO","IS","LEVEL","LIKE","LOCK","LONG","MAXEXTENTS","MINUS","MLSLABEL","MODE"
		,"MODIFY","NOAUDIT","NOCOMPRESS","NOT","NOWAIT","NULL","NUMBER","OF","OFFLINE","ON","ONLINE","OPTION","OR","ORDER","PCTFREE",
		"PRIOR","PRIVILEGES","PUBLIC","RAW","RENAME","RESOURCE","REVOKE","ROW","ROWID","ROWNUM","ROWS","SELECT","SESSION","SET","SHARE",
		"SIZE","SMALLINT","START","SUCCESSFUL","SYNONYM","SYSDATE","TABLE","THEN","TO","TRIGGER","UID","UNION","UNIQUE","UPDATE","USER",
		"VALIDATE","VALUES","VARCHAR","VARCHAR2","VIEW","WHENEVER","WHERE","WITH"	
	};
	
	// 문서속성 예약어 리스트
	protected static final String[] type_reversed = {
		"DOC_ID","DOC_NAME","DOC_TYPE","PAGE_CNT","ROOT_ID","IS_CURRENT","IS_LOCKED","LOCK_OWNER","LOCK_DATE","VERSION_NO",
		"VERSION_NOTE","PRESERVATION_YEAR","EXPIRED_DATE","IS_EXPIRED","DOC_DESCRIPTION","DOC_STATUS","ACL_ID","CREATOR_ID",
		"CREATOR_NAME","CREATE_DATE","UPDATER_ID","UPDATER_NAME","UPDATE_DATE","DELETER_ID","DELETER_NAME","DELETE_DATE",
		"OWNER_ID","REF_ID","DOC_NO","IS_TRANS","EX_ACL_ID","LOCATION","ACCESS_GRADE","FOLDER_ID","PAGE_TOTAL","SECURITY_LEVEL","KEYWORD",
		"IS_SHARE","AUTHOR_LIST","READ_CNT"		
	};

	
	@Override
	public Map<String, Object> typePageList(HashMap<String,Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<TypeVO> ret = new ArrayList<TypeVO>();
		int total = 0;
		
		TypeDao typeDao = sqlSession.getMapper(TypeDao.class);
		
		total = typeDao.typePagingCount(map);
		ret = typeDao.typePagingList(map);
		
		resultMap.put("page",map.get("nPage").toString());
		resultMap.put("records",total);
		resultMap.put("total",CommonUtil.getTotPageSize(total,Integer.parseInt(map.get("page_size").toString())));	
		resultMap.put("list",ret);
		
		// Ajax Paging 
		String strLink = "javascript:exsoftAdminTypeFunc.event.gridPage";
		String contextRoot = map.get("contextRoot") != null ? map.get("contextRoot").toString() : "";
		PagingAjaxUtil pagingInfo = new PagingAjaxUtil(Integer.parseInt(map.get("nPage").toString()),total,Integer.parseInt(map.get("page_size").toString()),10,strLink,contextRoot);		
		resultMap.put("pagingInfo",pagingInfo);		
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> typeDetailInfo(HashMap<String,Object> map) throws Exception {
		
		TypeDao typeDao = sqlSession.getMapper(TypeDao.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(map.get("type_id") == null)	{
			throw processException("common.required.error");	
		}
		
		// 1. XR_TYPE 정보 - map : type_id
		TypeVO typeVO = typeDao.typeDetailInfo(map);
		
		if(typeVO == null) {
			throw processException("common.data.exist.error");	
		}
		
		resultMap.put("typeVO",typeVO);
		resultMap.put("result",Constant.RESULT_TRUE);
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> attrList(HashMap<String,Object> map) throws Exception {
		
		TypeDao typeDao = sqlSession.getMapper(TypeDao.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();

		List<AttrVO>  attrList = new ArrayList<AttrVO>();
		List<AttrVO>  ret = new ArrayList<AttrVO>();
		
		//  XR_ATTRITEM - map : type_id / attr_id
		if(map.get("type_id") != null)	{
		
			// XR_ATTR 조회 :: 문서유형관리			
			attrList = typeDao.attrList(map);
			
			// XR_ATTRITEM 조회
			param.put("type_id", map.get("type_id"));			
			param.put("is_extended", map.get("is_extended"));
			ret = getAttrList(attrList,typeDao,param);
		}
		
		resultMap.put("records",ret.size());
		resultMap.put("list",ret);
		
		return resultMap;
	}
	
	@Override
	public List<AttrVO> typeAttrList(HashMap<String,Object> map) throws Exception {
		
		TypeDao typeDao = sqlSession.getMapper(TypeDao.class);
		
		HashMap<String, Object> param = new HashMap<String, Object>();

		List<AttrVO>  attrList = new ArrayList<AttrVO>();
		List<AttrVO>  ret = new ArrayList<AttrVO>();
		
		//  XR_ATTRITEM - map : type_id / attr_id
		if(map.get("type_id") != null)	{
			
			// XR_ATTR 조회 - 문서등록/수정			
			map.put("is_extended",Constant.T);
			attrList = typeDao.attrList(map);
			
			// XR_ATTRITEM 조회
			param.put("type_id", map.get("type_id"));			
			ret = getAttrList(attrList,typeDao,param);
		}
			
		return ret;	
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 속성에 따른 속성ITEM 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getAttrList
	 * @param attrList
	 * @param typeDao
	 * @param map
	 * @return List<AttrVO>
	 */
	public List<AttrVO> getAttrList(List<AttrVO> attrList,TypeDao typeDao,HashMap<String,Object> map) {
		
		List<AttrVO>  ret = new ArrayList<AttrVO>();
		List<AttrItemVO>  attrItemList = new ArrayList<AttrItemVO>();

		StringBuffer buf = new StringBuffer();
		String has_item = "";
		
		if(attrList != null && attrList.size() > 0)	{
			
			for(AttrVO attrVO : attrList) {
		
				map.put("attr_id", attrVO.getAttr_id());			
				attrItemList = typeDao.attrItemList(map);		
				
				if(attrItemList != null && attrItemList.size() > 0)	{
				
					attrVO.setItem_list(attrItemList);	
					
					for(AttrItemVO attrItem : attrItemList)	{
						
						buf.append(attrItem.getItem_name());
						buf.append(":");
						buf.append(attrItem.getItem_index());
						buf.append(",");
					}
					
					// 마지막 콤마 제거
					has_item = buf.toString();				
					if(has_item.lastIndexOf(",") != -1 )	{
						has_item = has_item.substring(0,has_item.length()-1);
					}
										
					attrVO.setHas_item_list(has_item);
				}
				attrVO.setIs_locked(Constant.T);		// attr_id 수정여부
					
				ret.add(attrVO);
				
				buf.setLength(0);
			}
		}
		
		return ret;
	}
	
	@Override
	public List<String> typeDeleteValid(HashMap<String,Object> map) throws Exception {
		
		DocumentDao documentDao = sqlSession.getMapper(DocumentDao.class);
		HashMap<String, Object> param = new HashMap<String, Object>();
		List<String> ret = new ArrayList<String>();
		
		String typeList =  map.get("typeIdList") != null ? map.get("typeIdList").toString() : "";
		
		// 1.입력값 유효성 체크
		if(typeList.equals("") ||  typeList.equals(""))	{
			throw processException("common.required.error");
		}
		
		JSONArray jsonArray = JSONArray.fromObject(typeList);
		if(jsonArray.size() > 0 ) {		
			 for(int j=0;j < jsonArray.size();j++)	{				 
				 ret.add(jsonArray.getJSONObject(j).getString("type_id").toString());
			 }
		}
		
		// 2.XR_TYPE  사용여부 (XR_DOCUMENT/XR_DOCUMENT_DEL)
		if(ret != null && ret.size() > 0) {
			
			param.put("type_list", ret);
			param.put("table_nm",Constant.DOC_TABLE);
			if(documentDao.isUsingType(param) > 0 )	{
				throw processException("type.fail.type.id.use");	
			}			
			
			param.put("table_nm",Constant.DOC_DEL_TABLE);
			if(documentDao.isUsingType(param) > 0 )	{
				throw processException("type.fail.type.id.use");	
			}
		}
		
		return ret;
	}
	
	
	@Override
	public List<AttrVO> typeWriteValid(HashMap<String,Object> map) throws Exception {
		
		TypeDao typeDao = sqlSession.getMapper(TypeDao.class);

		List<AttrVO> ret = new ArrayList<AttrVO>();

		// 문서속성 리스트 유효성 체크
		String attrArrayList =  map.get("attrArrayList") != null ? map.get("attrArrayList").toString() : "";		
		String has_item = "";
		String display_type = "";
		
		if(attrArrayList.equals("") ||  attrArrayList.equals(""))	{
			throw processException("common.required.error");
		}
		
		// JsonArray 객체 생성하기
		JSONArray jsonArray = JSONArray.fromObject(attrArrayList);
		
		if(jsonArray.size() > 0 ) {
			
			 for(int j=0;j < jsonArray.size();j++)	{

				 AttrVO attrVO  = new AttrVO();
				 
				 attrVO.setType_id(map.get("type_id").toString().toUpperCase());
				 attrVO.setAttr_id(jsonArray.getJSONObject(j).getString("attr_id").toUpperCase());
				 attrVO.setAttr_name(jsonArray.getJSONObject(j).getString("attr_name"));
				 attrVO.setAttr_size(Integer.parseInt(jsonArray.getJSONObject(j).getString("attr_size")));
				 attrVO.setSort_index(Integer.parseInt(jsonArray.getJSONObject(j).getString("sort_index")));
				 attrVO.setIs_mandatory(jsonArray.getJSONObject(j).getString("is_mandatory"));
				 attrVO.setIs_editable(jsonArray.getJSONObject(j).getString("is_editable"));
				 attrVO.setIs_search(jsonArray.getJSONObject(j).getString("is_search"));
				 attrVO.setDisplay_type(jsonArray.getJSONObject(j).getString("display_type"));				

				 display_type = jsonArray.getJSONObject(j).getString("display_type");
				 has_item = jsonArray.getJSONObject(j).getString("has_item");
				 
				 if(has_item.equals(""))	{
					 
					 if(!display_type.equals(Constant.DISPLAY_TYPE_INPUT))	{
						 throw processException("type.fail.type.has.item");
					 }
										
					 attrVO.setHas_item(Constant.F);
					 
				 }else {
					 attrVO.setHas_item(Constant.T);
					 attrVO.setDefault_item_index(Integer.parseInt(jsonArray.getJSONObject(j).getString("default_item_index")));

					 // XR_ATTRITEM 정보 설정하기	 
					 attrVO.setItem_list(setAttrItem(map,has_item,attrVO.getAttr_id()));	
					 
				 }
				 
				 // ATTR_ID 예약어 체크 로직 추가
				 for(int m=0;m<oracle_reversed.length;m++)		{						
					if(attrVO.getAttr_id().equals(oracle_reversed[m]))	{								
						throw processException("type.fail.type.id.reserved");
					}
				}
				 				 
				 // ATTR_ID 예약어 체크 로직 추가 :: 기본속성ID 체크 로직 추가
				 for(int m=0;m<type_reversed.length;m++)		{						
						if(attrVO.getAttr_id().equals(type_reversed[m]))	{								
							throw processException("type.fail.type.id.default");
						}
				 }

				 
				 // is_extended = T 기본값 처리 				 
				 ret.add(attrVO);				 
			 }					
		}		//  데이터 가공처리 완료
		
		// 데이터 유효성 체크처리 :: XR_TYPE 
		String validResult = typeValidCheck(map,typeDao);
		if(!validResult.equals(Constant.SUCCESS))	{
			throw processException(validResult);	
		}

		// 2. XR_ATTR - 등록단계 체크 SKIP
		// 3. XR_ATTRITEM - 등록단계  체크 SKIP
			
		return ret;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 속성 아이템 정보 설정하기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setAttrItem
	 * @param map
	 * @param has_item
	 * @param attr_id
	 * @return
	 * @throws Exception List<AttrItemVO>
	 */
	public List<AttrItemVO> setAttrItem(HashMap<String,Object> map,String has_item,String attr_id) throws Exception {
		
		List<AttrItemVO> ret = new ArrayList<AttrItemVO>();
		
		 String[] items = has_item.split("[,]");
		 
		 for(int i=0;i<items.length;i++) {
			 
			 String[] result = items[i].split("[:]");
			 
			 AttrItemVO attrItemVO = new AttrItemVO();
			 attrItemVO.setType_id(map.get("type_id").toString().toUpperCase());
			 attrItemVO.setAttr_id(attr_id);
			 attrItemVO.setItem_index(Integer.parseInt(result[1]));
			 attrItemVO.setItem_name(result[0]);
			
			 ret.add(attrItemVO);												
		 }
		
		
		return ret;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 유효성 체크 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typeValidCheck
	 * @param map 
	 * @param type : 등록/수정/삭제 
	 * @return
	 * @throws Exception String
	 */
	public String typeValidCheck(HashMap<String,Object> map,TypeDao typeDao) throws Exception {
				
		HashMap<String, Object> param = new HashMap<String, Object>();
		TypeVO typeVO = new TypeVO();
		String ret = Constant.SUCCESS;
		String type = map.get(Constant.TYPE) != null ? map.get(Constant.TYPE).toString() : "";
		
		if(type.equals(Constant.INSERT))	{

			// 1 TYPE_ID 중복체크
			param.put("type_id", map.get("type_id").toString().toUpperCase());
			typeVO = typeDao.typeDetailInfo(param);
			if(typeVO != null)	{				
				return "type.fail.type.id.double";
			}
			
			// 2 TYPE_NAME 중복체크
			param.clear();
			param.put("type_name", map.get("type_name").toString());
			typeVO = typeDao.typeDetailInfo(param);
			if(typeVO != null)	{
				return "type.fail.type.name.double";					
			}
			
			// 3 TYPE_ID 가 기 테이블명과 중복되는지 체크 :: 테이블 생성 못함
			if(Constant.TABLE_PREFIX.equals("XR_")) {
				param.put("object_name",Constant.TABLE_PREFIX+map.get("type_id").toString());
				if(typeDao.tableInfo(param) != 0)	{
					return "type.fail.type.id.table.double";					
				}				
			}
					
		}else if(type.equals(Constant.UPDATE))	{
			
			// 1.TYPE_NAME 중복체크			
			param.put("isPart",Constant.UPDATE);
			param.put("type_id", map.get("type_id").toString().toUpperCase());
			param.put("type_name", map.get("type_name").toString());
			typeVO = typeDao.typeDetailInfo(param);
			if(typeVO != null)	{
				return "type.fail.type.name.double";					
			}
						
		}

		return ret;
	}
	
	
	
	@Override
	public Map<String, Object> typeManagerWrite(HashMap<String,Object> map,TypeVO typeVO,
			List<AttrVO> attrList,SessionVO sessionVO) throws Exception {
		
		TypeDao typeDao = sqlSession.getMapper(TypeDao.class);		

		HashMap<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
				
		String tbl_name = Constant.TABLE_PREFIX + map.get("type_id").toString().toUpperCase();				// 0. TABLE 명 설정(대문자로)
		
		long history_seq = 0;
		int result = 0;		
									
		// 1. XR_TYPE INSERT
		typeVO.setTbl_name(tbl_name);
		typeVO.setType_id(map.get("type_id").toString().toUpperCase());
		typeVO.setSort_index(Integer.parseInt(map.get("sortIndex").toString()));
			
		result= typeDao.typeWrite(typeVO);
		if(result == 0)	{	throw processException("common.system.error");	}
			
		// 2. XR_ATTR INSERT
		for(AttrVO attrVO : attrList)	{
				
			result = typeDao.attrWrite(attrVO);				
			if(result == 0)	{	throw processException("common.system.error");	}
					
			// 3.XR_ATTR_ITEM INSERT ::  속성값 목록이 존재시만 수행
			if(attrVO.getItem_list() != null && attrVO.getItem_list().size() > 0 ) {		
				
				for(AttrItemVO attrItemVO : attrVO.getItem_list()) {						
					result = typeDao.attrItemWrite(attrItemVO);
					if(result == 0)	{	throw processException("common.system.error");	}						
				}					
			}
		}

		// 4.XR_HISTORY INSERT
		history_seq = commonService.commonNextVal(Constant.COUNTER_ID_HISTORY);
		HistoryVO historyVO = CommonUtil.setHistoryVO(history_seq, typeVO.getType_id(), Constant.ACTION_CREATE,  Constant.TARGET_TYPE, sessionVO);

		result = commonService.historyWrite(historyVO);
		if(result == 0)	{	throw processException("common.system.error");	}						

		// 5.문서유형 테이블 생성처리 DDL : return 값 의미없음
		param.put("tbl_name",tbl_name);
		param.put("attr_list", attrList);			
		typeDao.createType(param);
		
		resultMap.put("result",Constant.RESULT_TRUE);
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> typeManagerDelete(HashMap<String,Object> map,List<String> delList,SessionVO sessionVO) 
			throws Exception {
		
		TypeDao typeDao = sqlSession.getMapper(TypeDao.class);
		
		List<String> tbl_name_list = new ArrayList<String>();
		List<AttrVO> attrList = new ArrayList<AttrVO>();
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		long history_seq = 0;
		int result = 0;		

		// 데이터베이스 처리
		for(String type_id : delList ){
						
			// 1.XR_TYPE DELETE
			param.put("type_id", type_id);			
			result = typeDao.typeDelete(param);
			if(result == 0)	{	throw processException("common.system.error");	}				
			
			// 2.삭제할 속성명 리스트 목록 가져오기.
			attrList = typeDao.attrList(param);
			for (AttrVO attrVO : attrList) {
				
				// 3.XR_ATTR DELETE
				param.put("attr_id", attrVO.getAttr_id());
				result = typeDao.attrDelete(param);
				if(result == 0)	{	throw processException("common.system.error");	}			
				
				// 4.XR_ATTRITEM DELETE :: 속성이 없는 경우도 있음
				typeDao.attrItemDelete(param);
				
			}
		
			// 5.XR_HISTORY 등록처리
			history_seq = commonService.commonNextVal(Constant.COUNTER_ID_HISTORY);
			HistoryVO historyVO = CommonUtil.setHistoryVO(history_seq,type_id, Constant.ACTION_DELETE,  Constant.TARGET_TYPE, sessionVO);
			result = commonService.historyWrite(historyVO);
			if(result == 0)	{	throw processException("common.system.error");	}						
						
			// 6.삭제 table명 설정하기
			tbl_name_list.add(Constant.TABLE_PREFIX+type_id);

		}
		
		// 7. 문서유형 테이블 삭제하기 DDL
		for(String tbl_name : tbl_name_list)	{
			
			if(tbl_name != null && !tbl_name.equals("null"))	{				
				typeDao.dropType(tbl_name);				
			}
		}
		
		resultMap.put("result",Constant.RESULT_TRUE);
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> typeManagerUpdate(HashMap<String,Object> map,TypeVO typeVO,List<AttrVO> attrList,SessionVO sessionVO) 
			throws Exception {
	
		TypeDao typeDao = sqlSession.getMapper(TypeDao.class);		
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<HashMap<String,Object>> delList = new ArrayList<HashMap<String,Object>>();			
		List<HashMap<String,Object>> tblList = new ArrayList<HashMap<String,Object>>();			
		List<AttrVO> updateList = new ArrayList<AttrVO>();
		List<AttrVO> dbAttrList = new ArrayList<AttrVO>();
		List<AttrVO> insertList = new ArrayList<AttrVO>();
				
		String tbl_name = Constant.TABLE_PREFIX + map.get("type_id").toString().toUpperCase();			
		
		long history_seq = 0;
		int result = 0;		
		
		// 1.XR_TYPE 수정
		typeVO.setTbl_name(tbl_name);
		typeVO.setType_id(map.get("type_id").toString().toUpperCase());
		typeVO.setSort_index(Integer.parseInt(map.get("sortIndex").toString()));
		
		param.put("type_id",typeVO.getType_id().toUpperCase());
		dbAttrList = typeDao.attrList(param);
		
		param.put("type_name",typeVO.getType_name());
		param.put("is_hidden",typeVO.getIs_hidden());		// 체크
		param.put("sort_index",Integer.parseInt(map.get("sortIndex").toString()));
		result = typeDao.typeUpdate(param);
		if(result == 0)	{	throw processException("common.system.error");	}
		
		// 2.XR_ATTR LIST 가져오기
		// 2.1 XR_ATTR 삭제 목록 
		delList = typeDelList(dbAttrList,attrList);
		
		// 2.2 XR_ATTR 신규||수정 목록
		typeModifyList(dbAttrList,attrList,updateList,insertList);
		
		// 3. XR_ATTR 속성 등록/수정/삭제
		
		// 3.1 XR_ATTR 등록 XR_ATTRITEM 등록
		for(AttrVO attrVO : insertList)	{
						
			result = typeDao.attrWrite(attrVO);				
			if(result == 0)	{	throw processException("common.system.error");	}
			
			// XR_ATTR_ITEM INSERT ::  속성값 목록이 존재시만 수행
			if(attrVO.getItem_list() != null && attrVO.getItem_list().size() > 0 ) {		
							
				for(AttrItemVO attrItemVO : attrVO.getItem_list()) {						
					result = typeDao.attrItemWrite(attrItemVO);
					if(result == 0)	{	throw processException("common.system.error");	}						
				}					
			}

			// 테이블 변경 리스트 구하기.
			setTblList(tblList,tbl_name,attrVO.getAttr_id(),attrVO.getAttr_size(),Constant.INSERT);
		}
		
		// 32  XR_ATTR 수정 XR_ATTRITEM 삭제 후 등록
		for(AttrVO attrVO : updateList)	{
			
			HashMap<String,Object> upMap = new HashMap<String,Object>();
			
			upMap.put("attr_name",attrVO.getAttr_name());
			upMap.put("attr_size",attrVO.getAttr_size());
			upMap.put("is_editable",attrVO.getIs_editable());
			upMap.put("is_mandatory",attrVO.getIs_mandatory());
			upMap.put("is_search",attrVO.getIs_search());
			upMap.put("sort_index",attrVO.getSort_index());
			upMap.put("display_type",attrVO.getDisplay_type());
			upMap.put("has_item",attrVO.getHas_item());
			upMap.put("default_item_index",attrVO.getDefault_item_index());
			upMap.put("type_id",attrVO.getType_id());
			upMap.put("attr_id",attrVO.getAttr_id());

			// XR_ATTR UPDATE 
			result = typeDao.attrUpdate(upMap);
			if(result == 0)	{	throw processException("common.system.error");	}		
			
			// XR_ATTRITEM DELETE
			typeDao.attrItemDelete(upMap);	
			
			// XR_ATTR_ITEM INSERT
			if(attrVO.getItem_list() != null && attrVO.getItem_list().size() > 0 ) {		
				
				for(AttrItemVO attrItemVO : attrVO.getItem_list()) {						
					result = typeDao.attrItemWrite(attrItemVO);
					if(result == 0)	{	throw processException("common.system.error");	}						
				}					
			}

			// 테이블 변경 리스트 구하기.
			setTblList(tblList,tbl_name,attrVO.getAttr_id(),attrVO.getAttr_size(),Constant.UPDATE);
		}
		
		// 3.3  XR_ATTR 삭제 XR_ATTRITEM 삭제
		for(HashMap<String,Object> delMap : delList)	{
			
			// XR_ATTR DELETE 
			result = typeDao.attrDelete(delMap);
			if(result == 0)	{	throw processException("common.system.error");	}			
			
			// XR_ATTRITEM DELETE
			typeDao.attrItemDelete(delMap);	

			// 테이블 변경 리스트 구하기. :: 삭제시 attr_size = 0 으로 설정처리
			setTblList(tblList,tbl_name,delMap.get("attr_id").toString(),0,Constant.DELETE);
		}
		
		// 4. XR_HISTORY 등록처리
		history_seq = commonService.commonNextVal(Constant.COUNTER_ID_HISTORY);
		HistoryVO historyVO = CommonUtil.setHistoryVO(history_seq, typeVO.getType_id(), Constant.ACTION_UPDATE,  Constant.TARGET_TYPE, sessionVO);
		result = commonService.historyWrite(historyVO);
		if(result == 0)	{	throw processException("common.system.error");	}						

		// 5.TABLE 변경처리
		for(HashMap<String,Object> deleteMap : tblList)	{						
			typeDao.alterType(deleteMap);			
		}
		
		resultMap.put("result",Constant.RESULT_TRUE);
				
		return resultMap;
		
	}


	@Override
	public List<TypeVO> typeList(HashMap<String, Object> map) throws Exception {
		TypeDao typeDao = sqlSession.getMapper(TypeDao.class);
		
		List<TypeVO> typeList = typeDao.typeList(map);
		
		return typeList;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : Type 변경처리 :: 삭제 목록 구하기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typeDelList
	 * @param attrList
	 * @return
	 * @throws Exception List<HashMap<String,Object>>
	 */
	public List<HashMap<String,Object>> typeDelList(List<AttrVO> dbAttrList,List<AttrVO> attrList) throws Exception {
		
		List<HashMap<String,Object>> delList = new ArrayList<HashMap<String,Object>>();	
		
		boolean isFlag = false;
		
		for(AttrVO dbAttr : dbAttrList)	{
			
			HashMap<String,Object> delMap = new HashMap<String,Object>();
			
			delMap.put("type_id", dbAttr.getType_id());
			
			for(AttrVO attrVO : attrList ) {
			
				if(dbAttr.getAttr_id().equals(attrVO.getAttr_id()))	{	// 존재하는 경우 skip
					isFlag = true;
					break;
				}							
			}
			
			// 삭제목록에 추가한다.
			if(!isFlag)	{
				delMap.put("attr_id", dbAttr.getAttr_id());
				delList.add(delMap);
			}
			
			isFlag = false;
		}
		
		return delList;
	}

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형 수정처리 : 테이블 변경 목록 구하기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setTblList
	 * @param tblList
	 * @param tbl_name
	 * @param attr_id
	 * @param attr_size
	 * @param type
	 * @throws Exception void
	 */
	public void setTblList(List<HashMap<String,Object>> tblList,String tbl_name,String attr_id,int attr_size,String type) throws Exception {
		
		HashMap<String,Object> tblMap = new HashMap<String,Object> ();
		
		tblMap.put("tbl_name", tbl_name);
		tblMap.put("attr_id",attr_id);
		tblMap.put("attr_size",attr_size);
		tblMap.put("attr_status",type);
		tblList.add(tblMap);
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : : Type 변경처리 ::  수정/추가 목록 구하기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typeModifyList
	 * @param dbAttrList
	 * @param attrList
	 * @param updateList
	 * @param insertList
	 * @throws Exception void
	 */
	public void typeModifyList(List<AttrVO> dbAttrList,List<AttrVO> attrList,List<AttrVO> updateList,List<AttrVO> insertList ) 
			throws Exception {
		
		boolean isFlag = false;
	
		for(int k=0;k < attrList.size(); k++) {
			
			AttrVO attrVO = (AttrVO)attrList.get(k);
			
			for(AttrVO dbAttr : dbAttrList)	{
				
				if(attrVO.getAttr_id().equals(dbAttr.getAttr_id())) {

					updateList.add(attrVO);		// 수정목록 추가
					
					isFlag = true;					
					break;
				}
						
			}	// end of dbAttr

			if(!isFlag)	{	insertList.add(attrVO);	}
			
			isFlag = false;			
		}
		
	}
	
}
