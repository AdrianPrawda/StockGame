//NEEDS TO BE REVIEWED, NEEDS INTERFACE

package prog.core;

import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimerTask;
import java.util.Random;

import prog.interfaces.Agent;
import prog.core.provider.StockPriceProvider;
import prog.exception.FundsExceededException;
import prog.exception.NotEnoughSharesException;
import prog.interfaces.AccountManager;
import prog.exception.ObjectNotFoundException;

public class PlayerAgent implements Agent{
	GlobalTimer timer = GlobalTimer.getInstance();
	StockPriceProvider provider;
	TimerTask task;
	Player player;
	AccountManager manager;
	
	private final long DELAY;
	private final long PERIOD;
	
	public PlayerAgent(Player player, StockPriceProvider provider, AccountManager manager){
		this.provider = provider;
		this.player = player;
		this.manager = manager;
		DELAY = 500;
		PERIOD = 1000;
	}
	
	@Override
	//Start agent
	public void startTrading() {
		//Create new task and save reference
		task = new TimerTask(){
			public void run(){
				try {
					trade();
				} catch (FundsExceededException | NotEnoughSharesException e) {
					//Bot tried to sell/buy things that he shouldn't have
					System.out.println("I did it, mum!");
				}
			}
		};
		//Schedule task
		timer.schedule(task, DELAY, PERIOD);
	}

	@Override
	//Dismiss agent
	public void dismiss() {
		//Don't purge because of performance issues
		if(task != null){
			task.cancel();
			task = null;
		}else{
			throw new ObjectNotFoundException("Agent can't be dismissed if it hasn't been started yet!");
		}
	}

	@Override
	//If agent is trading returns true
	public boolean isTrading() {
		return task != null;

	}
	
	private void trade() throws FundsExceededException, NotEnoughSharesException{
		Random rng = new Random();
		Set<Share> snapshot = provider.getAllSharesAsSnapshot();
		boolean buying = rng.nextBoolean();
		
		//Buy if true, sell if false
		if(buying){

			Share[] shares = snapshot.toArray(new Share[0]);

			Share s = shares[rng.nextInt(snapshot.size())];
			//Calculate how many shares can be bought
			int max = (int)(player.getCashAccount().value() / s.getPrice());
			//Buy shares
			if(max >= 1){
				int r = rng.nextInt(max)+1;

				manager.buyShares(player.getName(), s.getName(), r);
			}
		}else{


			ShareDepositAccount sda = player.getShareDepositAccount();
			
			//Get shares owned by the player
			//// Optimiert
			ArrayList<Share> ownedShares = sda.getAllSharesAsSnapshot();
			
			
			//Return if player doesn't own shares
			if(ownedShares.size() <= 0){
				return;
			}
			
			
			//Chose random share to sell
		
			Share s = ownedShares.get(rng.nextInt(ownedShares.size()));
			
			//Sell shares
			int q = sda.numberOfShares(s.getName());
			int r = rng.nextInt(q);
			
//			System.out.println("I sold " + s.getName() + " " + r + " times");
			manager.sellShare(player.getName(), s.getName(), r);
		}
		
		System.out.flush();
	}

}
