package project1.client.mesh.buffer;

import project1.client.GLBufferHelper;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL46.*;

public class IndexBufferObject extends BufferObject {
    public IndexBufferObject(int[] data) {
        super(GL_ELEMENT_ARRAY_BUFFER);

        IntBuffer dataBuffer = null;

        try {
            dataBuffer = GLBufferHelper.wrap(data);

            glBufferData(type, dataBuffer, GL_STATIC_DRAW);
        } finally {
            GLBufferHelper.free(dataBuffer);
        }
    }
}
