package tw.ray.level;

import tw.ray.Main;
import tw.ray.graphics.Shader;
import tw.ray.graphics.Texture;
import tw.ray.graphics.VertexArray;
import tw.ray.math.Matrix4f;
import tw.ray.math.Vector3f;

public class Level {
    private final static float ratio = Main.ratio;
    private Bird bird;
    /**
     * Pipes: 5 on top, and 5 on bottom
     */
    private Pipe[] pipes = new Pipe[5 * 2];
    
    private VertexArray background;
    private Texture bgTexture;
    
    private int index = 0;
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
        bird = new Bird();
        createPipes();
    }
    
    private void createPipes() {
        Pipe.create();
        for (int i=0; i<pipes.length; i+=2) {
            // top pipe & bottom pipe
            pipes[i] = new Pipe(index * 3.0f, 4.0f);    
            pipes[i+1] = new Pipe(pipes[i].getX(), pipes[i].getY() - 11.0f); 
            index += 2;
        }
    }
    
    private void updatePipes() {
//        pipes[]
    }
    
    public void update() {
        xScroll--;
        
        if (-xScroll % 300 == 0) map++;
        
        bird.update();
    }
    
    private void renderPipes() {
        Shader.PIPE.enable();
        Shader.PIPE.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(xScroll * 0.03f, 0.0f, 0.0f)));
        
        Pipe.getTexture().bind();
        Pipe.getMesh().bind();
        
        for (int i=0; i<5*2; i++) {
            Shader.PIPE.setUniformMat4f("ml_matrix", pipes[i].getModelMatrix());
            Pipe.getMesh().draw();
        }
        
        Pipe.getMesh().unbind();
        Pipe.getTexture().unbind();
    }
    
    public void render() {
        bgTexture.bind();
        Shader.BG.enable();
        background.bind();
        
        for (int i=map; i<map + 4; i++) {
            Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));            
            background.draw();
        }
        
        Shader.BG.disable();
        bgTexture.unbind();
        
        renderPipes();
        bird.render();
        
    }
}
