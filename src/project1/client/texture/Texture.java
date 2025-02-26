package project1.client.texture;

import project1.client.GLBufferHelper;
import project1.client.resource.Resource;

import java.nio.Buffer;

import static org.lwjgl.opengl.GL46.*;

public class Texture extends Resource {
    public Texture() {
        super(glGenTextures());

        bind();
    }

    @Override
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, glID);
    }

    @Override
    public void delete() {
        if (isGLIDValid()) {
            glDeleteTextures(glID);
        }

        invalidateGLID();
    }

    public void bind(int sampler) {
        glActiveTexture(GL_TEXTURE0 + sampler);

        bind();
    }

    protected void setTextureData(int internalFormat, int width, int height, int format, int type, Buffer pixels) {
        glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, type, GLBufferHelper.getAddress(pixels));
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    }
}
