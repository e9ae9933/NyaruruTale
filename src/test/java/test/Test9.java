package test;

import io.github.e9ae9933.nyaruru.MathHelper;
import io.github.e9ae9933.nyaruru.client.core.ClientTickInfo;
import io.github.e9ae9933.nyaruru.client.core.Scene;
import io.github.e9ae9933.nyaruru.client.core.swing.SwingFrame;
import io.github.e9ae9933.nyaruru.client.renderer.FrameAnimator;
import io.github.e9ae9933.nyaruru.client.renderer.PixelLinerTextureManager;
import io.github.e9ae9933.nyaruru.client.renderer.vlt.VolatileTexture;
import io.github.e9ae9933.nyaruru.core.TickRecorder;
import io.github.e9ae9933.nyaruru.pxlsloader.PxlCharacter;
import io.github.e9ae9933.nyaruru.pxlsloader.PxlPose;
import io.github.e9ae9933.nyaruru.pxlsloader.PxlSequence;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.StringJoiner;

public class Test9
{
	public static void main(String[] args) throws Exception
	{
//		byte[] b=ResourceHelper.readResource("test.jpg");
//		BufferedImage bi=ImageIO.read(new ByteArrayInputStream(b));


		SwingFrame frame = new SwingFrame();
		PixelLinerTextureManager manager = new PixelLinerTextureManager("", () -> new VolatileTexture());
		PxlCharacter chara = manager.getCharacterByName("undertale");
//		a.step();
		TickRecorder tickRecorder = new TickRecorder();
		frame.show(800, 600, new Scene()
		{
			int ticks;
			int poseId = 0, sequenceId = 0;
			PxlPose pose=chara.getPose(poseId);
			PxlSequence sequence=pose.getSequence(sequenceId);
			FrameAnimator a=sequence.createFrameAnimator();

			boolean paused;
			@Override
			public void clientTick(ClientTickInfo info)
			{
				if (info.keyPressed.isFirstPressed(KeyEvent.VK_LEFT))
				{
					sequenceId = MathHelper.rangeDecrease(
							sequenceId, pose.getSequenceCount(),
							() -> (pose = chara.getPose(
									poseId = MathHelper.rangeDecrease(poseId, chara.getPoseCount())
							))
									.getSequenceCount() - 1);
					sequence=pose.getSequence(sequenceId);
					a=sequence.createFrameAnimator();
				}
				if (info.keyPressed.isFirstPressed(KeyEvent.VK_RIGHT))
				{
					sequenceId = MathHelper.rangeIncrease(
							sequenceId, pose.getSequenceCount(),
							() -> pose=chara.getPose(poseId=MathHelper.rangeIncrease(poseId,chara.getPoseCount())));
					sequence=pose.getSequence(sequenceId);
					a=sequence.createFrameAnimator();
				}
				if(info.keyPressed.isFirstPressed(KeyEvent.VK_SPACE))
					paused=!paused;
//				info.texture.drawRect(400,200,10,10,Color.BLACK,10);
//				a.get().renderTo(info.texture.getSubImageReference(200,100,0,0,true),200,100,ticks/60.0*MathHelper.PI*0);
				a.renderTo(info.texture, 400, 200, 0);
				info.texture.fillRect(400 - 2, 200 - 2, 4, 4, Color.BLACK);
				if(!paused)
					a.step();
				if(paused)
					info.texture.drawString("Paused",50,380,16,4,4,Color.BLACK);
				ticks++;
				tickRecorder.tick();
				info.texture.drawString("(1-indexed) pose %d / %d, sequence %d / %d".formatted(poseId+1,chara.getPoseCount(),sequenceId+1,pose.getSequenceCount()),50,400,16,4,4,Color.BLACK);
				info.texture.drawString(a.toString(), 50, 440, 16, 4, 4, Color.BLACK);
//				while(tickRecorder.getTps()>60)Thread.yield();
				tickRecorder.waitUntilNextTick(60.0);
				info.texture.drawString(tickRecorder.report(), 50, 420, 16, 4, 4, new Color(0xFF000000, true));
				if (!info.keyPressed.isEmpty())
				{
					StringJoiner sj = new StringJoiner(",\n\t", "{\n\t", "\n}");
					info.keyPressed.forEach((i, j) ->
					{
						sj.add("%s: %d".formatted(KeyEvent.getKeyText(i), j));
					});
					info.texture.drawString(sj.toString(), 50, 460, 16, 4, 4, Color.BLACK);
				}
			}
		});
	}
}
