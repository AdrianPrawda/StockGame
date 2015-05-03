//NEEDS TO BE REVIEWED, NEEDS INTERFACE

package prog.core;

import java.util.TimerTask;

import prog.core.provider.StockPriceProvider;
import prog.exception.FundsExceededException;
import prog.exception.NotEnoughSharesException;
import prog.interfaces.AccountManager;
import prog.exception.ObjectNotFoundException;

public class PlayerAgent{
	private GlobalTimer timer = GlobalTimer.getInstance();
	private TimerTask task;
	private StockPriceProvider provider;
	private long delay;
	private long period;
	private long[] tendency;
	private Share[] snapshot;
	private long minTradeDiff;
	private Player player;
	private AccountManager manager;
	
	public PlayerAgent(AccountManager manager, Player player, StockPriceProvider provider, long minTradeDiff, long delay, long period){
		this.provider = provider;
		this.delay = delay;
		this.period = period;
		snapshot = provider.getAllSharesAsSnapshot();
		tendency = new long[snapshot.length];
		this.minTradeDiff = minTradeDiff;
		this.player = player;
		this.manager = manager;
	}
	
	public PlayerAgent(AccountManager manager, Player player, StockPriceProvider provider, long minTradeDiff){
		this(manager, player, provider, minTradeDiff, 0, 2000);
	}
	
	public void startTrading(){
		task = new TimerTask(){
			public void run(){
				try {
					trade();
				} catch (NotEnoughSharesException e) {
					//Log
				} catch (FundsExceededException e) {
					//Log
				}
			}
		};
		
		timer.scheduleAtFixedRate(task, delay, period);
	}
	
	//Dismiss player agent
	public void dismiss(){
		//Don't purge because of performance issues
		if(task != null){
			task.cancel();
			task = null;
		}else{
			throw new ObjectNotFoundException("Agent can't be dismissed if it hasn't been started yet!");
//			throw new NullPointerException("TimerTask can't be canceled if it hasn't been sheduled yet!");
		}
	}
	
	public boolean isTrading(){
		if(task == null){
			return false;
		}
		return true;
	}
	
	private void trade() throws NotEnoughSharesException, FundsExceededException{
		//Get updated share rate
		Share[] temp = provider.getAllSharesAsSnapshot();
		int buy = -1;
		int sell = -1;
		
		for(int i=0; i<snapshot.length; i++){
			tendency[i] += temp[i].getPrice() - snapshot[i].getPrice();
			if(tendency[i] <= (minTradeDiff*-1)){
				//Check if selling value is the best
				if(buy >= 0 && tendency[i] < tendency[sell] && player.getShareDepositAccount().numberOfShares(snapshot[i].getName()) > 0){
					sell = i;
				}else if(sell > 0){
					sell = i;
				}
			}else if(tendency[i] >= minTradeDiff){
				//Check if buying value is the best
				if(buy >= 0 && tendency[i] > tendency[buy]){
					buy = i;
				}else if(buy > 0){
					buy = i;
				}
			}
		}
		
		if(buy >= 0){
			buyShares(buy, player.getCashAccount().value()/2);
		}
		if(sell >= 0){
			sellShares(sell, player.getShareDepositAccount().numberOfShares(snapshot[sell].getName()));
		}
		
	}
	
	private void sellShares(int i, int maxShares) throws NotEnoughSharesException{
		manager.sellShare(player.getName(), snapshot[i].getName(), maxShares);
		tendency[i] = 0;
	}
	
	private void buyShares(int i, long maxMoney) throws FundsExceededException{
		int quantity = (int)(maxMoney / snapshot[i].getPrice());
		manager.buyShares(player.getName(), snapshot[i].getName(), quantity);
		tendency[i] = 0;
	}

}
