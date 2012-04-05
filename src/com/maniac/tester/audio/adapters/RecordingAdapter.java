/**
 * 
 */
package com.maniac.tester.audio.adapters;

import com.maniac.tester.audio.Recording;
import com.maniac.tester.audio.Settings;
import com.maniac.tester.audio.controls.AudioControl;
import com.maniac.tester.audio.controls.StreamingControl;
import com.maniac.tester.audio.values.ChannelCount;
import com.maniac.tester.audio.values.RecordingState;
import com.maniac.tester.audio.values.State;

public abstract class RecordingAdapter extends Settings implements Recording
{
	public State state() 				{ return state; }
	public RecordingState recording()	{ return recording; }
	public ChannelCount channelCount() 	{ return channelCount; }
	
	abstract public AudioControl audio();
	abstract public StreamingControl streaming();
	
	public boolean isActual()			{ return showActual; }
	
	public void readActual(boolean showActual)
	{
		this.showActual = showActual;
	}

	/*
	 * Listener management
	 */
	
	public void addListener(Recording.Listener listener)
	{
		audio().addListener((AudioControl.Listener)listener);
		streaming().addListener((StreamingControl.Listener)listener);
	}
	
	public void removeListener(Recording.Listener listener)
	{
		audio().removeListener((AudioControl.Listener)listener);
		streaming().removeListener((StreamingControl.Listener)listener);
	}
	
	/* 
	 * sub-classes may replace these implementations with ones that perform some form of
	 * actual status gathering.  These are provided merely to give human-readable results.
	 */
	protected State state = new State();
	protected RecordingState recording = new RecordingState();
	protected ChannelCount channelCount = new ChannelCount();
	
	/*
	 * a toggle to tell us if the caller wants us to return the settings we were created
	 * with, or the settings which are actually in the initialized recording.
	 */
	protected boolean showActual;	
}