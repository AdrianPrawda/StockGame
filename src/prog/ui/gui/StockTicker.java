package prog.ui.gui;

import java.util.TimerTask;
import java.util.Timer;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;

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
			String output = "<html>";
			output += "<style>body{padding: 5px;width:600px}#players td{border: 1px black solid;width: 100px}</style>";
			output += "<body>"; 
			
			output += "Aktienkurse:<br><br><table>";
			Share[] shares = provider.getAllSharesAsSnapshot();
			for (int j = 0; j < shares.length; j++)
				output += "<tr><td>"+ shares[j].getName() +"</td><td>"+ ((float) shares[j].getPrice()/100) +" €</td></tr>";

			output += "</table>";
			
			Player[] players = manager.getPlayers();
		
			
			output += "<table id='players'>";
			output += "<tr><th></th><th>Vermögen</th><th>Kontostand</th><th>Aktien</th></tr>";
			for (int j = 0; j < players.length; j++)
			{
				output += "<tr>";
				output += "<td>"+ players[j].getName() +"</td><td>"+ ((float) players[j].value() / 100) +" €</td>";
				output += "<td>"+ ((float) players[j].getCashAccount().value() / 100) +" €</td>";
				Share[] ownedShares = players[j].getShareDepositAccount().getAllSharesAsSnapshot();
				output += "<td>";
				for (int i = 0; i < ownedShares.length; i++)
					output += "<div>" + ownedShares[i].getName() +" ("+ players[j].getShareDepositAccount().numberOfShares(ownedShares[i].getName()) +")</div>";
				output += "</td>";
				output += "</tr>";
			}
				
			
			output += "</table>";
			
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            DateFormat dateFormatter = DateFormat.getDateTimeInstance();
            output += dateFormatter.format(date) + "</body></html>";
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
