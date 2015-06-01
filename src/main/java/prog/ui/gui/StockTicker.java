package prog.ui.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import javax.swing.JFrame;
import javax.swing.JLabel;

import prog.core.GlobalTimer;
import prog.core.Player;
import prog.core.Share;
import prog.core.provider.*;
import prog.interfaces.AccountManager;

public class StockTicker extends JFrame{
	private static final int TICK_PERIOD = 1000;
	private GlobalTimer timer = GlobalTimer.getInstance();
	private JLabel clockLabel;
	private StockPriceProvider provider;
	private AccountManager manager;
	
	private class TickerTask extends TimerTask {
		public void run() {
			String output = createText();
			clockLabel.setText(output);
			clockLabel.repaint();
		}
		
		private String createText(){
			String[] langProperties = System.getProperty("language").split("-");
			
			Locale currentLocale = new Locale(langProperties[0], langProperties[1]);
			ResourceBundle bundle = ResourceBundle.getBundle("LabelsBundle", currentLocale);
			
			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(currentLocale);
			DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.DEFAULT, currentLocale);
			
			String output = "<html>";
			output += "<style>body{padding: 5px;width:600px}#players td{border: 1px black solid;width: 100px}</style>";
			output += "<body>"; 
			
			output += bundle.getString("StockTicker.stockPrice") + ":<br><br><table>";
			Set<Share> shares = provider.getAllSharesAsSnapshot();
			for( Share share : shares )
				output += "<tr><td>"+ share.getName() +"</td><td>"+ numberFormat.format(((float) share.getPrice()/100)) + "</td></tr>";

			output += "</table>";
			
			HashMap<String,Player> players = manager.getPlayers();
		
			
			output += "<table id='players'>";
			output += "<tr><th></th><th>" + bundle.getString("StockTicker.assets") + "</th><th>" + bundle.getString("StockTicker.balance") + "</th><th>" + bundle.getString("StockTicker.shares") + "</th></tr>";
			for ( Entry<String,Player> entry : players.entrySet() )
			{
				Player currentPlayer = entry.getValue();
				output += "<tr>";
				output += "<td>"+ currentPlayer.getName() +"</td><td>"+ numberFormat.format(((float) currentPlayer.value() / 100)) + "</td>";
				output += "<td>"+ numberFormat.format(((float) currentPlayer.getCashAccount().value() / 100)) + "</td>";
				ArrayList<Share> ownedShares = currentPlayer.getShareDepositAccount().getAllSharesAsSnapshot();
				output += "<td>";
				for ( Share share : ownedShares )
					output += "<div>" + share.getName() +" ("+ currentPlayer.getShareDepositAccount().numberOfShares(share.getName()) +")</div>";
				output += "</td>";
				output += "</tr>";
			}
				
			
			output += "</table>";
			
			Date date = new Date();
			output += dateFormat.format(date) + "</body></html>";
            return output;
		}
	}
	
	public StockTicker(StockPriceProvider provider, AccountManager manager) {
		this.provider = provider;
		this.manager = manager;
		
		clockLabel = new JLabel("Stock Ticker");
        add("Center", clockLabel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setSize(600, 400);
        setVisible(true);
	}
	
	public void start(){
		timer.scheduleAtFixedRate(new TickerTask(), 1000, TICK_PERIOD);
	}

}
