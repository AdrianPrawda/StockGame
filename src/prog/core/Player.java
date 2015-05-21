package prog.core;

import java.util.ArrayList;
import java.util.LinkedList;

public class Player {

	private final String NAME;
	private final CashAccount ACCOUNT;
	private final ShareDepositAccount SHARE_DEPOSIT_ACCOUNT;
	private PlayerAgent agent;
	private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	
	public Player(String name){
		NAME = name;
		ACCOUNT = new CashAccount(name);
		SHARE_DEPOSIT_ACCOUNT = new ShareDepositAccount(name);
		agent = null;
	}
	
	public String getName(){
		return NAME;
	}
	
	public CashAccount getCashAccount(){
		return ACCOUNT;
	}
	
	public ShareDepositAccount getShareDepositAccount(){
		return SHARE_DEPOSIT_ACCOUNT;
	}
	
	public PlayerAgent getPlayerAgent(){
		return agent;
	}
	
	
	//Set new player agent
	public void setPlayerAgent(PlayerAgent playerAgent){
		//if a player agent already exists, dismiss it
		if(agent != null && agent.isTrading() == true){
			agent.dismiss();
		}
		agent = playerAgent;
	}
	
	public long value(){
		long o = ACCOUNT.value() + SHARE_DEPOSIT_ACCOUNT.value();
		return o;
	}
	
	public String toString(){
		String out = "[" + NAME + "]: \nCash Account: " + ACCOUNT.toString() + "\n Share Deposit: " + SHARE_DEPOSIT_ACCOUNT.toString() + "\n Player value:" + value();
		return out;
	}
	
	public void addTransaction(Transaction.Type type, long money, Share share, int amount, long time)
	{
		this.transactions.add(new Transaction(type, money, share, amount, time));
	}
	
	public ArrayList<Transaction> getTransactions()
	{
		return this.transactions;
	}

	
}
