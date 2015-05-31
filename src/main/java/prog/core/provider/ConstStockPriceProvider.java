package prog.core.provider;

import prog.core.Share;

public class ConstStockPriceProvider extends StockPriceProvider {
	
	@Override
	//Update share rates
	protected void updateShareRate(Share share) {
		//Prices are constant, no need to update anything
	}

}
