package kr.co.exsoft.quartz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.exsoft.document.dao.PageDao;
import kr.co.exsoft.document.vo.PageVO;
import kr.co.exsoft.eframework.library.ExsoftAbstractServiceImpl;
import kr.co.exsoft.eframework.repository.EXrepClient;
import kr.co.exsoft.quartz.dao.QuartzDao;
import kr.co.exsoft.quartz.vo.BatchWorkVO;
import kr.co.exsoft.quartz.vo.FileQueueDeleteVO;
import kr.co.exsoft.statistics.dao.AuditDao;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 배치프로그램 서비스 구현 부분
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Service("quartzService")
public class QuartzServiceImpl extends ExsoftAbstractServiceImpl implements QuartzService {

	@Autowired
	@Qualifier("sqlSession")
	private SqlSession sqlSession;
	
	@Autowired
	@Qualifier("sqlSessionBatch")
	private SqlSession sqlSessionBatch;
	
	@Override
	public int batchWorkWrite(long work_idx,String work_type,String work_nm) throws Exception {
		
		int ret = 0;
		
		QuartzDao quartzDao = sqlSession.getMapper(QuartzDao.class);
		
		BatchWorkVO batchworkVO = setBatchWork(work_idx,work_type,work_nm);
		
		ret = quartzDao.batchWorkWrite(batchworkVO);
				
		return ret;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 배치작업로그 객체 생성하기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setBatchWork
	 * @param work_type
	 * @param work_nm
	 * @return BatchWorkVO
	 */
	public BatchWorkVO setBatchWork(long work_idx,String work_type,String work_nm) {
		
		BatchWorkVO vo = new BatchWorkVO();
		
		vo.setWork_idx(work_idx);
		vo.setWork_nm(work_nm);
		vo.setWork_type(work_type);
		
		return vo;
	}
	
	@Override
	public int batchWorkUpdate(HashMap<String,Object> map) throws Exception {
		
		int ret = 0;
		
		QuartzDao quartzDao = sqlSession.getMapper(QuartzDao.class);
		
		ret = quartzDao.batchWorkUpdate(map);
				
		return ret;
	}
	

	@Override
	public boolean isBatchWork(HashMap<String,Object> map) throws Exception {
		
		
		QuartzDao quartzDao = sqlSession.getMapper(QuartzDao.class);
		
		int result = quartzDao.isBatchWork(map);
		
		if(result > 0)	{
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public List<HashMap<String,Object>> auditExceedList(HashMap<String,Object> map) throws Exception {
		
		List<HashMap<String,Object>> ret = new ArrayList<HashMap<String,Object>>();
		List<CaseInsensitiveMap> auditList = new ArrayList<CaseInsensitiveMap>();
		
		QuartzDao quartzDao = sqlSession.getMapper(QuartzDao.class);
		auditList = quartzDao.auditExceedList(map);
		
		if(auditList.size() > 0)	{
			
			for(CaseInsensitiveMap caseMap : auditList) {
				
				HashMap<String,Object> auditMap = new HashMap<String,Object>();
				auditMap.put("user_id",caseMap.get("user_id"));
				auditMap.put("user_name",caseMap.get("user_name"));
				auditMap.put("read_count",caseMap.get("read_count"));
				auditMap.put("group_name",caseMap.get("group_name"));
				auditMap.put("group_id",caseMap.get("group_id"));
				
				ret.add(auditMap);
			}
			
		}
				
		return ret;
	}
	
	@Override
	public int writeAudit(HashMap<String,Object> map) throws Exception  {
		
		int ret = 0;
		
		AuditDao auditDao =  sqlSession.getMapper(AuditDao.class);
		
		ret = auditDao.writeAudit(map);
		
		return ret;
	}
	
	@Override
	public List<HashMap<String, Object>> batchDocList(HashMap<String,Object> map) throws Exception {
	
		List<HashMap<String,Object>> ret = new ArrayList<HashMap<String,Object>>();
		List<CaseInsensitiveMap> docList = new ArrayList<CaseInsensitiveMap>();

		QuartzDao quartzDao = sqlSession.getMapper(QuartzDao.class);
		
		String workType = map.get("workType") != null ? map.get("workType").toString() : "";
		
		// 0. 업무구분 정보가 없을 경우 에러처리
		if(workType.equals("")) {
			throw processException("common.system.error");	
		}
		
		// 1.만기문서:expired 개인휴지통:privateTrash 시스템휴지통:systemTrash
		if(workType.equals("expired")) {
			docList = quartzDao.expiredDocList(map);
		}else if(workType.equals("privateTrash")) {
			docList = quartzDao.privateTrashDocList(map);
		}else if(workType.equals("systemTrash")) {
			docList = quartzDao.systemTrashDocList(map);
		}
		
		// 2.문서목록이 있는 경우 수행처리
		if(docList.size() > 0)	{
			
			for(CaseInsensitiveMap caseMap : docList) {
				
				HashMap<String,Object> expiredMap = new HashMap<String,Object>();
				expiredMap.put("doc_id",caseMap.get("doc_id"));
				expiredMap.put("root_id",caseMap.get("root_id") != null ? caseMap.get("root_id") : "");
				expiredMap.put("is_locked",caseMap.get("is_locked"));
				expiredMap.put("type_id",caseMap.get("doc_type"));
			
				ret.add(expiredMap);
			}

		}
		
		return ret;
	}
	
	@Override
	public HashMap<String,Object> systemUserInfo(HashMap<String,Object> map) throws Exception {
		
		QuartzDao quartzDao = sqlSession.getMapper(QuartzDao.class);
		CaseInsensitiveMap userInfo = new CaseInsensitiveMap();
		HashMap<String,Object> ret = new HashMap<String,Object>();		
		
		// 1.SYSTEM_OPERATOR 정보 조회 :: 반드시 1계정만 존재한다.
		userInfo = quartzDao.systemUserInfo(map);
		
		// 2.리턴값 재정의 CaseInsensitiveMap => HashMap 변경처리
		ret.put("user_id", userInfo.get("user_id"));
		ret.put("user_name",userInfo.get("user_name"));
		ret.put("group_name", userInfo.get("group_name"));
		ret.put("group_id",userInfo.get("group_id"));
		
		return ret;
	}

	@Override
	public void docStatusProc(HashMap<String,Object> map) throws Exception {
		
		QuartzDao quartzDao = sqlSessionBatch.getMapper(QuartzDao.class);
		
		List<CaseInsensitiveMap> userDocList = new ArrayList<CaseInsensitiveMap>();
		List<CaseInsensitiveMap> groupDocList = new ArrayList<CaseInsensitiveMap>();
		
		// 1. 사용자별 문서현황 집계 정보 가져오기
		userDocList = quartzDao.userDocStatus(map);
		
		if(userDocList != null && userDocList.size() > 0) {
			
			for(CaseInsensitiveMap caseMap : userDocList) {
				
				// XR_DOCUMENT_USER_HT 객체값 설정
				HashMap<String,Object> status = new HashMap<String,Object>();					
				status.put("udate", caseMap.get("action_date"));
				status.put("user_id", caseMap.get("actor_id"));
				status.put("type_id", caseMap.get("type_id"));
				status.put("user_nm", caseMap.get("actor_nm"));
				status.put("group_id", caseMap.get("group_id"));
				status.put("group_nm", caseMap.get("group_nm"));
				status.put("create_cnt", caseMap.get("create_cnt"));
				status.put("read_cnt", caseMap.get("read_cnt"));
				status.put("update_cnt", caseMap.get("update_cnt"));
				status.put("delete_cnt", caseMap.get("delete_cnt"));
				
				// XR_DOCUMENT_USER_HT  등록처리
				quartzDao.userDocHtWrite(status);
			}

		}
		
		// 2. 부서별 문서현황 집계 정보 가져오기
		groupDocList = quartzDao.groupDocStatus(map);
		if(groupDocList != null && groupDocList.size() > 0) {
			
			for(CaseInsensitiveMap caseMap : groupDocList) {
				
				// XR_DOCUMENT_GROUP_HT 객체값 설정
				HashMap<String,Object> status = new HashMap<String,Object>();					
				status.put("gdate", caseMap.get("action_date"));
				status.put("group_id", caseMap.get("group_id"));
				status.put("type_id", caseMap.get("type_id"));
				status.put("create_cnt", caseMap.get("create_cnt"));
				status.put("read_cnt", caseMap.get("read_cnt"));
				status.put("update_cnt", caseMap.get("update_cnt"));
				status.put("delete_cnt", caseMap.get("delete_cnt"));
				
				// XR_DOCUMENT_GROUP_HT  등록처리
				quartzDao.groupDocHtWrite(status);
			}

		}

	}
	
	@Override
	public List<PageVO> delPageList(HashMap<String,Object> map) throws Exception {
		
		List<PageVO> ret = new ArrayList<PageVO>();
		
		QuartzDao quartzDao = sqlSession.getMapper(QuartzDao.class);
		
		ret = quartzDao.delPageList(map);
		
		return ret;
	}
	
	
	//FileQueue 리스트가져오기
	public List<FileQueueDeleteVO> fileQueueDeleteList(HashMap<String,Object> map) throws Exception {
		
		List<FileQueueDeleteVO> ret = new ArrayList<FileQueueDeleteVO>();
		
		QuartzDao quartzDao = sqlSession.getMapper(QuartzDao.class);
		
		ret = quartzDao.fileQueueDeleteList(map);
		
		return ret;
	}
	
	
	@Override
	public HashMap<String,Object> delPageProc(List<PageVO> pageList,EXrepClient eXrepClient ) throws Exception	{
	
		PageDao pageDao = sqlSessionBatch.getMapper(PageDao.class);
				
		HashMap<String,Object> result = new HashMap<String,Object>();

		long successCnt = 0;
		long totalSize = 0;
		
		// 삭제대상 반복처리
		for(PageVO pageVO : pageList) {
		
			HashMap<String,Object> param = new HashMap<String,Object>();
			param.put("page_id",pageVO.getPage_id());
			param.put("is_deleted",pageVO.getIs_deleted());
			
			pageDao.xrPageDelete(param);
			
			// eXrep 파일 삭제처리 :: 이미 삭제된 문서의 파일로 별도 백업처리하지 않는다.
			if(eXrepClient.isExists(pageVO.getVolume_id(),pageVO.getContent_path())) {
					
				if(eXrepClient.removeFile(pageVO.getVolume_id(), pageVO.getContent_path())) {
					successCnt++;
					totalSize = totalSize + pageVO.getPage_size();
				}				
			}									
			
			// NEXT PAGE Proc				
		}
		
		// 처리 결과 저장
		result.put("successCnt",successCnt);
		result.put("totalSize",totalSize);
		
				
		return result;
		
	}
	
	// FileQueue 삭제
	@Override
	public HashMap<String,Object> delFileProc(List<FileQueueDeleteVO> pageList,EXrepClient eXrepClient ) throws Exception	{
	
		QuartzDao quartzDao = sqlSessionBatch.getMapper(QuartzDao.class);
				
		HashMap<String,Object> result = new HashMap<String,Object>();	

		long successCnt = 0;
		//long totalSize = 0;
		
		// 삭제대상 반복처리
		for(FileQueueDeleteVO filequeuedeleteVO : pageList) {
		
			HashMap<String,Object> param = new HashMap<String,Object>();
			param.put("content_path",filequeuedeleteVO.getContent_path());
			
			quartzDao.deleteQueue(param);
			
			// eXrep 파일 삭제처리 :: 이미 삭제된 문서의 파일로 별도 백업처리하지 않는다.
			if(eXrepClient.isExists(filequeuedeleteVO.getVolume_id(),filequeuedeleteVO.getContent_path())) {
					
				if(eXrepClient.removeFile(filequeuedeleteVO.getVolume_id(), filequeuedeleteVO.getContent_path())) {
					successCnt++;
					//totalSize = totalSize + filequeuedeleteVO.getPage_size();
				}				
			}									
			
			// NEXT PAGE Proc				
		}
		
		// 처리 결과 저장
		result.put("successCnt",successCnt);
		//result.put("totalSize",totalSize);
		System.out.println("=======성공카운트====="+successCnt);
		//System.out.println("======토탈사이즈====="+totalSize);
		
		
				
		return result;
		
	}

	@Override
	public List<HashMap<String,Object>> tempDelDocList(HashMap<String,Object> map) throws Exception {
		
		List<HashMap<String,Object>> ret = new ArrayList<HashMap<String,Object>>();
		List<CaseInsensitiveMap> delDocList = new ArrayList<CaseInsensitiveMap>();
		
		QuartzDao quartzDao = sqlSessionBatch.getMapper(QuartzDao.class);
		
		delDocList = quartzDao.tempDelDocList(map);

		if(delDocList.size() > 0)	{
			
			for(CaseInsensitiveMap caseMap : delDocList) {
				
				HashMap<String,Object> delMap = new HashMap<String,Object>();
				delMap.put("doc_id",caseMap.get("doc_id"));
				delMap.put("root_id",caseMap.get("root_id") != null ? caseMap.get("root_id") : "");
				delMap.put("user_id",caseMap.get("user_id"));
				delMap.put("work_date",caseMap.get("work_date"));
			
				ret.add(delMap);
			}

		}
		
		return ret;
	}
		
	@Override
	public void tempDocDelete(HashMap<String,Object> map) throws Exception{
		
		QuartzDao quartzDao = sqlSessionBatch.getMapper(QuartzDao.class);
		quartzDao.tempDocDelete(map);		
		
	}
}
