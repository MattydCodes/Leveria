#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif
#define PROCESSING_TEXTURE_SHADER
uniform sampler2D texture;
uniform vec3 skyr;
uniform vec3 mousepos;
vec2 texOffset = vec2(0.0015,0.0026);
varying vec4 vertColor;
varying vec4 vertTexCoord;
float random (in vec2 st) {
    return fract(sin(dot(st.xy,vec2(12.9898,78.233)))*10000.0);
}
float noise (in vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);
    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(a, b, u.x) + (c - a)* u.y * (1.0 - u.x) +(d - b) * u.x * u.y;
}
#define OCTAVES 6
float fbm (in vec2 st) {
    float value = 0.0;
    float amplitude = .5;
    for (int i = 0; i < OCTAVES; i++) {
        value += amplitude * noise(st);
        st *= 2.;
        amplitude *= .5;
    }
    return value;
}
void main(void) {
        vec2 tc = vertTexCoord.st;
	for(int x = -2; x < 3; x++){ 
        for(int y = -2; y < 3; y++){ 
	vec4 col = texture2D(texture,tc+vec2(x,y));
	if(col.r > 0.9 && col.g < 0.95 && col.b > 0.9){
		float val = fbm((vertTexCoord.st+mousepos.st+vec2(x,y))*2);
		if(val > 0.1){
			gl_FragColor = mix(vec4(1,1,1,1.0),vec4(skyr,1.0),val);
		}else{
			gl_FragColor = vec4(skyr.rgb,1.0);
		}
	}
	}
	}
	
	
}
//cloud colour is (1.0,0,1.0)