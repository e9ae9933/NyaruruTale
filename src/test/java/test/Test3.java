package test;

import io.github.e9ae9933.nyaruru.SharedConstants;
import io.github.e9ae9933.nyaruru.client.core.ClientTickInfo;
import io.github.e9ae9933.nyaruru.client.core.Component;
import io.github.e9ae9933.nyaruru.client.core.Scene;
import io.github.e9ae9933.nyaruru.client.core.swing.SwingFrame;
import io.github.e9ae9933.nyaruru.client.renderer.Texture;

import java.awt.*;
import java.util.Deque;
import java.util.LinkedList;

public class Test3
{
	public static void main(String[] args)
	{
		SwingFrame frame=new SwingFrame();
		frame.show(800, 600, new Scene()
		{
			int ticks;
			Deque<Long> fps=new LinkedList<>();
			@Override
			public void clientTick(ClientTickInfo info)
			{
				long that=System.currentTimeMillis();
				ticks++;
				Texture texture=info.texture;
				texture.drawLine(100,100,100+ticks%500,100, Color.BLACK,5);
				long cur=System.currentTimeMillis();
				fps.add(cur);
				while(!fps.isEmpty()&&fps.getFirst()+1000<=cur)
					fps.pollFirst();

				texture.drawString("ABCDEFGHIJKLMNopqrstuvwxyz\n"+
						SharedConstants.gson.toJson(info)
				,0,0,16,4,4,new Color(0));
			}
		});
	}
}
