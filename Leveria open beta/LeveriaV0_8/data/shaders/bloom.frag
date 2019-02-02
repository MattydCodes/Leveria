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
  float count = 0;
  vec3 avgcol = vec3(0.0,0.0,0.0);
  for(int x = -8; x < 9; x++){ 
    for(int y = -8; y < 9; y++){ 
      vec2 tc = vertTexCoord.st + vec2(x*(1.0/320.0),y*(1.0/180.0));
      vec4 col = texture2D(texture,tc);
      if((col.r > 0.75 && col.g > 0.55 && col.g < 0.8 && col.b > 0.1 && col.b < 0.4) || (col.r > 0.7 && col.g > 0.7 && col.b > 0.7)){
        count=count+1;
        avgcol.rgb+=col.rgb;
      }
    }
  }
  if(count > 0){
    avgcol.r/=count;
    avgcol.g/=count;
    avgcol.b/=count;
    if(avgcol.r > 0.7 && avgcol.g > 0.7 && avgcol.b > 0.7){
      gl_FragColor = vec4(avgcol,count/500*(((avgcol.r+avgcol.g+avgcol.b)/3-0.7)*5));
    }else{
      gl_FragColor = vec4(avgcol,count/200);
    }
  }
}
//(255, 174, 68) (1.0,0.68235294117,0.266666666666)