package kr.co.exsoft.eframework.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.net.URL;
import java.io.File;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.StringTokenizer;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.lang.StringUtils;
import java.net.URLDecoder;

/***
 * 라이센스 관련 클래스
 * @author 패키지 개발팀
 * @since 2014.07.21
 * @version 3.0
 *
 */
public class LicenseUtil {
	
	
	public static void main(String[] args) {
			
		String ret = "";
		
		if(args.length == 1) {
			
			// 복호화
			ret = licenseDecrpt(args[0]);
			System.out.println("라이센스 복호화="+ret);
			
		}else if(args.length == 2) {
			
			// 암호화
			// LICENSE_TYPE = NAMED - 문서 등록자 수 / CONCURRENT - 세션 접속자 수
			// USER_COUNT = 0 - 무한 / 그외는 사용자수 이하
			ret = licenseEncrypt(args[0],Integer.parseInt(args[1]));
			System.out.println("라이센스 암호화="+ret);
			
		}else {
			System.out.println("Usage: LicenseUtil licenseType userCount");
		}
				
		
	}
	
	/**
	 * EDMS 라이센스 암호화
	 * @param licenseType
	 * @param userCount
	 * @return String
	 */
	public static String licenseEncrypt(String licenseType,int userCount) {
		
		String licenseKey = "";
		
		try {
			licenseKey = generateLicenseKey(licenseType, userCount);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return licenseKey;
	}
	
	/***
	 * EDMS 라이센스 복호화
	 * @param licenseKey
	 * @return String
	 */
	public static String licenseDecrpt(String licenseKey)	{
		
		String license = "";
		
		try {
			license = LicenseUtil.decipherLicenseKey(licenseKey);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return license;
	}
	
	/**
	 * 라이센스 키 생성.
	 * 
	 * @param licenseType
	 * @param userCount
	 * @return String
	 */
	public static String generateLicenseKey(String licenseType, int userCount) {
		
		String ksPass = "loveboat";
		String keyPass = "loveboat";
		String alias = "ab942e0f-9e4a-44b9-9f82-0a5f5d48ba12";
		String ret = null;
		
		try {
			// 인증서 정보 설정 
			URL url = ClassLoader.getSystemResource("kr/co/exsoft/eframework/cert/exsoft.pfx");
	    	FileInputStream certfis = new FileInputStream(new File(url.getFile()));
			
			// Private Key 생성.
			BufferedInputStream ksbufin = new BufferedInputStream(certfis);

			KeyStore ks = KeyStore.getInstance("PKCS12");
			ks.load(ksbufin, ksPass.toCharArray());
					
			PrivateKey key = (PrivateKey) ks.getKey(alias, keyPass.toCharArray());
			
			// 라이센스 키 생성.
			ret = spell("EDMsl|" + licenseType + "|" + userCount + "|", key);		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 라이센스 키 복호화 APPLICATION
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : decipherLicenseKey
	 * @param licenseKey
	 * @return String
	 * @throws Exception
	 */
	public static String decipherLicenseKey(String licenseKey) throws Exception {

		String ret = null;
		
		if (StringUtils.isNotBlank(licenseKey)) {
	    	
			// 인증서 정보 설정 및 public key 생성
			URL url = ClassLoader.getSystemResource("kr/co/exsoft/eframework/cert/exsoft.cer");
	    	FileInputStream certfis = new FileInputStream(new File(url.getFile()));
	    	
	    	CertificateFactory cf = CertificateFactory.getInstance("X.509");
	    	Certificate cert = cf.generateCertificate(certfis);
	    	
	    	PublicKey key = cert.getPublicKey();
	
	    	// 라이센스 키 복호화.
	    	ret = unspell(licenseKey, key);
		}
		
		return ret;
	}
	
	/**
	 * 
	 * <pre> 
	 * 1. 개요 :  라이센스 키 복호화 WAS
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : decipherLicenseKeyWeb
	 * @param licenseKey
	 * @return String
	 * @throws Exception
	 */
	public static String decipherLicenseKeyWeb(String licenseKey) throws Exception {

		String ret = null;
		
		if (StringUtils.isNotBlank(licenseKey)) {

			// URLDecoder - 경로에 공백이 있는 경우 처리
			String certName = URLDecoder.decode(LicenseUtil.class.getResource("/").getPath() + "kr/co/exsoft/eframework/cert/exsoft.cer","utf-8");	
			
			// 특정 WAS 경로 문제처리
			if(certName.indexOf("WEB-INF/") != -1) {				
				certName = certName.replace("WEB-INF","WEB-INF/");
			}
			
			FileInputStream certfis = new FileInputStream(certName);
			
	    	CertificateFactory cf = CertificateFactory.getInstance("X.509");
	    	Certificate cert = cf.generateCertificate(certfis);
	    	
	    	PublicKey key = cert.getPublicKey();
	
	    	// 라이센스 키 복호화.
	    	ret = unspell(licenseKey, key);
		}
		
		return ret;
	}
	
	/**
	 * 암호화.
	 * 
	 * @param word
	 * @param key
	 * @return String
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 */
	private static String spell(String word, Key key) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		
		byte[] cleartext = word.getBytes();
		byte[] ciphertext = cipher.doFinal(cleartext);
		
		return getString(ciphertext);		
	}

	/**
	 * 복호화.
	 * 
	 * @param word
	 * @param key
	 * @return String
	 */
	private static String unspell(String word, Key key) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, key);

		byte[] ciphertext = getBytes(word);
		byte[] cleartext = cipher.doFinal(ciphertext);
		
		return new String(cleartext);		
	}

	private static String getString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			sb.append((int) (0x00FF & b));
			if (i + 1 < bytes.length) {
				sb.append("-");
			}
		}
		
		return sb.toString();
	}

	private static byte[] getBytes(String str) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		StringTokenizer st = new StringTokenizer(str, "-", false);
		
		while (st.hasMoreTokens()) {
			int i = Integer.parseInt(st.nextToken());
			bos.write((byte) i);
		}
		
		return bos.toByteArray();
	}
	
}
