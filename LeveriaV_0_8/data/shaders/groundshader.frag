#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif
#define PROCESSING_TEXTURE_SHADER
uniform sampler2D texture;
uniform vec2 ppos;
vec2 texOffset = vec2(0.0015,0.003);
varying vec4 vertColor;
varying vec4 vertTexCoord;


vec4 lerpColor(vec4 col1){
	if(col1.r > 0.02 || col1.g > 0.02 || col1.b > 0.02 || col1.a > 0.02){
		return(vec4(col1.r-0.01,col1.g-0.01,col1.b-0.01,col1.a-0.01));
	}else{
		return(vec4(col1.r,col1.g,col1.b,col1.a));
	}
}

void main(void) {
	gl_FragColor = lerpColor(texture2D(texture,vertTexCoord.st));
}
