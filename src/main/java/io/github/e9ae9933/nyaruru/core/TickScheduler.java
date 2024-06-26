package io.github.e9ae9933.nyaruru.core;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TickScheduler <T,P extends Comparable<P>>
{
	int ticks=0;
	Map<Integer, List<Pair<P,Consumer<T>>>> scheduledTicks=new LinkedHashMap<>();
	boolean ticking=false;
	public TickScheduler()
	{

	}
	public void tickAndRun(T obj)
	{
		ticking=true;
		List<Pair<P,Consumer<T>>> list=scheduledTicks.remove(ticks);
		if(list!=null)
		{
//			list.sort(Comparator.comparing(Pair::getKey));
			list.sort(Map.Entry.comparingByKey());
			list.forEach(r -> r.getRight().accept(obj));
		}
		ticks++;
		ticking=false;
	}
	public SequencedContinuedConsumer.Builder<T,P> createSequencedBuilder(String name,P priority)
	{
		return new SequencedContinuedConsumer.Builder<T,P>(name,priority,this);
	}
	public void scheduleSequenced(String name,int delay,P priority,Predicate<T>... r)
	{
		SequencedContinuedConsumer.Builder<T,P> b=createSequencedBuilder(name,priority);
		for(Predicate<T> p:r)
			b.add(p);
		scheduleRaw(delay,priority,b.build());
	}
	public void scheduleContinued(String name,int delay,P priority, Predicate<T> r)
	{
		ContinuedConsumer<T,P> consumer=new ContinuedConsumer<>(name,priority,this,r);
		Validate.isTrue(delay>=0);
		Validate.isTrue(!ticking||delay>0,"Concurrent with delay 0");
		if(!scheduledTicks.containsKey(ticks+delay))
			scheduledTicks.put(ticks+delay,new ArrayList<>());
		scheduledTicks.get(ticks+delay).add(new ImmutablePair<>(priority,consumer));
	}
	public void schedule(String name,int delay, P priority, Consumer<T> r)
	{
		OnceConsumer<T> consumer=new OnceConsumer<>(name,r);
		Validate.isTrue(delay>=0);
		Validate.isTrue(!ticking||delay>0,"Concurrent with delay 0");
		if(!scheduledTicks.containsKey(ticks+delay))
			scheduledTicks.put(ticks+delay,new ArrayList<>());
		scheduledTicks.get(ticks+delay).add(new ImmutablePair<>(priority,consumer));
	}
	public void scheduleLoop(String name,int delay,int length, P priority, Consumer<T> r)
	{
		for(int i=0;i<length;i++)
			schedule(name,i+delay,priority,r);
	}
	public void scheduleRaw(int delay, P priority, Consumer<T> r)
	{
		Validate.isTrue(delay>=0);
		Validate.isTrue(!ticking||delay>0,"Concurrent with delay 0");
		if(!scheduledTicks.containsKey(ticks+delay))
			scheduledTicks.put(ticks+delay,new ArrayList<>());
		scheduledTicks.get(ticks+delay).add(new ImmutablePair<>(priority,r));
	}
	public boolean isEmpty()
	{
		return scheduledTicks.isEmpty();
	}
	public void clear()
	{
		scheduledTicks.clear();
	}

	@Deprecated
	public void skip(int ticks)
	{
		// this is only for debug.
		this.ticks+=ticks;
	}
	@Override
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		sb.append("TickScheduler tick %d:".formatted(ticks));
		scheduledTicks.forEach((delay,list)->{
			list.forEach(p->{
				Comparable<?> priority=p.getLeft();
				Consumer<T> consumer=p.getRight();
				sb.append("\n\t%d (%d): priority %s, consumer %s".formatted(delay-ticks,ticks,priority,consumer));
			});
		});
		return  sb.toString();
	}
}
