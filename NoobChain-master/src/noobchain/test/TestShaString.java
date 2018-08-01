package noobchain.test;

import java.security.MessageDigest;
import java.util.ArrayList;

import com.google.gson.GsonBuilder;

import noobchain.test.TestBlock;

public class TestShaString {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<TestBlock> blockchain = new ArrayList<TestBlock>(); 
		blockchain.add(new TestBlock("Hi im the first block", "0"));		
		blockchain.add(new TestBlock("Yo im the second block",blockchain.get(blockchain.size()-1).hash)); 
		blockchain.add(new TestBlock("Hey im the third block",blockchain.get(blockchain.size()-1).hash));
		
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);		
		System.out.println(blockchainJson);
String results= applySha256("");
System.out.println("applySha256:  "+results);

String target = new String(new char[92]).replace('\0', '0');
System.out.println("Target Difficulty:  "+target+"  \n -------->");
	}
public static String applySha256(String input){
		
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        
			//Applies sha256 to our input, 
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
	        System.out.println("Input bytes"+hash);
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
