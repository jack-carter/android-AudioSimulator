/**
 * 
 */
package com.maniac.tester.audio.values;

import com.maniac.tester.audio.Settings;

/**
 * @author J Carter
 *
 */
public interface ISettings 
{
	public interface ChangeListener
	{
		public void onChanged(Settings settings);
	}
	
	public Source source();
	public Channel channel();
	public Encoding encoding();
	public Sampling.Rate samplingRate();
	public MinimumBuffer minimumBuffer();
	public Buffer buffer();

	public void source(Source source);
	public void channel(Channel channel);
	public void encoding(Encoding encoding);
	public void samplingRate(Sampling.Rate rate);
	public void buffer(int size);

	public void addChangeListener(ChangeListener listener);
	public void removeChangeListener(ChangeListener listener);
}
