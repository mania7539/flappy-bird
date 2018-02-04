package tw.ray.level;

import static org.lwjgl.glfw.GLFW.*;

import tw.ray.graphics.Shader;
import tw.ray.graphics.Texture;
import tw.ray.graphics.VertexArray;
import tw.ray.input.Input;
import tw.ray.math.Matrix4f;
import tw.ray.math.Vector3f;

public class Bird {
    private final static float ratio = 2.0f;
    
    private float SIZE = 1.0f;
    private VertexArray mesh;
    private Texture texture;
    
    private Vector3f position = new Vector3f();
    private float rotation;
    
    /**
     * It's the value of Velocity
     */
    private float delta = 0.0f;
    
    public Bird() {
     // for x, y, z each time it reads
        float[] vertices = new float[] {
                -SIZE / ratio, -SIZE / ratio,  0.1f,
                -SIZE / ratio,  SIZE / ratio,  0.1f,
                 SIZE / ratio,  SIZE / ratio,  0.1f,
                 SIZE / ratio, -SIZE / ratio,  0.1f
        };
        
        byte[] indices = new byte[] {
                0, 1, 2,
                2, 3, 0
        };
        
        // for Texture Coordinates
        float[] tcs = new float[] {
                0, 1, 
                0, 0,
                1, 0,
                1, 1
        };
        
        mesh = new VertexArray(vertices, indices, tcs);
        texture = new Texture("res/bird.png");
    }
    
    public void update() {
        position.y -= delta;
        if (Input.isKeyDown(GLFW_KEY_SPACE)) {
            delta = -0.15f;
        } else {
            // How we manage velocity here is adding acceleration
            delta += 0.01f;
        }
        
        rotation = -delta * 90.0f;
    }
    
    public void fall() {
        
    }
    
    public void render() {
        Shader.BIRD.enable();
        Shader.BIRD.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rotation)));
        texture.bind();
        mesh.render();

        Shader.BIRD.disable();
        
    }
}
