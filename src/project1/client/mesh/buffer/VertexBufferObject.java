package project1.client.mesh.buffer;

import project1.client.GLBufferHelper;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL46.*;

public class VertexBufferObject extends BufferObject {
    private final int index;

    public VertexBufferObject(float[] data, int index, int size) {
        super(GL_ARRAY_BUFFER);

        this.index = index;

        FloatBuffer dataBuffer = null;

        try {
            dataBuffer = GLBufferHelper.wrap(data);

            glBufferData(type, dataBuffer, GL_STATIC_DRAW);
        } finally {
            GLBufferHelper.free(dataBuffer);
        }

        glVertexAttribPointer(index, size, GL_FLOAT, false, size * 4, 0L);
    }

    public void enable() {
        glEnableVertexAttribArray(index);
    }
}
