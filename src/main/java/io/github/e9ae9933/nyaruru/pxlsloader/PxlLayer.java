package io.github.e9ae9933.nyaruru.pxlsloader;

import io.github.e9ae9933.nyaruru.client.renderer.PixelLinerTextureManager;
import io.github.e9ae9933.nyaruru.client.renderer.Texture;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.function.Supplier;

public class PxlLayer
{
	int id;
	double id2;
	byte type=0;
	String name="";
	short alpha=10000;
	float x,y;
	double zmx=1,zmy=1;
	double rotR=0;
	int blendVariable=0;
	int useless1;
	byte useless2,useless3;
	transient BufferedImage image;
	boolean isImport=false;
	transient PxlFrame fa;
	transient PixelLinerTextureManager creator;
	transient Map<Pair<Integer, Double>, Pair<PxlImageAtlas, PxlImageAtlas.Uv>> idMap;
	PxlLayer(NoelByteBuffer b,Settings s,PxlFrame fa)
	{
		creator=s.creator;
		this.fa=fa;
		this.idMap=s.idMap;
		id=b.getInt();
		id2=b.getDouble();
		type=b.getByte();
		//get image
		name=b.getUTFString();
		alpha=b.getShort();
		x=b.getShort()/10f;
		y=b.getShort()/10f;
		zmx=b.getDouble();
		zmy=b.getDouble();
		rotR=b.getDouble();
		blendVariable=b.getUnsignedShort();
		useless1=b.getInt();
		useless2=b.getByte();
		useless3=b.getByte();
	}
	public BufferedImage getImage()
	{
		Pair<PxlImageAtlas, PxlImageAtlas.Uv> pr=idMap.get(new Pair<>(id,id2));
		if(pr==null)
		{
			throw new RuntimeException("Unfound image for "+name);
		}
		BufferedImage img=pr.first.image;
		PxlImageAtlas.Uv u=pr.second;
//		System.out.println("xy %d %d %d %d".formatted(u.x, u.y, u.width, u.height));
		BufferedImage sub = img.getSubimage(u.x, u.y, u.width, u.height);
		return sub;
	}
	public Texture getTexture()
	{
		return creator.getTexture(this);
	}

	public byte getType()
	{
		return type;
	}

	public String getName()
	{
		return name;
	}

	public short getAlpha()
	{
		return alpha;
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}

	public double getZmx()
	{
		return zmx;
	}

	public double getZmy()
	{
		return zmy;
	}

	public double getRotR()
	{
		return rotR;
	}

	public int getBlendVariable()
	{
		return blendVariable;
	}

	public int getUseless1()
	{
		return useless1;
	}

	public byte getUseless2()
	{
		return useless2;
	}

	public byte getUseless3()
	{
		return useless3;
	}

	public boolean isImport()
	{
		return isImport;
	}
}
