package io.github.e9ae9933.nyaruru.client.renderer.vlt;

import io.github.e9ae9933.nyaruru.SharedConstants;
import io.github.e9ae9933.nyaruru.client.renderer.SubTextureReference;
import io.github.e9ae9933.nyaruru.client.renderer.Texture;
import org.apache.commons.lang3.Validate;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class VolatileTexture extends Texture
{
	VolatileImage delegate;
	Graphics2D g;
	public VolatileTexture()
	{

	}
	@Override
	public int getWidth()
	{
		return delegate.getWidth();
	}

	@Override
	public int getHeight()
	{
		return delegate.getHeight();
	}

	@Override
	public Color getRGB(int x, int y)
	{
		throw new UnsupportedOperationException();
//		return null;
	}

	@Override
	public void createFrom(BufferedImage image)
	{
		delegate=image
				.createGraphics()
				.getDeviceConfiguration()
				.createCompatibleVolatileImage(image.getWidth(),image.getHeight(),Transparency.TRANSLUCENT);
		g=delegate.createGraphics();
//		createNew(image.getWidth(),image.getHeight());
		g.setBackground(new Color(0x00000000,true));
		g.clearRect(0,0,image.getWidth(),image.getHeight());
		g.drawImage(image,0,0,null);
		g.setFont(SharedConstants.getUnifontFont());
	}

	@Override
	public void createNew(int width, int height)
	{
//		System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment()
//				.getDefaultScreenDevice()
//				.getDefaultConfiguration()
//				.isTranslucencyCapable());
		delegate=GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice()
				.getDefaultConfiguration()
				.createCompatibleVolatileImage(width,height,Transparency.TRANSLUCENT);
		g=delegate.createGraphics();
		g.setBackground(new Color(0x00000000,true));
		g.clearRect(0,0,width,height);
		g.setFont(SharedConstants.getUnifontFont());
	}

	@Override
	public BufferedImage toBufferedImage()
	{
		throw new UnsupportedOperationException("use toImage instead");
//		return delegate.getSnapshot();
	}

	@Override
	public Image toImage()
	{
		return delegate;
	}

	@Override
	public void drawPixel(int x, int y, Color c)
	{
		g.setColor(c);
		g.fillRect(x,y,1,1);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2, Color c, int w)
	{
		g.setStroke(new BasicStroke(w));
		g.setColor(c);
		g.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void fillRect(int x, int y, int w, int h, Color c)
	{
		g.setColor(c);
		g.fillRect(x,y,w,h);
	}

	@Override
	public void drawString(String str, int x, int y, int size, int xdis, int ydis, Color c)
	{
		g.setFont(g.getFont().deriveFont((float)size));
		g.setColor(c);
		Validate.notBlank(str);
		AtomicInteger dy= new AtomicInteger(y);
		Arrays.stream(str.replace("\t"," ".repeat(SharedConstants.tabWidth)).split("\n"))
				.forEachOrdered(s->{
					AtomicInteger dx= new AtomicInteger(x);
					s.codePoints()
							.forEach(i->{
								dx.addAndGet(drawChar(i, dx.get(), dy.get(), size, c));
							});
					dy.addAndGet(ydis+size);
				});
	}

	@Override
	public int charWidth(int codePoint)
	{
		return g.getFontMetrics().charWidth(codePoint);
	}

	@Override
	public int drawChar(int codePoint, int x, int y, int size, Color c)
	{
//		System.out.println("draw %c".formatted(codePoint));
		g.setColor(c);
		g.getFont().deriveFont((float)size);
		g.drawString("%c".formatted(codePoint),x,y);
		return charWidth(codePoint);
	}

	@Override
	public void drawTexture(Texture t, int x, int y)
	{
		g.drawImage(t.toImage(),x,y,null);
//		System.out.println(delegate.contentsLost()+" "+ ((VolatileImage) t.toImage()).contentsLost());
	}
	@Override
	public void drawTexture(Texture t,AffineTransform trans,AlphaComposite alpha)
	{
		Composite c=g.getComposite();
		g.setComposite(alpha);
		g.drawImage(t.toImage(),trans,null);
		g.setComposite(c);
	}

	@Override
	public void drawTexture(Texture t, int x, int y, double angle)
	{
		AffineTransform at=AffineTransform.getTranslateInstance(x,y);
		at.rotate(angle);
		drawTexture(t,at);
	}

//	@Override
//	public void drawTexture(Texture t,int x,int y,double angle,int centerX,int centerY)
//	{
//		AffineTransform at=AffineTransform.getTranslateInstance(x,y);
//		at.rotate(angle,centerX,centerY);
//		drawTexture(t,at);
//	}

	@Override
	public void drawTexture(Texture t,AffineTransform trans)
	{
		g.drawImage(t.toImage(),trans,null);
	}

	@Override
	public SubTextureReference getSubImageReference(int x, int y, int w, int h, boolean allowOutOfBoundsRendering)
	{
		return new SubTextureReference(this,x,y,w,h,allowOutOfBoundsRendering);
	}
}
