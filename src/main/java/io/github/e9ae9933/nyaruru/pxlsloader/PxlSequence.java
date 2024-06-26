package io.github.e9ae9933.nyaruru.pxlsloader;

import io.github.e9ae9933.nyaruru.client.renderer.FrameAnimator;
import io.github.e9ae9933.nyaruru.client.renderer.PixelLinerTextureManager;

import java.util.Arrays;
import java.util.List;

public class PxlSequence
{
	int width,height;
	int bodyX,bodyY;
	int shiftX,shiftY;
	int loopTo;
	String[] frameSnd;
	int aim;
	PxlFrame[] frames;
	byte useless;
	transient PxlPose fa;
	transient PixelLinerTextureManager creator;
	PxlSequence(NoelByteBuffer base,Settings s,int aim,PxlPose fa)
	{
		this.fa=fa;
		this.aim=aim;
		this.creator=s.creator;
		NoelByteBuffer b=base.getSegment();
		useless=b.getByte();
		width=b.getUnsignedShort();
		height=b.getUnsignedShort();
		bodyX=b.getShort();
		bodyY=b.getShort();
		shiftX=b.getShort();
		shiftY=b.getShort();
		loopTo=b.getShort();
		int num=b.getUnsignedShort();
		frameSnd=new String[num];
		for(int i=0;i<num;i++)
			frameSnd[i]=b.getUTFString();
		int num2=base.getUnsignedShort();
		int num3=0;
		frames=new PxlFrame[num2];
		for(int i=0;i<num2;i++)
		{
			frames[i]=new PxlFrame(base,s,this);
		}
	}
	public List<PxlFrame> getFrameList()
	{
		return List.of(frames);
	}
	public PxlFrame getFrameByName(String s)
	{
		return Arrays.stream(frames)
				.filter(f->s.equalsIgnoreCase(f.name))
				.findFirst()
				.orElse(null);
	}
	public PxlFrame getFrame(int i)
	{
		return frames[i];
	}
	public int getFrameCount()
	{
		return frames.length;
	}
	public FrameAnimator createFrameAnimator()
	{
		return creator.createFrameAnimator(this);
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public int getBodyX()
	{
		return bodyX;
	}

	public int getBodyY()
	{
		return bodyY;
	}

	public int getShiftX()
	{
		return shiftX;
	}

	public int getShiftY()
	{
		return shiftY;
	}

	public int getLoopTo()
	{
		return loopTo;
	}

	public String[] getFrameSnd()
	{
		return frameSnd;
	}

	public int getAim()
	{
		return aim;
	}

	public byte getUseless()
	{
		return useless;
	}

	public PxlPose getFa()
	{
		return fa;
	}
}
