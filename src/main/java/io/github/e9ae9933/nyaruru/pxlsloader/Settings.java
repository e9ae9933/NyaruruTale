package io.github.e9ae9933.nyaruru.pxlsloader;

import io.github.e9ae9933.nyaruru.client.renderer.PixelLinerTextureManager;
import io.github.e9ae9933.nyaruru.client.renderer.Texture;

import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Settings
{
	Map<Pair<Integer,Double>,Pair<PxlImageAtlas, PxlImageAtlas.Uv>> idMap=new LinkedHashMap<>();
	Map<Pair<Integer,Double>, BufferedImage> idImage=new LinkedHashMap<>();

	PixelLinerTextureManager creator;

}
