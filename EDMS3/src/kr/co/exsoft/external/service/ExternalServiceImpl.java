package kr.co.exsoft.external.service;

import java.util.HashMap;
import java.util.List;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import kr.co.exsoft.eframework.library.ExsoftAbstractServiceImpl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import kr.co.exsoft.external.dao.ExternalDao;


/***
 * External 서비스 구현 부분 - 외부 DB 미사용시 @Autowired @Qualifier 주석처리하세요.
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Service("externalService")
public class ExternalServiceImpl extends ExsoftAbstractServiceImpl implements ExternalService {

	@Autowired
	@Qualifier("sqlSessionImp")
	private SqlSession sqlSessionImp;
	
	@Autowired
	@Qualifier("sqlSession")
	private SqlSession sqlSession;
	

	@Override
	public CaseInsensitiveMap externalUserDetail(HashMap<String,Object> map) throws Exception {
		
		ExternalDao externalDao = sqlSessionImp.getMapper(ExternalDao.class);
		
		CaseInsensitiveMap ret = new CaseInsensitiveMap();
		
		ret = externalDao.externalUserDetail(map);
	
		if (ret == null)	
			throw processException("result.nodata.msg");
		
		return ret;
	}
	
	@Override
	public int externalUserWrite(HashMap<String,Object> map) throws Exception  {
		
		int ret = 0;
		
		ExternalDao externalDao = sqlSessionImp.getMapper(ExternalDao.class);
		
		ret = externalDao.externalUserWrite(map);
		
		return ret;
	}
	
	
	@Override
	public int externalUserWriteTx(HashMap<String,Object> map) throws Exception {
		
		int ret = 0;
		
		ExternalDao externalDao = sqlSessionImp.getMapper(ExternalDao.class);
		
		ret = externalDao.externalUserWrite(map);
		
		if(ret != 1)
			throw processException("result.insert.fail");
		
		ret = externalDao.externalGroupedWrite(map);
		
		if(ret != 1)
			throw processException("result.insert.fail");
		
		return ret;
	}
	
	@Override
	public void batchUserWrite(List<HashMap<String,Object>> userList) throws Exception {

		ExternalDao externalDao = sqlSessionImp.getMapper(ExternalDao.class);
		
		for(HashMap<String,Object> map : userList) {
			
			externalDao.externalGroupedWrite(map);			
		}

	}
		
}
