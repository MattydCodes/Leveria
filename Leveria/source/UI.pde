PGraphics ui;
boolean iopen = false;
boolean sopen = false;
boolean cused = false;
float armourc = 0;
float damagec = 0;
float cdc = 0;
float cost = 0;
void updatehp(){
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
void updateflower(){
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
void updatelefthand(){
  ui.beginDraw();
  if(mouseX < 190*(width/1920.0) && mouseX > 110*(width/1920.0) && mouseY < 1040*(height/1080.0) && mouseY > 960*(height/1080.0)){
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
void updaterighthand(){
  ui.beginDraw();
  if(mouseX < 280*(width/1920.0) && mouseX > 200*(width/1920.0) && mouseY < 1040*(height/1080.0) && mouseY > 960*(height/1080.0)){
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
void updatearmour(){
  ui.beginDraw();
  if(mouseX < 370*(width/1920.0) && mouseX > 290*(width/1920.0) && mouseY < 1040*(height/1080.0) && mouseY > 960*(height/1080.0)){
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
void updatekillcount(){
  ui.beginDraw();
  ui.stroke(0);
  ui.strokeWeight(4);
  ui.fill(50);
  qud(1840,960,40,40);
  ui.fill(255);
  ui.text(str(km+kb)+"/25",1820,1007);
  ui.endDraw();
}
void updateui(){
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
void openinventory(){
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
      if(mouseX < (250+x*220+40)*(width/1920.0) && mouseX > (250+x*220-40)*(width/1920.0) && mouseY > (290+y*220)*(height/1080.0) && mouseY < (290+y*220+80)*(height/1080.0)){
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
            ui.text("Speed : " + round(inventory[x][y].cdreset*100)/float(100) + "s",255+x*220+40,355+y*220);
            ui.text("Value : " + int((inventory[x][y].stat*10)/(inventory[x][y].cdreset)) + " SF",255+x*220+40,380+y*220);
          }else{
            if(inventory[x][y].img == shielda){
              ui.text("Wooden Shield",255+x*220+40,290+y*220,390+x*220+40,50+y*220+80);
            }else if(inventory[x][y].img == shieldb){
              ui.text("Brass Shield",255+x*220+40,290+y*220,390+x*220+40,50+y*220+80);
            }else if(inventory[x][y].img == shieldc){
              ui.text("Divine Shield",255+x*220+40,290+y*220,390+x*220+40,50+y*220+80);
            }
            ui.text("Value : " + int(inventory[x][y].stat*10) + " SF",255+x*220+40,355+y*220);
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
  if(mouseX < (1500+40)*(width/1920.0) && mouseX > (1500-40)*(width/1920.0) && mouseY > 780*(height/1080.0) && mouseY < (780+80)*(height/1080.0)){
    ui.fill(100);
    if(mousePressed && cused == false && inhand.empty == false){
      if(inhand.weapon){
        points+=int((inhand.stat*10)/(inhand.cdreset));
      }else{
        points+=int(inhand.stat*10);
      }
      inhand = new item();
    }
  }else{
    ui.fill(50);
  }
  qud(1500,780,40,40);
  ui.image(bin,1460,780,80,80);
  if(mouseX < (1500+40)*(width/1920.0) && mouseX > (1500-40)*(width/1920.0) && mouseY > 680*(height/1080.0) && mouseY < (680+80)*(height/1080.0)){
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
  ui.text("Armour: " + int(inarmour.stat) + "%",1400,290);
  ui.text("Weapon 1 : " + int(lefthand.stat) + " (" + round(lefthand.cdreset*100)/float(100) + "s)",1400,310);
  ui.text("Weapon 2 : " + int(righthand.stat) +  " (" + round(righthand.cdreset*100)/float(100) + "s)",1400,330);
  ui.text("Days Survived: " + daycount,1400,350);
  ui.text("Flowers Picked : " + flowerstotal,1400,370);
  ui.text("Shade Fragments : " + int(points),1400,390);
  if(inforge.empty == false){
    if(inforge.weapon){
      ui.text("Armour Increase : 0% (0%)",1300,540);
      ui.text("Damage Increase : " + damagec + " (" + (inforge.stat+damagec) + ")",1300,580);
      ui.text("Speed Increase : " + cdc + "("+ (round((inforge.cdreset-cdc)*100)/float(100)) +")",1300,620);
    }else{
      ui.text("Armour Increase : " + armourc + "% (" + (inforge.stat+armourc) +")",1300,540);
      ui.text("Damage Increase : 0 (0)",1300,580);
      ui.text("Speed Increase : 0 (0)",1300,620);
    }
  }
  if(inforge.empty == false){
    if(inforge.weapon){
      cost=int((inforge.stat*10+damagec*10)/(inforge.cdreset-cdc));
    }else{
      cost=int((armourc+inforge.stat)*10);
    }
    ui.text("Cost : " + cost + " SF",1300,660);
  }else{
    cost = 0;
  }
  ui.fill(50);
  if(mouseX < (1600+20)*(width/1920.0) && mouseX > (1600-20)*(width/1920.0) && mouseY > 510*(height/1080.0) && mouseY < 550*(height/1080.0) && inforge.weapon == false){
    if(wheel != 0 && inforge.weapon == false){
      armourc+=0.5*wheel;
      if(inforge.stat+armourc > 95 || armourc < 0){
        armourc-=0.5*wheel;
      }
      wheel = 0;
    }
    ui.fill(100);
  }
  qud(1600,510,20,20);
  ui.fill(50);
  if(mouseX < (1600+20)*(width/1920.0) && mouseX > (1600-20)*(width/1920.0) && mouseY > 550*(height/1080.0) && mouseY < 590*(height/1080.0) && inforge.weapon){
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
  if(mouseX < (1600+20)*(width/1920.0) && mouseX > (1600-20)*(width/1920.0) && mouseY > 590*(height/1080.0) && mouseY < 630*(height/1080.0) && inforge.weapon){
    if(wheel != 0 && inforge.weapon){
      cdc+=wheel/100;
      if(cdc < 0 || inforge.cdreset-cdc < 0.01){
        cdc-=wheel/100;
      }
      cdc = round((cdc*100))/float(100);
      wheel = 0;
    }
    ui.fill(100);
  }
  qud(1600,590,20,20);
  ui.fill(50);
  if(mouseX < (1680+40)*(width/1920.0) && mouseX > (1680-40)*(width/1920.0) && mouseY > 530*(height/1080.0) && mouseY < 610*(height/1080.0) && cused == false && cost <= points){
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
void opensettings(){
  ui.beginDraw();
  ui.fill(50);
  ui.stroke(0);
  ui.strokeWeight(10);
  ui.rect(576,20,576,1040);
  ui.strokeWeight(5);
  if(mouseX > 940*(width/1920.0) && mouseX < 980*(width/1920.0) && mouseY > 65*(height/1080.0) && mouseY < 105*(height/1080.0)){
    sense+=wheel*0.01;
    sense = round(sense*100)/float(100);
    sense = constrain(sense,0.01,1);
    wheel = 0;
    ui.fill(100);
  }
  qud(960,65,20,20);
  ui.fill(50);
  if(mouseX > 756*(width/1920.0) && mouseX < 796*(width/1920.0) && mouseY > 940*(height/1080.0) && mouseY < 980*(height/1080.0)){ //save
    ui.fill(100);
    if(mousePressed && cused == false){
      saveinventory();
      
      cused = true;
    }
  }
  qud(776,940,20,20);
  ui.fill(50);
  if(mouseX > 1010*(width/1920.0) && mouseX < 1050*(width/1920.0) && mouseY > 940*(height/1080.0) && mouseY < 980*(height/1080.0)){ //savequit
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
void qud(float sx, float sy, float dx, float dy){
  ui.quad(sx,sy,sx+dx,sy+dy,sx,sy+dy*2,sx-dx,sy+dy);
}
void saveinventory(){
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

String itemtostring(item i){
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

item stringtoitem(String i){
  if(i.equals("e")){
    return(new item());
  }
  String type = i.substring(0,1);
  if(i.substring(1,2).equals("!")){
    int index = i.indexOf("&");
    float dmg = float(i.substring(2,index-1));
    float cdreset = float(i.substring(index+1,i.length()));
    if(type.equals("a")){
      return(new item(sworda,dmg,cdreset,msworda));
    }else if(type.equals("b")){
      return(new item(swordb,dmg,cdreset,mswordb));
    }else{
      return(new item(swordc,dmg,cdreset,mswordc));
    }
  }else{
    float stat = float(i.substring(2,i.length()));
    if(type.equals("a")){
      return(new item(shielda,stat));
    }else if(type.equals("b")){
      return(new item(shieldb,stat));
    }else{
      return(new item(shieldc,stat));
    }
  }
}
