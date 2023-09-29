package test;

import io.github.e9ae9933.nyaruru.client.ResourceHelper;
import io.github.e9ae9933.nyaruru.client.core.ClientTickInfo;
import io.github.e9ae9933.nyaruru.client.core.Scene;
import io.github.e9ae9933.nyaruru.client.core.swing.SwingFrame;
import io.github.e9ae9933.nyaruru.client.renderer.PixelLinerTextureManager;
import io.github.e9ae9933.nyaruru.client.renderer.Texture;
import io.github.e9ae9933.nyaruru.client.renderer.graphics.GraphicsTexture;
import io.github.e9ae9933.nyaruru.client.renderer.vlt.VolatileTexture;
import io.github.e9ae9933.nyaruru.core.TickRecorder;
import io.github.e9ae9933.nyaruru.pxlsloader.PxlCharacter;
import io.github.e9ae9933.nyaruru.pxlsloader.PxlLayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

public class Test7
{
	public static void main(String[] args) throws Exception
	{
//		byte[] b=ResourceHelper.readResource("test.jpg");
//		BufferedImage bi=ImageIO.read(new ByteArrayInputStream(b));


		SwingFrame frame=new SwingFrame();
		PixelLinerTextureManager manager=new PixelLinerTextureManager("",()->new VolatileTexture());
		PxlCharacter chara=manager.getCharacterByName("noel");
		TickRecorder tickRecorder=new TickRecorder();
		frame.show(800, 600, new Scene()
		{
			int ticks;
			@Override
			public void clientTick(ClientTickInfo info)
			{
				PxlLayer l=chara.getPoseByName("run")
						.getSequence(0)
						.getFrame(ticks/100%12)
						.getLayerByName("Layer");
				info.texture.drawLine(100,200,500,600,new Color(0),4);
				Texture t=l.getTexture();
				info.texture.drawTexture(t,100,100,ticks/200.0);

				Texture tt=info.texture.getSubImageReference(100,200,300,400,true);
				tt.drawTexture(l.getTexture(),50,50,ticks);
				tt.drawRect(0,0,300,400,Color.BLACK,5);
//				System.gc();

				ticks++;
				tickRecorder.tick();
				info.texture.drawString(tickRecorder.report(), 100,400,16,4,4,new Color(0xFF000000,true));
			}
		});
	}
}
