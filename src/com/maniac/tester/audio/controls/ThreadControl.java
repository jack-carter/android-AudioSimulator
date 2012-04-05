/**
 * 
 */
package com.maniac.tester.audio.controls;

/**
 * @author J Carter
 *
 */
public interface ThreadControl 
{
	public interface Listener
	{
		public void onInitialized(ThreadControl c);
		public void onStarted(ThreadControl c);
		public void onStopped(ThreadControl c);
		public void onError(ThreadControl r, String reason);
	}
	
	public void init();
	public void start();
	public void stop();
	
	public void addListener(Listener l);
	public void removeListener(Listener l);
}
