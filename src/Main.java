import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import graphics.Shader;
import input.Input;
import level.Level;
import math.Matrix4f;

public class Main implements Runnable {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final String TITLE = "Flappy Bird";
	
	private Thread thread;
	private boolean running = false;
	
	private long window;
	
	private Level level;
	
	public void start() {
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
	}
	
	private void init() {
		if (!glfwInit())
			System.err.println("Could not initialize GLFW!");
		else
			System.out.println("GLFW IS OK");
		
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);
		
		if (window == NULL) {
			System.err.println("Could not create GLFW Window");
			return;
		}
		
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, GLFWVidMode.WIDTH, GLFWVidMode.HEIGHT);
		
		glfwSetKeyCallback(window, new Input());
		
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GL.createCapabilities();
		
		glEnable(GL_DEPTH_TEST);
		glActiveTexture(GL_TEXTURE1);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		System.out.println("Version OpenGL: " + glGetString(GL_VERSION));
		Shader.loadAll();
		
		Matrix4f pr_matrix = new Matrix4f().orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
		Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BG.setUniform1i("tex", 1);
		
		Shader.BIRD.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BIRD.setUniform1i("tex", 1);
		
		Shader.PIPE.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.PIPE.setUniform1i("tex", 1);
		
		level = new Level();
	}
	
	public void run() {
		init();
		
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		
		double delta = 0.0;
		double ns = 1_000_000_000.0 / 60.0;
		
		int updates = 0;
		int frames = 0;
		
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0) {
				update();
				updates++;
				delta--;
			}
			
			render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("UPS: " + updates + " | FPS: " + frames);
				updates = 0;
				frames = 0;
			}
			
			if (glfwWindowShouldClose(window))
				running = false;
		}
		
		glfwDestroyWindow(window);
		glfwTerminate();
	}
	
	private void update() {
		glfwPollEvents();
		level.update();
		if (level.isGameOver()) {
			level = new Level();
		}
	}
	
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		level.render();
		
		if (glGetError() != GL_NO_ERROR)
			System.err.println(glGetError());
		
		glfwSwapBuffers(window);
	}
	
	public static void main(String[] args) {		
		new Main().start();
	}
	
}