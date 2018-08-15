#version 330 core

struct Light {
	vec3 direction;
	vec4 color;
	float intensity;
};

layout(location = 0) out vec3 diffuse_out;
layout(location = 1) out vec3 specular_out;
in vec2 v_texCoords;

uniform sampler2D normals;
uniform sampler2D depth;
uniform vec3 cameraPos;
uniform mat4 invProjMat;
uniform mat4 invViewMat;

uniform Light lightSource;

vec3 worldSpaceFromDepth(vec2 samplePos) {
	float sample = texture(depth, samplePos).r * 2. - 1.;
	vec4 clipSpace = vec4(samplePos * 2. - 1., sample, 1.);
	vec4 viewSpace = invProjMat * clipSpace;
	viewSpace /= viewSpace.w;
	
	return (invViewMat * viewSpace).xyz;
}

void main() {
	vec4 normalTex = texture(normals, v_texCoords);
	if(normalTex.a < .5) {
		discard;
		return;
	}

	vec3 pos    = worldSpaceFromDepth(v_texCoords);
	vec3 normal = normalize(normalTex.xyz * 2. - 1.);
	vec3 eye    = normalize(cameraPos - pos);
	vec3 refl   = normalize(-reflect(eye, normal));
	
	float d = max(dot(lightSource.direction, normal),0.);
	float s = pow(max(dot(lightSource.direction, refl), 0.), 45.);
	
	diffuse_out = lightSource.color.xyz * d * lightSource.intensity;
	specular_out = lightSource.color.xyz * s * lightSource.intensity;
}