/**
 * 
 */
package com.maniac.tester.audio.listeners;

import com.maniac.tester.audio.controls.StreamingControl;

/**
 * @author J Carter
 *
 */
public class StreamingControlListenerAdapter implements StreamingControl.Listener
{
	public void onStarted(StreamingControl c)			   {}
	public void onReceived(StreamingControl c)			   {}
	public void onStopped(StreamingControl c)			   {}
	public void onError(StreamingControl c, String reason) {}
}
