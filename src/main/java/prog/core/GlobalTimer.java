package prog.core;

import java.util.Timer;

public class GlobalTimer extends Timer {
	private static GlobalTimer globalTimer;
	
	private GlobalTimer() {};
	
	public static GlobalTimer getInstance(){
		if(globalTimer == null){
			globalTimer = new GlobalTimer();
		}
		
		return globalTimer;
	}
}
