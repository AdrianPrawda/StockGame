package prog.core;
public class Player {

	private final String NAME;
	private final CashAccount ACCOUNT;
	private final ShareDepositAccount SHARE_DEPOSIT_ACCOUNT;
	private PlayerAgent agent;
	
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
	
	public void setPlayerAgent(PlayerAgent playerAgent){
		if(agent != null){
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
}
