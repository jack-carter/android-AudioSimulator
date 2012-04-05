/**
 * 
 */
package com.maniac.tester.ui.helpers;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.maniac.tester.helpers.Log;
import com.maniac.testers.R;

/**
 * @author J Carter
 */
public abstract class BasicListActivity extends ListActivity 
{
	/*
	 * Sub-classes must define this.
	 */
	protected abstract ListAdapter adapter();

	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setListAdapter(adapter());
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu _menu)
	{
		getMenuInflater().inflate(optionsMenu(), _menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem _item)
	{
		switch (_item.getItemId())
		{
		case R.id.select: onSelectItems();   break;
		case R.id.done:   onDoneSelecting(); break;
		case R.id.add:    onAdd(); 			 break;
		case R.id.remove: onRemove(); 		 break;
		case R.id.flush:  onFlush();		 break;
		case R.id.trial:  onTimeTrials();    break;
		case R.id.exit:   onExit();          break;
		}
		
		return super.onOptionsItemSelected(_item);
	}

	protected void onSelectItems()   {}
	protected void onDoneSelecting() {}
	protected void onAdd()           {}
	protected void onRemove()        {}
	protected void onFlush()         {}
	protected void onTimeTrials()    {}
	
	protected void onExit()
	{
		Log.d(this,"exiting ...");
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	/**
	 * Sub-classes may re-define this to present a different menu.
	 */
	protected int optionsMenu() { return R.menu.list_options; }
	
	/**
	 * Provided for sub-classes to use if they should wish to.
	 */
	protected <T> ArrayAdapter<T> standardArrayAdapter( List<T> contents )
	{
		return new ArrayAdapter<T>(this,android.R.layout.simple_list_item_1, contents);
	}
}
