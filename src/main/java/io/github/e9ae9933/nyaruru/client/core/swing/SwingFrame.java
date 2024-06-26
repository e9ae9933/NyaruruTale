package io.github.e9ae9933.nyaruru.client.core.swing;

import io.github.e9ae9933.nyaruru.SharedConstants;
import io.github.e9ae9933.nyaruru.client.core.*;
import io.github.e9ae9933.nyaruru.client.renderer.vlt.VolatileTexture;
import org.apache.commons.lang3.time.StopWatch;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SwingFrame extends io.github.e9ae9933.nyaruru.client.core.Frame
{
	AtomicReference<JFrame> showingFrame = new AtomicReference<>();

	@SuppressWarnings("SynchronizeOnNonFinalField")
	@Override
	public void show(int w, int h, Scene component)
	{
//		if(showed.getAndSet(true))throw new IllegalStateException("already started");
		synchronized (showingFrame)
		{
			if (showingFrame.get() != null)
				throw new IllegalStateException("already started");
			showingFrame.set(new JFrame());
		}
		JFrame frame = showingFrame.get();
		final Map<Integer, Integer> keys = new HashMap<>();
		final boolean[] isClicking = {false,false};
		final AtomicBoolean running=new AtomicBoolean(false);
		final AtomicBoolean errorOccured=new AtomicBoolean(false);
		final AtomicReference<Thread> theThread=new AtomicReference<>();
		final AudioHandler handler=new AudioHandler();
		final AtomicInteger ticks=new AtomicInteger();
		JPanel panel = new JPanel()
		{
			@Override
			public void paint(Graphics g)
			{
				if (errorOccured.get())
					return;
				synchronized (running)
				{
					if (running.get())
						throw new ConcurrentModificationException();//is that possible?
					running.getAndSet(true);
					Thread t=Thread.currentThread();
					Thread pre=theThread.getAndSet(t);
					if(pre!=null&&pre!=t)
						throw new ConcurrentModificationException("wowie");
					try
					{
						int tick=ticks.incrementAndGet();
//						System.out.println("tick %d started".formatted(tick));
//					this.grabFocus();
						super.paint(g);
						int w = this.getWidth();
						int h = this.getWidth();
						VolatileTexture texture = new VolatileTexture();
						texture.createNew(w, h);


						Point p = getMousePosition();
						KeyMap copied;
						synchronized (keys)
						{
							copied = new KeyMap(keys);
							for (Map.Entry<Integer, Integer> integerIntegerEntry : keys.entrySet())
							{
								integerIntegerEntry.setValue(integerIntegerEntry.getValue() + 1);
							}
						}
						copied.remove(0);
						component.clientTick(new ClientTickInfo(
								texture,
								w,
								h,
								copied,
								p != null,
								p == null ? -1 : p.x,
								p == null ? -1 : p.y,
								isClicking[0],
								isClicking[1],
								null,
								handler
						));
						isClicking[1]=false;

//						texture.validate();
						g.drawImage(texture.toImage(), 0, 0, null);
//						texture.validate();
						texture.end();
						try{
//							Thread.sleep(1000);
						}catch (Exception e){throw new RuntimeException(e);}
//						System.out.println("tick %d end".formatted(tick));
					}
					catch (Throwable e)
					{
						e.printStackTrace();
						SharedConstants.showErrorDialog(e);
						System.exit(-1);
					}
					running.getAndSet(false);
				}
			}
		};
		panel.setFocusable(true);
		panel.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
//				System.err.println(e.paramString());
				synchronized (keys)
				{
//					keys.computeIfAbsent(e.getKeyCode(), (a) -> 0);
					keys.put(e.getKeyCode(), 0);
				}
			}

			@Override
			public void keyPressed(KeyEvent e)
			{
//				System.err.println(e.paramString());
				synchronized (keys)
				{
//					keys.computeIfAbsent(e.getKeyCode(), (a) -> 0);
					keys.put(e.getKeyCode(), 0);
				}
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
//				System.err.println(e.paramString());
				synchronized (keys)
				{
					keys.remove(e.getKeyCode());
				}
			}
		});
		panel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				super.mouseClicked(e);
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				super.mousePressed(e);
				isClicking[0] = true;
				isClicking[1]=true;
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				super.mouseReleased(e);
				isClicking[0] = false;
				isClicking[1]=false;
			}

			@Override
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				super.mouseWheelMoved(e);
			}
		});
		panel.setSize(w, h);
		panel.setPreferredSize(panel.getSize());
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		new Thread(() ->
		{
			while (frame.isShowing())
			{
				while (running.get())
				{
					try
					{
						Thread.sleep(1);
					} catch (Exception e)
					{
						throw new RuntimeException(e);
					}
				}
				frame.repaint();
			}
		}).start();

	}
}
