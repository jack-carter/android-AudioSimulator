/**
 * 
 */
package com.maniac.tester.ui.helpers;

import android.view.View;

public class Toggle<T extends View>
{
	private T idle, active;
	
	public void start() 
	{
		idle.setVisibility(View.GONE);
		active.setVisibility(View.VISIBLE);        		
	}
	
	public void stop() 
	{
		idle.setVisibility(View.VISIBLE);
		active.setVisibility(View.GONE);
	}
	
	public Toggle<T> idle(T idle)
	{
		this.idle = idle;
		return this;
	}
	
	public Toggle<T> active(T active)
	{
		this.active = active;
		return this;
	}
}