import processing.sound.*;
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
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.Component;
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
float cloudspawnrate = 0.8;
ArrayList<ground> grounds = new ArrayList<ground>();
PGraphics d3;
PGraphics bloomp;
PGraphics fogp;
PGraphics firefliesgraphic;
PShader bloom;
PShader fog;
PShader tints;
PShader floorlight;
void setup(){
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
  teleport.amp(1.5);
  pain.amp(1.5);
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
  msworda.scale(4,4,1.5);
  mswordb.scale(4,4,1.5);
  mswordc.scale(3,4,1.5);
  mswordd.scale(4,5,1.5);
  mdrop.scale(3.5,4,2.5); 
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
    ppos.z = 0;
    posheight = noise(ppos.x/float(dens)/float(divider),ppos.y/float(dens)/float(divider))*float(multi);
    lastz = posheight;
    seed = int(inven[24]);
    health = float(inven[25]);
    time = float(inven[26]);
    mouse.x = float(inven[27]);
    mouse.y = float(inven[28]);
    sense = float(inven[29]);
  }catch(Exception e){
    for(int x = 0; x < 5; x++){
      for(int y = 0; y < 3; y++){
        inventory[x][y] = new item();
      }
    }
    lefthand = new item(sworda,10,0.5,msworda);
    inarmour = new item(shielda,10);
    seed = int(random(10000));
    ppos.x = 100000+int(random(100000));
    ppos.y = 100000+int(random(100000));
  }
  updatehp();
  updateflower();
  updatelefthand();
  updaterighthand();
  noiseSeed(seed); 
  noCursor();
  initchunks();
}

void settings(){
  fullScreen(P3D,1);
  PJOGL.setIcon("images/sketch.png");
}

void draw(){
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
    bloom.set("skyr",red(currentsky)/float(255),green(currentsky)/float(255),blue(currentsky)/float(255));
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
    bloom.set("skyr",red(currentsky)/float(255),green(currentsky)/float(255),blue(currentsky)/float(255));
    bloomp.filter(bloom);
    if(km+kb == 25){
      fcd++;
      tints.set("alpha",fcd/180.0);
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
      float x1 = grounds.get(i).pos.x-ppos.x+wi*dens/2;
      float y1 = grounds.get(i).pos.y-ppos.y+wi*dens/2;
      if(dist(x,y,x1,y1) < chunkdist*0.75){
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


static final int[] getWindowLocation(final PApplet pa, int... xy) {
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
