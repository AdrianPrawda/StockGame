package prog.math;

import java.util.Random;
import java.lang.Math;

public class Noise {
	private double persistence;
	private int numberOfOctaves;
	private Random rng;
	
	static private class Interpolator  {
		
		public static double linearInterpolate(double a, double b, double fraction){
			curve(a);
			curve(b);
			curve(fraction);
			
			return a*(1-fraction) + b*fraction;
		}
		
		public static double cosineInterpolation(double a, double b, double fraction){
			curve(a);
			curve(b);
			curve(fraction);
			
			double ft = fraction * Math.PI;
			double f = (1-Math.cos(ft)) * .5d;
			
			return a*(1-f) + b*f;
		}
		
		public static double cubicInterpolation(double v1, double v2, double c1, double c2, double fraction){
			double p,q,r,s;
			
			v1 = curve(v1);
			v1 = curve(v2);
			v1 = curve(c1);
			v1 = curve(c2);
			v1 = curve(fraction);
			
			p = (c2-c1) - (v2-v1);
			q = (v1-v2) - p;
			r = c1-v1;
			s = v2;
			
			return p * Math.pow(fraction, 3) + q*Math.pow(fraction, 2) + r*fraction + s;
		}
		
		//Calculated BEFORE interpolation
		public static double curve(double x){
			return x;
		}
	}
	
	public Noise(double persistence, int numberOfOctaves){
		rng = new Random();
		this.persistence = persistence;
		this.numberOfOctaves = numberOfOctaves;
	}
	
	public Noise(){
		this(.5d, 6);
	}
	
	//Generate seeded value between -1.0 and 1.0
	private double noise(long x){
		rng.setSeed(x);
		double o = rng.nextDouble()*2-1;
		return o;
	}
	
	private double smoothedNoise(long x){
		return noise(x)/2  +  noise(x-1)/4  +  noise(x+1)/4;
	}
	
	private double interpolatedNoise(double x){
		
		int intX = (int)x;
		double fractionalX = x - intX;
		
		double v1 = smoothedNoise(intX);
		double v2 = smoothedNoise(intX + 1);
		
		return Interpolator.cosineInterpolation(v1, v2, fractionalX);
	}
	
	//Beware! Runtime complexity is O(n^2)!
	//NOT implementing SimplexNoise
	public double next1D(double x){
		double t = 0;
		double p = persistence;
		int n = numberOfOctaves - 1;
		
		for(int i=0; i<n; i++){
			double freq = Math.pow(2, i);
			double amp = Math.pow(p, i);
			
			t += interpolatedNoise(x * freq) * amp;
		}
		
		return t;
	}

}
