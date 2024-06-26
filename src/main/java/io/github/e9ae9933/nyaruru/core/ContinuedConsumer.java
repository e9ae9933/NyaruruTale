package io.github.e9ae9933.nyaruru.core;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ContinuedConsumer <T,P extends Comparable<P>> implements Consumer<T>
{
	String name;
	P priority;
	TickScheduler<T,P> tickScheduler;
	Predicate<T> predicate;
	int ticks;

	public ContinuedConsumer(String name, P priority, TickScheduler<T,P> tickScheduler, Predicate<T> predicate)
	{
		this.name = name;
		this.priority = priority;
		this.tickScheduler = tickScheduler;
		this.predicate = predicate;
	}

	@Override
	public void accept(T t)
	{
		ticks++;
		if(predicate.test(t))
			tickScheduler.scheduleRaw(1, priority, this);
	}

	@Override
	public String toString()
	{
		return "ContinuedConsumer (%s) %d".formatted(name,ticks);
	}
}
