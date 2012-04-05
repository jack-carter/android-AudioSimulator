/**
 * 
 */
package com.maniac.tester.audio;

import java.util.HashSet;
import java.util.Iterator;

/**
 * @author J Carter
 *
 */
public class Recordings extends HashSet<Recording>
{
	static final long serialVersionUID = 0L;
	
	static public Recordings getInstance()
	{
		if (recordings == null) {
			recordings = new Recordings();
		}
		
		return recordings;
	}

	static public Recording newRecording()
	{
		Recording r = new RealRecording();
		recordings.add(r);
		return r;
	}
	
	static public Recording find(int byHash)
	{
		Iterator<Recording> iterator = recordings.iterator();
		while (iterator.hasNext()) {
			Recording r = iterator.next();
			if (r.hashCode() == byHash)
				return r;
		}
		
		return null;
	}

	/**
	 * Kill every recording we presently have in inventory.
	 */
	static public void flushAll()
	{
		Iterator<Recording> iterator = Recordings.getInstance().iterator();
		while (iterator.hasNext())
			iterator.next().audio().release();
		Recordings.getInstance().clear();
	}
	
	/**
	 * There will only ever be 1 instance of this class, so
	 * we'll hide the constructor to keep it that way.
	 */
	private Recordings() {}
	
	static private Recordings recordings = new Recordings();
}
