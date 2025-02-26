#version 460 core

layout(location=0) in vec3 position;
layout(location=1) in vec2 texcoords;
layout(location=2) in vec3 normal;

out VSHOutput {
    vec2 texcoords;
} vshOutput;

void main() {
    gl_Position = vec4(position, 1.0);

    vshOutput.texcoords = texcoords;
}
