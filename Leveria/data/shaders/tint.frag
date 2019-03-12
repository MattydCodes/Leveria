#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif
#define PROCESSING_TEXTURE_SHADER
uniform sampler2D texture;
uniform float alpha;
varying vec4 vertColor;
varying vec4 vertTexCoord;
void main(void) {
  if(texture2D(texture,vertTexCoord.st).a > 0.1){
    gl_FragColor = vec4(texture2D(texture,vertTexCoord.st).rgb,alpha);
  }
}