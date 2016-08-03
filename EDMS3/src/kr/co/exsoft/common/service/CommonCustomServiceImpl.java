package kr.co.exsoft.common.service;

import java.util.HashMap;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * 메뉴/코드/세션 서비스 사이트 커스터마이징 구현 Sample
 * @author 패키지 개발팀
 * @since 2014.08.01
 * @version 3.0
 *
 */
@Service("commonCustomService")
public class CommonCustomServiceImpl extends CommonServiceImpl {
	
	@Autowired
	@Qualifier("sqlSession")
	private SqlSession sqlSession;
	
	@Override
	public CaseInsensitiveMap editorDetailInfo(HashMap<String,Object> map) throws Exception {
		
		CaseInsensitiveMap ret = new CaseInsensitiveMap();
		
		//CommonDao commonDao = sqlSession.getMapper(CommonDao.class);		
		//ret = commonDao.editorDetailInfo(map);
		
		ret.put("title","사이트커스터마이징");
		ret.put("create_dt","2014-08-01 14:00:05");
		ret.put("content","ttttttttttttttttttttttttttttttttttttttttt<br>ddddddddddddddddddd<br>");
		
		return ret;
	}

}
