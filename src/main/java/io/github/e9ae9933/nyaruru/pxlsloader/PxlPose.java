package io.github.e9ae9933.nyaruru.pxlsloader;

import java.util.ArrayList;
import java.util.List;

public class PxlPose
{
	boolean autoFlip;
	boolean tetraPose;
	String title;
	int width,height;
	int endJumpLoopCount;
	String endJumpTitle;
	String[] aliasTo;
	String comment;
	PxlSequence[] sequences;
	byte useless;
	transient PxlCharacter fa;
	PxlPose(NoelByteBuffer b,Settings s,PxlCharacter fa)
	{
		this.fa=fa;
		NoelByteBuffer target=b.getSegment();
		useless=target.getByte();
		autoFlip=target.getBoolean();
		tetraPose=target.getBoolean();
		title=target.getUTFString();
		width=target.getShort();
		height=target.getShort();
		endJumpLoopCount=target.getShort();
		endJumpTitle=target.getUTFString();
		int num2=target.getShort();
		aliasTo=new String[num2];
		//for(int i=0;i<num2;i++)
		for(int i=0;i<num2;i++)
			aliasTo[i]=target.getUTFString();
		if(useless>=2)
			comment=target.getUTFString();
		int num3;
		List<PxlSequence> list=new ArrayList<>();
		while((num3=Byte.toUnsignedInt(b.getByte()))!=0)
		{
			num3-=10;
			//System.out.println("num3 "+num3);
			list.add(new PxlSequence(b,s,num3,this));
		}
		sequences=list.toArray(new PxlSequence[0]);
	}
	public List<PxlSequence> getSequenceList()
	{
		return List.of(sequences);
	}
	public PxlSequence getSequence(int i)
	{
		return sequences[i];
	}
	public int getSequenceCount()
	{
		return sequences.length;
	}

	public boolean isAutoFlip()
	{
		return autoFlip;
	}

	public boolean isTetraPose()
	{
		return tetraPose;
	}

	public String getTitle()
	{
		return title;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public int getEndJumpLoopCount()
	{
		return endJumpLoopCount;
	}

	public String getEndJumpTitle()
	{
		return endJumpTitle;
	}

	public String[] getAliasTo()
	{
		return aliasTo;
	}

	public String getComment()
	{
		return comment;
	}

	public byte getUseless()
	{
		return useless;
	}

	public PxlCharacter getFa()
	{
		return fa;
	}

}
