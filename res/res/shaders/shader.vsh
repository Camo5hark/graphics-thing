#version 460 core

layout(location=0) in vec3 position;
layout(location=1) in vec2 texcoords;
layout(location=2) in vec3 normal;

out VSHOutput {
    vec3 position;
    vec2 texcoords;
    vec3 normal;
} vshOutput;

uniform mat4 projection;
uniform mat4 worldView;
uniform mat4 modelView;

void main() {
    vec4 modelViewPos = modelView * vec4(position, 1.0);

    gl_Position = projection * worldView * modelViewPos;
    vshOutput.position = modelViewPos.xyz;
    vshOutput.texcoords = texcoords;
    vshOutput.normal = normalize(modelView * vec4(normal, 0.0)).xyz;
}
