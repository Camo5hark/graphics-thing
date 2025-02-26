package project1.client.display;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;

public class Monitor {
    public final long glfwMonitorID;
    public final GLFWVidMode vidMode;

    public Monitor(int index) {
        PointerBuffer glfwMonitorIDs = glfwGetMonitors();

        if (glfwMonitorIDs == null) {
            throw new IllegalStateException("No monitors were found");
        }

        int monitorCount = glfwMonitorIDs.limit();

        if (index < 0 || index >= monitorCount) {
            throw new IndexOutOfBoundsException(index + ", " + monitorCount + " monitors found");
        }

        glfwMonitorID = glfwMonitorIDs.get(index);
        vidMode = glfwGetVideoMode(glfwMonitorID);
    }
}
