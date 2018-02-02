package tw.ray;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import tw.ray.graphics.Shader;
import tw.ray.math.Vector3f;

public class Main implements Runnable {

    private int width = 1280;
    private int height = 720;
    private String title = "Flappy Bird";
    private boolean running = false;
    private Thread thread;
    private long window;

    public void start() {
        running = true;
        thread = new Thread(this, "Display");
        thread.start();
    }

    private void init() {
        String version = glGetString(GL_VERSION);
        System.out.println(String.format("OpenGL %s", version));

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

    }

    private void update() {
        glfwPollEvents();   // deal with key events
    }

    private void render() {
        glfwSwapBuffers(window);
        glClear(GL_COLOR_BUFFER_BIT);
        glDrawArrays(GL_TRIANGLES, 0, 3);
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
        GL.createCapabilities();
        
        init();

        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        Shader shader = Shader.BASIC;
        shader.enable();
        shader.setUniform3f("col", new Vector3f(0.8f, 0.2f, 0.3f));

        while (running) {
            update();
            render();
            
            if (glfwWindowShouldClose(window) == GL_TRUE) {
                running = false;
            }
        }
    }

    public static void main(String[] args) {
        new Main().start();
    }

}
