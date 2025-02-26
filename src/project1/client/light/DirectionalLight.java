package project1.client.light;

import org.joml.Vector3d;

public class DirectionalLight {
    public Vector3d color;
    public double intensity;
    public Vector3d direction;

    public DirectionalLight(Vector3d color, double intensity, Vector3d direction) {
        this.color = color;
        this.intensity = intensity;
        this.direction = direction;
    }

    public DirectionalLight() {
        this(new Vector3d(1.0), 1.0, new Vector3d(1.0, -2.0, -0.5));
    }
}
