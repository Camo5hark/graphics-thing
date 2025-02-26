package project1.client;

import static org.lwjgl.glfw.GLFW.*;

public class Timer {
    private static double deltaTime = glfwGetTime();
    private static double fps;
    private static double time;

    public static void update() {
        double now = glfwGetTime();

        deltaTime = now - time;
        fps = 1.0 / deltaTime;
        time = now;
    }

    public static double getDeltaTime() {
        return deltaTime;
    }

    public static double getFPS() {
        return fps;
    }

    public static double getTime() {
        return time;
    }
}
