package io.github.e9ae9933.nyaruru.core;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SequencedContinuedConsumer <T,P extends Comparable<P>> extends ContinuedConsumer<T,P>
{
	SequencedContinuedConsumer<T,P> next;
	public SequencedContinuedConsumer(String name, P priority, TickScheduler<T, P> tickScheduler, Predicate<T> predicate,SequencedContinuedConsumer<T,P> next)
	{
		super(name, priority, tickScheduler, predicate);
		this.next=next;
	}

	@Override
	public void accept(T t)
	{
		ticks++;
		if(predicate.test(t))
			tickScheduler.scheduleRaw(1, priority, this);
		else if(next!=null) tickScheduler.scheduleRaw(1,next.priority,next);
	}
//	public static<T,P extends Comparable<P>> Builder<T,P> createBuilder(String name,P priority,TickScheduler<T,P> tickScheduler)

	@Override
	public String toString()
	{
		StringJoiner sb=new StringJoiner(", ","SequencedContinuedConsumer %d: [","]");
		SequencedContinuedConsumer<T,P> ptr=this;
		while(ptr!=null)
		{
			sb.add(ptr.name);
			ptr=ptr.next;
		}
		return sb.toString();
	}
	public static class Builder<T,P extends Comparable<P>>
	{
		List<Predicate<T>> list=new ArrayList<>();
		String name;
		P priority;
		TickScheduler<T,P> tickScheduler;
		Builder(String name, P priority, TickScheduler<T, P> tickScheduler){this.name=name;this.priority=priority;this.tickScheduler=tickScheduler;}
		public void add(Predicate<T> predicate)
		{
			list.add(predicate);
		}
		public void addOnce(Consumer<T> consumer)
		{
			add(new Predicate<T>()
			{
				@Override
				public boolean test(T t)
				{
					consumer.accept(t);
					return false;
				}
			});
		}
		public void delay(int delay)
		{
			Validate.isTrue(delay>=1);
			list.add(new Predicate<T>()
			{
				int ticks=0;
				@Override
				public boolean test(T t)
				{
					return ++ticks<delay;
				}
			});
		}
		public SequencedContinuedConsumer<T,P> build()
		{
			SequencedContinuedConsumer<T,P> last=null;
			for(int ptr=list.size()-1;ptr>=0;ptr--)
			{
				last=new SequencedContinuedConsumer<>(
						"%s (%d)".formatted(name,ptr),
						priority,
						tickScheduler,
						list.get(ptr),
						last
				);
			}
			return last;
		}
	}
}
