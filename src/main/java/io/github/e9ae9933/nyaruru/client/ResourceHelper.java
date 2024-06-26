package io.github.e9ae9933.nyaruru.client;

import org.apache.commons.lang3.Validate;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ResourceHelper
{
	public static byte[] readResource(String path)
	{
		try(InputStream is = ResourceHelper.class.getClassLoader().getResourceAsStream(path);)
		{
//			return Objects.requireNonNull(is).readAllBytes();
//			return Validate.notNull(is,"null").readAllBytes();
			return Objects.requireNonNull(is).readAllBytes();
		}
		catch (Exception e)
		{
			throw new RuntimeException("No resource found: "+path,e);
		}
	}
	public static URL readResourceAsURL(String path)
	{
		return Validate.notNull(ResourceHelper.class.getClassLoader().getResource(path),"No resource found: "+path);
	}
	public static String readResourceAsUTF(String path)
	{
		return new String(readResource(path), StandardCharsets.UTF_8);
	}
}
