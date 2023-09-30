package io.github.e9ae9933.nyaruru.core;

import org.apache.commons.lang3.Validate;

import java.util.*;
import java.util.function.Consumer;

public class TickScheduler <T>
{
	int ticks=0;
	Map<Integer, Set<Consumer<T>>> scheduledTicks=new LinkedHashMap<>();
	public TickScheduler()
	{

	}
	public void tickAndRun(T obj)
	{
		Set<Consumer<T>> list=scheduledTicks.remove(ticks);
		if(list!=null)
			list.forEach(r->r.accept(obj));
		ticks++;
	}
	public void schedule(int delay,Consumer<T> r)
	{
		Validate.isTrue(delay>=0);
		if(!scheduledTicks.containsKey(ticks+delay))
			scheduledTicks.put(ticks+delay,new HashSet<>());
		scheduledTicks.get(ticks+delay).add(r);
	}
	public void clear()
	{
		scheduledTicks.clear();
	}
}
