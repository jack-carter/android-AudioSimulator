/**
 * 
 */
package com.maniac.tester.audio.builders;

import com.maniac.tester.audio.Settings;
import com.maniac.tester.audio.values.Channel;
import com.maniac.tester.audio.values.Encoding;
import com.maniac.tester.audio.values.ISettings;
import com.maniac.tester.audio.values.Sampling;
import com.maniac.tester.audio.values.Source;

/**
 * @author J Carter
 *
 */
public class SettingsBuilder 
{
	public SettingsBuilder source(Source source) 
	{ 
		settings.source(source);
		return this; 
	}
	
	public SettingsBuilder channel(Channel channel)
	{ 
		settings.channel(channel);
		return this;
	}
	
	public SettingsBuilder encoding(Encoding encoding)
	{ 
		settings.encoding(encoding);
		return this;
	}
	
	public SettingsBuilder samplingRate(Sampling.Rate rate)
	{ 
		settings.samplingRate(rate);
		return this;
	}
	
	public SettingsBuilder buffer(int size)
	{ 
		settings.buffer(size);
		return this;
	}
	
	public ISettings create() 
	{ 
		return settings;
	}
	
	private ISettings settings = new Settings();
}
