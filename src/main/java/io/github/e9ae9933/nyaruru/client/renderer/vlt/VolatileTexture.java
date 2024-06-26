package io.github.e9ae9933.nyaruru.client.renderer.vlt;

import io.github.e9ae9933.nyaruru.SharedConstants;
import io.github.e9ae9933.nyaruru.client.renderer.SubTextureReference;
import io.github.e9ae9933.nyaruru.client.renderer.Texture;
import static io.github.e9ae9933.nyaruru.MathHelper.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;

public class VolatileTexture extends Texture
{
	VolatileImage delegate;
	int width;
	int height;
	Graphics2D g;
	public VolatileTexture()
	{

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
		throw new UnsupportedOperationException();
//		return null;
	}

	@Override
	public void createFrom(BufferedImage image)
	{
		width=image.getWidth();
		height=image.getHeight();
		delegate=SharedConstants.requestVolatileBuffer2(image.getWidth(),image.getHeight());
		g=delegate.createGraphics();
		loadTips();
		g.setBackground(new Color(0x00000000,true));
		g.clearRect(0,0,image.getWidth(),image.getHeight());
		g.drawImage(image,0,0,null);
		g.setFont(SharedConstants.getUnifontFont());
		clip();
	}

	@Override
	public void createNew(int width, int height)
	{
		this.width=width;
		this.height=height;
		delegate=SharedConstants.requestVolatileBuffer2(width,height);
		g=delegate.createGraphics();
		loadTips();
		g.setBackground(new Color(0x00000000,true));
		g.clearRect(0,0,width,height);
		g.setFont(SharedConstants.getUnifontFont());
		clip();
	}
	void loadTips()
	{
		g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
	}

	@Override
	public BufferedImage toBufferedImage()
	{
//		validate();
		throw new UnsupportedOperationException();
//		return delegate.getSnapshot();
	}

	@Override
	public Image toImage()
	{
//		validate();
		clip();
		return delegate;
	}
	void clip()
	{
		int realW=delegate.getWidth(),realH=delegate.getHeight();
		g.setBackground(new Color(0,true));
		g.fillRect(width,0,realW-width,realH);
		g.fillRect(0,height,width,realH-height);
	}

	@Override
	public void drawPixel(int x, int y, Color c)
	{
		g.setColor(c);
		g.fillRect(x,y,1,1);
	}

	@Override
	public void drawLine(double x1, double y1, double x2, double y2, Color c, double w)
	{
		g.setStroke(new BasicStroke((float)w,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
		g.setColor(c);
		g.drawLine(round(x1), round(y1), round(x2), round(y2));
	}

	@Override
	public void fillRect(int x, int y, int w, int h, Color c)
	{
		g.setColor(c);
		g.fillRect(x,y,w,h);
	}

	@Override
	public void drawString(String str, int x, int y, int size, Color c)
	{
		g.setColor(c);
		g.setFont(SharedConstants.getUnifontFont().deriveFont((float)size));
		FontMetrics metrics=g.getFontMetrics();
		int ascent=metrics.getAscent();
//		g.drawString(str,x,y+ascent);
		String[] split = str.replace("\t", " ".repeat(SharedConstants.tabWidth)).split("\n");
//		StopWatch sw=StopWatch.createStarted();
		for(int i=0;i<split.length;i++)
			if(y+(size)*i<getHeight())
				g.drawString(split[i],x,y+ascent+(size)*i);
//		System.out.println(sw.getTime());
	}

	@Override
	public void drawScaledString(String str, int x, int y, Font font, int xdis, int ydis, Color c, int scale)
	{
		int size=font.getSize();
		drawScaledString(str,x,y,cp->font,size,xdis,ydis,c,scale);
//		int size=font.getSize();
//		g.setFont(font);
//		g.setColor(c);
//		AtomicInteger dy= new AtomicInteger(y+0);
//		Arrays.stream(str.replace("\t"," ".repeat(SharedConstants.tabWidth)).split("\n"))
//				.forEach(s->{
//					AtomicInteger dx= new AtomicInteger(x);
//					s.codePoints()
//							.forEach(i->{
//								dx.addAndGet(drawFontedScaledChar(i, dx.get(), dy.get(), scale, c)+xdis);
//							});
//					dy.addAndGet(ydis+size*scale);
//				});
	}
	@Override
	public void drawScaledString(String str, int x, int y, IntFunction<Font> font,int size, int xdis, int ydis, Color c, int scale)
	{
		g.setColor(c);
		AtomicInteger dy= new AtomicInteger(y+0);
		Arrays.stream(str.replace("\t"," ".repeat(SharedConstants.tabWidth)).split("\n"))
				.forEach(s->{
					AtomicInteger dx= new AtomicInteger(x);
					s.codePoints()
							.forEach(i->{
								g.setFont(font.apply(i).deriveFont((float)size));
								dx.addAndGet(drawFontedScaledChar(i, dx.get(), dy.get(), scale, c)+xdis);
							});
					dy.addAndGet(ydis+size*scale);
				});
	}

	@Override
	public int charWidth(int codePoint)
	{
		return g.getFontMetrics().charWidth(codePoint);
	}

	protected int drawFontedScaledChar(int codePoint,int x,int y,int scale,Color c)
	{
		String target="%c".formatted(codePoint);
		int w=g.getFontMetrics().charWidth(codePoint);
		int h=g.getFontMetrics().getHeight();
		VolatileImage image=SharedConstants.requestVolatileBuffer2(w,h);
		Graphics2D g2d=image.createGraphics();
		g2d.setFont(g.getFont());
		g2d.setBackground(new Color(0,true));
		g2d.setColor(c);
		g2d.clearRect(0,0,w,h);
		g2d.drawString("%c".formatted(codePoint), 0, g.getFontMetrics().getAscent());
//			SharedConstants.volatileTextureFontBuffer.put(new ImmutablePair<>(g.getFont(),codePoint),image);
		AffineTransform at=g.getTransform();
		g.translate(x,y);
		g.scale(scale,scale);
		g.drawImage(image,0,0,null);
		g.setTransform(at);
//		g.drawImage(image.getScaledInstance(w*scale,h*scale,Image.SCALE_FAST),x,y,null);
		SharedConstants.returnVolatileBuffer(image);
		return w*scale;
	}
	@Override
	public int drawChar(int codePoint, int x, int y, int size, Color c)
	{
		g.setColor(c);
		Font font=g.getFont().deriveFont((float)size);
		g.setFont(font);
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
	public void validate()
	{
//		if(true)
//			throw new RuntimeException();
		int rt=delegate.validate(GraphicsEnvironment.
				getLocalGraphicsEnvironment().
				getDefaultScreenDevice().
				getDefaultConfiguration());
		try
		{
			if (rt != VolatileImage.IMAGE_OK)
				throw new RuntimeException("Validate failed: returned %d".formatted(rt));
			if (delegate.contentsLost())
				throw new RuntimeException("Contents lost");
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
//			e.printStackTrace();
		}
	}
	boolean finalized=false;
	@Override
	public void end()
	{
		if(!finalized)
		{
			finalized = true;
			SharedConstants.returnVolatileBuffer(delegate);
			delegate=null;
			g=null;
		}
	}

	@Override
	@SuppressWarnings({""})
	protected void finalize() throws Throwable
	{
		if(!finalized&&!SharedConstants.ALLOW_UNSAFE_BEHAVIORS)
		{
			try{
				Thread dummy=new Thread(()->{});
				Runtime.getRuntime().addShutdownHook(dummy);
				Runtime.getRuntime().removeShutdownHook(dummy);
			}
			catch (Exception e)
			{
				return;
			}
			Exception e=new IllegalStateException("UNHANDLED FINALIZE");
			e.printStackTrace();
			Thread.getAllStackTraces().keySet().forEach(
					t->
					{
						if (t != Thread.currentThread())
							t.interrupt();
					}
			);
			SharedConstants.showErrorDialog(e);
			System.exit(SharedConstants.IMAGE_LEAK);
		}
	}
}
