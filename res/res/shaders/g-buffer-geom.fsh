#version 460 core

struct Material {
    vec4 albedo;
    float shininess;
    int hasTexture;
};

in VSHOutput {
    vec3 position;
    vec2 texcoords;
    vec3 normal;
} vshOutput;

layout(location=0) out vec4 position;
layout(location=1) out vec4 normalAndShininess;
layout(location=2) out vec4 albedo;

uniform Material material;
uniform sampler2D albedoTexture;

void main() {
    position = vec4(vshOutput.position, 1.0);
    normalAndShininess = vec4(vshOutput.normal, material.shininess);

    if (material.hasTexture > 0) {
        vec4 textureColor = texture(albedoTexture, vshOutput.texcoords);

        albedo = textureColor;
        albedo.a *= material.albedo.a;
    } else {
        albedo = material.albedo;
    }
}
