package kr.co.exsoft.eframework.util;

import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.PasswordAuthentication;
import javax.mail.MessagingException;
import kr.co.exsoft.eframework.util.ConfigData;
/**
 * 메일발송 클래스
 *
 * @author 패키지팀
 * @since 2014. 9. 30.
 * @version 1.0
 * 
 */

public class MailSendUtil {

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 대량감사정책 메일발송처리
	 * 2. 처리내용 : GMAIL RELAY
	 * </pre>
	 * @Method Name : sendAuditMail
	 * @param subject
	 * @param receiverAddress
	 * @param messageText
	 * @throws Exception void
	 */
	public static void sendAuditMail(String subject,List<String> receiverAddress,String messageText) 
			throws Exception {

		Properties props = new Properties();
		
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.host", ConfigData.getString("REPORT_MAIL_SERVER_NAME"));
		props.put("mail.smtp.port", ConfigData.getString("REPORT_MAIL_SERVER_PORT"));
		props.put("mail.smtp.auth", "true");
		
		Session session = Session.getInstance(props,new javax.mail.Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			      return new PasswordAuthentication(ConfigData.getString("REPORT_MAIL_SENDER_ADDRESS"),ConfigData.getString("REPORT_MAIL_SENDER_PWD"));
			    }
			  });
		 
		try {
		
			// 수신인 목록 세팅.
			InternetAddress _receiverAddress[] = new InternetAddress[receiverAddress.size()];
			
			for (int i = 0; i < receiverAddress.size(); i++) {
				_receiverAddress[i] = new InternetAddress(receiverAddress.get(i));
			}
			
			// 메시지 세팅.
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(ConfigData.getString("REPORT_MAIL_SENDER_ADDRESS"),ConfigData.getString("REPORT_MAIL_SENDER_NAME")));
			message.setRecipients(Message.RecipientType.TO, _receiverAddress);
			message.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B")); 
			message.setContent(messageText.toString(), "text/html; charset=UTF-8");
	
			Transport.send(message);

		}catch (MessagingException e) {
		  throw new RuntimeException(e);
		}

	}
	
	/**
	 * <pre>
	 * 1. 개용 : 내문서 - 작업카트 - URL메일발송 (참조, 숨은참조 추가)
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : sendURLMail
	 * @param subject
	 * @param receiverAddress
	 * @param ccAddress
	 * @param hccAddress
	 * @param messageText
	 * @throws Exception void
	 */
	public static void sendURLMail(String subject,List<String> receiverAddress,List<String> ccAddress,List<String> hccAddress,String messageText) 
			throws Exception {

		Properties props = new Properties();
		
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.host", ConfigData.getString("REPORT_MAIL_SERVER_NAME"));
		props.put("mail.smtp.port", ConfigData.getString("REPORT_MAIL_SERVER_PORT"));
		props.put("mail.smtp.auth", "true");
		
		Session session = Session.getInstance(props,new javax.mail.Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			      return new PasswordAuthentication(ConfigData.getString("REPORT_MAIL_SENDER_ADDRESS"),ConfigData.getString("REPORT_MAIL_SENDER_PWD"));
			    }
			  });
		 
		try {
		
			// 수신인 목록 세팅.
			InternetAddress _receiverAddress[] = new InternetAddress[receiverAddress.size()];
			
			for (int i = 0; i < receiverAddress.size(); i++) {
				_receiverAddress[i] = new InternetAddress(receiverAddress.get(i));
			}
			
			// 참조 목록 세팅.
			InternetAddress _ccAddress[] = new InternetAddress[ccAddress.size()];
			
			for (int i = 0; i < ccAddress.size(); i++) {
				_ccAddress[i] = new InternetAddress(ccAddress.get(i));
			}
			
			// 숨은참조 목록 세팅.
			InternetAddress _hccAddress[] = new InternetAddress[hccAddress.size()];
			
			for (int i = 0; i < hccAddress.size(); i++) {
				_hccAddress[i] = new InternetAddress(hccAddress.get(i));
			}
			
			// 메시지 세팅.
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(ConfigData.getString("REPORT_MAIL_SENDER_ADDRESS"),ConfigData.getString("REPORT_MAIL_SENDER_NAME")));
			message.setRecipients(Message.RecipientType.TO, _receiverAddress);
			message.setRecipients(Message.RecipientType.CC, _ccAddress);
			message.setRecipients(Message.RecipientType.BCC, _hccAddress);
			message.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B")); 
			message.setContent(messageText.toString(), "text/html; charset=UTF-8");
	
			Transport.send(message);

		}catch (MessagingException e) {
		  throw new RuntimeException(e);
		}

	}
}
