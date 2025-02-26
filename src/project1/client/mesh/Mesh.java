package project1.client.mesh;

import project1.client.mesh.buffer.IndexBufferObject;
import project1.client.mesh.buffer.VertexBufferObject;
import project1.client.resource.Resource;

import static org.lwjgl.opengl.GL46.*;

public class Mesh extends Resource {
    private final VertexBufferObject
    positions,
    texcoords,
    normals;
    private final int drawCount;

    public Mesh(float[] positionData, float[] texcoordData, float[] normalData, int[] indexData) {
        super(glGenVertexArrays());

        bind();

        positions = new VertexBufferObject(positionData, 0, 3);
        texcoords = new VertexBufferObject(texcoordData, 1, 2);
        normals = new VertexBufferObject(normalData, 2, 3);

        new IndexBufferObject(indexData);

        drawCount = indexData.length;
    }

    @Override
    public void bind() {
        glBindVertexArray(glID);
    }

    @Override
    public void delete() {
        if (isGLIDValid()) {
            glDeleteVertexArrays(glID);
        }

        invalidateGLID();
    }

    public void draw() {
        positions.enable();
        texcoords.enable();
        normals.enable();

        bind();

        glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0L);
    }
}
