package tw.ray.level;

import tw.ray.Main;
import tw.ray.graphics.Shader;
import tw.ray.graphics.Texture;
import tw.ray.graphics.VertexArray;
import tw.ray.math.Matrix4f;
import tw.ray.math.Vector3f;

public class Level {
    private final static float ratio = Main.ratio;

    private VertexArray background;
    private Texture bgTexture;
    
    private int xScroll = 0;
    private int map = 0;
    
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
    
    public void update() {
        xScroll--;
        
        if (-xScroll % 300 == 0) map++;
    }
    
    public void render(Shader shader) {
        bgTexture.bind();
        shader.enable();
        background.bind();
        
        for (int i=map; i<map + 4; i++) {
            shader.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));            
            background.draw();
        }
        
        shader.disable();
        bgTexture.unbind();
    }
}
