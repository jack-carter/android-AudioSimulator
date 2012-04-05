/**
 * 
 */
package com.maniac.tester.audio.adapters;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author J Carter
 *
 */
public class ListenerControlAdapter<T>
{
	public interface Notifier<T>
	{
		public void notify(T listener);
	}
	
	public void addListener(T t) 
	{
		listeners.add(t);
	}
	
	public void removeListener(T t) 
	{
		listeners.remove(t);
	}
	
	public void notifyListeners(Notifier<T> notifier)
	{
		Iterator<T> listener = listeners.iterator();
		while (listener.hasNext())
			notifier.notify(listener.next());
	}
	
	private Set<T> listeners = new HashSet<T>();
}
