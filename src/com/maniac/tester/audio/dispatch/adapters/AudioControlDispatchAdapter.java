/**
 * 
 */
package com.maniac.tester.audio.dispatch.adapters;

import android.os.Handler;

import com.maniac.tester.audio.adapters.AudioControlAdapter;

/**
 * @author J Carter
 *
 */
public abstract class AudioControlDispatchAdapter extends AudioControlAdapter
{
	@Override
	protected void post(Runnable r)
	{
		handler().post(r);
	}
	
	abstract protected Handler handler();
}
