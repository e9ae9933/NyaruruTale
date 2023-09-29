package test;

import io.github.e9ae9933.nyaruru.SharedConstants;
import io.github.e9ae9933.nyaruru.client.core.ClientTickInfo;
import io.github.e9ae9933.nyaruru.client.core.Scene;
import io.github.e9ae9933.nyaruru.client.core.swing.SwingFrame;
import io.github.e9ae9933.nyaruru.client.renderer.PixelLinerTextureManager;
import io.github.e9ae9933.nyaruru.client.renderer.Texture;
import io.github.e9ae9933.nyaruru.client.renderer.graphics.GraphicsTexture;
import io.github.e9ae9933.nyaruru.pxlsloader.PxlCharacter;
import io.github.e9ae9933.nyaruru.pxlsloader.PxlLayer;

import java.awt.*;
import java.util.Deque;
import java.util.LinkedList;

public class Test4
{
	public static void main(String[] args)
	{
		SwingFrame frame=new SwingFrame();
		PixelLinerTextureManager manager=new PixelLinerTextureManager("",()->new GraphicsTexture());
		PxlCharacter chara=manager.getCharacterByName("noel");
		frame.show(800, 600, new Scene()
		{
			int ticks;
			@Override
			public void clientTick(ClientTickInfo info)
			{
				PxlLayer l=chara.getPoseByName("run")
					.getSequence(0)
					.getFrame(ticks/10%12)
					.getLayerByName("Layer");
				Texture t=l.getTexture();
				info.texture.drawTexture(t,100,100,ticks/200.0);
				GraphicsTexture gt=new GraphicsTexture();
				gt.createFrom(l.getImage());
				info.texture.drawTexture(gt,200,100);
				ticks++;
			}
		});
	}
}
