package io.github.e9ae9933.nyaruru.client.renderer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public abstract class Texture
{
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract Color getRGB(int x,int y);
	public abstract void createFrom(BufferedImage image);
	public abstract void createNew(int width,int height);
	@Deprecated
	public abstract BufferedImage toBufferedImage();
	public abstract Image toImage();
	public abstract void drawPixel(int x,int y,Color c);
	public abstract void drawLine(int x1,int y1,int x2,int y2,Color c,int w);
	public abstract void fillRect(int x,int y,int w,int h,Color c);
	public void drawRect(int x,int y,int w,int h,Color c,int width)
	{
		drawLine(x,y,x+w,y,c,width);
		drawLine(x,y,x,y+h,c,width);
		drawLine(x,y+h,x+w,y+h,c,width);
		drawLine(x+w,y,x+w,y+h,c,width);
	}
	public abstract void drawString(String str, int x, int y, int size, int xdis, int ydis, Color c);

	//	public abstract void drawChar(int codePoint,int x,int y);
	public abstract int charWidth(int codePoint);
	public abstract int drawChar(int codePoint, int x, int y, int size, Color c);

	public abstract void drawTexture(Texture t, int x, int y);
	public abstract void drawTexture(Texture t, int x, int y,double angle);

	public abstract void drawTexture(Texture t, AffineTransform trans, AlphaComposite alpha);

	public abstract void drawTexture(Texture t, AffineTransform trans);

	public abstract Texture getSubImageReference(int x, int y, int w, int h, boolean allowOutOfBoundsRendering);
}