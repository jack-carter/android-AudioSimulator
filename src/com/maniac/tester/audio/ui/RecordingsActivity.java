/**
 * 
 */
package com.maniac.tester.audio.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.maniac.tester.audio.Audio;
import com.maniac.tester.audio.Recording;
import com.maniac.tester.audio.Recordings;
import com.maniac.tester.ui.helpers.BasicListActivity;
import com.maniac.testers.R;

/**
 * @author J Carter
 */
public class RecordingsActivity extends BasicListActivity 
{
	/**
	 * This defines the entries that go in our list, and 
	 * help to identify each recording with a title.
	 */
	static private class Entry
	{
		public String toString() { return title;}
		
		public Entry title(String title) { this.title = title; return this; }
		public Entry recording(Recording recording) { this.recording = recording; return this; }
		
		String title;
		Recording recording;
	}
	
	private ArrayList<Entry> recordings = new ArrayList<Entry>();
	
	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		
		getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		checkMode();
		
		// if selection mode is off, then show "Add"
		menu.findItem(R.id.add).setVisible( !inSelectionMode() );
		
		// if selection mode is off, and we have more than 1 item show "Select items"		
		menu.findItem(R.id.select).setVisible( !inSelectionMode() && recordings.size() > 0 );
		
		// if selection mode is on, and we have at least 1 item selected, show "Remove"
		menu.findItem(R.id.remove).setVisible( inSelectionMode() && getListView().getCheckedItemCount() > 0);
		
		// if selection mode is on, then only show "Done selecting"
		menu.findItem(R.id.done).setVisible( inSelectionMode() );
		
		// if we have Recordings available to kill, then show "Kill all recordings"
		menu.findItem(R.id.flush).setVisible( !Recordings.getInstance().isEmpty() );
		
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected ArrayAdapter<Entry> adapter()
	{
		int format = getListView().getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE
					? android.R.layout.simple_list_item_multiple_choice
					: android.R.layout.simple_list_item_1;
		
		return new ArrayAdapter<Entry>(this,format, recordings);
	}

	@Override
	protected void onSelectItems()
	{
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		setListAdapter(adapter());
	}
	
	@Override
	protected void onDoneSelecting()
	{
		getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
		setListAdapter(adapter());		
	}
	
	@Override
	protected void onAdd()
	{
		adapter().add( new Entry()
							.title("recording #" + nextRecording())
							.recording(Audio.newRecording()));
		
		setListAdapter(adapter());
	}
	
	@Override
	protected void onRemove()
	{
		SparseBooleanArray POSs = getListView().getCheckedItemPositions();
		
		ArrayList<Entry> removals = new ArrayList<Entry>();
		
		for (int i=0; i < POSs.size(); i++)
			if (POSs.valueAt(i))
				removals.add( (Entry)getListView().getItemAtPosition(POSs.keyAt(i)) );
		
		for (Entry e : removals.toArray( new Entry[0] )) {
			Recordings.getInstance().remove(e.recording);
			e.recording.audio().release();
			adapter().remove(e);
		}
		
		setListAdapter(adapter());
	}
	
	@Override
	protected void onFlush()
	{
		Recordings.flushAll();
		adapter().clear();
		setListAdapter(adapter());
	}
	
	@Override
	protected void onTimeTrials()
	{
		startActivity( new Intent(this,TimeTrialActivity.class) );
	}
	
	@Override
	protected void onListItemClick(ListView _list, View _view, int _position, long _id) 
	{
		if ( !inSelectionMode() )
			open((Entry)getListAdapter().getItem(_position));
	}
	
	private void open(Entry item)
	{
		startActivity( new Intent(this,RecordingActivity.class)
							.putExtra("recording.title", item.title)
							.putExtra("recording.id", item.recording.hashCode())
							);		
	}
	
	private void checkMode()
	{
		// what mode should we be in?
		if ( inSelectionMode() && adapter().getCount() == 0 )
			getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
	}
	
	private boolean inSelectionMode()
	{
		return getListView().getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE;
	}
	
	private int nextRecording()
	{
		return ++nextRecording;
	}
	
	private int nextRecording;
}
