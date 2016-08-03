package kr.co.exsoft.eframework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

/***
 * ARIA 처리 클래스
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public class ARIAUtil {

	/**
	 * 암호화 처리
	 * @param str
	 * @param privateKey
	 * @return String
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 */
	public static String ariaEncrypt(String str, String privateKey) 
	throws InvalidKeyException, UnsupportedEncodingException {
		if (str==null || str.equals("")) return "";
		
		byte[] p;
		byte[] c;
		ARIAEngine instance = new ARIAEngine(256, privateKey);
		p = new byte[str.getBytes().length];
		p = str.getBytes();
		
		int len = str.getBytes().length;
		if ((len % 16) != 0) {
			len = (len / 16 + 1) * 16;
		}
		c = new byte[len];
		System.arraycopy(p, 0, c, 0, p.length);
		instance.encrypt(p, c, p.length);
		
		return byteArrayToHex(c).toUpperCase();
	}
	
	/**
	 * 파일 암호화 처리
	 * @param privateKey
	 * @param src
	 * @param dest
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 */
	public static void ariaFileEncrypt(String privateKey, String src, String dest) 
	throws IOException, InvalidKeyException, UnsupportedEncodingException {
		File f = new File(src); 
		FileInputStream fis = new FileInputStream(src);
		  
		long length = f.length();
  		byte[] b = new byte[(int)length];
		  
  		try {
			int offset = 0;
			int numRead = 0;
			while (offset < b.length && (numRead=fis.read(b, offset, b.length-offset)) >= 0) {
				offset += numRead;
			}
			if (offset < b.length) {
				throw new IOException(f.getName());
			}
  		} finally{
  			fis.close(); 
  		}
        
  		ARIAEngine instance = new ARIAEngine(256, privateKey);
        int len = b.length;
		if ((len % 16) != 0) {
			len = (len / 16 + 1) * 16;
		}
		byte[] c = new byte[len];
		System.arraycopy(b, 0, c, 0, b.length);
		instance.encrypt(b, c, b.length);
        FileOutputStream fos = new FileOutputStream(dest);
        try {
        	fos.write(c);
        } catch (IOException e) {
        	
        } finally {
        	try {
				fos.close();
            } catch (IOException e) {}
        }
	}

	/**
	 * 복호화 처리
	 * @param strHex
	 * @param privateKey
	 * @return String
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 */
	public static String ariaDecrypt(String strHex, String privateKey) 
	throws InvalidKeyException, UnsupportedEncodingException  {
		if (strHex==null || strHex.equals("")) return "";
		
		byte[] p;
		byte[] c;
		ARIAEngine instance = new ARIAEngine(256, privateKey);
		
		c = hexToByteArray(strHex);
		p = new byte[c.length];
		instance.decrypt(c, p, p.length);
		
		StringBuffer buf = new StringBuffer();
		buf.append(new String(p));
		
		return buf.toString().trim();
	}

	/**
	 * 파일 복호화 처리
	 * @param privateKey
	 * @param src
	 * @param dest
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 */
	public static void ariaFileDecrypt(String privateKey, String src, String dest) 
	throws IOException, InvalidKeyException, UnsupportedEncodingException {
		File f = new File(src); 
		FileInputStream fis = new FileInputStream(src);
		  
		long length = f.length();
  		byte[] b = new byte[(int)length];
		  
  		try {
			int offset = 0;
			int numRead = 0;
			while (offset < b.length && (numRead=fis.read(b, offset, b.length-offset)) >= 0) {
				offset += numRead;
			}
			if (offset < b.length) {
				throw new IOException(f.getName());
			}
  		} finally{
  			fis.close(); 
  		}
        
        ARIAEngine instance = new ARIAEngine(256, privateKey);
        int len = b.length;
		if ((len % 16) != 0) {
			len = (len / 16 + 1) * 16;
		}
		byte[] c = new byte[len];
		System.arraycopy(b, 0, c, 0, b.length);
		instance.decrypt(b, c, b.length);
		
		FileOutputStream fos = new FileOutputStream(dest); 
		try {
	        fos.write(c); 
		} catch (IOException e) {
			
		} finally {
			try {
				fos.close();
            } catch (IOException e) {}
		}
	}

	
	// hex to byte[] 
	public static byte[] hexToByteArray(String hex) { 
	    if (hex == null || hex.length() == 0) { 
	        return null; 
	    } 

	    byte[] ba = new byte[hex.length() / 2]; 
	    for (int i = 0; i < ba.length; i++) { 
	        ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16); 
	    } 
	    return ba; 
	} 

	// byte[] to hex 
	public static String byteArrayToHex(byte[] ba) { 
	    if (ba == null || ba.length == 0) { 
	        return null; 
	    } 

	    StringBuffer sb = new StringBuffer(ba.length * 2); 
	    String hexNumber; 
	    for (int x = 0; x < ba.length; x++) { 
	        hexNumber = "0" + Integer.toHexString(0xff & ba[x]); 

	        sb.append(hexNumber.substring(hexNumber.length() - 2)); 
	    } 
	    return sb.toString(); 
	}
	
}