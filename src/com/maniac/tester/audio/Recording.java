/**
 * 
 */
package com.maniac.tester.audio;

import com.maniac.tester.audio.controls.AudioControl;
import com.maniac.tester.audio.controls.RecordingControl;
import com.maniac.tester.audio.controls.StreamingControl;
import com.maniac.tester.audio.values.Buffer;
import com.maniac.tester.audio.values.Channel;
import com.maniac.tester.audio.values.ChannelCount;
import com.maniac.tester.audio.values.Encoding;
import com.maniac.tester.audio.values.ISettings;
import com.maniac.tester.audio.values.MinimumBuffer;
import com.maniac.tester.audio.values.RecordingState;
import com.maniac.tester.audio.values.Sampling;
import com.maniac.tester.audio.values.Source;
import com.maniac.tester.audio.values.State;

/**
 * @author J Carter
 *
 */
public interface Recording extends ISettings, RecordingControl
{
	public interface Listener extends AudioControl.Listener, StreamingControl.Listener
	{	
	}
	
	public boolean isActive();
	public boolean isActual();
	
	public Source source();
	public Channel channel();
	public Encoding encoding();
	public Sampling.Rate samplingRate();
	public MinimumBuffer minimumBuffer();
	public Buffer buffer();
	
	public State state();
	public RecordingState recording();
	public ChannelCount channelCount();
	public StreamingControl streaming();

	public void readActual(boolean readActual);

	public void addListener(Listener listener);
	public void removeListener(Listener listener);
}
