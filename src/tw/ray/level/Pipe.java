package tw.ray.level;

import tw.ray.graphics.Texture;
import tw.ray.graphics.VertexArray;
import tw.ray.math.Matrix4f;
import tw.ray.math.Vector3f;

public class Pipe {
    private static float width = 1.5f;
    private static float height = 8.0f;
    private static Texture texture;
    private static VertexArray mesh;
    
    private Vector3f position = new Vector3f();
    private Matrix4f ml_matrix;
    
    public static void create() {
        // for x, y, z each time it reads
        float[] vertices = new float[] {
                 0.0f,   0.0f, 0.1f,
                 0.0f, height, 0.1f,
                width, height, 0.1f,
                width,   0.0f, 0.1f
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
        texture = new Texture("res/pipe.png");
    }
    
    public Pipe(float x, float y) {
        position.x = x;
        position.y = y;
        ml_matrix = Matrix4f.translate(position);
    }
    
    public float getX() {
        return position.x;
    }
    
    public float getY() {
        return position.y;
    }
    
    public Matrix4f getModelMatrix() {
        return ml_matrix;
    }
    
    public static VertexArray getMesh() {
        return mesh;
    }
    
    public static Texture getTexture() {
        return texture;
    }
    
    public static float getWidth() {
        return width;
    }
    
    public static float getHeight() {
        return height;
    }
}
