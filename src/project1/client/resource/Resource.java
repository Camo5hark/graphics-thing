package project1.client.resource;

public abstract class Resource {
    protected int glID;

    protected Resource(int glID) {
        this.glID = glID;

        ResourceManager.add(this);
    }

    public abstract void bind();
    public abstract void delete();

    protected boolean isGLIDValid() {
        return glID != 0;
    }

    protected void invalidateGLID() {
        glID = 0;
    }

    public int getGLID() {
        return glID;
    }
}
