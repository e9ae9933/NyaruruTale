package test;

import io.github.e9ae9933.nyaruru.client.renderer.graphics.GraphicsTexture;

import javax.swing.*;
import java.awt.*;

public class Test1
{
	public static void main(String[] args)
	{
		GraphicsTexture gt=new GraphicsTexture();
		gt.createNew(1280,720);
		gt.drawLine(100,200,400,500, Color.BLACK,3);
		gt.drawLine(200,600,600,400,Color.RED,4);
		gt.drawLine(400,300,100,100,Color.BLUE,5);
		gt.drawPixel(10,10,Color.GREEN);
		ImageIcon ic=new ImageIcon(gt.toBufferedImage());
		JLabel label=new JLabel(ic);
		JPanel panel=new JPanel();
		panel.add(label);
		JFrame frame=new JFrame();
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
