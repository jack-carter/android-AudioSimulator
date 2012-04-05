/**
 * 
 */
package com.maniac.tester.audio.controls;

/**
 * @author J Carter
 *
 */
public interface StreamingControl 
{
	public interface Listener
	{
		public void onStarted(StreamingControl c);
		public void onReceived(StreamingControl c);
		public void onStopped(StreamingControl c);
		public void onError(StreamingControl c, String reason);
	}
	
	public boolean isStreaming();
	public Long bytesStreamed();
	
	public void start();
	public void stop();

	public void addListener(Listener l);
	public void removeListener(Listener l);
}
