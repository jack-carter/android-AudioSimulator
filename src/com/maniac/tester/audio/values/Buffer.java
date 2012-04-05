/**
 * 
 */
package com.maniac.tester.audio.values;

import com.maniac.tester.audio.Settings;

public class Buffer
{
	public Buffer(int size)
	{
		this.size = size;
	}
	
	public int size()
	{
		return size;
	}
	
	public Buffer size(int size)
	{
		this.size = size;
		return this;
	}
	
	@Override
	public String toString()
	{
		return Integer.toString(size());
	}
	
	private int size = Settings.BUFFER_NOT_SET;
}