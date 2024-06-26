package io.github.e9ae9933.nyaruru.client.core;

import io.github.e9ae9933.nyaruru.client.ResourceHelper;
import org.apache.commons.lang3.time.StopWatch;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.ByteArrayInputStream;

public class AudioHandler
{
	public AudioHandler()
	{
	}
	public Clip playAudio(String name)
	{
		Clip rt;
		StopWatch sw=StopWatch.createStarted();
		try(AudioInputStream ais=AudioSystem.getAudioInputStream(ResourceHelper.readResourceAsURL(name)))
		{
			Clip clip=AudioSystem.getClip();
			rt=clip;
			clip.open(ais);
			clip.start();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			System.err.println("play audio "+name+" used "+sw.getTime()+" ms");
		}
		return rt;
	}
}
