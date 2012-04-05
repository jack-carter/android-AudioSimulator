/**
 * 
 */
package com.maniac.tester.audio.listeners;

import com.maniac.tester.audio.controls.ThreadControl;

/**
 * @author J Carter
 *
 */
public class ThreadControlListenerAdapter implements ThreadControl.Listener
{
	public void onInitialized(ThreadControl c) 			{}
	public void onStarted(ThreadControl c) 				{}
	public void onStopped(ThreadControl c) 				{}
	public void onError(ThreadControl r, String reason) {}
}
