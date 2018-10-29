package ncsu.tinyurl.project;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import ncsu.tinyurl.project.UrlMap.AppInstance;

public class Encrypt {

	AppInstance ins;
	
	public Encrypt(AppInstance instance) {
		ins = instance;
	}
	
	public String encryptUrl(String url) {
		//implement a hashMap to return tinyurl for an input here
		String tinyUrl = generateSHA1(url);
		ins.addUrl(url, tinyUrl);
		return tinyUrl;
	}
	
	public static String generateSHA1(String message) {
		return hashString(message,"SHA-1");
	}
	
	private static String hashString(String message, String algorithm) {
 
            MessageDigest digest = null;
				try {
					digest = MessageDigest.getInstance(algorithm);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            byte[] hashedBytes = null;
				try {
					hashedBytes = digest.digest(message.getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            return convertByteArrayToHexString(hashedBytes);
 
    }
	
	 private static String convertByteArrayToHexString(byte[] arrayBytes) {
	        StringBuffer stringBuffer = new StringBuffer();
	        for (int i = 0; i < arrayBytes.length; i++) {
	            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
	                    .substring(1));
	        }
	        return stringBuffer.toString();
	    }
	
}
