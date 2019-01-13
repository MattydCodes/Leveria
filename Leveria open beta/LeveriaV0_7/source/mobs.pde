ArrayList<mob> mobs = new ArrayList<mob>();
ArrayList<boss> bosses = new ArrayList<boss>();
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
    spawn = new PVector(random(ppos.x-chunkdist,ppos.x+chunkdist),random(ppos.y-chunkdist,ppos.y+chunkdist));
    pos.x = spawn.x;
    pos.y = spawn.y;
    pos.z = noise((pos.x/dens)/float(divider),(pos.y/dens)/divider)*float(multi);
    posf.x = spawn.x+=random(-1000,1000);
    posf.y = spawn.y+=random(-1000,1000);
  }
  //possibly thread
  void walk(){
    pos.z = lerp(pos.z,noise((pos.x/dens)/float(divider),(pos.y/dens)/divider)*float(multi),0.1);
    if(dist(pos.x,pos.y,posf.x,posf.y) < 10){
      posf.x = spawn.x+random(-1000,1000);
      posf.y = spawn.y+random(-1000,1000);
    }else if(persuit && attack == false){
      float x = ppos.x-pos.x;
      float y = ppos.y-pos.y;
      float t = sqrt(x*x) + sqrt(y*y);
      pos.x+=14*(x/t);
      pos.y+=14*(y/t);
      lastm.x = 14*(x/t);
      lastm.y = 14*(y/t);
    }else if(persuit == false){
      float x = posf.x-pos.x;
      float y = posf.y-pos.y;
      float t = sqrt(x*x) + sqrt(y*y);
      pos.x+=4*(x/t);
      pos.y+=4*(y/t);      
      lastm.x = 4*(x/t);
      lastm.y = 4*(y/t);
    }   
    if(dist(ppos.x,ppos.y,spawn.x,spawn.y) < 1000 && health > 0 && persuit == false){
      persuit = true;
      pursuit.play();
    }
    if(dist(ppos.x,ppos.y,spawn.x,spawn.y) > 4000 && (time < 180 || time > 330)){
      persuit = false;
    }
    if(dist(ppos.x,ppos.y,pos.x,pos.y) < 250 && attack == false && cd == 0){
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
          if(dist(ppos.x,ppos.y,pos.x,pos.y) < 350){
            if(inarmour.empty == false){
              health-=damage*(1-inarmour.stat/100);
            }else{
              health-=damage;
            }
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
    for(int i = 0; i < grounds.size(); i++){
      try{
      float x = grounds.get(i).pos.x+grounds.get(i).tpos.x;
      float y = grounds.get(i).pos.y+grounds.get(i).tpos.y;
      if(dist(pos.x,pos.y,x,y) < treehit){
        float t = sqrt(lastm.x*lastm.x)+sqrt(lastm.y*lastm.y);
        pos.x-=lastm.x+(lastm.x/t)*0.75;
        pos.y-=lastm.y+(lastm.y/t)*0.75;
      }
      }catch(Exception e){
      }
    }
    rot = atan(lastm.y/lastm.x);
  }
  void display(){
    d3.translate(pos.x,pos.y,pos.z-50);
    d3.rotateZ(rot);
    d3.rotateX(radians(-90));
    d3.rotateY(radians(-90));
    d3.rotateY(radians(attackr));
    d3.fill(255,0,0);
    d3.ambient(80);
    d3.shape(shade);
    d3.noFill();
    d3.rotateY(-radians(attackr));
    d3.rotateY(radians(90));
    d3.rotateX(radians(90));
    d3.rotateZ(-rot);
    d3.translate(-pos.x,-pos.y,-pos.z+50);
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
    pos = new PVector(random(ppos.x-chunkdist/2,ppos.x+chunkdist/2),random(ppos.y-chunkdist/2,ppos.y+chunkdist/2),0);
    boxes = new PVector[int(random(8,15))];
    for(int i = 0; i < boxes.length; i++){
      int r = round(random(359));
      float x = 800 * cos(r);
      float y = 800 * sin(r);
      float z = noise(((x+pos.x)/dens)/float(divider),((y+pos.y)/dens)/divider)*float(multi)-500;
      boxes[i] = new PVector(x,y,z);
    }
    try{
      for(int i = 0; i < grounds.size(); i++){ 
        if(dist(pos.x,pos.y,grounds.get(i).tpos.x+grounds.get(i).pos.x,grounds.get(i).tpos.y+grounds.get(i).pos.y) < 1500){
          grounds.get(i).object.removeChild(grounds.get(i).object.getChildIndex(grounds.get(i).branch));
          grounds.get(i).object.removeChild(grounds.get(i).object.getChildIndex(grounds.get(i).row));
          grounds.get(i).tpos.z = 0;
        }
      } 
    }catch(Exception e){
      println("oopsies");
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
    pos.z = noise((pos.x/dens)/float(divider),(pos.y/dens)/divider)*float(multi);
    if(dist(ppos.x,ppos.y,pos.x,pos.y) < 1000 && health > 0){
      if(pursuit.isPlaying() == false){
        pursuit.play();
      }
    }
    if(dist(ppos.x,ppos.y,pos.x,pos.y) < 1300 && attack == false && cd == 0){
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
          projs.add(new proj(pos,end));
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
  rot = atan((1*(y/t))/(1*(x/t)));
  }
  void display(){
    for(int i = projs.size()-1; i > -1; i--){
      float x = projs.get(i).pos.x;
      float y = projs.get(i).pos.y;
      if(dist(x,y,ppos.x,ppos.y) < 100 && health > 0){
        health-=damage*(1-inarmour.stat/100);
        updatehp();
        projs.remove(i);
      }else if(dist(x,y,pos.x,pos.y) > 2000){
        projs.remove(i);
      }else{
        projs.get(i).display();
      }
    }
    for(int i = 0; i < boxes.length; i++){
      for(int j = projs.size()-1; j > -1; j--){
        float x = projs.get(j).pos.x;
        float y = projs.get(j).pos.y;
        if(dist(x,y,pos.x+boxes[i].x,pos.y+boxes[i].y) < 212){
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
    if(rot < 0){
      d3.rotateZ(rot);
    }else{
      d3.rotateZ(-rot);
    }
    d3.rotateX(radians(-90));
    d3.rotateY(radians(-90));
    d3.fill(255,0,0);
    d3.ambient(255);
    d3.shape(mboss);
    d3.noFill();
    d3.rotateY(radians(90));
    d3.rotateX(radians(90));
    if(rot < 0){
      d3.rotateZ(-rot);
    }else{
      d3.rotateZ(rot);
    }
    d3.translate(-pos.x,-pos.y,-pos.z+50);
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
    move = new PVector(16*(x/t),16*(y/t),16*(z/t));
    rot = atan((1*(y/t))/(1*(x/t)));
  }
  void display(){
    pos.x-=move.x;
    pos.y-=move.y;
    pos.z-=move.z;
    d3.translate(pos.x,pos.y,pos.z-180);
    d3.rotateZ(rot);
    d3.fill(255,0,0);
    d3.rotateY(radians(90));
    d3.shape(mproj);
    d3.noFill();
    d3.rotateY(radians(-90));
    d3.rotateZ(-rot);
    d3.translate(-pos.x,-pos.y,-pos.z+180);
  }
}
void managemobs(){
  for(int i = mobs.size()-1; i > -1; i--){
    if(dist(mobs.get(i).pos.x,mobs.get(i).pos.y,ppos.x,ppos.y) > chunkdist){
      mobs.remove(i);
      continue;
    }else if(mobs.get(i).hp <= 0){
      if(random(1) >= 0.5){
        mobdrops.add(new drops(mobs.get(i).pos,false));
      }
      mobs.remove(i);
      points+=50;
      deathmob.stop();
      deathmob.play();
      continue;
    }
  }
  int mobc = 20;
  mobc+=daycount*5;
  if(time > 180){
    mobc*=1.5;
  }
  if(mobs.size() < mobc){
    mobs.add(new mob());
  }
  for(int i = 0; i < mobs.size(); i++){
    mobs.get(i).walk();
    mobs.get(i).display();
  } 
  if(bosses.size() < 2){
    bosses.add(new boss());
  }
  for(int i = bosses.size()-1; i > -1; i--){
    float x = bosses.get(i).pos.x;
    float y = bosses.get(i).pos.y;
    if(bosses.get(i).hp <= 0){
      deathmob.stop();
      deathmob.play();
      mobdrops.add(new drops(bosses.get(i).pos,true));
      points+=100;
    }
    if(dist(x,y,ppos.x,ppos.y) > chunkdist || bosses.get(i).hp <= 0){
      bosses.remove(i);
    }else{
      bosses.get(i).run();
      bosses.get(i).display();
    }
  }
}