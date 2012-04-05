/**
 * 
 */
package com.maniac.tester.helpers;

/**
 * @author J Carter
 *
 */
public class Log 
{
	static public void d(Object o, Object msg)
	{
		android.util.Log.d("AudioSimulator", "[" + ID(o) + "] " + msg.toString());
	}
	
	static public String ID(Object o)
	{
		return ("" + Integer.toString(o.hashCode(),16)).toUpperCase();
	}
}
