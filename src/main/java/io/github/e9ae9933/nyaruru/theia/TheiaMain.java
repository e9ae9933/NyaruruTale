package io.github.e9ae9933.nyaruru.theia;

import io.github.e9ae9933.nyaruru.client.core.swing.SwingFrame;
import io.github.e9ae9933.nyaruru.ut.UndertaleFrame;
import io.github.e9ae9933.nyaruru.ut.UndertaleScene;

public class TheiaMain
{
	public static void main(String[] args)
	{
		SwingFrame sf=new SwingFrame();
		sf.show(640,480,new UndertaleScene(new UndertaleFrame()));
	}
}
