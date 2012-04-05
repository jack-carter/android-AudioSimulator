/**
 * 
 */
package com.maniac.tester.audio.values;

import com.maniac.tester.helpers.NamedValue;

/**
 * @author J Carter
 *
 */
public class Sampling 
{
	static public int AT_16KHZ = 16000;
	
	static public Sampling.Rate[] samplingRates()
	{
		if ( standardSamplingRates == null )
		{
			standardSamplingRates = new Sampling.Rate[]
			{
				new Sampling.Rate( 8000, "8 KHz"),
				new Sampling.Rate(11025,"11 KHz"),
				new Sampling.Rate(16000,"16 KHz"),
				new Sampling.Rate(22050,"22 KHz"),
				new Sampling.Rate(44100,"44 KHz"),
			};
		}
		
		return standardSamplingRates;
	}
	
	static public Sampling.Rate valueOf(int value)
	{
		for (Sampling.Rate rate : samplingRates())
			if ( rate.value() == value )
				return rate;
		
		return null;
	}
	
	static private Sampling.Rate[] standardSamplingRates;
	
	static public class Rate extends NamedValue
	{
		private Rate(int rate,String name)
		{
			super(rate,name);
		}		
	}
}
