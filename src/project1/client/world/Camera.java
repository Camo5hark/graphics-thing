package project1.client.world;

import org.joml.Matrix4d;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import project1.client.Client;
import project1.helper.MathHelper;
import project1.client.display.Window;

public class Camera {
    private static final Window WINDOW = Client.CLIENT.getWindow();

    public double fov;
    public Vector3d position;
    public Vector3d rotation;
    private Matrix4d projection;
    private Matrix4d worldView;

    public Camera(double fov, Vector3d position, Vector3d rotation) {
        this.fov = fov;
        this.position = position;
        this.rotation = rotation;

        updateProjection();
        updateWorldView();
    }

    public Camera() {
        this(90.0, new Vector3d(), new Vector3d());
    }

    public void updateProjection() {
        projection = new Matrix4d().perspective(Math.toRadians(fov), (double) WINDOW.width / (double) WINDOW.height, 0.001, 1000.0);
    }

    public void updateWorldView() {
        rotation.x = MathHelper.normalizeRadians(rotation.x);
        rotation.y = MathHelper.normalizeRadians(rotation.y);
        rotation.z = MathHelper.normalizeRadians(rotation.z);
        worldView = new Matrix4d()
                .rotate(new Quaterniond().rotateZ(-rotation.z))
                .rotate(new Quaterniond().rotateX(-rotation.x))
                .rotate(new Quaterniond().rotateY(-rotation.y))
                .translate(position.negate(new Vector3d()));
    }

    public Matrix4d getProjection() {
        return projection;
    }

    public Matrix4d getWorldView() {
        return worldView;
    }
}
