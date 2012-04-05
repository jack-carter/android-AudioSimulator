/**
 * 
 */
package com.maniac.tester.audio;

import android.media.AudioRecord;
import android.os.Handler;

import com.maniac.tester.audio.adapters.*;
import com.maniac.tester.audio.controls.StreamingControl;
import com.maniac.tester.audio.controls.ThreadControl;
import com.maniac.tester.audio.values.Channel;
import com.maniac.tester.audio.values.ChannelCount;
import com.maniac.tester.audio.values.Encoding;
import com.maniac.tester.audio.values.ISettings;
import com.maniac.tester.audio.values.MinimumBuffer;
import com.maniac.tester.audio.values.RecordingState;
import com.maniac.tester.audio.values.Sampling;
import com.maniac.tester.audio.values.Source;
import com.maniac.tester.audio.values.State;
import com.maniac.tester.helpers.Log;
import com.maniac.tester.helpers.Transition;

/**
 * This class encapsulates all of the tid-bits necessary to run
 * a streaming reader in the background against an AudioRecord.
 * This can serve as the basis for all other Recording implementations,
 * or simply as a template to follow.
 * 
 * @author J Carter
 */
public class RealRecording extends RecordingAdapter {

	private AudioControlAdapter audioControl;
	private StreamingControlAdapter streamingControl;
	
	{
		audioControl = new AudioControlAdapter() {
			
			public void init()    { my.init();    }
			public void start()   { my.start();   }
			public void stop()    { my.stop();    }
			public void release() { my.release(); }
			
			public boolean isRecording() { return my.isRecording(); }
			
			private RealRecording my = RealRecording.this;
		}; 
		
		streamingControl = new StreamingControlAdapter() {
			
			public boolean isStreaming() { return target().isStreaming(); }
			public Long bytesStreamed()  { return target().bytesStreamed(); }
			public void start() 		 { target().start(); }
			public void stop() 			 { target().stop(); }
			
			private StreamingControl target()
			{
				return my.streamingPolicy.streaming();
			}
			
			private RealRecording my = RealRecording.this;
		};
	}
	
	public RealRecording()
	{
		ISettings settings = Audio.settings();
		
		source		( settings.source() );
		channel		( settings.channel() );
		encoding	( settings.encoding() );
		samplingRate( settings.samplingRate() );
		buffer		( settings.buffer().size() );
		
		state = new State() {
			public int value() {
				return audio == null ? super.value() : audio.getState();
			}
		};
		
		recording = new RecordingState() {
			public int value() {
				return audio == null ? super.value() : audio.getRecordingState();
			}
		};
		
		channelCount = new ChannelCount() {
			public int value() {
				return audio == null ? super.value() : audio.getChannelCount();
			}
		};
	}

	// Audio controls
	
	public AudioControlAdapter audio()			
	{ 
		return audioControl;
	}
	
	// Streaming controls
	
	public StreamingControlAdapter streaming() 
	{ 
		return streamingControl; 
	}
	
	public boolean isActive() 			{ return audio != null; }
	
	public Source source() 				{ return settings().source(); }
	public Channel channel() 			{ return settings().channel(); }
	public Encoding encoding() 			{ return settings().encoding(); }
	public Sampling.Rate samplingRate() { return settings().samplingRate(); }
	
	/*
	 * AUDIO CONTROLS
	 * 
	 * This section serves as private implementations for the 
	 * interfaces which are defined by AudioControl, which we
	 * adhere to.  These are called by a forwarding object,
	 * that serves as an Adapter (see GoF).
	 */
	private void init() 
	{
		Log.d(this,"audio: init()");
		
		try {
			privRelease();
			if ( (audio = Audio.create(this)) == null )
				audio().onError("Could not initialize AudioRecord");
			else
			    audio().onInitialized();

			threadControl.init();
			
		} catch (IllegalArgumentException e) {
			audio().onError(e.toString());
		}
	}
	
	private void start()
	{
		Log.d(this,"audio: start()");
		streamingPolicy.audioTransition.trigger();
	}
	
	private void stop()
	{
		Log.d(this,"audio: stop()");
		streamingPolicy.audioTransition.trigger();
	}
	
	private void release()
	{
		Log.d(this,"audio: release()");		
		threadControl.stop();
		privRelease();
		audio().onReleased();
	}

	private boolean isRecording()
	{
		return (audio != null && audio.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING);
	}

	/**
	 * Helper method since we need to clean-up our AudioRecord instance
	 * from a couple of different places.
	 */
	private void privRelease()
	{
		if (audio != null) {
			audio.release();
			audio = null;    			
		}    		
	}
	
	/**
	 * This method uses a "swivel" to establish how we will respond to requests for
	 * settings information.  We will either respond with the settings we were given
	 * upon construction, or with the present state of the underlying audio device,
	 * depending upon if we've been asked to "showActual" state, or original settings.
	 */
	synchronized private Settings settings()
	{
		return showActual == true && audio != null ? audioState() : superSettings();
	}
	
	/**
	 * As a Recording we maintain the parameters with which to create our AudioRecord
	 * instance.  These are maintained in our superclass, so here we will simply be
	 * looking to our parent for our original settings.
	 */
	private Settings superSettings()
	{
		return new Settings()
		{
			public Source source() 				 { return RealRecording.super.source(); }
			public Channel channel() 			 { return RealRecording.super.channel(); }
			public Encoding encoding() 			 { return RealRecording.super.encoding(); }
			public Sampling.Rate samplingRate()  { return RealRecording.super.samplingRate(); }
			public MinimumBuffer minimumBuffer() { return RealRecording.super.minimumBuffer(); }
		};
	}
	
	/**
	 * When we're asked to return the actual state of the underlying AudioRecord, we'll simply
	 * pull that information directly from our AudioRecord instance, do a bit of conversion to
	 * useful, and strongly-typed, items and send it back.
	 */
	private Settings audioState()
	{
		return new Settings()
		{
			public Source source() 				 { return Source.valueOf(audio.getAudioSource()); }
			public Channel channel() 			 { return Channel.valueOf(audio.getChannelConfiguration()); }
			public Encoding encoding() 			 { return Encoding.valueOf(audio.getAudioFormat()); }
			public Sampling.Rate samplingRate()  { return Sampling.valueOf(audio.getSampleRate()); }
			public MinimumBuffer minimumBuffer() { return settings().minimumBuffer(); }
		};
	}

	/*
	 * A reference to the instance of AudioRecord that we will be using.  We re-create this
	 * during certain sequences, so we will manage it dynamically.
	 */
	private AudioRecord audio;
	
	/*
	 * A reference to the the present background thread that we need.
	 */
	private Thread thread;
	
	/*
	 * This is the instance that controls what the background thread does with the audio.
	 */
	private StreamingPolicy streamingPolicy = new StreamingPolicy();
	
	/*
	 * To ease the interface of dealing with threads, and their lifetime, we will 
	 * devise a simple controller for our thread.
	 */
	private ThreadControl threadControl = new ThreadControlAdapter() {	
		
		public void init() {
			
			if (thread != null) {
				Log.d(this,"streaming: prior thread still running, terminating");
				stop();
			}
			
			streamingPolicy = new StreamingPolicy();
			thread = new Thread(streamingPolicy,"streaming.thread." + Log.ID(this));
			thread.start();			
		}
		
		public void stop() { 
			streamingPolicy.stop(); 
		}
	};

	/**
	 * This class defines a standard boiler-plate for audio processing.  Of interest is that
	 * this implementation separates out (a) control of the underlying AudioRecord instance,
	 * (b) control of the streaming (e.g. reading of data) from that instance, and (c) the
	 * control over the lifetime of the thread itself.  This class is intended to act as a
	 * template that can be extended, or altered, as the need arises.
	 */
	private class StreamingPolicy implements Runnable
	{
		public void stop()
		{
			stopRunning = Boolean.TRUE;
			streaming().stop();
		}
		
		public StreamController streaming()
		{
			return streamControl;
		}
		
		/**
		 * This is the workhorse of this Recording implementation.  The loop it runs
		 * is fairly straight-forward, and establishes a pattern that all Recording
		 * implementations can follow.  This implementation opts for the delegation 
		 * approach where this loop outlines the overall flow of streaming, but
		 * delegates large portions of operations to other components.
		 * 
		 * Of particular note is the use of Transitions and Policies to drive the
		 * desired behavior.
		 */
		public void run()
		{
			Log.d(RealRecording.this,"streaming: launching background thread ...");
			
			while (stopRunning == Boolean.FALSE) 
			{
				audioTransition.check();
				streamingTransition.check();

				if (streaming == Boolean.FALSE) {
					waitPolicy.run();
				} else {
					readPolicy.run();
					throttlingPolicy.run();
				}
			}
			
			Log.d(RealRecording.this,"streaming: terminating background thread ...");
		}

		/*
		 * This serves as suitable handling for sending notifications to listeners, that
		 * may be on the main UI thread.
		 */
		private Handler handler = new Handler();
		
		/*
		 * Simple flag which is used to determine the lifetime of the background thead.
		 */
		private Boolean stopRunning = Boolean.FALSE;
		
		/*
		 * Simple flag which is used to indicate a caller's "intention" for streaming,
		 * which is then used to effectively turn streaming on/off.
		 */
		private Boolean streaming = Boolean.FALSE;

		/*
		 * This controls how the thread will react to requests for start()/stop()
		 * of the streaming (e.g. reading of data), and will also handle notifications
		 * to any listeners that have registered interest.
		 */
		private StreamController streamControl = new StreamController();

		/*
		 * This catches the transition from audio started to audio stopped
		 * and vice versa, notifying our listeners along the way.  It is 
		 * crucial that this be invoked within the context of the streaming
		 * thread as the calls to startRecording()/stop() adjust thread
		 * priorities to keep the read() operation at the same priority
		 * level as the background thread which AudioRecord uses to
		 * capture audio input.
		 */
		public Transition audioTransition = new Transition() {
			
			public void onFalse()  { 
				Log.d(RealRecording.this,"audio: recording");
				audio.startRecording();
				handler.post(new Runnable() {
					public void run() {
						audio().onStarted();
					}});
			}
			
			public void onTrue() {
				Log.d(RealRecording.this,"audio: recording stopped");
				audio.stop();
				handler.post(new Runnable() {
					public void run() {
						audio().onStopped();
					}});
			}
			
			public boolean test() { 
				return audio().isRecording();
			}
			
			public void trigger() {
				super.trigger();
				synchronized (streamControl) {
					streamControl.notifyAll();
				}
			}
		};
		
		/*
		 * This catches the transitions from streaming OFF-to-ON, and ON-to-OFF,
		 * and notifies StreamControl.Listeners.
		 */
		private Transition streamingTransition = new Transition() {
			
			public void onTrue() { 
				handler.post(new Runnable() {
					public void run() {
						streaming().onStarted(); 	
					}});
			}
			
			public void onFalse() { 
				handler.post(new Runnable() {
					public void run() {
						streaming().onStopped(); 
					}});
			}
			
			public boolean test() {
				return streamControl.isStreaming();
			}			
		};
		
		/*
		 * This provides the fine-grained control over streaming of the
		 * audio that is being captured.  This is kept separate from the
		 * control of the underlying audio device itself.
		 */
		private class StreamController extends StreamingControlAdapter
		{
			public boolean isStreaming() {
				return streaming == Boolean.TRUE;
			}
			
			synchronized public void start() {
				Log.d(recording,"streaming.start()");
				streamingTransition.trigger();
				streaming = Boolean.TRUE;
				notifyAll();
			}
			
			synchronized public void stop() { 
				Log.d(recording,"streaming.stop()");
				streamingTransition.trigger();
				streaming = Boolean.FALSE;
				notifyAll();
			}
			
			public void moreBytes(long n) {
				bytesRead += n;
			}
			
			public Long bytesStreamed()
			{
				return bytesRead;
			}
			
			public byte[] buffer()
			{
				return buffer;
			}
			
			// we will forward all notifications through our parent Recording
			
			public void onStarted()  { recording.streaming().onStarted();  }
			public void onStopped()  { recording.streaming().onStopped();  }			
			
			private RealRecording recording = RealRecording.this;
			public long bytesRead;
			
			private byte[] buffer = new byte[100];
		};
		
		/*
		 * this defines the means by which the streaming thread will idle
		 * itself while awaiting input from external callers to start
		 * streaming.
		 */
		private Runnable waitPolicy = new Runnable() {
			public void run() {
				try {
					Log.d(RealRecording.this,"streaming: idle");
					synchronized (streamControl) {
						streamControl.wait();
					}
					Log.d(RealRecording.this,"streaming: active");
				} catch (final InterruptedException e) {
					handler.post(new Runnable() {
						public void run() {
							audio().onError(e.toString());
						}});
				}				
			}};
			
		/*
		 * every streaming thread has its own reading policy to determine
		 * the means by which audio information is read from the AudioRecord.
		 * This defines the means by which this streaming implementation
		 * works, but allows for rapid replacement, even during runtime
		 * operations.
		 */
		private Runnable readPolicy = new Runnable() {

			public void run() {
				try {
				
				final int n = audio.read(streaming().buffer(), 0, streaming().buffer().length);
				
				switch (n)
				{
				case AudioRecord.ERROR_INVALID_OPERATION:
					handler.post(new Runnable() {
						public void run() {
							audio().onError("streaming: ERROR_INVALID_OPERATION");
						}});
					break;
					
				case AudioRecord.ERROR_BAD_VALUE:
					handler.post(new Runnable() {
						public void run() {
							audio().onError("streaming: ERROR_BAD_VALUE");
						}});
					break;
					
				default: // read some data
					handler.post(new Runnable() {
						public void run() {
							streamControl.moreBytes((long)n);
							streaming().onReceived();
						}});
				}
				
				} catch (final Exception e) {
					handler.post(new Runnable() {
						public void run() {
							streaming().onError(e.toString());
						}});
				}				
			}};
		
		/*
		 * some occasions may call for throttling the speed with which
		 * reads are performed on the AudioRecord.  This allows for
		 * differing implementations to alter throttling as circumstances
		 * require, even allowing for dynamically changing the policy at
		 * runtime.
		 */
		private Runnable throttlingPolicy = new Runnable() {
			public void run() {
				// no delay
			}};
	}
}