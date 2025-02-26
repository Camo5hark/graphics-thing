package project1.client.resource;

import project1.client.GLBufferHelper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ResourceLoader {
    public String resRoot;

    public ResourceLoader(String resRoot) {
        this.resRoot = resRoot;
    }

    private InputStream getResource(String resPath) {
        return getClass().getResourceAsStream(resRoot + resPath);
    }

    private byte[] load(String resPath) {
        InputStream resource = getResource(resPath);

        try {
            byte[] read = new byte[resource.available()];

            if (resource.read(read) != -1) {
                return read;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String loadString(String resPath) {
        byte[] loaded = load(resPath);

        if (loaded != null) {
            return new String(loaded, StandardCharsets.UTF_8);
        }

        return null;
    }

    public ByteBuffer loadGLByteBuffer(String resPath) {
        byte[] loaded = load(resPath);

        if (loaded != null) {
            return GLBufferHelper.wrap(loaded);
        }

        return null;
    }
}
