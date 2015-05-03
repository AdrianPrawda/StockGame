package prog.interfaces;

import prog.core.Share;

public interface StockPriceInfo{

	//Check if a certain share is listed
	public boolean isShareListed(String shareName);
	
	//Get the share rate of a share
	public long getCurrentShareRate(String shareName);
	
	//A list of all shares as snapshot
	public Share[] getAllSharesAsSnapshot();
}
