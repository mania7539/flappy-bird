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
    
    private void updatePipes() {
//        pipes[]
    }
    
    public void update(int xScroll) {
        if (-xScroll % 300 == 0) map++;
    }
    
    public void render(int xScroll) {
        background.bind();
        bgTexture.bind();
        Shader.BG.enable();
        
        for (int i=map; i<map + 4; i++) {
            Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));            
            background.draw();
        }
        
        Shader.BG.disable();
        bgTexture.unbind();
    }
}
