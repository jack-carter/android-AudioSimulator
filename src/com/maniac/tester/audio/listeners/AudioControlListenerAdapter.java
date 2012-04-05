/**
 * 
 */
package com.maniac.tester.audio.listeners;

import com.maniac.tester.audio.controls.AudioControl;

/**
 * @author J Carter
 *
 */
public class AudioControlListenerAdapter implements AudioControl.Listener
{
	public void onInitialized(AudioControl c)			{}
	public void onStarted(AudioControl c)				{}
	public void onStopped(AudioControl c)				{}
	public void onReleased(AudioControl c)				{}
	public void onError(AudioControl r, String reason) 	{}	
}
