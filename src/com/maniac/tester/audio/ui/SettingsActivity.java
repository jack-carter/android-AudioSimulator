/**
 * 
 */
package com.maniac.tester.audio.ui;

import com.maniac.tester.audio.Audio;
import com.maniac.tester.audio.Recordings;
import com.maniac.tester.audio.Settings;
import com.maniac.tester.audio.values.ISettings;
import com.maniac.tester.helpers.NamedValue;
import com.maniac.testers.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author J Carter
 *
 */
public class SettingsActivity extends Activity implements Settings.ChangeListener
{
	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		setContentView(R.layout.settings);
		
		final Spinner source   = (Spinner)findViewById(R.id.audio_source);
		final Spinner rate     = (Spinner)findViewById(R.id.sample_rate);
		final Spinner channel  = (Spinner)findViewById(R.id.channel_configuration);
		final Spinner encoding = (Spinner)findViewById(R.id.audio_encoding);

		fill( source  , Audio.sources()      , sourceSelection );
		fill( rate    , Audio.samplingRates(), rateSelection );
		fill( channel , Audio.channels()     , channelSelection );
		fill( encoding, Audio.encodings()    , encodingSelection );
		
		source	 .setSelection( indexOf( settings().source().name()		, Audio.sources()));
		rate	 .setSelection( indexOf( settings().samplingRate().name()	, Audio.samplingRates()));
		channel	 .setSelection( indexOf( settings().channel().name()		, Audio.channels()));
		encoding .setSelection( indexOf( settings().encoding().name()		, Audio.encodings()));
		
		final Button save      = (Button)findViewById(R.id.save);
		
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				save();
				finish();
			}
		});

		minSize = (TextView)findViewById(R.id.min_buffer_size);
		bufSize = (TextView)findViewById(R.id.buffer_size);

		bufSize.setText( settings().buffer().toString() );
		
		settings().addChangeListener(this);
	}
	
	@Override
	public void onDestroy()
	{
		settings().removeChangeListener(this);
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    	save();
	    return super.onKeyDown(keyCode, event);
	}
	
	// helper methods
	
	private void fill(Spinner spinner,Object[] objects,AdapterView.OnItemSelectedListener responder)
	{
	    ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_spinner_item,objects);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    spinner.setOnItemSelectedListener(responder);
	}
	
	private <T extends NamedValue> int indexOf(String name, T[] namedValues)
	{
		for (int i=0; i < namedValues.length; i++)
			if (namedValues[i].name().equals(name))
				return i;
		
		return -1;
	}
	
	private void save()
	{
		try {
			settings().buffer( Integer.valueOf(bufSize.getText().toString()) );			
		} catch (Exception e) {
			
		}		
	}
	
	/**
	 * helper class to simplify responses to Spinner selections
	 */
	private abstract class Selection implements AdapterView.OnItemSelectedListener
	{
	    public void onNothingSelected(AdapterView<?> parent) {}
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
	    {
	    	onSelected((int)id);
	    }	    
	    
	    abstract protected void onSelected(int n);
	}
	
	// responses to user selections of the various settings
	
	private Selection sourceSelection = new Selection() {
		protected void onSelected(int n) {
			settings().source(Audio.sources()[n]);
		}
	};
	
	private Selection rateSelection = new Selection() {
		protected void onSelected(int n) {
			settings().samplingRate(Audio.samplingRates()[n]);
		}
	};
	
	private Selection channelSelection = new Selection() {
		protected void onSelected(int n) {
			settings().channel(Audio.channels()[n]);
		}
	};
	
	private Selection encodingSelection = new Selection() {
		protected void onSelected(int n) {
			settings().encoding(Audio.encodings()[n]);
		}
	};
	
	@Override
	public void onChanged(Settings settings)
	{
		minSize.setText( settings.minimumBuffer().toString() );
	}
	
	private ISettings settings()
	{
		if ( settings == null ) {
			int hash = getIntent().getExtras().getInt("recording.id");
	        settings = Recordings.find( hash );
	        
	        if (settings == null)
	        	throw new NullPointerException("could not find Recording with hash of " + hash);
		}
		
		return settings;
	}
	
	private ISettings settings;
	private TextView minSize,bufSize;
}
