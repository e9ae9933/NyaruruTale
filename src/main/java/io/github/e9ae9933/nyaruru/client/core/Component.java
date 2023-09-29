package io.github.e9ae9933.nyaruru.client.core;

import java.awt.event.KeyEvent;

public abstract class Component
{
	public int width,height;
	public boolean isSizeSet()
	{
		return false;
	}
//	public boolean setVisible()
//	{
//
//	}
	public HandleResult handleHover(int x,int y)
	{
		return HandleResult.UNHANDLED;
	}
	public HandleResult handleLeftClick(int x,int y)
	{
		return HandleResult.UNHANDLED;
	}
	public HandleResult handleKey(KeyEvent event)
	{
		return HandleResult.UNHANDLED;
	}
	public void clientTick(ClientTickInfo info)
	{

	}
}
