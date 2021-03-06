package tw.ray.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import tw.ray.util.BufferUtils;

public class Texture {
    private int width, height;
    private int texture;

    public Texture(String path) {
        texture = load(path);
    }

    private int load(String path) {
        int[] pixels = null;
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(path));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // map RGBA format to ABGR format (right format for OpenGL)
        int[] data = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff00) >> 8;
            int b = (pixels[i] & 0xff);
            data[i] = a << 24 | b << 16 | g << 8 | r;
        }
        
        // colors are essentially unsigned byte
        int tex = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, tex);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);  // use GL_NEAREST to keep bird on nice and sharp
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);  // scale up and scale down the image
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
        glBindTexture(GL_TEXTURE_2D, 0);
        
        return tex;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texture);
    }
    
    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getID() {
        return texture;
    }
    
    public static int getTextureIndex(int GL_TEXTURE) {
        switch (GL_TEXTURE) {
            case GL_TEXTURE0: return 0;
            case GL_TEXTURE1: return 1;
        }
        
        return -1;
    }
}
