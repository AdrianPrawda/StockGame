package prog.core;

import prog.exception.*;

public class ShareDepositAccount extends Asset{
   ShareItem[] shareItems = new ShareItem[0];
   
   public ShareDepositAccount(String name){
      this.name = name;
   }
   
   public long sharePriceDifference(Share share, String shareName){
	   ShareItem s = null;
	   
	   try{
		   s = getShareItem(shareName);
	   }catch(ObjectNotFoundException e){
		   throw new ObjectNotFoundException("Player does not own a share from " + shareName);
	   }
	   long purchasePricePerShare = s.getPurchaseValue()/s.getQuantity();
	   
	   return share.getPrice() - purchasePricePerShare;
   }
   
   //Get value of all shares
   public long value(){
      int v = 0;
      for(int i=0; i<shareItems.length; i++){
         v += shareItems[i].value();
      }
      return v;
   }
   
   //Info about ShareDepositAccount
   public String toString(){
	   return ("[" + name + "] Value: " + value() + " Items: " + shareItems.length);
   }
   
   //Check if a share item is already in this deposit
   private boolean isShareItemListed(ShareItem shareItem){
	   return isShareItemListed(shareItem.getName());
   }
   
   //Check if a share item is already in this deposit
   private boolean isShareItemListed(String shareItemName){
	   for(int i=0; i<shareItems.length; i++){
		   if(shareItems[i].getName().equals(shareItemName)){
			   //Share item with this name is already in this deposit
			   return true;
		   }
	   }
	   
	   //Share item with this name is not in this deposit
	   return false;
   }
   
   //Get share item by name
   private ShareItem getShareItem(String shareItemName){
	   for(int i=0; i<shareItems.length; i++){
		   //If element was found, return
		   if(shareItems[i].getName().equals(shareItemName)){
			   return shareItems[i];
		   }
	   }
	   
	   //If no element was found throw exception
	   throw new ObjectNotFoundException("Share Item " + shareItemName + " not found");
   }
   
   
   //Add a new share item
   private void addShareItem(ShareItem shareItem){
	   //Check if share item already exists
	   if(isShareItemListed(shareItem)){
		   //Share item already exists!
		   throw new ObjectAlreadyExistsException("Share item " + shareItem.getName() + " already exists");
	   }
	   
	   ShareItem[] out = new ShareItem[shareItems.length+1];
	   
	   //Copy share items
	   for(int i=0; i<shareItems.length; i++){
		   out[i] = shareItems[i];
	   }
	   
	   //Add share item
	   out[out.length-1] = shareItem;
	   shareItems = out;
   }
   
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
   
   public int numberOfShares(String shareName){
	   return getShareItem(shareName).getQuantity();
   }
   
}
