boolean[] keyp = new boolean[8];
int jumpvel = 0;
float prevheight;
PVector mouse = new PVector(0,0);
PVector lastmove = new PVector(0,0);
PVector righthandr = new PVector(0,0,0);
PVector lefthandr = new PVector(0,0,0);
float posheight = 0;
float wheel = 0;
float sense = 0.1;
boolean right = false;
void keyPressed(){
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
    particles.add(new particlesystem(new PVector(ppos.x,ppos.y,posheight),100, 50, 0.04, 0.005, 400, 10, 10, color(255,67,18),0,270,360));
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
    lefthand = new item(sworda,10,0.5,msworda);
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
    ppos.x = int(ppos.x/1000)*1000;
    ppos.y+=random(-100000,100000);
    ppos.y = int(ppos.y/1000)*1000;
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
      particles.add(new particlesystem(new PVector(ppos.x,ppos.y,posheight),100, 50, -0.02, 0.005, 400, 20, 20, color(255,255,255),0,270,360));
    }
    //particlesystem(PVector origin,int amount_, float maxv, float g, float d_, int lifespan, int w_, int h_, color c_){
  }
}

//------------------------------------------------------------------------------------------------------------------------------------------------------

void keyReleased(){
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

void mousePressed(){
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

void move(){
  float r = 16; //speed
  float x = r * cos(radians(mouse.x));
  float y = r * sin(radians(mouse.x));
  float p = 1;
  int keysp = 0;
  posheight = lerp(posheight,noise(ppos.x/float(dens)/float(divider),ppos.y/float(dens)/float(divider))*float(multi),0.1);
  lastmove.x = ppos.x;
  lastmove.y = ppos.y;
  if((posheight > ppos.z+posheight && entered == false) || (entered && ppos.z <= 0)){
    keyp[5] = false;
    jumpvel = 0;
    ppos.z = 0;
  }
  for(int i = 0; i < 4; i++){
    if(keyp[i]){
      p-=0.25;
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
      ppos.z+=(noise(ppos.x/float(dens)/float(divider),ppos.y/float(dens)/float(divider))*float(multi)-prevheight);                           
      prevheight = noise(ppos.x/float(dens)/float(divider),ppos.y/float(dens)/float(divider))*float(multi);
    }
    ppos.z+=jumpvel*0.3;
    jumpvel-=20*0.3;
  }
}

void mouseMoved(){
  if(iopen == false && sopen == false && itutorial == false && menu == false){
    mousemove();
  }
}
void mouseDragged(){
  if(iopen == false && sopen == false && itutorial == false && menu == false){
    mousemove();
  }
}  
void mousemove(){
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

void mouseWheel(MouseEvent event) {
  if(event.getCount() > 0){
    wheel = -1;
  }else{
    wheel = 1;
  }
}
