package com.maniac.tester.audio.ui;

import java.util.Timer;
import java.util.TimerTask;

import com.maniac.tester.audio.Recording;
import com.maniac.tester.audio.Recordings;
import com.maniac.tester.audio.controls.AudioControl;
import com.maniac.tester.audio.controls.StreamingControl;
import com.maniac.tester.helpers.Condition;
import com.maniac.tester.helpers.Log;
import com.maniac.tester.helpers.ValueHolder;
import com.maniac.tester.ui.helpers.*;
import com.maniac.tester.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

/**
 * @author J Carter
 */
public class RecordingActivity extends Activity implements Recording.Listener
{
	static 
	{
		ValueHolder.setUnknownResponse("<no recording>");
	}
	
	private class DisplayFields
	{
        TextView source        ;
        TextView rate          ;
        TextView channel       ;
        TextView encoding      ;
        TextView bufferSize    ;
        TextView minBufferSize ;
        TextView channels  	   ;
        TextView status    	   ;
        TextView recording     ;
        TextView streaming	   ;
        TextView bytes_streamed;
        
        Switch   initAudio	   ;
        Switch	 flowAudio	   ;
        Switch	 readAudio	   ;
        Switch	 realAudio	   ;
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /*** DEPRECATED
        
        // setup the toggle behavior for the activation buttons
        
        final Button go   = (Button)findViewById(R.id.go_button);
        final Button stop = (Button)findViewById(R.id.stop_button);
        
        final Toggle<Button> toggleButton = new Toggle<Button>().idle(go).active(stop);
        
        go.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		toggleButton.start();
        		toggleHeading.start();
        		audio.start();
        	}
        });
        
        stop.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		toggleButton.stop();
        		toggleHeading.stop();
        		audio.stop();
        	}
        });        
		 ***/
        
        /*** DEPRECATED

        // setup toggle behavior for the "listening ..." header
        
        final TextView idle = (TextView)findViewById(R.id.heading_idle);
        final TextView listening = (TextView)findViewById(R.id.heading_listening);
        
        toggleHeading = new Toggle<TextView>().idle(idle).active(listening);
        
         ***/
        
		Log.d(this, "getRecording().isActive() = " + getRecording().isActive() );
        Log.d(this, "getRecording().audio().isRecording() = " + getRecording().audio().isRecording() );
        Log.d(this, "getRecording().streaming().isStreaming() = " + getRecording().streaming().isStreaming() );
        Log.d(this, "getRecording().isActual() = " + getRecording().isActual() );

        // setup the screen title for our use
        
        final TextView title = (TextView)findViewById(R.id.heading_title);
        
        title.setVisibility(View.VISIBLE);
        title.setText(getRecordingTitle());
        
        // map the user controls which we will use
        
        ui.initAudio      = (Switch)findViewById(R.id.init_audio);
        ui.flowAudio      = (Switch)findViewById(R.id.capture_audio);
        ui.readAudio      = (Switch)findViewById(R.id.read_audio);
        ui.realAudio      = (Switch)findViewById(R.id.show_actual);
        
        // initialize our switches with present Recording state
        
		ui.initAudio.setChecked( getRecording().isActive() );
        ui.flowAudio.setChecked( getRecording().audio().isRecording() );
        ui.readAudio.setChecked( getRecording().streaming().isStreaming() );
        ui.realAudio.setChecked( getRecording().isActual() );

        // map the fields in the status panel
        
        ui.source         = (TextView)findViewById(R.id.source);
        ui.rate           = (TextView)findViewById(R.id.sampling_rate);
        ui.channel        = (TextView)findViewById(R.id.channel);
        ui.encoding       = (TextView)findViewById(R.id.encoding);
        ui.bufferSize     = (TextView)findViewById(R.id.buffer_size);
        ui.minBufferSize  = (TextView)findViewById(R.id.min_buffer_size);
        ui.channels       = (TextView)findViewById(R.id.channel_count);
        ui.status         = (TextView)findViewById(R.id.status);
        ui.recording      = (TextView)findViewById(R.id.recording_status);
        ui.streaming      = (TextView)findViewById(R.id.streaming);
        ui.bytes_streamed = (TextView)findViewById(R.id.bytes_streamed);

        // setup touch behavior for the various on/off switches

        final Condition allowInit = new Condition() {
        	protected boolean test() {
        		return !ui.flowAudio.isChecked() && !ui.readAudio.isChecked();
        	}};
        
        ui.initAudio.setOnCheckedChangeListener(new OnCheckedChangeListenerAdapter() {        	
        	public void onChecked () { getRecording().audio().init(); }
        	public void onNotChecked() { getRecording().audio().release(); }        	
        	public void onBoth() {
        		Log.d(this,"switches: initAudio switch");
       			ui.flowAudio.setClickable(isChecked());
    			ui.readAudio.setClickable(isChecked());        		
        	}});
        
        ui.flowAudio.setOnCheckedChangeListener(new OnCheckedChangeListenerAdapter() {
        	public void onChecked() { getRecording().audio().start();}
        	public void onNotChecked() { getRecording().audio().stop(); }
        	public void onBoth() { 
        		ui.initAudio.setClickable( allowInit.isTrue() ); 
        		Log.d(this,"switches: flowAudio switch");
        	}});
        
        ui.readAudio.setOnCheckedChangeListener(new OnCheckedChangeListenerAdapter() {
        	public void onChecked() { getRecording().streaming().start(); }
        	public void onNotChecked() { getRecording().streaming().stop(); }
        	public void onBoth() {
        		Log.d(this,"switches: readAudio switch");	
        	}});
        
        ui.realAudio.setOnCheckedChangeListener(new OnCheckedChangeListenerAdapter() {
			public void onBoth() { 
				getRecording().readActual(isChecked()); 
        		Log.d(this,"switches: realAudio switch");
			}});        
	}
		
	@Override
	public void onResume()
	{
		super.onResume();	

		update();		
		getRecording().addListener(this);
		
		if ( getRecording().streaming().isStreaming() )
			startByteCounter();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		getRecording().addListener(this);
		stopByteCounter();
	}
	
	@Override
	public void onDestroy()
	{
        ui.initAudio.setOnCheckedChangeListener(null);       	
        ui.flowAudio.setOnCheckedChangeListener(null);
        ui.readAudio.setOnCheckedChangeListener(null);
        ui.realAudio.setOnCheckedChangeListener(null);
        
        super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.options, menu);
		return super.onCreateOptionsMenu(menu);			
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		menu.findItem(R.id.settings).setVisible(!getRecording().isActive());
		menu.findItem(R.id.no_settings).setVisible(getRecording().isActive());
		
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.kill:
			getRecording().audio().release();
			break;
			
		case R.id.settings: 
			startActivity( new Intent(this,SettingsActivity.class).putExtra("recording.id", getRecording().hashCode()) );
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	/*
	 * AudioControl.Listener implementation
	 */
	
	@Override public void onInitialized(AudioControl c) 
	{ 
		update("initialized"); 
	}
	
	@Override public void onStarted(AudioControl c) 
	{ 
		update("started");  
		
		/*** DEPRECATED
		toggleHeading.start();
		 ***/
	}
	
	@Override public void onStopped(AudioControl audio) 
	{ 
		update("stopped");
		
		/*** DEPRECATED
		toggleHeading.stop();
		 ***/
	}
	
	@Override public void onReleased(AudioControl audio) 
	{ 
		update("released");    
	}
	
	@Override public void onError(AudioControl audio, String reason)
	{ 
		Log.d(null,reason);
		update(reason);
	}
		
	/*
	 * StreamingControl.Listener implementation
	 */
	
	private Timer byteCounter;
	
	@Override public void onStarted(StreamingControl c)
	{
		startByteCounter();
		update("streaming");		
	}
	
	@Override public void onReceived(StreamingControl streaming)
	{ 
		update("changed");
	}
	
	@Override public void onStopped(StreamingControl streaming)
	{
		stopByteCounter();
		update("streaming halted");		
	}
	
	@Override public void onError(StreamingControl audio, String reason)
	{ 
		Log.d(null,reason);
		update(reason);
	}
		
	/*
	 * Implementation support for notifications and visual updates
	 */
	
	private void update(String message)
	{
		Log.d(this, message);
		update();		
	}
	
	private void update()
	{
		Recording audio = getRecording();
		
        ui.source		 .setText( audio.source().name() );
        ui.rate		     .setText( audio.samplingRate().name() );
        ui.channel		 .setText( audio.channel().name() );
        ui.encoding	     .setText( audio.encoding().name() );
        ui.bufferSize	 .setText( audio.buffer().toString() );
        ui.minBufferSize .setText( audio.minimumBuffer().toString() );
        
        ui.channels      .setText( audio.isActive() ? audio.channelCount().toString() : "<no recording>");
        ui.status        .setText( audio.isActive() ? audio.state().toString() : "<no recording>");
        ui.recording     .setText( audio.isActive() ? audio.recording().toString() : "<no recording>");
        ui.streaming     .setText( audio.isActive() ? (audio.streaming().isStreaming() ? "YES" : "NO") : "<no recording>" );        
        ui.bytes_streamed.setText( audio.isActive() ? audio.streaming().bytesStreamed().toString() : "<no recording>");
	}

	private String getRecordingTitle()
	{
        String recordingTitle = getIntent().getExtras().getString("recording.title");
        return (recordingTitle == null ? "Recording Information" : "Recording: " + recordingTitle);	
	}
	
	private Recording getRecording()
	{
		if ( audio == null ) {
			int hash = getIntent().getExtras().getInt("recording.id");
	        audio = Recordings.find( hash );
	        
	        if (audio == null)
	        	throw new NullPointerException("could not find Recording with hash of " + hash);
		}
		
		return audio;
	}
	
	private void startByteCounter()
	{
		if (byteCounter != null)
			byteCounter.cancel();
		
		byteCounter = new Timer("byte.counter");
		byteCounter.schedule( new TimerTask() {
			Handler handler = new Handler();
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						ui.bytes_streamed.setText( Long.toString(getRecording().streaming().bytesStreamed()) );					
					}});
			}}, 250, 500 );		
	}
	
	private void stopByteCounter()
	{
		if (byteCounter != null)
			byteCounter.cancel();
	}
	
	private DisplayFields ui = new DisplayFields();
    private Recording audio;
    
    /*** DEPRECATED
    private Toggle<TextView> toggleHeading;
     ***/
}