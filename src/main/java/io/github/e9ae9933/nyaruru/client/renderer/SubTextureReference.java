package io.github.e9ae9933.nyaruru.client.renderer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.function.IntFunction;

public class SubTextureReference extends Texture
{
	Texture delegate;
	boolean allowOutOfBounds;
	int px,py;
	int width,height;
	public SubTextureReference(Texture delegate,int px,int py,int width,int height,boolean allowOutOfBounds)
	{
		this.delegate=delegate;
		this.px=px;
		this.py=py;
		this.width=width;
		this.height=height;
		this.allowOutOfBounds=allowOutOfBounds;
	}
	@Override
	public int getWidth()
	{
		return width;
	}

	@Override
	public int getHeight()
	{
		return height;
	}

	@Override
	public Color getRGB(int x, int y)
	{
		return delegate.getRGB(px+x,py+y);
	}

	@Override
	public void createFrom(BufferedImage image)
	{
		throw new UnsupportedOperationException("This is a reference!");
	}

	@Override
	public void createNew(int width, int height)
	{
		throw new UnsupportedOperationException("This is a reference!");
	}

	@Override
	public BufferedImage toBufferedImage()
	{
		throw new UnsupportedOperationException("This is a reference!");
	}

	@Override
	public Image toImage()
	{
		throw new UnsupportedOperationException("This is a reference!");
	}

	@Override
	public void drawPixel(int x, int y, Color c)
	{
		delegate.drawPixel(px+x,py+y,c);
	}

	@Override
	public void drawLine(double x1, double y1, double x2, double y2, Color c, double w)
	{
		delegate.drawLine(px+x1,py+y1,px+x2,py+y2,c,w);
	}

	@Override
	public void fillRect(int x, int y, int w, int h, Color c)
	{
		delegate.fillRect(px+x,py+y,w,h,c);
	}

	@Override
	public void drawString(String str, int x, int y, int size, Color c)
	{
		delegate.drawString(str,px+x,py+y,size, c);
	}

	@Override
	public void drawScaledString(String str, int x, int y, Font font, int xdis, int ydis, Color c, int scale)
	{
		delegate.drawScaledString(str,px+x,py+y,font,xdis,ydis,c,scale);
	}

	@Override
	public void drawScaledString(String str, int x, int y, IntFunction<Font> font, int size, int xdis, int ydis, Color c, int scale)
	{
		delegate.drawScaledString(str,px+x,py+y,font,size,xdis,ydis,c,scale);
	}

	@Override
	public int charWidth(int codePoint)
	{
		return delegate.charWidth(codePoint);
	}

	@Override
	public int drawChar(int codePoint, int x, int y, int size, Color c)
	{
		return delegate.drawChar(codePoint,px+x,py+y,size,c);
	}

	@Override
	public void drawTexture(Texture t, int x, int y)
	{
		delegate.drawTexture(t,px+x,py+y);
	}

	@Override
	public void drawTexture(Texture t, int x, int y, double angle)
	{
		delegate.drawTexture(t,px+x,py+y,angle);
	}

	@Override
	public void drawTexture(Texture t, AffineTransform trans, AlphaComposite alpha)
	{
		AffineTransform aff=new AffineTransform(trans);
		aff.preConcatenate(AffineTransform.getTranslateInstance(px,py));
		delegate.drawTexture(t,aff,alpha);
	}

	@Override
	public void drawTexture(Texture t, AffineTransform trans)
	{
		AffineTransform aff=new AffineTransform(trans);
		aff.preConcatenate(AffineTransform.getTranslateInstance(px,py));
		delegate.drawTexture(t,aff);
	}

	@Override
	public SubTextureReference getSubImageReference(int x, int y, int w, int h, boolean allowOutOfBoundsRendering)
	{
		return new SubTextureReference(this,px+x,py+y,w,h,allowOutOfBoundsRendering);
	}
}
