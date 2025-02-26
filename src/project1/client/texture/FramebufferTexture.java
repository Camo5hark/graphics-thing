package project1.client.texture;

public class FramebufferTexture extends Texture {
    public final int attachment;
    public final boolean draw;

    public FramebufferTexture(int internalFormat, int width, int height, int format, int type, int attachment, boolean draw) {
        super();

        this.attachment = attachment;
        this.draw = draw;

        setTextureData(internalFormat, width, height, format, type, null);
    }
}
