package prog.core;

import prog.exception.FundsExceededException;
import prog.exception.InvalidNumberException;

public class CashAccount extends Asset{
   private long balance;
   public static long STARTING_FUNDS = 100000;
   
   public CashAccount(String name){
      this.name = name;
      balance = STARTING_FUNDS;
   }
   
   public long value(){
      return balance;
   }
   
   public String toString(){
	   return ("[" + name + "] Balance: " + balance);
   }
   
   //Withdraw funds
   public void withdraw(Share share, int quantity) throws FundsExceededException{
	   //Check if quantity is negative
	   if(quantity < 0){
		   throw new InvalidNumberException("Quantity has to be positive");
	   }
	   
	   //Calculate price
	   long price = share.getPrice() * quantity;
	   
	   //Check if there is enough money on this account
	   if(balance < price){
		   //Not enough money!
		   throw new FundsExceededException("Not enough funds available");
	   }
	   
	   balance -= price;
   }
   
   //Deposit funds
   public void deposit(Share share, int quantity){
	   //Check if quantity is negative
	   if(quantity < 0){
		   throw new InvalidNumberException("Quantity has to be positive");
	   }
	   
	   //Calculate new balance
	   balance += (share.getPrice() * quantity);
   }
   
}
