ArrayList<particlesystem> particles = new ArrayList<particlesystem>();
class particlesystem{
  int amount = 0;
  float gravity = 0;
  float d = 0;
  PVector[] pos;
  PVector[] vel;
  int[] fcl;
  int ls = 0;
  int count = 0;
  PImage tex;
  color c;
  particlesystem(PVector origin,int amount_, float maxv, float g, float d_, int lifespan, int w_, int h_, color c_, float degree, float degreez, int rng){
    c = c_;
    amount = amount_;
    ls = lifespan;
    gravity = g;
    d = d_;
    tex = createImage(w_,h_,ARGB);
    tex.loadPixels();
    for(int i = 0; i < tex.pixels.length; i++){
      if(noise(i+cos(millis()/1000.0)) > 0.5){
        tex.pixels[i] = color(red(c)+random(-10,10),green(c)+random(-10,10),blue(c)+random(-10,10));
      }
    }
    tex.updatePixels();
    pos = new PVector[amount];
    vel = new PVector[amount];
    fcl = new int[amount];
    for(int i = 0; i < amount; i++){
      float rnd = radians(random(-rng,rng));
      degree = radians(degree);
      degreez = radians(degreez);
      float x = cos(degree+rnd);
      float y = sin(degree+rnd);
      float z = cos(random(degreez+rnd));
      float t = sqrt(x*x) + sqrt(y*y) + sqrt(z*z);
      float v = random(maxv);
      vel[i] = new PVector(x/t*v,y/t*v,z/t*v);
      pos[i] = new PVector(origin.x,origin.y,origin.z);
      fcl[i] = int(random(lifespan));
    }
  }
  void display(){
    count++;
    for(int i = 0; i < amount; i++){
      if(count > fcl[i]){
        continue;
      }
      pos[i].x+=vel[i].x;
      pos[i].y+=vel[i].y;
      pos[i].z+=vel[i].z;
      vel[i].x = lerp(vel[i].x,0,d);
      vel[i].y = lerp(vel[i].y,0,d);
      vel[i].z = lerp(vel[i].z,0,d);
      vel[i].z-=gravity;
    }
    for(int i = 0; i < amount; i++){
      if(count > fcl[i]){
        continue;
      }
     // d3.rotateX(bearing(ppos.x,ppos.y,pos[i].x,pos[i].y)+HALF_PI);
      d3.translate(pos[i].x,pos[i].y,pos[i].z);
      d3.rotateZ(bearing(ppos.x,ppos.y,pos[i].x,pos[i].y)+HALF_PI);
      d3.beginShape(QUAD);
      d3.textureMode(NORMAL);
      d3.texture(tex);
      d3.emissive(c);
      d3.vertex(0,0,0,0,0);
      d3.vertex(tex.width,0,0,1,0);
      d3.vertex(tex.width,0,tex.height,1,1);
      d3.vertex(0,0,tex.height,0,1);
      d3.endShape();
      d3.rotateZ(-bearing(ppos.x,ppos.y,pos[i].x,pos[i].y)-HALF_PI);
      d3.translate(-pos[i].x,-pos[i].y,-pos[i].z);
      //d3.rotateX(-bearing(ppos.x,ppos.y,pos[i].x,pos[i].y)-HALF_PI);
    }
  }
}

void manageparticles(){
  for(int i = particles.size()-1; i > -1; i--){
    if(particles.get(i).count >= particles.get(i).ls){
      particles.remove(i);
      continue;
    }
    particles.get(i).display();
  }
}
