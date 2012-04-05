/**
 * 
 */
package com.maniac.tester.audio.values;

import com.maniac.tester.helpers.NamedValue;

import android.media.MediaRecorder;

/**
 * @author J Carter
 *
 */
public class Source extends NamedValue
{
	static public Source[] sources()
	{
		if ( sources == null )
		{
			sources = new Source[]
	   		{
   				new Source(MediaRecorder.AudioSource.CAMCORDER,"CAMCORDER"),
   				new Source(MediaRecorder.AudioSource.DEFAULT,"DEFAULT"),
   				new Source(MediaRecorder.AudioSource.MIC,"MIC"),
   				new Source(MediaRecorder.AudioSource.VOICE_CALL,"VOICE_CALL"),
   				new Source(MediaRecorder.AudioSource.VOICE_COMMUNICATION,"VOICE_COMMUNICATION"),
   				new Source(MediaRecorder.AudioSource.VOICE_DOWNLINK,"VOICE_DOWNLINK"),
   				new Source(MediaRecorder.AudioSource.VOICE_RECOGNITION,"VOICE_RECOGNITION"),
   				new Source(MediaRecorder.AudioSource.VOICE_UPLINK,"VOICE_UPLINK")
	   		};			
		}
		
		return sources;
	}
	
	static public Source valueOf(int value)
	{
		for (Source source : sources())
			if ( source.value() == value )
				return source;
		
		return null;
	}
	
	private Source(int source,String name)
	{
		super(source,name);
	}

	static private Source[] sources;
}
