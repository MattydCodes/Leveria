float time = 0;
color sunset = color(255,84,34);
color day = color(10,100,250);
color night = color(0,0,0);
color light = color(255,245,245);
color currentsky;
int daycount = 0;
void sky(){
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
void sunmoonlight(){
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
  time+=0.03;
  if(time > 359){
    time = time-360;
    daycount++;
  }
}
