package tw.ray.graphics;

import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import tw.ray.Main;
import tw.ray.math.Matrix4f;
import tw.ray.math.Vector3f;
import tw.ray.util.ShaderUtils;

public class Shader {
    public static Shader BASIC = new Shader("shaders/shader.vert", "shaders/shader.frag");
    public static Shader BG = new Shader("shaders/bg.vert", "shaders/bg.frag");
    public static Shader BIRD = new Shader("shaders/bird.vert", "shaders/bird.frag");
    public static Shader PIPE = new Shader("shaders/pipe.vert", "shaders/pipe.frag");
    
    /**
     * These are Attribute Locations.
     * Attribute is like uniform, it saves every single vertex essentially
     * 
     * VERTEX_ATTRIB : Vertex Attribute
     * TCOORD_ATTRIB : Texture Coordinates Attribute    
     */
    public final static int VERTEX_ATTRIB = 0;
    public final static int TCOORD_ATTRIB = 1;
    
    private final int ID;
    private boolean enabled = false;
    private Map<String, Integer> locationCacheMap = new HashMap<String, Integer>();

    private Shader(String vertexPath, String fragmentPath) {
        ID = ShaderUtils.load(vertexPath, fragmentPath);
        float ratio = Main.ratio;
        Matrix4f projection_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f*ratio, 10.0f*ratio, -1.0f, 1.0f);
        
        String[] split;
        split = vertexPath.split("/");
        String vertexFileName = split[split.length-1];
        split = fragmentPath.split("/");
        String fragmentFileName = split[split.length-1];
        
        enable();
        
        if (vertexFileName.equals("shader.vert") && fragmentFileName.equals("shader.frag")) {
            
        } else if (vertexFileName.equals("bg.vert") && fragmentFileName.equals("bg.frag")) {
            setUniformMat4f("pr_matrix", projection_matrix);
            glActiveTexture(GL_TEXTURE1);
            setUniform1i("tex", Texture.getTextureIndex(GL_TEXTURE1));
        } else if (vertexFileName.equals("bird.vert") && fragmentFileName.equals("bird.frag")) {
            setUniformMat4f("pr_matrix", projection_matrix);
            glActiveTexture(GL_TEXTURE1);
            setUniform1i("tex", Texture.getTextureIndex(GL_TEXTURE1));
        } else if (vertexFileName.equals("pipe.vert") && fragmentFileName.equals("pipe.frag")) {
            setUniformMat4f("pr_matrix", projection_matrix);
            glActiveTexture(GL_TEXTURE1);
            setUniform1i("tex", Texture.getTextureIndex(GL_TEXTURE1));
        }
        
        disable();
    }

    public static void loadAll() {
        BASIC = new Shader("shaders/shader.vert", "shaders/shader.frag");
        BG = new Shader("shaders/bg.vert", "shaders/bg.frag");
        BIRD = new Shader("shaders/bird.vert", "shaders/bird.frag");
        PIPE = new Shader("shaders/pipe.vert", "shaders/pipe.frag");
    }
    
    public int getUniform(String name) {
        if (locationCacheMap.containsKey(name)) {
            return locationCacheMap.get(name);
        }
        
        int result = glGetUniformLocation(ID, name);    // this code transfers data from GPU to CPU
        if (result == -1) {
            System.err.println(String.format("Could not find uniform variable %s!", name));
        } else {
            locationCacheMap.put(name, result);
        }
        
        return result;
    }

    public void setUniform3f(String name, Vector3f vector) {
        if (!enabled) enable();
        glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
    }

    public void setUniform2f(String name, float f1, float f2) {
        if (!enabled) enable();
        glUniform2f(getUniform(name), f1, f2);
    }

    public void setUniform1f(String name, float f1) {
        if (!enabled) enable();
        glUniform1f(getUniform(name), f1);
    }
    
    public void setUniform1i(String name, int i1) {
        if (!enabled) enable();
        glUniform1i(getUniform(name), i1);
    }
    
    public void setUniformMat4f(String name, Matrix4f matrix) {
        if (!enabled) enable();
        glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
    }

    public void enable() {
        glUseProgram(ID);
        enabled = true;
    }

    public void disable() {
        glUseProgram(0);
        enabled = false;
    }
    
    public int getProgramID() {
        return ID;
    }
}
