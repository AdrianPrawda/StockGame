package prog.interfaces;

import prog.exception.FundsExceededException;
import prog.exception.NotEnoughSharesException;
import prog.core.Asset;
import prog.ui.AsCommand;

public interface AccountManager {
	
	@AsCommand(commandName = "crp", description = "<name> * create new player")
	public void createPlayer(String name);
	
	@AsCommand(commandName = "bs", description = "<player> <sharename> <amount> * buy that amount of shares")
	public void buyShares(String playerName, String shareName, int quantity) throws FundsExceededException;
	
	@AsCommand(commandName = "ss", description = "<player> <sharename> <amount> * sell that amount of shares")
	public void sellShare(String playerName, String shareName, int quantity) throws NotEnoughSharesException;
	
	public long assetValue(Asset asset);
	
	@AsCommand(commandName = "pv", description = "<player> * get value of a players assets")
	public long playerValue(String playerName);
	
	public long sharePrice(String shareName);
	
	public String availableShares();
	
	@AsCommand(commandName = "lsp", description = "* list all registered players")
	public String playerList();
	
	@AsCommand(commandName = "diff", description = "<player> <sharename> * get difference between purchase price and current selling price")
	public long SharePriceDifference(String player, String shareName);
	
	@AsCommand(commandName = "na", description = "<player> <maxTradeDifference> * initilaize a bot that will automatically trade for the player")
	public void addPlayerAgent(String playerName, long maxTradeDiff);
	
	@AsCommand(commandName = "da", description = "<player> * dismiss trading bot")
	public void dismissPlayerAgent(String playerName);

}
