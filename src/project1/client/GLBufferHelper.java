package project1.client;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryUtil.*;

public class GLBufferHelper {
    public static void free(Buffer buffer) {
        if (buffer != null) {
            memFree(buffer);
        }
    }

    public static long getAddress(Buffer buffer) {
        return buffer != null ? memAddress(buffer) : 0L;
    }

    public static ByteBuffer allocByte(int size) {
        return memAlloc(size);
    }

    public static IntBuffer allocInt(int size) {
        return memAllocInt(size);
    }

    public static FloatBuffer allocFloat(int size) {
        return memAllocFloat(size);
    }

    public static ByteBuffer wrap(byte... data) {
        return (ByteBuffer) allocByte(data.length).put(data).flip();
    }

    public static IntBuffer wrap(int... data) {
        return (IntBuffer) allocInt(data.length).put(data).flip();
    }

    public static FloatBuffer wrap(float[] data) {
        return (FloatBuffer) allocFloat(data.length).put(data).flip();
    }
}
