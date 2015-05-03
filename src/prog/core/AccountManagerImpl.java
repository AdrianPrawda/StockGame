package prog.core;

import prog.exception.*;
import prog.core.provider.*;
import prog.interfaces.AccountManager;

public class AccountManagerImpl implements AccountManager {
	
	//Prices of stocks are here
	private StockPriceProvider provider;
	
	//Share Cache
	private Share[] shares;
	
	//List of registered players
	private Player[] players;

	public AccountManagerImpl(StockPriceProvider provider){
		players = new Player[0];
		this.provider = provider;
		shares = provider.getAllSharesAsSnapshot();
	}
	
	//Update share cache
	private void updateShareCache(){
		shares = provider.getAllSharesAsSnapshot();
	}
	
	@Override
	//Create new Player (name has to be unique!)
	public void createPlayer(String name) {
		
		//buffer output
		Player[] out = new Player[players.length+1];
		
		for(int i=0; i<players.length; i++){
			//Search for player
			if(players[i].getName().equals(name)){
				//Player already exists!
				throw new ObjectAlreadyExistsException("Can't create new player, player " + name + " already exists!");
			}
			//Copy players array in the meantime
			out[i] = players[i];
		}
		
		//Newly created player is always the last entry
		out[out.length-1] = new Player(name);
		players = out;
	}
	
	//Check if given player name is listed
	private boolean isPlayerListed(String playerName){
		for(int i=0; i<players.length; i++){
			//Check if player name already exists
			if(players[i].getName().equals(playerName)){
				//If so: return true (Player is already listed)
				return true;
			}
		}
		
		//No player with this name exists -> player name is not listed
		return false;
	}
	
	//Get player object by name
	private Player getPlayer(String playerName){
		for(int i=0; i<players.length; i++){
			//Check if player name exists
			if(players[i].getName().equals(playerName)){
				//Return player object
				return players[i];
			}
		}
		
		//Player doesn't exist
		throw new ObjectNotFoundException("Player " + playerName + " not found");
	}
	
	//Get share object by name
	private Share getShare(String shareName){
		//Update before returning the Share (we don't want outdated share info)
		updateShareCache();
		return provider.getShare(shareName);
	}

	@Override
	//Buy shares (primarily for public usage, private method can work directly with objects)
	public void buyShares(String playerName, String shareName, int quantity) throws FundsExceededException{
		updateShareCache();
		buyShares(getPlayer(playerName), getShare(shareName), quantity);
	}
	
	//Buy shares (Easier to handle with this parameter list if used internally)
	private void buyShares(Player player, Share share, int quantity) throws FundsExceededException{
		//Prevent user from buying negative shares
		if(quantity < 0){
			throw new InvalidNumberException("Can't buy negative shares");
		}
		
		//Remove funds before removing shares because of the possibility of an needed exception handling
		try{
			player.getCashAccount().withdraw(share, quantity);
		}catch(FundsExceededException e){
			throw e;
		}
		//Add shares
		player.getShareDepositAccount().addShares(share, quantity);
	}

	@Override
	//Sell shares
	public void sellShare(String playerName, String shareName, int quantity) throws NotEnoughSharesException{
		updateShareCache();
		sellShares(getPlayer(playerName), getShare(shareName), quantity);
	}
	
	//Sell shares
	private void sellShares(Player player, Share share, int quantity) throws NotEnoughSharesException{
		//Prevent user from buying negative shares
		if(quantity < 0){
			throw new InvalidNumberException("Can't sell negative shares");
		}
		
		//Try to sell shares first (Exception handling!)
		try{
			player.getShareDepositAccount().removeShares(share.getName(), quantity);
		}catch(NotEnoughSharesException e){
			throw e;
		}
		
		//Deposit money on player's account
		player.getCashAccount().deposit(share, quantity);
	}

	@Override
	//Get value of an Asset
	public long assetValue(Asset asset) {
		return asset.value();
	}

	@Override
	//Get total value of a player (cash + shares)
	public long playerValue(String playerName) {
		Player p = getPlayer(playerName);
		
		//cash value + share value
		return (p.getCashAccount().value() + p.getShareDepositAccount().value());
	}

	@Override
	//Get price of a share
	public long sharePrice(String shareName) {
		updateShareCache();
		
		Share share = getShare(shareName);
		return share.getPrice();
	}

	@Override
	//Print info for all available shares
	public String availableShares() {
		updateShareCache();
		String out = "";
		
		//Get name and price of every share
		for(int i=0; i<shares.length; i++){
			out = out + shares[i].getName() + ": " + shares[i].getPrice() + "\n";
		}

		return out;
	}
	
	@Override
	//Get info about all classes handled by AccountManagerImpl
	public String toString(){
		//All available shares
		String out = "Available Shares:\n" + availableShares() + "\nPlayers:\n";
		
		//All players
		for(int i=0; i<players.length; i++){
		out = out + players[i].toString() + "\n";
		}
		
		return out;
	}
	
	//A List of all registered player names
	public String playerList(){
		String out = "";
		
		//Get every players' name
		for(int i=0; i<players.length; i++){
			out += players[i].getName() + "\n";
		}
		
		return out;
	}

	@Override
	//Get price difference of a given share between now and and the time of purchase
	public long SharePriceDifference(String player, String shareName) {
		Player p = getPlayer(player);
		Share s = getShare(shareName);
		
		//Actual difference is calculated in ShareDepositAccount
		return p.getShareDepositAccount().sharePriceDifference(s, shareName);
	}
	
	//Add a player agent (trade bot)
	public void addPlayerAgent(String playerName, long minTradeDiff){
		Player player = getPlayer(playerName);
		PlayerAgent agent = new PlayerAgent(this, player, provider, minTradeDiff);
		player.setPlayerAgent(agent);
	}
	
	
	//Dismiss a player agent (trade bot)
	public void dismissPlayerAgent(String playerName){
		PlayerAgent agent = getPlayer(playerName).getPlayerAgent();
		agent.dismiss();
	}

}
