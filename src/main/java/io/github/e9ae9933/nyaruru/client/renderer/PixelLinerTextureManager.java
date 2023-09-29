package io.github.e9ae9933.nyaruru.client.renderer;

import io.github.e9ae9933.nyaruru.client.ResourceHelper;
import io.github.e9ae9933.nyaruru.pxlsloader.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PixelLinerTextureManager
{
	private final String path;
	private final Supplier<Texture> creator;
	private final Map<String,PxlCharacter> cache;
	private final Map<PxlLayer,Texture> cachedTextures;
//	private final Map<PxlFrame,FrameAnimator>
//	private final Object lock=new Object();
	public PixelLinerTextureManager(String path,Supplier<Texture> creator)
	{
		this.path=(path.isEmpty()?"":path+"/");
		this.creator=creator;
		cache=new HashMap<>();
		cachedTextures=new HashMap<>();
	}
//	public void loadAll()
//	{
//		synchronized (lock)
//		{
//
//		}
//	}
	public PxlCharacter getCharacterByName(String s)
	{
		synchronized (cache)
		{
			PxlCharacter pc=cache.get(s.toLowerCase());
			if(pc!=null)
				return pc;
			cache.put(s.toLowerCase(),pc=new PxlCharacter(
					ResourceHelper.readResource("%s%s.pxls".formatted(path,s)),
					this
			));
			return pc;
		}
	}
	public Texture getTexture(PxlLayer layer)
	{
		synchronized (cachedTextures)
		{
			if(!cachedTextures.containsKey(layer))
			{
				Texture t = creator.get();
				t.createFrom(layer.getImage());
//				System.out.println("create texture");
				cachedTextures.put(layer, t);
			}
			return cachedTextures.get(layer);
		}
	}
	public FrameAnimator createFrameAnimator(PxlSequence f)
	{
		return new FrameAnimator(this,f);
	}
}
