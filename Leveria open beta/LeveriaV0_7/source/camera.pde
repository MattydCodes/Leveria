float ch = 200;
float rz = 0;
float lastz = 0;
void cam(){
  int r = 300;
  float x = r * cos(radians(mouse.x));
  float y = r * sin(radians(mouse.x));
  float z = r * cos(radians(mouse.y));
  d3.camera(0,0,-ch,-x,-y,-ch+z,rz,0,1-rz);
}