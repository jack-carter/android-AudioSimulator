/**
 * 
 */
package com.maniac.tester.audio.adapters;

import com.maniac.tester.audio.controls.AudioControl;

/**
 * @author J Carter
 *
 */
public abstract class AudioControlAdapter extends ListenerControlAdapter<AudioControl.Listener> implements AudioControl 
{
	// AudioControl implementation
	//
	@Override public void init() {}
	@Override public void start() {}
	@Override public void stop() {}
	@Override public void release() {}	
	@Override public boolean isRecording() { return false; }
	
	public void onInitialized() 
	{
		notifyListeners( new Notifier<AudioControl.Listener>() {
			public void notify(final AudioControl.Listener listener) {
				post(new Runnable() {
					public void run() {
						listener.onInitialized(AudioControlAdapter.this);
					}});
				}});
	}

	public void onStarted() 
	{
		notifyListeners( new Notifier<AudioControl.Listener>() {
			public void notify(final AudioControl.Listener listener) {
				post(new Runnable() {
					public void run() {
						listener.onStarted(AudioControlAdapter.this);
					}});
				}});
	}

	public void onStopped() 
	{
		notifyListeners( new Notifier<AudioControl.Listener>() {
			public void notify(final AudioControl.Listener listener) {
				post(new Runnable() {
					public void run() {
						listener.onStopped(AudioControlAdapter.this);
					}});
				}});
	}

	public void onReleased() 
	{
		notifyListeners( new Notifier<AudioControl.Listener>() {
			public void notify(final AudioControl.Listener listener) {
				post(new Runnable() {
					public void run() {
						listener.onReleased(AudioControlAdapter.this);
					}});
				}});
	}

	public void onError(final String reason) 
	{
		notifyListeners( new Notifier<AudioControl.Listener>() {
			public void notify(final AudioControl.Listener listener) {
				post(new Runnable() {
					public void run() {
						listener.onError(AudioControlAdapter.this,reason);
					}});
				}});
	}
	
	protected void post(Runnable post)
	{
		post.run();
	}
}
