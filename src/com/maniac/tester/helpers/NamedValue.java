/**
 * 
 */
package com.maniac.tester.helpers;

/**
 * @author J Carter
 *
 */
public class NamedValue
{
	public NamedValue() {}
	public NamedValue(int value, String name)
	{
		this.value = value;
		this.name = name;
	}
	
	public int value() { return value; }
	public String name() { return name; }
	
	public NamedValue value(int value)
	{
		this.value = value;
		return this;
	}
	
	public NamedValue name(String name)
	{
		this.name = name;
		return this;
	}
	
	public String toString()
	{
		return name();
	}
	
	private int value;
	private String name;
}
