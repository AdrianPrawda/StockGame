package prog.interfaces;

import prog.exception.FundsExceededException;
import prog.exception.NotEnoughSharesException;
import prog.core.AccountManagerProxy;
import prog.core.Asset;
import prog.core.Player;
import prog.ui.AsCommand;

public interface AccountManager {
	
	//Create new player
	@AsCommand(commandName = "crp", description = "<name> * create new player")
	public void createPlayer(String name);
	
	//Buy shares
	@AsCommand(commandName = "bs", description = "<player> <sharename> <amount> * buy that amount of shares")
	public void buyShares(String playerName, String shareName, int quantity) throws FundsExceededException;
	
	//Sell shares
	@AsCommand(commandName = "ss", description = "<player> <sharename> <amount> * sell that amount of shares")
	public void sellShare(String playerName, String shareName, int quantity) throws NotEnoughSharesException;
	
	//Get value of an asset
	public long assetValue(Asset asset);
	
	//Get a players total value (values of all his assets combined)
	@AsCommand(commandName = "pv", description = "<player> * get value of a players assets")
	public long playerValue(String playerName);
	
	//Price of a share
	public long sharePrice(String shareName);
	
	//All available shares
	public String availableShares();
	
	//The name of all registered players
	@AsCommand(commandName = "lsp", description = "* list all registered players")
	public String playerList();
	
	//Price difference between a share now and back when the player bought it
	@AsCommand(commandName = "diff", description = "<player> <sharename> * get difference between purchase price and current selling price")
	public long SharePriceDifference(String player, String shareName);
	
	//Ad a new player agent (trade bot)
	@AsCommand(commandName = "sa", description = "<player> * initilaize a bot that will automatically trade for the player")
	public void addPlayerAgent(String playerName);
	
	//Dismiss a player agent (trade bot)
	@AsCommand(commandName = "da", description = "<player> * dismiss trading bot")
	public void dismissPlayerAgent(String playerName);
	
	public Player[] getPlayers();

	public void setProxy(AccountManager accountManagerProxy);

}
