/**
 * 
 */
package com.maniac.tester.audio;

import java.util.HashSet;
import java.util.Set;

import com.maniac.tester.audio.values.Buffer;
import com.maniac.tester.audio.values.Channel;
import com.maniac.tester.audio.values.Encoding;
import com.maniac.tester.audio.values.ISettings;
import com.maniac.tester.audio.values.MinimumBuffer;
import com.maniac.tester.audio.values.Sampling;
import com.maniac.tester.audio.values.Source;


/**
 * @author J Carter
 *
 */
public class Settings implements ISettings
{
	static public int BUFFER_NOT_SET = -1;

	@Override
	public void addChangeListener(ChangeListener listener)
	{
		changeListeners.add(listener);
	}
	
	@Override
	public void removeChangeListener(ChangeListener listener)
	{
		changeListeners.remove(listener);
	}
	
	public Source source() { return source; }
	public Channel channel() { return channel; }
	public Encoding encoding() { return encoding; }
	public Sampling.Rate samplingRate() { return samplingRate; }
	public MinimumBuffer minimumBuffer() { return minBuffer; }
	
	public Buffer buffer() { 
		if (buffer.size() == BUFFER_NOT_SET)
			buffer.size(minBuffer.size()); 
		return buffer; 
	}
	
	public void source(Source source) { this.source = source; changed(); }
	public void channel(Channel channel) { this.channel = channel; changed(); }
	public void encoding(Encoding encoding) { this.encoding = encoding; changed(); }
	public void samplingRate(Sampling.Rate rate) { this.samplingRate = rate; changed(); }
	public void buffer(int size) { this.buffer = new Buffer(size); changed(); }
	
	private void changed()
	{
		for (ChangeListener listener: changeListeners.toArray(new ChangeListener[0] ))
			listener.onChanged(this);
	}
	
	private Source source;
	private Channel channel;
	private Encoding encoding;
	private Sampling.Rate samplingRate;
	private Buffer buffer = new Buffer(BUFFER_NOT_SET);
	private MinimumBuffer minBuffer = new MinimumBuffer(this);
	
	private Set<ChangeListener> changeListeners = new HashSet<ChangeListener>();
}
