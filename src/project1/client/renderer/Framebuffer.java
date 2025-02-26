package project1.client.renderer;

import project1.client.Client;
import project1.client.GLBufferHelper;
import project1.client.display.Window;
import project1.client.resource.Resource;
import project1.client.texture.FramebufferTexture;
import project1.helper.ListHelper;

import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL46.*;

public class Framebuffer extends Resource {
    private static final Window WINDOW = Client.CLIENT.getWindow();
    public static final int CLEAR_MASK = GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT;

    private final int width;
    private final int height;
    private final int clearMask;
    public final FramebufferTexture[] textures;

    public Framebuffer(int width, int height, int clearMask, FramebufferTexture... textures) {
        super(glGenFramebuffers());

        this.width = width;
        this.height = height;
        this.clearMask = clearMask;
        this.textures = textures;

        bind();

        ArrayList<Integer> drawData = new ArrayList<>();

        for (FramebufferTexture texture : textures) {
            texture.bind();

            glFramebufferTexture2D(GL_FRAMEBUFFER, texture.attachment, GL_TEXTURE_2D, texture.getGLID(), 0);

            if (texture.draw) {
                drawData.add(texture.attachment);
            }
        }

        IntBuffer drawBuffer = null;

        try {
            drawBuffer = GLBufferHelper.wrap(ListHelper.intListToArray(drawData));

            glDrawBuffers(drawBuffer);
        } finally {
            GLBufferHelper.free(drawBuffer);
        }

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new IllegalStateException("Framebuffer not complete");
        }

        bindDefault();
    }

    @Override
    public void bind() {
        bind(width, height, glID, clearMask);
    }

    @Override
    public void delete() {
        if (isGLIDValid()) {
            glDeleteFramebuffers(glID);
        }

        invalidateGLID();
    }

    private static void bind(int width, int height, int glID, int clearMask) {
        glViewport(0, 0, width, height);
        glBindFramebuffer(GL_FRAMEBUFFER, glID);
        glClear(clearMask);
    }

    public static void bindDefault() {
        bind(WINDOW.width, WINDOW.height, 0, CLEAR_MASK);
    }
}
