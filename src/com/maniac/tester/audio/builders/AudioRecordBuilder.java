/**
 * 
 */
package com.maniac.tester.audio.builders;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

/**
 * @author J Carter
 *
 */
public class AudioRecordBuilder 
{
	static public int NOT_SET = -1;
	
	public int source() 	   { return source; }
	public int sampleRate()    { return sampleRate; }
	public int channel() 	   { return channel; }
	public int encoding() 	   { return encoding; }
	public int bufferSize()    { return bufferSize == NOT_SET ? minBufferSize() : bufferSize; }
	public int minBufferSize() { return AudioRecord.getMinBufferSize( sampleRate(), channel(), encoding() ); }
	
	public AudioRecordBuilder source(int source) 		 { this.source = source; 		 return this; }
	public AudioRecordBuilder sampleRate(int sampleRate) { this.sampleRate = sampleRate; return this; }
	public AudioRecordBuilder channel(int channel) 		 { this.channel = channel; 		 return this; }
	public AudioRecordBuilder encoding(int encoding) 	 { this.encoding = encoding; 	 return this; }
	public AudioRecordBuilder bufferSize(int bufferSize) { this.bufferSize = bufferSize; return this; }
	
	public AudioRecord create()
	{
		return store(new AudioRecord( source(), sampleRate(), channel(), encoding(), bufferSize() ));
	}
	
	static public void flush()
	{
		Iterator<AudioRecord> iterator = createdRecords.iterator();
		while (iterator.hasNext()) {
			AudioRecord r = iterator.next();
			r.stop();
			r.release();
		}
		createdRecords.clear();
	}
	
	static private AudioRecord store(AudioRecord r)
	{
		createdRecords.add(r);
		return r;
	}
	
	static private Set<AudioRecord> createdRecords = new HashSet<AudioRecord>();
	
	private int source     = MediaRecorder.AudioSource.MIC;
	private int sampleRate = 16000;
	private int channel    = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	private int encoding   = AudioFormat.ENCODING_PCM_16BIT;
	private int bufferSize = NOT_SET;
}
