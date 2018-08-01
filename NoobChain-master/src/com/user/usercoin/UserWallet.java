package com.user.usercoin;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserWallet {
	
	public PrivateKey privateKey;
	public PublicKey publicKey;
	
	public HashMap<String,UserOutputTransaction> UTXOs = new HashMap<String,UserOutputTransaction>();
	
	public UserWallet() {
		generateKeyPair();
	}
		
	public void generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random); //256 
	        KeyPair keyPair = keyGen.generateKeyPair();
	        // Set the public and private keys from the keyPair
	        privateKey = keyPair.getPrivate();
	        publicKey = keyPair.getPublic();
	        
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public float getBalance() {
		float total = 0;	
        for (Map.Entry<String, UserOutputTransaction> item: UserCoin.UTXOs.entrySet()){
        	UserOutputTransaction UTXO = item.getValue();
            if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
            	UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
            	total += UTXO.value ; 
            }
        }  
		return total;
	}
	
	public UserTransaction sendFunds(PublicKey _recipient,float value ) {
		if(getBalance() < value) {
			System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}
		ArrayList<UserInputTransaction> inputs = new ArrayList<UserInputTransaction>();
		
		float total = 0;
		for (Map.Entry<String, UserOutputTransaction> item: UTXOs.entrySet()){
			UserOutputTransaction UTXO = item.getValue();
			total += UTXO.value;
			inputs.add(new UserInputTransaction(UTXO.id));
			if(total > value) break;
		}
		
		UserTransaction newTransaction = new UserTransaction(publicKey, _recipient , value, inputs);
		newTransaction.generateSignature(privateKey);
		
		for(UserInputTransaction input: inputs){
			UTXOs.remove(input.transactionOutputId);
		}
		
		return newTransaction;
	}
	
}

