package project1.client.shader;

import project1.client.Client;
import project1.client.resource.Resource;
import project1.client.resource.ResourceLoader;

import static org.lwjgl.opengl.GL46.*;

public class Shader extends Resource {
    public static final ResourceLoader RESOURCE_LOADER = Client.CLIENT.getResourceLoader();

    public Shader(int type, String resPath) {
        super(glCreateShader(type));

        glShaderSource(glID, RESOURCE_LOADER.loadString("shaders/" + resPath));
        glCompileShader(glID);

        if (glGetShaderi(glID, GL_COMPILE_STATUS) == 0) {
            throw new IllegalStateException(resPath + ": Shader compilation error" + glGetShaderInfoLog(glID));
        }
    }

    @Override
    public void bind() {}

    @Override
    public void delete() {
        if (isGLIDValid()) {
            glDeleteShader(glID);
        }

        invalidateGLID();
    }
}
