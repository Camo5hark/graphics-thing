package project1.client.texture;

import project1.client.Client;
import project1.client.GLBufferHelper;
import project1.client.resource.ResourceLoader;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL46.*;

public class MapTexture extends Texture {
    private static final ResourceLoader RESOURCE_LOADER = Client.CLIENT.getResourceLoader();

    public MapTexture(String resPath) {
        super();

        ByteBuffer resource = null;
        IntBuffer width = null;
        IntBuffer height = null;
        IntBuffer channels = null;
        ByteBuffer pixels = null;

        try {
            resource = RESOURCE_LOADER.loadGLByteBuffer("textures/" + resPath);
            width = GLBufferHelper.allocInt(1);
            height = GLBufferHelper.allocInt(1);
            channels = GLBufferHelper.allocInt(1);
            pixels = stbi_load_from_memory(resource, width, height, channels, STBI_rgb_alpha);

            if (pixels == null) {
                throw new IllegalStateException(resPath + ": Texture loading error: " + stbi_failure_reason());
            }

            setTextureData(GL_RGBA, width.get(), height.get(), GL_RGBA, GL_UNSIGNED_BYTE, pixels.flip());
        } finally {
            GLBufferHelper.free(pixels);
            GLBufferHelper.free(channels);
            GLBufferHelper.free(height);
            GLBufferHelper.free(width);
            GLBufferHelper.free(resource);
        }
    }
}
