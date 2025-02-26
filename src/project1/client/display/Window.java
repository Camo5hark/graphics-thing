package project1.client.display;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class Window {
    public boolean fullscreen;
    public Monitor monitor;
    public int width;
    public int height;
    public String title;
    public boolean vSync;
    private long glfwWindowID;

    public Window(boolean fullscreen, Monitor monitor, int width, int height, String title, boolean vSync) {
        this.fullscreen = fullscreen;
        this.monitor = monitor;
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync = vSync;

        refresh();
    }

    public void refresh() {
        destroy();

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        if (monitor == null) {
            throw new IllegalArgumentException("Window requires a monitor");
        }

        int monitorWidth = monitor.vidMode.width();
        int monitorHeight = monitor.vidMode.height();

        if (fullscreen) {
            width = monitorWidth;
            height = monitorHeight;
        }

        glfwWindowID = glfwCreateWindow(width, height, title, fullscreen ? monitor.glfwMonitorID : 0L, 0L);

        if (glfwWindowID == 0) {
            throw new IllegalStateException("Failed to create window");
        }

        glfwSetWindowPos(glfwWindowID, (monitorWidth - width) / 2, (monitorHeight - height) / 2);
        glfwMakeContextCurrent(glfwWindowID);
        glfwSwapInterval(vSync ? 1 : 0);
        glfwShowWindow(glfwWindowID);

        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
        //glEnable(GL_CULL_FACE);
        //glCullFace(GL_BACK);

        glfwSetFramebufferSizeCallback(glfwWindowID, (long glfwWindowID, int width, int height) -> {
            if (this.glfwWindowID == glfwWindowID) {
                this.width = width;
                this.height = height;
            }
        });
    }

    public void destroy() {
        if (glfwWindowID != 0L) {
            Callbacks.glfwFreeCallbacks(glfwWindowID);

            glfwDestroyWindow(glfwWindowID);
        }

        glfwWindowID = 0L;
    }

    public boolean isOpen() {
        return !glfwWindowShouldClose(glfwWindowID);
    }

    public void swapBuffers() {
        glfwSwapBuffers(glfwWindowID);
    }

    public long getGlfwWindowID() {
        return glfwWindowID;
    }
}
