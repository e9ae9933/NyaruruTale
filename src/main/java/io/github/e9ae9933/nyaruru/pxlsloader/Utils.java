package io.github.e9ae9933.nyaruru.pxlsloader;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Utils
{
	public static byte[] readAllBytes(InputStream is)
	{
		try
		{
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			BufferedInputStream bis=new BufferedInputStream(is);
			byte[] b=new byte[8192];
			int len;
			while((len=bis.read(b))>0)
				bos.write(b,0,len);
			return bos.toByteArray();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	public static void writeAllUTFString(File file,String s)
	{
		ignoreExceptions(()->{
			FileOutputStream fos=new FileOutputStream(file);
			fos.write(s.getBytes(StandardCharsets.UTF_8));
			fos.close();
		});
	}
	public static void writeAllBytes(File file,byte[] b)
	{
		ignoreExceptions(()->{
			FileOutputStream fos=new FileOutputStream(file);
			fos.write(b);
			fos.close();
		});
	}
	public static String readAllUTFString(InputStream is)
	{
		return new String(readAllBytes(is), StandardCharsets.UTF_8);
	}
	public static String readAllUTFString(File file)
	{
		return new String(readAllBytes(file), StandardCharsets.UTF_8);
	}
	public static InputStream readFromResources(String name,boolean create)
	{
		try
		{
			File file = new File(name);
			if (!file.exists())
			{
				InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(name);
				if(is==null)
					throw new NullPointerException("File not found: "+name);
				if (create)
				{
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(readAllBytes(is));
					is.close();
					fos.close();
				}
				else
					return is;
			}
			return new FileInputStream(file);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	public static byte[] readAllBytes(File file)
	{
		try
		{
			FileInputStream fis=new FileInputStream(file);
			byte[] b=readAllBytes(fis);
			fis.close();
			return b;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	public static <T> T ignoreExceptions(SupplierWithExceptions<T> supplier)
	{
		try
		{
			return supplier.get();
		}
		catch (Exception e)
		{
			//e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public static void ignoreExceptions(ConsumerWithExceptions supplier)
	{
		try
		{
			supplier.accept();
		}
		catch (Exception e)
		{
			//e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	@FunctionalInterface
	public interface SupplierWithExceptions<T>
	{
		public T get() throws Exception;
	}
	@FunctionalInterface
	public interface ConsumerWithExceptions
	{
		public void accept() throws Exception;
	}
}
