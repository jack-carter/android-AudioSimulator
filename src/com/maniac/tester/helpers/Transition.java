/**
 * 
 */
package com.maniac.tester.helpers;

public abstract class Transition
{
	public void onTrue() {}
	public void onFalse() {}
	
	public void check() 
	{ 
		if (edge_occurred) {
			edge_occurred = false;
			
			if (test())
				onTrue();
			else
				onFalse();
		}
	}
	
	public void trigger() 
	{ 
		edge_occurred = true; 
	}

	abstract public boolean test();
	
	private boolean edge_occurred = false;
}