#version 330 core

out vec4 fragColor;
in vec2 v_texCoords;

uniform sampler2D buffers[5];
uniform int index;

void main() {
	fragColor = texture(buffers[index], v_texCoords);
}