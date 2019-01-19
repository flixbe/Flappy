import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWVidMode;

public class Main implements Runnable {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final String TITLE = "Flappy Bird";
	
	private Thread thread;
	private boolean running = false;
	
	private long window;
	
	public void start() {
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
	}
	
	private void init() {
		if (!glfwInit())
			System.err.println("GLFW not init!");
		else
			System.out.println("GLFW OK");
		
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);
		
		if (window == NULL)
			return;
		
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, GLFWVidMode.WIDTH, GLFWVidMode.HEIGHT);
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
	}
	
	public void run() {
		init();
		while (running) {
			update();
			render();
			
			if (glfwWindowShouldClose(window))
				running = false;
		}
	}
	
	private void update() {
		glfwPollEvents();
	}
	
	private void render() {
		glfwSwapBuffers(window);
	}
	
	public static void main(String[] args) {		
		new Main().start();
	}
	
}