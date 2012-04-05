/**
 * 
 */
package com.maniac.tester.audio.dispatch.listeners;

import android.os.Handler;

import com.maniac.tester.audio.controls.AudioControl;
import com.maniac.tester.audio.listeners.AudioControlListenerAdapter;

/**
 * This class provides a simple mechanism whereby a notifier will notify the
 * penultimate listener via a dispatch mechanism with an appropriate Handler
 * and post() operations.  The intention of this class is to hide such an 
 * implementation from the instance that is performing the notification.
 * 
 * @author J Carter
 */
public abstract class AudioControlDispatchListener extends AudioControlListenerAdapter 
{
	public void onInitialized(final AudioControl c)
	{
		post(new Runnable() {
			public void run() {
				target().onInitialized(c);
			}});
	}
	
	public void onStarted(final AudioControl c)
	{
		post(new Runnable() {
			public void run() {
				target().onStarted(c);
			}});		
	}
	
	public void onStopped(final AudioControl c)
	{
		post(new Runnable() {
			public void run() {
				target().onStopped(c);
			}});
	}
	
	public void onReleased(final AudioControl c)
	{
		post(new Runnable() {
			public void run() {
				target().onReleased(c);
			}});
	}
	
	public void onError(final AudioControl c, final String reason)
	{
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
	
	abstract protected AudioControl.Listener target();
}
