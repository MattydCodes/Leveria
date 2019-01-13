#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif
#define PROCESSING_TEXTURE_SHADER
uniform sampler2D texture;
uniform float skyr;
vec2 texOffset = vec2(0.0015,0.003);
varying vec4 vertColor;
varying vec4 vertTexCoord;
void main(void) {
  float count = 1;
  vec3 avgcol = vec3(0.0,0.0,0.0);
  for(int x = -9; x < 10; x++){ 
    for(int y = -9; y < 10; y++){ 
      vec2 tc = vertTexCoord.st + vec2(x*0.003,y*0.0054);
      vec4 col = texture2D(texture,tc);
      if((col.r <= 1.0 && col.r > 0.9 && col.g > 0.6 && col.g < 0.7 && col.b > 0.2 && col.b < 0.3) || (col.rgb == vec3(1.0,1.0,1.0))){
        count=count+1;
        avgcol.rgb+=col.rgb;
      }
    }
  }
  if(count > 0){
    avgcol.r/=count;
    avgcol.g/=count;
    avgcol.b/=count;
    gl_FragColor = vec4(avgcol,count/200);
  }
}
//(255, 174, 68) (1.0,0.68235294117,0.266666666666)