package tw.ray;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import tw.ray.graphics.Shader;
import tw.ray.graphics.Texture;
import tw.ray.input.Input;
import tw.ray.level.Level;
import tw.ray.math.Matrix4f;
import tw.ray.math.Vector3f;

public class Main implements Runnable {
    public final static float ratio = 9.0f / 16.0f;
    private int width = 1280;
    private int height = 720;
    private String title = "Flappy Bird";
    private boolean running = false;
    private Thread thread;
    private long window;
    private Level level;
    private Shader shader, bg;
    
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

        // This program will be a 16*9 window
        bg = Shader.BG;
        
    }

    private void update() {
        glfwPollEvents();   // deal with key events
        level.update();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        level.render();
        checkGLError();
        glfwSwapBuffers(window);
    }

    public void run() {
        if (glfwInit() != GL_TRUE) {
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
            
            if (glfwWindowShouldClose(window) == GL_TRUE) {
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
