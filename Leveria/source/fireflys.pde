ArrayList<firefly> fireflies = new ArrayList<firefly>();
float falpha = 255;
boolean dir = true;
PShape firefliesmodel;
PGraphics f1;
PGraphics f2;
PGraphics f3;
class firefly{
  int frame = 1;
  int intval = 0;
  PVector pos = new PVector(ppos.x+random(-chunkdist/4,chunkdist/4),ppos.y+random(-chunkdist/4,chunkdist/4),0);
  PVector vel = new PVector(random(2,5),random(360),random(10,500));
  PVector velf = new PVector(random(2,5),random(360),random(10,500));
  PShape model;
  firefly(){
    pos.z = noise((pos.x/dens)/float(divider),(pos.y/dens)/divider)*float(multi);
    model = createShape();
    model.beginShape(QUAD);
    model.ambient(255,255,255);
    model.emissive(255,255,255);
    model.noStroke();
    model.texture(f1);
    model.normal(0, 0, 1);
    model.vertex(-20, -20, 0, 0);
    model.vertex(+20, -20, 10, 0);
    model.vertex(+20, +20, 10, 10);
    model.vertex(-20, +20, 0, 10);
    model.endShape(); 
    model.translate(pos.x,pos.y,pos.z-vel.z);
    model.rotateX(radians(90));
    firefliesmodel.addChild(model);
  }
  void move(){
    animation();
    vel.x = lerp(vel.x,velf.x,0.01);
    vel.y = lerp(vel.y,velf.y,0.01);
    vel.z = lerp(vel.z,velf.z,0.01);
    float x = vel.x * cos(radians(vel.y));
    float y = vel.x * sin(radians(vel.y));
    pos.x+=x;
    pos.y+=y;
    pos.z=lerp(pos.z,noise((pos.x/dens)/float(divider),(pos.y/dens)/divider)*float(multi),0.1);    
    float x1 = -chunkdist*0.3 * cos(radians(mouse.x));
    float y1 = -chunkdist*0.3 * sin(radians(mouse.x));
    float x2 = pos.x-ppos.x;
    float y2 = pos.y-ppos.y;
    if(dist(x1,y1,x2,y2) > chunkdist*0.3){
      float rnd = random(chunkdist*0.6);
      float x3;
      float y3;
      if(right == false){
        float val = mouse.x+260;
        if(val > 360){
          val = val-360;
        }
        x3 = rnd * cos(radians(val));
        y3 = rnd * sin(radians(val));
      }else{
        float val = mouse.x-260;
        if(val < 0){
          val = 360+val;
        }
        x3 = rnd * cos(radians(val));
        y3 = rnd * sin(radians(val));
      }
      pos = new PVector(ppos.x+x3,ppos.y+y3,0);
      pos.z = noise((pos.x/dens)/float(divider),(pos.y/dens)/divider)*float(multi);
      vel = new PVector(random(2,5),random(360),random(10,500));
      velf = new PVector(random(2,5),random(360),random(10,500));
    }
    if(dist(pos.x,pos.y,ppos.x,ppos.y) > chunkdist*0.3){
      float deg = mouse.x + random(-50,50) + 180;
      float dis = random(100,5000);
      x = dis * cos(radians(deg));
      y = dis * sin(radians(deg));
      pos = new PVector(ppos.x+x,ppos.y+y,0);
      pos.z = noise((pos.x/dens)/float(divider),(pos.y/dens)/divider)*float(multi);
      vel = new PVector(random(2,5),random(360),random(10,500));
      velf = new PVector(random(2,5),random(360),random(10,500));
    }
    if(dist(vel.x,vel.y,vel.z,velf.x,velf.y,velf.z) < 1){
      velf = new PVector(random(2,5),random(360),random(10,500));
    }
    model.resetMatrix();
    model.rotateX(radians(90));
    model.rotateZ(bearing(ppos.x,ppos.y,pos.x,pos.y)+HALF_PI);
    model.translate(pos.x,pos.y,pos.z-vel.z);
  }
  void animation(){
    if(frame == 1 && dir && intval > 2){
      frame = 2;
      model.setTexture(f2);
      intval = 0;
    }else if(frame == 2 && dir && intval > 2){  
      frame = 3;
      model.setTexture(f3);
      dir = false;
      intval = 0;
    }else if(frame == 3 && intval > 2){ 
      frame = 2;
      model.setTexture(f2);
      intval = 0;
    }else if(frame == 2 && dir == false && intval > 2){
      frame = 1;
      model.setTexture(f1);
      dir = true;
      intval = 0;
    }
    intval++;
  }
}
void managefireflies(){
  if(time > 180 && time < 330){
    falpha = 255;
  }else if(time >= 330){
    falpha = 255-(time-330)/30*255;
  }else if(time > 150 && time <= 180){
    falpha = (time-150)/30*255;
  }
  if(fireflies.size() < 60){
    fireflies.add(new firefly());
  }
 
  d3.shape(firefliesmodel);
  tints.set("alpha",falpha/255);
  f1.beginDraw();
  f1.image(fireimg1,0,0,10,10);
  f1.filter(tints);
  f1.endDraw();
  f1.filter(tints);
  f2.beginDraw();
  f2.image(fireimg2,0,0,10,10);
  f2.endDraw();
  f2.filter(tints);
  f3.beginDraw();
  f3.image(fireimg3,0,0,10,10);
  f3.endDraw();
  f3.filter(tints);
}
