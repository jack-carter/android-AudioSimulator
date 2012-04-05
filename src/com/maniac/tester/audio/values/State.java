package com.maniac.tester.audio.values;

import com.maniac.tester.helpers.ValueHolder;

import android.media.AudioRecord;


/**
 * Helper class that converts the AudioRecord.getState() result (e.g. initialization state)
 * into a human-readable string.
 */
public class State extends ValueHolder
{
	static public State valueOf(int state)
	{
		return new State(state);
	}
	
	static public State valueOf(AudioRecord r)
	{
		return new State( r == null ? -1 : r.getState() );
	}
	
	public State() {}
	public State(int state)
	{
		super(state);
	}
	
	public String toString()
	{
		String text = null;
		
		switch (value())
		{
		case AudioRecord.STATE_UNINITIALIZED:
			text = "UNINITIALIZED";
			break;
		case AudioRecord.STATE_INITIALIZED:
			text = "INITIALIZED";
			break;
		default:
			text = super.toString();
		}
	
		return text;
	}
}