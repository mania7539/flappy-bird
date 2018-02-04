package tw.ray.math;

import static java.lang.Math.*;

import java.nio.FloatBuffer;

import tw.ray.util.BufferUtils;

public class Matrix4f {
    public final static int SIZE = 4 * 4;
    public float[] elements = new float[SIZE];

    public FloatBuffer toFloatBuffer() {
        return BufferUtils.createFloatBuffer(elements);
    }
    
    /**
     * Create an identity matrix, in which all elements in the main diagonal are
     * equal to 1, and everything else is 0. Everything * Identity will still be
     * itself.
     * 
     * @return
     */
    public static Matrix4f identify() {
        Matrix4f result = new Matrix4f();

        for (int i = 0; i < SIZE; i++) {
            result.elements[i] = 0.0f;
        }
        
        // Take notes:  row , col: since OpenGL is a column major ordering system
        result.elements[0 + 0 * 4] = 1.0f;
        result.elements[1 + 1 * 4] = 1.0f;
        result.elements[2 + 2 * 4] = 1.0f;
        result.elements[3 + 3 * 4] = 1.0f;

        return result;
    }

    public Matrix4f multiply(Matrix4f matrix) {
        Matrix4f result = new Matrix4f();

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                float sum = 0;
                for (int e = 0; e < 4; e++) {
                    sum += this.elements[x * 4 + e] * matrix.elements[e * 4 + y];
                }
                result.elements[x * 4 + y] = sum;
            }
        }

        return result;
    }

    /**
     * Move an object around 2D or 3D space, OpenGL takes column major ordering
     * for matrices
     * 
     * @param vector
     * @return
     */
    public static Matrix4f translate(Vector3f vector) {
        Matrix4f result = identify();
        
        // Take notes:  row , col: since OpenGL is a column major ordering system
        result.elements[0 + 3 * 4] = vector.x;
        result.elements[1 + 3 * 4] = vector.y;
        result.elements[2 + 3 * 4] = vector.z;

        return result;
    }

    /**
     * Rotate along the z-axis
     * 
     * @param angle
     * @return
     */
    public static Matrix4f rotate(float angle) {
        Matrix4f result = identify();
        float r = (float) toRadians(angle);
        float cos = (float) cos(r);
        float sin = (float) sin(r);
        
        // Take notes:  row , col: since OpenGL is a column major ordering system
        result.elements[0 + 0 * 4] = cos;
        result.elements[1 + 0 * 4] = sin;

        result.elements[2 + 0 * 4] = -sin;
        result.elements[3 + 0 * 4] = cos;

        return result;
    }

    /**
     * Rotate around all axis
     * 
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
        
        // Take notes:  row , col: since OpenGL is a column major ordering system
        result.elements[0 + 0 * 4] = x * omc + cos;
        result.elements[1 + 0 * 4] = x * y * omc + z * sin;
        result.elements[2 + 0 * 4] = x * z * omc - y * sin;

        result.elements[0 + 1 * 4] = x * y * omc - z * sin;
        result.elements[1 + 1 * 4] = y * omc + cos;
        result.elements[2 + 1 * 4] = y * z * omc + x * sin;

        result.elements[0 + 2 * 4] = x * z * omc + y * sin;
        result.elements[1 + 2 * 4] = y * z * omc - x * sin;
        result.elements[2 + 2 * 4] = z * omc + cos;

        return result;
    }

    /**
     * Projection
     * 
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
        
        // Take notes:  row , col: since OpenGL is a column major ordering system
        result.elements[0 + 0 * 4] = 2.0f / (right - left);
        result.elements[1 + 1 * 4] = 2.0f / (top - bottom);
        result.elements[2 + 2 * 4] = 2.0f / (near - far);

        result.elements[0 + 3 * 4] = (left + right) / (left - right);
        result.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
        result.elements[2 + 3 * 4] = (near + far) / (far - near);

        return result;
    }
}
