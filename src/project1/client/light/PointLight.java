package project1.client.light;

import org.joml.Vector3d;

public class PointLight extends AmbientLight {
    public Vector3d color;
    public double intensity;
    public Vector3d position;

    public PointLight(Vector3d color, double intensity, Vector3d position) {
        this.color = color;
        this.intensity = intensity;
        this.position = position;
    }

    public PointLight() {
        this(new Vector3d(1.0), 1.0, new Vector3d());
    }
}
