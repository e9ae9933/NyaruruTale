package test;

import io.github.e9ae9933.nyaruru.client.ResourceHelper;
import org.apache.commons.lang3.Validate;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
public class Test6Gl
{
	public static void main(String[] args) throws Throwable
	{
		Validate.isTrue(glfwInit());
		System.out.println(glfwGetVersionString());
//		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,3);
//		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,3);
		glfwWindowHint(GLFW_RESIZABLE,GLFW_FALSE);
		long handle=glfwCreateWindow(800,600,"Test",NULL,NULL);
		Validate.isTrue(handle!=0);
		glfwMakeContextCurrent(handle);
		GL.createCapabilities();

		int[] ans=new int[1];

		int vertexShader=glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, ResourceHelper.readResourceAsUTF("vertex_shader.shader"));
		glCompileShader(vertexShader);
		glGetShaderiv(vertexShader,GL_COMPILE_STATUS,ans);
		System.out.println(ans[0]+" "+glGetShaderInfoLog(vertexShader));

		int fragmentShader=glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader,ResourceHelper.readResourceAsUTF("fragment_shader.shader"));
		glCompileShader(fragmentShader);
		glGetShaderiv(fragmentShader,GL_COMPILE_STATUS,ans);
		System.out.println(ans[0]+" "+glGetShaderInfoLog(vertexShader));

		int shaderProgram=glCreateProgram();
		glAttachShader(shaderProgram,vertexShader);
		glAttachShader(shaderProgram,fragmentShader);
		glLinkProgram(shaderProgram);
		glGetShaderiv(fragmentShader,GL_LINK_STATUS,ans);
		System.out.println(ans[0]+" "+glGetShaderInfoLog(vertexShader));

		glUseProgram(shaderProgram);
		glDeleteShader(fragmentShader);
		glDeleteShader(vertexShader);

		BufferedImage bi=ImageIO.read(ClassLoader.getSystemResourceAsStream("test.jpg"));
		ByteBuffer bb= BufferUtils.createByteBuffer(4*bi.getWidth()*bi.getHeight());
		System.out.println(bi.getWidth()+" "+bi.getHeight());
		for(int j=0;j<bi.getHeight();j++)
			for(int i=0;i<bi.getWidth();i++)
			{
				Color c=new Color(bi.getRGB(i,j),true);
				bb.put((byte) c.getRed());
				bb.put((byte) c.getGreen());
				bb.put((byte) c.getBlue());
				bb.put((byte) c.getAlpha());
//				for(int ii=0;ii<1;ii++)
//				{
//					bb.put((byte)3);
//					bb.put((byte)100);
//					bb.put((byte)255);
//					bb.put((byte)127);
//				}
			}
		bb.flip();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		int texture=glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D,texture);
		glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA,bi.getWidth(),bi.getHeight(),0,GL_RGBA,GL_UNSIGNED_BYTE,bb);
		glUniform1i(glGetUniformLocation(shaderProgram,"ourTexture"),0);
//		glGenerateMipmap(GL_TEXTURE_2D);
//		glBindTexture(GL_TEXTURE_2D,0);
//		glEnable(GL_TEXTURE_2D);


		try(MemoryStack ms=stackPush())
		{
			FloatBuffer f=ms.callocFloat(12);
			f.put(new float[]{
					-0.5f,-0.5f,-0.5f,-0.5f,
					0.5f,-0.5f,0.5f,-0.5f,
					0.5f,0.5f,0.5f,0.5f
			});
			f.flip();
			int vbo =glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER,f,GL_STATIC_DRAW);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0,4,GL_FLOAT,false,0,0);
		}

		while(!glfwWindowShouldClose(handle))
		{
			glDrawArrays(GL_TRIANGLES,0,3);
			glfwSwapBuffers(handle);
			glfwPollEvents();
		}
		glfwTerminate();
	}
}
