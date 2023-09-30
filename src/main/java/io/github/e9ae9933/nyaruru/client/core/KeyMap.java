package io.github.e9ae9933.nyaruru.client.core;

import java.util.LinkedHashMap;
import java.util.Map;

public class KeyMap extends LinkedHashMap<Integer,Integer>
{
	public KeyMap()
	{
		super();
	}

	public KeyMap(Map<? extends Integer, ? extends Integer> m)
	{
		super(m);
	}

	public KeyMap(int initialCapacity, float loadFactor, boolean accessOrder)
	{
		super(initialCapacity, loadFactor, accessOrder);
	}

	public KeyMap(int initialCapacity, float loadFactor)
	{
		super(initialCapacity, loadFactor);
	}

	public KeyMap(int initialCapacity)
	{
		super(initialCapacity);
	}

	public boolean isFirstPressed(int key)
	{
		return this.getOrDefault(key,-1)==0;
	}

	public boolean isFirstPressedAndRemove(int key)
	{
		Integer i=this.get(key);
		if(i==null)
			return false;
		this.remove(key);
		return i==0;
	}
	public boolean isPressed(int key)
	{
		return this.containsKey(key);
	}
}
