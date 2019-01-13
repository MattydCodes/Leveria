import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 
import java.awt.Robot; 
import java.awt.event.InputEvent; 
import java.awt.event.KeyEvent; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Leveria_V0_7 extends PApplet {


SoundFile death;
SoundFile deathmob;
SoundFile pain;
SoundFile pursuit;
SoundFile attack;
SoundFile forge;
SoundFile ambient;



Robot robot;
int seed;
int dens = 100;
int wi = 10;
int hi = 10;
int multi = 10000;
int divider = 200;
int chunkdist = 20*(wi*dens); //how many chunks to render in each direction.
float treespawnrate = 1;
int grassdens = 200; //amount of grass per chunk.
float flowerspawnrate = 0.3f;
float cloudspawnrate = 0.3f;
ArrayList<ground> grounds = new ArrayList<ground>();
PGraphics d3;
PGraphics bloomp;
PGraphics fogp;
PShader bloom;
PShader fog;
public void setup(){
  
  //size(1920,1080,P2D);
  //frameRate(1000);
  d3 = createGraphics(1920,1080,P3D);
  ui = createGraphics(1920,1080,P2D);
  bloomp = createGraphics(960,540,P2D);
  fogp = createGraphics(1920,1080,P2D);
  d3.smooth(16);
  death = new SoundFile(this, "death.wav");
  deathmob = new SoundFile(this, "deathmob.wav");
  pain = new SoundFile(this, "painsoundshade.wav");
  pursuit = new SoundFile(this, "pursuit.wav");
  attack = new SoundFile(this, "attack.wav");
  forge = new SoundFile(this, "forge.wav");
  ambient = new SoundFile(this, "ambientsound.wav");
  ambient.loop();
  flower = loadImage("poppy.png");
  swordph = loadImage("swordph.png");
  sworda = loadImage("sworda.png");
  swordb = loadImage("swordb.png");
  swordc = loadImage("swordc.png");
  shielda = loadImage("shielda.png");
  shieldb = loadImage("shieldb.png");
  shieldc = loadImage("shieldc.png");
  shield = loadImage("shield.png");
  shieldph = loadImage("shieldph.png");
  bin = loadImage("bin.png");
  anvil = loadImage("anvil.png");
  hammer = loadImage("hammer.png");
  pillartext = loadImage("models/pillartext.png");
  inventorytxt = loadImage("inventorytxt.png");
  statstxt = loadImage("statstxt.png");
  forgetxt = loadImage("forgetxt.png");
  tutorial = loadImage("tutorial.png");
  mswordc = loadShape("models/swordc.obj");
  mswordb = loadShape("models/swordb.obj");
  msworda = loadShape("models/sworda.obj");
  shade = loadShape("models/shade.obj");
  mboss = loadShape("models/shadeboss.obj");
  mproj = loadShape("models/proj.obj");
  mdrop = loadShape("models/scroll.obj");
  bloom = loadShader("bloom.frag");
  fog = loadShader("fog.frag");
  shade.scale(10);
  mboss.scale(10);
  mproj.scale(4);
  msworda.scale(4);
  mswordb.scale(4);
  mswordc.scale(4);
  mdrop.scale(4);
  try{
    String[] inven = loadStrings("data/inventory.txt");
    for(int i = 0; i < 15; i++){
      int y = PApplet.parseInt(i/5);
      int x = i-y*5;
      inventory[x][y] = stringtoitem(inven[i]);
    }
    lefthand = stringtoitem(inven[15]);
    righthand = stringtoitem(inven[16]);
    inarmour = stringtoitem(inven[17]);
    flowerc = PApplet.parseInt(inven[18]);
    flowerstotal = PApplet.parseInt(inven[19]);
    points = PApplet.parseInt(inven[20]);
    daycount = PApplet.parseInt(inven[21]);
    ppos.x = PApplet.parseFloat(inven[22]);
    ppos.y = PApplet.parseFloat(inven[23]);
    seed = PApplet.parseInt(inven[24]);
    health = PApplet.parseFloat(inven[25]);
    time = PApplet.parseFloat(inven[26]);
    mouse.x = PApplet.parseFloat(inven[27]);
    mouse.y = PApplet.parseFloat(inven[28]);
    sense = PApplet.parseFloat(inven[29]);
  }catch(Exception e){
    println(e);
    for(int x = 0; x < 5; x++){
      for(int y = 0; y < 3; y++){
        inventory[x][y] = new item();
      }
    }
    lefthand = new item(sworda,10,0.5f,msworda);
    inarmour = new item(shielda,10);
    seed = PApplet.parseInt(random(10000));
  }
  updatehp();
  updateflower();
  updatelefthand();
  updaterighthand();
  ui.beginDraw();
  ui.image(tutorial,0,0,width,height);
  ui.endDraw();
  noiseSeed(seed); 
  noCursor();
  initchunks();
  try {
    robot = new Robot();
    robot.setAutoDelay(0);
  } 
  catch (Exception e) {
    println(e.getMessage());
  }
  robot.mouseMove(width/2,height/2);
}
public void draw(){
  if(iopen){
    cursor();
    openinventory();
    tint(100);
    image(d3,0,0,width,height);
    tint(255);
    image(ui,0,0,width,height);
  }else if(sopen){
    cursor();
    opensettings();
    tint(100);
    image(d3,0,0,width,height);
    tint(255);
    image(ui,0,0,width,height);
  }else{
    if(health == 0){
      deathanimation();
    }
    cooldowns();
    attackanimation();
    draw3d();
    bloomp.beginDraw();
    bloomp.clear();
    bloomp.image(d3,0,0,960,540);
    bloomp.endDraw();
    fogp.beginDraw();
    fogp.clear();
    fogp.image(d3,0,0,1920,1080);
    fogp.endDraw();
    bloom.set("skyr",red(currentsky)/PApplet.parseFloat(255),green(currentsky)/PApplet.parseFloat(255),blue(currentsky)/PApplet.parseFloat(255));
    fog.set("skyc",red(currentsky)/PApplet.parseFloat(255),green(currentsky)/PApplet.parseFloat(255),blue(currentsky)/PApplet.parseFloat(255));
    bloomp.filter(bloom);
    fogp.filter(fog);
    image(d3,0,0,width,height);
    image(fogp,0,0,width,height);
    image(bloomp,0,0,width,height);
    image(ui,0,0,width,height);
  }
}

public void draw3d(){
  d3.beginDraw();
  sky();
  d3.endDraw();
  d3.beginDraw();
  sunmoonlight();
  if(updatedchunks){
    updatedchunks = false;
    thread("chunkupdate");
  }
  allcollision();
  move();
  lastz = lerp(lastz,ppos.z,0.25f);
  d3.translate(-ppos.x,-ppos.y,-posheight+lastz); 
  for(int i = 0; i < grounds.size(); i++){
    try{
      float x = -chunkdist/2 * cos(radians(mouse.x));
      float y = -chunkdist/2 * sin(radians(mouse.x));
      float x1 = grounds.get(i).pos.x-ppos.x;
      float y1 = grounds.get(i).pos.y-ppos.y;
      if(dist(x,y,x1,y1) < chunkdist*0.7f){
        grounds.get(i).display();
      }
    }catch(Exception e){
    }
  }
  managemobs();
  managedrops();
  d3.translate(ppos.x,ppos.y,posheight-lastz);
  d3.resetMatrix();
  cam();
  if(righthand.empty == false){
    displaymodel(righthand.model,-30+righthandr.x/PApplet.parseFloat(4),140+righthandr.x/PApplet.parseFloat(9),righthandr.x,-righthandr.x/PApplet.parseFloat(5),0);
  }
  if(lefthand.empty == false){
    displaymodel(lefthand.model,30-lefthandr.x/PApplet.parseFloat(4),140+lefthandr.x/PApplet.parseFloat(9),lefthandr.x,lefthandr.x/PApplet.parseFloat(5),0);
  }  
  d3.endDraw();
}

public void displaymodel(PShape model, float offset, float r, float xr, float yr, float zr){
    float x = r * cos(radians(mouse.x+offset));
    float y = r * sin(radians(mouse.x+offset));
    d3.translate(-x,-y,-180-map(mouse.y,0,180,-100,100));
    d3.rotateZ(radians(mouse.x+90));
    d3.rotateX(radians(-90+xr));
    d3.rotateY(radians(90+yr));
    d3.ambient(80);
    d3.shape(model);
    d3.rotateY(radians(-90-yr));
    d3.rotateX(radians(90-xr));
    d3.rotateZ(radians(-mouse.x-90));
    d3.translate(x,y,180+map(mouse.y,0,180,-100,100));
}
float time = 0;
int sunset = color(255,84,34);
int day = color(10,100,250);
int night = color(5,10,75);
int light = color(255,245,245);
int currentsky;
int daycount = 0;
public void sky(){
  if(time >= 150 && time <= 180){
    d3.background(lerpColor(day,night,(time-150)/30));
  }else if(time < 150){
    d3.background(day);
  }else if(time >= 330){
    d3.background(lerpColor(night,day,(time-330)/30));
  }else{
    d3.background(night);
  }
  d3.rotateX(radians(time-90));
  d3.translate(0,0,-500);
  d3.fill(255,174,68);
  d3.sphere(25);
  d3.translate(0,0,500);
  d3.translate(0,0,500);
  d3.fill(255);
  d3.sphere(25);
  d3.translate(0,0,-500);
  d3.rotateX(radians(-time+90));
}
public void sunmoonlight(){
  d3.ambientLight(40,40,40);
  d3.directionalLight(40,40,40,1,0,1);
  d3.rotateX(radians(time-90));
  if(time >= 150 && time <= 180){
    d3.directionalLight(255-constrain((time-150)/30*255,0,255),255-constrain((time-150)/30*255,0,255),255-constrain((time-150)/30*255,0,255),0,0,1);
    currentsky = lerpColor(day,night,(time-150)/30);
  }else if(time < 150){
    d3.directionalLight(255,255,255,0,0,1);
    currentsky = day;
  }else if(time >= 330){
    d3.directionalLight((time-330)/30*255,(time-330)/30*255,(time-330)/30*255,0,0,1);
    currentsky = lerpColor(night,day,(time-330)/30);
  }else{
    currentsky = night;
  }
  d3.rotateX(-radians(time-90));
  time+=0.025f;
  if(time > 359){
    time = 0;
    daycount++;
  }
}
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
      if(random(1) >= 0.333f){
        float dmg = round(random(20+constrain(daycount,0,20)*2,40+constrain(daycount,0,20)*2));
        drop = new item(swordc,dmg,round(100*(dmg/(40+constrain(daycount,0,20)*2)/2))/PApplet.parseFloat(100),mswordc);
      }else{
        drop = new item(shieldc,round(constrain(random(40+constrain(daycount,0,39),60+constrain(daycount,0,39)),40,99)));
      }
    }else{
      if(random(1) >= 0.25f){
        if(random(1) >= 0.5f){
          float dmg = random(5+daycount,10+daycount);
          drop = new item(sworda,round(dmg),round(100*(dmg/(10+constrain(daycount,0,10))))/PApplet.parseFloat(100),msworda);
        }else{
          float dmg = random(10+daycount,15+daycount);
          drop = new item(swordb,round(dmg),round(100*(dmg/(15+constrain(daycount,0,15))))/PApplet.parseFloat(100),mswordb);
        }
      }else{
        if(random(1) >= 0.5f){
          drop = new item(shielda,round(constrain(random(10+daycount,20+daycount),10,50)));
        }else{
          drop = new item(shieldb,round(constrain(random(20+daycount,40+daycount),20,75)));
        }
      }
    }
  }
  public void display(boolean interact){
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

public void cooldowns(){
  if(lefthand.cd > 0){
    lefthand.cd-=0.016f;
    if(lefthand.cd < 0){
      lefthand.cd = 0;
    }
  }
  if(righthand.cd > 0){
    righthand.cd-=0.016f;
    if(righthand.cd < 0){
      righthand.cd = 0;
    }
  }
}

public void attackanimation(){
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

public void deathanimation(){
  tint(150,100,100);
  for(int i = 0; i < 8; i++){
    keyp[i] = false;
  }
  if(rz < 1){
    rz+=0.01f;
    ch-=0.6f;
  }
  ui.beginDraw();
  ui.text("Press R to respawn",885,530);
  ui.endDraw();
}

public void managedrops(){
  for(int i = mobdrops.size()-1; i > -1; i--){
    mobdrops.get(i).display(keyp[7]);
    if(mobdrops.get(i).picked){
      mobdrops.remove(i);
    }
  }
}

public void fixinven(){
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
PGraphics ui;
boolean iopen = false;
boolean sopen = false;
boolean cused = false;
float armourc = 0;
float damagec = 0;
float cdc = 0;
float cost = 0;
public void updatehp(){
  if(health <= 0){
    health = 0;
    death.play();
    for(int x = 0; x < 5; x++){
      for(int y = 0; y < 3; y++){
        inventory[x][y] = new item();
      }
    }
    righthand = new item();
    lefthand = new item();
  }else if(health > 100){
    health = 100;
  }
  ui.beginDraw();
  ui.fill(50);
  ui.stroke(0);
  ui.strokeWeight(4);
  qud(60,20,40,40);
  qud(100,68,30,30);
  qud(69,105,24,24);
  qud(95,137,18,18);
  qud(76,162,12,12);
  qud(147,23,40,40);
  ui.image(shield,108,30,80,80);
  ui.fill(0);
  ui.text(inarmour.stat + "%",128,70);
  ui.fill(255,20,10);
  ui.strokeWeight(2);
  ui.stroke(245,10,0);
  if(health >= 80){
    qud(60,96,-map(health,80,100,1,36),-map(health,80,100,1,36));
    qud(100,124,-26,-26);
    qud(69,149,-20,-20);
    qud(95,169,-14,-14);
    qud(76,182,-8,-8);
  }else if(health >= 60){
    qud(100,124,-map(health,60,80,1,26),-map(health,60,80,1,26));
    qud(69,149,-20,-20);
    qud(95,169,-14,-14);
    qud(76,182,-8,-8);
  }else if(health >= 40){
    qud(69,149,-map(health,40,60,1,20),-map(health,40,60,1,20));
    qud(95,169,-14,-14);
    qud(76,182,-8,-8);
  }else if(health >= 20){
    qud(95,169,-map(health,20,40,1,14),-map(health,20,40,1,14));
    qud(76,182,-8,-8);
  }else if(health < 20 && health > 0){
    qud(76,182,-map(health,0,20,1,8),-map(health,0,20,0,8));
  }
  ui.endDraw();  
}
public void updateflower(){
  ui.beginDraw();
  ui.fill(50);
  ui.stroke(0);
  ui.strokeWeight(4);
  qud(60,960,40,40);
  ui.image(flower,18,960,80,80);
  qud(60,1011,12,12);
  ui.fill(255);
  ui.textSize(16);
  ui.text(str(flowerc),55,1030);
  ui.endDraw();
}
public void updatelefthand(){
  ui.beginDraw();
  if(mouseX < 190 && mouseX > 110 && mouseY < 1040 && mouseY > 960){
    if(inhand.weapon && mousePressed && cused == false && lefthand.empty){
      lefthand = inhand;
      inhand = new item();
      cused = true;
    }else if(inhand.empty && mousePressed && cused == false && lefthand.empty == false){
      inhand = lefthand;
      lefthand = new item();
      cused = true;
    }else if(mousePressed && inhand.empty == false && lefthand.empty == false && cused == false){
      item temp = lefthand;
      lefthand = inhand;
      inhand = temp;
      cused = true;
    }
    ui.fill(100);
  }else{
    ui.fill(50);
  }
  ui.stroke(0);
  ui.strokeWeight(4);
  qud(150,960,40,40);
  if(lefthand.empty){
    ui.image(swordph,110,960,80,80);
  }else{
    ui.image(lefthand.img,110,960,80,80);
  }
  ui.endDraw();  
}
public void updaterighthand(){
  ui.beginDraw();
  if(mouseX < 280 && mouseX > 200 && mouseY < 1040 && mouseY > 960){
    if(inhand.weapon && mousePressed && cused == false && righthand.empty){
      righthand = inhand;
      inhand = new item();
      cused = true;
    }else if(inhand.empty && mousePressed && cused == false && righthand.empty == false){
      inhand = righthand;
      righthand = new item();
      cused = true;
    }else if(mousePressed && inhand.empty == false && righthand.empty == false && cused == false){
      item temp = righthand;
      righthand = inhand;
      inhand = temp;
      cused = true;
    }
    ui.fill(100);
  }else{
    ui.fill(50);
  }
  ui.stroke(0);
  ui.strokeWeight(4);
  qud(240,960,40,40);
  if(righthand.empty){
    ui.image(swordph,200,960,80,80);
  }else{
    ui.image(righthand.img,200,960,80,80);
  }
  ui.endDraw();  
}
public void updatearmour(){
  ui.beginDraw();
  if(mouseX < 370 && mouseX > 290 && mouseY < 1040 && mouseY > 960){
    if(inhand.weapon == false && mousePressed && cused == false && inarmour.empty){
      inarmour = inhand;
      inhand = new item();
      cused = true;
    }else if(inhand.empty && mousePressed && cused == false && inarmour.empty == false){
      inhand = inarmour;
      inarmour = new item();
      cused = true;
    }else if(mousePressed && inhand.empty == false && inarmour.empty == false && cused == false){
      item temp = inarmour;
      inarmour = inhand;
      inhand = temp;
      cused = true;
    }
    ui.fill(100);
  }else{
    ui.fill(50);
  }
  ui.stroke(0);
  ui.strokeWeight(4);
  qud(330,960,40,40);
  if(inarmour.empty){
    ui.image(shieldph,290,960,80,80);
  }else{
    ui.image(inarmour.img,290,960,80,80);
  }
  ui.endDraw(); 
}
public void openinventory(){
  ui.beginDraw();
  ui.clear();
  ui.endDraw();
  updatehp();
  updateflower();
  updatelefthand();
  updaterighthand();
  updatearmour();
  ui.beginDraw();
  ui.fill(50);
  ui.stroke(0);
  ui.strokeWeight(10);
  ui.rect(150,150,1620,780);
  ui.line(1240,150,1240,930);
  ui.line(1240,410,1770,410);
  ui.strokeWeight(4);
  ui.image(inventorytxt,540,80);
  ui.image(statstxt,1375,100,200,200);
  ui.image(forgetxt,1400,350,200,200);
  for(int x = 0; x < 5; x++){
    for(int y = 0; y < 3; y++){
      if(mouseX < 250+x*220+40 && mouseX > 250+x*220-40 && mouseY > 290+y*220 && mouseY < 290+y*220+80){
        if(inventory[x][y].empty == false){
          ui.rect(250+x*220+40,290+y*220,140,105);
          ui.fill(0);
          if(inventory[x][y].weapon){
            if(inventory[x][y].img == sworda){
              ui.text("Wooden Sword",255+x*220+40,290+y*220,390+x*220+40,50+y*220+80);
            }else if(inventory[x][y].img == swordb){
              ui.text("Silver Sword",255+x*220+40,290+y*220,390+x*220+40,50+y*220+80);
            }else if(inventory[x][y].img == swordc){
              ui.text("GodSlayer Sword",255+x*220+40,290+y*220,390+x*220+40,50+y*220+80);
            }
            ui.text("Damage : " + inventory[x][y].stat,255+x*220+40,330+y*220);
            ui.text("Speed : " + round(inventory[x][y].cdreset*100)/PApplet.parseFloat(100) + "s",255+x*220+40,355+y*220);
            ui.text("Value : " + PApplet.parseInt((inventory[x][y].stat)/(inventory[x][y].cdreset)*2) + "SF",255+x*220+40,380+y*220);
          }else{
            if(inventory[x][y].img == shielda){
              ui.text("Wooden Shield",255+x*220+40,290+y*220,390+x*220+40,50+y*220+80);
            }else if(inventory[x][y].img == shieldb){
              ui.text("Silver Shield",255+x*220+40,290+y*220,390+x*220+40,50+y*220+80);
            }else if(inventory[x][y].img == shieldc){
              ui.text("GodSlayer Shield",255+x*220+40,290+y*220,390+x*220+40,50+y*220+80);
            }
            ui.text("Value : " + PApplet.parseInt(inventory[x][y].stat*10) + "SF",255+x*220+40,355+y*220);
            ui.text("Armour : " + inventory[x][y].stat + "%",255+x*220+40,330+y*220);
          }
        }
        if(mousePressed && inhand.empty && inventory[x][y].empty == false && cused == false){
          inhand = inventory[x][y];
          inventory[x][y] = new item();
          cused = true;
        }else if(mousePressed && inhand.empty == false && inventory[x][y].empty && cused == false){
          inventory[x][y] = inhand;
          inhand = new item();
          cused = true;
        }else if(mousePressed && inhand.empty == false && inventory[x][y].empty == false && cused == false){
          item temp = inventory[x][y];
          inventory[x][y] = inhand;
          inhand = temp;
          cused = true;
        }
        ui.fill(100);
      }else{
        ui.fill(50);
      }
      qud(250+x*220,290+y*220,40,40);
      if(inventory[x][y].empty == false){
        ui.image(inventory[x][y].img,210+x*220,290+y*220,80,80);
      }
    }
  }
  if(mouseX < 1500+40 && mouseX > 1500-40 && mouseY > 780 && mouseY < 780+80){
    ui.fill(100);
    if(mousePressed && cused == false && inhand.empty == false){
      if(inhand.weapon){
        points+=PApplet.parseInt((inhand.stat)/(inhand.cdreset)*2);
      }else{
        points+=PApplet.parseInt(inhand.stat*10);
      }
      inhand = new item();
    }
  }else{
    ui.fill(50);
  }
  qud(1500,780,40,40);
  ui.image(bin,1460,780,80,80);
  if(mouseX < 1500+40 && mouseX > 1500-40 && mouseY > 680 && mouseY < 680+80){
    ui.fill(100);
    if(mousePressed && inhand.empty && inforge.empty == false && cused == false){
      inhand = inforge;
      inforge = new item();
      cused = true;
      armourc = 0;
      damagec = 0;
      cdc = 0;
    }else if(mousePressed && inhand.empty == false && inforge.empty && cused == false){
      inforge = inhand;
      inhand = new item();
      cused = true;
      armourc = 0;
      damagec = 0;
      cdc = 0;
    }else if(mousePressed && inhand.empty == false && inforge.empty == false && cused == false){
      item temp;
      temp = inforge;
      inforge = inhand;
      inhand = temp;
      cused = true;
      armourc = 0;
      damagec = 0;
      cdc = 0;
    }
  }else{
    ui.fill(50);
  }
  qud(1500,680,40,40);
  if(inforge.empty){
    ui.image(anvil,1460,680,80,80);
  }else{
    ui.image(inforge.img,1460,680,80,80);
  }
  ui.fill(0);
  ui.text("Health Points: " + round(health),1400,270);
  ui.text("Armour: " + PApplet.parseInt(inarmour.stat) + "%",1400,290);
  ui.text("Weapon 1 : " + PApplet.parseInt(lefthand.stat) + " (" + round(lefthand.cdreset*100)/PApplet.parseFloat(100) + "s)",1400,310);
  ui.text("Weapon 2 : " + PApplet.parseInt(righthand.stat) +  " (" + round(righthand.cdreset*100)/PApplet.parseFloat(100) + "s)",1400,330);
  ui.text("Days Survived: " + daycount,1400,350);
  ui.text("Flowers Picked : " + flowerstotal,1400,370);
  ui.text("Shade Fragments : " + PApplet.parseInt(points),1400,390);
  if(inforge.empty == false){
    if(inforge.weapon){
      ui.text("Armour Increase : 0% (0%)",1300,540);
      ui.text("Damage Increase : " + damagec + " (" + (inforge.stat+damagec) + ")",1300,580);
      ui.text("Speed Increase : " + cdc + "("+ (round((inforge.cdreset-cdc)*100)/PApplet.parseFloat(100)) +")",1300,620);
    }else{
      ui.text("Armour Increase : " + armourc + "% (" + (inforge.stat+armourc) +")",1300,540);
      ui.text("Damage Increase : 0 (0)",1300,580);
      ui.text("Speed Increase : 0 (0)",1300,620);
    }
  }
  if(inforge.empty == false){
    if(inforge.weapon){
      cost=PApplet.parseInt((inforge.stat+damagec*damagec)/(inforge.cdreset-cdc)*2*(1+cdc*5));
    }else{
      cost=PApplet.parseInt((armourc*armourc+inforge.stat)*10);
    }
    ui.text("Cost : " + cost + "SF",1300,660);
  }else{
    cost = 0;
  }
  ui.fill(50);
  if(mouseX < 1600+20 && mouseX > 1600-20 && mouseY > 510 && mouseY < 550 && inforge.weapon == false){
    if(wheel != 0 && inforge.weapon == false){
      armourc+=0.5f*wheel;
      if(inforge.stat+armourc > 95 || armourc < 0){
        armourc-=0.5f*wheel;
      }
      wheel = 0;
    }
    ui.fill(100);
  }
  qud(1600,510,20,20);
  ui.fill(50);
  if(mouseX < 1600+20 && mouseX > 1600-20 && mouseY > 550 && mouseY < 590 && inforge.weapon){
    if(wheel != 0 && inforge.weapon){
      damagec+=wheel;
      if(damagec < 0){
        damagec-=wheel;
      }
      wheel = 0;
    }
    ui.fill(100);
  }
  qud(1600,550,20,20);
  ui.fill(50);
  if(mouseX < 1600+20 && mouseX > 1600-20 && mouseY > 590 && mouseY < 630 && inforge.weapon){
    if(wheel != 0 && inforge.weapon){
      cdc+=wheel/100;
      if(cdc < 0 || inforge.cdreset-cdc < 0.01f){
        cdc-=wheel/100;
      }
      cdc = round((cdc*100))/PApplet.parseFloat(100);
      wheel = 0;
    }
    ui.fill(100);
  }
  qud(1600,590,20,20);
  ui.fill(50);
  if(mouseX < 1680+40 && mouseX > 1680-40 && mouseY > 530 && mouseY < 610 && cused == false && cost <= points){
    ui.fill(100);
    if(mousePressed && inforge.empty == false && (cdc != 0 || damagec != 0 || armourc != 0)){
      if(inforge.weapon){
        inforge.stat+=damagec;
        inforge.cdreset-=cdc;
        cdc = 0;
        damagec = 0;
      }else{
        inforge.stat+=armourc;
        armourc = 0;
      }
      points-=cost;
      forge.play();
      cused = true;
    }
  }
  qud(1680,530,40,40);
  ui.image(hammer,1640,530,80,80);
  if(inhand.empty == false){
    ui.image(inhand.img,mouseX-40,mouseY-40,80,80);
  }
  ui.endDraw();
}
public void opensettings(){
  ui.beginDraw();
  ui.fill(50);
  ui.stroke(0);
  ui.strokeWeight(10);
  ui.rect(576,20,576,1040);
  ui.strokeWeight(5);
  if(mouseX > 940 && mouseX < 980 && mouseY > 65 && mouseY < 105){
    sense+=wheel*0.01f;
    sense = round(sense*100)/PApplet.parseFloat(100);
    sense = constrain(sense,0.01f,1);
    wheel = 0;
    ui.fill(100);
  }
  qud(960,65,20,20);
  ui.fill(50);
  if(mouseX > 756 && mouseX < 796 && mouseY > 940 && mouseY < 980){ //save
    ui.fill(100);
    if(mousePressed && cused == false){
      saveinventory();
      cused = true;
    }
  }
  qud(776,940,20,20);
  ui.fill(50);
  if(mouseX > 1010 && mouseX < 1050 && mouseY > 940 && mouseY < 980){ //savequit
    ui.fill(100);
    if(mousePressed && cused == false){
      saveinventory();
      exit();
      cused = true;
    }
  }
  qud(1030,940,20,20);
  ui.fill(0);
  ui.text("Mouse Sensitivity : " + sense,746,90);
  ui.text("Save",706,965);
  ui.text("Save & Quit",906,965);
  ui.endDraw();
}
public void qud(float sx, float sy, float dx, float dy){
  ui.quad(sx,sy,sx+dx,sy+dy,sx,sy+dy*2,sx-dx,sy+dy);
}
public void saveinventory(){
  String[] inven = new String[30];
  for(int x = 0; x < 5; x++){ //! = weapon
    for(int y = 0; y < 3; y++){ //? = armour //e = empty //& seperates stat and cdreset.
      int index = x + y * 5;
      inven[index] = itemtostring(inventory[x][y]);
    }
  }
  inven[15] = itemtostring(lefthand);
  inven[16] = itemtostring(righthand);
  inven[17] = itemtostring(inarmour);
  inven[18] = str(flowerc);
  inven[19] = str(flowerstotal);
  inven[20] = str(points);
  inven[21] = str(daycount);
  inven[22] = str(ppos.x);
  inven[23] = str(ppos.y);
  inven[24] = str(seed);
  inven[25] = str(health);
  inven[26] = str(time);
  inven[27] = str(mouse.x);
  inven[28] = str(mouse.y);
  inven[29] = str(sense);
  saveStrings("data/inventory.txt",inven);
}

public String itemtostring(item i){
  if(i.empty){
    return("e"); 
  }else if(i.empty == false){
    String type;
    if(i.weapon){
      if(i.img == sworda){
        type = "a";
      }else if(i.img == swordb){
        type = "b";
      }else{
        type = "c";
      }
      return(type + "!" + str(i.stat) + "&" + str(i.cdreset)); 
    }else{
      if(i.img == shielda){
        type = "a";
      }else if(i.img == shieldb){
        type = "b";
      }else{
        type = "c";
    }
    return(type + "?" + str(i.stat));
    }
  }  
  return("e");
}

public item stringtoitem(String i){
  if(i.equals("e")){
    return(new item());
  }
  String type = i.substring(0,1);
  if(i.substring(1,2).equals("!")){
    int index = i.indexOf("&");
    float dmg = PApplet.parseFloat(i.substring(2,index-1));
    float cdreset = PApplet.parseFloat(i.substring(index+1,i.length()));
    if(type.equals("a")){
      return(new item(sworda,dmg,cdreset,msworda));
    }else if(type.equals("b")){
      return(new item(swordb,dmg,cdreset,mswordb));
    }else{
      return(new item(swordc,dmg,cdreset,mswordc));
    }
  }else{
    float stat = PApplet.parseFloat(i.substring(2,i.length()));
    if(type.equals("a")){
      return(new item(shielda,stat));
    }else if(type.equals("b")){
      return(new item(shieldb,stat));
    }else{
      return(new item(shieldc,stat));
    }
  }
}
float ch = 200;
float rz = 0;
float lastz = 0;
public void cam(){
  int r = 300;
  float x = r * cos(radians(mouse.x));
  float y = r * sin(radians(mouse.x));
  float z = r * cos(radians(mouse.y));
  d3.camera(0,0,-ch,-x,-y,-ch+z,rz,0,1-rz);
}
boolean updatedchunks = true;
PVector lastchunk = new PVector(0,0);
public void chunkupdate(){
  int cx = PApplet.parseInt(PApplet.parseInt(ppos.x)/(wi*dens))*(wi*dens);
  int cy = PApplet.parseInt(PApplet.parseInt(ppos.y)/(hi*dens))*(hi*dens);
  if(cx > lastchunk.x){
    for(int i = grounds.size()-1; i > -1; i--){
      if(grounds.get(i).pos.x < cx-chunkdist/2){
        grounds.remove(i);
      }
    }
    for(int y = 0; y < chunkdist/wi/dens+1; y++){
      grounds.add(new ground(cx+chunkdist/2,cy-chunkdist/2+y*wi*dens,wi,hi,dens));
    }
    lastchunk.x = cx;
  }else if(cx < lastchunk.x){
    for(int i = grounds.size()-1; i > -1; i--){
      if(grounds.get(i).pos.x > cx+chunkdist/2){
        grounds.remove(i);
      }
    }
    for(int y = 0; y < chunkdist/wi/dens+1; y++){
      grounds.add(new ground(cx-chunkdist/2,cy-chunkdist/2+y*wi*dens,wi,hi,dens));
    }
    lastchunk.x = cx;  
  }
  
//------------------------------------------------------------------------------------------------------------------------------------------------------

  if(cy > lastchunk.y){
    for(int i = grounds.size()-1; i > -1; i--){
      if(grounds.get(i).pos.y < cy-chunkdist/2){
        grounds.remove(i);
      }
    }
    for(int x = 0; x < chunkdist/wi/dens+1; x++){
      grounds.add(new ground(cx-chunkdist/2+x*wi*dens,cy+chunkdist/2,wi,hi,dens));
    }
    lastchunk.y = cy;
  }else if(cy < lastchunk.y){
    for(int i = grounds.size()-1; i > -1; i--){
      if(grounds.get(i).pos.y > cy+chunkdist/2){
        grounds.remove(i);
      }
    }
    for(int x = 0; x < chunkdist/wi/dens+1; x++){
      grounds.add(new ground(cx-chunkdist/2+x*wi*dens,cy-chunkdist/2,wi,hi,dens));
    }
    lastchunk.y = cy;  
  }
 
//-------------------------------------------------------------------------------------------------------------------------------------------------------
  updatedchunks = true;
}
PImage flower;
PImage swordph;
PImage sworda;
PImage swordb;
PImage swordc;
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
PShape shade;
PShape mboss;
PShape mproj;
PShape msworda;
PShape mswordb;
PShape mswordc;
PShape mdrop;
PImage tutorial;
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
  float cdreset = 1-1/(daycount+1)*0.1f;
  float increment = 180/lefthand.cdreset/60;
  float damage = random(5+daycount*2,10+daycount*2);
  float rot;
  mob(){
    spawn = new PVector(random(ppos.x-chunkdist,ppos.x+chunkdist),random(ppos.y-chunkdist,ppos.y+chunkdist));
    pos.x = spawn.x;
    pos.y = spawn.y;
    pos.z = noise((pos.x/dens)/PApplet.parseFloat(divider),(pos.y/dens)/divider)*PApplet.parseFloat(multi);
    posf.x = spawn.x+=random(-1000,1000);
    posf.y = spawn.y+=random(-1000,1000);
  }
  //possibly thread
  public void walk(){
    pos.z = lerp(pos.z,noise((pos.x/dens)/PApplet.parseFloat(divider),(pos.y/dens)/divider)*PApplet.parseFloat(multi),0.1f);
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
      cd-=0.016f;
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
        pos.x-=lastm.x+(lastm.x/t)*0.75f;
        pos.y-=lastm.y+(lastm.y/t)*0.75f;
      }
      }catch(Exception e){
      }
    }
    rot = atan(lastm.y/lastm.x);
  }
  public void display(){
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
  float cdreset = 1.5f;//0.5-1/(daycount+1)*0.1;
  float increment = 180/lefthand.cdreset/60;
  float damage = random(25+daycount*2,50+daycount*2);
  float rot;
  PVector[] boxes;
  boss(){
    pos = new PVector(random(ppos.x-chunkdist/2,ppos.x+chunkdist/2),random(ppos.y-chunkdist/2,ppos.y+chunkdist/2),0);
    boxes = new PVector[PApplet.parseInt(random(8,15))];
    for(int i = 0; i < boxes.length; i++){
      int r = round(random(359));
      float x = 800 * cos(r);
      float y = 800 * sin(r);
      float z = noise(((x+pos.x)/dens)/PApplet.parseFloat(divider),((y+pos.y)/dens)/divider)*PApplet.parseFloat(multi)-500;
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
  public void run(){
    if(random(1) > 0.995f && faster == 0){
      faster = PApplet.parseInt(random(60,240));
    }
    if(faster > 0){
      faster--;
      cdreset=0.25f;
    }else{
      cdreset=1.5f;
    }
    pos.z = noise((pos.x/dens)/PApplet.parseFloat(divider),(pos.y/dens)/divider)*PApplet.parseFloat(multi);
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
          PVector end = new PVector(ppos.x,ppos.y,noise((ppos.x/dens)/PApplet.parseFloat(divider),(ppos.y/dens)/divider)*PApplet.parseFloat(multi));
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
    cd-=0.016f;
    if(cd < 0){
      cd = 0;
      attack = false;
    }
  float x = ppos.x-pos.x;
  float y = ppos.y-pos.y;
  float t = sqrt(x*x) + sqrt(y*y);
  rot = atan((1*(y/t))/(1*(x/t)));
  }
  public void display(){
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
  public void display(){
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
public void managemobs(){
  for(int i = mobs.size()-1; i > -1; i--){
    if(dist(mobs.get(i).pos.x,mobs.get(i).pos.y,ppos.x,ppos.y) > chunkdist){
      mobs.remove(i);
      continue;
    }else if(mobs.get(i).hp <= 0){
      if(random(1) >= 0.5f){
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
    mobc*=1.5f;
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
boolean[] keyp = new boolean[8];
int jumpvel = 0;
float prevheight;
PVector mouse = new PVector(0,0);
PVector lastmove = new PVector(0,0);
PVector righthandr = new PVector(0,0,0);
PVector lefthandr = new PVector(0,0,0);
float posheight = 0;
float wheel = 0;
float sense = 0.1f;
public void keyPressed(){
  if((key == 'w' || key == 'W') && iopen == false && health > 0){
    keyp[0] = true;
  }else if((key == 's' || key == 'S') && iopen == false && health > 0){
    keyp[1] = true;
  }else if((key == 'd' || key == 'D') && iopen == false && health > 0){
    keyp[2] = true;
  }else if((key == 'a' || key == 'A') && iopen == false && health > 0){
    keyp[3] = true;
  }else if(keyCode == 16 && iopen == false && health > 0){
    keyp[4] = true;
  }else if(keyCode == 32 && keyp[5] == false && iopen == false && health > 0){
    keyp[5] = true;
    prevheight = noise(ppos.x/dens/divider,ppos.y/dens/divider)*multi;
    jumpvel = 40;
    ppos.z = 1;
  }else if(keyCode == 32 && ppos.z !=0 && iopen == false && health > 0){
    keyp[6] = true;
  }else if((key == 'e' || key == 'E') && iopen == false && health > 0){
    keyp[7] = true;
  }else if((key == 'h' || key == 'H') && flowerc > 0 && health < 100 && health > 0 && iopen == false){
    health+=10;
    flowerc--;
    updatehp();
    updateflower();
  }else if((key == 'i' || key == 'I') && health > 0 && sopen == false){
    if(iopen){
      delay(1);
      ui.beginDraw();
      ui.clear();
      ui.endDraw();
      updatehp();
      updateflower();
      updaterighthand();
      updatelefthand();
      updatearmour();
      iopen = false;
      fixinven();
      robot.mouseMove(width/2,height/2);
      noCursor();
      d3.beginDraw();
      d3.clear();
      d3.endDraw();
      draw3d();
    }else{
      draw3d();
      cursor();
      openinventory();
      iopen = true;
    }
  }else if((key == 'r' || key == 'R') && health == 0){
    ch = 200;
    rz = 0;
    health = 100;
    daycount = 0;
    for(int i = mobs.size()-1; i > -1; i--){
      mobs.remove(i);
    }
    lefthand = new item(sworda,10,0.5f,msworda);
    for(int i = grounds.size()-1; i > 0; i--){
      grounds.remove(i);
    }
    ppos.x+=random(-100000,100000);
    ppos.x = PApplet.parseInt(ppos.x/1000)*1000;
    ppos.y+=random(-100000,100000);
    ppos.y = PApplet.parseInt(ppos.y/1000)*1000;
    tint(255);
    initchunks();
    ui.beginDraw();
    ui.clear();
    ui.endDraw();
    updatehp();
    updateflower();
    updatelefthand();
    updaterighthand();
    updatearmour();
  }else if(key == 27){
    if(iopen){
      delay(1);
      ui.beginDraw();
      ui.clear();
      ui.endDraw();
      updatehp();
      updateflower();
      updaterighthand();
      updatelefthand();
      updatearmour();
      iopen = false;
      fixinven();
      robot.mouseMove(width/2,height/2);
      noCursor();
      d3.beginDraw();
      d3.clear();
      d3.endDraw();
      draw3d();
    }else{
      if(sopen){
        delay(1);
        ui.beginDraw();
        ui.clear();
        ui.endDraw();
        updatehp();
        updateflower();
        updaterighthand();
        updatelefthand();
        updatearmour();
        sopen = false;
        robot.mouseMove(width/2,height/2);
        noCursor();
        d3.beginDraw();
        d3.clear();
        d3.endDraw();
        draw3d();
      }else{
        draw3d();
        sopen = true;
      }
    }
    key = 0;
  }
}

//------------------------------------------------------------------------------------------------------------------------------------------------------

public void keyReleased(){
  if(key == 'w' || key == 'W'){
    keyp[0] = false;
  }else if(key == 's' || key == 'S'){
    keyp[1] = false;
  }else if(key == 'd' || key == 'D'){
    keyp[2] = false;
  }else if(key == 'a' || key == 'A'){
    keyp[3] = false;
  }else if(keyCode == 16){
    keyp[4] = false;
  }else if(keyCode == 32){
    keyp[6] = false;
  }else if(key == 'e' || key == 'E'){
    keyp[7] = false;
  }
}

public void mousePressed(){
  cused = false;
  if(mouseButton == RIGHT && iopen == false && righthand.cd == 0 && righthand.weapon){
    attack.stop();
    attack.play();
    righthand.cd = righthand.cdreset;
    righthandr.z = 1;
    righthandr.x = 1;
  }
  if(mouseButton == LEFT && iopen == false && lefthand.cd == 0 && lefthand.weapon){
    attack.stop();
    attack.play();
    lefthand.cd = lefthand.cdreset;
    lefthandr.z = 1;
    lefthandr.x = 1;
  }
}

//------------------------------------------------------------------------------------------------------------------------------------------------------

public void move(){
  float r = 8; //speed
  float x = r * cos(radians(mouse.x));
  float y = r * sin(radians(mouse.x));
  float p = 1;
  int keysp = 0;
  posheight = lerp(posheight,noise(ppos.x/PApplet.parseFloat(dens)/PApplet.parseFloat(divider),ppos.y/PApplet.parseFloat(dens)/PApplet.parseFloat(divider))*PApplet.parseFloat(multi),0.1f);
  lastmove.x = ppos.x;
  lastmove.y = ppos.y;
  if(posheight > ppos.z+posheight){
    keyp[5] = false;
    jumpvel = 0;
    ppos.z = 0;
  }
  for(int i = 0; i < 4; i++){
    if(keyp[i]){
      p-=0.25f;
      keysp++;
      p=constrain(p,0,2);
    }
  }
  if(keyp[4]){
    p*=2;
  }
  if(keyp[0] || keyp[1] || keyp[2] || keyp[3]){
    rotx = map(mouseX,0,width,360,0);
  }
  if(keyp[0]){
    ppos.x-=x*p;
    ppos.y-=y*p;
  }
  if(keyp[1]){
    ppos.x+=x*p;
    ppos.y+=y*p;
    rotx-=180/keysp;
  }
  if(keyp[2]){
    x = r * cos(radians(mouse.x+90));
    y = r * sin(radians(mouse.x+90));
    ppos.x+=x*p;
    ppos.y+=y*p;
    rotx-=90/keysp;
  }
  if(keyp[3]){
    x = r * cos(radians(mouse.x-90));
    y = r * sin(radians(mouse.x-90));
    ppos.x+=x*p;
    ppos.y+=y*p;
    rotx+=90/keysp;
  }
  lastmove.x-=ppos.x;
  lastmove.y-=ppos.y;
  if(ppos.z!=0){//noise(ppos.x/float(dens)/float(divider),ppos.y/float(dens)/float(divider))*float(multi)
    ppos.z+=(noise(ppos.x/PApplet.parseFloat(dens)/PApplet.parseFloat(divider),ppos.y/PApplet.parseFloat(dens)/PApplet.parseFloat(divider))*PApplet.parseFloat(multi)-prevheight);                           
    prevheight = noise(ppos.x/PApplet.parseFloat(dens)/PApplet.parseFloat(divider),ppos.y/PApplet.parseFloat(dens)/PApplet.parseFloat(divider))*PApplet.parseFloat(multi);
    ppos.z+=jumpvel*0.3f;
    jumpvel-=9*0.3f;
  }
}

public void mouseMoved(){
  if(iopen == false && sopen == false){
    mousemove();
  }
}
public void mouseDragged(){
  if(iopen == false && sopen == false){
    mousemove();
  }
}  
public void mousemove(){
  mouse.x+=(width/2-mouseX)*sense;
  mouse.y+=(height/2-mouseY)*sense;
  robot.mouseMove(width/2,height/2);
  if(mouse.x >= 360){
    mouse.x = 1+(mouse.x-360);
  }else if(mouse.x <= 0){
    mouse.x = 359+mouse.x;
  }
  if(mouse.y >= 180){
    mouse.y = 180;
  }else if(mouse.y <= 0){
    mouse.y = 0;
  }
}

public void mouseWheel(MouseEvent event) {
  if(event.getCount() > 0){
    wheel = -1;
  }else{
    wheel = 1;
  }
}
PVector ppos = new PVector(0,0,0);
//PVector startingpos = new PVector(0,0);
float health = 100;
float armour = 0;
float avgdmg = 0;
float rotx;
int points = 0;
int flowerstotal = 0;
//Class will be used to load the ground sections in "chunks".
boolean updatedcollision = true;
int snowbiome = color(255,255,255);
int grassbiome = color(31,227,93);
int desertbiome = color(253,250,179);
int prevbiome;
int treehit = 150;
int flowerhit = 300;
class ground{
  int biome;
  int density;
  PVector pos = new PVector(0,0);
  PVector fpos = new PVector(0,0,0);
  PVector tpos;
  PVector res = new PVector(0,0);
  PShape object = createShape(GROUP);
  PShape flower;
  PShape branch;
  PShape row;
  ground(float x, float y, int w, int h, int d){
    pos.x = x;
    pos.y = y;
    res.x = w+1;
    res.y = h+1;
    density = d;
    float[][] values = new float[PApplet.parseInt(res.x)][PApplet.parseInt(res.y)];
  
//------------------------------------------------------------------------------------------------------------------------------------------------------

    for(int i = 0; i < res.x; i++){
      for(int j = 0; j < res.y; j++){    
        values[i][j] = noise((this.pos.x/dens+i)/PApplet.parseFloat(divider),(this.pos.y/dens+j)/divider)*PApplet.parseFloat(multi);
      }
    }
    
//------------------------------------------------------------------------------------------------------------------------------------------------------

    PImage texture = createImage(PApplet.parseInt(res.x),PApplet.parseInt(res.y),RGB);
    texture.loadPixels();
    float avgval = 0;
    for(int i = 0; i < res.x; i++){
      for(int j = 0; j < res.y; j++){   
        int index = i + j * PApplet.parseInt(res.x);
        float val = noise((pos.x/dens+i)/1000,(pos.y/dens+j)/1000,((pos.x/dens+i)-(pos.y/dens+j))/1000);
        avgval+=val;
        if(val >= 0.3333f && val < 0.6666f){
          texture.pixels[index] = grassbiome; 
        }else if(val >= 0.6666f){
          texture.pixels[index] = snowbiome;
        }else{
          texture.pixels[index] = desertbiome;          
        }
      }
    }  
    avgval/=res.x*res.y;
    if(avgval >= 0.3333f && avgval < 0.6666f){
      biome = 1;
    }else if(avgval >= 0.6666f){
      biome = 2;
    }else{      
      biome = 3;
    }
    texture.updatePixels();
    
//------------------------------------------------------------------------------------------------------------------------------------------------------

    PShape terrain = createShape(GROUP);
    for(int i = 0; i < res.x-1; i++){
      PShape temp = createShape();
      temp.beginShape(TRIANGLE_STRIP);
      temp.texture(texture);
      temp.noFill();
      temp.noStroke();
      //temp.ambient(texture.pixels[0]);
      for(int j = 0; j < res.y; j++){   
        temp.vertex(i*density,j*density,values[i][j],i,j);
        temp.vertex((i+1)*density,j*density,values[i+1][j],i+1,j);
      }   
      temp.endShape();
      terrain.addChild(temp);
    } 
    object.addChild(terrain);
 
//------------------------------------------------------------------------------------------------------------------------------------------------------

    tpos = new PVector(0,0,0);
    float record = 100;
    for(int i = 1; i < res.x-1; i++){
      for(int j = 1; j < res.y-1; j++){
        float total = 0;
        for(int xi = -1; xi < 2; xi++){
          for(int yi = -1; yi < 2; yi++){
            total+=noise(xi+pos.x/dens,yi+pos.y/dens,values[i][j]);
          }
        }
        total/=9;
        if(total < record){
          record = total;
          tpos.x = i*dens;
          tpos.y = j*dens;
        }
      }
    }
    if(record < treespawnrate){
      tpos.z = 1;
      PImage treetexture = createImage(2,1,RGB);
      treetexture.loadPixels();
      if(biome == 1){
        treetexture.pixels[0] = color(31,227,93);
        treetexture.pixels[1] = color(107,74,4);
      }else if(biome == 2){
        treetexture.pixels[0] = color(255,255,255);
        treetexture.pixels[1] = color(157,124,54);
      }else if(biome == 3){
        treetexture.pixels[0] = color(253,250,179);
        treetexture.pixels[1] = color(127,94,24);
      }
      treetexture.updatePixels();
      //treetexture.pixels[1] = color(107,74,4);
      branch = createShape();
      branch.beginShape(TRIANGLE_STRIP);
      branch.noStroke();
      branch.texture(treetexture);
      branch.noFill();
      branch.ambient(treetexture.pixels[1]);
      for(int l = -1; l < 9; l++){
        for(int r = 0; r < 6; r++){
          float xoff = (50-l*2) * cos(radians(r*72));
          float yoff = (50-l*2) * sin(radians(r*72));
          branch.vertex(xoff+tpos.x,yoff+tpos.y,-l*60+values[PApplet.parseInt(tpos.x/dens)][PApplet.parseInt(tpos.y/dens)],2,1);
          xoff = (50-(l+1)*2) * cos(radians(r*72));
          yoff = (50-(l+1)*2) * sin(radians(r*72));
          branch.vertex(xoff+tpos.x,yoff+tpos.y,-(l+1)*60+values[PApplet.parseInt(tpos.x/dens)][PApplet.parseInt(tpos.y/dens)],2,1);
        }
      }
      branch.endShape(CLOSE);
      object.addChild(branch);
      PVector[][] rnds = new PVector[5][6];
      for(int r = 0; r < 5; r++){
        for(int i = 0; i < 5; i++){
          rnds[4-i][r] = new PVector(random(-9*i,9*i),random(-9*i,9*i),random(-9*i,9*i));
        }
        rnds[r][5] = rnds[r][0];
      }
      row = createShape();
      for(int l = 0; l < 3; l++){
        row.beginShape(TRIANGLE_STRIP);
        row.noStroke();
        row.texture(treetexture);
        row.noFill();
        row.ambient(treetexture.pixels[0]);
        for(int r = 0; r < 6; r++){
          float xoff = (330-l*88) * cos(radians(r*72));
          float yoff = (330-l*88) * sin(radians(r*72));
          row.vertex(xoff+tpos.x+rnds[l][r].x,yoff+tpos.y+rnds[l][r].y,-l*150+values[PApplet.parseInt(tpos.x/dens)][PApplet.parseInt(tpos.y/dens)]+rnds[l][r].z-120,1,1);
          xoff = (264-(l+1)*88) * cos(radians(r*72));
          yoff = (264-(l+1)*88) * sin(radians(r*72));
          row.vertex(xoff+tpos.x+rnds[l+1][r].x,yoff+tpos.y+rnds[l+1][r].y,-(l+1)*150+values[PApplet.parseInt(tpos.x/dens)][PApplet.parseInt(tpos.y/dens)]+rnds[l+1][r].z-180,1,1);
        }
      }
      row.endShape();
      object.addChild(row);
    }
    
//------------------------------------------------------------------------------------------------------------------------------------------------------

    //Put grass ect here
    PShape grass = createShape();
    float val = noise(pos.x/dens,pos.y/dens);
    for(int i = 0; i < grassdens-val*grassdens; i++){
      PImage grassc = createImage(1,1,RGB);
      if(biome == 1){
        grassc.pixels[0] = color(31,227,93);
      }else if(biome == 2){
        grassc.pixels[0] = color(255,255,255);
      }else{
        grassc.pixels[0] = color(253,250,179);
      }
      grass.beginShape(TRIANGLES);
      grass.texture(grassc);
      grass.noStroke();
      grass.noFill();
      grass.ambient(grassc.pixels[0]);
      float rndx = random(-1,1);
      float rndy = random(-1,1);
      float grassh = 0;
      if(noise((pos.x/dens+rndx+(noise(pos.x/dens*i,i)-0.5f)*2*wi)/divider,(pos.y/dens+rndy+(noise(pos.y/dens,i)-0.5f)*2*hi)/divider)*multi < noise((pos.x/dens+(noise(pos.x*i/dens,i)-0.5f)*2*wi)/divider,(pos.y/dens+(noise(pos.y/dens,i)-0.5f)*2*hi)/divider)*multi){                                                                                        
        grassh=noise((pos.x/dens+(noise(pos.x/dens*i,i)-0.5f)*2*wi)/divider,(pos.y/dens+(noise(pos.y/dens,i)-0.5f)*2*hi)/divider)*multi;
      }else{
        grassh=noise((pos.x/dens+rndx+(noise(pos.x*i/dens,i)-0.5f)*2*wi)/divider,(pos.y/dens+rndy+(noise(pos.y/dens,i)-0.5f)*2*hi)/divider)*multi;
      }
      grass.vertex((noise(pos.x/dens*i,i)-0.5f)*2*wi*dens,(noise(pos.y/dens,i)-0.5f)*2*hi*dens,grassh,1,1);
      grass.vertex(((noise(pos.x/dens*i,i)-0.5f)*2*wi+rndx*0.25f)*dens,((noise(pos.y/dens,i)-0.5f)*2*hi+rndy*0.25f)*dens,grassh-random(20,30),1,1);              
      grass.vertex(((noise(pos.x/dens*i,i)-0.5f)*2*wi+rndx*0.5f)*dens,((noise(pos.y/dens,i)-0.5f)*2*hi+rndy*0.5f)*dens,grassh,1,1);
      grass.vertex(((noise(pos.x/dens*i,i)-0.5f)*2*wi+rndx*0.375f)*dens,((noise(pos.y/dens,i)-0.5f)*2*hi+rndy*0.325f)*dens,grassh,1,1);
      grass.vertex(((noise(pos.x/dens*i,i)-0.5f)*2*wi+rndx*0.625f)*dens,((noise(pos.y/dens,i)-0.5f)*2*hi+rndy*0.625f)*dens,grassh-random(20,30),1,1);      
      grass.vertex(((noise(pos.x/dens*i,i)-0.5f)*2*wi+rndx*0.875f)*dens,((noise(pos.y/dens,i)-0.5f)*2*hi+rndy*0.875f)*dens,grassh,1,1);
      grass.vertex(((noise(pos.x/dens*i,i)-0.5f)*2*wi+rndx*0.75f)*dens,((noise(pos.y/dens,i)-0.5f)*2*hi+rndy*0.75f)*dens,grassh,1,1);
      grass.vertex(((noise(pos.x/dens*i,i)-0.5f)*2*wi+rndx*0.875f)*dens,((noise(pos.y/dens,i)-0.5f)*2*hi+rndy*0.875f)*dens,grassh-random(20,30),1,1);              
      grass.vertex(((noise(pos.x/dens*i,i)-0.5f)*2*wi+rndx)*dens,((noise(pos.y/dens,i)-0.5f)*2*hi+rndy)*dens,grassh,1,1);
      grass.endShape();
    }
    object.addChild(grass);
    
//------------------------------------------------------------------------------------------------------------------------------------------------------

    if(noise(this.pos.x/dens,this.pos.y/dens) <= flowerspawnrate){
      fpos.z = 1;
      fpos.x = noise(pos.x/dens,values[0][0]/multi*divider)*wi*dens;
      fpos.y = noise(pos.y/dens,values[0][0]/multi*divider)*wi*dens;
      flower = createFlower(fpos.x,fpos.y);
      object.addChild(flower);
    }
  
//------------------------------------------------------------------------------------------------------------------------------------------------------

    if(noise(this.pos.x/dens,this.pos.y/dens) <= cloudspawnrate){
      float a = (noise(this.pos.x*this.pos.y/dens,this.pos.y/dens)-0.5f)*wi*dens*2;
      float b = (noise(this.pos.x/dens,this.pos.y*this.pos.x/dens)-0.5f)*wi*dens*2;
      float ah = noise((this.pos.x/dens)/PApplet.parseFloat(divider),(this.pos.y/dens)/divider)*PApplet.parseFloat(multi)-random(2000,2500);
      int rnd = PApplet.parseInt(random(4,6));
      for(int i = 0; i < rnd; i++){
        object.addChild(createCloud(a+random(-400,400),b+random(-400,400),ah+random(-100,100),300+random(-50,50)));
      }
      //object.addChild(createCloud(a,b,ah,150));
    }
  }

//------------------------------------------------------------------------------------------------------------------------------------------------------

  public PShape createCloud(float a, float b, float c, float r){
    PShape cloud = createShape();
    PImage texture = createImage(1,1,RGB);
    texture.loadPixels();
    texture.pixels[0] = color(255,255,255);
    PVector[][] shape = new PVector[21][21];
    for(int i = 0; i < 21; i++){
      float lon = map(i,0,20,-PI,PI);
      for(int j = 0; j < 21; j++){
        float lat = map(j,0,20,-HALF_PI,HALF_PI);
        float x = r * sin(lon) * cos(lat);
        float y = r * sin(lon) * sin(lat);
        float z = r * cos(lon);
        float t = sqrt(x*x)+sqrt(y*y)+sqrt(z*z);
        shape[i][j] = new PVector(x+noise(x/r+r+a,y/r+r+b,z/r+r)*(x/t)*r*1.5f+a,y+noise(x/r+r+a,y/r+r+b,z/r+r)*(y/t)*r*1.5f+b,z+noise(x*2/r+r+a,y*2/r+r+b,z*2/r+r)*(z/t)*r/4+c);     
      }
    }  
    for(int i = 0; i < 20; i++){
      cloud.beginShape(TRIANGLE_STRIP);
      cloud.ambient(255);
      cloud.noStroke();
      cloud.texture(texture);
      for(int j = 0; j < 21; j++){
        cloud.vertex(shape[i][j].x,shape[i][j].y,shape[i][j].z,1,1);
        cloud.vertex(shape[i+1][j].x,shape[i+1][j].y,shape[i+1][j].z,1,1);
      }
      cloud.endShape();
    }
    return(cloud);
  }
  
//------------------------------------------------------------------------------------------------------------------------------------------------------

  public PShape createFlower(float x, float y){
    PShape f = createShape();
    PImage ftext = createImage(3,1,RGB);
    ftext.pixels[0] = color(10,255,50);
    ftext.pixels[1] = color(0,0,0);
    ftext.pixels[2] = color(255,0,0);
    f.beginShape(TRIANGLE_STRIP);
    f.noFill();
    f.noStroke();
    f.ambient(ftext.pixels[0]);
    f.texture(ftext);
    float ground = noise((this.pos.x/dens+x/dens)/divider,(this.pos.y/dens+y/dens)/divider)*multi;
    for(int i = 0; i < 4; i++){
      for(int r = 0; r < 6; r++){
        float xoff = 4 * cos(radians(r*72));
        float yoff = 4 * sin(radians(r*72));
        f.vertex(xoff+x,yoff+y,-i*15+ground,1,1);
        f.vertex(xoff+x,yoff+y,-(i+1)*15+ground,1,1);
      }
    }
    f.ambient(ftext.pixels[1]);
    for(int r = 0; r < 6; r++){
      f.vertex(x,y,-60+ground,2,1);
      float xoff = 15 * cos(radians(r*72));
      float yoff = 15 * sin(radians(r*72));
      f.vertex(xoff+x,yoff+y,-60+ground,2,1);      
    }
    f.ambient(ftext.pixels[2]);
    for(int r = 0; r < 6; r++){
      float xoff = 15 * cos(radians(r*72));
      float yoff = 15 * sin(radians(r*72));
      f.vertex(xoff+x,yoff+y,-60+ground,3,1);
      xoff = 30 * cos(radians(r*72));
      yoff = 30 * sin(radians(r*72));
      f.vertex(xoff+x,yoff+y,-70+ground,3,1);      
    }
    f.endShape();
    return f;
  }
  
//------------------------------------------------------------------------------------------------------------------------------------------------------

  public void display(){
    d3.noFill();
    d3.noStroke();
    d3.translate(this.pos.x,this.pos.y);
    d3.shape(object);
    d3.translate(-this.pos.x,-this.pos.y);
  }
  
//-----------------------------------------------------------------------------------------------------------------------------------------------------

  public void collisions(boolean interact){
    if(dist(pos.x,pos.y,ppos.x,ppos.y) < 2000){
      if(dist(ppos.x,ppos.y,tpos.x+pos.x,tpos.y+pos.y) < treehit && tpos.z == 1){
        ppos.x+=lastmove.x;
        ppos.y+=lastmove.y;
      }
      if(dist(ppos.x,ppos.y,fpos.x+pos.x,fpos.y+pos.y) < flowerhit && interact && fpos.z == 1 && flowerc < 5){
        object.removeChild(object.getChildIndex(flower));
        flowerc++;
        flowerstotal++;
        updateflower();
        fpos.z = 0;
      }
    }
  }
}

//------------------------------------------------------------------------------------------------------------------------------------------------------

public void initchunks(){
  chunkupdate();
  for(int x = 0; x < chunkdist/wi/dens; x++){
    for(int y = 0; y < chunkdist/wi/dens; y++){
      grounds.add(new ground(PApplet.parseInt(PApplet.parseInt(ppos.x)/(wi*dens))*(wi*dens)-chunkdist/2+x*wi*dens,PApplet.parseInt(PApplet.parseInt(ppos.y)/(wi*dens))*(wi*dens)-chunkdist/2+y*hi*dens,wi,hi,dens));
    }
  }
  chunkupdate();
}

//------------------------------------------------------------------------------------------------------------------------------------------------------

public void allcollision(){
  boolean interact = keyp[7];
  for(int i = 0; i < grounds.size(); i++){
    grounds.get(i).collisions(interact);
  }
  for(int i = 0; i < bosses.size(); i++){
    for(int j = 0; j < bosses.get(i).boxes.length; j++){
      if(dist(ppos.x,ppos.y,bosses.get(i).pos.x+bosses.get(i).boxes[j].x,bosses.get(i).pos.y+bosses.get(i).boxes[j].y) < 150){
        ppos.x+=lastmove.x;
        ppos.y+=lastmove.y;
      }
    }
  }
  updatedcollision = true;
}
  public void settings() {  fullScreen(P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Leveria_V0_7" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
