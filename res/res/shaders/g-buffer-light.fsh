#version 460 core
#define GAMMA_EXP 2.2
#define SPEC_EXP 16.0
#define HALO_SCALE 1.25

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
    vec2 texcoords;
} vshOutput;

out vec4 fragColor;

uniform sampler2D positionTexture;
uniform sampler2D normalAndShininessTexture;
uniform sampler2D albedoTexture;

uniform vec3 cameraPos;

uniform AmbientLight ambientLight;
uniform DirectionalLight directionalLight;
uniform PointLight pointLight;
uniform SpotLight spotLight;

vec3 position;
vec3 normal;
float shininess;
vec3 albedo;

vec3 calcLight(vec3 lightDir, vec3 color, float intensity) {
    float diffuseFactor = max(dot(normal, lightDir), 0.0);
    vec3 lightDiffuse = albedo * color * intensity * diffuseFactor;

    float specularFactor = pow(max(dot(normal, normalize(lightDir + normalize(cameraPos - position))), 0.0), SPEC_EXP);
    vec3 lightSpecular = albedo * color * shininess * specularFactor;

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
    vec3 toLight = pointLight.position - position;

    return calcPosLight(toLight, normalize(toLight), pointLight.color, pointLight.intensity);
}

vec3 calcSpotLight() {
    vec3 toLight = spotLight.position - position;
    vec3 lightDir = normalize(toLight);

    float theta = dot(-lightDir, normalize(spotLight.direction));
    float haloStart = cos(spotLight.cutOff);
    float haloEnd = cos(spotLight.cutOff * HALO_SCALE);

    vec3 light = calcPosLight(toLight, lightDir, spotLight.color, spotLight.intensity);
    float attenuation = clamp((theta - haloEnd) / (haloStart - haloEnd), 0.0, 1.0);

    return light * attenuation;
}

void main() {
    position = texture(positionTexture, vshOutput.texcoords).rgb;

    vec4 normalAndShininess = texture(normalAndShininessTexture, vshOutput.texcoords);

    normal = normalAndShininess.rgb;
    shininess = normalAndShininess.a;

    vec4 albedoAndOpacity = texture(albedoTexture, vshOutput.texcoords);

    albedo = albedoAndOpacity.rgb;

    fragColor = vec4(pow(calcAmbientLight() + calcDirectionalLight() + calcPointLight() + calcSpotLight(), gammaExp), albedoAndOpacity.a);
}
