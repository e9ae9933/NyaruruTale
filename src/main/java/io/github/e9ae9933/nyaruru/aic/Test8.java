package io.github.e9ae9933.nyaruru.aic;

import io.github.e9ae9933.nyaruru.MathHelper;
import io.github.e9ae9933.nyaruru.SharedConstants;
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
import io.github.e9ae9933.nyaruru.pxlsloader.Utils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

public class Test8
{
	public static void main(String[] args) throws Exception
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			JOptionPane.showMessageDialog(null, "请选择一个 PixelLiner 文件。\n以 .pxl 或 .pxls 结尾。", "选择文件", JOptionPane.INFORMATION_MESSAGE);

			byte[] b = Utils.chooseFileAndGetAllBytes(new FileFilter()
			{
				@Override
				public boolean accept(File f)
				{
					return f.isDirectory()||f.getName().toLowerCase().endsWith(".pxl") || f.getName().toLowerCase().endsWith(".pxls");
				}

				@Override
				public String getDescription()
				{
					return ".pxl, .pxls";
				}
			});
			SwingFrame frame = new SwingFrame();
			PixelLinerTextureManager manager = new PixelLinerTextureManager("", () -> new VolatileTexture());
			PxlCharacter chara = manager.getCharacterByBinary(b);

			StringBuilder sb=new StringBuilder();
			chara.printDir(sb);
			Utils.writeAllBytes(new File("test.txt"),sb.toString().getBytes(StandardCharsets.UTF_8));

			Utils.writeAllBytes(new File("test.json"),Utils.gson.toJson(chara).getBytes(StandardCharsets.UTF_8));

			TickRecorder tickRecorder = new TickRecorder();
			frame.show(800, 600, new Scene()
			{
				int ticks;
				int poseId = 0, sequenceId = 0;
				PxlPose pose = chara.getPose(poseId);
				PxlSequence sequence = pose.getSequence(sequenceId);
				FrameAnimator a = sequence.createFrameAnimator();

				boolean paused;
				int copyState;

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
						sequence = pose.getSequence(sequenceId);
						a = sequence.createFrameAnimator();
					}
					if (info.keyPressed.isFirstPressed(KeyEvent.VK_RIGHT))
					{
						sequenceId = MathHelper.rangeIncrease(
								sequenceId, pose.getSequenceCount(),
								() -> pose = chara.getPose(poseId = MathHelper.rangeIncrease(poseId, chara.getPoseCount())));
						sequence = pose.getSequence(sequenceId);
						a = sequence.createFrameAnimator();
					}
					if (info.keyPressed.isFirstPressed(KeyEvent.VK_SPACE))
						paused = !paused;
					if(info.keyPressed.isFirstPressed(KeyEvent.VK_D))
					{
						SharedConstants.debugPixelLinerBoxes=
								Boolean.logicalXor(true,SharedConstants.debugPixelLinerBoxes);
					}
					if(info.keyPressed.isFirstPressed(KeyEvent.VK_R))
					{
						a=sequence.createFrameAnimator();
					}
					if(info.keyPressed.isPressed(KeyEvent.VK_CONTROL)&&info.keyPressed.isPressed(KeyEvent.VK_C))
					{
						if(copyState==0)
							try
							{
								Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(pose.getTitle()), null);
								copyState=1;
							}
							catch (Exception e)
							{
								e.printStackTrace();
								copyState=-1;
							}
					}
					else {
						copyState=0;
					}
					a.renderTo(info.texture, 400, 200, 0);
					info.texture.fillRect(400 - 2, 200 - 2, 4, 4, Color.BLACK);
					if(info.keyPressed.isFirstPressed(KeyEvent.VK_ENTER))
						a.stepFrame();
					else if (!paused)
						a.step();
					if (paused)
						info.texture.drawString("Paused", 50, 380, 16, Color.BLACK);
					ticks++;
					tickRecorder.tick();
					info.texture.drawString("""
							使用 向左箭头 和 向右箭头 切换姿势和序列。
							使用 D 打开边界显示。
							使用 空格 暂停。
							使用 Ctrl + C 复制当前姿势。
							使用 回车 来步进。
							使用 R 来重置。""",50,50,16,Color.BLACK);
					info.texture.drawString("(1-indexed) pose %d / %d, sequence %d / %d".formatted(poseId + 1, chara.getPoseCount(), sequenceId + 1, pose.getSequenceCount()), 50, 400, 16, Color.BLACK);
					info.texture.drawString(a.toString(), 50, 440, 16, Color.BLACK);
					tickRecorder.waitUntilNextTick(60.0);
					info.texture.drawString(tickRecorder.report(), 50, 420, 16, new Color(0xFF000000, true));
					info.texture.drawString("mouse %s%s(%d, %d)%s".formatted(info.mouseInBounds?"in bound ":"",info.mouseClicking?"clicking ":true?"":"\" ",info.mouseX,info.mouseY,info.mouseClicked?" first":""),50,460,16,Color.BLACK);
					if(copyState==1)
						info.texture.drawString("已复制到剪贴板",50,480,16,Color.BLACK);
					else if(copyState==-1)
						info.texture.drawString("无法复制到剪贴板",50,480,16,Color.RED);

					if (!info.keyPressed.isEmpty())
					{
						StringJoiner sj = new StringJoiner(",\n\t", "{\n\t", "\n}");
						info.keyPressed.forEach((i, j) ->
						{
							sj.add("%s: %d".formatted(KeyEvent.getKeyText(i), j));
						});
						info.texture.drawString(sj.toString(), 50, 500, 16, Color.BLACK);
					}
				}
			});
		}
		catch (Exception e)
		{
			StringWriter sw=new StringWriter();
			PrintWriter w=new PrintWriter(sw);
			e.printStackTrace(w);
			w.close();
			sw.close();
			JOptionPane.showMessageDialog(null,sw.toString(),"错误",JOptionPane.ERROR_MESSAGE);
		}
	}
}
