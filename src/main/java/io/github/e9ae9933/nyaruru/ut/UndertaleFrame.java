package io.github.e9ae9933.nyaruru.ut;

import io.github.e9ae9933.nyaruru.MathHelper;
import io.github.e9ae9933.nyaruru.client.core.ClientTickInfo;
import io.github.e9ae9933.nyaruru.client.core.Component;
import io.github.e9ae9933.nyaruru.client.renderer.FrameAnimator;
import io.github.e9ae9933.nyaruru.client.renderer.PixelLinerTextureManager;
import io.github.e9ae9933.nyaruru.client.renderer.Texture;
import io.github.e9ae9933.nyaruru.core.TickRecorder;
import io.github.e9ae9933.nyaruru.core.TickScheduler;
import io.github.e9ae9933.nyaruru.pxlsloader.PxlCharacter;
import org.apache.commons.lang3.time.StopWatch;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class UndertaleFrame extends Component
{
	public UndertaleFrame()
	{
	}
	TickRecorder tickRecorder=new TickRecorder(1000,false);
	Rectangle box=new Rectangle(38,256,564,130);
	boolean firstTick=true;
	PixelLinerTextureManager textureManager;
	PxlCharacter chara;
	FrameAnimator[] buttonAnimator=new FrameAnimator[4];
//	Consumer<ClientTickInfo>[] buttonConsumer=new Consumer[4];
	Consumer<ClientTickInfo> buttonConsumer;
	Consumer<ClientTickInfo> targetConsumer;
	TickScheduler<ClientTickInfo> nextTickEntry=new TickScheduler<>();
	FrameAnimator fight;
	FrameAnimator ptr;
	FrameAnimator knife;
	FrameAnimator damage;
	Predicate<ClientTickInfo> createFight()
	{
		fight.reset();
		ptr.reset();
		knife.reset();
		return new Predicate<ClientTickInfo>()
		{
			int ticks=-1;
			int ticks2=0;
			int ticks3=0;
			int ticks4=0;
			int from,to,maxHp;
			boolean stopped;
			boolean handleFight;
			@Override
			public boolean test(ClientTickInfo info)
			{
				if(ticks==-1)
				{
					if (info.keyPressed.isFirstPressedAndRemove(KeyEvent.VK_X))
						return false;
					else if (info.keyPressed.isFirstPressedAndRemove(KeyEvent.VK_Z))
					{
						ticks=0;
					}
					info.texture.drawScaledString("* 镁塔顿\n中国智造，惠及全球\n实验室。",
							100, 256 + 15, new Font("宋体", Font.PLAIN, 12),
							8, 8, Color.WHITE, 2);
				}
				else
				{
					if(info.keyPressed.isFirstPressedAndRemove(KeyEvent.VK_Z))
						stopped=true;
					if(!handleFight)
						fight.renderTo(info.texture, (int) box.getCenterX(), (int) box.getCenterY(),0);
					int l=box.x,r=box.x+box.width;
					int time= (int) (230*0.75);
					if(ticks>time)
						return false;
					int pos=(r-l)*ticks/time+l;
					if(!handleFight)
					ptr.renderTo(info.texture,pos, ((int) box.getCenterY()),0);
					if(stopped)
					{
						ticks2++;
						if(ticks2%(230/10)==0)
							ptr.stepFrame();
						if(ticks2%(230/6)==0)
							knife.stepFrame();
						if(knife.loopedCount==0)
							knife.renderTo(info.texture,(int)box.getCenterX(),box.y-150,0,1.5,1.5);
						else {
							ticks3++;
							int limit=42360/42360*230*4;
							if(limit>=ticks3)
							{
								int lim = (int) (230 / 2);
								int lim2=230;
								int up = 240;
								double x = ticks3 * 1.0 / lim;
								double y = 4 * (-x * x + x);
								if (y < 0) y = 0;
								int scale = 30;
								int px = (int) box.getCenterX();
								int py = (int) (box.getCenterY() - up - (int) (y * scale));
								from = (int) 1e9;
								to = from - 998244353;
								maxHp = from;
								int[] dmg = {9, 9, 8, 2, 4, 4, 3, 5, 3};
								for (int i = 0; i < dmg.length; i++)
								{
									int pos2 = (int) ((i - (dmg.length - 1) / 2.0) * 16 * 2);
									damage.position = dmg[i];
									damage.renderTo(info.texture, px + pos2, py, 0, 1, 1);
								}
								int all = 149;
								info.texture.drawRect(px - all - 1, py + 20, all * 2 + 2, 15, Color.BLACK, 1);
								info.texture.fillRect(px - all, py + 21, all * 2, 13, new Color(64, 64, 64));
								int w =Math.max(to>0?1:0,(int)MathHelper.ratio(MathHelper.ratio(1.0 * ticks3 / lim2, from, to) / maxHp, 0, 2 * all - 1));
								info.texture.fillRect(px - all, py + 21, w, 13, Color.GREEN);
							}else {

								if(handleFight)
								{
									ticks4++;
									int limit3=230/4;
									if(ticks4>limit3)
										return false;

									fight.renderTo(info.texture, (int) box.getCenterX(), (int) box.getCenterY(),
											0,
											MathHelper.ratio(ticks4*1.0/limit3,1,0),
											1,
											(float)MathHelper.ratio(ticks4*1.0/limit3,1,0)
											);
								}
								handleFight=true;
							}
						}
					}
					else
					{
						ticks++;
					}
				}
				return true;
			}
		};
	}
	void init(ClientTickInfo info)
	{
		textureManager=info.textureManager;
		chara=textureManager.getCharacterByName("undertale");
		fight=chara.getPoseByName("atk").getSequence(0).createFrameAnimator();
		ptr=chara.getPoseByName("ptr").getSequence(0).createFrameAnimator();
		knife=chara.getPoseByName("knife").getSequence(0).createFrameAnimator();
		damage=chara.getPoseByName("damage").getSequence(0).createFrameAnimator();
		buttonAnimator[0]=chara.getPoseByName("fight").getSequence(0).createFrameAnimator();
		buttonAnimator[1]=chara.getPoseByName("act").getSequence(0).createFrameAnimator();
		buttonAnimator[2]=chara.getPoseByName("item").getSequence(0).createFrameAnimator();
		buttonAnimator[3]=chara.getPoseByName("mercy").getSequence(0).createFrameAnimator();
		buttonAnimator[0].stepFrame();
		buttonConsumer= new Consumer<ClientTickInfo>()
		{
			int target=0;
			Predicate<ClientTickInfo> targetPredicate;
			@Override
			public void accept(ClientTickInfo inf)
			{
				if(targetPredicate!=null)
				{
					if (!targetPredicate.test(inf))
						targetPredicate = null;
//					else return;
				}
				else if (inf.keyPressed.isFirstPressedAndRemove(KeyEvent.VK_Z))
				{
					switch (target)
					{
						case 0:
							targetPredicate=createFight();
					}
				} else
				{
					if (inf.keyPressed.isFirstPressedAndRemove(KeyEvent.VK_LEFT))
					{
						int next = (target + 3) % 4;
						buttonAnimator[target].stepFrame();
						buttonAnimator[next].stepFrame();
						target=next;
					}
					if (inf.keyPressed.isFirstPressedAndRemove(KeyEvent.VK_RIGHT))
					{
						int next = (target + 1) % 4;
						buttonAnimator[target].stepFrame();
						buttonAnimator[next].stepFrame();
						target=next;
					}
				}
				for (int i = 0; i < 4; i++)
				{
					buttonAnimator[i].renderTo(inf.texture,
							32 + 55 + (110 + 43) * i + (i >= 2 ? 7 : 0), 453, 0
					);
				}
			}
		};
		targetConsumer=buttonConsumer;
		firstTick=false;
	}
	@Override
	public void clientTick(ClientTickInfo info)
	{
		//230fps
		StopWatch sw=StopWatch.createStarted();
		if(!!firstTick)
			init(info);
		tickRecorder.tick();
		info.texture.fillRect(0,0,640,480, Color.BLACK);
		targetConsumer.accept(info);
		drawOuter(info.texture,box.x,box.y,box.x+box.width,box.y+box.height,6,Color.WHITE);


		info.texture.drawString("%s\n%.3f ms".formatted(tickRecorder.report(),sw.getNanoTime()/1e6),0,12,12,1,1,Color.WHITE);
		tickRecorder.waitUntilNextTick(230);
	}
	void drawOuter(Texture t, int x1, int y1, int x2, int y2, int w,Color c)
	{
		t.fillRect(x1-w,y1-w,x2-x1+w,w,c);
		t.fillRect(x2,y1-w,w,y2-y1+w,c);
		t.fillRect(x1,y2,x2-x1+w,w,c);
		t.fillRect(x1-w,y1,w,y2-y1+w,c);
	}
}
