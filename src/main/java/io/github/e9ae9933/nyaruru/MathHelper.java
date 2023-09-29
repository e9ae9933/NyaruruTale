package io.github.e9ae9933.nyaruru;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

public class MathHelper
{
	public static final double PI=Math.PI;
	public static int round(double f)
	{
		return (int) Math.round(f);
	}
	public static int floor(double f)
	{
		return (int)Math.floor(f);
	}
	public static int clamp(int x,int l,int r)
	{
		return x<l?l:x>r?r:x;
	}
	public static short round255(float f)
	{
		return (short)clamp((int) (f+0.5f),0,255);
	}
	public static double distance(int x1,int y1,int x2,int y2)
	{
		return Math.sqrt((x1-x2)*1l*(x1-x2)+(y1-y2)*1l*(y1-y2));
	}
	public static double sin(double x)
	{
		return Math.sin(x);
	}
	public static double cos(double x)
	{
		return Math.cos(x);
	}
	public static double tan(double x)
	{
		return Math.tan(x);
	}
	public static double atan2xy(double x,double y)
	{
		return Math.atan2(y,x);
	}
	public static int floorMod(int x,int y)
	{
		return Math.floorMod(x,y);
	}
	public static int rangeIncrease(int x,int high)
	{
		return x+1>=high?0:x+1;
	}
	public static int rangeIncrease(int x,int high,Runnable r)
	{
		if(x+1>=high)
			r.run();
		return x+1>=high?0:x+1;
	}
	public static int rangeIncrease(int x, int high, IntSupplier s)
	{
		if(x+1>=high)
			return s.getAsInt();
		return x+1>=high?0:x+1;
	}
	public static int rangeDecrease(int x,int high)
	{
		return x-1<0?high-1:x-1;
	}
	public static int rangeDecrease(int x,int high,Runnable r)
	{
		if(x-1<0)
			r.run();
		return x-1<0?high-1:x-1;
	}
	public static int rangeDecrease(int x,int high,IntSupplier s)
	{
		if(x-1<0)
			return s.getAsInt();
		return x-1<0?high-1:x-1;
	}
	private static List<Point> rotate(double x1,double y1,double x2,double y2,double angle)
	{
		double sin=sin(angle),cos=cos(angle);
		Map<Point,Integer> rt=new HashMap<>();
		for(double i=x1+0.25;i<x2;i+=0.5)
			for(double j=y1+0.25;j<y2;j+=0.5)
			{
				int x=round(i*cos-j*sin);
				int y=round(i*sin+j*cos);
//				rt.compute(new ImmutablePair<>((int)x,(int)y),(p,c)->c+1);
//				rt.compute(new Point((int)x,(int)y),
//						(pair,cnt)->cnt==null?1:cnt+1);
				rt.put(new Point((int)x,(int)y),1);
			}
		return rt.keySet().stream().toList();
	}
	public static List<Pair<Point,Point>> rotateWithOriginal(double x1,double y1,double x2,double y2,double angle)
	{
		List<Point> rted=rotate(x1,y1,x2,y2,angle);
		double sin=sin(-angle),cos=cos(-angle);
//		System.out.println(rted);
		BiFunction<Double,Double,Point> trans=(i,j)->{
			int x=round(i*cos-j*sin);
			int y=round(i*sin+j*cos);
			return new Point(x,y);
		};
		return rted.stream()
				.parallel()
				.map(p->{
							int i=p.left,j=p.right;
						int x=round(i*cos-j*sin);
						int y=round(i*sin+j*cos);
						return new ImmutablePair<>(new Point(i,j),new Point(x,y));
						})
				.filter(p->p.right.left>=x1&&p.right.left<x2&&p.right.right>=y1&&p.right.right<y2)
				.collect(Collectors.toList());
	}
	public static class Point extends ImmutablePair<Integer,Integer>
	{
		public Point(Integer left, Integer right)
		{
			super(left, right);
		}
	}
}
