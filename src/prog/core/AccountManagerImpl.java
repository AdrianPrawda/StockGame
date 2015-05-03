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
		
//		provider = new ConstStockPriceProvider();
//		provider = new RandomStockPriceProvider(6);
		this.provider = provider;
		
		shares = provider.getAllSharesAsSnapshot();
	}
	
	private void updateShareCache(){
		shares = provider.getAllSharesAsSnapshot();
	}
	
	@Override
	//Create new Player (name has to be unique!)
	public void createPlayer(String name) {
		
		Player[] out = new Player[players.length+1];
		
		for(int i=0; i<players.length; i++){
			//Search for player
			if(players[i].getName().equals(name)){
				//Player already exists!
				throw new ObjectAlreadyExistsException("Can't create new player, player " + name + " already exists!");
			}
			out[i] = players[i];
		}
		
		out[out.length-1] = new Player(name);
		players = out;
	}
	
	//Check if given player name is unique
	private boolean isPlayerListed(String playerName){
		for(int i=0; i<players.length; i++){
			//Check if player name already exists
			if(players[i].getName().equals(playerName)){
				//If so: return true
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
		updateShareCache();
		return provider.getShare(shareName);
	}

	@Override
	//Buy shares
	public void buyShares(String playerName, String shareName, int quantity) throws FundsExceededException{
		updateShareCache();
		buyShares(getPlayer(playerName), getShare(shareName), quantity);
	}
	
	private void buyShares(Player player, Share share, int quantity) throws FundsExceededException{
		//Prevent user from buying negative shares
		if(quantity < 0){
			throw new InvalidNumberException("Can't buy negative shares");
		}
		
		//Remove funds
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
	
	private void sellShares(Player player, Share share, int quantity) throws NotEnoughSharesException{
		//Prevent user from buying negative shares
		if(quantity < 0){
			throw new InvalidNumberException("Can't sell negative shares");
		}
		
		//Sell shares
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
		for(int i=0; i<players.length; i++){
			if(players[i].getName().equals(playerName)){
				//Calculate player value (Cash + Shares)
				return (players[i].getCashAccount().value() + players[i].getShareDepositAccount().value());
			}
		}
			
		throw new ObjectNotFoundException("Player " + playerName + " not found");
	}

	@Override
	//Get price of a share
	public long sharePrice(String shareName) {
		updateShareCache();
		for(int i=0; i<shares.length; i++){
			if(shares[i].getName().equals(shareName)){
				return shares[i].getPrice();
			}
		}
		
		throw new ObjectNotFoundException("Share " + shareName + " not found");
	}

	@Override
	//Print info for all available shares
	public String availableShares() {
		updateShareCache();
		String out = "";
		
		for(int i=0; i<shares.length; i++){
			out = out + shares[i].getName() + ": " + shares[i].getPrice() + "\n";
		}

		return out;
	}
	
	@Override
	//Get info about all classes handled by AccountManagerImpl
	public String toString(){
		String out = "Available Shares:\n" + availableShares() + "\nPlayers:\n";
		
		for(int i=0; i<players.length; i++){
		out = out + players[i].toString() + "\n";
		}
		
		return out;
	}
	
	public String playerList(){
		String out = "";
		
		for(int i=0; i<players.length; i++){
			out += players[i].getName() + "\n";
		}
		
		return out;
	}

	@Override
	public long SharePriceDifference(String player, String shareName) {
		Player p = getPlayer(player);
		Share s = getShare(shareName);
		
		return p.getShareDepositAccount().sharePriceDifference(s, shareName);
	}
	
	public void addPlayerAgent(String playerName, long minTradeDiff){
		Player player = getPlayer(playerName);
		PlayerAgent agent = new PlayerAgent(this, player, provider, minTradeDiff);
		player.setPlayerAgent(agent);
	}
	
	public void dismissPlayerAgent(String playerName){
		PlayerAgent agent = getPlayer(playerName).getPlayerAgent();
		agent.dismiss();
	}
	
//	//FOR TESTING PURPOSES ONLY - Add money to a players account
//	public void addPlayerMoney(String playerName, long cash){
//		//Check if player exists
//		if(!isPlayerListed(playerName)){
//			//If not, throw exception
//			throw new ObjectNotFoundException(playerName + " not found");
//		}
//		
//		getPlayer(playerName).getCashAccount().addCash(cash);
//	}

}
