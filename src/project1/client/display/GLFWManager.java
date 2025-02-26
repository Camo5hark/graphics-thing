package project1.client.display;

import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

public class GLFWManager {
    public static void init() {
        glfwSetErrorCallback(GLFWErrorCallback.createThrow());

        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }
    }

    public static void term() {
        glfwTerminate();

        GLFWErrorCallback errorCallback = glfwSetErrorCallback(null);

        if (errorCallback != null) {
            errorCallback.free();
        }
    }

    public static void pollEvents() {
        glfwPollEvents();
    }
}
