package tw.ray.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import tw.ray.math.Matrix4f;
import tw.ray.math.Vector3f;
import tw.ray.util.ShaderUtils;

public class Shader {
    public final static Shader BASIC = new Shader("shaders/shader.vert", "shaders/shader.frag");
    public final static Shader BG = new Shader("shaders/bg.vert", "shaders/bg.frag");
    
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
    private Map<String, Integer> locationCacheMap = new HashMap<String, Integer>();

    private Shader(String vertex, String fragment) {
        ID = ShaderUtils.load(vertex, fragment);

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
        glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
    }

    public void setUniform2f(String name, float f1, float f2) {
        glUniform2f(getUniform(name), f1, f2);
    }

    public void setUniform1f(String name, float f1) {
        glUniform1f(getUniform(name), f1);
    }
    
    public void setUniform1i(String name, int i1) {
        glUniform1i(getUniform(name), i1);
    }
    
    public void setUniformMat4f(String name, Matrix4f matrix) {
        glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
    }

    public void enable() {
        glUseProgram(ID);
    }

    public void disable() {
        glUseProgram(0);
    }
    
    public int getProgramID() {
        return ID;
    }
}
