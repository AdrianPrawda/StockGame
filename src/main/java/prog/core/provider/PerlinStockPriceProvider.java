package prog.core.provider;

import prog.core.Share;
import prog.exception.InvalidNumberException;
import prog.math.Noise;

import java.lang.Math;

public class PerlinStockPriceProvider extends StockPriceProvider {
	private final long MAX;
	
	private Noise noise;
	private double start;
	private double mult;
	private double last;
	private long c;

	public PerlinStockPriceProvider(double persistence, int octaves, double start, double mult, long maxDeviation) {
		if(octaves < 1){
			//Number of octaves has to be at least 1
			throw new InvalidNumberException("Only values <1 are allowed.");
		}
		noise = new Noise(persistence, octaves);
		this.start = start;
		this.mult = mult;
		
		c = 0;
		MAX = maxDeviation;
	}

	@Override
	protected void updateShareRate(Share share) {
		//Calculate noise
		double r = noise.next1D(start + (start*mult*(c)));
		
		//Get percentile difference
		double p = (1-(r/last)) * -1;
		last = r;
		
		//Calculate new price
		double priceNew = share.getPrice() + (share.getPrice()*p);
		//Calculate delta from new price
		long priceDelta = Math.round(Math.floor(priceNew - share.getPrice()));
		
		//If delta is to big, try again next time
		if(priceDelta > MAX){
			return;
		}
		
		//Change price accordingly
		share.changePrice(priceDelta);
		
		//Increase counter
		c += 1;

	}

}
