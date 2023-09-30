package io.github.e9ae9933.nyaruru.ut;

import io.github.e9ae9933.nyaruru.SharedConstants;
import io.github.e9ae9933.nyaruru.client.core.ClientTickInfo;
import io.github.e9ae9933.nyaruru.client.core.Scene;
import io.github.e9ae9933.nyaruru.client.renderer.DoNothingTexture;
import io.github.e9ae9933.nyaruru.client.renderer.PixelLinerTextureManager;
import io.github.e9ae9933.nyaruru.client.renderer.Texture;
import io.github.e9ae9933.nyaruru.client.renderer.vlt.VolatileTexture;
import io.github.e9ae9933.nyaruru.core.TickRecorder;

public class UndertaleScene extends Scene
{
	public UndertaleScene(UndertaleFrame frame)
	{
		this.component=frame;
	}
	PixelLinerTextureManager textureManager=new PixelLinerTextureManager("",()->new VolatileTexture());
	TickRecorder tickRecorder=new TickRecorder(5000,false);
	Texture lastTexture;
	@Override
	public void clientTick(ClientTickInfo info)
	{
		info.textureManager=textureManager;
		if(tickRecorder.getTps()> SharedConstants.fps &&lastTexture!=null)
		{
			Texture theTexture=info.texture;
			theTexture.drawTexture(lastTexture,0,0);
			info.texture = new DoNothingTexture(info.texture.getWidth(), info.texture.getHeight());
			component.clientTick(info);
			info.texture=theTexture;
		}
		else
		{
			component.clientTick(info);
			tickRecorder.tick();
		}
	}
}
