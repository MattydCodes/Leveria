import processing.sound.*;
SoundFile death;
SoundFile deathmob;
SoundFile pain;
SoundFile pursuit;
SoundFile attack;
SoundFile forge;
SoundFile ambient;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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
float flowerspawnrate = 0.3;
float cloudspawnrate = 0.3;
ArrayList<ground> grounds = new ArrayList<ground>();
PGraphics d3;
PGraphics bloomp;
PGraphics fogp;
PShader bloom;
PShader fog;
void setup(){
  fullScreen(P2D);
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
      int y = int(i/5);
      int x = i-y*5;
      inventory[x][y] = stringtoitem(inven[i]);
    }
    lefthand = stringtoitem(inven[15]);
    righthand = stringtoitem(inven[16]);
    inarmour = stringtoitem(inven[17]);
    flowerc = int(inven[18]);
    flowerstotal = int(inven[19]);
    points = int(inven[20]);
    daycount = int(inven[21]);
    ppos.x = float(inven[22]);
    ppos.y = float(inven[23]);
    seed = int(inven[24]);
    health = float(inven[25]);
    time = float(inven[26]);
    mouse.x = float(inven[27]);
    mouse.y = float(inven[28]);
    sense = float(inven[29]);
  }catch(Exception e){
    println(e);
    for(int x = 0; x < 5; x++){
      for(int y = 0; y < 3; y++){
        inventory[x][y] = new item();
      }
    }
    lefthand = new item(sworda,10,0.5,msworda);
    inarmour = new item(shielda,10);
    seed = int(random(10000));
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
void draw(){
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
    bloom.set("skyr",red(currentsky)/float(255),green(currentsky)/float(255),blue(currentsky)/float(255));
    fog.set("skyc",red(currentsky)/float(255),green(currentsky)/float(255),blue(currentsky)/float(255));
    bloomp.filter(bloom);
    fogp.filter(fog);
    image(d3,0,0,width,height);
    image(fogp,0,0,width,height);
    image(bloomp,0,0,width,height);
    image(ui,0,0,width,height);
  }
}

void draw3d(){
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
  lastz = lerp(lastz,ppos.z,0.25);
  d3.translate(-ppos.x,-ppos.y,-posheight+lastz); 
  for(int i = 0; i < grounds.size(); i++){
    try{
      float x = -chunkdist/2 * cos(radians(mouse.x));
      float y = -chunkdist/2 * sin(radians(mouse.x));
      float x1 = grounds.get(i).pos.x-ppos.x;
      float y1 = grounds.get(i).pos.y-ppos.y;
      if(dist(x,y,x1,y1) < chunkdist*0.7){
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
    displaymodel(righthand.model,-30+righthandr.x/float(4),140+righthandr.x/float(9),righthandr.x,-righthandr.x/float(5),0);
  }
  if(lefthand.empty == false){
    displaymodel(lefthand.model,30-lefthandr.x/float(4),140+lefthandr.x/float(9),lefthandr.x,lefthandr.x/float(5),0);
  }  
  d3.endDraw();
}

void displaymodel(PShape model, float offset, float r, float xr, float yr, float zr){
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