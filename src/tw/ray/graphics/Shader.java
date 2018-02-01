package tw.ray.graphics;

import tw.ray.math.Vector3f;
import tw.ray.util.ShaderUtils;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int ID;
    
    public final static Shader BASIC = new Shader("shaders/shader.vert", "shaders/shader.frag");
    
    private Shader(String vertex, String fragment) {
        ID = ShaderUtils.load(vertex,  fragment);
        
    }
    
    public int getUniform(String name) {
        return glGetUniformLocation(ID, name);
    }
    
    public void setUniform3f(String name, Vector3f vector) {
        glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
    }
    
    public void enable() {
        glUseProgram(ID);
    }
    
    public void disable() {
        glUseProgram(0);
    }
}
