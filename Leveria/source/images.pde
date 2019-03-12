PImage corner;
PImage flower;
PImage swordph;
PImage sworda;
PImage swordb;
PImage swordc;
PImage swordd;
PImage shielda;
PImage shieldb;
PImage shieldc;
PImage shield;
PImage shieldph;
PImage bin;
PImage inventorytxt;
PImage statstxt;
PImage forgetxt;
PImage anvil;
PImage hammer;
PImage pillartext;
PImage cloudtext;
PShape overloardshoot;
PShape overloardslam;
PShape[] peoplemodels = new PShape[3];
PShape shade;
PShape shadspn;
PShape mboss;
PShape mproj;
PShape msworda;
PShape mswordb;
PShape mswordc;
PShape mswordd;
PShape clawL;
PShape clawR;
PShape mdrop;
PShape tree;
PImage tutorial;
PImage fireimg1;
PImage fireimg2;
PImage fireimg3;
PImage portal1;
PImage portal2;
PImage portal3;
PImage menusgoff;
PImage menusgon;
PImage menungoff;
PImage menungon;
PImage menuegoff;
PImage menuegon;

PShape loadshape(String path, String textu){
  PShape r = loadShape(path);
  PImage tex = loadImage(textu);
  tex.loadPixels();
  PShape s = createShape();
  s.beginShape(TRIANGLES);
  s.noStroke();
  s.texture(tex);
  s.textureMode(NORMAL);
  for (int i=0; i<r.getChildCount (); i++) {
    if (r.getChild(i).getVertexCount() ==3) {
      for (int j=0; j<r.getChild (i).getVertexCount(); j++) {
        PVector p = r.getChild(i).getVertex(j);
        PVector n = r.getChild(i).getNormal(j);
        float u = r.getChild(i).getTextureU(j);
        float v = r.getChild(i).getTextureV(j);
        try{
          int xu = int(u * tex.width);
          int yv = int(v * tex.height);
          int index = xu + yv * tex.width;
          if((red(tex.pixels[index]) + green(tex.pixels[index]) + blue(tex.pixels[index]))/3.0 == 255){
            s.emissive(255,255,255);
          }else{
            s.emissive(0,0,0);
          }
        }catch(Exception e){
        }
        s.normal(n.x, n.y, n.z);
        s.vertex(p.x, p.y, p.z, u, v);
      }
    }
  }
  s.endShape();
  return s;
}
