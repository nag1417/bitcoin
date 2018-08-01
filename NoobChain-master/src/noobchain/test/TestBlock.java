package noobchain.test;

import java.security.MessageDigest;
import java.util.Date;

import org.bouncycastle.crypto.io.MacInputStream;

public class TestBlock {

	public String hash;
	public String previousHash;
	private String data; //our data will be a simple message.
	private long timeStamp; //as number of milliseconds since 1/1/1970.

	//Block Constructor.
	public TestBlock(String data,String previousHash ) {
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
	
	}
	public static void main(String[] args) {
		String res=applySha256("USerInputs");
		System.out.println("results:   "+res);
		//results:   294c5a106a8ffd6c3e7defda8d0fa533d7cfe698d629ac3d02edc9250dc98d5a
	}
	public static String applySha256(String input){
		
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        
			//Applies sha256 to our input, 
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
	        
			StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
