package kr.co.exsoft.common.service;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import kr.co.exsoft.common.dao.CommonDao;
import kr.co.exsoft.common.dao.CodeDao;
import kr.co.exsoft.common.dao.MenuDao;
import kr.co.exsoft.common.dao.HistoryDao;
import kr.co.exsoft.common.dao.ConfDao;
import kr.co.exsoft.process.dao.ProcessDao;
import kr.co.exsoft.rgate.dao.RgateDao;
import kr.co.exsoft.user.dao.GroupDao;
import kr.co.exsoft.user.dao.UserDao;
import kr.co.exsoft.user.vo.GroupVO;
import kr.co.exsoft.common.vo.CodeVO;
import kr.co.exsoft.common.vo.ConfVO;
import kr.co.exsoft.common.vo.HistoryVO;
import kr.co.exsoft.common.vo.MenuAuthVO;
import kr.co.exsoft.common.vo.MenuVO;
import kr.co.exsoft.common.vo.PageHtVO;
import kr.co.exsoft.common.vo.RecentlyObjectVO;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.dao.PageDao;
import kr.co.exsoft.document.vo.PageVO;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import kr.co.exsoft.eframework.library.ExsoftAbstractServiceImpl;
import kr.co.exsoft.eframework.handler.SessionManager;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.repository.EXrepClient;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.util.LicenseUtil;
import kr.co.exsoft.eframework.util.PagingAjaxUtil;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.UtilFileApp;
import kr.co.exsoft.folder.dao.FolderDao;
import kr.co.exsoft.folder.vo.FolderVO;
import net.sf.json.JSONArray;

/**
 * 메뉴/코드/세션 서비스 구현 부분
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Service("commonService")
public class CommonServiceImpl extends ExsoftAbstractServiceImpl implements CommonService {

	@Autowired
	@Qualifier("sqlSession")
	private SqlSession sqlSession;
	
	@Autowired
	@Qualifier("sqlSessionBatch")
	private SqlSession sqlSessionBatch;
	
	protected static final Log logger = LogFactory.getLog(CommonServiceImpl.class);
	
	// 폴더경로 및 권한부서목록 리스트 전역함수 설정
	protected static List<String> authGroupList = new ArrayList<String>();
	protected static List<String> folderList = new ArrayList<String>();
	
	
	@Override
	public int commonNextVal(String counter_id) throws Exception {
		
		int ret = 0 ;
		
		CommonDao commonDao = sqlSession.getMapper(CommonDao.class);
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("counter_id", counter_id);
		if(ConfigData.getBoolean("DATABASE.FUNCTION"))	{							
			ret = commonDao.comNextVal(map);
		}else {
			commonDao.comNextValInc(map);
			ret = commonDao.comCurrvalTable(map);
		}
		
		return ret;
	}
	
	@Override
	public int currentVal(String counter_id) throws Exception {
		
		int ret = 0;
		
		CommonDao commonDao = sqlSession.getMapper(CommonDao.class);
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("counter_id", counter_id);
		
		ret  = commonDao.comCurrentVal(map);		
		
		return ret;
	}
	
	@Override
	public int nextValTable(String counter_id) throws Exception{
		
		int ret = 0;
		
		CommonDao commonDao = sqlSession.getMapper(CommonDao.class);
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("counter_id", counter_id);
		
		commonDao.comNextValInc(map);
		ret = commonDao.comCurrvalTable(map);
		
		return ret;
	}
	
	@Override
	public CodeVO codeDetail(HashMap<String,Object> map) throws Exception {
		
		CodeVO ret = new CodeVO();
		
		CodeDao codeDao = sqlSession.getMapper(CodeDao.class);
		
		ret = codeDao.codeDetail(map);
		
		return ret;
	}
		
	@Override
	public List<CodeVO> codeList(HashMap<String,Object> map) throws Exception {
		
		CodeDao codeDao = sqlSession.getMapper(CodeDao.class);
		
		List<CodeVO> ret = new ArrayList<CodeVO>();
	
		ret = codeDao.codeSelectList(map);
		
		return ret;
	}
	
	@Override
	public int editorWrite(HashMap<String,Object> map) throws Exception {
		
		int ret = 0;
		
		CommonDao commonDao = sqlSession.getMapper(CommonDao.class);
		
		ret = commonDao.editorWrite(map);
		
		return ret;
	}
	
	@Override
	public CaseInsensitiveMap editorDetailInfo(HashMap<String,Object> map) throws Exception {
		
		CaseInsensitiveMap ret = new CaseInsensitiveMap();
		
		CommonDao commonDao = sqlSession.getMapper(CommonDao.class);
		
		ret = commonDao.editorDetailInfo(map);
		
		return ret;
	}
	
	@Override
	public boolean checkUserLicense() throws Exception {
		
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		
		boolean ret = false;
		
		String[] licenseInfo = null;
		String license = "";
		
		try {
			// 1.라이센스키 복호화
			license = LicenseUtil.decipherLicenseKeyWeb(ConfigData.getString("LICENSE_KEY"));
			
		}catch(Exception e)	{
			logger.error(e.getMessage());
			throw processException("login.fail.license.invalid");
		}
		
		// 2.라이센스 유효성을 체크한다.
		// 2.0 라이센스키 존재여부
		if (StringUtils.isBlank(license)) {
			throw processException("login.fail.license.invalid");
		}
		
		// 2.1 세개의 값으로 이루어져 있는지 확인한다.
		licenseInfo = StringUtil.split2Array(license, "|", false);
		if (licenseInfo.length != 3) {
			throw processException("login.fail.license.invalid");
		}

		// 2.2 첫번째 값을 확인한다.
		if (!StringUtils.equals(licenseInfo[0], "EDMsl")) {
			throw processException("login.fail.license.invalid");
		}
		
		// 2.3 두번째 값을 확인한다.
		if (!StringUtils.equals(licenseInfo[1], Constant.LICENSE_TYPE_CONCURRENT) 
				&& !StringUtils.equals(licenseInfo[1], Constant.LICENSE_TYPE_NAMED)) {
			throw processException("login.fail.license.invalid");
		}
		
		// 2.4 세번째 값을 확인한다.
		if (!StringUtil.isNumeric(licenseInfo[2]) || Integer.parseInt(licenseInfo[2]) < 0) {
			throw processException("login.fail.license.invalid");
		}
		
		String licenseType = licenseInfo[1];
		int licenseUserCount = Integer.parseInt(licenseInfo[2]);	
		
		logger.info("License type = " + licenseType);
		logger.info("License user count = " + licenseUserCount);
		
		// 라이센스 인원이 무제한인 경우.
		if (licenseUserCount == 0) {			
			ret = true;
		} else {
			
			int currentUserCount = 0;
			if (StringUtils.equals(licenseType, Constant.LICENSE_TYPE_CONCURRENT)) {
				List<String> sessionUserIdList = SessionManager.getSessionUserIdList();
				currentUserCount = sessionUserIdList.size();			
			}else if (StringUtils.equals(licenseType, Constant.LICENSE_TYPE_NAMED)) {
				currentUserCount = userDao.namedUserCount();
			}
			logger.info("currentUserCount = " + currentUserCount);
			
			if (currentUserCount < licenseUserCount) {
				ret = true;
			}				
		}
		
		
		return  ret;
	}
	
	@Override
	public Map<String, Object> codePageList(HashMap<String,Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
	
		List<CodeVO> ret = new ArrayList<CodeVO>();
		int total = 0;
		
		CodeDao codeDao = sqlSession.getMapper(CodeDao.class);
			
		total = codeDao.codePagingCount(map) ;
		ret = codeDao.codePagingList(map);
				
		resultMap.put("page",map.get("nPage").toString());
		resultMap.put("records",total);
		resultMap.put("total",CommonUtil.getTotPageSize(total,Integer.parseInt(map.get("page_size").toString())));	
		resultMap.put("list",ret);
				
		// Ajax Paging 
		String strLink = "javascript:exsoftAdminMenuFunc.event.gridPage";
		String contextRoot = map.get("contextRoot") != null ? map.get("contextRoot").toString() : "";
		PagingAjaxUtil pagingInfo = new PagingAjaxUtil(Integer.parseInt(map.get("nPage").toString()),total,Integer.parseInt(map.get("page_size").toString()),5,strLink,contextRoot);		
		resultMap.put("pagingInfo",pagingInfo);	
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> menuAuthList(HashMap<String,Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<MenuAuthVO> ret = new ArrayList<MenuAuthVO>();
		
		MenuDao menuDao = sqlSession.getMapper(MenuDao.class);
		
		ret = menuDao.menuAuthList(map);
		
		resultMap.put("page",1);
		resultMap.put("total",ret.size());
		resultMap.put("list",ret);
				
		return resultMap;
	}
	
	@Override
	public List<MenuAuthVO> adminMenuAuthList(HashMap<String,Object> map) throws Exception {
		
		List<MenuAuthVO> ret = new ArrayList<MenuAuthVO>();
		
		MenuDao menuDao = sqlSession.getMapper(MenuDao.class);
		
		ret = menuDao.menuAuthList(map);
		
		return ret;
	}
	
	@Override
	public  Map<String, Object> codeManager(CodeVO codeVO,HashMap<String,Object> map) throws Exception {
		
		CodeDao codeDao = sqlSession.getMapper(CodeDao.class);
		MenuDao menuDao = sqlSession.getMapper(MenuDao.class);
		
		// 리턴값 정의
		Map<String, Object> resultMap = new HashMap<String, Object>();		
		resultMap.put("result",Constant.RESULT_FALSE);
		
		HashMap<String,Object> param1 = new HashMap<String,Object>();
		HashMap<String,Object> param2 = new HashMap<String,Object>();
		String bizType = map.get(Constant.TYPE).toString();
		
		CodeVO resultVO = new CodeVO();
		
		int ret = 0;
		
		/*********************************************************************
		 * 해당 함수는 추후 다국어 코드 테이블을 확장하여야 한다.
		 *********************************************************************/
		
		if(bizType.equals(Constant.INSERT))	{
		
			// Case1. 공통코드 등록처리
									
			// 1.1 중복코드값 체크
			param1.put("code_id",codeVO.getCode_id());
			param1.put("gcode_id",codeVO.getGcode_id());
			param1.put("type",Constant.INSERT);
			resultVO = codeDao.codeDetail(param1);
			
			if(resultVO != null)	{				
				throw processException("code.fail.id.double");
			}
			
			// 1.2 중복코드명 체크
			param2.put("code_nm",codeVO.getCode_nm());
			param2.put("gcode_id",codeVO.getGcode_id());
			param2.put("type",Constant.INSERT);
			resultVO = codeDao.codeDetail(param2);
			
			if(resultVO != null)	{				
				throw processException("code.fail.name.double");
			}
			
			// 1.3 코드값 등록처리
			codeVO.setSort_index(codeDao.codeMaxSortIndex(param1));		// 동일 그룹코드내의 MAX SORT_INDEX	
			ret = codeDao.codeWrite(codeVO);
			
		}else if(bizType.equals(Constant.UPDATE))	{
			
			// Case2. 공통코드 수정처리
			
			// 2.1 코드명 중복체크					
			param1.put("code_id",codeVO.getCode_id());
			param1.put("gcode_id",codeVO.getGcode_id());
			param1.put("code_nm",codeVO.getCode_nm());
			param1.put("type",Constant.UPDATE);
			resultVO = codeDao.codeDetail(param1);
			
			if(resultVO != null)	{				
				throw processException("code.fail.name.double");
			}
			
			ret = codeDao.codeUpdate(param1);
			
		}else if(bizType.equals(Constant.DELETE))	{
			
			// Case3. 공통코드 삭제처리 - 단 IS_SYS=N 인 경우에만 삭제가능함.
			
			// 3.1 코드값 사용중인지 체크한다. XR_USER
			param1.put("role_id",codeVO.getCode_id());
			if(codeDao.isRoleUsing(param1) > 0)	{
				throw processException("code.fail.id.use");
			}
			
			// 3.2 XR_MENU_AUTH DELETE
			MenuAuthVO menuAuthVO = new MenuAuthVO();
			menuAuthVO.setRole_id(codeVO.getCode_id());
			ret = menuDao.menuAuthDelete(menuAuthVO);
			
			// 3.3 XR_CODE DELETE(CODE_ID/GCODE_ID)
			param1.put("code_id",codeVO.getCode_id());
			param1.put("gcode_id",codeVO.getGcode_id());
			ret = codeDao.codeDelete(param1);
						
		}
		
		if(ret == 1) {
			resultMap.put("result",Constant.RESULT_TRUE);
		}
		
		return resultMap;
	}
	
	
	@Override
	public Map<String, Object> menuAuthManager(List<MenuAuthVO> menuAuthList,HashMap<String,Object> map) throws Exception{
		
		MenuDao menuDao = sqlSessionBatch.getMapper(MenuDao.class);
		HashMap<String,Object> param = new HashMap<String,Object>();
		
		// 리턴값 정의
		Map<String, Object> resultMap = new HashMap<String, Object>();		
		resultMap.put("result",Constant.RESULT_FALSE);
		
		String bizType = map.get(Constant.TYPE).toString();
		
		if(menuAuthList != null && menuAuthList.size() >0 )	{
										
			if(bizType.equals(Constant.INSERT))	{						// 메뉴권한 등록처리
				
				for(MenuAuthVO menuAuthVO : menuAuthList) {					

					HashMap<String,Object> menuMap = new HashMap<String,Object>();
					
					menuMap.put("role_id",menuAuthVO.getRole_id());
					menuMap.put("gcode_id",menuAuthVO.getGcode_id());
					menuMap.put("menu_cd",menuAuthVO.getMenu_cd());

					// 존재하지 않는 데이터에 한해서만 등록처리
					if(menuDao.menuAuthDetail(menuMap) == null) {
						
						// 상위메뉴가 존재하는 체크한다.(상위메뉴명 정보 가져온다.)
						param.put("menu_cd",menuAuthVO.getMenu_cd());
						MenuVO menuVO = menuDao.menuDetail(param);
						if(menuVO != null && menuVO.getMenu_level() == 1 
								&& !menuVO.getSu_menu_cd().equals(menuVO.getMenu_cd()))	{
							
							// 상위메뉴가 존재하는 경우 권한등록 여부를 체크한다.
							menuMap.put("menu_cd",menuVO.getSu_menu_cd());
							
							// 선택 메뉴 상위 등록처리
							if(menuDao.menuAuthDetail(menuMap) == null) {
								
								MenuAuthVO upperAuth = new MenuAuthVO();
								upperAuth.setRole_id(menuAuthVO.getRole_id());
								upperAuth.setGcode_id(menuAuthVO.getGcode_id());
								upperAuth.setMenu_cd(menuVO.getSu_menu_cd());
								upperAuth.setPart(Constant.MENU_ALL);
								
								menuDao.menuAuthWrite(upperAuth);
							}
						}
						
						// 선택 메뉴 등록처리 
						menuDao.menuAuthWrite(menuAuthVO);
					}
				}
				
				resultMap.put("result",Constant.RESULT_TRUE);
				
			}else if(bizType.equals(Constant.UPDATE))	{				// 메뉴권한 수정처리
				
				// 메뉴권한 삭제처리
				for(MenuAuthVO menuAuthVO : menuAuthList) {										
					menuDao.menuAuthDelete(menuAuthVO);					
				}
				
				// 메뉴권한 입력처리
				for(MenuAuthVO menuAuthVO : menuAuthList) {										
					menuDao.menuAuthWrite(menuAuthVO)	;
				}
				
				resultMap.put("result",Constant.RESULT_TRUE);
				
				
			}else if(bizType.equals(Constant.DELETE))	{					// 메뉴권한 삭제처리
				
				// 메뉴권한 삭제처리
				// 최상위 레벨인 경우 하위 메뉴가 선택하지 않는 경우라도 삭제처리한다.
				for(MenuAuthVO menuAuthVO : menuAuthList) {										
					
					MenuAuthVO chkInfo = new MenuAuthVO();
					int menu_level = 0;
					
					HashMap<String,Object> menuMap = new HashMap<String,Object>();
					
					menuMap.put("role_id",menuAuthVO.getRole_id());
					menuMap.put("gcode_id",menuAuthVO.getGcode_id());
					menuMap.put("menu_cd",menuAuthVO.getMenu_cd());
					
					chkInfo = menuDao.menuAuthDetail(menuMap); 				
					if(chkInfo != null)	{		// 상위메뉴에 의해서 하위메뉴가 삭제된 경우는 skip 처리한다.
						
						menu_level = chkInfo.getMenu_level();
						
						if(menu_level == 1)	{
							// 해당 메뉴만 삭제처리해준다.
							menuDao.menuAuthDelete(menuAuthVO);
						}else {
							// 상위메뉴인 경우 하위 메뉴도 같이 삭제처리해준다.
							menuDao.subMenuAuthDelete(menuAuthVO);
						}
					}
					
				}
				
				resultMap.put("result",Constant.RESULT_TRUE);
			}
		
		}
		
		return resultMap;
	}
		
	
	@Override
	public List<MenuAuthVO> setMenuAuthParam(String[] arrData) {
		
		 List<MenuAuthVO> menuAuthList = new ArrayList<MenuAuthVO>();
		 String[] colummStr = null;
		 
		 if(arrData != null) {
			 
			 for(String data : arrData) {

					MenuAuthVO menuAuthVO = new MenuAuthVO();
					
					colummStr = data.split("#");
					
					menuAuthVO.setRole_id(colummStr[0]);
					menuAuthVO.setGcode_id(colummStr[1]);
					menuAuthVO.setMenu_cd(colummStr[2]);
					if(colummStr.length == 4)		{
						menuAuthVO.setPart(colummStr[3]);
					}
					
					menuAuthList.add(menuAuthVO);
					
				}
		 }
		 
		 return menuAuthList;
	}
	
	@Override
	public List<MenuAuthVO> setMenuAuthParam(String[] arrData,HashMap<String,Object> map) {
		
		 List<MenuAuthVO> menuAuthList = new ArrayList<MenuAuthVO>();
		 
		 if(arrData != null) {
			 
			 for(String data : arrData) {

					MenuAuthVO menuAuthVO = new MenuAuthVO();

					menuAuthVO.setRole_id(map.get("role_id").toString());
					menuAuthVO.setGcode_id(Constant.CODE_ROLE);
					menuAuthVO.setMenu_cd(data);
										
					menuAuthList.add(menuAuthVO);
					
				}
		 }
		 		 
		 return menuAuthList;
	}
	
	@Override
	public Map<String, Object> menuList(HashMap<String,Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<MenuVO> ret = new ArrayList<MenuVO>();
		
		MenuDao menuDao = sqlSession.getMapper(MenuDao.class);
		
		map.put("is_menu_auth",Constant.T);		// 메뉴권한적용대상인 경우
		
		// [APPLIANCE VERSION] 
		if(ConfigData.getString("VERSION_INFO") != null && 
					ConfigData.getString("VERSION_INFO").equals(Constant.PRODUCT_EDMS_APPLIANCE)) {
			map.put("is_appliance",Constant.T);
		}
		
		ret = menuDao.menuList(map);
		
		resultMap.put("page",1);
		resultMap.put("total",ret.size());
		resultMap.put("list",ret);
		
		return resultMap;
	}
	
	@Override
	public String getMenuAuth(HashMap<String,Object> map) throws Exception {
		
		String ret = "";
		
		MenuAuthVO menuAuthVO = new MenuAuthVO();
		MenuDao menuDao = sqlSession.getMapper(MenuDao.class);
		
		menuAuthVO = menuDao.menuAuthDetail(map);
				
		if(menuAuthVO != null) {
			ret = menuAuthVO.getPart();
		}
		
		return ret;
	}
	
	@Override
	public int historyWrite(HistoryVO historyVO) throws Exception {
		
		int ret = 0;
		
		HistoryDao historyDao = sqlSession.getMapper(HistoryDao.class);
		
		ret = historyDao.historyWrite(historyVO);
		
		return ret;
	}
	
	@Override
	public Map<String, Object> auditConfig(HashMap<String,Object> map) throws Exception {
	
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ConfDao confDao = sqlSession.getMapper(ConfDao.class);

		// 1. 열람감사정책 조회(변경테이블 적용완료)		
		List<ConfVO> confList =  new ArrayList<ConfVO>();
		confList = confDao.sysConfigDetail(map);
		
		if(confList != null & confList.size() > 0)	{
			
			for(ConfVO conf : confList)	{
								
				switch(conf.getSkey())	{
				
					case "THRESHOLD":
						resultMap.put("read_count_threshold", conf.getSval());
						break;
					case "MAIL":
						resultMap.put("send_report_mail", conf.getSval());
						break;
					case "ADDRESS":
						resultMap.put("report_mail_receiver_address", conf.getSval());
						break;	
				}
				
			}
			
		}else {
			// 시스템 초기값 설정
			resultMap.put("send_report_mail",Constant.F);
			resultMap.put("read_count_threshold", 100);
			resultMap.put("report_mail_receiver_address", "");
		}

		return resultMap;
	}
	
	@Override
	public List<HashMap<String,Object>> trashConfig(HashMap<String,Object> map) throws Exception {
		
		List<HashMap<String,Object>> ret = new ArrayList<HashMap<String,Object>>();
		
		ConfDao confDao = sqlSession.getMapper(ConfDao.class);
		
		// 1.휴지통관리 정책 :: 개인/시스템 정보 가져오기
		List<ConfVO> trashList =  new ArrayList<ConfVO>();
		
		trashList = confDao.sysConfigDetail(map);
		if(trashList != null & trashList.size() > 0)	{
			
			for(ConfVO conf : trashList)	{
					
				HashMap<String,Object> retMap = new HashMap<String,Object>();
				
				retMap.put("doc_type",conf.getSkey());
				retMap.put("is_use",conf.getIs_use());
				retMap.put("decade",conf.getSval());
				
				ret.add(retMap);
			}
			
		}else {
			
			// 휴지통 정책(개인/시스템) 기본값 설정
			HashMap<String,Object> retMap = new HashMap<String,Object>();
			
			// 1.개인휴지통 관리
			retMap.put("doc_type",Constant.PRIVATE_TRASH);
			retMap.put("is_use",Constant.YES);
			retMap.put("decade",ConfigData.getInt("PRIVATE.TRASH.DECADE"));
			ret.add(retMap);
			
			// 2.시스템휴지통 관리
			retMap.put("doc_type",Constant.SYSTEM_TRASH);
			retMap.put("is_use",Constant.YES);
			retMap.put("decade",ConfigData.getInt("SYSTEM.TRASH.DECADE"));
			ret.add(retMap);
		}

		return ret;
	}
	
		
	@Override
	public Map<String, Object> confDetail(HashMap<String,Object> map) throws Exception {
				
		ConfDao confDao = sqlSession.getMapper(ConfDao.class);
		//RgateDao rgateDao = sqlSession.getMapper(RgateDao.class);
					
		List<HashMap<String,Object>> trashList =  new ArrayList<HashMap<String,Object>>();
		HashMap<String,Object> param = new HashMap<String,Object>();		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String,Object> auditMap = new HashMap<String,Object>();
		CaseInsensitiveMap urlConfig = new CaseInsensitiveMap();
		
		List<CaseInsensitiveMap> versionConfig =  new ArrayList<CaseInsensitiveMap>();

		// 0.입력값 체크
		if(map.get("type") == null || map.get("stype") == null)	{
			throw processException("common.required.error");	
		}else if(!map.get("type").equals(Constant.SELECT))	{
			throw processException("common.required.error");
		}
		
		String stype = map.get("stype").toString();
			
		if(stype.equals(Constant.SYS_TYPE_AUDIT)) {				// 열람감사정책 조회(테이블변경처리)
		
			param.put("stype",Constant.SYS_TYPE_AUDIT);
			auditMap = this.auditConfig(param);
			resultMap.put("audit", auditMap);
			
		}else if(stype.equals(Constant.SYS_TYPE_TRASH)) {		// 휴지통관리(개인/시스템)	
			
			param.put("stype",Constant.SYS_TYPE_TRASH);
			trashList = this.trashConfig(param);
			for(HashMap<String,Object> trashMap : trashList) {
				if(trashMap.get("doc_type") != null &&  trashMap.get("doc_type").toString().equals(Constant.PRIVATE_TRASH)) {
					resultMap.put("ptrash", trashMap);		// 개인 휴지통 정책
				}else {
					resultMap.put("strash", trashMap);		// 시스템 휴지통 정책
				}			
			}	
		}else if(stype.equals(Constant.SYS_TYPE_FILE)) {			// 문서등록 설정
			
			List<ConfVO> confList =  new ArrayList<ConfVO>();
			param.put("stype", Constant.SYS_TYPE_FILE);
			confList = confDao.sysConfigDetail(map);
			
			if(confList != null & confList.size() > 0)	{
				
				for(ConfVO conf : confList)	{					
					resultMap.put(conf.getSkey(),conf);
				}
				
			}else {				
				// 시스템 기본값 설정
				resultMap.put("EXT","exe;bat;dll;ocx;");
				resultMap.put("FILECNT",10);
				resultMap.put("FILESIZE",1024);
				resultMap.put("FILETOTAL",4096);
			}						
		}else if(stype.equals(Constant.SYS_TYPE_VERSION)) {		// 버전관리(개인문서/업무문서)

			versionConfig = confDao.versionConfigDetail();
			for(CaseInsensitiveMap versonMap : versionConfig) {
				
				if(versonMap.get("vkey") != null &&  versonMap.get("vkey").toString().equals(Constant.MAP_ID_MYPAGE)) {
					resultMap.put("mypage",versonMap);
				}else {
					resultMap.put("workspace",versonMap);
				}
			}

		}else if(stype.equals(Constant.SYS_TYPE_URL)) {				// URL 유효기간 설정
			urlConfig = confDao.urlConfigDetail();
			resultMap.put("expired",urlConfig.get("val").toString());
		}

		/*
		 * 		// 3.rGate 정책주기
		param.put("ckey",Constant.LSC_POLICY_UPDATE_CYCLE);
		resultMap.put("cvalue", rgateDao.rgatePolicyDetail(param));	
		 */
	
		resultMap.put("result",Constant.RESULT_TRUE);
		
		return resultMap;
	}

	@Override
	public Map<String, Object> confProc(HashMap<String,Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ConfDao confDao = sqlSession.getMapper(ConfDao.class);
		//RgateDao rgateDao = sqlSession.getMapper(RgateDao.class);
		
		HashMap<String,Object> param = new  HashMap<String,Object>();
		
		/************************************************************************
		 * 테이블변경으로 인한 DAO Refactoring 추후
		 ************************************************************************/
		
		int result = 0;
		
		if(map.get("stype") == null)	{
			throw processException("common.required.error");	
		}else if(!map.get("type").equals(Constant.UPDATE))	{
			throw processException("common.required.error");
		}
		
		String stype = map.get("stype").toString();
		
		if(stype.equals(Constant.SYS_TYPE_AUDIT)) {
			
			param.put("skey","THRESHOLD");
			param.put("sval",map.get(Constant.READ_COUNT_THRESHOLD));
			result = confDao.auditConfigUpdate(param);
			if(result == 0)	{	throw processException("common.system.error");	}			
			
			param.put("skey","MAIL");
			param.put("sval",map.get(Constant.SEND_REPORT_MAIL));
			result = confDao.auditConfigUpdate(param);
			if(result == 0)	{	throw processException("common.system.error");	}			
			
			param.put("skey","ADDRESS");
			param.put("sval",map.get(Constant.REPORT_MAIL_RECEIVER_ADDRESS));
			result = confDao.auditConfigUpdate(param);
			if(result == 0)	{	throw processException("common.system.error");	}						
			
		}else if(stype.equals(Constant.SYS_TYPE_TRASH)) {
			
			// 개인휴지통 관리
			param.put("doc_type",Constant.PRIVATE_TRASH);
			param.put("is_use", map.get("pis_use"));
			param.put("decade", map.get("pdecade") != null ? map.get("pdecade") : ConfigData.getInt("PRIVATE.TRASH.DECADE")  );
			result = confDao.trashConfigUpdate(param);
			if(result == 0)	{	throw processException("common.system.error");	}
						
			// 시스템휴지통 관리
			param.put("doc_type",Constant.SYSTEM_TRASH);
			param.put("is_use", map.get("sis_use"));
			param.put("decade", map.get("sdecade") != null ? map.get("sdecade") : ConfigData.getInt("SYSTEM.TRASH.DECADE") );
			result = confDao.trashConfigUpdate(param);
			if(result == 0)	{	throw processException("common.system.error");	}			
						
		}else if(stype.equals(Constant.SYS_TYPE_FILE)) {	
			
			// 문서관리 파일설정
			result = updateFileConfig(confDao,map,Constant.FILE_EXT);
			if(result == 0)	{	throw processException("common.system.error");	}
			
			result = updateFileConfig(confDao,map,Constant.FILE_CNT);
			if(result == 0)	{	throw processException("common.system.error");	}
			
			result = updateFileConfig(confDao,map,Constant.FILE_SIZE);
			if(result == 0)	{	throw processException("common.system.error");	}
			
			result = updateFileConfig(confDao,map,Constant.FILE_TOTAL);
			if(result == 0)	{	throw processException("common.system.error");	}
			
		}else if(stype.equals(Constant.SYS_TYPE_VERSION)) {
			
			// 개인문서 버전관리
			param.put("vkey",Constant.MAP_ID_MYPAGE);
			param.put("val",map.get("mVersion"));
			param.put("is_use",map.get("is_mVersion"));
			result = confDao.versionConfigUpdate(param);
			if(result == 0)	{	throw processException("common.system.error");	}
			
			// 업무문서 버전관리
			param.put("vkey",Constant.MAP_ID_WORKSPACE); 
			param.put("val",map.get("wVersion"));
			param.put("is_use",map.get("is_wVersion"));
			result = confDao.versionConfigUpdate(param);
			if(result == 0)	{	throw processException("common.system.error");	}
			
		}else if(stype.equals(Constant.SYS_TYPE_URL)) {		
			
			param.put("vkey",Constant.SYSTEM_EXPIRED);
			param.put("val",map.get("expired"));
			result = confDao.urlConfigUpdate(param);
			if(result == 0)	{	throw processException("common.system.error");	}
			
		}
		
		/*
		// rGate 정책주기
		rMap.put("ckey",Constant.LSC_POLICY_UPDATE_CYCLE);
		rMap.put("cvalue",map.get("cvalue"));
		result = rgateDao.rgatePolicyUpdate(rMap);
		if(result == 0)	{	throw processException("common.system.error");	}
		*/
	
		resultMap.put("result",Constant.RESULT_TRUE);
		
		return resultMap;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  문서관리 첨부파일 설정 수정처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : updateFileConfig
	 * @param confDao
	 * @param map
	 * @param key
	 * @return int
	 */
	public int updateFileConfig(ConfDao confDao,HashMap<String,Object> map,String key) {
		
		int result = 0;

		HashMap<String,Object> param = new  HashMap<String,Object>();
		
		switch(key)	{
		
			case "EXT":				
				param.put("fkey","EXT");
				param.put("fval",map.get("ext"));					
				break;
				
			case "FILECNT":
				param.put("fkey","FILECNT");
				if(map.get("is_fileCnt") != null && map.get("is_fileCnt").toString().equals(Constant.YES)) {			
					param.put("fval",map.get("fileCnt"));
					param.put("is_use",Constant.YES);
				}else {
					param.put("is_use",Constant.NO);
				}
				break;
				
			case "FILESIZE":
				param.put("fkey","FILESIZE");
				if(map.get("is_fileSize") != null && map.get("is_fileSize").toString().equals(Constant.YES)) {			
					param.put("fval",map.get("fileSize"));
					param.put("is_use",Constant.YES);
				}else {
					param.put("is_use",Constant.NO);
				}
				break;
				
			case "FILETOTAL":
				param.put("fkey","FILETOTAL");
				if(map.get("is_fileTotal") != null && map.get("is_fileTotal").toString().equals(Constant.YES)) {			
					param.put("fval",map.get("fileTotal"));
					param.put("is_use",Constant.YES);
				}else {
					param.put("is_use",Constant.NO);
				}
				break;
		}
		
		result = confDao.fileConfigUpdate(param);

		return result;
	}
	
	@Override
	public Map<String, Object> pageMenuInfo(HashMap<String,Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();		
		
		MenuDao menuDao = sqlSessionBatch.getMapper(MenuDao.class);
		MenuVO menuVO = menuDao.menuDetail(map);

		resultMap.put("menu_nm",menuVO.getMenu_nm());
		resultMap.put("menu_cd",menuVO.getMenu_cd());
		resultMap.put("su_menu_nm",menuVO.getSu_menu_nm());
		
		return resultMap;
	}

	@Override
	public List<String> authGroupList(String part,SessionVO sessionVO) throws Exception {
		
		List<String> ret = new ArrayList<String>();

		// 1.ROLE 권한 정보 
		List<String> authGruoupList = new ArrayList<String>();
	
		// **겸직부서는 고도화시 적용처리한다. 
		if(part.equals(Constant.MENU_TEAM)) {
			authGruoupList.add(sessionVO.getSessManage_group());
		}else	if(part.equals(Constant.MENU_GROUP)) {
			authGruoupList = getChildGroup(sessionVO.getSessManage_group());				
		}
		
		// 관리대상 부서정보 리스트를 가져온다.
		ret = new ArrayList<String>(new HashSet<String>(authGruoupList));			

		return ret;
		
	}
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 하위 그룹 목록 가져오기 MAIN
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getChildGroup
	 * @param group_id
	 * @return List<String>
	 */
	public List<String> getChildGroup(String group_id) {

		authGroupList.clear();

		// 1. 하위 그룹 목록 가져오기 RECURSIVE
		getLowLevelNode(group_id);
		
		// 2. 요청 GROUP_ID 리스트 추가 처리
		authGroupList.add(group_id);
		
		return authGroupList;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 하위 그룹 목록 가져오기 RECURSIVE
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getLowLevelNode
	 * @param group_id void
	 */
	public void getLowLevelNode(String group_id) {
		
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("parentId", group_id);
		
		List<GroupVO> groupList = new ArrayList<GroupVO>();
		
		groupList = groupDao.childGroupList(param);
		
		
		if(groupList.size() > 0 ) {
			
			for(GroupVO groupVO : groupList)  {
				
				// 1.GROUP_ID를 LIST에 추가
				authGroupList.add(groupVO.getGroup_id());
				
				// 2.RECURSIVE 처리
				getLowLevelNode(groupVO.getGroup_id());
				
			}
			
		}else {
			authGroupList.add(group_id);
		}

	}
				
	@Override
	public String folderFullPath(HashMap<String, Object> map) throws Exception {
		
		String fullPath = "";
		StringBuffer buf = new StringBuffer();
		
		folderList.clear();
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		// 1.FOLDER 가 존재하는지 체크한다.
		FolderVO folderVO = folderDao.folderDetail(map);
		if(folderVO != null)	{
			
			folderList.add(folderVO.getFolder_name_ko());
			
			getLowLevelFolder(folderDao,folderVO);
		}

		// 2.Folder 전체경로 가져오기.
		if(folderList.size() >0) {
			
			for(int i=folderList.size()-1;i>=0;i--) {
				
				String folderPath = (String)folderList.get(i);
				buf.append("/");			
				buf.append(folderPath);				
			}
			
			fullPath = buf.toString();			
		}
		
		return fullPath;
	}

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 상위 폴더 목록 가져오기 RECURSIVE
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getLowLevelFolder
	 * @param folderDao
	 * @param folderVO void
	 */
	public void getLowLevelFolder(FolderDao folderDao,FolderVO folderVO) {
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("folder_id",folderVO.getParent_id());		
		FolderVO vo = folderDao.folderDetail(param);
  
		if(!folderVO.getParent_id().equals(folderVO.getFolder_id()) && !folderVO.getParent_id().equals("") ) {
			// 최상위 폴더가 아닌경우 RECURSIVE
			folderList.add(vo.getFolder_name_ko());			
			getLowLevelFolder(folderDao,vo);
		}
	}
	
	
	@Override
	public List<PageVO> setPageList(HashMap<String, Object> map) throws Exception {
		
		List<String> page_list = new ArrayList<String>();
		List<PageVO> downList = new ArrayList<PageVO>();
		List<PageVO> ret = new ArrayList<PageVO>();
		
		PageDao pageDao = sqlSession.getMapper(PageDao.class);
		
		/***********************************************************************************************************
		 * pageList=[{"page_id":"DOC000000007536"},{"page_id":"DOC000000007535"}]
		 ***********************************************************************************************************/
		String pageList =  map.get("pageList") != null ? map.get("pageList").toString() : "";
		
		// 1. 입력값 유효성 체크
		if(pageList.equals("") ||  pageList.equals(""))	{
			throw processException("common.required.error");
		}
		
		// 2. JsonArray To List
		JSONArray jsonArray = JSONArray.fromObject(pageList);
		if(jsonArray.size() > 0 ) {		
			 for(int j=0;j < jsonArray.size();j++)	{					 
				 page_list.add(jsonArray.getJSONObject(j).getString("page_id").toString());
			 }
		}
		
		// 3.PageVO 객체 구하기
		HashMap<String,Object> param = new HashMap<String,Object>();
		param.put("page_list", page_list);
		downList = pageDao.downPageList(param);
		
		// 4.EXREP 연계해서 파일정보 가져오기. :: eXrep C/S Client 생성.  
		EXrepClient eXrepClient = new EXrepClient();
		
		if(downList != null && downList.size() > 0)	{
			
			String downPath = ConfigData.getString("FILE_DOWN_PATH") + UUID.randomUUID().toString() + "/";
			UtilFileApp.createDir(downPath);				
			
			// eXrep C/S 접속.
			eXrepClient.connect();		
			
			for(PageVO pageVO : downList){
				
				String downFile = pageVO.getPage_name();
				String savePath = downPath + downFile;
							
				if(eXrepClient.getFile(pageVO.getVolume_id(), pageVO.getContent_path(), savePath)) {					
					pageVO.setDown_path(savePath);
					ret.add(pageVO);
				}else {					
					continue;
				}
									
			}
			
			// eXrep C/S Client Close
			eXrepClient.disconnect();
		}
		
		return ret;
	}
	
	@Override
	public List<PageVO> setPageLocalList(HashMap<String, Object> map) throws Exception {
		
		List<String> page_list = new ArrayList<String>();
		List<PageVO> downList = new ArrayList<PageVO>();
		List<PageVO> ret = new ArrayList<PageVO>();
		
		PageDao pageDao = sqlSession.getMapper(PageDao.class);
		
		String volumePath = "";
		String realFile = "";
		
		/***********************************************************************************************************
		 * pageList=[{"page_id":"DOC000000007536"},{"page_id":"DOC000000007535"}]
		 ***********************************************************************************************************/
		String pageList =  map.get("pageList") != null ? map.get("pageList").toString() : "";
		
		// 1. 입력값 유효성 체크
		if(pageList.equals("") ||  pageList.equals(""))	{
			throw processException("common.required.error");
		}
		
		// 2. JsonArray To List
		JSONArray jsonArray = JSONArray.fromObject(pageList);
		if(jsonArray.size() > 0 ) {		
			 for(int j=0;j < jsonArray.size();j++)	{					 
				 page_list.add(jsonArray.getJSONObject(j).getString("page_id").toString());
			 }
		}
		
		// 3.PageVO 객체 구하기
		HashMap<String,Object> param = new HashMap<String,Object>();
		param.put("page_list", page_list);
		downList = pageDao.downPageList(param);
		
		// 4.Appliacne 인 경우 Local Store 경로에서 가져온다.  		
		if(downList != null && downList.size() > 0)	{
			
			String downPath = ConfigData.getString("FILE_DOWN_PATH") + UUID.randomUUID().toString() + "/";
			UtilFileApp.createDir(downPath);				
			
			for(PageVO pageVO : downList){
				
				String downFile = pageVO.getPage_name();
				String savePath = downPath + downFile;
				
				// (주) Appliance Local Store 사용시 ContentPath에 첫부분에 / 붙지 않는다.)
				volumePath = ConfigData.getString(pageVO.getVolume_id());
				realFile = volumePath + pageVO.getContent_path();
				
				File file = new File(realFile);
				
				if(file.exists())	{
					UtilFileApp.copyfile(realFile,savePath);
					pageVO.setDown_path(savePath);
					ret.add(pageVO);
				}else {
					continue;	
				}
			}

		}
		
		return ret;
	}
	
	@Override
	public void pageHtWrite(List<PageVO> pageList,SessionVO sessionVO) throws Exception {
		
		HistoryDao historyDao = sqlSession.getMapper(HistoryDao.class);
		
		for(PageVO pageVO : pageList){
			
			long page_seq = commonNextVal(Constant.COUNTER_ID_PAGE_HT);			
			
			PageHtVO pageHtVO = new PageHtVO();
			
			pageHtVO.setPage_seq(page_seq);
			pageHtVO.setDoc_id(pageVO.getRef_doc_id());			// XR_PAGE DOC_ID 컬럼과 구분하기 위해 변경처리 해당컬럼은 INDEX VOLUME의 DOC_ID
			pageHtVO.setTarget_id(pageVO.getPage_id());
			pageHtVO.setAction_id(Constant.ACTION_READ);
			pageHtVO.setActor_id(sessionVO.getSessId());
			pageHtVO.setActor_nm(sessionVO.getSessName());
			pageHtVO.setGroup_id(sessionVO.getSessGroup_id());
			pageHtVO.setGroup_nm(sessionVO.getSessGroup_nm());
			pageHtVO.setConnect_ip(sessionVO.getSessRemoteIp());
			pageHtVO.setAction_place(Constant.ACTION_PLACE);			
			historyDao.pageHtWrite(pageHtVO);
		}
		
	}
	
	@Override
	public void setPageToModel(HashMap<String, Object> map,Map<String, Object> menuInfo,
			Map<String, Object> partInfo,SessionVO sessionVO) throws Exception {
	
		MenuDao menuDao = sqlSession.getMapper(MenuDao.class);
		MenuAuthVO menuAuthVO = new MenuAuthVO();
		
		// 0.파라미터 설정
		HashMap<String,Object> param = new HashMap<String,Object>();		
		param.put("menu_cd",map.get("menu_cd") != null ?  map.get("menu_cd").toString() : "" );
		param.put("role_id",sessionVO.getSessRole_id());
						
		// 1.메뉴정보 가져오기
		MenuVO menuVO = menuDao.menuDetail(param);		
		menuInfo.put("menu_nm",menuVO.getMenu_nm());
		menuInfo.put("menu_cd",menuVO.getMenu_cd());
		if(menuVO.getSu_menu_cd().equals(Constant.USERAUTH_MENU))	{
			menuInfo.put("su_menu_nm","문서관리");
		}else {
			menuInfo.put("su_menu_nm",menuVO.getSu_menu_nm());
		}
		
		// 2.메뉴권한 정보 가져오기
		menuAuthVO = menuDao.menuAuthDetail(param);
		if(menuAuthVO != null) {		
			partInfo.put("part", menuAuthVO.getPart());			
		}else {			
			throw processException("result.access.no.auth");
		}
		
	}
	

	@Override
	public PageVO urlPageInfo(HashMap<String, Object> map) throws Exception {
		
		PageDao pageDao = sqlSession.getMapper(PageDao.class);
		
		PageVO ret = new PageVO();
		
		/************************************************************************************
		 * URL 복사 : PAGE_ID || 유효기간
		 ************************************************************************************/
		String params =  map.get("params") != null ? map.get("params").toString() : "";
		String convertData = "";
		
		// 1. 입력값 유효성 체크
		if(params.equals("") ||  params.equals(""))	{
			throw processException("common.required.error");
		}
		
		// 2. URL 복사기간 유효성 체크
		convertData = new String(Base64.decodeBase64(params)) ;
		String[] urlInfo = convertData.split("[#]");	
		String todayStr = StringUtil.getToday().replaceAll("-","");
		
		if(Integer.parseInt(todayStr) > Integer.parseInt(urlInfo[1].replaceAll("-",""))) {
			throw processException("url.expired.date");
		}
				
		// 3.PageVO 객체구하기
		HashMap<String,Object> param = new HashMap<String,Object>();
		param.put("page_id",urlInfo[0]);
		param.put("is_deleted",Constant.F);
		ret = pageDao.pageDetailInfo(param);
		
		if(ret == null)	{
			throw processException("url.file.not.found");
		}
		
		// 4. EXREP 연계해서 파일정보 가져오기. :: eXrep C/S Client 생성.  
		EXrepClient eXrepClient = new EXrepClient();
		
		String downPath = ConfigData.getString("FILE_DOWN_PATH") + UUID.randomUUID().toString() + "/";
		UtilFileApp.createDir(downPath);				
		
		// 5. eXrep C/S 접속.
		eXrepClient.connect();		
		
		String downFile = ret.getPage_name();
		String savePath = downPath + downFile;
				
		if(eXrepClient.getFile(ret.getVolume_id(), ret.getContent_path(), savePath)) {					
			ret.setDown_path(savePath);
		}else {					
			throw processException("url.file.not.found");
		}
		
		// 6. eXrep C/S Client Close
		eXrepClient.disconnect();
				
		return ret;
	}
	
	@Override
	public Map<String, Object> deleteRecently(HashMap<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		CommonDao commonDao = sqlSession.getMapper(CommonDao.class);
		commonDao.deleteRecently(map);
		
		resultMap.put("result",Constant.RESULT_TRUE);

		return resultMap;
	}

	@Override
	public int insertDeleteFileQueue(HashMap<String, Object> map) throws Exception {
		int ret = 0;
		
		CommonDao commonDao = sqlSession.getMapper(CommonDao.class);
		
		ret = commonDao.insertDeleteFileQueue(map);
		
		return ret;
	}

	@Override
	public Map<String, Object> configFileInfo(HashMap<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ConfDao confDao = sqlSession.getMapper(ConfDao.class);
		
		// 1. 첨부파일 관련 설정정보(XR_FILE_CONFIG =>  XR_SYSCONFIG 테이블 통합처리)		
		List<ConfVO> confList =  new ArrayList<ConfVO>();
		map.put("stype", Constant.SYS_TYPE_FILE);
		confList = confDao.sysConfigDetail(map);
		
		for(ConfVO conf : confList)	{			
			resultMap.put(conf.getSkey(), conf);				
		}

		return resultMap;
	}

	@Override
	public int insertRecentlyObject(RecentlyObjectVO recentlyVo) throws Exception {
		
		CommonDao commonDao = sqlSession.getMapper(CommonDao.class);
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		int result = 0;
		// 1. 신규 내용 등록
		result = commonDao.insertRecentlyObject(recentlyVo);
		
		// 2. 15개 이후 내용 삭제
		List<RecentlyObjectVO> list = new ArrayList<RecentlyObjectVO>();
		list = commonDao.selectRecentlyObject(recentlyVo);
		if(list.size() == 16){
			paramMap.put("recently_id", list.get(15).getIdx());
			commonDao.deleteRecently(paramMap);
		}
		
		
		return result;
	}
	
}
