package test;

import io.github.e9ae9933.nyaruru.client.renderer.graphics.GraphicsTexture;

import javax.swing.*;
import java.awt.*;

public class Test2
{
	public static void main(String[] args)
	{
		GraphicsTexture gt=new GraphicsTexture();
		gt.createNew(1280,720);
		gt.fillRect(100,200,400,300, Color.BLACK);
		gt.drawLine(200,200,300,400,new Color(0x7f66ccff,true),5);
		gt.drawLine(200,500,500,300,new Color(0xffee0000),1);
		gt.drawLine(300,0,300,600,new Color(0xffffff00),1);
		gt.drawLine(0,200,600,200,new Color(0xffff0000),1);
		gt.drawChar('原',300,500,32,new Color(0x00ff00));
		gt.drawChar('神',332,500,32,new Color(0x00ff00));
		gt.drawChar('1',300,532,32,new Color(0x00ff00));
		gt.drawChar('2',316,532,32,new Color(0x00ff00));
		JFrame frame=new JFrame();
		frame.setContentPane(new JLabel(new ImageIcon(gt.toBufferedImage())));
		frame.pack();
		frame.setVisible(true);
	}
}
