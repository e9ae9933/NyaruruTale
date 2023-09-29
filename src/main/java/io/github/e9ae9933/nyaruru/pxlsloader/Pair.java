package io.github.e9ae9933.nyaruru.pxlsloader;

import org.apache.commons.lang3.tuple.MutablePair;

public class Pair<T, T1> extends MutablePair<T,T1>
{
	public Pair()
	{
	}

	public Pair(T left, T1 right)
	{
		super(left, right);
		this.first=left;
		this.second=right;
	}
	public T first;
	public T1 second;
}
