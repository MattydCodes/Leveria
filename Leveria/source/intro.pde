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
void runintro(){
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
void runbossoutro(){
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
        lefthand = new item(swordd,lefthand.stat*1.5,lefthand.cdreset,mswordd);
      }else if(righthand.empty == false){
        righthand = new item(swordd,righthand.stat*1.5,righthand.cdreset,mswordd);
      }else{
        lefthand = new item(swordd,50,0.25,mswordd);
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
      lefthand = new item(swordd,lefthand.stat*1.5,lefthand.cdreset,mswordd);
    }else if(righthand.empty == false){
      righthand = new item(swordd,righthand.stat*1.5,righthand.cdreset,mswordd);
    }else{
      lefthand = new item(swordd,50,0.25,mswordd);
    }
    introx = 0;
    introy = 0;
    updateui();
  }
  ui.textSize(16);
  ui.endDraw();
  image(ui,0,0,width,height);
}
void runcredits(){
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
