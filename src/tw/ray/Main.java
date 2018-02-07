package tw.ray;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.Random;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import tw.ray.graphics.Shader;
import tw.ray.graphics.Texture;
import tw.ray.input.Input;
import tw.ray.level.Bird;
import tw.ray.level.Level;
import tw.ray.level.Pipe;
import tw.ray.math.Matrix4f;
import tw.ray.math.Vector3f;

public class Main implements Runnable {

    // This program will be a 16*9 window
    public final static float ratio = 9.0f / 16.0f;
    private int width = 1280;
    private int height = 720;
    private String title = "Flappy Bird";
    private boolean running = false;
    private Thread thread;
    private long window;
    
    private int xScroll = 0;
    private Level level;
    private Bird bird;
    /**
     * Pipes: 5 on top, and 5 on bottom
     */
    private Pipe[] pipes = new Pipe[5 * 2];
    private int index = 0;
    private Random random = new Random();
    
    private void createPipes() {
        Pipe.create();
        for (int i=0; i<pipes.length; i+=2) {
            // top pipe & bottom pipe (0,0 is at the bottom left corner in OpenGL)
            pipes[i] = new Pipe(index * 3.0f, random.nextFloat() * 4.0f);    
            pipes[i+1] = new Pipe(pipes[i].getX(), pipes[i].getY() - 11.0f); 
            index += 2;
        }
    }
    
    private void renderPipes() {
        Shader.PIPE.enable();
        Shader.PIPE.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(xScroll * 0.03f, 0.0f, 0.0f)));
        
        Pipe.getTexture().bind();
        Pipe.getMesh().bind();
        
        for (int i=0; i<pipes.length; i++) {
            Shader.PIPE.setUniformMat4f("ml_matrix", pipes[i].getModelMatrix());
            Shader.PIPE.setUniform1i("top", (i % 2 == 0) ? 1 : 0);
            Pipe.getMesh().draw();
        }
        
        Pipe.getMesh().unbind();
        Pipe.getTexture().unbind();
    }
    
    public void start() {
        running = true;
        thread = new Thread(this, "Display");
        thread.start();
    }

    private void init() {
        String version = glGetString(GL_VERSION);
        System.out.println(String.format("OpenGL %s", version));

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        
        level = new Level();
        bird = new Bird();
        createPipes();
        
    }

    private void update() {
        glfwPollEvents();   // deal with key events
        
        xScroll--;
        
        level.update(xScroll);
        bird.update();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        level.render(xScroll);

        renderPipes();
        bird.render();
        
        checkGLError();
        glfwSwapBuffers(window);
    }

    public void run() {
        if (!glfwInit()) {
            return;
        }

        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            return;
        }

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
        glfwSetKeyCallback(window, new Input());
        GL.createCapabilities();
        
        init();

        long lastTime = System.nanoTime();
        double delta = 0.0;
        double ns = 1000000000.0 / 60.0;
        long timer = System.currentTimeMillis();
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
                System.out.println(String.format("%d ups, %d fps", updates, frames));
                updates = 0;
                frames = 0;
            }
            
            if (glfwWindowShouldClose(window)) {
                running = false;
            }
        }
    }

    public void checkGLError() {
        int i = glGetError();
        if (i != GL_NO_ERROR) {
            System.out.println(String.format("GL Error Code: %d", i));
        }
    }
    
    public static void main(String[] args) {
        new Main().start();
    }

}
