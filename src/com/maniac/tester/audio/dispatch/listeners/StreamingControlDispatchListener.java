/**
 * 
 */
package com.maniac.tester.audio.dispatch.listeners;

import android.os.Handler;

import com.maniac.tester.audio.controls.StreamingControl;
import com.maniac.tester.audio.listeners.StreamingControlListenerAdapter;

/**
 * @author J Carter
 *
 */
public abstract class StreamingControlDispatchListener extends StreamingControlListenerAdapter 
{
	public void onStarted(final StreamingControl c) {
		post(new Runnable() {
			public void run() {
				target().onStarted(c);
			}});
	}

	public void onReceived(final StreamingControl c) {
		post(new Runnable() {
			public void run() {
				target().onReceived(c);
			}});
	}
	
	public void onStopped(final StreamingControl c) {
		post(new Runnable() {
			public void run() {
				target().onStopped(c);
			}});
	}
	
	public void onError(final StreamingControl c, final String reason) {
		post(new Runnable() {
			public void run() {
				target().onError(c,reason);
			}});
	}
	
	private void post(Runnable r)
	{
		handler().post(r);
	}
	
	/*
	 * sub-classes provide their own implementation of what Handler
	 * to use.
	 */
	abstract protected Handler handler();
	
	abstract protected StreamingControl.Listener target();
}
