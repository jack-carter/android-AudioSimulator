package com.maniac.tester.helpers;

/**
 * Simple base class to give a numeric value a human-readable name
 */
public class ValueHolder
{
	public ValueHolder() {}
	public ValueHolder(int value) { this.value = value; }
	
	public int value()
	{
		return value;
	}
	
	public String toString()
	{
		return value() == -1 ? unknown : Integer.toString(value());
	}
	
	static public void setUnknownResponse(String _unknown) { 
		unknown = _unknown;
	}
	
	private int value = -1;
	static private String unknown = "<unknown>";
}