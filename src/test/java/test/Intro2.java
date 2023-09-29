/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package test;

import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

/**
 * After we learnt that LWJGL 3 provides the functions exported by a native library as static Java methods, in this
 * second part of the introductory series we will look at how to communicate data between our Java application and the
 * native library. One example of such data could be a simple array of vectors to upload to OpenGL in order to draw a
 * simple triangle.
 * <p>
 * It is important to know how Java can communicate data with a native library, such as OpenGL. There are generally two
 * different <em>kinds</em> of memory which we can allocate.
 * <ul>
 * <li>memory that lives on the garbage collected Java heap managed by the JVM. This is called "on-heap" or "heap"
 * memory, because with "heap" here we mean the memory region managed by the JVM
 * <li>memory that lives in unmanaged memory the JVM does not know about and will not garbage collect. This is also
 * called "off-heap" memory
 * </ul>
 * The former memory is used for all normal Java object and array allocations. The advantage is that the Java programmer
 * does not have to care about freeing/deallocating that memory once it is not used anymore. The JVM will know this and
 * will reclaim the memory using its built-in garbage collector. And the JVM can employ more sophisticated mechanisms,
 * such as escape analysis, to even completely avoid allocating memory for objects.
 * <p>
 * The downside is that we cannot transfer this kind of memory to a native library because:
 * <ul>
 * <li>native libraries use the C memory model which basically consists of a large virtual region of memory adressable
 * using byte addresses
 * <li>we cannot get the virtual memory byte address of a Java object (actually, we _can_ but it is not part of this
 * introduction)
 * <li>even if we could get the address of a Java object, we have no standards-compliant reliable way of knowing the
 * layout of the memory
 * <li>and worse, the JVM can move memory around freely, so any virtual memory address we obtain of a Java-managed
 * object can potentially change
 * </ul>
 * Because of these limitations, we leave that kind of memory to the JVM for allocating normal Java objects and arrays,
 * and concentrate on the other kind of memory, "off-heap" memory.
 * <p>
 * Using off-heap memory we <em>can</em> get the physical virtual memory address of the allocated memory, which will
 * also not change throughout the lifetime of the process. Therefore, we can communicate this memory address to native
 * libraries. Those native libraries can then read from or write to the memory.
 *
 * @author Kai Burjack
 */
public class Intro2 {

	/**
	 * In this example, we will use OpenGL to draw a single triangle on a window.
	 * <p>
	 * As mentioned above, we have to use off-heap memory for this in order to communicate the virtual memory address to
	 * OpenGL, which in turn will read the data we provided at that address.
	 * <p>
	 * The example here will upload the position vectors of a simple triangle to an OpenGL Vertex Buffer Object.
	 */
	public static void main(String[] args) {
		/*
		 * We know this already from the first part. We call glfwInit() to initialize GLFW.
		 */
		glfwInit();

		/*
		 * Create a visible window using GLFW. This is not the focus of this part of the introduction, but we have to do
		 * it anyway in order to see anything on the screen.
		 */
		long window = createWindow();

		try(MemoryStack ms=MemoryStack.stackPush())
		{
			FloatBuffer buffer=ms.callocFloat(6);
			buffer.put(-0.5f).put(-0.5f);
			buffer.put(+0.5f).put(-0.5f);
			buffer.put(+0.0f).put(+0.5f);

			buffer.flip();

			int vbo = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, vbo);

			glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		}

//		glEnableClientState(GL_VERTEX_ARRAY);
//		glVertexPointer(2, GL_FLOAT, 0, 0L);


		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0,2,GL_FLOAT,false,8,0);
		while (!glfwWindowShouldClose(window)) {
			glfwPollEvents();
			glDrawArrays(GL_TRIANGLES, 0, 3);
			glfwSwapBuffers(window);
		}
		glfwTerminate();
		System.out.println("Fin.");
	}

	private static long createWindow() {
		/*
		 * In order to see anything, we create a new window using GLFW's glfwCreateWindow().
		 */
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		long window = glfwCreateWindow(800, 600, "Intro2", NULL, NULL);

		/*
		 * Tell GLFW to make the OpenGL context current so that we can make OpenGL calls.
		 */
		glfwMakeContextCurrent(window);

		/*
		 * Tell LWJGL 3 that an OpenGL context is current in this thread. This will result in LWJGL 3 querying function
		 * pointers for various OpenGL functions.
		 */
		createCapabilities();

		/*
		 * Return the handle to the created window.
		 */
		return window;
	}

}