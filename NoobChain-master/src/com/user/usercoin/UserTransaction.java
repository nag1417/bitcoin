package com.user.usercoin;
import java.security.*;
import java.util.ArrayList;

public class UserTransaction {
	
	public String transactionId; //Contains a hash of transaction*
	public PublicKey sender; //Senders address/public key.
	public PublicKey reciepient; //Recipients address/public key.
	public float value; //Contains the amount we wish to send to the recipient.
	public byte[] signature; //This is to prevent anybody else from spending funds in our wallet.
	
	public ArrayList<UserInputTransaction> inputs = new ArrayList<UserInputTransaction>();
	public ArrayList<UserOutputTransaction> outputs = new ArrayList<UserOutputTransaction>();
	
	private static int sequence = 0; //A rough count of how many transactions have been generated 
	
	// Constructor: 
	public UserTransaction(PublicKey from, PublicKey to, float value,  ArrayList<UserInputTransaction> inputs) {
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
	}
	
	public boolean processTransaction() {
		
		if(verifySignature() == false) {
			System.out.println("#Transaction Signature failed to verify");
			return false;
		}
				
		//Gathers transaction inputs (Making sure they are unspent):
		for(UserInputTransaction i : inputs) {
			i.UTXO = UserCoin.UTXOs.get(i.transactionOutputId);
		}

		//Checks if transaction is valid:
		if(getInputsValue() < UserCoin.minimumTransaction) {
			System.out.println("Transaction Inputs too small: " + getInputsValue());
			System.out.println("Please enter the amount greater than " + UserCoin.minimumTransaction);
			return false;
		}
		
		//Generate transaction outputs:
		float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
		transactionId = calulateHash();
		outputs.add(new UserOutputTransaction( this.reciepient, value,transactionId)); //send value to recipient
		outputs.add(new UserOutputTransaction( this.sender, leftOver,transactionId)); //send the left over 'change' back to sender		
				
		//Add outputs to Unspent list
		for(UserOutputTransaction o : outputs) {
			UserCoin.UTXOs.put(o.id , o);
		}
		
		//Remove transaction inputs from UTXO lists as spent:
		for(UserInputTransaction i : inputs) {
			if(i.UTXO == null) continue; //if Transaction can't be found skip it 
			UserCoin.UTXOs.remove(i.UTXO.id);
		}
		
		return true;
	}
	
	public float getInputsValue() {
		float total = 0;
		for(UserInputTransaction i : inputs) {
			if(i.UTXO == null) continue; //if Transaction can't be found skip it, This behavior may not be optimal.
			total += i.UTXO.value;
		}
		return total;
	}
	
	public void generateSignature(PrivateKey privateKey) {
		String data = UserStringUtil.getStringFromKey(sender) + UserStringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
		signature = UserStringUtil.applyECDSASig(privateKey,data);		
	}
	
	public boolean verifySignature() {
		String data = UserStringUtil.getStringFromKey(sender) + UserStringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
		return UserStringUtil.verifyECDSASig(sender, data, signature);
	}
	
	public float getOutputsValue() {
		float total = 0;
		for(UserOutputTransaction o : outputs) {
			total += o.value;
		}
		return total;
	}
	
	private String calulateHash() {
		sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
		return UserStringUtil.applySha256(
				UserStringUtil.getStringFromKey(sender) +
				UserStringUtil.getStringFromKey(reciepient) +
				Float.toString(value) + sequence
				);
	}
}
