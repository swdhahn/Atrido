#version 400

in vec3 position;
out vec2 textureCoords;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void){
	
    textureCoords = vec2(position.x, position.z);
	gl_Position = projectionMatrix * viewMatrix * vec4(position, 1);
	
}