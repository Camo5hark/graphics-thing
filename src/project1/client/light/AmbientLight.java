package project1.client.light;

import org.joml.Vector3d;

public class AmbientLight {
    public Vector3d color;
    public double intensity;

    public AmbientLight(Vector3d color, double intensity) {
        this.color = color;
        this.intensity = intensity;
    }

    public AmbientLight() {
        this(new Vector3d(1.0), 0.1);
    }
}
