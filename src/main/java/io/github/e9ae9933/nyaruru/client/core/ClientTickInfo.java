package io.github.e9ae9933.nyaruru.client.core;

import io.github.e9ae9933.nyaruru.client.renderer.Texture;

import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Set;

public class ClientTickInfo
{
	public transient Texture texture;
	public int renderWidth;
	public int renderHeight;
	public KeyMap keyPressed;
	public boolean mouseInBounds;
	public int mouseX;
	public int mouseY;
	public boolean mouseClicking;
	public ClientTickInfo(){}

	public ClientTickInfo(Texture texture, int renderWidth, int renderHeight, KeyMap keyPressed, boolean mouseInBounds, int mouseX, int mouseY, boolean mouseClicking)
	{
		this.texture = texture;
		this.renderWidth = renderWidth;
		this.renderHeight = renderHeight;
		this.keyPressed = keyPressed;
		this.mouseInBounds = mouseInBounds;
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.mouseClicking = mouseClicking;
	}
}