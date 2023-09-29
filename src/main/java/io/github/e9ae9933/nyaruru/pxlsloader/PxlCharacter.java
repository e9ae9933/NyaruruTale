package io.github.e9ae9933.nyaruru.pxlsloader;

import io.github.e9ae9933.nyaruru.client.renderer.PixelLinerTextureManager;
import io.github.e9ae9933.nyaruru.client.renderer.Texture;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class PxlCharacter
{
	//List<PxlImage> images=new ArrayList<>();
	PxlPose[] poses;
	PxlImageAtlas[] atlases;
	public PxlCharacter(byte[] b, PixelLinerTextureManager creator)
	{
		NoelByteBuffer buf=new NoelByteBuffer(b);
		Settings s=new Settings();
		s.creator=creator;
		load(buf,s);
		buf.end();
	}
	private void load(NoelByteBuffer b, Settings s)
	{
		// header
		if(b.getInt()!=2000791807)
			throw new IllegalArgumentException("invalid header");
		if(!b.getString(4).equals("PXLS"))
			throw new IllegalArgumentException("invalid header");
		while(b.size()>0)
		{
			if(b.size()<14)
			{
				break;
			}
			String op=new String(b.getNBytes(14), StandardCharsets.UTF_8);
			//System.out.printf("Section header %s\n",op);
			switch (op)
			{
				case "%IMGD_SECTION%":
					throw new IllegalArgumentException("does not support for compressed image data");
				case "%IMGS_SECTION%":
					throw new IllegalArgumentException("not supported");
				case "%PACK_SECTION%":
				{
					//P_IMG_PACKED
					int len = b.getInt();
					NoelByteBuffer target = new NoelByteBuffer(b.getNBytes(len));
					int n = target.getInt();
					atlases=new PxlImageAtlas[n];
					for (int i = 0; i < n; i++)
					{
						atlases[i]=new PxlImageAtlas(target,s,i);
					}
					break;
				}
				case "%IMGV_SECTION%":
				{
					NoelByteBuffer target = b.getSegment();
					System.out.println("Ignored segment IMGV with byte(s) " + target.size());
					target.getAllBytes();
					break;
				}
				case "%POSE_SECTION%":
				{
					NoelByteBuffer target = b.getSegment();
					int n = target.getInt();
					//System.out.println("Found "+n+" pose(s)");
					poses=new PxlPose[n];
					for(int i=0;i<n;i++)
					{
						//System.out.println("Reading pose "+i);
						poses[i]=new PxlPose(target,s,this);
					}
					break;
				}
				default:
					throw new IllegalArgumentException("invalid section header");
			}
		}
		b.end();
	}
	public List<PxlPose> getPoseList()
	{
		return List.of(poses);
	}
	public PxlPose getPoseByName(String s)
	{
		return Arrays.stream(poses)
				.filter(p->s.equalsIgnoreCase(p.title))
				.findFirst()
				.orElse(null);
	}
	public PxlPose getPose(int i)
	{
		return poses[i];
	}
	public int getPoseCount()
	{
		return poses.length;
	}
}
