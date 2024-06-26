package io.github.e9ae9933.nyaruru;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.e9ae9933.nyaruru.client.ResourceHelper;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.StopWatch;

import javax.swing.*;
import java.awt.*;
import java.awt.image.VolatileImage;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.IntStream;

@SuppressWarnings({"deprecation", "NonAsciiCharacters"})
public class SharedConstants
{
	public static final int IMAGE_LEAK=-1;
	public static boolean debugPixelLinerBoxes=false;
	public static final boolean ALLOW_UNSAFE_BEHAVIORS=false;
	public  static boolean debug=System.getProperties().containsKey("debug");
	public static boolean maximumCompatibility=System.getProperties().contains("maximumCompatibility");
//	public static Map<Pair<Font,Integer>, BufferedImage> volatileTextureFontBuffer=new LinkedHashMap<>();
	public static int tabWidth=4;
	public static int skip=3;
	final static HashSet<VolatileImage> bufferedImages=new HashSet<>();
	final static HashMap<String,Font> bufferedFonts=new HashMap<>();
	public static GraphicsConfiguration graphicsConfiguration=GraphicsEnvironment.
			getLocalGraphicsEnvironment().
			getDefaultScreenDevice().
			getDefaultConfiguration();
	static
	{
		Vector<VolatileImage> imgs=new Vector<>();
//		for(int i=3;i<=10;i++)
//		{
//			int n=(10-i+1)*2;
//			int size=1<<i;
//			System.out.println("cacheing (%d, %d) x %d".formatted(size,size,n));
//			IntStream.range(0,n)
////					.parallel()
//					.forEach(j->imgs.add(requestVolatileBuffer2(size,size)));
//		}
		imgs.forEach(img->returnVolatileBuffer(img));
		imgs.clear();
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		toolkit.getFontMetrics(get宋体Font());
	}
	public static VolatileImage requestVolatileBuffer2(int w, int h)
	{
		try
		{
			synchronized (bufferedImages)
			{
				Optional<VolatileImage> opt = bufferedImages.stream()
						.filter(i ->{
							int width=i.getWidth();
							int height=i.getHeight();
							return w==width&&h==height;
						})
						.findFirst();
				opt.ifPresent(i ->
				{
					bufferedImages.remove(i);
					Graphics2D g2d = i.createGraphics();
					g2d.setColor(Color.GREEN);
					g2d.fillRect(0, 0, w, h);
					int rt = i.validate(graphicsConfiguration);
					if (rt == VolatileImage.IMAGE_INCOMPATIBLE)
						throw new RuntimeException("incompatible");
					if (rt == VolatileImage.IMAGE_RESTORED || i.contentsLost())
					{
						System.err.println("restored or lost on request");
					}
				});
				//okay then
				return opt.orElseGet(() -> {
					StopWatch sw=StopWatch.createStarted();
					VolatileImage img=graphicsConfiguration.
						createCompatibleVolatileImage(w, h, Transparency.TRANSLUCENT);
					img.validate(graphicsConfiguration);
					System.out.println("create (%d, %d) used %.3f ms".formatted(w,h,sw.getNanoTime()/1e6));
					return img;
				});
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{

		}
	}
//	@CallerSensitive
	public static void returnVolatileBuffer(VolatileImage image)
	{
		synchronized (bufferedImages)
		{
			Graphics2D g2d = image.createGraphics();
			g2d.setColor(Color.GREEN);
			g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
			int rt = image.validate(graphicsConfiguration);
			if (rt == VolatileImage.IMAGE_INCOMPATIBLE)
				throw new RuntimeException("incompatible");
			if (rt == VolatileImage.IMAGE_RESTORED || image.contentsLost())
				System.err.println("restored or lost on return");
			bufferedImages.add(image);
		}
	}
	public static Gson gson=new GsonBuilder()
			.disableHtmlEscaping()
			.serializeNulls()
			.serializeSpecialFloatingPointValues()
			.setPrettyPrinting()
			.create();
	public static Font getUnifontFont()
	{
		return getFont("unifont-15.1.02.otf");
	}
	public static Font getMarsNeedsCunnilingusFont()
	{
		return getFont("Mars_Needs_Cunnilingus.ttf");
	}
	public static Font get8BitWonderFont()
	{
		return getFont("8-BIT_WO.ttf");
	}
	public static Font getDeterminationFont()
	{
		return getFont("Determination.otf");
	}
	public static Font get宋体Font()
	{
		return new Font("宋体",Font.PLAIN,0);
	}
	public static Font getFont(String s)
	{
		if(!bufferedFonts.containsKey(s.toLowerCase()))
			try
			{
				byte[] b=ResourceHelper.readResource(s);
				Font f=Font.createFont(Font.TRUETYPE_FONT,new ByteArrayInputStream(b));
				bufferedFonts.put(s.toLowerCase(),f);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		return bufferedFonts.get(s.toLowerCase());
	}
	public synchronized static void showErrorDialog(Throwable e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.close();
		JOptionPane.showMessageDialog(
				null,
				"Error occured:\n" +
						sw.toString(),
				"Something went wrong",
				JOptionPane.ERROR_MESSAGE
		);
	}
}
