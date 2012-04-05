/**
 * 
 */
package com.maniac.tester.audio.values;

import com.maniac.tester.helpers.NamedValue;

import android.media.AudioFormat;

/**
 * @author J Carter
 *
 */
public class Channel extends NamedValue
{
	static public Channel[] channels()
	{
		if ( channels == null )
		{
			channels = new Channel[]
           {
				new Channel(AudioFormat.CHANNEL_IN_MONO,"IN_MONO"),
				new Channel(AudioFormat.CHANNEL_IN_STEREO,"IN_STEREO"),
				new Channel(AudioFormat.CHANNEL_CONFIGURATION_MONO,"CONFIGURATION_MONO"),
				new Channel(AudioFormat.CHANNEL_CONFIGURATION_STEREO,"CONFIGURATION_STEREO"),
           };
		}
		
		return channels;
	}
	
	static public Channel valueOf(int value)
	{
		for (Channel channel : channels())
			if (channel.value() == value)
				return channel;
		
		return null;
	}
	
	private Channel(int channel, String name)
	{
		super(channel,name);
	}
	
	static private Channel[] channels;
}
