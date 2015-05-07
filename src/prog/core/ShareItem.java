package prog.core;

import prog.exception.WrongShareException;
import prog.exception.NotEnoughSharesException;

public class ShareItem extends Asset{
   private int quantity;
   private long purchaseValue;
   private final Share REF;
   
   public ShareItem(Share reference){
      quantity = 0;
      purchaseValue = 0;
      REF = reference;
      name = REF.getName();
   }
   
   public int getQuantity(){
      return quantity;
   }
   
   public long getPurchaseValue(){
      return purchaseValue;
   }
   
   public String getName(){
      return name;
   }
   
   public Share getRef(){
	   return REF;
   }
   
   public String getShareName(){
	   return REF.getName();
   }
   
   //value is equal to purchase value (for now)
   public long value(){
      return this.getPurchaseValue();
   }
   
   public String toString(){
	   return ("[" + name + "] Quantity: " + getQuantity() + " Value: " + value());
   }
   
   public Share getShare()
   {
	   return REF;
   }
   
   //Add shares
   public void addShares(Share share, int quantity){
	   //Check if given share is compatible with this share item
	  if(share.getName().equals(REF.getName()) == false){
		  //If share is not compatible throw exception
		  throw new WrongShareException(share.getName() + " does not match share: " + REF.getName());
	  }
	  
	  //Add quantity and increase purchase value
      this.quantity += quantity;
      purchaseValue += share.getPrice() * quantity;
   }
   
   //Remove shares
   public void removeShares(int quantity) throws NotEnoughSharesException{
	   //Check if quantity exceeds number of available shares
	   if(quantity > this.quantity){
		   //If so, throw exception
		   throw new NotEnoughSharesException("Not enough shares available. Shares requested :" + quantity + ". Shares available" + this.quantity + ".");
	   }
	   
	   //Decrease purchase value (?)
	   purchaseValue -= ((purchaseValue / this.quantity) * quantity);
	   
	   this.quantity -= quantity;
   }
}
