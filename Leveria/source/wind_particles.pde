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
    pos.z = noise((pos.x/dens)/float(divider),(pos.y/dens)/divider)*float(multi);
  }
  void move(){
    float x = windvel.x * cos(radians(windvel.y));
    float y = windvel.x * sin(radians(windvel.y));
    pos.x+=x;
    pos.y+=y;
    pos.z=lerp(pos.z,noise((pos.x/dens)/float(divider),(pos.y/dens)/divider)*float(multi),0.1);
    rh=lerp(rh,random(rh-10,rh+10),0.01);
    if(dist(pos.x,pos.y,ppos.x,ppos.y) > chunkdist/4 || duration > 1000){
      dead = true;
    }
  }
  void display(){
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

void wind(){
  windvel.x=lerp(windvel.x,windvelf.x,0.005);
  wind.amp(map(windvel.x,1,20,0.0005,0.1));
  float channel = sin(radians(mouse.x-windvel.x+90))*-1;
  wind.pan(channel);
  windvel.y=lerp(windvel.y,windvelf.y,0.005);
  if(dist(windvel.x,windvel.y,windvelf.x,windvelf.y) < 0.1){
    windvelf = new PVector(random(5,20),random(360));
  }
  d3.ambientLight(255,255,255);
  for(int i = windps.size()-1; i > -1; i--){
    windps.get(i).display();
    if(windps.get(i).dead){
      windps.remove(i);
    }
  }
  if(windps.size() < int(map(windvel.x,5,20,5,100))){
    windps.add(new windparticle());
  }
}

void movepf(){
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
