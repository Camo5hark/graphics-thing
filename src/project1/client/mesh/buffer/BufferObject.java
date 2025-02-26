package project1.client.mesh.buffer;

import project1.client.resource.Resource;

import static org.lwjgl.opengl.GL46.*;

public class BufferObject extends Resource {
    protected final int type;

    public BufferObject(int type) {
        super(glGenBuffers());

        this.type = type;

        bind();
    }

    @Override
    public void bind() {
        glBindBuffer(type, glID);
    }

    @Override
    public void delete() {
        if (isGLIDValid()) {
            glDeleteBuffers(glID);
        }

        invalidateGLID();
    }
}
