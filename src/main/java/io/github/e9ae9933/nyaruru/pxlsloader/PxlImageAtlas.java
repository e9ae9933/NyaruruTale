package io.github.e9ae9933.nyaruru.pxlsloader;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class PxlImageAtlas
{
	static class Uv
	{
		int id;
		double id2;
		int x,y,width,height;

		public Uv(){}
		public Uv(int id, double id2, int x, int y, int width, int height)
		{
			this.id = id;
			this.id2 = id2;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
	}
	Uv[] pos;
	transient BufferedImage image;
	PxlImageAtlas(NoelByteBuffer b,Settings s,int id)
	{
		int type=b.getByte()-22;
		if(type>=0)
		{
			int num = Byte.toUnsignedInt(b.getByte());
			int margin = Byte.toUnsignedInt(b.getByte());
			if(margin!=1)
				throw new IllegalArgumentException("what?! not 1 margin?!");
			//System.out.println("margin "+margin);
			int num2 = b.getInt();
			pos = new Uv[num2];
			for (int i = 0; i < num2; i++)
			{
				Uv t = new Uv();
				t.id = b.getInt();
				t.id2 = b.getDouble();
				t.x = b.getInt();
				t.y = b.getInt();
				t.width = b.getInt();
				t.height = b.getInt();
				pos[i]=t;
//				System.out.printf("Found atlas %d, %f: (%d, %d) [%d, %d]\n", t.id, t.id2, t.x, t.y, t.width, t.height);
			}
			if (num == 1)
			{
				int w=b.getInt();
				int h=b.getInt();
				JOptionPane.showMessageDialog(null,"这个文件是 png 分离形式。\n请指定读取的图片: %dx%d".formatted(w,h),null,JOptionPane.INFORMATION_MESSAGE);
				byte[] png=Utils.chooseFileAndGetAllBytes(new FileFilter()
				{
					@Override
					public boolean accept(File f)
					{
						return f.isDirectory()||f.getName().toLowerCase().endsWith(".png");
					}

					@Override
					public String getDescription()
					{
						return ".png";
					}
				});
				try
				{
					image = ImageIO.read(new ByteArrayInputStream(png));
				}
				catch (Exception e)
				{
					throw new IllegalArgumentException(e);
				}
				if(image.getWidth()!=w||image.getHeight()!=h)
				{
					JOptionPane.showMessageDialog(null,"期望的分辨率: %dx%d\n图片的分辨率: %dx%d\n将尝试强行继续执行。\n这很可能导致错误产生。",null,JOptionPane.WARNING_MESSAGE);
				}
//				throw new RuntimeException("No load from png allowed.");
			} else
			{
				NoelByteBuffer img = b.getSegment();
				try
				{
					image=ImageIO.read(new ByteArrayInputStream(img.getAllBytes()));
					ImageIO.write(image,"png",new File("test.png"));
				} catch (Exception e)
				{
					throw new IllegalArgumentException(e);
				}
			}
			Arrays.stream(pos).forEach(
					p->{
						s.idMap.put(new Pair<>(p.id,p.id2),
								new Pair<>(this,p));
					}
			);
		}
	}
	@Deprecated
	static List<PxlImage> readFromBytes(NoelByteBuffer b,Settings s)
	{
		int type=b.getByte()-22;
		if(type>=0)
		{
			int num=Byte.toUnsignedInt(b.getByte());
			int margin=Byte.toUnsignedInt(b.getByte());
			int num2=b.getInt();
			Uv[] pos=new Uv[num2];
			for(int i=0;i<num2;i++)
			{
				Uv t=new Uv();
				t.id=b.getInt();
				t.id2=b.getDouble();
				t.x=b.getInt();
				t.y=b.getInt();
				t.width=b.getInt();
				t.height=b.getInt();
//				System.out.printf("Found atlas %d, %f: (%d, %d) [%d, %d]\n",t.id,t.id2,t.x,t.y,t.width,t.height);
			}
			if(num==1)
			{
				int width=b.getInt();
				int height=b.getInt();
				//todo: load texture
			}
			else
			{
				NoelByteBuffer img=b.getSegment();
				try{
					FileOutputStream fos=new FileOutputStream("test.png");

					fos.write(img.getNBytes(img.size()));
					fos.close();
				}
				catch (Exception e)
				{

				}
			}
		}
		return null;
	}
}
