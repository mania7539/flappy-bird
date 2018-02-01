package tw.ray.math;

import static java.lang.Math.*;

public class Matrix4f {
    public float[] matrix = new float[4 * 4];
    
    /**
     * Create an identity matrix, in which all elements in the main diagonal
     *  are equal to 1, and everything else is 0. Everything * Identity will still be itself. 
     * @return
     */
    public static Matrix4f identify() {
        Matrix4f result = new Matrix4f();
        
        for (int i=0; i< 4 * 4; i++) {
            result.matrix[i] = 0.0f;
        }
        
        result.matrix[0 * 4 + 0] = 1.0f;
        result.matrix[1 * 4 + 1] = 1.0f;
        result.matrix[2 * 4 + 2] = 1.0f;
        result.matrix[3 * 4 + 3] = 1.0f;
        
        return result;
    }
    
    public Matrix4f multiply(Matrix4f matrix) {
        Matrix4f result = new Matrix4f();
        
        for (int x=0; x<4; x++) {
            for (int y=0; y<4; y++) {
                float sum = 0;
                for (int e=0; e<4; e++) {
                    sum += this.matrix[x*4 + e] * matrix.matrix[e*4 + y];
                }
                result.matrix[x*4 + y] = sum;
            }
        }
        
        return result;
    }
    
    /** 
     * Move an object around 2D or 3D space,
     *  OpenGL takes column major ordering for matrices
     * @param vector
     * @return
     */
    public static Matrix4f translate(Vector3f vector) {
        Matrix4f result = identify();
        
        result.matrix[3 * 4 + 0] = vector.x;
        result.matrix[3 * 4 + 1] = vector.x;
        result.matrix[3 * 4 + 2] = vector.x;
        
        return result;
    }
    
    /**
     * Rotate along the z-axis
     * @param angle
     * @return
     */
    public static Matrix4f rotate(float angle) {
        Matrix4f result = identify();
        float r = (float) toRadians(angle);
        float cos = (float) cos(r);
        float sin = (float) sin(r);
        
        result.matrix[0 * 4 + 0] = cos;
        result.matrix[0 * 4 + 1] = sin;
        
        result.matrix[0 * 4 + 2] = -sin;
        result.matrix[0 * 4 + 3] = cos;
        
        return result;
    }
    
    /**
     * Rotate around all axis
     * @param angle
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static Matrix4f rotate(float angle, float x, float y, float z) {
        Matrix4f result = identify();
        float r = (float) toRadians(angle);
        float cos = (float) cos(r);
        float sin = (float) sin(r);
        float omc = 1.0f - cos;
        
        result.matrix[0 * 4 + 0] = x * omc + cos;
        result.matrix[0 * 4 + 1] = x * y * omc + z * sin;
        result.matrix[0 * 4 + 2] = x * z * omc - y * sin;
        
        result.matrix[1 * 4 + 0] = x * y * omc - z * sin;
        result.matrix[1 * 4 + 1] = y * omc + cos;
        result.matrix[1 * 4 + 2] = y * z * omc + x * sin;
        
        result.matrix[2 * 4 + 0] = x * z * omc + y * sin;
        result.matrix[2 * 4 + 1] = y * z * omc - x * sin;
        result.matrix[2 * 4 + 2] = z * omc + cos;
        
        return result;
    }
    
    /**
     * Projection
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     * @return
     */
    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f result = identify();
        
        result.matrix[0 * 4 + 0] = 2.0f / (right - left);
        result.matrix[1 * 4 + 1] = 2.0f / (top - bottom);
        result.matrix[2 * 4 + 2] = 2.0f / (near - far);
        
        result.matrix[3 * 4 + 0] = (left - right) / (left - right);
        result.matrix[3 * 4 + 1] = (bottom + top) / (bottom - top);
        result.matrix[3 * 4 + 2] = (near + far) / (far - near);
        
        return result;
    }
}
