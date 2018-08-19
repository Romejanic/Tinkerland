#version 330 core

layout(location = 0) out vec4 fragColor;
layout(location = 1) out vec4 fragNormal;
layout(location = 2) out vec2 fragSpecular;

in vec2 v_texCoords;
in vec3 v_normal;

uniform sampler2D albedoTex;

void main() {
	fragColor  = vec4(texture(albedoTex, v_texCoords).xyz, 1.);
	fragNormal = vec4(normalize(v_normal) * .5 + .5, 1.);
	fragSpecular = vec2(0., 1. - fragColor.x);
}