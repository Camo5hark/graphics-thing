#version 460 core
#define GAMMA_EXP 2.2
#define SPEC_EXP 16.0
#define HALO_SCALE 1.25

struct Material {
    vec4 albedo;
    float shininess;
    int hasTexture;
};

struct AmbientLight {
    vec3 color;
    float intensity;
};

struct DirectionalLight {
    vec3 color;
    float intensity;
    vec3 direction;
};

struct PointLight {
    vec3 color;
    float intensity;
    vec3 position;
};

struct SpotLight {
    vec3 color;
    float intensity;
    vec3 position;
    vec3 direction;
    float cutOff;
};

const vec3 gammaExp = vec3(GAMMA_EXP);

in VSHOutput {
    vec3 position;
    vec2 texcoords;
    vec3 normal;
} vshOutput;

out vec4 fragColor;

uniform Material material;
uniform sampler2D albedoTexture;

uniform vec3 cameraPos;

uniform AmbientLight ambientLight;
uniform DirectionalLight directionalLight;
uniform PointLight pointLight;
uniform SpotLight spotLight;

vec3 albedo;

vec3 calcLight(vec3 lightDir, vec3 color, float intensity) {
    float diffuseFactor = max(dot(vshOutput.normal, lightDir), 0.0);
    vec3 lightDiffuse = albedo * color * intensity * diffuseFactor;

    float specularFactor = pow(max(dot(vshOutput.normal, normalize(lightDir + normalize(cameraPos - vshOutput.position))), 0.0), SPEC_EXP); //max(dot(normalize(cameraPos - _position), normalize(reflect(-lightDir, _normal))), 0.0);
    vec3 lightSpecular = albedo * color * material.shininess * specularFactor;

    return lightDiffuse + lightSpecular;
}

vec3 calcPosLight(vec3 toLight, vec3 lightDir, vec3 color, float intensity) {
    float distance = length(toLight);

    return calcLight(lightDir, color, intensity) / (distance * distance);
}

vec3 calcAmbientLight() {
    return albedo * ambientLight.color * ambientLight.intensity;
}

vec3 calcDirectionalLight() {
    return calcLight(-directionalLight.direction, directionalLight.color, directionalLight.intensity);
}

vec3 calcPointLight() {
    vec3 toLight = pointLight.position - vshOutput.position;

    return calcPosLight(toLight, normalize(toLight), pointLight.color, pointLight.intensity);
}

vec3 calcSpotLight() {
    vec3 toLight = spotLight.position - vshOutput.position;
    vec3 lightDir = normalize(toLight);

    float theta = dot(-lightDir, normalize(spotLight.direction));
    float haloStart = cos(spotLight.cutOff);
    float haloEnd = cos(spotLight.cutOff * HALO_SCALE);

    vec3 light = calcPosLight(toLight, lightDir, spotLight.color, spotLight.intensity);
    float attenuation = clamp((theta - haloEnd) / (haloStart - haloEnd), 0.0, 1.0);

    return light * attenuation;
}

void main() {
    float opacity = material.albedo.a;

    if (material.hasTexture > 0) {
        vec4 textureColor = texture(albedoTexture, vshOutput.texcoords);

        albedo = textureColor.rgb;
        opacity *= textureColor.a;
    } else {
        albedo = material.albedo.rgb;
    }

    fragColor = vec4(pow(calcAmbientLight() + calcDirectionalLight() + calcPointLight() + calcSpotLight(), gammaExp), opacity);
}
