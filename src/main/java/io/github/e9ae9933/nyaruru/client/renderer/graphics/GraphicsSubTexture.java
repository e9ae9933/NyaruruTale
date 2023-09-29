package io.github.e9ae9933.nyaruru.client.renderer.graphics;

import java.awt.*;

public abstract class GraphicsSubTexture extends GraphicsTexture
{
	int px,py;
	boolean allowOutOfBoundsRendering;
	GraphicsTexture parent;
	GraphicsSubTexture(int x,int y,int w,int h,GraphicsTexture parent,boolean allowOutOfBoundsRendering)
	{
		this.fontPage=parent.fontPage;
		this.width=w;
		this.height=h;
		this.px=x;
		this.py=y;
		this.parent=parent;
		this.allowOutOfBoundsRendering=allowOutOfBoundsRendering;
	}

	@Override
	public void drawPixel(int x, int y, Color c)
	{
		if((x<0||x>=width||y<0||y>=height)&&!allowOutOfBoundsRendering)return;
		parent.drawPixel(px+x,py+y,c);
	}
}
