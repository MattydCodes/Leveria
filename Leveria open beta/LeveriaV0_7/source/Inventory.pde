ArrayList<drops> mobdrops = new ArrayList<drops>();
class item{
  PImage img;
  float stat = 0;
  boolean weapon = false;
  boolean empty;
  float cd = 0;
  float cdreset = 0;
  PShape model;
  item(PImage img_, float stat_){
    img = img_;
    stat = stat_;
    empty = false;
  }
  item(PImage img_, float stat_, float cdreset_, PShape model_){
    img = img_;
    model = model_;
    cdreset = cdreset_;
    stat = stat_;
    weapon = true;
    empty = false;
  }
  item(){
    empty = true;
  }
}
item[][] inventory = new item[5][3];
item inhand = new item();
item lefthand = new item();
item righthand = new item();
item inarmour = new item();
item inforge = new item();
int flowerc = 0;

class drops{
  item drop;
  PVector pos;
  boolean picked = false;
  drops(PVector pos_, boolean bossdrop){
    pos = pos_;
    pos.z-=10;
    if(bossdrop){
      if(random(1) >= 0.333){
        float dmg = round(random(20+constrain(daycount,0,20)*2,40+constrain(daycount,0,20)*2));
        drop = new item(swordc,dmg,round(100*(dmg/(40+constrain(daycount,0,20)*2)/2))/float(100),mswordc);
      }else{
        drop = new item(shieldc,round(constrain(random(40+constrain(daycount,0,39),60+constrain(daycount,0,39)),40,99)));
      }
    }else{
      if(random(1) >= 0.25){
        if(random(1) >= 0.5){
          float dmg = random(5+daycount,10+daycount);
          drop = new item(sworda,round(dmg),round(100*(dmg/(10+constrain(daycount,0,10))))/float(100),msworda);
        }else{
          float dmg = random(10+daycount,15+daycount);
          drop = new item(swordb,round(dmg),round(100*(dmg/(15+constrain(daycount,0,15))))/float(100),mswordb);
        }
      }else{
        if(random(1) >= 0.5){
          drop = new item(shielda,round(constrain(random(10+daycount,20+daycount),10,50)));
        }else{
          drop = new item(shieldb,round(constrain(random(20+daycount,40+daycount),20,75)));
        }
      }
    }
  }
  void display(boolean interact){
    if(dist(ppos.x,ppos.y,pos.x,pos.y) < flowerhit && interact){
      int x = 0; 
      int y = 0;
      boolean found = true;
      for(y = 0; y < 3; y++){
        for(x = 0; x < 5; x++){
          if(inventory[x][y].empty && found){
            found = false;
            break;
          }
        }
        if(found == false){
          break;
        }
      }
      if(found == false){
        inventory[x][y] = drop;
        picked = true;
      }
    }
    d3.translate(pos.x,pos.y,pos.z);
    d3.shape(mdrop);
    d3.translate(-pos.x,-pos.y,-pos.z);
  }
}

void cooldowns(){
  if(lefthand.cd > 0){
    lefthand.cd-=0.016;
    if(lefthand.cd < 0){
      lefthand.cd = 0;
    }
  }
  if(righthand.cd > 0){
    righthand.cd-=0.016;
    if(righthand.cd < 0){
      righthand.cd = 0;
    }
  }
}

void attackanimation(){
  float speedl = 180/lefthand.cdreset/60;
  float speedr = 180/righthand.cdreset/60;
  if(righthandr.x != 0 || righthandr.z == 1){
    if(righthandr.x < 90 && righthandr.z == 1){
      righthandr.x+=speedr;
    }else if(righthandr.x >= 90 && righthandr.z == 1){
      righthandr.z = 0;
      righthandr.x = 90;
      for(int i = 0; i < mobs.size(); i++){
        float x1 = mobs.get(i).pos.x;
        float y1 = mobs.get(i).pos.y;
        float r = 140;
        float x = r * cos(radians(mouse.x));
        float y = r * sin(radians(mouse.x));
        if(dist(ppos.x-x,ppos.y-y,x1,y1) < 200){
          mobs.get(i).hp-=righthand.stat;
          pain.stop();
          pain.play();
        }
      }
      for(int i = 0; i < bosses.size(); i++){
        float x1 = bosses.get(i).pos.x;
        float y1 = bosses.get(i).pos.y;
        float r = 140;
        float x = r * cos(radians(mouse.x));
        float y = r * sin(radians(mouse.x));
        if(dist(ppos.x-x,ppos.y-y,x1,y1) < 200){
          bosses.get(i).hp-=righthand.stat;
          pain.stop();
          pain.play();
        }        
      }
    }else if(righthandr.z == 0){
      righthandr.x-=speedr;
    }
    if(righthandr.x < 0){
      righthandr.x = 0;
    }
  }
  if(lefthandr.x != 0 || lefthandr.z == 1){
    if(lefthandr.x < 90 && lefthandr.z == 1){
      lefthandr.x+=speedl;
    }else if(lefthandr.x >= 90 && lefthandr.z == 1){
      lefthandr.z = 0;
      for(int i = 0; i < mobs.size(); i++){
        float x1 = mobs.get(i).pos.x;
        float y1 = mobs.get(i).pos.y;
        float r = 140;
        float x = r * cos(radians(mouse.x));
        float y = r * sin(radians(mouse.x));
        if(dist(ppos.x-x,ppos.y-y,x1,y1) < 200){
          mobs.get(i).hp-=lefthand.stat;
          pain.stop();
          pain.play();
        }
      }
      for(int i = 0; i < bosses.size(); i++){
        float x1 = bosses.get(i).pos.x;
        float y1 = bosses.get(i).pos.y;
        float r = 140;
        float x = r * cos(radians(mouse.x));
        float y = r * sin(radians(mouse.x));
        if(dist(ppos.x-x,ppos.y-y,x1,y1) < 200){
          bosses.get(i).hp-=lefthand.stat;
          pain.stop();
          pain.play();
        }        
      }
    }else if(lefthandr.z == 0){
      lefthandr.x-=speedl;
    }
    if(lefthandr.x < 0){
      lefthandr.x = 0;
    }
  }
}

void deathanimation(){
  tint(150,100,100);
  for(int i = 0; i < 8; i++){
    keyp[i] = false;
  }
  if(rz < 1){
    rz+=0.01;
    ch-=0.6;
  }
  ui.beginDraw();
  ui.text("Press R to respawn",885,530);
  ui.endDraw();
}

void managedrops(){
  for(int i = mobdrops.size()-1; i > -1; i--){
    mobdrops.get(i).display(keyp[7]);
    if(mobdrops.get(i).picked){
      mobdrops.remove(i);
    }
  }
}

void fixinven(){
  if(inhand.empty == false){
      boolean found = true;
      int x = 0;
      int y = 0;
      for(y = 0; y < 3; y++){
        for(x = 0; x < 5; x++){
          if(inventory[x][y].empty && found){
            found = false;
            break;
          }
        }
        if(found == false){
          break;
        }
      }
      if(found == false){
        inventory[x][y] = inhand;
        inhand = new item();
      }
  }else if(inforge.empty == false){
      boolean found = true;
      int x = 0;
      int y = 0;
      for(y = 0; y < 3; y++){
        for(x = 0; x < 5; x++){
          if(inventory[x][y].empty && found){
            found = false;
            break;
          }
        }
        if(found == false){
          break;
        }
      }
      if(found == false){
        inventory[x][y] = inforge;
        inforge = new item();
      }
  }
}