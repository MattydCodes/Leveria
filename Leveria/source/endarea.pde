// Make a shader for the ground lighting.
int stage = 0;
boolean stuck = false;
float bcdreset = 1;
float bcd = 0;
float bossrot = 0;
PVector bosspos = new PVector(14500,1500,0);
float bossvel = 0;
PShape portal;
boolean entered = false;
boolean bossdead = false;
boolean pdir = true;
int count = 1;
int frame = 1;
int delay = 0;
int deadcount = 0;
float bosshp = 7500;
PGraphics groundshader;
boolean attacking = false;
ArrayList<shadowspawn> shadowspawns = new ArrayList<shadowspawn>();
ArrayList<proj> bossproj = new ArrayList<proj>();
void displayportal(){
  count++;
  if(frame == 1 && pdir && count > 4){
    frame = 2;
    portal.setTexture(portal2);
    count = 0;
  }else if(frame == 2 && pdir && count > 4){  
    frame = 3;
    portal.setTexture(portal3);
    pdir = false;
    count = 0;
  }else if(frame == 3 && count > 4){ 
    frame = 2;
    portal.setTexture(portal2);
    count = 0;
  }else if(frame == 2 && pdir == false && count > 4){
    frame = 1;
    portal.setTexture(portal1);
    pdir = true;
    count = 0;
  }
  d3.translate(portalpos.x,portalpos.y,portalpos.z-300);
  d3.rotateZ(bearing(ppos.x,ppos.y,portalpos.x,portalpos.y)+HALF_PI);
  d3.shape(portal);
  d3.rotateZ(-bearing(ppos.x,ppos.y,portalpos.x,portalpos.y)-HALF_PI);
  d3.translate(-portalpos.x,-portalpos.y,-portalpos.z+300);
  if(constrain(1-dist(ppos.x,ppos.y,portalpos.x,portalpos.y)/5000,0,1) != 0){
    if(portalhum.isPlaying() == false){
      portalhum.play();
    }
    portalhum.amp(constrain(1-dist(ppos.x,ppos.y,portalpos.x,portalpos.y)/5000,0,1));
    if(frameCount%10 == 0){
      float ro = bearing(ppos.x,ppos.y,portalpos.x,portalpos.y);
      float x = 500 * cos(ro);
      float y = 500 * sin(ro);
      particles.add(new particlesystem(new PVector(portalpos.x+x,portalpos.y+y,portalpos.z-300),100, 100, -0.05, 0.05, 60, 20, 20, color(10,10,10),0,0,360));
      //particlesystem(PVector origin,int amount_, float maxv, float g, float d_, int lifespan, int w_, int h_, color c_, float degree, float degreez, int rng){
    }
  }else{
    portalhum.pause();
  }
  if(dist(ppos.x,ppos.y,portalpos.x,portalpos.y) < 150){
    entered = true;
    mproj.scale(2);
    ppos = new PVector(500,1500,0);
    posheight = 1;
    lastz = 1;
    ambient.pause();
    portalhum.pause();
    whispering.amp(0);
    mouse.x = 180;
    for(int i = mobs.size()-1; i > -1; i--){
      mobs.remove(i);
    }
    for(int i = bosses.size()-1; i > -1; i--){
      bosses.remove(i);
    }
    for(int i = grounds.size()-1; i > -1; i--){
      grounds.remove(i);
    }
    for(int x = 25; x > 10; x--){
      shadowspawns.add(new shadowspawn(new PVector(x*600,0,-160),false,2+(x/25.0)*3));
      shadowspawns.add(new shadowspawn(new PVector(x*600,3000,-160),false,2+(x/25.0)*3));
    }
  }
}
void drawend(){
  if(frameCount%120 == 0 && stage > 0){
    shadowspawns.add(new shadowspawn(new PVector(random(15000),random(3000),-160),true,5));
  }
  whispering.amp(constrain(shadowspawns.size()/50.0,0,1));
  move();
  ppos.x = constrain(ppos.x,240,14760);
  ppos.y = constrain(ppos.y,240,2760);
  if(attacking == false){
    if(delay == 0){
      whispering.amp(constrain(1-dist(0,ppos.x,0,2500)/2500,0,1));
    }
    if(ppos.x > 2500 && delay == 0){
      whispering.amp(0);
      delay = 1;
    }
    if(delay > 0){
      delay++;
      if(delay > 180){
        attacking = true;
        whispering.amp(1);
        for(int i = 0; i < shadowspawns.size(); i++){
          shadowspawns.get(i).attack = true;
        }
      }
    }
  }
  d3.beginDraw();
  d3.background(0);
  d3.ambientLight(200,200,200);
  float x = 400*cos(radians(mouse.x));
  float y = 400*sin(radians(mouse.x));
  d3.pointLight(255,255,255,x,y,50);
  if(bossdead == false){
    bossattacks();
  }
  lighttexture();
  count++;
  d3.translate(-ppos.x,-ppos.y,ppos.z); 
  d3.textureMode(IMAGE);
  d3.beginShape(QUAD);
  d3.texture(groundshader);
  d3.ambient(255);
  d3.emissive(255,255,255);
  d3.vertex(0,0,0,0,3000);
  d3.vertex(15000,0,0,15000,3000);
  d3.vertex(15000,3000,0,15000,0);
  d3.vertex(0,3000,0,0,0);
  d3.endShape();
  for(int i = shadowspawns.size()-1; i > -1; i--){
    shadowspawns.get(i).display();
    if(shadowspawns.get(i).hp <= 0){
      shadowspawns.remove(i);
    }
  } 
  if(stuck == false){
    bossrot = bearing(ppos.x,ppos.y,bosspos.x,bosspos.y);
  }
  if(bossdead == false){
    d3.translate(bosspos.x,bosspos.y,bosspos.z-130);
    d3.rotateX(-HALF_PI);
    d3.rotateY(-bossrot+HALF_PI);
    if(stage == 2 || stage == 0){
      d3.shape(overloardslam);
    }else{
      d3.shape(overloardshoot);
    }
    d3.rotateY(bossrot-HALF_PI);
    d3.rotateX(HALF_PI);
    d3.translate(-bosspos.x,-bosspos.y,-bosspos.z+130);
  }
  manageparticles();
  d3.translate(ppos.x,ppos.y,-ppos.z);
  camend();
  if(righthand.empty == false){
    displaymodel(righthand.model,-30+righthandr.x/float(4),140+righthandr.x/float(9),righthandr.x,-righthandr.x/float(5),0);
  }
  if(lefthand.empty == false){
    displaymodel(lefthand.model,30-lefthandr.x/float(4),140+lefthandr.x/float(9),lefthandr.x,lefthandr.x/float(5),0);
  } 
  if(deadcount!=0){
    tints.set("alpha",(180-deadcount)/180.0);
    d3.filter(tints);
    deadcount++;
    if(deadcount > 180){
      entered = false;
      ibossoutro = true;
      if(entered){
        entered = false;
      }
      ch = 200;
      rz = 0;
      health = 100;
      daycount = 0;
      for(int i = mobs.size()-1; i > -1; i--){
        mobs.remove(i);
      }
      for(int i = grounds.size()-1; i > 0; i--){
        grounds.remove(i);
      }
      ppos.x=portalpos.x;
      ppos.y=portalpos.y;
      ppos.z = portalpos.z;
      tint(255);
      thread("initchunks");
      ui.beginDraw();
      ui.clear();
      ui.endDraw();
      updatehp();
      updateflower();
      updatelefthand();
      updaterighthand();
      updatearmour();
    }
  }
  d3.endDraw();
}
void lighttexture(){
  groundshader.beginDraw();
  groundshader.image(groundshader,0,0,15000,3000);
  if(stage == 2){
    if(bosspos.z < 0){
      groundshader.noFill();
      groundshader.stroke(255);
      groundshader.strokeWeight(3);
      groundshader.ellipse(bosspos.x,3000-bosspos.y,bossvel/70.0*625,bossvel/70.0*625);
    }
  }
  groundshader.noStroke();
  groundshader.fill(255);
  groundshader.ellipse(ppos.x,3000-ppos.y,500,500);
  groundshader.filter(floorlight);
  groundshader.endDraw();
}
class shadowspawn{
  PVector pos = new PVector(0,0,0);
  PVector lastm = new PVector(1,0);
  PVector spos = new PVector(0,0,0);
  float sm = 1;
  float atrot = 0;
  float hp = 100;
  boolean attack = false;
  boolean a = false;
  boolean back = false;
  shadowspawn(PVector pos_, boolean attack_, float sm_){
    pos = pos_;
    spos = pos_;
    attack = attack_;
    sm = sm_;
  }
  void display(){
    float rot;
    if(attack){
      if(dist(ppos.x,ppos.y,pos.x,pos.y) < 100){
        a = true;
      }
      if(a){
        if(atrot < 90 && back == false){
          atrot+=5;
        }else{
          atrot-=5;
        }
        if(atrot >= 90){
          back = true;
          if(dist(ppos.x,ppos.y,pos.x,pos.y) < 150){
            if(inarmour.empty == false){
              health-=50*(1.0-inarmour.stat/100.0);
            }else{
              health-=50;
            }
            painplayer.stop();
            painplayer.play();
            particles.add(new particlesystem(new PVector(ppos.x,ppos.y,posheight),200, 100, -0.02, 0.005, 60, 6, 6, color(255,255,255),0,270,360));
            updatehp();
          }
        }
        if(atrot <= 0){
          a = false;
          atrot = 0;
          back = false;
        }
      }else{
        float x = ppos.x-pos.x;
        float y = ppos.y-pos.y;
        float t = sqrt(x*x) + sqrt(y*y);
        pos.x+=6*(x/t)*sm;
        pos.y+=6*(y/t)*sm;
        lastm.x = 8*(x/t)*sm;
        lastm.y = 8*(y/t)*sm;
      }
      rot = -bearing(0,0,lastm.x,lastm.y)-HALF_PI;
    }else{
      rot = bearing(pos.x,1500,pos.x,pos.y)-HALF_PI;
    }
    d3.translate(pos.x,pos.y,pos.z);
    d3.rotateX(-HALF_PI);
    d3.rotateY(rot);
    d3.rotateX(-radians(atrot));
    d3.shape(shadspn);
    d3.rotateX(radians(atrot));
    d3.rotateY(-rot);
    d3.rotateX(HALF_PI);
    d3.translate(-pos.x,-pos.y,-pos.z);
  }
}
void bossattacks(){
  if(stage == 2){
    if(bosspos.z > 0){
      bosspos.z = 0;
      particles.add(new particlesystem(new PVector(bosspos.x,bosspos.y,bosspos.z-100),100, 50, -0.02, 0.005, 60, 5, 5, color(255,255,255),0,0,360));
      stuck = true;
      slam.stop();
      slam.play();
      bossvel = 0;
      if(dist(ppos.x,ppos.y,bosspos.x,bosspos.y) < 625){
        if(inarmour.empty == false){
          health-=constrain((1200-dist(ppos.x,ppos.y,bosspos.x,bosspos.y))*0.5*(1.0/inarmour.stat),0,200);
          updatehp();
        }else{
          health-=constrain(1200-dist(ppos.x,ppos.y,bosspos.x,bosspos.y)*0.5,0,200);
          updatehp();
        }
      }
    }
    if(bosspos.z < 0){
      bosspos.z+=bossvel;
      bossvel+=3*((1-(bosshp/10000.0))*4);
    }else if(bcd < 0){
      bcdreset = random(3,6);
      bcd = bcdreset;
      stuck = false;
      particles.add(new particlesystem(new PVector(bosspos.x,bosspos.y,bosspos.z-100),50, 50, 0, 0.05, 120, 20, 20, color(79,9,104),0,0,360));
      bosspos.z = -5000;
      bosspos.x = ppos.x;
      bosspos.y = ppos.y;
    }else if(stuck){
      bcd-=0.01666;
    }
  }else if(stage == 1){
    if(bcd < 0){
      float x = 240 * (cos(bearing(bosspos.x,bosspos.y,ppos.x,ppos.y)-HALF_PI-radians(50)));
      float y = 240 * (sin(bearing(bosspos.x,bosspos.y,ppos.x,ppos.y)-HALF_PI-radians(50)));
      bossproj.add(new proj(new PVector(bosspos.x-x,bosspos.y-y,bosspos.z-160),ppos));
      necroattack.stop();
      necroattack.play();
      bcdreset = random(0.15,1);
      bcd = bcdreset;
    }else{
      bcd-=0.01666;
    }
  }
  d3.translate(-ppos.x,-ppos.y,ppos.z);
  for(int i = bossproj.size()-1; i > -1; i--){
    bossproj.get(i).display();
    if(dist(bossproj.get(i).pos.x,bossproj.get(i).pos.y,ppos.x,ppos.y) < 150){
      bossproj.remove(i);
      if(inarmour.empty){
        health-=80;
      }else{
        health-=80*(1.0-inarmour.stat/100.0);
      }
      painplayer.stop();
      painplayer.play();
      particles.add(new particlesystem(new PVector(ppos.x,ppos.y,posheight),200, 100, -0.02, 0.005, 60, 6, 6, color(255,255,255),0,270,360));
      updatehp();
    }else if(bossproj.get(i).pos.x > 16000 || bossproj.get(i).pos.x < -1000 || bossproj.get(i).pos.y > 4000 || bossproj.get(i).pos.y < -1000){
      bossproj.remove(i);
    }
  }
  d3.translate(ppos.x,ppos.y,-ppos.z);
}
