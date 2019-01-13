float time = 0;
color sunset = color(255,84,34);
color day = color(10,100,250);
color night = color(5,10,75);
color light = color(255,245,245);
color currentsky;
int daycount = 0;
void sky(){
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
void sunmoonlight(){
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
  time+=0.025;
  if(time > 359){
    time = 0;
    daycount++;
  }
}