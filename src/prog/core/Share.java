package prog.core;

public class Share implements Comparable<Share> {
   private final String NAME;
   private long price;
   
   //Copy constructor
   public Share(Share share){
	   this(share.getName(), share.getPrice());
   }
   
   public Share(String name, long price){
      NAME = name;
      this.price = price;
   }
   
   public String getName(){
      return NAME;
   }
   
   public long getPrice(){
      return price;
   }
   
   public void changePrice(long difference){
	   if(price+difference < 0){
		   price = 0;
	   }else{
		   price += difference;
	   }
   }
   
   public String toString(){
	   return ("[" + NAME + "] Price: " + price);
   }
   
   //Shares are equal is there price and name are equal
   public boolean equals(Object obj){
      //Check if this and the given object's class are equal
      if(obj.getClass().equals(this.getClass())){
         //If so check if there name and price is equal
         if(((Share) obj).getName().equals(NAME) && (((Share) obj).getPrice() == this.price)){
            //Return true if requirements are met
            return true;
         }
      }
      //Return false if not
      return false;
   }

	@Override
	public int compareTo(Share share) {
		// TODO Auto-generated method stub
		return getName().compareTo(share.getName());
	}
   
}
