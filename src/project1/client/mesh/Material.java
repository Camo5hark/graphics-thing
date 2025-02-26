package project1.client.mesh;

import org.joml.Vector4d;
import project1.client.texture.MapTexture;

public class Material {
    public Vector4d albedo;
    public double shininess;
    public MapTexture texture;

    public Material(Vector4d albedo, double shininess, MapTexture texture) {
        this.albedo = albedo;
        this.shininess = shininess;
        this.texture = texture;
    }

    public Material() {
        this(new Vector4d(1.0), 1.0, null);
    }

    public boolean hasTexture() {
        return texture != null;
    }
}
