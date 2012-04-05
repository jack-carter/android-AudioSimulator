/**
 * 
 */
package com.maniac.tester.audio;

import com.maniac.tester.audio.builders.SettingsBuilder;
import com.maniac.tester.audio.values.Channel;
import com.maniac.tester.audio.values.Encoding;
import com.maniac.tester.audio.values.ISettings;
import com.maniac.tester.audio.values.Sampling;
import com.maniac.tester.audio.values.Source;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

/**
 * @author J Carter
 *
 */
public final class Audio 
{
	static public Source[] 	 	  sources() 		{ return Source.sources(); }
	static public Encoding[] 	  encodings() 		{ return Encoding.encodings(); }
	static public Channel[]  	  channels() 		{ return Channel.channels(); }
	static public Sampling.Rate[] samplingRates() 	{ return Sampling.samplingRates(); }
	static public Recordings 	  recordings()		{ return Recordings.getInstance(); }
	
	static public ISettings settings()      
	{ 
		if ( settings == null )
			settings = defaultSettings;
		return settings; 
	}
	
	static public void settings(ISettings newSettings)
	{ 
		settings = newSettings; 
	}
	
	static public Recording newRecording()
	{
		return Recordings.newRecording();
	}
	
	static public AudioRecord create(Settings settings)
	{
		return new AudioRecord
		( 
		settings().source().value(),
		settings().samplingRate().value(),
		settings().channel().value(),
		settings().encoding().value(),
		settings().buffer().size()
		);
	}
	
	static private ISettings settings;
	
	static private ISettings defaultSettings = new SettingsBuilder()
		.source(Source.valueOf(MediaRecorder.AudioSource.MIC))
		.channel(Channel.valueOf(AudioFormat.CHANNEL_CONFIGURATION_MONO))
		.encoding(Encoding.valueOf(AudioFormat.ENCODING_PCM_16BIT))
		.samplingRate(Sampling.valueOf(Sampling.AT_16KHZ))
		.buffer(Settings.BUFFER_NOT_SET)
		.create();
}
