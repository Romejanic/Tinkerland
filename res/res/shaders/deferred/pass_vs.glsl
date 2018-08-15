#version 330 core

layout(location = 0) in vec3 vertex;
layout(location = 1) in vec2 texCoords;
layout(location = 2) in vec3 normal;

uniform mat4 projViewMat;
uniform mat4 modelMat;

out vec2 v_texCoords;
out vec3 v_normal;

void main() {
	gl_Position = projViewMat * modelMat * vec4(vertex, 1.);
	v_texCoords = texCoords;
	v_normal    = (modelMat * vec4(normal, 0.)).xyz;
}