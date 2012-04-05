/**
 * 
 */
package com.maniac.tester.audio.ui;

import java.util.ArrayList;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.maniac.tester.audio.builders.AudioRecordBuilder;
import com.maniac.tester.audio.values.RecordingState;
import com.maniac.tester.audio.values.State;
import com.maniac.tester.helpers.*;
import com.maniac.tester.ui.helpers.BasicListActivity;

/**
 * @author J Carter
 *
 */
public class TimeTrialActivity extends BasicListActivity 
{
	static abstract public class TimeTrial implements Runnable
	{
		public TimeTrial(String title) { this.title = title; }
		public String title() { return title; }
		public String toString() { return title; }
		
		public void run() { 
			go(); 
			clean();
		}
		
		public ArrayList<AudioRecord> records()
		{
			return records;
		}
		
		private void clean()
		{
			AudioRecordBuilder.flush();
		}
		
		abstract protected void go();
		
		private String title;
		private ArrayList<AudioRecord> records = new ArrayList<AudioRecord>();
	}
	
	protected ArrayAdapter<TimeTrial> adapter()
	{
		return standardArrayAdapter(getTimeTrials());
	}
	
	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		setTitle("Time Trials");
	}
	
	@Override
	protected void onListItemClick(ListView _list, View _view, int _position, long _id) 
	{
		perform((TimeTrial)getListAdapter().getItem(_position));
	}
	
	private void perform(TimeTrial trial)
	{
		try { new Thread(trial,"time.trial").start(); } catch (Exception e) { Log.d(this, "EXCEPTION: " + e.toString()); }		
	}

	private ArrayList<TimeTrial> getTimeTrials()
	{
		return trials;
	}
	
	static private AudioRecordBuilder builder = new AudioRecordBuilder()
		.source(MediaRecorder.AudioSource.MIC)
		.sampleRate(16000)
		.channel(AudioFormat.CHANNEL_CONFIGURATION_MONO)
		.encoding(AudioFormat.ENCODING_PCM_16BIT);		

	static private ArrayList<TimeTrial> trials = new ArrayList<TimeTrial>();
	
	static
	{
		trials.add(new TimeTrial("How many can we create?") {
			public void go() {
				ArrayList<AudioRecord> records = new ArrayList<AudioRecord>();
				while (records.size() < 1000) {
					AudioRecord record = builder.create();
					records.add(record);
					Log.d(this, "initializing record #" + records.size() + ", getState()=" + State.valueOf(record));
				}
				for (AudioRecord r : records.toArray(new AudioRecord[0]))
					r.release();
			}});

		trials.add(new TimeTrial("What if we always release?") {
			public void go() {
				while (records().size() < 1000) {
					AudioRecord record = builder.create();
					records().add(record);
					Log.d(this, "initializing record #" + records().size() + ", getState()=" + State.valueOf(record));
					record.release();
				}
			}});

		trials.add(new TimeTrial("How many can we start up?") {
			public void go() {
				while (records().size() < 10) {
					AudioRecord record = builder.create();
					record.startRecording();
					Log.d(this, "starting record #" + records().size() + ", getRecordingState()=" + RecordingState.valueOf(record));
					records().add(record);
				}
				
				for (int i=0; i < records().size(); i++) {
					Log.d(this, "record(" + i + ").getRecordingStatus() = " + RecordingState.valueOf(records().get(i)));
					records().get(i).stop();
				}
			}});
		
		trials.add(new TimeTrial("Can a 2nd start immediately?") {
			public void go() {
				AudioRecord 
					r1 = builder.create(),
					r2 = builder.create();
				
				r1.startRecording();
				r1.stop();
				r2.startRecording();
				
				Log.d(this,"r1.getRecordingState()=" + RecordingState.valueOf(r1));
				Log.d(this,"r2.getRecordingState()=" + RecordingState.valueOf(r2));
				
				r2.stop();
			}});

		trials.add(new TimeTrial("Can I release() more than once?") {
			public void go() {
				AudioRecord r1 = builder.create();
				Log.d(this,"r1.getState()="+State.valueOf(r1));
				r1.release();
				Log.d(this,"r1.getState()="+State.valueOf(r1));
				r1.release();
				Log.d(this,"r1.getState()="+State.valueOf(r1));
			}});
	}	
}
