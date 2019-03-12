import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 
import java.awt.Robot; 
import java.awt.event.InputEvent; 
import java.awt.event.KeyEvent; 
import java.awt.Component; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Leveria_V1_0 extends PApplet {


SoundFile death;
SoundFile deathmob;
SoundFile pain;
SoundFile pursuit;
SoundFile attack;
SoundFile necroattack;
SoundFile slam;
SoundFile forge;
SoundFile ambient;
SoundFile portalhum;
SoundFile whispering; 
SoundFile teleport;
SoundFile wind;
SoundFile fpickup;
SoundFile heal;
SoundFile keytap;
SoundFile painplayer;
SoundFile spickup;
SoundFile open;
SoundFile close;
SoundFile paino;




Robot robot;
int[] xy = new int[2];
int seed;
int dens = 100;
int wi = 60;
int hi = 60;
int multi = 10000;
int divider = 300;
int chunkdist = 6*(wi*dens); //how many chunks to render in each direction.
float treespawnrate = 1;
int grassdens = 1800; //amount of grass per chunk.
int treecount = 18; //make even number 
float flowerspawnrate = 1;
int flowercount = 4;
float cloudspawnrate = 0.8f;
ArrayList<ground> grounds = new ArrayList<ground>();
PGraphics d3;
PGraphics bloomp;
PGraphics fogp;
PGraphics firefliesgraphic;
PShader bloom;
PShader fog;
PShader tints;
PShader floorlight;
public void setup(){
  noiseDetail(5);
  try {
    robot = new Robot();
    robot.setAutoDelay(0);
  } 
  catch (Exception e) {
    println(e.getMessage());
  }
  getWindowLocation(this, xy);
  print(xy[0] + "," + xy[1]);
  robot.mouseMove(xy[0]+width/2,xy[1]+height/2);
  firefliesmodel = createShape(PShape.GROUP);
  d3 = createGraphics(1920,1080,P3D);
  ui = createGraphics(1920,1080,P2D);
  bloomp = createGraphics(640,380,P2D);
  fogp = createGraphics(1632,918,P2D);
  groundshader = createGraphics(15000,3000,P2D);
  f1 = createGraphics(10,10,P2D);
  f2 = createGraphics(10,10,P2D);
  f3 = createGraphics(10,10,P2D);
  d3.smooth(16);
  paino = new SoundFile(this, "sounds/paino.wav");
  heal = new SoundFile(this, "sounds/heal.wav");
  open = new SoundFile(this, "sounds/open.wav");
  close = new SoundFile(this, "sounds/close.wav");
  death = new SoundFile(this, "sounds/death.wav");
  deathmob = new SoundFile(this, "sounds/deathmob.wav");
  pain = new SoundFile(this, "sounds/painsoundshade.wav");
  painplayer = new SoundFile(this, "sounds/pain.wav");
  pursuit = new SoundFile(this, "sounds/pursuit2.wav");
  attack = new SoundFile(this, "sounds/attack.wav");
  necroattack = new SoundFile(this, "sounds/necromancerattack.wav");
  slam = new SoundFile(this, "sounds/slam.wav");
  forge = new SoundFile(this, "sounds/forge.wav");
  ambient = new SoundFile(this, "sounds/ambientsound.wav");
  portalhum = new SoundFile(this, "sounds/Portalhum.wav");
  whispering = new SoundFile(this, "sounds/whispering.wav");
  teleport = new SoundFile(this, "sounds/teleport.wav");
  wind = new SoundFile(this, "sounds/wind2.wav");
  fpickup = new SoundFile(this, "sounds/flowerpickup.wav");
  spickup = new SoundFile(this, "sounds/scroll.wav");
  keytap = new SoundFile(this, "sounds/keytap.wav");
  wind.loop();
  portalhum.loop();
  portalhum.amp(0);
  whispering.loop();
  whispering.amp(0);
  wind.amp(0);
  ambient.loop();
  teleport.amp(1.5f);
  pain.amp(1.5f);
  corner = loadImage("images/corner.png");
  flower = loadImage("images/poppy.png");
  swordph = loadImage("images/swordph.png");
  sworda = loadImage("images/sworda.png");
  swordb = loadImage("images/swordb.png");
  swordc = loadImage("images/swordc.png");
  swordd = loadImage("images/swordd.png");
  shielda = loadImage("images/shielda.png");
  shieldb = loadImage("images/shieldb.png");
  shieldc = loadImage("images/shieldc.png");
  shield = loadImage("images/shield.png");
  shieldph = loadImage("images/shieldph.png");
  bin = loadImage("images/bin.png");
  anvil = loadImage("images/anvil.png");
  hammer = loadImage("images/hammer.png");
  inventorytxt = loadImage("images/inventory.png");
  cloudtext = loadImage("images/cloudtext.png");
  statstxt = loadImage("images/stats.png");
  forgetxt = loadImage("images/forge.png");
  tutorial = loadImage("images/tutorial.png");
  fireimg1 = loadImage("images/firefly1.png");
  fireimg2 = loadImage("images/firefly2.png");
  fireimg3 = loadImage("images/firefly3.png");
  portal1 = loadImage("images/portal1.png");
  portal2 = loadImage("images/portal2.png");
  portal3 = loadImage("images/portal3.png");
  menusgoff = loadImage("images/sgoff.png");
  menusgon = loadImage("images/sgon.png");
  menungoff = loadImage("images/ngoff.png");
  menungon = loadImage("images/ngon.png");
  menuegoff = loadImage("images/egoff.png");
  menuegon = loadImage("images/egon.png");
  mswordd = loadshape("models/swordd.obj","models/swordd.png");
  mswordc = loadshape("models/swordc.obj","models/swordc.png");
  mswordb = loadshape("models/swordb.obj","models/swordbtext.png");
  msworda = loadshape("models/sworda.obj","models/swordatext.png");
  peoplemodels[0] = loadshape("models/people1.obj","models/people1.png");
  peoplemodels[1] = loadshape("models/people2.obj","models/people2.png");
  peoplemodels[2] = loadshape("models/people3.obj","models/people3.png");
  shade = loadshape("models/wraith.obj","models/wraith.png");
  shadspn = loadshape("models/thrall.obj","models/thrall.png");
  clawL = loadshape("models/ClawLeft.obj","models/projtext.png");
  clawR = loadshape("models/ClawRight.obj","models/projtext.png");
  mboss = loadshape("models/cultist.obj","models/cultist.png");
  mproj = loadshape("models/proj.obj","models/projtext.png");
  mdrop = loadShape("models/scroll.obj");
  overloardshoot = loadshape("models/overlord1.obj","models/overlord.png");
  overloardshoot.scale(15);
  overloardslam = loadshape("models/overlord2.obj","models/overlord.png");
  overloardslam.scale(15);
  bloom = loadShader("shaders/bloom.frag");
  fog = loadShader("shaders/fog.frag");
  tints = loadShader("shaders/tint.frag");
  floorlight = loadShader("shaders/groundshader.frag");
  peoplemodels[0].scale(15);
  peoplemodels[1].scale(15);
  peoplemodels[2].scale(15);
  shade.scale(15);
  shadspn.scale(10);
  clawL.scale(10);
  clawR.scale(10);
  mboss.scale(15);
  mproj.scale(8,8,4);
  msworda.scale(4,4,1.5f);
  mswordb.scale(4,4,1.5f);
  mswordc.scale(3,4,1.5f);
  mswordd.scale(4,5,1.5f);
  mdrop.scale(3.5f,4,2.5f); 
  portalpos = new PVector(0,0,-5000);
  portal = createShape();
  portal.beginShape(QUAD);
  portal.noStroke();
  portal.texture(portal1);
  portal.normal(0, 0, 1);
  portal.vertex(-300, -300, 0, 0);
  portal.vertex(+300, -300, 300, 0);
  portal.vertex(+300, +300, 300, 300);
  portal.vertex(-300, +300, 0, 300);
  portal.endShape(); 
  portal.rotateX(radians(90));
  intro = loadStrings("intro.txt");
  bossoutro = loadStrings("outro.txt");
  credits = loadStrings("credits.txt");
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
    ppos.z = 0;
    posheight = noise(ppos.x/PApplet.parseFloat(dens)/PApplet.parseFloat(divider),ppos.y/PApplet.parseFloat(dens)/PApplet.parseFloat(divider))*PApplet.parseFloat(multi);
    lastz = posheight;
    seed = PApplet.parseInt(inven[24]);
    health = PApplet.parseFloat(inven[25]);
    time = PApplet.parseFloat(inven[26]);
    mouse.x = PApplet.parseFloat(inven[27]);
    mouse.y = PApplet.parseFloat(inven[28]);
    sense = PApplet.parseFloat(inven[29]);
  }catch(Exception e){
    for(int x = 0; x < 5; x++){
      for(int y = 0; y < 3; y++){
        inventory[x][y] = new item();
      }
    }
    lefthand = new item(sworda,10,0.5f,msworda);
    inarmour = new item(shielda,10);
    seed = PApplet.parseInt(random(10000));
    ppos.x = 100000+PApplet.parseInt(random(100000));
    ppos.y = 100000+PApplet.parseInt(random(100000));
  }
  updatehp();
  updateflower();
  updatelefthand();
  updaterighthand();
  noiseSeed(seed); 
  noCursor();
  initchunks();
}

public void settings(){
  fullScreen(P3D,1);
  PJOGL.setIcon("images/sketch.png");
}

public void draw(){
  if(menu){
    cloudtextmanager();
    drawmenu();
  }else if(iintro){
    runintro();
  }else if(ibossoutro){
    runbossoutro();
  }else if(icredits){
    runcredits();
  }else if(itutorial){
    image(tutorial,0,0,width,height);
  }else if(iopen){
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
  }else if(entered){
    if(health == 0){
      deathanimation();
    }
    cooldowns();
    attackanimation();
    drawend();
    bloomp.beginDraw();
    bloomp.clear();
    bloomp.image(d3,0,0,640,380);
    bloomp.endDraw();
    bloom.set("skyr",red(currentsky)/PApplet.parseFloat(255),green(currentsky)/PApplet.parseFloat(255),blue(currentsky)/PApplet.parseFloat(255));
    bloomp.filter(bloom);
    background(0);
    image(d3,0,0,width,height);
    image(bloomp,0,0,width,height);
    image(ui,0,0,width,height);
  }else{
    if(health == 0){
      deathanimation();
    }
    try{
      if(frameCount%30 == 0){
        for(int i = 0; i < bosses.size(); i++){
          bosses.get(i).checktrees();
        }
      }
    }catch(Exception e){
    }
    cooldowns();
    attackanimation();
    draw3d();
    bloomp.beginDraw();
    bloomp.clear();
    bloomp.image(d3,0,0,640,380);
    bloomp.endDraw();
    bloom.set("skyr",red(currentsky)/PApplet.parseFloat(255),green(currentsky)/PApplet.parseFloat(255),blue(currentsky)/PApplet.parseFloat(255));
    bloomp.filter(bloom);
    if(km+kb == 25){
      fcd++;
      tints.set("alpha",fcd/180.0f);
      if(fcd == 180){
        icredits = true;
      }
      d3.filter(tints);
      bloomp.filter(tints);
      background(0);
    }
    image(d3,0,0,width,height);
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
      float x1 = grounds.get(i).pos.x-ppos.x+wi*dens/2;
      float y1 = grounds.get(i).pos.y-ppos.y+wi*dens/2;
      if(dist(x,y,x1,y1) < chunkdist*0.75f){
        grounds.get(i).display();
      }
    }catch(Exception e){
    }
  }
  managedrops();
  managemobs();
  if(bossdead == false && portalpos.z != -5000){
    displayportal();
  }
  manageparticles();
  if(time >= 150 && time <= 360){
    managefireflies();
  }else if(time > 10 && time < 140){
    wind();
  }
  if(movedpf){
    movedpf = false;
    thread("movepf");
  }
  d3.translate(ppos.x,ppos.y,posheight-lastz); 
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


public static final int[] getWindowLocation(final PApplet pa, int... xy) {
  if (xy == null || xy.length < 2)  xy = new int[2];
 
  final Object surf = pa.getSurface().getNative();
  final PGraphics canvas = pa.getGraphics();
 
  if (canvas.isGL()) {
    xy[0] = ((com.jogamp.nativewindow.NativeWindow) surf).getX();
    xy[1] = ((com.jogamp.nativewindow.NativeWindow) surf).getY();
  } else if (canvas instanceof processing.awt.PGraphicsJava2D) {
    final java.awt.Component f =
      ((processing.awt.PSurfaceAWT.SmoothCanvas) surf).getFrame();
 
    xy[0] = f.getX();
    xy[1] = f.getY();
  } else try {
    final java.lang.reflect.Method getStage =
      surf.getClass().getDeclaredMethod("getStage");
 
    getStage.setAccessible(true);
 
    final Object stage = getStage.invoke(surf);
    final Class<?> st = stage.getClass();
 
    final java.lang.reflect.Method getX = st.getMethod("getX");
    final java.lang.reflect.Method getY = st.getMethod("getY");
 
    xy[0] = ((Number) getX.invoke(stage)).intValue();
    xy[1] = ((Number) getY.invoke(stage)).intValue();
  }
  catch (final ReflectiveOperationException e) {
    System.err.println(e);
  }
 
  return xy;
}
float time = 0;
int sunset = color(255,84,34);
int day = color(10,100,250);
int night = color(0,0,0);
int light = color(255,245,245);
int currentsky;
int daycount = 0;
public void sky(){
  if(time >= 120 && time <= 180){
    d3.background(lerpColor(day,night,(time-120)/60));
  }else if(time < 150){
    d3.background(day);
  }else if(time >= 300){
    d3.background(lerpColor(night,day,(time-300)/60));
  }else{
    d3.background(night);
  }
  d3.rotateX(radians(time-40));
  d3.translate(0,0,-500);
  d3.fill(255,174,68);
  d3.sphere(25);
  d3.translate(0,0,500);
  d3.translate(0,0,500);
  d3.fill(255);
  d3.sphere(25);
  d3.translate(0,0,-500);
  d3.rotateX(radians(-time+40));
}
//Change ambient depending on the day time. 
public void sunmoonlight(){
  d3.directionalLight(15,15,15,0,0,1);
  //d3.ambientLight(10,10,10);
  d3.rotateX(radians(time+90));
  if(time >= 150 && time < 180){
    d3.directionalLight(constrain((time-150)/30*10,0,10),constrain((time-150)/30*20,0,10),constrain((time-150)/30*20,0,10),0,0,-1);
  }else if(time > 300){
    d3.directionalLight(constrain((330-time)/30*10,0,10),constrain((330-time)/30*20,0,10),constrain((330-time)/30*20,0,10),0,0,-1);
  }else if(time > 180 && time < 300){
    d3.directionalLight(10,10,10,0,0,-1);
  }
  if(time >= 150 && time <= 180){
    d3.directionalLight(255-constrain((time-150)/30*255,0,255),255-constrain((time-150)/30*255,0,255),255-constrain((time-150)/30*255,0,255),0,0,1);
    d3.ambientLight((255-constrain((time-150)/30*255,0,255))/4+10,(255-constrain((time-150)/30*255,0,255))/4+10,(255-constrain((time-150)/30*255,0,255))/4+10);
    currentsky = lerpColor(day,night,(time-150)/30);
  }else if(time < 150){
    d3.directionalLight(255,255,255,0,0,1);
    d3.ambientLight(255.0f/4+10,255.0f/4+10,255.0f/4+10);
    currentsky = day;
  }else if(time >= 330){
    d3.directionalLight((time-330)/30*255,(time-330)/30*255,(time-330)/30*255,0,0,1);
    d3.ambientLight(((time-330)/30*255)/4+10,((time-330)/30*255)/4+10,((time-330)/30*255)/4+10);
    currentsky = lerpColor(night,day,(time-330)/30);
  }else{
    currentsky = night;
    d3.ambientLight(10,10,10);
  }
  d3.rotateX(-radians(time+90));
  time+=0.03f;
  if(time > 359){
    time = time-360;
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
        float dmg = round(random(31+constrain(daycount,0,20)*2,50+constrain(daycount,0,20)*2));
        drop = new item(swordc,dmg,round(100*(dmg/(40+constrain(daycount,0,20)*2)/2))/PApplet.parseFloat(100),mswordc);
      }else{
        drop = new item(shieldc,round(constrain(random(40+constrain(daycount,0,39),60+constrain(daycount,0,39)),40,99)));
      }
    }else{
      if(random(1) >= 0.25f){
        if(random(1) >= 0.5f){
          float dmg = random(5+daycount/5,15+daycount/5);
          drop = new item(sworda,round(dmg),round(100*(dmg/(10+constrain(daycount,0,10))))/PApplet.parseFloat(100),msworda);
        }else{
          float dmg = random(16+daycount/5,30+daycount/5);
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
      if(entered){
        if(dist(ppos.x,ppos.y,bosspos.x,bosspos.y) < 500 && bosshp > 0){
          bosshp-=righthand.stat;
          paino.stop();
          paino.play();
          if(bosshp <= 0){
            deadcount++;
            bossdead = true;
            whispering.amp(0);
            for(int i = shadowspawns.size()-1; i > -1; i--){
              shadowspawns.remove(i);
            }
            mproj.scale(0.5f);
          }
          if(stage == 0){
            stage = 1;
            bosspos.x = 0;
          }else if(stage == 1){
            stage = 2;
            bosspos.z = -5000;
          }
        }
        for(int i = 0; i < shadowspawns.size(); i++){
          float x1 = shadowspawns.get(i).pos.x;
          float y1 = shadowspawns.get(i).pos.y;
          float r = 140;
          float x = r * cos(radians(mouse.x));
          float y = r * sin(radians(mouse.x));
          if(dist(ppos.x-x,ppos.y-y,x1,y1) <  300){
            shadowspawns.get(i).hp-=righthand.stat;
            pain.stop();
            pain.play();
            break;
          }          
        }
      }else{
        for(int i = 0; i < mobs.size(); i++){
          float x1 = mobs.get(i).pos.x;
          float y1 = mobs.get(i).pos.y;
          float r = 140;
          float x = r * cos(radians(mouse.x));
          float y = r * sin(radians(mouse.x));
          if(dist(ppos.x-x,ppos.y-y,x1,y1) < 300){
            mobs.get(i).hp-=righthand.stat;
            if(mobs.get(i).hp > 0 && random(1) >= 0.75f){
              particles.add(new particlesystem(new PVector(mobs.get(i).pos.x,mobs.get(i).pos.y,mobs.get(i).pos.z-100),20, 50, 0, 0.05f, 120, 20, 20, color(79,9,104),0,0,360));
              float ro = bearing(ppos.x,ppos.y,mobs.get(i).pos.x,mobs.get(i).pos.y)+radians(random(-120,120));
              float dis = random(-2500,-1500);
              mobs.get(i).pos.x+=dis*cos(ro);
              mobs.get(i).pos.y+=dis*sin(ro);
              teleport.stop();
              teleport.play();
            }
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
          if(dist(ppos.x-x,ppos.y-y,x1,y1) < 300){
            bosses.get(i).hp-=righthand.stat;
            pain.stop();
            pain.play();
          }        
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
      if(entered){
        if(dist(ppos.x,ppos.y,bosspos.x,bosspos.y) < 500 && bosshp > 0){
          bosshp-=lefthand.stat;
          paino.stop();
          paino.play();
          if(bosshp <= 0){
            deadcount++;
            bossdead = true;
            whispering.amp(0);
            for(int i = shadowspawns.size()-1; i > -1; i--){
              shadowspawns.remove(i);
            }
            mproj.scale(0.5f);
          }
         if(stage == 0){
            stage = 1;
            bosspos.x = 500;
          }else if(stage == 1){
            stage = 2;
            bosspos.z = 5000;
          }
        }
        for(int i = 0; i < shadowspawns.size(); i++){
          float x1 = shadowspawns.get(i).pos.x;
          float y1 = shadowspawns.get(i).pos.y;
          float r = 140;
          float x = r * cos(radians(mouse.x));
          float y = r * sin(radians(mouse.x));
          if(dist(ppos.x-x,ppos.y-y,x1,y1) < 300){
            shadowspawns.get(i).hp-=lefthand.stat;
            pain.stop();
            pain.play();
            break;
          }  
        }
      }else{
        for(int i = 0; i < mobs.size(); i++){
          float x1 = mobs.get(i).pos.x;
          float y1 = mobs.get(i).pos.y;
          float r = 140;
          float x = r * cos(radians(mouse.x));
          float y = r * sin(radians(mouse.x));
          if(dist(ppos.x-x,ppos.y-y,x1,y1) < 300){
            mobs.get(i).hp-=lefthand.stat;
            if(mobs.get(i).hp > 0 && random(1) >= 0.75f){
              particles.add(new particlesystem(new PVector(mobs.get(i).pos.x,mobs.get(i).pos.y,mobs.get(i).pos.z-100),20, 50, 0, 0.05f, 120, 20, 20, color(79,9,104),0,0,360));
              float ro = bearing(ppos.x,ppos.y,mobs.get(i).pos.x,mobs.get(i).pos.y)+radians(random(-120,120));
              float dis = random(-2500,-1500);
              mobs.get(i).pos.x+=dis*cos(ro);
              mobs.get(i).pos.y+=dis*sin(ro);
              teleport.stop();
              teleport.play();
            }
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
          if(dist(ppos.x-x,ppos.y-y,x1,y1) < 300){
            bosses.get(i).hp-=lefthand.stat;
            pain.stop();
            pain.play();
          }        
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
      spickup.stop();
      spickup.play();
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
    if(death.isPlaying() == false){
      death.play();
    }
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
  if(mouseX < 190*(width/1920.0f) && mouseX > 110*(width/1920.0f) && mouseY < 1040*(height/1080.0f) && mouseY > 960*(height/1080.0f)){
    if(inhand.weapon && mousePressed && cused == false && lefthand.empty){
      lefthand = inhand;
      inhand = new item();
      cused = true;
    }else if(inhand.empty && mousePressed && cused == false && lefthand.empty == false){
      inhand = lefthand;
      lefthand = new item();
      cused = true;
    }else if(mousePressed && inhand.empty == false && lefthand.empty == false && cused == false && inhand.weapon){
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
  if(mouseX < 280*(width/1920.0f) && mouseX > 200*(width/1920.0f) && mouseY < 1040*(height/1080.0f) && mouseY > 960*(height/1080.0f)){
    if(inhand.weapon && mousePressed && cused == false && righthand.empty){
      righthand = inhand;
      inhand = new item();
      cused = true;
    }else if(inhand.empty && mousePressed && cused == false && righthand.empty == false){
      inhand = righthand;
      righthand = new item();
      cused = true;
    }else if(mousePressed && inhand.empty == false && righthand.empty == false && cused == false && inhand.weapon){
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
  if(mouseX < 370*(width/1920.0f) && mouseX > 290*(width/1920.0f) && mouseY < 1040*(height/1080.0f) && mouseY > 960*(height/1080.0f)){
    if(inhand.weapon == false && mousePressed && cused == false && inarmour.empty){
      inarmour = inhand;
      inhand = new item();
      cused = true;
    }else if(inhand.empty && mousePressed && cused == false && inarmour.empty == false){
      inhand = inarmour;
      inarmour = new item();
      cused = true;
    }else if(mousePressed && inhand.empty == false && inarmour.empty == false && cused == false && inhand.weapon == false){
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
public void updatekillcount(){
  ui.beginDraw();
  ui.stroke(0);
  ui.strokeWeight(4);
  ui.fill(50);
  qud(1840,960,40,40);
  ui.fill(255);
  ui.text(str(km+kb)+"/25",1820,1007);
  ui.endDraw();
}
public void updateui(){
  ui.beginDraw();
  ui.clear();
  ui.endDraw();
  updatehp();
  updateflower();
  updatelefthand();
  updaterighthand();
  updatearmour();
  if(bossdead){
    updatekillcount();
  }
}
public void openinventory(){
  updateui();
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
  ui.image(forgetxt,1400,380,200,200);
  for(int x = 0; x < 5; x++){
    for(int y = 0; y < 3; y++){
      if(mouseX < (250+x*220+40)*(width/1920.0f) && mouseX > (250+x*220-40)*(width/1920.0f) && mouseY > (290+y*220)*(height/1080.0f) && mouseY < (290+y*220+80)*(height/1080.0f)){
        if(inventory[x][y].empty == false){
          ui.rect(250+x*220+40,290+y*220,140,105);
          ui.fill(0);
          if(inventory[x][y].weapon){
            if(inventory[x][y].img == sworda){
              ui.text("Wooden Sword",255+x*220+40,290+y*220,390+x*220+40,50+y*220+80);
            }else if(inventory[x][y].img == swordb){
              ui.text("Silver Sword",255+x*220+40,290+y*220,390+x*220+40,50+y*220+80);
            }else if(inventory[x][y].img == swordc){
              ui.text("Golden Rapier",255+x*220+40,290+y*220,390+x*220+40,50+y*220+80);
            }else if(inventory[x][y].img == swordd){
              ui.text("Damocles Sword",255+x*220+40,290+y*220,390+x*220+40,50+y*220+80);
            }
            ui.text("Damage : " + inventory[x][y].stat,255+x*220+40,330+y*220);
            ui.text("Speed : " + round(inventory[x][y].cdreset*100)/PApplet.parseFloat(100) + "s",255+x*220+40,355+y*220);
            ui.text("Value : " + PApplet.parseInt((inventory[x][y].stat*10)/(inventory[x][y].cdreset)) + " SF",255+x*220+40,380+y*220);
          }else{
            if(inventory[x][y].img == shielda){
              ui.text("Wooden Shield",255+x*220+40,290+y*220,390+x*220+40,50+y*220+80);
            }else if(inventory[x][y].img == shieldb){
              ui.text("Brass Shield",255+x*220+40,290+y*220,390+x*220+40,50+y*220+80);
            }else if(inventory[x][y].img == shieldc){
              ui.text("Divine Shield",255+x*220+40,290+y*220,390+x*220+40,50+y*220+80);
            }
            ui.text("Value : " + PApplet.parseInt(inventory[x][y].stat*10) + " SF",255+x*220+40,355+y*220);
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
  if(mouseX < (1500+40)*(width/1920.0f) && mouseX > (1500-40)*(width/1920.0f) && mouseY > 780*(height/1080.0f) && mouseY < (780+80)*(height/1080.0f)){
    ui.fill(100);
    if(mousePressed && cused == false && inhand.empty == false){
      if(inhand.weapon){
        points+=PApplet.parseInt((inhand.stat*10)/(inhand.cdreset));
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
  if(mouseX < (1500+40)*(width/1920.0f) && mouseX > (1500-40)*(width/1920.0f) && mouseY > 680*(height/1080.0f) && mouseY < (680+80)*(height/1080.0f)){
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
      cost=PApplet.parseInt((inforge.stat*10+damagec*10)/(inforge.cdreset-cdc));
    }else{
      cost=PApplet.parseInt((armourc+inforge.stat)*10);
    }
    ui.text("Cost : " + cost + " SF",1300,660);
  }else{
    cost = 0;
  }
  ui.fill(50);
  if(mouseX < (1600+20)*(width/1920.0f) && mouseX > (1600-20)*(width/1920.0f) && mouseY > 510*(height/1080.0f) && mouseY < 550*(height/1080.0f) && inforge.weapon == false){
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
  if(mouseX < (1600+20)*(width/1920.0f) && mouseX > (1600-20)*(width/1920.0f) && mouseY > 550*(height/1080.0f) && mouseY < 590*(height/1080.0f) && inforge.weapon){
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
  if(mouseX < (1600+20)*(width/1920.0f) && mouseX > (1600-20)*(width/1920.0f) && mouseY > 590*(height/1080.0f) && mouseY < 630*(height/1080.0f) && inforge.weapon){
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
  if(mouseX < (1680+40)*(width/1920.0f) && mouseX > (1680-40)*(width/1920.0f) && mouseY > 530*(height/1080.0f) && mouseY < 610*(height/1080.0f) && cused == false && cost <= points){
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
      forge.stop();
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
  if(mouseX > 940*(width/1920.0f) && mouseX < 980*(width/1920.0f) && mouseY > 65*(height/1080.0f) && mouseY < 105*(height/1080.0f)){
    sense+=wheel*0.01f;
    sense = round(sense*100)/PApplet.parseFloat(100);
    sense = constrain(sense,0.01f,1);
    wheel = 0;
    ui.fill(100);
  }
  qud(960,65,20,20);
  ui.fill(50);
  if(mouseX > 756*(width/1920.0f) && mouseX < 796*(width/1920.0f) && mouseY > 940*(height/1080.0f) && mouseY < 980*(height/1080.0f)){ //save
    ui.fill(100);
    if(mousePressed && cused == false){
      saveinventory();
      
      cused = true;
    }
  }
  qud(776,940,20,20);
  ui.fill(50);
  if(mouseX > 1010*(width/1920.0f) && mouseX < 1050*(width/1920.0f) && mouseY > 940*(height/1080.0f) && mouseY < 980*(height/1080.0f)){ //savequit
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
  heal.stop();
  heal.play();
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
  d3.perspective(PI/3.0f, PApplet.parseFloat(width)/PApplet.parseFloat(height), (height/2.0f) / tan(PI/3.0f/2.0f)/20.0f, (height/2.0f) / tan(PI/3.0f/2.0f)*7.5f);
  d3.camera(0,0,-ch,-x,-y,-ch+z,rz,0,1-rz);
}
public void camend(){
  int r = 300;
  float x = r * cos(radians(mouse.x));
  float y = r * sin(radians(mouse.x));
  float z = r * cos(radians(mouse.y));
  d3.perspective(PI/3.0f, PApplet.parseFloat(width)/PApplet.parseFloat(height), (height/2.0f) / tan(PI/3.0f/2.0f)/60.0f, (height/2.0f) / tan(PI/3.0f/2.0f)*100.0f);
  if(stage == 2 && stuck && bcd > bcdreset-1){
    d3.camera(0,0,-ch,-x,-y,-ch+z,rz+cos(bcd*50)/10.0f,0,1-rz);  
  }else{
    d3.camera(0,0,-ch,-x,-y,-ch+z,rz,0,1-rz);  
  }
}
boolean updatedchunks = true;
PVector lastchunk = new PVector(0,0);
public void chunkupdate(){
  int cx = PApplet.parseInt(PApplet.parseInt(ppos.x-PApplet.parseInt(wi*dens/2))/(wi*dens))*(wi*dens)-wi*dens;
  int cy = PApplet.parseInt(PApplet.parseInt(ppos.y+PApplet.parseInt(wi*dens/2))/(hi*dens))*(hi*dens)-wi*dens*2;
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
  cloudtextmanager();
  updatedchunks = true;
}
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
public void displayportal(){
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
      particles.add(new particlesystem(new PVector(portalpos.x+x,portalpos.y+y,portalpos.z-300),100, 100, -0.05f, 0.05f, 60, 20, 20, color(10,10,10),0,0,360));
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
      shadowspawns.add(new shadowspawn(new PVector(x*600,0,-160),false,2+(x/25.0f)*3));
      shadowspawns.add(new shadowspawn(new PVector(x*600,3000,-160),false,2+(x/25.0f)*3));
    }
  }
}
public void drawend(){
  if(frameCount%120 == 0 && stage > 0){
    shadowspawns.add(new shadowspawn(new PVector(random(15000),random(3000),-160),true,5));
  }
  whispering.amp(constrain(shadowspawns.size()/50.0f,0,1));
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
    displaymodel(righthand.model,-30+righthandr.x/PApplet.parseFloat(4),140+righthandr.x/PApplet.parseFloat(9),righthandr.x,-righthandr.x/PApplet.parseFloat(5),0);
  }
  if(lefthand.empty == false){
    displaymodel(lefthand.model,30-lefthandr.x/PApplet.parseFloat(4),140+lefthandr.x/PApplet.parseFloat(9),lefthandr.x,lefthandr.x/PApplet.parseFloat(5),0);
  } 
  if(deadcount!=0){
    tints.set("alpha",(180-deadcount)/180.0f);
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
public void lighttexture(){
  groundshader.beginDraw();
  groundshader.image(groundshader,0,0,15000,3000);
  if(stage == 2){
    if(bosspos.z < 0){
      groundshader.noFill();
      groundshader.stroke(255);
      groundshader.strokeWeight(3);
      groundshader.ellipse(bosspos.x,3000-bosspos.y,bossvel/70.0f*625,bossvel/70.0f*625);
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
  public void display(){
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
              health-=50*(1.0f-inarmour.stat/100.0f);
            }else{
              health-=50;
            }
            painplayer.stop();
            painplayer.play();
            particles.add(new particlesystem(new PVector(ppos.x,ppos.y,posheight),200, 100, -0.02f, 0.005f, 60, 6, 6, color(255,255,255),0,270,360));
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
public void bossattacks(){
  if(stage == 2){
    if(bosspos.z > 0){
      bosspos.z = 0;
      particles.add(new particlesystem(new PVector(bosspos.x,bosspos.y,bosspos.z-100),100, 50, -0.02f, 0.005f, 60, 5, 5, color(255,255,255),0,0,360));
      stuck = true;
      slam.stop();
      slam.play();
      bossvel = 0;
      if(dist(ppos.x,ppos.y,bosspos.x,bosspos.y) < 625){
        if(inarmour.empty == false){
          health-=constrain((1200-dist(ppos.x,ppos.y,bosspos.x,bosspos.y))*0.5f*(1.0f/inarmour.stat),0,200);
          updatehp();
        }else{
          health-=constrain(1200-dist(ppos.x,ppos.y,bosspos.x,bosspos.y)*0.5f,0,200);
          updatehp();
        }
      }
    }
    if(bosspos.z < 0){
      bosspos.z+=bossvel;
      bossvel+=3*((1-(bosshp/10000.0f))*4);
    }else if(bcd < 0){
      bcdreset = random(3,6);
      bcd = bcdreset;
      stuck = false;
      particles.add(new particlesystem(new PVector(bosspos.x,bosspos.y,bosspos.z-100),50, 50, 0, 0.05f, 120, 20, 20, color(79,9,104),0,0,360));
      bosspos.z = -5000;
      bosspos.x = ppos.x;
      bosspos.y = ppos.y;
    }else if(stuck){
      bcd-=0.01666f;
    }
  }else if(stage == 1){
    if(bcd < 0){
      float x = 240 * (cos(bearing(bosspos.x,bosspos.y,ppos.x,ppos.y)-HALF_PI-radians(50)));
      float y = 240 * (sin(bearing(bosspos.x,bosspos.y,ppos.x,ppos.y)-HALF_PI-radians(50)));
      bossproj.add(new proj(new PVector(bosspos.x-x,bosspos.y-y,bosspos.z-160),ppos));
      necroattack.stop();
      necroattack.play();
      bcdreset = random(0.15f,1);
      bcd = bcdreset;
    }else{
      bcd-=0.01666f;
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
        health-=80*(1.0f-inarmour.stat/100.0f);
      }
      painplayer.stop();
      painplayer.play();
      particles.add(new particlesystem(new PVector(ppos.x,ppos.y,posheight),200, 100, -0.02f, 0.005f, 60, 6, 6, color(255,255,255),0,270,360));
      updatehp();
    }else if(bossproj.get(i).pos.x > 16000 || bossproj.get(i).pos.x < -1000 || bossproj.get(i).pos.y > 4000 || bossproj.get(i).pos.y < -1000){
      bossproj.remove(i);
    }
  }
  d3.translate(ppos.x,ppos.y,-ppos.z);
}
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
    pos.z = noise((pos.x/dens)/PApplet.parseFloat(divider),(pos.y/dens)/divider)*PApplet.parseFloat(multi);
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
  public void move(){
    animation();
    vel.x = lerp(vel.x,velf.x,0.01f);
    vel.y = lerp(vel.y,velf.y,0.01f);
    vel.z = lerp(vel.z,velf.z,0.01f);
    float x = vel.x * cos(radians(vel.y));
    float y = vel.x * sin(radians(vel.y));
    pos.x+=x;
    pos.y+=y;
    pos.z=lerp(pos.z,noise((pos.x/dens)/PApplet.parseFloat(divider),(pos.y/dens)/divider)*PApplet.parseFloat(multi),0.1f);    
    float x1 = -chunkdist*0.3f * cos(radians(mouse.x));
    float y1 = -chunkdist*0.3f * sin(radians(mouse.x));
    float x2 = pos.x-ppos.x;
    float y2 = pos.y-ppos.y;
    if(dist(x1,y1,x2,y2) > chunkdist*0.3f){
      float rnd = random(chunkdist*0.6f);
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
      pos.z = noise((pos.x/dens)/PApplet.parseFloat(divider),(pos.y/dens)/divider)*PApplet.parseFloat(multi);
      vel = new PVector(random(2,5),random(360),random(10,500));
      velf = new PVector(random(2,5),random(360),random(10,500));
    }
    if(dist(pos.x,pos.y,ppos.x,ppos.y) > chunkdist*0.3f){
      float deg = mouse.x + random(-50,50) + 180;
      float dis = random(100,5000);
      x = dis * cos(radians(deg));
      y = dis * sin(radians(deg));
      pos = new PVector(ppos.x+x,ppos.y+y,0);
      pos.z = noise((pos.x/dens)/PApplet.parseFloat(divider),(pos.y/dens)/divider)*PApplet.parseFloat(multi);
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
  public void animation(){
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
public void managefireflies(){
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

public PShape loadshape(String path, String textu){
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
          int xu = PApplet.parseInt(u * tex.width);
          int yv = PApplet.parseInt(v * tex.height);
          int index = xu + yv * tex.width;
          if((red(tex.pixels[index]) + green(tex.pixels[index]) + blue(tex.pixels[index]))/3.0f == 255){
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
boolean iintro = true;
boolean ibossoutro = false;
boolean icredits = false;
boolean itutorial = true;
String[] intro;
String[] bossoutro;
String[] credits;
String displayed = "";
int introx = 0;
int introy = 0;
int del = 32;
public void runintro(){
  ui.beginDraw();
  if(introy < intro.length){
    ui.textSize(20);
    if(intro[introy].substring(introx,introx+1) != " " && introx < intro[introy].length()-1){
      ui.background(0);
      displayed = displayed + intro[introy].substring(introx,introx+1);
      ui.fill(0);
      ui.noStroke();
      ui.rect(460,480,1000,200);
      ui.fill(255);
      ui.text(displayed,460,480,1000,200);
      ui.stroke(255);
      ui.image(corner,360,400);
      ui.scale(-1,-1);
      ui.image(corner,-1560,-700);
      keytap.stop();
      if(keytap.isPlaying() == false){
        keytap.play();
      }
      delay(del);
      introx++;
    }
    if(keyCode == 27){
      iintro = false;
      introx = 0;
      introy = 0;
      ui.textSize(16);
      updateui();
    }
    if(mousePressed){
      del = 5;
    }
    if(introx >= intro[introy].length()-2){
      ui.fill(0);
      ui.noStroke();
      ui.rect(750,750,320,180);
      ui.fill(255);
      ui.text("Click to continue!",870,800);
      if(mousePressed){
        introy++;
        introx = 0;
        displayed = "";
        del = 32;
        delay(200);
      }
    }
  }else{
    delay(3000);
    iintro = false;
    introx = 0;
    introy = 0;
    updateui();
  }
  ui.textSize(16);
  ui.endDraw();
  image(ui,0,0,width,height);
}
public void runbossoutro(){
  ui.beginDraw();
  if(introy < bossoutro.length){
    ui.textSize(20);
    if(bossoutro[introy].substring(introx,introx+1) != " " && introx < bossoutro[introy].length()-1){
      ui.background(0);
      displayed = displayed + bossoutro[introy].substring(introx,introx+1);
      ui.fill(0);
      ui.noStroke();
      ui.rect(460,480,1000,200);
      ui.fill(255);
      ui.text(displayed,460,480,1000,200);
      ui.stroke(255);
      ui.image(corner,360,400);
      ui.scale(-1,-1);
      ui.image(corner,-1560,-700);
      keytap.stop();
      if(keytap.isPlaying() == false){
        keytap.play();
      }
      delay(del);
      introx++;
    }
    if(keyCode == 27){
      ibossoutro = false;
      if(lefthand.empty == false){
        lefthand = new item(swordd,lefthand.stat*1.5f,lefthand.cdreset,mswordd);
      }else if(righthand.empty == false){
        righthand = new item(swordd,righthand.stat*1.5f,righthand.cdreset,mswordd);
      }else{
        lefthand = new item(swordd,50,0.25f,mswordd);
      }
      introx = 0;
      introy = 0;
      ui.textSize(16);
      updateui();
    }
    if(mousePressed){
      del = 5;
    }
    if(introx >= bossoutro[introy].length()-2){
      ui.fill(0);
      ui.noStroke();
      ui.rect(750,750,320,180);
      ui.fill(255);
      ui.text("Click to continue!",870,800);
      if(mousePressed){
        introy++;
        introx = 0;
        displayed = "";
        del = 32;
      }
    }
  }else{
    delay(3000);
    ibossoutro = false;
    if(lefthand.empty == false){
      lefthand = new item(swordd,lefthand.stat*1.5f,lefthand.cdreset,mswordd);
    }else if(righthand.empty == false){
      righthand = new item(swordd,righthand.stat*1.5f,righthand.cdreset,mswordd);
    }else{
      lefthand = new item(swordd,50,0.25f,mswordd);
    }
    introx = 0;
    introy = 0;
    updateui();
  }
  ui.textSize(16);
  ui.endDraw();
  image(ui,0,0,width,height);
}
public void runcredits(){
  ui.beginDraw();
  if(introy < credits.length){
    ui.textSize(20);
    if(credits[introy].substring(introx,introx+1) != " " && introx < credits[introy].length()-1){
      ui.background(0);
      displayed = displayed + credits[introy].substring(introx,introx+1);
      ui.fill(0);
      ui.noStroke();
      ui.rect(460,480,1000,200);
      ui.fill(255);
      ui.text(displayed,460,480,1000,200);
      ui.stroke(255);
      ui.image(corner,360,400);
      ui.scale(-1,-1);
      ui.image(corner,-1560,-700);
      keytap.stop();
      if(keytap.isPlaying() == false){
        keytap.play();
      }
      delay(del);
      introx++;
    }
    if(keyCode == 27){
      icredits = false;
      menu = true;
      introx = 0;
      introy = 0;
      ui.textSize(16);
      updateui();
    }
    if(mousePressed){
      del = 5;
    }
    if(introx >= credits[introy].length()-2){
      ui.fill(0);
      ui.noStroke();
      ui.rect(750,750,320,180);
      ui.fill(255);
      ui.text("Click to continue!",870,800);
      if(mousePressed){
        introy++;
        introx = 0;
        displayed = "";
        del = 32;
      }
    }
  }else{
    delay(3000);
    icredits = false;
    menu = true;
    introx = 0;
    introy = 0;
    updateui();
  }
  ui.textSize(16);
  ui.endDraw();
  image(ui,0,0,width,height);
}
boolean menu = true;
public void sunmoonmenu(){
  d3.directionalLight(15,15,15,0,0,1);
  //d3.ambientLight(10,10,10);
  d3.rotateX(radians(time+90));
  if(time >= 150 && time < 180){
    d3.directionalLight(constrain((time-150)/30*10,0,10),constrain((time-150)/30*20,0,10),constrain((time-150)/30*20,0,10),0,0,-1);
  }else if(time > 300){
    d3.directionalLight(constrain((330-time)/30*10,0,10),constrain((330-time)/30*20,0,10),constrain((330-time)/30*20,0,10),0,0,-1);
  }else if(time > 180 && time < 300){
    d3.directionalLight(10,10,10,0,0,-1);
  }
  if(time >= 150 && time <= 180){
    d3.directionalLight(255-constrain((time-150)/30*255,0,255),255-constrain((time-150)/30*255,0,255),255-constrain((time-150)/30*255,0,255),0,0,1);
    d3.ambientLight((255-constrain((time-150)/30*255,0,255))/4+10,(255-constrain((time-150)/30*255,0,255))/4+10,(255-constrain((time-150)/30*255,0,255))/4+10);
    currentsky = lerpColor(day,night,(time-150)/30);
  }else if(time < 150){
    d3.directionalLight(255,255,255,0,0,1);
    d3.ambientLight(255.0f/4+10,255.0f/4+10,255.0f/4+10);
    currentsky = day;
  }else if(time >= 330){
    d3.directionalLight((time-330)/30*255,(time-330)/30*255,(time-330)/30*255,0,0,1);
    d3.ambientLight(((time-330)/30*255)/4+10,((time-330)/30*255)/4+10,((time-330)/30*255)/4+10);
    currentsky = lerpColor(night,day,(time-330)/30);
  }else{
    currentsky = night;
    d3.ambientLight(10,10,10);
  }
  d3.rotateX(-radians(time+90));
  time+=0.1f;
  if(time > 359){
    time = 0;
  }
}
public void cammenu(){
  int r = 100;
  float x = r * cos(radians(mouse.x));
  float y = r * sin(radians(mouse.x));
  mouse.x+=0.25f;
  if(mouse.x >= 360){
    mouse.x = 1+(mouse.x-360);
  }
  d3.perspective(PI/3.0f, PApplet.parseFloat(width)/PApplet.parseFloat(height), (height/2.0f) / tan(PI/3.0f/2.0f)/8.0f, (height/2.0f) / tan(PI/3.0f/2.0f)*7.5f);
  d3.camera(0,0,-500,-x*10,-y*10,-600,rz,0,1-rz);  
}
public void drawbackground(){
  d3.beginDraw();
  sky();
  d3.endDraw();
  d3.beginDraw();
  sunmoonmenu();
  allcollision();
  posheight = lerp(posheight,noise(ppos.x/PApplet.parseFloat(dens)/PApplet.parseFloat(divider),ppos.y/PApplet.parseFloat(dens)/PApplet.parseFloat(divider))*PApplet.parseFloat(multi),0.1f);
  lastz = lerp(lastz,ppos.z,0.25f);
  d3.translate(-ppos.x,-ppos.y,-posheight+lastz); 
  for(int i = 0; i < grounds.size(); i++){
    try{
      float x = -chunkdist/2 * cos(radians(mouse.x));
      float y = -chunkdist/2 * sin(radians(mouse.x));
      float x1 = grounds.get(i).pos.x-ppos.x;
      float y1 = grounds.get(i).pos.y-ppos.y;
      if(dist(x,y,x1,y1) < chunkdist*0.8f){
        grounds.get(i).display();
      }
    }catch(Exception e){
    }
  }
  managemobs();
  if(time >= 150 && time <= 360){
    managefireflies();
  }else if(time > 10 && time < 140){
    wind();
  }
  if(movedpf){
    movedpf = false;
    thread("movepf");
  }
  d3.translate(ppos.x,ppos.y,posheight-lastz); 
  cammenu();
  d3.endDraw();
}
public void drawmenu(){
  cursor();
  drawbackground();
  ui.beginDraw();
  ui.clear();
  if(bossdead == false){
    if(mouseX > 760*(width/1920) && mouseX < 1160*(width/1920) && mouseY > 350*(height/1080) && mouseY < 450*(height/1080)){
      ui.image(menusgon,760,300);
      if(mousePressed){
        menu = false;
        updateui();
        noCursor();
      }
    }else{
      ui.image(menusgoff,760,300);
    }
  }
  if(mouseX > 760*(width/1920) && mouseX < 1160*(width/1920) && mouseY > 550*(height/1080) && mouseY < 650*(height/1080)){
    ui.image(menungon,760,500);
    if(mousePressed){
      for(int i = 0; i < grounds.size(); i++){
        grounds.remove(i);
        System.gc();
      }
      seed = PApplet.parseInt(random(100000));
      noiseSeed(seed);
      entered = false;
      bossdead = false;
      delay = 0;
      deadcount = 0;
      bosshp = 7500;
      ch = 200;
      rz = 0;
      health = 100;
      daycount = 0;
      for(int i = mobs.size()-1; i > -1; i--){
        mobs.remove(i);
      }
      lefthand = new item(sworda,10,0.5f,msworda);
      inarmour = new item(shielda,10);
      for(int x = 0; x < 5; x++){
        for(int y = 0; y < 3; y++){
          inventory[x][y] = new item();
        }
      }
      for(int i = grounds.size()-1; i > 0; i--){
        grounds.remove(i);
      }
      ppos.x+=random(-100000,100000);
      ppos.x = PApplet.parseInt(ppos.x/1000)*1000;
      ppos.y+=random(-100000,100000);
      ppos.y = PApplet.parseInt(ppos.y/1000)*1000;
      tint(255);
      initchunks();
      updateui();
      ppos.x = PApplet.parseInt(ppos.x);
      ppos.y = PApplet.parseInt(ppos.y);
      chunkupdate();
      ppos.x = random(-1000000,1000000);
      ppos.x = PApplet.parseInt(ppos.x);
      ppos.y = random(-1000000,1000000);
      ppos.y = PApplet.parseInt(ppos.y);
      chunkupdate();
      initchunks();
      for(int x = 0; x < 5; x++){
        for(int y = 0; y < 3; y++){
          inventory[x][y] = new item();
        }
      }
      lefthand = new item(sworda,10,0.5f,msworda);
      righthand = new item();
      inarmour = new item(shielda,10);
      points = 0;
      flowerc = 0;
      daycount = 0;
      health = 100;
      time = random(359);
    }
  }else{
    ui.image(menungoff,760,500);
  }
  if(mouseX > 760*(width/1920) && mouseX < 1160*(width/1920) && mouseY > 750*(height/1080) && mouseY < 850*(height/1080)){
    ui.image(menuegon,760,700);
    if(mousePressed){
      exit();
    }
  }else{
    ui.image(menuegoff,760,700);
  }
  ui.endDraw();
  bloomp.beginDraw();
  bloomp.clear();
  bloomp.image(d3,0,0,640,380);
  bloomp.endDraw();
  bloom.set("skyr",red(currentsky)/PApplet.parseFloat(255),green(currentsky)/PApplet.parseFloat(255),blue(currentsky)/PApplet.parseFloat(255));
  bloomp.filter(bloom);
  image(d3,0,0,width,height);
  image(bloomp,0,0,width,height);
  image(ui,0,0,width,height);
}
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
  float cdreset = 1-1/(daycount+1)*0.1f;
  float increment = 180/lefthand.cdreset/60;
  float damage = random(5+daycount*2,10+daycount*2);
  float rot;
  mob(){
    float d = random(chunkdist*0.1f,chunkdist*0.3f);
    float deg = radians(random(360));
    float x = d * cos(deg);
    float y = d * sin(deg);
    spawn = new PVector(ppos.x+x,ppos.y+y);
    pos.x = spawn.x;
    pos.y = spawn.y;
    pos.z = noise((pos.x/dens)/PApplet.parseFloat(divider),(pos.y/dens)/divider)*PApplet.parseFloat(multi);
    posf.x = spawn.x+=random(-wi*dens/2,wi*dens/2);
    posf.y = spawn.y+=random(-wi*dens/2,wi*dens/2);
    particles.add(new particlesystem(new PVector(pos.x,pos.y,pos.z-100),20, 50, 0, 0.05f, 120, 20, 20, color(79,9,104),0,0,360));
  }
  //possibly thread
  public void walk(){
    pos.z = lerp(pos.z,noise((pos.x/dens)/PApplet.parseFloat(divider),(pos.y/dens)/divider)*PApplet.parseFloat(multi),0.2f);
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
              health-=damage*(1.0f-inarmour.stat/100.0f);
            }else{
              health-=damage;
            }
            painplayer.stop();
            painplayer.play();
            particles.add(new particlesystem(new PVector(ppos.x,ppos.y,posheight),200, 100, -0.02f, 0.005f, 60, 6, 6, color(255,255,255),0,270,360));
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
  public void display(){
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
  float cdreset = 1.5f;//0.5-1/(daycount+1)*0.1;
  float increment = 180/lefthand.cdreset/60;
  float damage = random(25+daycount*2,50+daycount*2);
  float rot;
  PVector[] boxes;
  boss(){
    float d = random(chunkdist*0.3f,chunkdist*0.5f);
    float deg = radians(random(360));
    pos = new PVector(ppos.x+d * cos(deg),ppos.y+d * sin(deg),0);
    pos.z = noise((pos.x/dens)/PApplet.parseFloat(divider),(pos.y/dens)/divider)*PApplet.parseFloat(multi)-70;
    boxes = new PVector[PApplet.parseInt(random(8,15))];
    for(int i = 0; i < boxes.length; i++){
      int r = round(random(359));
      float x = 1200 * cos(r);
      float y = 1200 * sin(r);
      float z = noise(((x+pos.x)/dens)/PApplet.parseFloat(divider),((y+pos.y)/dens)/divider)*PApplet.parseFloat(multi)-450;
      boxes[i] = new PVector(x,y,z);
    }
    try{
      checktrees();
    }catch(Exception e){
    }
  }
  public void checktrees(){
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
          PVector end = new PVector(ppos.x,ppos.y,noise((ppos.x/dens)/PApplet.parseFloat(divider),(ppos.y/dens)/divider)*PApplet.parseFloat(multi));
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
    cd-=0.016f;
    if(cd < 0){
      cd = 0;
      attack = false;
    }
  float x = ppos.x-pos.x;
  float y = ppos.y-pos.y;
  float t = sqrt(x*x) + sqrt(y*y);
  rot = bearing(0,0,(x/t),(y/t));
  }
  public void display(){
    for(int i = projs.size()-1; i > -1; i--){
      float x = projs.get(i).pos.x;
      float y = projs.get(i).pos.y;
      if(dist(x,y,ppos.x,ppos.y) < 100 && health > 0){
        health-=damage*(1-inarmour.stat/100);
        painplayer.stop();
        painplayer.play();
        particles.add(new particlesystem(new PVector(ppos.x,ppos.y,posheight),200, 100, -0.02f, 0.005f, 60, 6, 6, color(255,255,255),0,270,360));
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
  public void display(){
    if(dist(pos.x,pos.y,posf.x,posf.y) < 10){
      posf.x = pos.x+random(-1000,1000);
      posf.y = pos.y+random(-1000,1000);
    }
    float x = posf.x-pos.x;
    float y = posf.y-pos.y;
    float t = sqrt(x*x) + sqrt(y*y);
    pos.x+=3*x/t;
    pos.y+=3*y/t;
    pos.z = lerp(pos.z,noise((pos.x/dens)/PApplet.parseFloat(divider),(pos.y/dens)/divider)*PApplet.parseFloat(multi),0.1f);
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
  public void display(){
    pos.x-=move.x;
    pos.y-=move.y;
    pos.z-=move.z;
    d3.translate(pos.x,pos.y,pos.z-180);
    d3.rotateZ(rot);
    d3.fill(255,0,0);
    d3.rotateY(radians(90));
    d3.rotateZ(radians(millis()/1000.0f*360));
    d3.shape(mproj);
    d3.rotateZ(-radians(millis()/1000.0f*360));
    d3.noFill();
    d3.rotateY(radians(-90));
    d3.rotateZ(-rot);
    d3.translate(-pos.x,-pos.y,-pos.z+180);
  }
}
public void managemobs(){
  float recdist = chunkdist;
  for(int i = mobs.size()-1; i > -1; i--){
    if(dist(mobs.get(i).pos.x,mobs.get(i).pos.y,ppos.x,ppos.y) < recdist){
      recdist = dist(mobs.get(i).pos.x,mobs.get(i).pos.y,ppos.x,ppos.y);
    } 
    if(dist(mobs.get(i).pos.x,mobs.get(i).pos.y,ppos.x,ppos.y) > chunkdist*0.6f){
      mobs.remove(i);
      continue;
    }else if(mobs.get(i).hp <= 0){
      if(random(1) >= 0.5f){
        mobdrops.add(new drops(mobs.get(i).pos,false));
      }
      if(bossdead){
        peoples.add(new people(mobs.get(i).pos,mobs.get(i).rot));
        particles.add(new particlesystem(new PVector(mobs.get(i).pos.x,mobs.get(i).pos.y,mobs.get(i).pos.z-50),50, 50, -0.02f, 0.005f, 400, 10, 10, color(255,255,255),0,0,360));
        km++;
        updatekillcount();
      }else{
        particles.add(new particlesystem(new PVector(mobs.get(i).pos.x,mobs.get(i).pos.y,mobs.get(i).pos.z-50),50, 50, -0.02f, 0.005f, 400, 10, 10, color(10,10,10),0,0,360));
      }
      mobs.remove(i);
      points+=100;
      deathmob.stop();
      deathmob.play();
      continue;
    }
  }
  whispering.amp(constrain(0.5f-recdist/3000,0,0.5f));
  int mobc = 10;
  int bossc = 0;
  mobc+=daycount*2;
  mobc = constrain(mobc,0,50);
  bossc+=daycount/2;
  bossc = constrain(bossc,0,8);
  if(time > 180){
    mobc*=1.5f;
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
        particles.add(new particlesystem(new PVector(bosses.get(i).pos.x,bosses.get(i).pos.y,bosses.get(i).pos.z-50),50, 50, -0.02f, 0.005f, 400, 10, 10, color(255,255,255),0,0,360));
        kb++;
        updatekillcount();
      }else{
        particles.add(new particlesystem(new PVector(bosses.get(i).pos.x,bosses.get(i).pos.y,bosses.get(i).pos.z-50),50, 50, -0.02f, 0.005f, 400, 10, 10, color(10,10,10),0,0,360));
      }
      if(daycount > 4 && random(1) >= 0.0f){
        portalpos = bosses.get(i).pos;
      }
      bosses.remove(i);
    }else if(dist(x,y,ppos.x,ppos.y) > chunkdist*0.6f){
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
public float bearing(float a1, float a2, float b1, float b2) {
    float theta = atan2(b1 - a1, a2 - b2);
    if (theta < 0.0f)
        theta += TWO_PI;
    return theta-PI/2;
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
boolean right = false;
public void keyPressed(){
  if((key == 'w' || key == 'W') && iopen == false && sopen == false && itutorial == false && menu == false && health > 0){
    keyp[0] = true;
  }else if((key == 's' || key == 'S') && iopen == false && sopen == false && itutorial == false && menu == false && health > 0){
    keyp[1] = true;
  }else if((key == 'd' || key == 'D') && iopen == false && sopen == false && itutorial == false && menu == false && health > 0){
    keyp[2] = true;
  }else if((key == 'a' || key == 'A') && iopen == false && sopen == false && itutorial == false && menu == false && health > 0){
    keyp[3] = true;
  }else if(keyCode == 16 && iopen == false && sopen == false && itutorial == false && menu == false && health > 0){
    keyp[4] = true;
  }else if(keyCode == 32 && keyp[5] == false && iopen == false && sopen == false && itutorial == false && menu == false && health > 0){
    keyp[5] = true;
    prevheight = noise(ppos.x/dens/divider,ppos.y/dens/divider)*multi;
    jumpvel = 80;
    ppos.z = 1;
  }else if(keyCode == 32 && ppos.z !=0 && iopen == false && sopen == false && itutorial == false && menu == false && health > 0){
    keyp[6] = true;
  }else if((key == 'e' || key == 'E') && iopen == false && sopen == false && itutorial == false && menu == false && health > 0){
    keyp[7] = true;
  }else if((key == 'h' || key == 'H') && flowerc > 0 && health < 100 && health > 0 && iopen == false && sopen == false && itutorial == false && menu == false){
    heal.stop();
    heal.play();
    health+=10;
    flowerc--;
    particles.add(new particlesystem(new PVector(ppos.x,ppos.y,posheight),100, 50, 0.04f, 0.005f, 400, 10, 10, color(255,67,18),0,270,360));
    updatehp();
    updateflower();
  }else if((key == 'i' || key == 'I') && health > 0 && sopen == false && itutorial == false && menu == false){
    if(iopen){
      delay(1);
      updateui();
      iopen = false;
      close.stop();
      close.play();
      fixinven();
      robot.mouseMove(width/2,height/2);
      noCursor();
    }else{
      cursor();
      open.stop();
      open.play();
      openinventory();
      iopen = true;
    }
  }else if((key == 'r' || key == 'R') && health == 0){
    entered = false;
    bossdead = false;
    delay = 0;
    deadcount = 0;
    bosshp = 7500;
    ch = 200;
    rz = 0;
    health = 100;
    daycount = 0;
    lefthand = new item(sworda,10,0.5f,msworda);
    inarmour = new item(shielda,10);
    for(int x = 0; x < 5; x++){
      for(int y = 0; y < 3; y++){
        inventory[x][y] = new item();
      }
    }
    for(int i = grounds.size()-1; i > 0; i--){
      grounds.remove(i);
    }
    ppos.x+=random(-100000,100000);
    ppos.x = PApplet.parseInt(ppos.x/1000)*1000;
    ppos.y+=random(-100000,100000);
    ppos.y = PApplet.parseInt(ppos.y/1000)*1000;
    tint(255);
    initchunks();
    updateui();
  }else if(key == 27){
    if(iopen){
      delay(1);
      updateui();
      iopen = false;
      close.stop();
      close.play();
      fixinven();
      robot.mouseMove(width/2,height/2);
      noCursor();
    }else if(sopen){
      delay(1);
      updateui();
      sopen = false;
      close.stop();
      close.play();
      robot.mouseMove(width/2,height/2);
      noCursor();
    }else if(itutorial){
      itutorial = false;
      close.stop();
      close.play();
    }else if(entered == false && bossdead == false){
      open.stop();
      open.play();
      sopen = true;
    }
    key = 0;
  }else if((key == 'k' || key == 'K') && iopen == false && sopen == false && menu == false){
    if(itutorial){
      updateui();
      itutorial = false;    
      close.stop();
      close.play();
    }else{
      open.stop();
      open.play();
      itutorial = true;
    }
  }else if(key == '/'){
    points+=100;
    if(iopen == false){
      particles.add(new particlesystem(new PVector(ppos.x,ppos.y,posheight),100, 50, -0.02f, 0.005f, 400, 20, 20, color(255,255,255),0,270,360));
    }
    //particlesystem(PVector origin,int amount_, float maxv, float g, float d_, int lifespan, int w_, int h_, color c_){
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
  if(mouseButton == RIGHT && iopen == false && sopen == false && menu == false && itutorial == false && righthand.cd == 0 && righthand.weapon){
    attack.stop();
    attack.play();
    righthand.cd = righthand.cdreset;
    righthandr.z = 1;
    righthandr.x = 1;
  }
  if(mouseButton == LEFT && iopen == false && sopen == false && menu == false && itutorial == false && lefthand.cd == 0 && lefthand.weapon){
    attack.stop();
    attack.play();
    lefthand.cd = lefthand.cdreset;
    lefthandr.z = 1;
    lefthandr.x = 1;
  }
}

//------------------------------------------------------------------------------------------------------------------------------------------------------

public void move(){
  float r = 16; //speed
  float x = r * cos(radians(mouse.x));
  float y = r * sin(radians(mouse.x));
  float p = 1;
  int keysp = 0;
  posheight = lerp(posheight,noise(ppos.x/PApplet.parseFloat(dens)/PApplet.parseFloat(divider),ppos.y/PApplet.parseFloat(dens)/PApplet.parseFloat(divider))*PApplet.parseFloat(multi),0.1f);
  lastmove.x = ppos.x;
  lastmove.y = ppos.y;
  if((posheight > ppos.z+posheight && entered == false) || (entered && ppos.z <= 0)){
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
    if(entered == false){
      ppos.z+=(noise(ppos.x/PApplet.parseFloat(dens)/PApplet.parseFloat(divider),ppos.y/PApplet.parseFloat(dens)/PApplet.parseFloat(divider))*PApplet.parseFloat(multi)-prevheight);                           
      prevheight = noise(ppos.x/PApplet.parseFloat(dens)/PApplet.parseFloat(divider),ppos.y/PApplet.parseFloat(dens)/PApplet.parseFloat(divider))*PApplet.parseFloat(multi);
    }
    ppos.z+=jumpvel*0.3f;
    jumpvel-=20*0.3f;
  }
}

public void mouseMoved(){
  if(iopen == false && sopen == false && itutorial == false && menu == false){
    mousemove();
  }
}
public void mouseDragged(){
  if(iopen == false && sopen == false && itutorial == false && menu == false){
    mousemove();
  }
}  
public void mousemove(){
  if(mouseX > width/2){
    right = true;
  }else{
    right = false;
  }
  mouse.x+=(width/2-mouseX)*sense;
  mouse.y+=(height/2-mouseY)*sense;
  robot.mouseMove(xy[0]+width/2,xy[1]+height/2);
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
  int c;
  particlesystem(PVector origin,int amount_, float maxv, float g, float d_, int lifespan, int w_, int h_, int c_, float degree, float degreez, int rng){
    c = c_;
    amount = amount_;
    ls = lifespan;
    gravity = g;
    d = d_;
    tex = createImage(w_,h_,ARGB);
    tex.loadPixels();
    for(int i = 0; i < tex.pixels.length; i++){
      if(noise(i+cos(millis()/1000.0f)) > 0.5f){
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
      fcl[i] = PApplet.parseInt(random(lifespan));
    }
  }
  public void display(){
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

public void manageparticles(){
  for(int i = particles.size()-1; i > -1; i--){
    if(particles.get(i).count >= particles.get(i).ls){
      particles.remove(i);
      continue;
    }
    particles.get(i).display();
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
int treehit = 200;
int flowerhit = 300;
class ground{
  int biome;
  int density;
  PVector pos = new PVector(0,0);
  PVector res = new PVector(0,0);
  PShape object = createShape(GROUP);
  PShape[] clouds;
  PShape[] trees = new PShape[treecount];
  PVector[] tposs = new PVector[treecount];
  PShape[] flowers = new PShape[flowercount];
  PVector[] fposs = new PVector[flowercount];
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
    for(int i = 0; i < flowercount; i++){
      fposs[i] = new PVector(0,0,0);
    }
    for(int i = 0; i < treecount; i++){
      tposs[i] = new PVector(0,0,0);
    }
    if(biome != 3){
    for(int n = 0; n < treecount; n++){
      if(noise(pos.x,pos.y,n) < treespawnrate){
        tposs[n].x = (noise(pos.x/dens*n,n*n*pos.y/pos.x*n*n)-0.25f)*2*wi*dens;
        tposs[n].y = (noise(pos.y/dens*n,n*n*pos.x/pos.y)-0.25f)*2*wi*dens;
        boolean found = true;
        for(int i = 0; i < treecount; i++){
          if(dist(tposs[n].x,tposs[n].y,tposs[i].x,tposs[i].y) < 300 && tposs[i].z == 1){
            found = false;
          }
        }
        if(found){
          trees[n] = createTree(tposs[n].x,tposs[n].y,biome);
          object.addChild(trees[n]);
          tposs[n].z = 1;
        }
      }
    }
    for(int n = 0; n < flowercount; n++){
      if(noise(pos.x,pos.y,n) < flowerspawnrate){
        fposs[n].x = (noise(pos.x/dens*cos(n)*100,n*n*pos.y/pos.x*n*n)-0.25f)*2*wi*dens;
        fposs[n].y = (noise(pos.y/dens*cos(sin(n))*100,n*n*pos.x/pos.y)-0.25f)*2*wi*dens;
        boolean found = true;
        for(int i = 0; i < flowercount; i++){
          if(dist(fposs[n].x,fposs[n].y,fposs[i].x,fposs[i].y) < 200 && i != n){
            found = false;
          }
        }
        if(found){
          flowers[n] = createFlower(fposs[n].x,fposs[n].y);
          object.addChild(flowers[n]);
          fposs[n].z = 1;
        }
      }
    }
  }
//------------------------------------------------------------------------------------------------------------------------------------------------------

    //Put grass ect here
    PShape grass = createShape();
    grass.beginShape(TRIANGLES);
    float val = noise(pos.x/dens,pos.y/dens)*1.5f;
    if(biome == 3){
      val*=0.1f;
    }
    for(int i = 0; i < grassdens*val; i++){
      PImage grassc = createImage(1,1,RGB);
      if(biome == 1){
        grassc.pixels[0] = color(31,227,93);
      }else if(biome == 2){
        grassc.pixels[0] = color(255,255,255);
      }else{
        grassc.pixels[0] = color(253,250,179);
      }
      grass.texture(grassc);
      grass.noStroke();
      grass.noFill();
      grass.ambient(grassc.pixels[0]);
      float rndx = random(-1,1);
      float rndy = random(-1,1);
      float grassh = 0;
      if(noise((pos.x/dens+rndx+(noise(pos.x/PApplet.parseFloat(dens)*i,i)-0.25f)*2*wi)/divider,(pos.y/PApplet.parseFloat(dens)+rndy+(noise(pos.y/dens,i)-0.25f)*2*hi)/divider)*multi < noise((pos.x/PApplet.parseFloat(dens)+(noise(pos.x/dens*i,i)-0.25f)*2*wi)/divider,(pos.y/dens+(noise(pos.y/PApplet.parseFloat(dens),i)-0.25f)*2*hi)/divider)*multi){                                                                                        
        grassh=noise((pos.x/PApplet.parseFloat(dens)+(noise(pos.x/PApplet.parseFloat(dens)*i,i)-0.25f)*2*wi)/divider,(pos.y/PApplet.parseFloat(dens)+(noise(pos.y/PApplet.parseFloat(dens),i)-0.25f)*2*hi)/divider)*multi;
        //                       ((noise(pos.x/dens*i,i)-0.25)*2*wi*dens,               (noise(pos.y/dens,i)-0.25)*2*hi*dens,grassh,1,1);
      }else{
        grassh=noise((pos.x/PApplet.parseFloat(dens)+rndx+(noise(pos.x/PApplet.parseFloat(dens)*i,i)-0.25f)*2*wi)/divider,(pos.y/PApplet.parseFloat(dens)+rndy+(noise(pos.y/PApplet.parseFloat(dens),i)-0.25f)*2*hi)/divider)*multi;
        //                            ((noise(pos.x/dens*i,i)-0.25)*2*wi*dens,(noise(pos.y/dens,i)-0.25)*2*hi*dens,grassh,1,1);
      }
      rndx*=100;
      rndy*=100;
      grass.vertex((noise(pos.x/PApplet.parseFloat(dens)*i,i)-0.25f)*2*wi*dens,(noise(pos.y/PApplet.parseFloat(dens),i)-0.25f)*2*hi*dens,grassh,1,1);
      grass.vertex(((noise(pos.x/PApplet.parseFloat(dens)*i,i)-0.25f)*2*wi)*dens+rndx*0.25f,((noise(pos.y/PApplet.parseFloat(dens),i)-0.25f)*2*hi)*dens+rndy*0.25f,grassh-random(20,30),1,1);              
      grass.vertex(((noise(pos.x/PApplet.parseFloat(dens)*i,i)-0.25f)*2*wi)*dens+rndx*0.5f,((noise(pos.y/PApplet.parseFloat(dens),i)-0.25f)*2*hi)*dens+rndy*0.5f,grassh,1,1);
      grass.vertex(((noise(pos.x/PApplet.parseFloat(dens)*i,i)-0.25f)*2*wi)*dens+rndx*0.375f,((noise(pos.y/PApplet.parseFloat(dens),i)-0.25f)*2*hi)*dens+rndy*0.325f,grassh,1,1);
      grass.vertex(((noise(pos.x/PApplet.parseFloat(dens)*i,i)-0.25f)*2*wi)*dens+rndx*0.625f,((noise(pos.y/PApplet.parseFloat(dens),i)-0.25f)*2*hi)*dens+rndy*0.625f,grassh-random(20,30),1,1);      
      grass.vertex(((noise(pos.x/PApplet.parseFloat(dens)*i,i)-0.25f)*2*wi)*dens+rndx*0.875f,((noise(pos.y/PApplet.parseFloat(dens),i)-0.25f)*2*hi)*dens+rndy*0.875f,grassh,1,1);
      grass.vertex(((noise(pos.x/PApplet.parseFloat(dens)*i,i)-0.25f)*2*wi)*dens+rndx*0.75f,((noise(pos.y/PApplet.parseFloat(dens),i)-0.25f)*2*hi)*dens+rndy*0.75f,grassh,1,1);
      grass.vertex(((noise(pos.x/PApplet.parseFloat(dens)*i,i)-0.25f)*2*wi)*dens+rndx*0.875f,((noise(pos.y/PApplet.parseFloat(dens),i)-0.25f)*2*hi)*dens+rndy*0.875f,grassh-random(20,30),1,1);              
      grass.vertex(((noise(pos.x/PApplet.parseFloat(dens)*i,i)-0.25f)*2*wi)*dens+rndx,((noise(pos.y/PApplet.parseFloat(dens),i)-0.25f)*2*hi)*dens+rndy,grassh,1,1);
    }
    grass.endShape();
    object.addChild(grass);
  
//------------------------------------------------------------------------------------------------------------------------------------------------------

    if(noise(this.pos.x/dens,this.pos.y/dens) <= cloudspawnrate){
      float a = (noise(this.pos.x*this.pos.y/dens,this.pos.y/dens))*wi*dens;
      float b = (noise(this.pos.x/dens,this.pos.y*this.pos.x/dens))*wi*dens;
      float ah = noise((this.pos.x/dens)/PApplet.parseFloat(divider),(this.pos.y/dens)/divider)*PApplet.parseFloat(multi)-random(3000,3500);
      int rnd = PApplet.parseInt(random(4,6));
      clouds = new PShape[rnd];
      for(int i = 0; i < rnd; i++){
        clouds[i] = createCloud(a+random(-400,400),b+random(-400,400),ah+random(-100,100),600+random(-200,200));
        object.addChild(clouds[i]);
      }
    }
  }

//------------------------------------------------------------------------------------------------------------------------------------------------------

  public PShape createCloud(float a, float b, float c, float r){
    PShape cloud = createShape();
    PImage texture = createImage(1,1,RGB);
    texture.loadPixels();
    texture.pixels[0] = color(253,253,253);
    PVector[][] shape = new PVector[12][12];
    for(int i = 0; i < 12; i++){
      float lon = map(i,0,11,-PI,PI);
      for(int j = 0; j < 12; j++){
        float lat = map(j,0,11,-HALF_PI,HALF_PI);
        float x = r * sin(lon) * cos(lat);
        float y = r * sin(lon) * sin(lat);
        float z = r * cos(lon);
        float t = sqrt(x*x)+sqrt(y*y)+sqrt(z*z);
        shape[i][j] = new PVector(x+noise(x/r+r+a,y/r+r+b,z/r+r)*(x/t)*r*1.5f+a,y+noise(x/r+r+a,y/r+r+b,z/r+r)*(y/t)*r*1.5f+b,z+noise(x*2/r+r+a,y*2/r+r+b,z*2/r+r)*(z/t)*r/4+c);     
      }
    }  
    for(int i = 0; i < 11; i++){
      cloud.beginShape(TRIANGLE_STRIP);
      cloud.emissive(255,255,255);
      cloud.noStroke();
      for(int j = 0; j < 12; j++){
        cloud.vertex(shape[i][j].x,shape[i][j].y,shape[i][j].z,1,1);
        cloud.vertex(shape[i+1][j].x,shape[i+1][j].y,shape[i+1][j].z,1,1);
      }
      cloud.endShape();
    }
    return(cloud);
  }
  
  public PShape createTree(float x, float y, int biome){
    PImage treetexture = createImage(2,1,RGB);
    PShape tree;
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
    tree = createShape();
    tree.beginShape(TRIANGLE_STRIP);
    tree.noStroke();
    tree.texture(treetexture);
    tree.noFill();
    tree.ambient(treetexture.pixels[1]);
    for(int l = -1; l < 4; l++){
      for(int r = 0; r < 6; r++){
        float xoff = (80-l*2) * cos(radians(r*72));
        float yoff = (80-l*2) * sin(radians(r*72));
        tree.vertex(xoff+x,yoff+y,-l*120+noise(((pos.x+x)/dens)/PApplet.parseFloat(divider),((pos.y+y)/dens)/divider)*PApplet.parseFloat(multi),2,1);
        xoff = (80-(l+1)*2) * cos(radians(r*72));
        yoff = (80-(l+1)*2) * sin(radians(r*72));
        tree.vertex(xoff+x,yoff+y,-(l+1)*120+noise(((pos.x+x)/dens)/PApplet.parseFloat(divider),((pos.y+y)/dens)/divider)*PApplet.parseFloat(multi),2,1);
      }
    }
    tree.endShape();
    PVector[][] rnds = new PVector[5][6];
    for(int r = 0; r < 5; r++){
      for(int i = 0; i < 5; i++){
        rnds[4-i][r] = new PVector(random(-9*i,9*i),random(-9*i,9*i),random(-9*i,9*i));
      }
      rnds[r][5] = rnds[r][0];
    }
    for(int l = 0; l < 3; l++){
      tree.beginShape(TRIANGLE_STRIP);
      tree.noStroke();
      tree.texture(treetexture);
      tree.noFill();
      tree.ambient(treetexture.pixels[0]);
      for(int r = 0; r < 6; r++){
        float xoff = (500-l*133) * cos(radians(r*72));
        float yoff = (500-l*133) * sin(radians(r*72));
        tree.vertex(xoff+x+rnds[l][r].x,yoff+y+rnds[l][r].y,-l*250+noise(((pos.x+x)/dens)/PApplet.parseFloat(divider),((pos.y+y)/dens)/divider)*PApplet.parseFloat(multi)-120,1,1);
        xoff = (400-(l+1)*133) * cos(radians(r*72));
        yoff = (400-(l+1)*133) * sin(radians(r*72));
        tree.vertex(xoff+x+rnds[l+1][r].x,yoff+y+rnds[l+1][r].y,-(l+1)*250+noise(((pos.x+x)/dens)/PApplet.parseFloat(divider),((pos.y+y)/dens)/divider)*PApplet.parseFloat(multi)-180,1,1);
      }
      tree.vertex(x,y,-(l+1)*150+noise(((pos.x+x)/dens)/PApplet.parseFloat(divider),((pos.y+y)/dens)/divider)*PApplet.parseFloat(multi)-180,1,1);
    }
    tree.endShape();
    return(tree);
  }
//------------------------------------------------------------------------------------------------------------------------------------------------------

  public PShape createFlower(float x, float y){
    PShape f = createShape();
    PImage ftext = createImage(3,1,RGB);
    ftext.pixels[0] = color(10,255,50);
    ftext.pixels[1] = color(10,10,10);
    ftext.pixels[2] = color(255,0,0);
    f.beginShape(TRIANGLE_STRIP);
    f.emissive(5,25,25);
    f.noFill();
    f.noStroke();
    f.ambient(ftext.pixels[0]);
    f.texture(ftext);
    float ground = noise((this.pos.x/dens+x/dens)/divider,(this.pos.y/dens+y/dens)/divider)*multi;
    for(int r = 0; r < 6; r++){
        float xoff = 4 * cos(radians(r*72));
        float yoff = 4 * sin(radians(r*72));
        f.vertex(xoff+x,yoff+y,ground,1,1);
        f.vertex(xoff+x,yoff+y,-60+ground,1,1);
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
    d3.translate(pos.x,pos.y);
    d3.shape(object);
    try{
    for(int i = 0; i < clouds.length; i++){
      d3.shape(clouds[i]);
    }
    }catch(Exception e){
    }
    d3.translate(-pos.x,-pos.y);
  }
  
//-----------------------------------------------------------------------------------------------------------------------/-------------------------

  public void collisions(boolean interact){
    if(dist(pos.x,pos.y,ppos.x,ppos.y) < wi*dens*2){
      for(int i = 0; i < treecount; i++){
        if(dist(ppos.x,ppos.y,tposs[i].x+pos.x,tposs[i].y+pos.y) < treehit && tposs[i].z == 1){
          ppos.x+=lastmove.x;
          ppos.y+=lastmove.y;
        }
      }
      for(int i = 0; i < flowercount; i++){
        if(dist(ppos.x,ppos.y,fposs[i].x+pos.x,fposs[i].y+pos.y) < flowerhit && fposs[i].z == 1 && interact && flowerc < 5){
          object.removeChild(object.getChildIndex(flowers[i]));
          flowerc++;
          flowerstotal++;
          updateflower();
          fposs[i].z = 0;
          fpickup.stop();
          fpickup.play();
        }
      }
    }
  }
}

//------------------------------------------------------------------------------------------------------------------------------------------------------

public void initchunks(){
  //cx = int(int(ppos.x)/(wi*dens))*(wi*dens);
  int gx = PApplet.parseInt(PApplet.parseInt(ppos.x-PApplet.parseInt(wi*dens/2))/(wi*dens))*(wi*dens);
  int gy = PApplet.parseInt(PApplet.parseInt(ppos.y+PApplet.parseInt(wi*dens/2))/(wi*dens))*(wi*dens);
  for(int x = 0; x < chunkdist/wi/dens*2; x++){
    for(int y = 0; y < chunkdist/wi/dens*2; y++){
      grounds.add(new ground(gx+(x-chunkdist/wi/dens)*wi*dens,gy+(y-chunkdist/wi/dens)*wi*dens,wi,hi,dens));
    }
  }
  chunkupdate();
  ppos.x-=wi*dens;
  ppos.y-=wi*dens;
  chunkupdate();
  ppos.x+=wi*dens;
  ppos.y+=wi*dens;
  chunkupdate();
  ppos.x+=wi*dens;
  ppos.y+=wi*dens;
  chunkupdate();
  ppos.x-=wi*dens;
  ppos.y-=wi*dens;
  chunkupdate();
}

//------------------------------------------------------------------------------------------------------------------------------------------------------

public void allcollision(){
  try{
  for(int i = 0; i < grounds.size(); i++){
    grounds.get(i).collisions(keyp[7]);
  }
  }catch(Exception e){
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

//------------------------------------------------------

public void cloudtextmanager(){
  cloudtext.loadPixels();
  for(int i = 0; i < grounds.size(); i++){
    try{
      for(int j = 0; j < grounds.get(i).clouds.length; j++){
        int x = 51 + PApplet.parseInt((ppos.x-grounds.get(i).pos.x)/PApplet.parseFloat(wi*dens)*50);
        int y = PApplet.parseInt((ppos.y-grounds.get(i).pos.y)/PApplet.parseFloat(wi*dens)*50)+PApplet.parseInt(time*10);
        if(y <= 0){
          y = 3600-y;
        }else if(y >= 3600){
          y = y-3600;
        }
        int index = x + y * 101;
        grounds.get(i).clouds[j].setEmissive(cloudtext.pixels[index]);
      }
    }catch(Exception e){
      println(e + "CLOUDS");
    }
  }
}
boolean movedpf = true;
PVector windvel = new PVector(random(5,20),random(360));
PVector windvelf = new PVector(random(5,20),random(360));
ArrayList<windparticle> windps = new ArrayList<windparticle>();
class windparticle{
  PVector pos = new PVector(ppos.x+random(-chunkdist/4,chunkdist/4),ppos.y+random(-chunkdist/4,chunkdist/4),0);
  float rh = random(10,300);
  int duration = 0;
  boolean dead = false;
  windparticle(){
    pos.z = noise((pos.x/dens)/PApplet.parseFloat(divider),(pos.y/dens)/divider)*PApplet.parseFloat(multi);
  }
  public void move(){
    float x = windvel.x * cos(radians(windvel.y));
    float y = windvel.x * sin(radians(windvel.y));
    pos.x+=x;
    pos.y+=y;
    pos.z=lerp(pos.z,noise((pos.x/dens)/PApplet.parseFloat(divider),(pos.y/dens)/divider)*PApplet.parseFloat(multi),0.1f);
    rh=lerp(rh,random(rh-10,rh+10),0.01f);
    if(dist(pos.x,pos.y,ppos.x,ppos.y) > chunkdist/4 || duration > 1000){
      dead = true;
    }
  }
  public void display(){
    d3.translate(pos.x,pos.y,pos.z-rh);
    d3.rotateZ(radians(windvel.y));
    d3.fill(255,60);
    d3.ambient(255);
    d3.box(20,20,5);
    d3.rotateZ(-radians(windvel.y));
    d3.translate(-pos.x,-pos.y,-pos.z+rh);
    duration++;
  }
}

public void wind(){
  windvel.x=lerp(windvel.x,windvelf.x,0.005f);
  wind.amp(map(windvel.x,1,20,0.0005f,0.1f));
  float channel = sin(radians(mouse.x-windvel.x+90))*-1;
  wind.pan(channel);
  windvel.y=lerp(windvel.y,windvelf.y,0.005f);
  if(dist(windvel.x,windvel.y,windvelf.x,windvelf.y) < 0.1f){
    windvelf = new PVector(random(5,20),random(360));
  }
  d3.ambientLight(255,255,255);
  for(int i = windps.size()-1; i > -1; i--){
    windps.get(i).display();
    if(windps.get(i).dead){
      windps.remove(i);
    }
  }
  if(windps.size() < PApplet.parseInt(map(windvel.x,5,20,5,100))){
    windps.add(new windparticle());
  }
}

public void movepf(){
  if(time >= 150 && time <= 360){
    for(int i = 0; i < fireflies.size(); i++){
      fireflies.get(i).move();
    }
  }else{
    for(int i = 0; i < windps.size(); i++){
      windps.get(i).move();
    }
  }  
  movedpf = true;
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Leveria_V1_0" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
