package io.github.e9ae9933.nyaruru.pxlsloader;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
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
	public static File directory=new File(".");
	public static byte[] chooseFileAndGetAllBytes(FileFilter f)
	{
		JFileChooser c=new JFileChooser(directory);
		c.setFileSelectionMode(JFileChooser.FILES_ONLY);
		c.setMultiSelectionEnabled(false);
		c.setFileFilter(f);
		int rt=c.showDialog(null,null);
		if(rt!=0) throw new RuntimeException("User cancelled file choosing");
		directory=c.getCurrentDirectory();
		try(FileInputStream is=new FileInputStream(c.getSelectedFile()))
		{
			return is.readAllBytes();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	public static File chooseDirectory()
	{
		JFileChooser c=new JFileChooser(directory);
		c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		c.setMultiSelectionEnabled(false);
//		c.setFileFilter(f);
		int rt=c.showDialog(null,null);
		if(rt!=0) throw new RuntimeException("User cancelled file choosing");
		directory=c.getCurrentDirectory();
		return c.getSelectedFile();
	}
	public static void info(String msg)
	{
		JOptionPane.showMessageDialog(null,msg,null,JOptionPane.INFORMATION_MESSAGE);
	}
	public static void warning(String msg)
	{
		JOptionPane.showMessageDialog(null,msg,null,JOptionPane.WARNING_MESSAGE);
	}
	public static void fatal(String msg)
	{
		JOptionPane.showMessageDialog(null,msg,null,JOptionPane.ERROR_MESSAGE);
	}
	public static Gson gson=new GsonBuilder().registerTypeAdapterFactory(TypeAdapters.newFactory(Class.class, new TypeAdapter<Class>()
	{
		@Override
		public void write(JsonWriter out, Class value) throws IOException
		{
			if(value==null)
				out.nullValue();
			else
				out.value(value.getName());
		}

		@Override
		public Class read(JsonReader in) throws IOException
		{
			try
			{
				return Class.forName(in.nextString());
			} catch (ClassNotFoundException e)
			{
				throw new IOException(e);
			}
		}
	})).registerTypeHierarchyAdapter(File.class,new TypeAdapter<File>()
	{
		@Override
		public void write(JsonWriter out, File value) throws IOException
		{
			if(value==null)
				out.nullValue();
			else
				out.value(value.getPath());
		}

		@Override
		public File read(JsonReader in) throws IOException
		{
			return new File(in.nextString());
		}
	}).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)/*.serializeNulls()*/.setPrettyPrinting().serializeSpecialFloatingPointValues().disableHtmlEscaping().create();
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
