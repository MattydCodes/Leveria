boolean menu = true;
void sunmoonmenu(){
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
    d3.ambientLight(255.0/4+10,255.0/4+10,255.0/4+10);
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
  time+=0.1;
  if(time > 359){
    time = 0;
  }
}
void cammenu(){
  int r = 100;
  float x = r * cos(radians(mouse.x));
  float y = r * sin(radians(mouse.x));
  mouse.x+=0.25;
  if(mouse.x >= 360){
    mouse.x = 1+(mouse.x-360);
  }
  d3.perspective(PI/3.0, float(width)/float(height), (height/2.0) / tan(PI/3.0/2.0)/8.0, (height/2.0) / tan(PI/3.0/2.0)*7.5);
  d3.camera(0,0,-500,-x*10,-y*10,-600,rz,0,1-rz);  
}
void drawbackground(){
  d3.beginDraw();
  sky();
  d3.endDraw();
  d3.beginDraw();
  sunmoonmenu();
  allcollision();
  posheight = lerp(posheight,noise(ppos.x/float(dens)/float(divider),ppos.y/float(dens)/float(divider))*float(multi),0.1);
  lastz = lerp(lastz,ppos.z,0.25);
  d3.translate(-ppos.x,-ppos.y,-posheight+lastz); 
  for(int i = 0; i < grounds.size(); i++){
    try{
      float x = -chunkdist/2 * cos(radians(mouse.x));
      float y = -chunkdist/2 * sin(radians(mouse.x));
      float x1 = grounds.get(i).pos.x-ppos.x;
      float y1 = grounds.get(i).pos.y-ppos.y;
      if(dist(x,y,x1,y1) < chunkdist*0.8){
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
void drawmenu(){
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
      seed = int(random(100000));
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
      ppos.x = int(ppos.x);
      ppos.y = int(ppos.y);
      chunkupdate();
      ppos.x = random(-1000000,1000000);
      ppos.x = int(ppos.x);
      ppos.y = random(-1000000,1000000);
      ppos.y = int(ppos.y);
      chunkupdate();
      initchunks();
      for(int x = 0; x < 5; x++){
        for(int y = 0; y < 3; y++){
          inventory[x][y] = new item();
        }
      }
      lefthand = new item(sworda,10,0.5,msworda);
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
  bloom.set("skyr",red(currentsky)/float(255),green(currentsky)/float(255),blue(currentsky)/float(255));
  bloomp.filter(bloom);
  image(d3,0,0,width,height);
  image(bloomp,0,0,width,height);
  image(ui,0,0,width,height);
}
