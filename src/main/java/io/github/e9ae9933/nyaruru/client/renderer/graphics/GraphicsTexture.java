package io.github.e9ae9933.nyaruru.client.renderer.graphics;

import io.github.e9ae9933.nyaruru.MathHelper;
import io.github.e9ae9933.nyaruru.SharedConstants;
import io.github.e9ae9933.nyaruru.client.renderer.Texture;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class GraphicsTexture extends Texture
{
	private short[][] r,g,b;
	private float[][] alpha;
	public int width,height;

	public static class UniChar
	{
		public short[] bites=new short[16];
		public boolean wide;
	}
	public UniChar[] fontPage;
//	@SuppressWarnings("StatementWithEmptyBody")
	public GraphicsTexture()
	{
		fontPage=SharedConstants.getUnifont();
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
		return new Color(MathHelper.round255(alpha[x][y] * 255) << 24 | r[x][y] << 16 | g[x][y] << 8 | b[x][y],true);
	}

	@Override
	public void createFrom(BufferedImage image)
	{
		width=image.getWidth();
		height=image.getHeight();
		r=new short[width][height];
		g=new short[width][height];
		b=new short[width][height];
		alpha=new float[width][height];
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++)
			{
				int rgb=image.getRGB(i,j);
				r[i][j]= (short) (rgb>>>16&0xff);
				g[i][j]=(short)(rgb>>>8&0xff);
				b[i][j]=(short)(rgb&0xff);
				alpha[i][j]=(rgb>>>24&0xff)/255.0f;
			}
	}

	@Override
	public void createNew(int width, int height)
	{
		this.width=width;
		this.height=height;
		r=new short[width][height];
		g=new short[width][height];
		b=new short[width][height];
		alpha=new float[width][height];
	}

	@Override
	public BufferedImage toBufferedImage()
	{
		BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		if(SharedConstants.maximumCompatibility)
			for(int i=0;i<width;i++)
			{
				for (int j = 0; j < height; j++)
				{
					int rgb=MathHelper.round255(alpha[i][j] * 255) << 24 | r[i][j] << 16 | g[i][j] << 8 | b[i][j];
					image.setRGB(i,j,rgb);
				}
			}
		else
		{
			int[] buf=new int[width*height];
			IntStream.range(0,width).parallel().forEach((i)->
			{
				for (int j = 0; j < height; j++)
				{
					buf[j*width+i]=MathHelper.round255(alpha[i][j] * 255) << 24 | r[i][j] << 16 | g[i][j] << 8 | b[i][j];
				}
			});
			image.getRaster().setDataElements(0,0,width,height,buf);
		}
		return image;
	}

	@Override
	public Image toImage()
	{
		return null;
	}

//				if((MathHelper.round255(alpha[i][j]*255)|r[i][j]<<16|g[i][j]<<8|b[i][j])!=0)
//				System.out.println(Integer.toHexString(MathHelper.round255(alpha[i][j]*255)|r[i][j]<<16|g[i][j]<<8|b[i][j]));
//				image.getRaster().setPixel(i,j,new int[]{r[i][j],g[i][j],b[i][j],127});

	@Override
	public void drawPixel(int x, int y, Color c)
	{
		if(x<0||x>=width||y<0||y>=height)return;
		int r1=r[x][y],g1=g[x][y],b1=b[x][y];
		float a1=alpha[x][y];
		int r2=c.getRed(),g2=c.getGreen(),b2=c.getBlue();
		float a2=c.getAlpha()/255f;
		r[x][y]=MathHelper.round255(r1*(1-a2)+r2*a2);
		g[x][y]=MathHelper.round255(g1*(1-a2)+g2*a2);
		b[x][y]=MathHelper.round255(b1*(1-a2)+b2*a2);
		alpha[x][y]=a1*(1-a2)+a2;
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2, Color c, int w)
	{
		double len=MathHelper.distance(x1,y1,x2,y2);
		List<Pair<MathHelper.Point, MathHelper.Point>> p=MathHelper.rotateWithOriginal(0,w/-2.0,len,w/2.0,MathHelper.atan2xy(x2-x1,y2-y1));
		p.forEach(pr->
		{
			int i=pr.getLeft().left;
			int j=pr.getLeft().right;
//			System.out.println("rotate "+pr);
			drawPixel(x1+i,y1+j,c);
		});
	}

	@Override
	public void fillRect(int x, int y, int w, int h, Color c)
	{
		for(int i=0;i<w;i++)
		for(int j=0;j<h;j++)
		drawPixel(x+i,y+j,c);
	}

//	@Override
//	public void drawString(String str, int x, int y, Font font)
//	{
//		throw new UnsupportedOperationException();
//	}


	@Override
	public void drawString(String str, int x, int y, int size,int xdis, int ydis, Color c)
	{
//		Validate.notBlank(str);
		AtomicInteger dy= new AtomicInteger();
		Arrays.stream(str.split("\n"))
				.forEach(s->{
					AtomicInteger dx= new AtomicInteger();
					s.codePoints()
									.forEach(i->{
										dx.addAndGet(drawChar(i, dx.get(), dy.get(), size, c));
									});
					dy.addAndGet(ydis+size);
				});
	}

	@Override
	public void drawScaledString(String str, int x, int y, Font font, int xdis, int ydis, Color c, int scale)
	{

	}

	@Override
	public int charWidth(int codePoint)
	{
		if(fontPage.length<=codePoint)return -1;
		UniChar page=fontPage[codePoint];
		if(page==null)return -1;
		return page.wide?16:8;
	}

	@Override
	public int drawChar(int codePoint, int x, int y,int size,Color c)
	{
		if(fontPage.length<=codePoint)return -1;
		UniChar page=fontPage[codePoint];
		if(page==null)return -1;
		int w=page.wide?16:8;
		for(int i=0;i<size;i++)
			for(int j=0;j<(page.wide?size:size/2);j++)
//			{
//				boolean op=(((page.bites[i*16/size]>>>(page.wide?16:8)-j*8/size))&1)==1;
//				if(op)
//				{
//					drawPixel(x+j,y+i,c);
//				}
//			}
				if(!(((page.bites[i*16/size]>>>(w-(j*16/size+1)))&1)==0))
					drawPixel(x+j,y+i,c);
		return page.wide?size:size/2;
	}

	@Override
	public void drawTexture(Texture t, int x, int y)
	{
		int w=t.getWidth(),h=t.getHeight();
		IntStream.range(0,w)
				.forEach(
						i->{
							for(int j=0;j<h;j++)
								drawPixel(x+i,y+j,t.getRGB(i,j));
						}
				);
	}

	@Override
	public void drawTexture(Texture t, int x, int y, double angle)
	{
		int w=t.getWidth(),h=t.getHeight();
		List<Pair<MathHelper.Point, MathHelper.Point>> pairs = MathHelper.rotateWithOriginal(0, 0, w, h, angle);
		pairs.forEach(p->{
			drawPixel(x+p.getLeft().left,y+p.getLeft().right,t.getRGB(p.getRight().left,p.getRight().right));
		});
	}

	@Override
	public void drawTexture(Texture t, AffineTransform trans, AlphaComposite alpha)
	{

	}

	@Override
	public void drawTexture(Texture t, AffineTransform trans)
	{

	}

	@Override
	public Texture getSubImageReference(int x, int y, int w, int h,boolean allowOutOfBoundsRendering)
	{
		return null;
//		return new GraphicsSubTexture(x,y,w,h,this,allowOutOfBoundsRendering);
	}
}
