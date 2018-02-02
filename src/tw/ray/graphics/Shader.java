package tw.ray.graphics;

import static org.lwjgl.opengl.GL20.*;

import tw.ray.math.Vector3f;
import tw.ray.util.ShaderUtils;

public class Shader {

    private final int ID;

    public final static Shader BASIC = new Shader("shaders/shader.vert", "shaders/shader.frag");

    private Shader(String vertex, String fragment) {
        ID = ShaderUtils.load(vertex, fragment);

    }

    public int getUniform(String name) {
        int result = glGetUniformLocation(ID, name);    // this code transfers data from GPU to CPU
        if (result == -1) {
            System.err.println(String.format("Could not find uniform variable %s!", name));
        }
        return result;
    }

    public void setUniform3f(String name, Vector3f vector) {
        glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
    }

    public void setUniform1i(String name, int i1) {
        glUniform1i(getUniform(name), i1);
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
