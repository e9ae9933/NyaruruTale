package io.github.e9ae9933.nyaruru.core;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class OnceConsumer<T> implements Consumer<T>
{
	String name;
	Consumer<T> consumer;

	public OnceConsumer(String name, Consumer<T> consumer)
	{
		this.name = name;
		this.consumer=consumer;
	}

	@Override
	public void accept(T t)
	{
		consumer.accept(t);
	}

	@Override
	public String toString()
	{
		return "OnceConsumer (%s)".formatted(name);
	}
}
