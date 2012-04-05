/**
 * 
 */
package com.maniac.tester.helpers;

/**
 * @author J Carter
 *
 */
public abstract class Condition 
{
	public boolean isTrue() { return test(); }
	public boolean isFalse() { return !isTrue(); }
	
	protected abstract boolean test();
}
