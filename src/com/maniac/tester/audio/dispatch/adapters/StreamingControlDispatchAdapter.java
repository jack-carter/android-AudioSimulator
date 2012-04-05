/**
 * 
 */
package com.maniac.tester.audio.dispatch.adapters;

import android.os.Handler;

import com.maniac.tester.audio.adapters.StreamingControlAdapter;

/**
 * @author J Carter
 *
 */
public abstract class StreamingControlDispatchAdapter extends StreamingControlAdapter 
{
	@Override
	public void post(Runnable r)
	{
		handler().post(r);
	}
	
	abstract protected Handler handler();
}
