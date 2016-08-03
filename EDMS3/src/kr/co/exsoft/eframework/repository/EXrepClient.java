package kr.co.exsoft.eframework.repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.exsoft.cs.client.IxClient;
import com.exsoft.net.SizedInputStream;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.eframework.exception.ExrepClientException;

/***
 * eXrep api 클래스
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public class EXrepClient {

	protected static final Log logger = LogFactory.getLog(EXrepClient.class);
	
	protected IxClient client;
	protected boolean isConnected;
	protected String location;
	
	// eXrep info - config.properties
	protected String ip = ConfigData.getString("EXREP_IP");
	protected String port = ConfigData.getString("EXREP_PORT");
	protected String timeout = ConfigData.getString("EXREP_TIMEOUT");
	protected String buffer_size =ConfigData.getString("EXREP_BUFFER_SIZE");
	protected String user_id = ConfigData.getString("EXREP_USER_ID");
	protected String user_password = ConfigData.getString("EXREP_USER_PASSWORD");
	protected String service = ConfigData.getString("EXREP_SERVICE");
	
	/**
	 * EXrepClient 생성자.
	 */
	public EXrepClient() {
		client = new IxClient();
	}
	
    public boolean isConnected() throws ExrepClientException {
        return isConnected;
    }

    /**
     *  eXrep C/S 접속 및 API Client 얻기.
     * @return IxClient
     * @throws ExrepClientException
     */
    public IxClient connect() throws ExrepClientException {
    	
    	logger.info("EXREP CLIENT START");
    	
    	try {
    		
    		// 이미 연결 되어 있을 시.
	    	if (isConnected) {
	    		logger.debug("eXrep C/S already connected.");	    		
	    		return client;
	    	}
	    	
        	// Connect.
            if (!client.isConnected()) {
	            client.setBufferSize(Integer.parseInt(buffer_size));
	            client.connect(ip, Integer.parseInt(port), Integer.parseInt(timeout));
	            logger.debug("eXrep C/S connected.");
            }
            
            // 로그인.
            if (client.login(user_id, user_password)) {
            	logger.debug("eXrep C/S logged in.");
            } else {
            	// 실패 시.
		        client.disconnect();
		        logger.debug("eXrep C/S disconnected.");
		        throw new Exception(client.getErrorMessage());
            }

            isConnected = true;
    		
    	} catch (Exception e) {
			isConnected = false;
			throw new ExrepClientException(e);
		} finally {
			logger.info("Return Value = " + client);
    	}
    	    	
    	return client;
    }
    
    /**
     *  eXrep C/S 접속 해제.
     * @throws ExrepClientException
     */
    public void disconnect() throws ExrepClientException {
		
		boolean ret = false;
		
		try {
	    	// 연결 되어 있을 시.
	    	if (isConnected) {
		    	// Disconnect.
		        client.disconnect();
		        logger.debug("eXrep C/S disconnected.");
	    	}
	        
	        isConnected = false;
	        
	        ret = true;

		} catch (Exception e) {
			logger.error(CommonUtil.getPrintStackTrace(e));
			throw new ExrepClientException(e);
		} finally {
			logger.info("Return Value = " + ret);
    	}
    }
    
    /**
     * eXrep 파일 등록
     * @param localFile
     * @param volumeName
     * @param savePath
     * @param overwrite
     * @return boolean
     * @throws ExrepClientException
     */
    public boolean putFile(String localFile, String volumeName, String savePath, boolean overwrite) throws ExrepClientException {

		boolean ret = false;
		
		try {
			
			// 폴더 경로.
			String folderPath = StringUtil.getFolderPath(savePath);
			
			// 폴더가 존재하지 않을 시.
			if (!client.isExists(service, volumeName, folderPath)) {
				if (client.createFolder(service, volumeName, folderPath)) {
					logger.debug("Folder is created.");
				} else {
					throw new Exception("Failed to create folder.");
				}
			}
			
			ret = client.putFile(localFile, service, volumeName, savePath, overwrite);
			
		} catch (Exception e) {
			logger.error(CommonUtil.getPrintStackTrace(e));
			throw new ExrepClientException(e);
		} finally {
			logger.info("Return Value = " + ret);
    	}
		
		return ret;
    }
    
    /**
     * * eXrep 파일 등록
     * @param inputStream
     * @param volumeName
     * @param savePath
     * @param overwrite
     * @return boolean
     * @throws ExrepClientException
     */
    public boolean putFile(SizedInputStream inputStream, String volumeName, String savePath, boolean overwrite) throws ExrepClientException {

		boolean ret = false;
		
		try {

			// 폴더 경로.
			String folderPath = StringUtil.getFolderPath(savePath);
			
			// 폴더가 존재하지 않을 시.
			if (!client.isExists(service, volumeName, folderPath)) {
				if (client.createFolder(service, volumeName, folderPath)) {
					logger.debug("Folder is created : " + volumeName + ":" + folderPath);
				} else {
					throw new Exception("Failed to create folder.");
				}
			}
			
			ret = client.putFile(inputStream, service, volumeName, savePath, overwrite);
			
		} catch (Exception e) {
			logger.error(CommonUtil.getPrintStackTrace(e));
			throw new ExrepClientException(e);
		} finally {
			logger.info("Return Value = " + ret);
    	}
		
		return ret;
    }
    
    /**
     * 파일 얻기.
     * @param volumeName
     * @param filePath
     * @return SizedInputStream
     * @throws ExrepClientException
     */
    public SizedInputStream getFile(String volumeName, String filePath) throws ExrepClientException {

		SizedInputStream ret = null;
		
		try {
			
			ret = client.getFile(service, volumeName, filePath);
			
		} catch (Exception e) {
			logger.error(CommonUtil.getPrintStackTrace(e));
			throw new ExrepClientException(e);
		} finally {
			logger.info("Return Value = " + ret);
    	}
		
		return ret;
    }
    
    /**
     *  파일 얻기.
     * @param volumeName
     * @param filePath
     * @param savePath
     * @return boolean
     * @throws ExrepClientException
     */
    public boolean getFile(String volumeName, String filePath, String savePath) throws ExrepClientException {

		boolean ret = false;
		
		try {

			ret = client.getFile(service, volumeName, filePath, savePath);
			
		} catch (Exception e) {
			logger.error(CommonUtil.getPrintStackTrace(e));
			throw new ExrepClientException(e);
		} finally {
			logger.info("Return Value = " + ret);
    	}
		
		return ret;
    }
    
    /***
     * 파일 복사.
     * @param volumeName
     * @param srcPath
     * @param savePath
     * @param overwrite
     * @return boolean
     * @throws ExrepClientException
     */
    public boolean copyFile(String volumeName, String srcPath, String savePath, boolean overwrite) throws ExrepClientException {

		boolean ret = false;
		
		try {
			
			// 폴더 경로.
			String folderPath = StringUtil.getFolderPath(savePath);
			
			// 폴더가 존재하지 않을 시.
			if (!client.isExists(service, volumeName, folderPath)) {
				if (client.createFolder(service, volumeName, folderPath)) {
					logger.debug("Folder is created : " + volumeName + ":" + folderPath);
				} else {
					throw new Exception("Failed to create folder.");
				}
			}
			
			ret = client.copyFile(service, volumeName, srcPath, savePath, overwrite);
			
		} catch (Exception e) {
			logger.error(CommonUtil.getPrintStackTrace(e));
			throw new ExrepClientException(e);
		} finally {
			logger.info("Return Value = " + ret);
    	}
		
		return ret;
    }
    
    /**
     * 파일 삭제.
     * @param volumeName
     * @param filePath
     * @return boolean
     * @throws ExrepClientException
     */
    public boolean removeFile(String volumeName, String filePath) throws ExrepClientException {

		boolean ret = false;
		
		try {

			ret = client.removeFile(service, volumeName, filePath);
			
		} catch (Exception e) {
			logger.error(CommonUtil.getPrintStackTrace(e));
			throw new ExrepClientException(e);
		} finally {
			logger.info("Return Value = " + ret);
    	}
		
		return ret;
    }
    
    /**
     * 존재 여부 확인.
     * @param volumeName
     * @param path
     * @return boolean
     * @throws ExrepClientException
     */
    public boolean isExists(String volumeName, String path) throws ExrepClientException {

		boolean ret = false;
		
		try {

			ret = client.isExists(service, volumeName, path);
			
		} catch (Exception e) {
			logger.error(CommonUtil.getPrintStackTrace(e));
			throw new ExrepClientException(e);
		} finally {
			logger.info("Return Value = " + ret);
    	}
		
		return ret;
    }
    
}
