package project1.client.world;

import org.joml.Matrix4d;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import project1.helper.MathHelper;

public class Transform {
    public Vector3d position;
    public Vector3d rotation;

    public Transform(Vector3d position, Vector3d rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Transform() {
        this(new Vector3d(), new Vector3d());
    }

    public Matrix4d calcModelView() {
        rotation.x = MathHelper.normalizeRadians(rotation.x);
        rotation.y = MathHelper.normalizeRadians(rotation.y);
        rotation.z = MathHelper.normalizeRadians(rotation.z);

        return new Matrix4d()
                .translate(position)
                .rotate(new Quaterniond().rotateY(rotation.y))
                .rotate(new Quaterniond().rotateX(rotation.x))
                .rotate(new Quaterniond().rotateZ(rotation.z));
    }
}
