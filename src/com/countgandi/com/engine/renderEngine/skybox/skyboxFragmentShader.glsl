#version 400

in vec2 textureCoords;
out vec4 out_Color;

uniform float blendFactor;
uniform vec3 fogColor;

const float lowerLimit = 0.0;
const float upperLimit = 30.0;

float rand(vec2 n);
float noise(float p);
float noise(vec2 p);

void main(void){
	float noises = noise(textureCoords);
    vec4 finalColor = vec4(1);
    
    float factor = (textureCoords.y - lowerLimit) / (upperLimit - lowerLimit);
    factor = clamp(factor, 0.0, 1.0);
    out_Color = finalColor;
}

float rand(float n){return fract(sin(n) * 43758.5453123);}

float noise(float p){
	float fl = floor(p);
  float fc = fract(p);
	return mix(rand(fl), rand(fl + 1.0), fc);
}
	
float noise(vec2 n) {
	const vec2 d = vec2(0.0, 1.0);
  vec2 b = floor(n), f = smoothstep(vec2(0.0), vec2(1.0), fract(n));
	return mix(mix(rand(b), rand(b + d.yx), f.x), mix(rand(b + d.xy), rand(b + d.yy), f.x), f.y);
}