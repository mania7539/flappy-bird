package tw.ray.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import tw.ray.util.BufferUtils;

public class VertexArray {
    /**
     * the amount of vertices we ACTUALLY render.
     * however, we use IndexBuffer to draw, so it should be the number of indices
     * 
     * vao : Vertex Array Object
     * vbo : Vertex Buffer Object
     * ibo : Index Buffer Object
     * tbo : Texture Coordinates Buffer Object
     */
    private int count;  
    private int vao, vbo, ibo, tbo;
    
    /**
     *  What is the concept?    vao is like a group of buffers, we want all buffers to be connected, so we put all buffers into a vao
     *  How it works?           vao starts > vbo, tbo, ibo > vao ends
     * @param vertices
     * @param indices
     * @param textureCoordinates
     */
    public VertexArray(float[] vertices, byte[] indices, float[] textureCoordinates) {
        count = indices.length;
        
        // Vertex Array is like a group of buffers
        //      we want all these buffers to be connected in a way.
        //      to do that, we put all buffers into Vertex Array.
        //      but really, these data are stored in buffers.
        vao = glGenVertexArrays();  
        glBindVertexArray(vao);
        
        
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices), GL_STATIC_DRAW);
        
        // Attribute is like uniform, it saves every single vertex essentially.
        // Size gives 3 is for x, y, z each time it reads, 
        //      and the 3 will be taken into Vertex Shader (vec4 in shader will accept this) 
        glVertexAttribPointer(Shader.VERTEX_ATTRIB, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(Shader.VERTEX_ATTRIB);
        
        
        tbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(textureCoordinates), GL_STATIC_DRAW);
        
        // It's like uniform, it saves every single vertex essentially.
        // Size gives 2 is for texture x, y each time it reads
        glVertexAttribPointer(Shader.TCOORD_ATTRIB, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(Shader.TCOORD_ATTRIB);
        
        
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createByteBuffer(indices), GL_STATIC_DRAW);

        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
    
    public void bind() {
        glBindVertexArray(vao);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
    }
    
    public void unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
    
    public void draw() {
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0);
    }
    
    public void render() {
        bind();
        draw();
    }
}
