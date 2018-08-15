#version 330 core

out vec4 fragColor;
in vec2 v_texCoords;

uniform sampler2D gBuffer;
uniform sampler2D diffuse;
uniform sampler2D specular;
uniform vec4 ambient;

void main() {
	vec4 albedo = texture(gBuffer, v_texCoords);
	if(albedo.a < .5) {
		discard;
		return;
	}
	vec3 totalDiffuse = ambient.xyz + texture(diffuse, v_texCoords).xyz;
	vec3 totalSpecular = texture(specular, v_texCoords).xyz;
	fragColor.xyz = albedo.xyz * totalDiffuse + totalSpecular;
	fragColor.a   = 1.;
}