package prog.core;

import java.util.ArrayList;

import prog.exception.*;

public class ShareDepositAccount extends Asset{
	//Share items
   ArrayList<ShareItem> shareItems = new ArrayList<ShareItem>();
   
   public ShareDepositAccount(String name){
      this.name = name;
   }
   
   //Get a copy of all shares
	public ArrayList<Share> getAllSharesAsSnapshot(){
		
		ArrayList<Share> shares = new ArrayList<Share>();
		
		shareItems.forEach( item -> shares.add(item.getShare()) );

		
		return shares;
		
	}
   
   
   //Get difference between a share at the current price and a share that had already been bought
   public long sharePriceDifference(Share share, String shareName){
	   ShareItem s = null;
	   
	   try{
		   s = getShareItem(shareName);
	   }catch(ObjectNotFoundException e){
		   //Re-throw exception but change the error text
		   throw new ObjectNotFoundException("Player does not own a share from " + shareName);
	   }
	   
	   //Price difference is calculated
	   long purchasePricePerShare = s.getPurchaseValue()/s.getQuantity();
	   
	   return share.getPrice() - purchasePricePerShare;
   }
   
   //Get value of all shares
   public long value(){
     
      return shareItems
    		  .stream()
    		  .mapToLong( ShareItem::value )
    		  .sum();
   }
   
   //Info about ShareDepositAccount
   public String toString(){
	   return ("[" + name + "] Value: " + value() + " Items: " + shareItems.size());
   }
   
   //Check if a share item is already in this deposit
   private boolean isShareItemListed(ShareItem shareItem){
	   return isShareItemListed(shareItem.getName());
   }
   
   //Check if a share item is already in this deposit
   private boolean isShareItemListed(String shareItemName){
	   return shareItems
			   .stream()
			   .anyMatch( s -> s.getName().equals(shareItemName) );

   }
   
   //Get share item by name
   private ShareItem getShareItem(String shareItemName){
	   return shareItems
			   .stream()
			   .filter( s -> s.getName().equals(shareItemName) )
			   .findAny()
			   .orElseThrow(() -> new ObjectNotFoundException("Share Item " + shareItemName + " not found")); 

   }
   
   
   //Add a new share item
   private void addShareItem(ShareItem shareItem){
	   //Check if share item already exists
	   if(isShareItemListed(shareItem)){
		   //Share item already exists!
		   throw new ObjectAlreadyExistsException("Share item " + shareItem.getName() + " already exists");
	   }
	   
	   shareItems.add(shareItem);
	   

   }
   
   //Create a new share item uniquely defined by the ref share
   private void addShareItem(Share ref){
	   addShareItem(new ShareItem(ref));
   }
   
   //Add shares to this deposit
   public void addShares(Share share, int quantity){
	   //Check if a correct share item already exists in this deposit
	   if(!isShareItemListed(share.getName())){
		   //If not, create new share item
		   addShareItem(share);
	   }
	   
	   //Add shares
	   getShareItem(share.getName()).addShares(share, quantity);
   }

   
   //Remove shares from this deposit
   public void removeShares(String shareItemName, int quantity) throws NotEnoughSharesException
   {
	   //Check if share item exists
	   if(!isShareItemListed(shareItemName)){
		   //If not, throw exception
		   throw new ObjectNotFoundException("Share Item " + shareItemName + " not found");
	   }
	   
	   //Remove shares
	   try{
		   getShareItem(shareItemName).removeShares(quantity);
	   }catch(NotEnoughSharesException e){
		   throw e;
	   }
   }
   
   //Get the number of shares in share item via the shares name
   public int numberOfShares(String shareName){
	   int n = 0;
	   
	   try{
		   n = getShareItem(shareName).getQuantity();
	   }catch (ObjectNotFoundException e){
		   return n;
	   }
	   
	   return n;
   }
   
}
