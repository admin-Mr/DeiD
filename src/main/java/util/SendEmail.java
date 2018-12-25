package util; 

import java.util.ArrayList;

import org.apache.commons.mail.*;

public class SendEmail {
	//private String contentText;
	
	public boolean send(String setHostNameStr, String addToStr, String setFromStr, String subjectTextStr, String contentTextStr){
		return send(setHostNameStr, 0, null, null, addToStr, setFromStr, subjectTextStr, contentTextStr);
	}
	
	public boolean send(String setHostNameStr, int smtpPort, String usernameStr, String passwordStr, ArrayList<String> addToStr, String setFromStr, String subjectTextStr, String contentTextStr){
		boolean bResult = false;
		for (int i=0; i<addToStr.size(); i++){			
			bResult = send(setHostNameStr, smtpPort, usernameStr, passwordStr, addToStr.get(i), setFromStr, subjectTextStr, contentTextStr);			
		}
		return bResult;
	}
	
	
	public boolean send(String setHostNameStr, int smtpPort,String usernameStr, String passwordStr, String addToStr, String setFromStr, String subjectTextStr, String contentTextStr){
		try{
			SimpleEmail email = new SimpleEmail();	// 構造一個mail對象
			email.setHostName(setHostNameStr);	// 設置主機名
			email.addTo(addToStr);	// 設置對象
			email.setFrom(setFromStr);	// 設置發送人
			email.setSubject(subjectTextStr);	// 設置主題
			email.setCharset("GBK");	// 設置發送使用的字符集
			//contentText = contentTextStr;
			email.setContent(contentTextStr, "text/html;charset=GBK");		// 設置內容
			if (smtpPort != 0)
				email.setSmtpPort(smtpPort);
			if (usernameStr != null && passwordStr != null){
				email.setAuthenticator(new DefaultAuthenticator(usernameStr, passwordStr));
				email.setSSL(false);							
			}
			email.send();													// 發送
			return true;
		}catch (EmailException exe) {
			System.out.println("ISSUE SendEmail send() EmailException."+exe.getMessage());
			exe.printStackTrace();
	    	return false;
		} catch (Exception ex){
			System.out.println("ISSUE SendEmail send() other Exception."+ex.getMessage());
		   	ex.printStackTrace();    	 
	    	return false;
		}
	}
}
