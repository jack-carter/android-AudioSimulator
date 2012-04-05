/**
 * 
 */
package com.maniac.tester.ui.helpers;

import android.widget.CompoundButton;

/**
 * @author J Carter
 *
 */
public class OnCheckedChangeListenerAdapter implements CompoundButton.OnCheckedChangeListener
{
	public void onCheckedChanged (CompoundButton buttonView, boolean isChecked) {
		
		checked = isChecked;
		
		onBoth();
		
		if (isChecked)
			onChecked();
		else
			onNotChecked();
	}
	
	protected void onBoth() {}
	protected void onChecked() {}
	protected void onNotChecked() {}
	
	protected boolean isChecked() { return checked; }
	
	private boolean checked;
}
