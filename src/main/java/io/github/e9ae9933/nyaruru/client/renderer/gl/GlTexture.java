package io.github.e9ae9933.nyaruru.client.renderer.gl;

import io.github.e9ae9933.nyaruru.client.renderer.Texture;

import java.awt.*;
import java.awt.image.BufferedImage;


public abstract class GlTexture extends Texture
{
	@Override
	public int getWidth()
	{
		return 0;
	}

	@Override
	public int getHeight()
	{
		return 0;
	}

	@Override
	public Color getRGB(int x, int y)
	{
		return null;
	}

	@Override
	public void createFrom(BufferedImage image)
	{

	}

	@Override
	public void createNew(int width, int height)
	{

	}

	@Override
	public BufferedImage toBufferedImage()
	{
		return null;
	}

	@Override
	public Image toImage()
	{
		return null;
	}

	@Override
	public void drawPixel(int x, int y, Color c)
	{

	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2, Color c, int w)
	{

	}

	@Override
	public void fillRect(int x, int y, int w, int h, Color c)
	{

	}

	@Override
	public void drawString(String str, int x, int y, int size, int xdis, int ydis, Color c)
	{

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
	}

	@Override
	public void drawTexture(Texture t, int x, int y)
	{

	}

	@Override
	public void drawTexture(Texture t, int x, int y, double angle)
	{

	}

	@Override
	public Texture getSubImageReference(int x, int y, int w, int h, boolean allowOutOfBoundsRendering)
	{
		return null;
	}
}
