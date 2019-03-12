ArrayList<mob> mobs = new ArrayList<mob>();
ArrayList<boss> bosses = new ArrayList<boss>();
ArrayList<people> peoples = new ArrayList<people>();
PVector portalpos = new PVector(0,0,-10000);
int km = 0;
int kb = 0;
int fcd = 0;
class mob{
  float hp = 100+daycount;
  PVector spawn;
  PVector pos = new PVector(0,0,0);
  PVector posf = new PVector(0,0);
  PVector lastm = new PVector(0,0);
  boolean persuit = false;
  boolean attack = false;
  float attackr = 0;
  float attackz = 0;
  float cd = 0;
  float cdreset = 1-1/(daycount+1)*0.1;
  float increment = 180/lefthand.cdreset/60;
  float damage = random(5+daycount*2,10+daycount*2);
  float rot;
  mob(){
    float d = random(chunkdist*0.1,chunkdist*0.3);
    float deg = radians(random(360));
    float x = d * cos(deg);
    float y = d * sin(deg);
    spawn = new PVector(ppos.x+x,ppos.y+y);
    pos.x = spawn.x;
    pos.y = spawn.y;
    pos.z = noise((pos.x/dens)/float(divider),(pos.y/dens)/divider)*float(multi);
    posf.x = spawn.x+=random(-wi*dens/2,wi*dens/2);
    posf.y = spawn.y+=random(-wi*dens/2,wi*dens/2);
    particles.add(new particlesystem(new PVector(pos.x,pos.y,pos.z-100),20, 50, 0, 0.05, 120, 20, 20, color(79,9,104),0,0,360));
  }
  //possibly thread
  void walk(){
    pos.z = lerp(pos.z,noise((pos.x/dens)/float(divider),(pos.y/dens)/divider)*float(multi),0.2);
    if(dist(pos.x,pos.y,posf.x,posf.y) < 10){
      posf.x = spawn.x+random(-1500,1500);
      posf.y = spawn.y+random(-1500,1500);
    }else if(persuit && attack == false){
      float x = ppos.x-pos.x;
      float y = ppos.y-pos.y;
      float t = sqrt(x*x) + sqrt(y*y);
      pos.x+=34*(x/t);
      pos.y+=34*(y/t);
      lastm.x = 34*(x/t);
      lastm.y = 34*(y/t);
    }else if(persuit == false){
      float x = posf.x-pos.x;
      float y = posf.y-pos.y;
      float t = sqrt(x*x) + sqrt(y*y);
      pos.x+=4*(x/t);
      pos.y+=4*(y/t);      
      lastm.x = 4*(x/t);
      lastm.y = 4*(y/t);
    }   
    if(dist(ppos.x,ppos.y,spawn.x,spawn.y) < 1500 && health > 0 && persuit == false && menu == false){
      persuit = true;
    }
    if(dist(ppos.x,ppos.y,spawn.x,spawn.y) > 4000 && (time < 180 || time > 330)){
      persuit = false;
    }
    if(dist(ppos.x,ppos.y,pos.x,pos.y) < 250 && attack == false && cd == 0 && menu == false){
      attack = true;
      cd = cdreset;
      attackz = 1;
    }
    if(attack){
      float speed = 180/cdreset/60;
      if(attackr != 0 || attackz == 1){
        if(attackr < 90 && attackz == 1){
          attackr+=speed;
        }else if(attackr >= 90 && attackz == 1){
          attackz = 0;
          if(dist(ppos.x,ppos.y,pos.x,pos.y) < 450){
            if(inarmour.empty == false){
              health-=damage*(1.0-inarmour.stat/100.0);
            }else{
              health-=damage;
            }
            painplayer.stop();
            painplayer.play();
            particles.add(new particlesystem(new PVector(ppos.x,ppos.y,posheight),200, 100, -0.02, 0.005, 60, 6, 6, color(255,255,255),0,270,360));
            updatehp();
            if(health <= 0){
              persuit = false;
              attackz = 0;
              attackr = 0;
            }
          }
        }else if(attackz == 0){
          attackr-=speed;
        }
        if(attackr <= 0){
          attackr = 0;
          attack = false;
          cd = 0;
        }
      } //180/lefthand.cdreset/60;
      cd-=0.016;
      if(cd < 0){
        cd = 0;
        attack = false;
      }
    }
    //for(int i = 0; i < grounds.size(); i++){
    //    for(int j = 0; j < treecount; j++){
    //      try{
    //        float x = grounds.get(i).pos.x+grounds.get(i).tposs[j].x;
    //        float y = grounds.get(i).pos.y+grounds.get(i).tposs[j].y;
    //        if(dist(pos.x,pos.y,x,y) < treehit && grounds.get(i).tposs[j].z == 1){
    //          float t = sqrt(lastm.x*lastm.x)+sqrt(lastm.y*lastm.y);
    //          pos.x+=lastm.x+(lastm.x/t)*0.75;
    //          pos.y+=lastm.y+(lastm.y/t)*0.75;
    //        }
    //      }catch(Exception e){
    //      }
    //  }
    //}
    rot = bearing(0,0,lastm.x,lastm.y);
  }
  void display(){
    d3.emissive(255,255,255);
    d3.ambient(255,255,255);
    d3.translate(pos.x,pos.y,pos.z-120);
    d3.rotateZ(rot);
    d3.rotateX(radians(-90));
    d3.rotateY(radians(-90));
    d3.fill(255,0,0);
    d3.ambient(80);
    d3.shape(shade);
    d3.noFill();
    d3.rotateX(-radians(attackr));
    d3.translate(95,150,-80);
    d3.shape(clawR);
    d3.translate(-95,-150,80);
    d3.translate(-80,150,-80);
    d3.shape(clawL);
    d3.translate(80,-150,80);
    d3.rotateX(radians(attackr));
    d3.rotateY(radians(90));
    d3.rotateX(radians(90));
    d3.rotateZ(-rot);
    d3.translate(-pos.x,-pos.y,-pos.z+120);
    d3.emissive(0,0,0);
  }
}
class boss{
  ArrayList<proj> projs = new ArrayList<proj>();
  float hp = 500+daycount;
  PVector pos = new PVector(0,0,0);
  boolean attack = false;
  int faster = 0;
  float attackr = 0;
  float attackz = 0;
  float cd = 0;
  float cdreset = 1.5;//0.5-1/(daycount+1)*0.1;
  float increment = 180/lefthand.cdreset/60;
  float damage = random(25+daycount*2,50+daycount*2);
  float rot;
  PVector[] boxes;
  boss(){
    float d = random(chunkdist*0.3,chunkdist*0.5);
    float deg = radians(random(360));
    pos = new PVector(ppos.x+d * cos(deg),ppos.y+d * sin(deg),0);
    pos.z = noise((pos.x/dens)/float(divider),(pos.y/dens)/divider)*float(multi)-70;
    boxes = new PVector[int(random(8,15))];
    for(int i = 0; i < boxes.length; i++){
      int r = round(random(359));
      float x = 1200 * cos(r);
      float y = 1200 * sin(r);
      float z = noise(((x+pos.x)/dens)/float(divider),((y+pos.y)/dens)/divider)*float(multi)-450;
      boxes[i] = new PVector(x,y,z);
    }
    try{
      checktrees();
    }catch(Exception e){
    }
  }
  void checktrees(){
    try{
      for(int i = 0; i < grounds.size(); i++){ 
        for(int j = 0; j < treecount; j++){
          if(dist(pos.x,pos.y,grounds.get(i).tposs[j].x+grounds.get(i).pos.x,grounds.get(i).tposs[j].y+grounds.get(i).pos.y) < 4000 && grounds.get(i).tposs[j].z == 1){
            grounds.get(i).object.removeChild(grounds.get(i).object.getChildIndex(grounds.get(i).trees[j]));
            grounds.get(i).tposs[j].z = 0;
          }
        }
      }   
    }catch(Exception e){
    }
  }
  void run(){
    if(random(1) > 0.995 && faster == 0){
      faster = int(random(60,240));
    }
    if(faster > 0){
      faster--;
      cdreset=0.25;
    }else{
      cdreset=1.5;
    }
    if(dist(ppos.x,ppos.y,pos.x,pos.y) < 2500 && attack == false && cd == 0 && menu == false){
      attack = true;
      cd = cdreset;
      attackz = 1;
      attackr = 1;
    }
    if(attack){
      float speed = 180/cdreset/60;
      if(attackr != 0 || attackz == 1){
        if(attackr < 90 && attackz == 1){
          attackr+=speed;
        }else if(attackr >= 90 && attackz== 1){
          PVector end = new PVector(ppos.x,ppos.y,noise((ppos.x/dens)/float(divider),(ppos.y/dens)/divider)*float(multi));
          float x = 130 * cos(bearing(pos.x,pos.y,ppos.x,ppos.y)-HALF_PI);
          float y = 130 * sin(bearing(pos.x,pos.y,ppos.x,ppos.y)-HALF_PI);
          projs.add(new proj(new PVector(pos.x+x,pos.y+y,pos.z+65),end));
          necroattack.stop();
          necroattack.play();
          attackz = 0;
          }
      }else if(attackz == 0){
        attackr-=speed;
      }
      if(attackr < 0){
        attackr = 0;
        attack = false;
      }
    }
    cd-=0.016;
    if(cd < 0){
      cd = 0;
      attack = false;
    }
  float x = ppos.x-pos.x;
  float y = ppos.y-pos.y;
  float t = sqrt(x*x) + sqrt(y*y);
  rot = bearing(0,0,(x/t),(y/t));
  }
  void display(){
    for(int i = projs.size()-1; i > -1; i--){
      float x = projs.get(i).pos.x;
      float y = projs.get(i).pos.y;
      if(dist(x,y,ppos.x,ppos.y) < 100 && health > 0){
        health-=damage*(1-inarmour.stat/100);
        painplayer.stop();
        painplayer.play();
        particles.add(new particlesystem(new PVector(ppos.x,ppos.y,posheight),200, 100, -0.02, 0.005, 60, 6, 6, color(255,255,255),0,270,360));
        updatehp();
        projs.remove(i);
      }else if(dist(x,y,pos.x,pos.y) > 2500){
        projs.remove(i);
      }else{
        projs.get(i).display();
      }
    }
    for(int i = 0; i < boxes.length; i++){
      for(int j = projs.size()-1; j > -1; j--){
        float x = projs.get(j).pos.x;
        float y = projs.get(j).pos.y;
        if(dist(x,y,pos.x+boxes[i].x,pos.y+boxes[i].y) < 150){
          projs.remove(j);
        }
      }
      d3.translate(pos.x+boxes[i].x,pos.y+boxes[i].y,boxes[i].z);
      d3.fill(120);
      d3.ambient(50,50,50);
      d3.box(150,150,1000);
      d3.noFill();
      d3.translate(-pos.x-boxes[i].x,-pos.y-boxes[i].y,-boxes[i].z);
    }
    d3.translate(pos.x,pos.y,pos.z-50);
    d3.rotateZ(rot);
    d3.rotateX(radians(-90));
    d3.rotateY(radians(-90));
    d3.fill(255,0,0);
    d3.ambient(255);
    d3.shape(mboss);
    d3.noFill();
    d3.rotateY(radians(90));
    d3.rotateX(radians(90));
    d3.rotateZ(-rot); 
    d3.translate(-pos.x,-pos.y,-pos.z+50);
  } 
}
class people{
  PVector pos = new PVector(0,0,0);
  PVector posf = new PVector(0,0,0);
  int m = 1;
  float rot = 0;
  people(PVector pos_, float rot_){
    pos = new PVector(pos_.x,pos_.y,pos_.z);
    posf = new PVector(pos.x,pos.y,pos.z);
    rot = rot_;
    m = round(random(0,2));
  }
  void display(){
    if(dist(pos.x,pos.y,posf.x,posf.y) < 10){
      posf.x = pos.x+random(-1000,1000);
      posf.y = pos.y+random(-1000,1000);
    }
    float x = posf.x-pos.x;
    float y = posf.y-pos.y;
    float t = sqrt(x*x) + sqrt(y*y);
    pos.x+=3*x/t;
    pos.y+=3*y/t;
    pos.z = lerp(pos.z,noise((pos.x/dens)/float(divider),(pos.y/dens)/divider)*float(multi),0.1);
    rot = bearing(posf.x,posf.y,pos.x,pos.y); 
    d3.translate(pos.x,pos.y,pos.z-110);
    d3.rotateZ(rot);
    d3.rotateY(HALF_PI);
    d3.rotateZ(-HALF_PI);
    d3.shape(peoplemodels[m]);
    d3.rotateZ(HALF_PI);
    d3.rotateY(-HALF_PI);
    d3.rotateZ(-rot);
    d3.translate(-pos.x,-pos.y,-pos.z+110);
  }
}
class proj{
  PVector pos = new PVector(0,0,0);
  PVector move;
  float rot;
  proj(PVector start, PVector end){
    pos.x = start.x;
    pos.y = start.y;
    pos.z = start.z;
    float x = start.x-end.x;
    float y = start.y-end.y;
    float z = start.z-end.z;
    float t = sqrt(x*x) + sqrt(y*y) + sqrt(z*z);
    move = new PVector(32*(x/t),32*(y/t),32*(z/t));
    rot = bearing(0,0,1*(x/t),1*(y/t));
  }
  void display(){
    pos.x-=move.x;
    pos.y-=move.y;
    pos.z-=move.z;
    d3.translate(pos.x,pos.y,pos.z-180);
    d3.rotateZ(rot);
    d3.fill(255,0,0);
    d3.rotateY(radians(90));
    d3.rotateZ(radians(millis()/1000.0*360));
    d3.shape(mproj);
    d3.rotateZ(-radians(millis()/1000.0*360));
    d3.noFill();
    d3.rotateY(radians(-90));
    d3.rotateZ(-rot);
    d3.translate(-pos.x,-pos.y,-pos.z+180);
  }
}
void managemobs(){
  float recdist = chunkdist;
  for(int i = mobs.size()-1; i > -1; i--){
    if(dist(mobs.get(i).pos.x,mobs.get(i).pos.y,ppos.x,ppos.y) < recdist){
      recdist = dist(mobs.get(i).pos.x,mobs.get(i).pos.y,ppos.x,ppos.y);
    } 
    if(dist(mobs.get(i).pos.x,mobs.get(i).pos.y,ppos.x,ppos.y) > chunkdist*0.6){
      mobs.remove(i);
      continue;
    }else if(mobs.get(i).hp <= 0){
      if(random(1) >= 0.5){
        mobdrops.add(new drops(mobs.get(i).pos,false));
      }
      if(bossdead){
        peoples.add(new people(mobs.get(i).pos,mobs.get(i).rot));
        particles.add(new particlesystem(new PVector(mobs.get(i).pos.x,mobs.get(i).pos.y,mobs.get(i).pos.z-50),50, 50, -0.02, 0.005, 400, 10, 10, color(255,255,255),0,0,360));
        km++;
        updatekillcount();
      }else{
        particles.add(new particlesystem(new PVector(mobs.get(i).pos.x,mobs.get(i).pos.y,mobs.get(i).pos.z-50),50, 50, -0.02, 0.005, 400, 10, 10, color(10,10,10),0,0,360));
      }
      mobs.remove(i);
      points+=100;
      deathmob.stop();
      deathmob.play();
      continue;
    }
  }
  whispering.amp(constrain(0.5-recdist/3000,0,0.5));
  int mobc = 10;
  int bossc = 0;
  mobc+=daycount*2;
  mobc = constrain(mobc,0,50);
  bossc+=daycount/2;
  bossc = constrain(bossc,0,8);
  if(time > 180){
    mobc*=1.5;
  }
  if(bossdead){
    mobc = 20-km;
    bossc = 5-kb;
  }
  if(mobs.size() < mobc){
    mobs.add(new mob());
  }
  for(int i = 0; i < mobs.size(); i++){
    mobs.get(i).walk();
    mobs.get(i).display();
  }
  if(bosses.size() < bossc){
    bosses.add(new boss());
  }
  for(int i = bosses.size()-1; i > -1; i--){ 
    float x = bosses.get(i).pos.x;
    float y = bosses.get(i).pos.y;
    if(bosses.get(i).hp <= 0){
      deathmob.stop();
      deathmob.play();
      if(bossdead == false){
        mobdrops.add(new drops(new PVector(bosses.get(i).pos.x,bosses.get(i).pos.y,bosses.get(i).pos.z+70),true));
      }
      points+=500;
      if(bossdead){
        peoples.add(new people(bosses.get(i).pos,bosses.get(i).rot));
        particles.add(new particlesystem(new PVector(bosses.get(i).pos.x,bosses.get(i).pos.y,bosses.get(i).pos.z-50),50, 50, -0.02, 0.005, 400, 10, 10, color(255,255,255),0,0,360));
        kb++;
        updatekillcount();
      }else{
        particles.add(new particlesystem(new PVector(bosses.get(i).pos.x,bosses.get(i).pos.y,bosses.get(i).pos.z-50),50, 50, -0.02, 0.005, 400, 10, 10, color(10,10,10),0,0,360));
      }
      if(daycount > 4 && random(1) >= 0.0){
        portalpos = bosses.get(i).pos;
      }
      bosses.remove(i);
    }else if(dist(x,y,ppos.x,ppos.y) > chunkdist*0.6){
      bosses.remove(i);
    }else{
      bosses.get(i).run();
      bosses.get(i).display();
    }
  }
  for(int i = 0; i < peoples.size(); i++){
    println(i);
    peoples.get(i).display();
  }
}
float bearing(float a1, float a2, float b1, float b2) {
    float theta = atan2(b1 - a1, a2 - b2);
    if (theta < 0.0)
        theta += TWO_PI;
    return theta-PI/2;
}
