package com.maniac.tester.audio.values;

import com.maniac.tester.helpers.ValueHolder;

import android.media.AudioRecord;


/**
 * Helper class that converts the AudioRecord recording format (e.g. encoding method)
 * into a human-readable string.
 */
public class RecordingState extends ValueHolder
{
	static public RecordingState valueOf(int status)
	{
		return new RecordingState(status);
	}
	
	static public RecordingState valueOf(AudioRecord r)
	{
		return new RecordingState( r == null ? -1 : r.getRecordingState() );
	}
	
	public RecordingState() {}
	public RecordingState(int status)
	{
		super(status);
	}
	
	public String toString()
	{
		String text = null;
		
		switch (value())
		{
		case AudioRecord.RECORDSTATE_STOPPED:
			text = "STOPPED";
			break;
		case AudioRecord.RECORDSTATE_RECORDING:
			text = "RECORDING";
			break;
		default:
			text = super.toString();
		}
	
		return text;
	}
}