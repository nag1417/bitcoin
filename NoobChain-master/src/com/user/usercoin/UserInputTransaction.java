package com.user.usercoin;

public class UserInputTransaction {
	public String transactionOutputId; //Reference to TransactionOutputs -> transactionId
	public UserOutputTransaction UTXO; //Contains the Unspent transaction output
	
	public UserInputTransaction(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}
}
