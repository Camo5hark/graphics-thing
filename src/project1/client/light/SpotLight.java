package project1.client.light;

import org.joml.Vector3d;

public class SpotLight extends PointLight {
    public Vector3d color;
    public double intensity;
    public Vector3d position;
    public Vector3d direction;
    public double cutOff;

    public SpotLight(Vector3d color, double intensity, Vector3d position, Vector3d direction, double cutOff) {
        this.color = color;
        this.intensity = intensity;
        this.position = position;
        this.direction = direction;
        this.cutOff = cutOff;
    }

    public SpotLight() {
        this(new Vector3d(1.0), 1.0, new Vector3d(), new Vector3d(0.0, -1.0, 0.0), Math.toRadians(15.0));
    }
}
