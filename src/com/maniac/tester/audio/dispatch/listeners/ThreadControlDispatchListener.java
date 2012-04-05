/**
 * 
 */
package com.maniac.tester.audio.dispatch.listeners;

import android.os.Handler;

import com.maniac.tester.audio.controls.ThreadControl;
import com.maniac.tester.audio.listeners.ThreadControlListenerAdapter;

/**
 * @author J Carter
 *
 */
public abstract class ThreadControlDispatchListener extends ThreadControlListenerAdapter 
{
	public void onInitialized(final ThreadControl c)
	{
		post(new Runnable() {
			public void run() {
				target.onInitialized(c);
			}});
	}
	
	public void onStarted(final ThreadControl c)
	{
		post(new Runnable() {
			public void run() {
				target.onStarted(c);
			}});
	}
	
	public void onStopped(final ThreadControl c) 
	{
		post(new Runnable() {
			public void run() {
				target.onStopped(c);
			}});

	}
	
	public void onError(final ThreadControl c, final String reason)
	{
		post(new Runnable() {
			public void run() {
				target.onError(c,reason);
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

	private ThreadControl.Listener target;
}
