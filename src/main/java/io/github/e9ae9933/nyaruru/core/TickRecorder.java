package io.github.e9ae9933.nyaruru.core;

import java.util.LinkedList;
import java.util.Queue;

public class TickRecorder
{
	long last;
	final Queue<Long> ticks;
	double lastTickTime;
	public TickRecorder()
	{
		this(5000);
	}
	public TickRecorder(long last)
	{
		this.last=last;
		this.ticks=new LinkedList<>();
	}
	public void tick()
	{
		long now=System.currentTimeMillis();
		if(lastTickTime ==0)
			lastTickTime =now;
		synchronized (ticks)
		{
			while(!ticks.isEmpty()&&ticks.peek()+last<=now)
//				if()
					ticks.poll();
			ticks.add(now);
		}
	}
	public String report()
	{
		synchronized (ticks)
		{
			if (ticks.isEmpty())
				return "No tick data avaliable";
			Long[] l = ticks.toArray(new Long[]{});
			long max = 0;
			for (int i = 1; i < l.length; i++)
				max = Math.max(max, l[i] - l[i - 1]);
			long now=System.currentTimeMillis();
			return "All %d ticks, tps %.2f, average %.2f ms, max %d ms (%.2f tps), mem %d MiB".formatted(
					l.length,
					(l.length-1.0)*1000/(now-l[0]),
					(now-l[0])/(l.length-1.0),
					max,
					1000.0/max,
					(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1048576
			);
		}
	}
	public double getTps()
	{
		synchronized (ticks)
		{
			if(ticks.isEmpty())
				return 0;
			return (ticks.size()-1.0)*1000/(System.currentTimeMillis()-ticks.peek());
		}
	}
	public void waitUntilNextTick(double tps)
	{
		double wait;
		synchronized (ticks)
		{
			if(ticks.isEmpty())
				return;
			long now=System.currentTimeMillis();
			double mspt=1000.0/tps;
			wait=lastTickTime+mspt-now;
			if(wait<0)
			{
				System.err.println("Can't keep up! Is the server overloaded? Running %.1fms or %.1f ticks behind".formatted(
						-wait,
						-wait/tps
				));
				lastTickTime=now;
				return;
			}
			lastTickTime+=mspt;
		}
		try{
			Thread.sleep((long) wait);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
//	public long getNeedSleepUntilTps(double tps)
//	{
//		synchronized (ticks)
//		{
//			if(ticks.isEmpty())
//				return 0;
//			long target= (long) (1000L*(ticks.size()-1)/tps+ticks.peek());
//			return Math.max(0L,target-System.currentTimeMillis());
//		}
//	}
	@Override
	public String toString()
	{
		return super.toString();
	}
}
