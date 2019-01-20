import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import input.Input;

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
		
		glfwSetKeyCallback(window, new Input());
		
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GL.createCapabilities();
		
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		System.out.println("Version OpenGL: " + glGetString(GL_VERSION));		
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
		
		if (Input.keys[GLFW_KEY_SPACE])
			System.out.println("FLAP!");
	}
	
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glfwSwapBuffers(window);
	}
	
	public static void main(String[] args) {		
		new Main().start();
	}
	
}