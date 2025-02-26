package project1.client.shader;

import org.joml.Matrix4d;
import org.joml.Vector3d;
import org.joml.Vector4d;
import project1.client.GLBufferHelper;
import project1.client.light.*;
import project1.client.mesh.Material;
import project1.client.resource.Resource;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL46.*;

public class ShaderProgram extends Resource {
    public ShaderProgram(Shader... shaders) {
        super(glCreateProgram());

        for (Shader shader : shaders) {
            glAttachShader(glID, shader.getGLID());
        }

        glLinkProgram(glID);

        confirmStatus(GL_LINK_STATUS, "LINK");

        glValidateProgram(glID);

        confirmStatus(GL_VALIDATE_STATUS, "VALIDATE");
    }

    @Override
    public void bind() {
        glUseProgram(glID);
    }

    @Override
    public void delete() {
        if (isGLIDValid()) {
            glDeleteProgram(glID);
        }

        invalidateGLID();
    }

    private void confirmStatus(int status, String name) {
        if (glGetProgrami(glID, status) == 0) {
            throw new IllegalStateException("Shader program " + name + " error" + glGetProgramInfoLog(glID));
        }
    }

    private int getUniformLocation(String name) {
        int location = glGetUniformLocation(glID, name);

        if (location == -1) {
            throw new IllegalStateException("Could not find uniform: " + name);
        }

        return location;
    }

    public void setUniformInt(String name, int value) {
        glUniform1i(getUniformLocation(name), value);
    }

    public void setUniformFloat(String name, double value) {
        glUniform1f(getUniformLocation(name), (float) value);
    }

    public void setUniformVec3(String name, Vector3d value) {
        FloatBuffer valueBuffer = null;

        try {
            valueBuffer = GLBufferHelper.allocFloat(3);

            glUniform3fv(getUniformLocation(name), value.get(valueBuffer));
        } finally {
            GLBufferHelper.free(valueBuffer);
        }
    }

    public void setUniformVec4(String name, Vector4d value) {
        FloatBuffer valueBuffer = null;

        try {
            valueBuffer = GLBufferHelper.allocFloat(4);

            glUniform4fv(getUniformLocation(name), value.get(valueBuffer));
        } finally {
            GLBufferHelper.free(valueBuffer);
        }
    }

    public void setUniformMat4(String name, Matrix4d value) {
        FloatBuffer valueBuffer = null;

        try {
            valueBuffer = GLBufferHelper.allocFloat(16);

            glUniformMatrix4fv(getUniformLocation(name), false, value.get(valueBuffer));
        } finally {
            GLBufferHelper.free(valueBuffer);
        }
    }

    public void setUniformMaterial(String name, Material material) {
        setUniformVec4(name + ".albedo", material.albedo);
        setUniformFloat(name + ".shininess", material.shininess);
        setUniformInt(name + ".hasTexture", material.hasTexture() ? 1 : 0);
    }

    public void setUniformAmbientLight(String name, AmbientLight value) {
        setUniformVec3(name + ".color", value.color);
        setUniformFloat(name + ".intensity", value.intensity);
    }

    public void setUniformDirectionalLight(String name, DirectionalLight value) {
        setUniformVec3(name + ".color", value.color);
        setUniformFloat(name + ".intensity", value.intensity);
        setUniformVec3(name + ".direction", value.direction);
    }

    public void setUniformPointLight(String name, PointLight value) {
        setUniformVec3(name + ".color", value.color);
        setUniformFloat(name + ".intensity", value.intensity);
        setUniformVec3(name + ".position", value.position);
    }

    public void setUniformSpotLight(String name, SpotLight value) {
        setUniformVec3(name + ".color", value.color);
        setUniformFloat(name + ".intensity", value.intensity);
        setUniformVec3(name + ".position", value.position);
        setUniformVec3(name + ".direction", value.direction);
        setUniformFloat(name + ".cutOff", value.cutOff);
    }
}
