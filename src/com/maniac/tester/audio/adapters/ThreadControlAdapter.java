/**
 * 
 */
package com.maniac.tester.audio.adapters;

import com.maniac.tester.audio.controls.ThreadControl;

/**
 * @author J Carter
 *
 */
public class ThreadControlAdapter extends ListenerControlAdapter<ThreadControl.Listener> implements ThreadControl 
{
	@Override public void init()  {}
	@Override public void start() {}
	@Override public void stop()  {}
}
