package prog.core;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

import prog.exception.*;
import prog.core.provider.*;
import prog.interfaces.AccountManager;

public class AccountManagerImpl implements AccountManager {
	
	
	private AccountManager proxy;
	
	//Prices of stocks are here
	private StockPriceProvider provider;
	
	//Share Cache
	private Set<Share> shares;
	
	//List of registered players
	private HashMap<String,Player> players = new HashMap<String,Player>();

	public AccountManagerImpl(StockPriceProvider provider){

		this.provider = provider;
		shares = provider.getAllSharesAsSnapshot();

	}
	
	
	//// Proxy Methoden notwendig, damit auch Agent-Aktionen geloggt werden k�nnen; evtl. optimierungsbed�rftig
	public void setProxy(AccountManager proxy)
	{
		this.proxy = proxy;
	}
	
	public AccountManager getProxy()
	{
		return this.proxy;
	}
	
	
	//Update share cache
	private void updateShareCache(){
		shares = provider.getAllSharesAsSnapshot();
	}
	
	@Override
	//Create new Player (name has to be unique!)
	public void createPlayer(String name) {
		
		if (players.containsKey(name))
			throw new ObjectAlreadyExistsException("Can't create new player, player " + name + " already exists!");
		players.put(name, new Player(name));

	}
	
	//Check if given player name is listed
	private boolean isPlayerListed(String playerName){
		return players.containsKey(playerName);

	}
	
	//Get player object by name
	private Player getPlayer(String playerName){
		if (!players.containsKey(playerName))
			throw new ObjectNotFoundException("Player " + playerName + " not found");
		return players.get(playerName);
		
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
		player.getCashAccount().withdraw(share, quantity);
		//Add shares
		player.getShareDepositAccount().addShares(share, quantity);
		player.addTransaction(Transaction.Type.BUY, quantity*share.getPrice(), share, quantity, System.currentTimeMillis());
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
		player.getShareDepositAccount().removeShares(share.getName(), quantity);

		
		//Deposit money on player's account
		player.getCashAccount().deposit(share, quantity);
		player.addTransaction(Transaction.Type.SELL, quantity*share.getPrice(), share, quantity, System.currentTimeMillis());
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

		return shares
				.stream()
				.map( s -> String.format("%s: %d", s.getName(), s.getPrice()) )
				.collect(Collectors.joining("\n"));
	}
	
	@Override
	//Get info about all classes handled by AccountManagerImpl
	public String toString(){
		//All available shares
		String out = "Available Shares:\n" + availableShares() + "\nPlayers:\n";
		
		out += players
				.keySet()
				.stream()
				.collect( Collectors.joining("\n") );


		
		return out;
	}
	
	//A List of all registered player names
	public String playerList(){
		
		return players
				.keySet()
				.stream()
				.collect( Collectors.joining("\n") );
	}
	
	public HashMap<String,Player> getPlayers()
	{
		return players;
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
	public void addPlayerAgent(String playerName){
		Player player = getPlayer(playerName);
		PlayerAgent agent = new PlayerAgent(player, provider, this.getProxy());
		player.setPlayerAgent(agent);
		agent.startTrading();
	}
	
	
	//Dismiss a player agent (trade bot)
	public void dismissPlayerAgent(String playerName){
		getPlayer(playerName).getPlayerAgent().dismiss();
	}


	@Override
	public String getTransactions(String playerName, String... options) 
	{
			
		ArrayList<Transaction> transactions = getPlayer(playerName).getTransactions();
			    
	    return transactions
	    		.stream()
	    		.sorted( Transaction.getComparator(options) )
	    		.map( Transaction::toString )
	    		.collect( Collectors.joining() );
		
	}


}
