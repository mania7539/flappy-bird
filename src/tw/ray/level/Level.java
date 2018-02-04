package tw.ray.level;

import tw.ray.Main;
import tw.ray.graphics.Shader;
import tw.ray.graphics.Texture;
import tw.ray.graphics.VertexArray;

public class Level {
    private final static float ratio = Main.ratio;

    private VertexArray background;
    private Texture bgTexture;
    
    public Level() {
        // for x, y, z each time it reads
        float[] vertices = new float[] {
                -10.0f, -10.0f*ratio, 0.0f,
                -10.0f,  10.0f*ratio, 0.0f,
                  0.0f,  10.0f*ratio, 0.0f,
                  0.0f, -10.0f*ratio, 0.0f
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
        
        background = new VertexArray(vertices, indices, tcs);
        bgTexture = new Texture("res/bg.jpeg");
        
    }
    
    public void render(Shader shader) {
        bgTexture.bind();
        shader.enable();
        background.render();
        shader.disable();
        bgTexture.unbind();
    }
}
