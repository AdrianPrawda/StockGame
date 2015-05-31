package prog.core.provider;

import prog.core.Share;
import java.util.Random;

public class RandomStockPriceProvider extends StockPriceProvider {
	private Random rng;
	private int maxDeviation;
	
	public RandomStockPriceProvider(int maxDeviation){
		rng = new Random();
		this.maxDeviation = maxDeviation;
	}

	@Override
	protected void updateShareRate(Share share) {
		int r1 = rng.nextInt(maxDeviation+1);
		float r2 = rng.nextFloat();
		
		if(r2<0.5f){
			share.changePrice(-r1);
		}else{
			share.changePrice(r1);
		}

	}

}
