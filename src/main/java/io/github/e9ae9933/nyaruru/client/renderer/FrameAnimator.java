package io.github.e9ae9933.nyaruru.client.renderer;

import io.github.e9ae9933.nyaruru.MathHelper;
import io.github.e9ae9933.nyaruru.pxlsloader.PxlFrame;
import io.github.e9ae9933.nyaruru.pxlsloader.PxlSequence;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class FrameAnimator
{
	PixelLinerTextureManager manager;
	PxlSequence sequence;
	int position;
	int stepped;
	public FrameAnimator(PixelLinerTextureManager manager, PxlSequence sequence)
	{
		this.manager=manager;
		this.sequence=sequence;
		this.position=0;
		this.stepped=0;
	}
	public void renderTo(Texture target,int x,int y,double theta)
	{
//		System.out.println(sequence.getWidth()+" "+sequence.getHeight());
		PxlFrame f=sequence.getFrame(position);
		f.getLayerList().forEach(l->{
			Texture t=manager.getTexture(l);
			int w=t.getWidth(),h=t.getHeight();
			AffineTransform at=new AffineTransform();
			at.concatenate(AffineTransform.getScaleInstance(l.getZmx(),l.getZmy()));
			at.concatenate(AffineTransform.getTranslateInstance(x,y));
			at.concatenate(AffineTransform.getTranslateInstance(l.getX(),l.getY()));
			at.concatenate(AffineTransform.getRotateInstance(theta+l.getRotR()));
			at.concatenate(AffineTransform.getTranslateInstance(-w/2.0,-h/2.0));
			AlphaComposite alpha=AlphaComposite.SrcOver.derive(l.getAlpha()/10000.0f);
			target.drawTexture(t,at,alpha);


			double[] dx={0,w,w,0};
			double[] dy={0,0,h,h};
			Point2D[] p={new Point2D.Double(0,0),new Point2D.Double(w,0),new Point2D.Double(w,h),new Point2D.Double(0,h)};
			Point2D[] dst=new Point2D[4];
			at.transform(p,0,dst,0,4);
			for(int i=0;i<4;i++)
			{
				Point2D a=dst[i],b=dst[(i+1)%4];
				target.drawLine((int) a.getX(), (int) a.getY(), (int) b.getX(), (int) b.getY(),new Color(0x7f000000,true),1);
			}
		});
		AffineTransform at=new AffineTransform();
		int w=sequence.getFa().getWidth(),h=sequence.getFa().getHeight();
		double px=-w/2.0;
		double py=-h/2.0;
		at.translate(x+px,y+py);
		at.rotate(theta,-px,-py);
		double[] dx={0,w,w,0};
		double[] dy={0,0,h,h};
		Point2D[] p={new Point2D.Double(0,0),new Point2D.Double(w,0),new Point2D.Double(w,h),new Point2D.Double(0,h)};
		Point2D[] dst=new Point2D[4];
		at.transform(p,0,dst,0,4);
		for(int i=0;i<4;i++)
		{
			Point2D a=dst[i],b=dst[(i+1)%4];
			target.drawLine((int) a.getX(), (int) a.getY(), (int) b.getX(), (int) b.getY(),Color.BLACK,1);
		}
	}
	public void step()
	{
		stepped++;
		if(stepped>=sequence.getFrame(position).getCrf60())
		{
			stepped=0;
			position++;
			if (position >= sequence.getFrameCount())
				position = sequence.getLoopTo();
		}
	}
	public void renderAndStep(Texture target,int x,int y,double theta)
	{
		renderTo(target, x, y, theta);
		step();
	}
	public void reset()
	{
		position=0;
		stepped=0;
	}

	@Override
	public String toString()
	{
		//				"character= " + sequence.getFa()
		return "FrameAnimator{pose=%s (\"%s\"), sequence=%d, frame=%s, position=%d, stepped=%d}"
				.formatted(
						sequence.getFa().getTitle(),
						sequence.getFa().getComment(),
						sequence.getAim(),
						sequence.getFrame(position).getName(),
						position,
						stepped);
	}
}
