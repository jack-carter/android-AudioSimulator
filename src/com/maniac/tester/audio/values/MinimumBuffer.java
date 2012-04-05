package com.maniac.tester.audio.values;

import com.maniac.tester.audio.Settings;

import android.media.AudioRecord;

public class MinimumBuffer
{
	public MinimumBuffer(Settings settings) {
		this.settings = settings;
	}

	public int size()
	{
		return AudioRecord.getMinBufferSize(
				settings.samplingRate().value(),
				settings.channel().value(),
				settings.encoding().value()
				); 
	}
	
	@Override
	public String toString()
	{
		String text = null;
		
		switch (size())
		{
		case AudioRecord.ERROR:
			text = "ERROR";
			break;
			
		case AudioRecord.ERROR_BAD_VALUE:
			text = "ERROR_BAD_VALUE";
			break;
			
		default:
			text = Integer.toString(size());
		}
		
		return text;
	}		

	private final Settings settings;
}