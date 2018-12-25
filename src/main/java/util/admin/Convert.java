package util.admin;

import java.security.*;

public class Convert {

	public Convert() {

	
	}
	
	public static String process(String test){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
//			String test = "111111";

			md.update(test.getBytes());
			byte[] digest = md.digest();
			
			System.out.println("digest: " + digest);
			String s = convert(digest);
			System.out.println("s: " + s);
			return s;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	

	static String convert(byte[] digest){
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < digest.length; ++i)
		{
		    byte b = digest[i];
		    int value = (b & 0x7F) + (b < 0 ? 128 : 0);
		    buffer.append(value < 16 ? "0" : "");
		    buffer.append(Integer.toHexString(value));
		}
		System.out.println("buffer.toString(): " + buffer.toString());
		return buffer.toString();
	}

	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		EnDe_Cypher en = new EnDe_Cypher();
//		en.process("111111");
//	}

}