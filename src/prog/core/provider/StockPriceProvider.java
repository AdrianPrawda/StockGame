package prog.core.provider;

import prog.core.GlobalTimer;
import prog.core.Player;
import prog.core.Share;
import prog.interfaces.StockPriceInfo;
import prog.exception.ObjectNotFoundException;
import prog.exception.ObjectAlreadyExistsException;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.Map.Entry;

public abstract class StockPriceProvider implements StockPriceInfo {
	//Create a few shares to play around with
	SortedSet<Share> shares = new TreeSet<Share>();
	//Set a global timer
	protected GlobalTimer timer = GlobalTimer.getInstance();
	
	public StockPriceProvider(){

		startUpdate();
	}
	
	public void createShare(String shareName, long price){
		if(isShareListed(shareName)){
			throw new ObjectAlreadyExistsException();
		}
		shares.add(new Share(shareName, price));

	}
	
	//Check if a share is listed or not
	public boolean isShareListed(String shareName){
		for ( Share share : shares )
		{
			if (share.getName() == shareName)
				return true;
		}
		return false;
	}
	
	//Get the current share rate of a share
	public long getCurrentShareRate(String shareName){
		//Share doesn't exist
		if (!isShareListed(shareName))
		{
			throw new ObjectNotFoundException("Share " + shareName + " doesn't exist");
		}
		Share share = getShare(shareName);
		return share.getPrice();
		
		
		
	}
	
	//Get a copy of all shares
	public SortedSet getAllSharesAsSnapshot(){

		SortedSet<Share> copy = new TreeSet<Share>(shares);
		
		return copy;
		

	}
	
	//Get a copy of a specific share
	//// Wieso copy?
	public Share getShare(String shareName){
		for ( Share share : shares )
		{			
			if ( share.getName().equals(shareName) )
				return share;
		}
	
		
		//Share doesn't exist
		throw new ObjectNotFoundException("Share " + shareName + " doesn't exist");
	}
	
	//Start auto-updating share rates
	public void startUpdate(){
		timer.scheduleAtFixedRate(new TimerTask(){
			public void run(){
				updateShareRates();
			}
		}, 3000, 1000);
	}
	
	//Get info
	public String shareInfo(){
		String out = "";
		
		for( Share share : shares ){
			out += share.getName() + "\n" + share.getPrice() + "\n\n";
		}
		
		return out;
		
	}
	
	public String shareList(){
		String out = "";
		
		for( Share share : shares ){
			out += share.getName() + "\n";
		}
		
		return out;
	}
	
	//Update all share rates
	protected void updateShareRates(){
		for( Share share : shares ){
			updateShareRate(share);
		}
	}
	
	//Update a single shares share rate
	protected abstract void updateShareRate(Share share);
}
