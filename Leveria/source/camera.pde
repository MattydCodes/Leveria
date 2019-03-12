float ch = 200;
float rz = 0;
float lastz = 0;
void cam(){
  int r = 300;
  float x = r * cos(radians(mouse.x));
  float y = r * sin(radians(mouse.x));
  float z = r * cos(radians(mouse.y));
  d3.perspective(PI/3.0, float(width)/float(height), (height/2.0) / tan(PI/3.0/2.0)/20.0, (height/2.0) / tan(PI/3.0/2.0)*7.5);
  d3.camera(0,0,-ch,-x,-y,-ch+z,rz,0,1-rz);
}
void camend(){
  int r = 300;
  float x = r * cos(radians(mouse.x));
  float y = r * sin(radians(mouse.x));
  float z = r * cos(radians(mouse.y));
  d3.perspective(PI/3.0, float(width)/float(height), (height/2.0) / tan(PI/3.0/2.0)/60.0, (height/2.0) / tan(PI/3.0/2.0)*100.0);
  if(stage == 2 && stuck && bcd > bcdreset-1){
    d3.camera(0,0,-ch,-x,-y,-ch+z,rz+cos(bcd*50)/10.0,0,1-rz);  
  }else{
    d3.camera(0,0,-ch,-x,-y,-ch+z,rz,0,1-rz);  
  }
}
