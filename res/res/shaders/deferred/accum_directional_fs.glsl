#version 330 core
#include <lighting/pbr.glsl>

#ifndef __DEFERRED_MAX_LIGHTS_DIRECTIONAL__
	#define __DEFERRED_MAX_LIGHTS_DIRECTIONAL__ 10
#endif

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
uniform sampler2D specular;
uniform vec3 cameraPos;
uniform mat4 invProjMat;
uniform mat4 invViewMat;

uniform Light lightSources[__DEFERRED_MAX_LIGHTS_DIRECTIONAL__];
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
	vec2 specularTex = texture(specular, v_texCoords).rg;
	float metallic   = specularTex.x;
    float roughness  = specularTex.y;

	vec3 pos    = worldSpaceFromDepth(v_texCoords);
	vec3 normal = normalize(normalTex.xyz * 2. - 1.);
	vec3 eye    = normalize(cameraPos - pos);
	vec3 refl   = normalize(-reflect(eye, normal));
	
	diffuse_out = vec3(0.);
	specular_out = vec3(0.);
	
	for(int i = 0; i < lightCount; i++) {
		Light lightSource = lightSources[i];
		vec3 halfVec = normalize(lightSource.direction + eye);
        
        float ldoth = max(dot(lightSource.direction,halfVec) ,0.);
        float ndoth = max(dot(normal,halfVec) ,0.);
        float ndotv = max(dot(normal,eye),0.);
        float ndotl = max(dot(normal,lightSource.direction) ,0.);
        
        vec3 spec = BRDF_CookTorrance(ldoth, ndoth, ndotv, ndotl, roughness, metallic);
	
		diffuse_out += lightSource.color.xyz * ndotl * lightSource.intensity;
		specular_out += lightSource.color.xyz * spec * lightSource.intensity;
	}
}