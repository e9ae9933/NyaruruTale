package io.github.e9ae9933.nyaruru.client.renderer;

import io.github.e9ae9933.nyaruru.client.renderer.vlt.VolatileTexture;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class DoNothingTexture extends Texture
{
	int w,h;
	public DoNothingTexture(int w,int h)
	{
		this.w=w;
		this.h=h;
	}

	@Override
	public int getWidth()
	{
		return w;
	}

	@Override
	public int getHeight()
	{
		return h;
	}

	@Override
	public Color getRGB(int x, int y)
	{
		return new Color(0,true);
	}

	@Override
	public void createFrom(BufferedImage image)
	{
//		super.createFrom(image);
	}

	@Override
	public void createNew(int width, int height)
	{
//		super.createNew(width, height);
	}

	@Override
	public BufferedImage toBufferedImage()
	{
		return new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
	}

	@Override
	public Image toImage()
	{
		return toBufferedImage();
	}

	@Override
	public void drawPixel(int x, int y, Color c)
	{
//		super.drawPixel(x, y, c);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2, Color c, int w)
	{
//		super.drawLine(x1, y1, x2, y2, c, w);
	}

	@Override
	public void fillRect(int x, int y, int w, int h, Color c)
	{
//		super.fillRect(x, y, w, h, c);
	}

	@Override
	public void drawString(String str, int x, int y, int size, int xdis, int ydis, Color c)
	{
//		super.drawString(str, x, y, size, xdis, ydis, c);
	}

	@Override
	public void drawScaledString(String str, int x, int y, Font font, int xdis, int ydis, Color c, int scale)
	{
//		super.drawScaledString(str, x, y, font, xdis, ydis, c, scale);
	}

	@Override
	public int charWidth(int codePoint)
	{
		return 0;
	}

	@Override
	public int drawChar(int codePoint, int x, int y, int size, Color c)
	{
		return 0;
//		return super.drawChar(codePoint, x, y, size, c);
	}

	@Override
	public void drawTexture(Texture t, int x, int y)
	{
//		super.drawTexture(t, x, y);
	}

	@Override
	public void drawTexture(Texture t, AffineTransform trans, AlphaComposite alpha)
	{
//		super.drawTexture(t, trans, alpha);
	}

	@Override
	public void drawTexture(Texture t, int x, int y, double angle)
	{
//		super.drawTexture(t, x, y, angle);
	}

	@Override
	public void drawTexture(Texture t, AffineTransform trans)
	{
//		super.drawTexture(t, trans);
	}

	@Override
	public SubTextureReference getSubImageReference(int x, int y, int w, int h, boolean allowOutOfBoundsRendering)
	{
		return new SubTextureReference(this,x, y, w, h, allowOutOfBoundsRendering);
	}
}
