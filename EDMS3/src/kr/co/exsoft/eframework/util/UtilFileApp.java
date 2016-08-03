package kr.co.exsoft.eframework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/***
 * 파일처리 관련 클래스
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public class UtilFileApp {
	
	protected static final Log logger = LogFactory.getLog(UtilFileApp.class);
    
    public UtilFileApp() {
    	
    }
    
    /***
     * 새로운 디렉토리 생성
     * @param dir
     * @throws IOException
     * @throws SecurityException
     */
    public static void createDir(String dir) throws IOException,SecurityException {
        checkSecure(dir);
        File f = new File(dir);
        if (!f.exists()) {
            if (f.mkdirs() != true) {
                throw new IOException();
            }
        }
    }
    
    /**
     * 상위디렉토리 전부를 생성하고 자신을 생성한다.
     * @param dir
     * @throws IOException
     * @throws SecurityException
     */
    public static void createDirAllPath(String dir) throws IOException,SecurityException {
    	
        if (dir.length() > 2) {
            
        	checkSecure(dir);
            
            File f = new File(dir);
            //상위가 존재하지 않는경우
            if (!f.exists()) {  
                //다시 상위를 체크한다.
                createDirAllPath(dir.substring(0, dir.lastIndexOf(File.separator)));
                if (f.mkdirs() != true) {
                    throw new IOException();
                }
                
            }
        }
    }
    
    /**
     * 디렉토리 및 파일 삭제
     * @param dir
     * @throws IOException
     * @throws SecurityException
     */
    public static void deleteDir(String dir) throws IOException,
    SecurityException {
        if (isWinOS())
            deleteDirByFile(dir);
        else
            deleteDirByRuntime(dir);
    }
    
    
    /**
     * 디렉토리 및 파일 삭제 - recursion을 적용하여 디렉토리 및 디렉토리내 파일과 디렉토리를 모두 삭제
     * @param dir
     * @throws IOException
     * @throws SecurityException
     */
    public static void deleteDirByFile(String dir) throws IOException,
    SecurityException {
        checkSecure(dir);
        File f = new File(dir);
        if (f.exists()) {
            String[] file = f.list();
            File df;
            for (int i = 0; i < file.length; i++) {
                df = new File(dir + File.separator + file[i]);
                if (df.isDirectory()) {
                    deleteDirByFile(dir + File.separator + file[i]);
                } else {              
                	try {
                		df.delete();
                	}catch(Exception e) {                		
                		logger.error(e.getMessage());
                	}
                }
            }

            if (f.delete() != true) {
                throw new IOException();
            }

        }
    }
        
    /**
     * 디렉토리 및 파일 삭제 runtime으로 처리
     * @param dir
     * @throws IOException
     */
    public static void deleteDirByRuntime(String dir) throws IOException {
        checkSecure(dir);
        try {
            String[] cmd = new String[3];
            cmd[0] = "rm";
            cmd[1] = "-rf";
            cmd[2] = dir;
            execRunTime(cmd);
        } catch (Exception e) {
            deleteDirByFile(dir);
        }
    }
    
    /***
     * 파일 삭제 - 디렉토리내 파일 모두 삭제
     * @param dir
     * @throws IOException
     * @throws SecurityException
     */
    public static void deletAllFile(String dir) throws IOException,
    SecurityException {
        checkSecure(dir);
        File f = new File(dir);
        if (f.exists()) {
            String[] file = f.list();
            File df;
            for (int i = 0; i < file.length; i++) {
                df = new File(dir + File.separator + file[i]);
                df.delete();
            }
        }
    }
    

   /**
    * 파일 삭제 
    * @param filename
    * @throws IOException
    * @throws SecurityException
    */
    public static void deletFile(String filename) throws IOException,
    SecurityException {
        checkSecure(filename);
        File f = new File(filename);
        if (f.exists() && f.isFile())
            if (!f.delete())
                throw new IOException();
    }

    /**
     * 파일 삭제
     * @param files
     * @throws IOException
     * @throws SecurityException
     */
    public static void deletFile(String[] files) throws IOException,
    SecurityException {
        if (files != null)
            for (int i = 0; i < files.length; i++) {
                checkSecure(files[i]);
                deletFile(files[i]);
            }
    }
    
    /**
     * 파일 용량 체크
     * @param filename
     * @return long
     * @throws IOException
     */
    public static long getFileSize(String filename) throws IOException {
        File f = new File(filename);
        return f.length();
    }
    
    /**
     * 디렉토리내 전체 파일 용량 체크 SunOS or Linux일 경우 runtime으로 처리
     * @param dir
     * @return long
     * @throws IOException
     */
    public static long getDirectorySize(String dir) throws IOException {
        long filesize = 0;
        if (isWinOS())
            filesize = getDirectorySizeByFile(dir);
        else
            filesize = getDirectorySizeByRuntime(dir);
        return filesize;
    }

    /**
     * 디렉토리내 전체 파일 용량 체크 : recursion을 적용하여 디렉토리 및 디렉토리내 파일 전체 용량 체크
     * @param dir
     * @return long
     * @throws IOException
     */
    public static long getDirectorySizeByFile(String dir) throws IOException {
        long filesize = 0;
        java.io.File filepath = new java.io.File(dir);
        if (filepath.isDirectory()) {
            File files[] = filepath.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory())
                    filesize += getDirectorySizeByFile(files[i]
                                                             .getAbsolutePath());
                else
                    filesize += getFileSize(files[i].getAbsolutePath());
            }
        } else {
            filesize = getFileSize(filepath.getAbsolutePath());
        }
        return filesize;
    }
    
    /***
     * 디렉토리내 전체 파일 용량 체크 runtime으로 처리
     * @param dir
     * @return long
     * @throws IOException
     */
    public static long getDirectorySizeByRuntime(String dir) throws IOException {
        long filesize = 0;
        try {
            String[] cmd = new String[3];
            cmd[0] = "du";
            cmd[1] = "-sk";
            cmd[2] = dir;
            String s = execRunTime(cmd);
            StringBuffer buf = new StringBuffer();
            char[] c = s.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (c[i] == '\t')
                    break;
                else
                    buf.append(c[i]);
            }
            filesize = Long.parseLong(buf.toString()) * 1024;
        } catch (Exception e) {
            filesize = getDirectorySizeByFile(dir);
        }
        return filesize;
    }
    
    /**
     * 파일 용량 체크
     * @param files
     * @return long
     * @throws IOException
     * @throws SecurityException
     */
    public static long getFileSize(String[] files) throws IOException,
    SecurityException {
        long lFileSize = 0;
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                lFileSize += getFileSize(files[i]);
            }
        }
        return lFileSize;
    }

    /**
     * 디렉토리내 파일을 배열로 반환
     * @param dir
     * @return File
     * @throws IOException
     * @throws SecurityException
     */
    public static File[] getAllFile(String dir) throws IOException,
    SecurityException {
        checkSecure(dir);
        java.io.File filepath = new java.io.File(dir);
        File[] files = filepath.listFiles();
        return files;
    }
    
    /**
     * 디렉토리내 파일 존재여부 확인 
     * @param dir
     * @return boolean
     * @throws IOException
     * @throws SecurityException
     */
    public static boolean isExistFile(String dir) throws IOException,
    SecurityException {
        java.io.File filepath = new java.io.File(dir);
        File[] files = filepath.listFiles();
        if (files != null && files.length > 0)
            return true;
        else
            return false;
    }

    /**
     * 파일배열을 받아 파일명을 배열로 반환
     * @param files
     * @return String
     * @throws IOException
     * @throws SecurityException
     */
    @SuppressWarnings("null")
	public static String[] getAllFileName(File[] files) throws IOException,
    SecurityException {
        String[] filename = null;
        for (int i = 0; i < files.length; i++) {
            checkSecure(files[i].getName());
            filename[i] = files[i].getName();
        }
        return filename;
    }
    
    /**
     * 디렉토리를 받아 절대파일명을 배열로 반환
     * @param dir
     * @return String
     * @throws IOException
     * @throws SecurityException
     */
    public static String[] getAllFullFileName(String dir) throws IOException,
    SecurityException {
        checkSecure(dir);
        java.io.File filepath = new java.io.File(dir);
        String[] filename = filepath.list();
        for (int i = 0; i < filename.length; i++) {
            filename[i] = dir + File.separator + filename[i];
        }
        return filename;
    }
    
    /**
     * 파일 다운로드
     * @param res
     * @param strFileName
     * @param strDownFileName
     * @throws IOException
     */
    public static void downloadFile(HttpServletResponse res,
            String strFileName, String strDownFileName) throws IOException {
    	
        checkSecure(strFileName);
        OutputStream out = null;
        FileInputStream in = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {

            strDownFileName = removeToken(strDownFileName);

            out = res.getOutputStream();
            in = new FileInputStream(new File(strFileName));
            
            res.setHeader("Content-Disposition", "attachment; filename="+ CharConversion.K2E(strDownFileName) + ";");
            
            String fileType[] = UtilFileApp.getFileType(strDownFileName.substring(strDownFileName.lastIndexOf('.') + 1));
            res.setContentType(fileType[2]);
            
            fis = new FileInputStream(strFileName);
            bis = new BufferedInputStream(fis);
            out = res.getOutputStream();
            byte buffer[] = new byte[2048];
            int bytesRead = 0;
            
            while ((bytesRead = bis.read(buffer)) != -1)
                out.write(buffer, 0, bytesRead);
            
            out.flush();
            
        } finally {
        	
        	 try {in.close();} catch (Exception e) {}
        	 try {out.close();} catch (Exception e) {}
        	 try {fis.close();} catch (Exception e) {}
        	 try {bis.close();} catch (Exception e) {}
          
        }
    }
    
    
    /**
     * 파일다운로드
     * @param res
     * @param strFileName
     * @param strDownFileName
     * @throws IOException
     */
    public static void showFile(HttpServletResponse res,
            String strFileName, String strDownFileName) throws IOException {
    	
        checkSecure(strFileName);
        OutputStream out = null;
        FileInputStream in = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {

            strDownFileName = removeToken(strDownFileName);

            out = res.getOutputStream();
            in = new FileInputStream(new File(strFileName));
            
            res.setHeader("Content-Disposition", "filename="+ CharConversion.K2E(strDownFileName) + ";");
            
            String fileType[] = UtilFileApp.getFileType(strDownFileName.substring(strDownFileName.lastIndexOf('.') + 1));
            res.setContentType(fileType[2]);
            
            fis = new FileInputStream(strFileName);
            bis = new BufferedInputStream(fis);
            out = res.getOutputStream();
            byte buffer[] = new byte[2048];
            int bytesRead = 0;
            
            while ((bytesRead = bis.read(buffer)) != -1)
                out.write(buffer, 0, bytesRead);
            
            out.flush();
            
        } finally {
        	try {in.close();} catch (Exception e) {}
       	 	try {out.close();} catch (Exception e) {}
       	 	try {fis.close();} catch (Exception e) {}
       	 	try {bis.close();} catch (Exception e) {}
        }
    }
    
    /**
     * 웹부라우저 정보 확인
     * @param request
     * @return String
     */
    private static String getUserAgent(HttpServletRequest request) {
    	String header = request.getHeader("User-Agent");
    	if(header.indexOf("MSIE") > -1) {
    		return "MSIE";
    	} else if(header.indexOf("Chrome") > -1) {
    		return "Chrome";
    	} else if(header.indexOf("Opera") > -1) {
    		return "Opera";
    	} else if(header.indexOf("Safari") > -1) {
    		return "Safari";
    	}
    	return "Firefox";
    }
    

    /***
     * 웹부라우저 Dispostion 정보
     * @param filename
     * @param browser
     * @return String
     * @throws Exception
     */
    private static String getDisposition(String filename, String browser) throws Exception {
    	String dispositionPrefix = "attachment;filename=";
    	String encodedFilename = null;
    	if(browser.equals("MSIE")) {
    		encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
    	} else if(browser.equals("Firefox")) {
    		encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "ISO-8859-1") + "\"";
    	} else if(browser.equals("Opera")) {
    		encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "ISO-8859-1") + "\"";
    	} else if(browser.equals("Safari")) {
    		encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "ISO-8859-1") + "\"";    		
    	} else if(browser.equals("Chrome")) {
    		StringBuffer sb = new StringBuffer();
    		for(int idx = 0; idx < filename.length(); idx++) {
    			char c = filename.charAt(idx);
    			if(c > '~') {
    				sb.append(URLEncoder.encode("" + c, "UTF-8"));
    			} else {
    				sb.append(c);
    			}
    		}
    		encodedFilename = sb.toString();
    	} else {
    		throw new RuntimeException("Not supported browser");
    	}
    	return dispositionPrefix + encodedFilename;
    }
    
    /**
     * 해당 입력 스트림으로부터 오는 데이터를 다운로드 한다.
     * @param request
     * @param response
     * @param saveFilename 저장된 파일명 경로(파일명 포함)
     * @param realFilename 저장할 원 파일명
     * @throws ServletException
     * @throws IOException
     */
    public static void download(HttpServletRequest request, HttpServletResponse response,
    	String saveFilename, String realFilename) throws ServletException, IOException {
    	
    	File saveFile = new File(saveFilename);

    	if (saveFile == null || !saveFile.exists() || saveFile.length() <= 0 || saveFile.isDirectory()) {
    		throw new IOException("파일 객체가 Null 혹은 존재하지 않거나 길이가 0, 혹은 파일이 아닌 디렉토리이다.");
    	}
    	realFilename = removeToken(realFilename);
    	String mimetype = request.getSession().getServletContext().getMimeType(saveFile.getName());
    	InputStream is = null;
    	String mime = null;
    	try {
    		is = new FileInputStream(saveFile);
    		
    		if (mimetype == null || mimetype.length() == 0) {
        		mime = "application/octet-stream;";
        	}
    		byte[] buffer = new byte[2048];

        	response.setContentType(mime + "; charset=utf-8");
        	
        	String browser = getUserAgent(request);
        	try {
				response.setHeader("Content-Disposition", getDisposition(realFilename, browser));
			} catch (Exception e) {
				e.printStackTrace();
			}
        	// 파일 사이즈가 정확하지 않을때는 아예 지정하지 않는다.
        	if (saveFile.length() > 0) {
        		response.setHeader("Content-Length", "" + saveFile.length());
        	}

        	BufferedInputStream fin = null;
        	BufferedOutputStream outs = null;

        	try {
        		fin = new BufferedInputStream(is);
        		outs = new BufferedOutputStream(response.getOutputStream());
        		int read = 0;

        		while ((read = fin.read(buffer)) != -1) {
        			outs.write(buffer, 0, read);
        		}
        	} finally {
        		try {
        			outs.close();
        		} catch (Exception ex1) {
        		}

        		try {
        			fin.close();
        		} catch (Exception ex2) {	
        		}
        	} // end of try/catch
    	} finally {
    		try {
    			is.close();
    		} catch (Exception ex) {
    		}
    	}	
    }

    
    /**
     * 파일 존재 유무 확인
     * @param filename
     * @return boolean
     * @throws IOException
     */
    public static boolean isExists(String filename) throws IOException {
        File f = new File(filename);
        return f.exists();
    }

    /**
     * 파일명 변경
     * @param strFilename
     * @param strNewFileName
     * @return boolean
     * @throws IOException
     */
    public static boolean renameTo(String strFilename, String strNewFileName)
    throws IOException {
        checkSecure(strFilename);
        File f = new File(strFilename);
        File nf = new File(strNewFileName);
        return f.renameTo(nf);
    }
    
    /**
     * 파일 복사 
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void copyfile(String src, String dest) throws IOException {
        checkSecure(src);
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            File inputFile = new File(src);
            if (inputFile.isFile()) {
                File outputFile = new File(dest);
                if (outputFile.exists()) {
                    outputFile = new File(dest + "_1");
                }
                in = new FileInputStream(inputFile);
                out = new FileOutputStream(outputFile);
                
                int bytesRead = 0;
                byte b[] = new byte[1024 * 2];
                while ((bytesRead = in.read(b)) != -1)
                    out.write(b, 0, bytesRead);
                out.flush();
            }
        }catch (Exception e) {
        	e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception e) {
            }
            try {
                if (out != null)
                    out.close();
            } catch (Exception e) {
            }
        }
    }
        
    /**
     * 파일 최근 변경시간 가져오기
     * @param filename
     * @return long
     * @throws IOException
     */
    public static long getLastModified(String filename) throws IOException {
        File f = new File(filename);
        return f.lastModified();
    }
    
    /**
     * 파일 여부 확인
     * @param filename
     * @return boolean
     * @throws IOException
     */
    public static boolean isFile(String filename) throws IOException {
        File f = new File(filename);
        return f.isFile();
    }
    
    /**
     * 디렉토리 여부확인
     * @param filename
     * @return boolean
     * @throws IOException
     */
    public static boolean isDirectory(String filename) throws IOException {
        File f = new File(filename);
        return f.isDirectory();
    }

    /***
     * 파일확장자에 따른 File Icon Image
     * @param strExtension
     * @return String
     */
    public static String[] getFileType(String strExtension) {
        if (strExtension != null)
            strExtension = strExtension.toLowerCase();
        String[] arrayFileType = { "", "", "" };
        if (strExtension == null) {
            arrayFileType[0] = "Unknown";
            arrayFileType[1] = "file.png";
            arrayFileType[2] = "application/octet-stream";
        } else if (strExtension.equals("")) {
            arrayFileType[0] = "Unknown";
            arrayFileType[1] = "file.png";
            arrayFileType[2] = "application/octet-stream";
        } else if (strExtension.equals("jpg") || strExtension.equals("jpeg")
                || strExtension.equals("jpe")) {
            arrayFileType[0] = "JPEG Image";
            arrayFileType[1] = "jpg.png";
            arrayFileType[2] = "image/jpeg";
        } else if (strExtension.equals("gif")) {
            arrayFileType[0] = "JPEG Image";
            arrayFileType[1] = "gif.png";
            arrayFileType[2] = "image/png";
        } else if (strExtension.equals("doc") || strExtension.equals("docx") || strExtension.equals("rtf")) {
            arrayFileType[0] = "MS Word";
            arrayFileType[1] = "doc.png";
            arrayFileType[2] = "application/msword";
        } else if (strExtension.equals("xls") || strExtension.equals("xlsx")) {
            arrayFileType[0] = "MS Excel";
            arrayFileType[1] = "xls.png";
            arrayFileType[2] = "application/vnd.ms-excel";
        } else if (strExtension.equals("ppt") || strExtension.equals("pptx")) {
            arrayFileType[0] = "MS PowerPoint";
            arrayFileType[1] = "ppt.png";
            arrayFileType[2] = "application/vnd.ms-powerpoint";
        } else if (strExtension.equals("pds")) {
            arrayFileType[0] = "MS PowerPoint";
            arrayFileType[1] = "pds.png";
            arrayFileType[2] = "application/vnd.ms-powerpoint";
        } else if (strExtension.equals("txt")) {
            arrayFileType[0] = "텍스트 문서";
            arrayFileType[1] = "txt.png";
            arrayFileType[2] = "text/plain";
        } else if (strExtension.equals("hwp")) {
            arrayFileType[0] = "한글워드문서";
            arrayFileType[1] = "hwp.png";
            arrayFileType[2] = "application/octet-stream";
        } else if (strExtension.equals("html") || strExtension.equals("htm")) {
            arrayFileType[0] = "HTML";
            arrayFileType[1] = "html.png";
            arrayFileType[2] = "text/html";
        } else if (strExtension.equals("pdf")) {
            arrayFileType[0] = "Adobe Acrobat";
            arrayFileType[1] = "pdf.png";
            arrayFileType[2] = "application/pdf";
        } else if (strExtension.equals("bmp")) {
            arrayFileType[0] = "BMP Image";
            arrayFileType[1] = "bmp.png";
            arrayFileType[2] = "image/vnd.wap.wbmp";
        } else if (strExtension.equals("exe")) {
            arrayFileType[0] = "실행파일";
            arrayFileType[1] = "exe.png";
            arrayFileType[2] = "application/octet-stream";
        } else if (strExtension.equals("zip") || strExtension.equals("rar")
                || strExtension.equals("tar") || strExtension.equals("gzip")
                || strExtension.equals("gz")) {
            arrayFileType[0] = "압축파일";
            arrayFileType[1] = "zip.png";
            arrayFileType[2] = "application/zip";
        } else if (strExtension.equals("mp3")) {
            arrayFileType[0] = "MP3 Sound";
            arrayFileType[1] = "mp3.png";
            arrayFileType[2] = "application/octet-stream";
        } else if (strExtension.equals("eml")) {
            arrayFileType[0] = "E-mail";
            arrayFileType[1] = "eml.png";
            arrayFileType[2] = "application/octet-stream";
        } else if (strExtension.equals("pic") || strExtension.equals("pict")) {
            arrayFileType[0] = "PICT Image";
            arrayFileType[1] = "pict.png";
            arrayFileType[2] = "application/octet-stream";
        } else if (strExtension.equals("tif") || strExtension.equals("tiff")) {
            arrayFileType[0] = "TIF Image";
            arrayFileType[1] = "file.png";
            arrayFileType[2] = "image/tiff";
        } else if (strExtension.equals("avi") || strExtension.equals("mpeg")
                || strExtension.equals("mpg") || strExtension.equals("mpe")) {
            arrayFileType[0] = "TIF Image";
            arrayFileType[1] = "avi.png";
            arrayFileType[2] = "video/x-msvideo";
        } else if (strExtension.equals("reg")) {
            arrayFileType[0] = "등록항목";
            arrayFileType[1] = "reg.png";
            arrayFileType[2] = "application/octet-stream";
        } else if (strExtension.equals("ico")) {
    	    arrayFileType[0] = "PICT Image";
    	    arrayFileType[1] = "gif.png";
    	    arrayFileType[2] = "image/ico";
        } else if (strExtension.equals("png")) {
    	    arrayFileType[0] = "png Image";
    	    arrayFileType[1] = "gif.png";
    	    arrayFileType[2] = "image/png"; 
        }else if (strExtension.equals("dwg")) {
    	    arrayFileType[0] = "autocad Image";
    	    arrayFileType[1] = "dwg.png";
    	    arrayFileType[2] = "image/png"; 
        } else if (strExtension.equals("gul")) {
    	    arrayFileType[0] = "gul Image";
    	    arrayFileType[1] = "gul.png";
    	    arrayFileType[2] = "image/png"; 
        } else {
            arrayFileType[0] = "Unknown";
            arrayFileType[1] = "file.png";
            arrayFileType[2] = "application/octet-stream";
        }
        return arrayFileType;
    }
    
    /**
     * 디렉토리를 받아 디렉토리내 파일개수 반환
     * @param dir
     * @return int
     * @throws IOException
     * @throws SecurityException
     */
    public static int getNumOfFile(String dir) throws IOException,
    SecurityException {
        int nNumOfFile = 0;
        java.io.File filepath = new java.io.File(dir);
        String[] filename = filepath.list();
        if (filename != null)
            nNumOfFile = filename.length;
        return nNumOfFile;
    }

    /**
     * 디렉토리내 파일/디렉토리 삭제(longDate 이전에 생성된 파일/디렉토리 삭제)
     * @param dir
     * @param longDate
     * @throws IOException
     * @throws SecurityException
     */
    public static void deletAllFile(String dir, long longDate)
    throws IOException, SecurityException {
        checkSecure(dir);
        File f = new File(dir);
        if (dir == null || dir.equals(File.separator) || dir.equals(""))
            throw new IOException();
        if (f.exists()) {
            String[] file = f.list();
            File df;
            for (int i = 0; i < file.length; i++) {
                df = new File(dir + File.separator + file[i]);
                if (longDate >= df.lastModified()) {
                    if (df.isDirectory())
                        deleteDirByFile(dir + File.separator + file[i]);
                    else
                        df.delete();
                }
            }
        }
    }
    

    private static String execRunTime(String[] cmd) throws Exception {
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader br = new BufferedReader(new InputStreamReader(p
                .getInputStream()));
        String strValue = br.readLine();
        return strValue;
    }
    
    /**
     * Windows 와 UNIX 계열구분
     * @return boolean
     */
    private static boolean isWinOS() {
    	
    	boolean isWinOS = false;    	
    	
    	if(ConfigData.getString("OS.NAME").equals("WINDOW")) {    		
    		isWinOS = true;
    	}
        
    	return isWinOS;
    }
    
    /**
     * 디렉토리 유효성 체크
     * @param str
     * @throws IOException
     */
    public static void checkSecure(String str) throws IOException {
        if (str != null && str.indexOf("../") != -1)
            throw new IOException();
    }
    
    /**
     * Windows에서 사용하지 못하는 문자 제거(파일명 깨짐 방지를 위해)
     * @param str
     * @return String
     */
    public static String removeToken(String str) {
        return str.replaceAll("[\\/:*?<>|;]", "");
    }
    
    /**
     * 이미지 파일 여부
     * @param fileExtName
     * @return boolean
     */
    public static boolean isImage(String fileExtName){
    	String[] imageExt = {"image/gif","image/vnd.wap.wbmp","image/jpeg"};
    	for( int i=0; i< imageExt.length; i++ ){
    		if( fileExtName.toLowerCase().equals(imageExt[i]))
    			return true;
    	}
    		
    	return false;
    }
    
    /**
     * 엑셀다운로드 
     * @param res
     * @param strContent
     * @param strDownFileName
     * @throws IOException
     */
    public static void downloadContentExcel(HttpServletResponse res,
            String strContent, String strDownFileName) throws IOException {
    	
    	checkSecure(strDownFileName);
    	ServletOutputStream out = null;
	    try {
	    	strDownFileName = removeToken(strDownFileName);
	        out = res.getOutputStream();
	        res.setHeader("Content-Disposition", "attachment; filename="+ strDownFileName + ";");
	        res.setContentType("application/vnd.ms-excel");
	        out.println(CharConversion.K2E(strContent));
	    } finally {
            try {
                    out.close();
            } catch (Exception e) {
            }
    	}
    }	
}
