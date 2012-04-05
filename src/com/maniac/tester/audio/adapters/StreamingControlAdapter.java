/**
 * 
 */
package com.maniac.tester.audio.adapters;

import com.maniac.tester.audio.controls.StreamingControl;

/**
 * @author J Carter
 *
 */
public class StreamingControlAdapter extends ListenerControlAdapter<StreamingControl.Listener> implements StreamingControl 
{
	// StreamingControl implementation
	//
	@Override public boolean isStreaming() { return false; }
	@Override public Long bytesStreamed() { return 0L; }
	@Override public void start() {}
	@Override public void stop() {}
	
	public void onStarted() 
	{
		notifyListeners( new Notifier<StreamingControl.Listener>() {
			public void notify(final StreamingControl.Listener listener) {
				post(new Runnable() {
					public void run() {
						listener.onStarted(StreamingControlAdapter.this);						
					}});
				}});
	}

	public void onReceived() 
	{
		notifyListeners( new Notifier<StreamingControl.Listener>() {
			public void notify(final StreamingControl.Listener listener) {
				post(new Runnable() {
					public void run() {
						listener.onReceived(StreamingControlAdapter.this);						
					}});
				}});
	}

	public void onStopped() 
	{
		notifyListeners( new Notifier<StreamingControl.Listener>() {
			public void notify(final StreamingControl.Listener listener) {
				post(new Runnable() {
					public void run() {
						listener.onStopped(StreamingControlAdapter.this);						
					}});
				}});
	}

	public void onError(final String reason) 
	{
		notifyListeners( new Notifier<StreamingControl.Listener>() {
			public void notify(final StreamingControl.Listener listener) {
				post(new Runnable() {
					public void run() {
						listener.onError(StreamingControlAdapter.this,reason);						
					}});
				}});
	}
	
	protected void post(Runnable post)
	{
		post.run();
	}
}
