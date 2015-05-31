package prog.core.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import prog.core.Share;

public class HistoricalStockPriceProvider extends StockPriceProvider {
	
	private final String CSV_SEPARATOR = ",";
	
	private LocalDate currentDate = LocalDate.now();
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yy").withLocale(Locale.ENGLISH);
	
	private Map<LocalDate,HashMap<Share,Integer>> rates = new TreeMap<LocalDate,HashMap<Share,Integer>>();
	
	private Map<String,String> toETR = new HashMap<String,String>(){
		{
			put("BMW","BMW");
			put("Siemens", "SIE");
			put("Commerzbank", "CBK");
			put("Volkswagen", "VOW");
			put("Osram", "OSR");
			put("BASF", "BAS");
		}
	};
		
	
	/*
	 * CSV Values
	 * 0: Date
	 * 1: Open
	 * 2: High
	 * 3: Low
	 * 4: Close
	 * 5: Volume
	 */
	private void init() throws IOException
	{
		LocalDate date;
		URL url;
		InputStream is;
		String[] values;
		BufferedReader reader;
		String line;
		int rate;
	
		for ( Share currentShare : shares )
		{
			
			url = new URL("http://www.google.com/finance/historical?output=csv&q=ETR:" + toETR.get(currentShare.getName()));
			is = url.openConnection().getInputStream();		
			reader = new BufferedReader( new InputStreamReader( is )  );
						
			// Überspringe erste Zeile (Überschriften)
			reader.readLine();
			
			while( ( line = reader.readLine() ) != null )  
			{
						
				values = line.split(CSV_SEPARATOR);				
				rate = (int) (Float.parseFloat(values[1]) * 100);	
				date = LocalDate.parse(values[0], formatter);
				
				if (date.isBefore(currentDate))
					currentDate = date;

				rates.putIfAbsent(date, new HashMap<Share,Integer>());
				
				rates.get(date).put(currentShare, rate);
				

			}
			reader.close();			
			
		}		

	}

	@Override
	protected void updateShareRate(Share share) 
	{
		
		if (rates.get(currentDate) == null || rates.get(currentDate).get(share) == null)
			return;

		share.setPrice(rates.get(currentDate).get(share).intValue());
		
		
	}
	
	@Override
	protected void updateShareRates(){
		if (rates.isEmpty())
			try {
				init();
			} catch (IOException e) {
				e.printStackTrace();
			}
		super.updateShareRates();
//		System.out.println(currentDate);
		currentDate = currentDate.plusDays(1);
	}

}
