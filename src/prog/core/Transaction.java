package prog.core;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import prog.exception.InvalidArgumentException;



public class Transaction 
{
	
	private Type type;
	private long money;
	private Share share;
	private int amount;
	private long time;
	
	public enum Type
	{
		BUY, SELL
	}
	
	public enum SortBy
	{
		SHARE, TIME, AMOUNT, MONEY, TYPE
	}
	
	public Transaction(Type type, long money, Share share, int amount, long time)
	{
		this.type = type;
		this.money = money;
		this.share = share;
		this.amount = amount;
		this.time = time;
	}
	
	@Override
	public String toString()
	{
		String s = new String();
		Date d = new Date(time);
		Format format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String date = format.format(d);
		
		s += "[" + date + "]\t";
		s += (type == Type.BUY) ? "BUY" : "SELL";
		s += "\t"+ share.getName();
		s += "\t"+ amount;
		s += "\t";
		s += (type == Type.BUY) ? "-" : "+";
		s += ((float) this.money/100) + " €\n";
		
		
		
		return s;
	}
	
	public Share getShare()
	{
		return this.share;
	}
	
	public long getTime()
	{
		return this.time;
	}
	
	public int getAmount()
	{
		return this.amount;
	}
	
	public long getMoney()
	{
		return this.money;
	}
	
	public Type getType()
	{
		return this.type;
	}
	
	
	
	public static Comparator<Transaction> getComparator( String[] sortStrings )
	{
		
		ArrayList<SortBy> sorts = new ArrayList<SortBy>();
		for ( String currentSort : sortStrings )
		{
			switch(currentSort)
			{
			case "date":
				sorts.add(SortBy.TIME);
				break;
			case "share":
				sorts.add(SortBy.SHARE);
				break;
			case "amount":
				sorts.add(SortBy.AMOUNT);
				break;
			case "money":
				sorts.add(SortBy.MONEY);
				break;
			case "type":
				sorts.add(SortBy.TYPE);
				break;
			default:
				throw new InvalidArgumentException("Sort Field not found");
			}
		}
		return new TransactionComparator(sorts.toArray(new SortBy[0]));
	}
	
	
	
	private static class TransactionComparator implements Comparator<Transaction>
	{
		
		private SortBy[] sortBy;
		
		private TransactionComparator (SortBy[] sortBy)
		{
			this.sortBy = sortBy;
		}

		@Override
		public int compare(Transaction t1, Transaction t2) 
		{
			int result = 0;
			for ( SortBy currentSort : this.sortBy)
			{
				switch(currentSort)
				{
				case SHARE:
					result = t1.getShare().getName().compareTo(t2.getShare().getName());
					if (result != 0)
						return result;
					break;
				case TIME:
					result = Long.signum(t1.getTime() - t2.getTime());
					if (result != 0)
						return result;
					break;
				case AMOUNT:
					result = t1.getAmount() - t2.getAmount();
					if (result != 0)
						return result;
					break;
				case MONEY:
					result = Long.signum(t1.getMoney() - t2.getMoney());
					if (result != 0)
						return result;
					break;
				case TYPE:
					result = t1.getType().compareTo(t2.getType());
					if (result != 0)
						return result;
					break;

				}
			}
			return 0;
		}
		
	}

}
