package io.github.e9ae9933.nyaruru;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.e9ae9933.nyaruru.client.ResourceHelper;
import io.github.e9ae9933.nyaruru.client.renderer.graphics.GraphicsTexture;
import org.apache.commons.lang3.Validate;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class SharedConstants
{
	private static GraphicsTexture.UniChar[] unifont;
	private static Font unifontFont;
	public static boolean maximumCompatibility=System.getProperties().contains("maximumCompatibility");
	public static int tabWidth=4;
	public static Gson gson=new GsonBuilder()
			.disableHtmlEscaping()
			.serializeNulls()
			.serializeSpecialFloatingPointValues()
			.setPrettyPrinting()
			.create();
	public static Font getUnifontFont()
	{
		if(unifontFont==null)
		{
			try
			{
				byte[] b = ResourceHelper.readResource("unifont-15.1.02.otf");
				unifontFont=Font.createFont(Font.TRUETYPE_FONT, new ByteArrayInputStream(b));
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		return unifontFont;
	}
	@SuppressWarnings("StatementWithEmptyBody")
	public static GraphicsTexture.UniChar[] getUnifont()
	{
		if(unifont!=null)
			return unifont;
		GraphicsTexture.UniChar[] fontPage;
		//read unifont
		byte[] b= ResourceHelper.readResource("unifont-15.1.01.bdf");
		Scanner cin=new Scanner(new String(b));
		while(!cin.nextLine().equalsIgnoreCase("endproperties"));
		fontPage=new GraphicsTexture.UniChar[65536];
		int n=Integer.parseInt(cin.nextLine().split(" ")[1]);
//		System.err.println("n "+n);
		for(int i=0;i<n;i++)
		{
//			System.err.println(i);
//			Validate.isTrue(cin.nextLine().equals("STARTCHAR"));
			cin.nextLine();
//			System.out.println(cin.nextLine());
			int pos=Integer.parseInt(cin.nextLine().split(" ")[1]);
			cin.nextLine();
			cin.nextLine();
			int w=Integer.parseInt(cin.nextLine().split(" ")[1]);
			if(w!=8&&w!=16)
				throw new UnsupportedOperationException("width must be 8 or 16 for unifont");
//			Validate.isTrue(cin.nextLine().equals("BITMAP"));
			cin.nextLine();
			GraphicsTexture.UniChar page=fontPage[pos]=new GraphicsTexture.UniChar();
			page.wide=w==16;
			for(int j=0;j<16;j++)
			{
				int point=Integer.parseInt(cin.nextLine(),16);
				page.bites[j]=(short)point;
			}
			Validate.isTrue(cin.nextLine().equals("ENDCHAR"));
		}
		cin.close();
		return unifont=fontPage;
	}
}