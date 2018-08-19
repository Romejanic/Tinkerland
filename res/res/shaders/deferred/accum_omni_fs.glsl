#version 330 core
#include <lighting/pbr.glsl>

#ifndef __DEFERRED_MAX_LIGHTS_OMNI__
	#define __DEFERRED_MAX_LIGHTS_OMNI__ 100
#endif

struct Light {
	vec3 position;
	float range;
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

uniform Light lightSources[__DEFERRED_MAX_LIGHTS_OMNI__];
uniform int lightCount;

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
	
	diffuse_out = vec3(0.);
	specular_out = vec3(0.);
	
	for(int i = 0; i < lightCount; i++) {
		Light lightSource = lightSources[i];
		vec3 light  = lightSource.position - pos;
		float dist  = length(light);
		if(dist > lightSource.range) {
			continue;
		}
		light *= 1. / dist;
	
		float atten  = max(1. - ((dist*dist) / (lightSource.range*lightSource.range)), 0.);
		vec3 halfVec = normalize(light + eye);
        
        float ldoth = max(dot(light,halfVec) ,0.);
        float ndoth = max(dot(normal,halfVec) ,0.);
        float ndotv = max(dot(normal,eye),0.);
        float ndotl = max(dot(normal,light) ,0.);
        
        float metallic  = 0.;
        float roughness = .6;
        vec3 spec = BRDF_CookTorrance(ldoth, ndoth, ndotv, ndotl, roughness, metallic);
	
		diffuse_out += lightSource.color.xyz * ndotl * lightSource.intensity * atten;
		specular_out += lightSource.color.xyz * spec * lightSource.intensity * atten;
	}
}