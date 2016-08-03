package kr.co.exsoft.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.folder.dao.FolderDao;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 메모리 캐쉬 구현부분
 *
 * @author 패키지 개발팀
 * @since 2014. 11. 4.
 * @version 1.0
 * 
 */
@Service("CacheService")
public class CacheServiceImpl implements CacheService {
	
	@Autowired
	CacheManager cachemanager;
	
	@Autowired
	@Qualifier("sqlSession")
	private SqlSession sqlSession;
	
	protected static final Log logger = LogFactory.getLog(CacheServiceImpl.class);

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getData
	 * @param cacheName
	 * @param cacheKey
	 * @return
	 * @throws Exception Object
	 */
	private Object getData(String cacheName, String cacheKey) throws Exception {
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		Object result = null;
		
		if(cacheName.equals(Constant.EHCACHE_CACHE_NAME_FOLDERLIST)) {
			Map<String, String> folderIDs = new HashMap<String, String>();
			Map<String, String> folderNames = new HashMap<String, String>();
			List<CaseInsensitiveMap> folderList = new ArrayList<CaseInsensitiveMap>();
			
			// 1. folder 정보를 가져온다.
			folderList = folderDao.folderIdsList();
			
			// 2. map에 담는다. [key]folder_id, [value]parent_id :: 개인문서함 제외
			for(CaseInsensitiveMap caseMap : folderList) {
				folderIDs.put(caseMap.get("FOLDER_ID").toString(), caseMap.get("PARENT_ID").toString());
				folderNames.put(caseMap.get("FOLDER_ID").toString(), caseMap.get("FOLDER_NAME_KO").toString());
			}
			
			switch (cacheKey) {
				case Constant.EHCACHE_CACHE_KEY_FOLDERIDS:result = folderIDs;break;
				case Constant.EHCACHE_CACHE_KEY_FOLDERNAMES:result = folderNames;break;
				default:break;
			}
			
		}

		return result;
	}
	
	
	
	@Override
	public Object getCache(String cacheName, String cacheKey) throws Exception {
		Object result = null;
		
		// 1. cache factory를 가져온다
		Cache cache = cachemanager.getCache(cacheName);
		
		// 2. cache factory에 저장된 cache를 가져온다
		Element element = cache.get(cacheKey);
		
		// 3. cache가 미존재하면 생성 한다.
		if(element == null) {
			logger.debug("["+cacheName+"]["+cacheKey+"][getCache()] : 메모리에 캐쉬 미존재 신규 생성");
			result = getData(cacheName, cacheKey);
			cache.put(new Element(cacheKey, result));
		} else {
			logger.debug("["+cacheName+"]["+cacheKey+"][getCache()] : 메모리에 캐쉬 존재");
			result = element.getObjectValue();
		}
		
		return result;
	}

	@Override
	public void replaceCache(String cacheName, String cacheKey, Object obj) throws Exception {
		// 1. cache factory를 가져온다
		Cache cache = cachemanager.getCache(cacheName);
		
		// 2. cache factory에 저장된 cache를 가져온다
		Element element = cache.get(cacheKey);
		
		// 3. cache가 미존재하면 생성 한다.
		if(element == null) {
			logger.debug("["+cacheName+"]["+cacheKey+"][replaceCache] : 메모리에 캐쉬가 존재하지 않습니다. 미존재 신규 생성");
			cache.put(new Element(cacheKey, obj));
		} else {
			logger.debug("["+cacheName+"]["+cacheKey+"][replaceCache] : 메모리에 캐쉬를 변경 합니다.");
			// element를 변경할 수 있는 방법이 없음 cache의 replace 사용 안함
			cache.removeElement(element);
			cache.put(new Element(cacheKey, obj));			
		}
	}
	
	@Override
	public boolean menuAuthByFolderID(String folder_id, String group_id) throws Exception {
		
		@SuppressWarnings("unchecked")
		Map<String, String> srcFolderMap = (HashMap<String, String>)getCache(Constant.EHCACHE_CACHE_NAME_FOLDERLIST, Constant.EHCACHE_CACHE_KEY_FOLDERIDS);
		
		folder_id = !StringUtil.isEmpty(folder_id) ? folder_id : "";
		group_id = !StringUtil.isEmpty(group_id) ? group_id : "";
		
		String group_folder_id = group_id.replace(Constant.ID_PREFIX_GROUP, Constant.ID_PREFIX_FOLDER);
		boolean isAuth = false;
		
		if(group_folder_id.equals(folder_id))
			return true;
		
		int loopCnt = 0;
		while(!StringUtil.isEmpty(folder_id) && !folder_id.equals(Constant.FOLDER_TOP_ID)){
			if(srcFolderMap.containsKey(folder_id)){
				if(group_folder_id.equals(folder_id))
					return true;
				
				folder_id = srcFolderMap.get(folder_id);
			} else {
				return false;
			}
			
			//무한 loop 방지 :: 부서 depth 최대한 10000개
			loopCnt++;
			if( loopCnt == 10000)
				break;
		}
		
		return isAuth;
	}
	
	@Override
	public String getFolderFullpathNameByFolderId(String folder_id,	boolean isFristSlash) throws Exception {
		Map<String, String> objFoldeIds = (HashMap<String, String>)getCache(Constant.EHCACHE_CACHE_NAME_FOLDERLIST, Constant.EHCACHE_CACHE_KEY_FOLDERIDS);
		Map<String, String> objFolderName = (HashMap<String, String>)getCache(Constant.EHCACHE_CACHE_NAME_FOLDERLIST, Constant.EHCACHE_CACHE_KEY_FOLDERNAMES);

		String fullPathName = "";
		String tempFolderId = folder_id;
		
		int loopCnt = 0;
		while(objFolderName.containsKey(tempFolderId)){
			fullPathName = objFolderName.get(tempFolderId)+"/"+fullPathName;
			tempFolderId = objFoldeIds.get(tempFolderId); //부모폴더ID
			
			//무한 loop 방지 :: 부서 depth 최대한 1000개
			loopCnt++;
			if( loopCnt == 1000)
				break;
			
			if(tempFolderId.equals(objFoldeIds.get(tempFolderId)))
				break;
		}
		
		return isFristSlash ? "/"+fullPathName : fullPathName;
		
	}
	
}
