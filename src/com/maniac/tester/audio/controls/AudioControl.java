/**
 * 
 */
package com.maniac.tester.audio.controls;

/**
 * @author J Carter
 *
 */
public interface AudioControl 
{
	public interface Listener
	{
		public void onInitialized(AudioControl c);
		public void onStarted(AudioControl c);
		public void onStopped(AudioControl c);
		public void onReleased(AudioControl c);
		public void onError(AudioControl r, String reason);
	}
	
	public boolean isRecording();
	
	public void init();
	public void start();
	public void stop();
	public void release();
	
	public void addListener(Listener l);
	public void removeListener(Listener l);
}
