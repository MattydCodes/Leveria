//Class will be used to load the ground sections in "chunks".
boolean updatedcollision = true;
color snowbiome = color(255,255,255);
color grassbiome = color(31,227,93);
color desertbiome = color(253,250,179);
color prevbiome;
int treehit = 150;
int flowerhit = 300;
class ground{
  int biome;
  int density;
  PVector pos = new PVector(0,0);
  PVector fpos = new PVector(0,0,0);
  PVector tpos;
  PVector res = new PVector(0,0);
  PShape object = createShape(GROUP);
  PShape flower;
  PShape branch;
  PShape row;
  ground(float x, float y, int w, int h, int d){
    pos.x = x;
    pos.y = y;
    res.x = w+1;
    res.y = h+1;
    density = d;
    float[][] values = new float[int(res.x)][int(res.y)];
  
//------------------------------------------------------------------------------------------------------------------------------------------------------

    for(int i = 0; i < res.x; i++){
      for(int j = 0; j < res.y; j++){    
        values[i][j] = noise((this.pos.x/dens+i)/float(divider),(this.pos.y/dens+j)/divider)*float(multi);
      }
    }
    
//------------------------------------------------------------------------------------------------------------------------------------------------------

    PImage texture = createImage(int(res.x),int(res.y),RGB);
    texture.loadPixels();
    float avgval = 0;
    for(int i = 0; i < res.x; i++){
      for(int j = 0; j < res.y; j++){   
        int index = i + j * int(res.x);
        float val = noise((pos.x/dens+i)/1000,(pos.y/dens+j)/1000,((pos.x/dens+i)-(pos.y/dens+j))/1000);
        avgval+=val;
        if(val >= 0.3333 && val < 0.6666){
          texture.pixels[index] = grassbiome; 
        }else if(val >= 0.6666){
          texture.pixels[index] = snowbiome;
        }else{
          texture.pixels[index] = desertbiome;          
        }
      }
    }  
    avgval/=res.x*res.y;
    if(avgval >= 0.3333 && avgval < 0.6666){
      biome = 1;
    }else if(avgval >= 0.6666){
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

    tpos = new PVector(0,0,0);
    float record = 100;
    for(int i = 1; i < res.x-1; i++){
      for(int j = 1; j < res.y-1; j++){
        float total = 0;
        for(int xi = -1; xi < 2; xi++){
          for(int yi = -1; yi < 2; yi++){
            total+=noise(xi+pos.x/dens,yi+pos.y/dens,values[i][j]);
          }
        }
        total/=9;
        if(total < record){
          record = total;
          tpos.x = i*dens;
          tpos.y = j*dens;
        }
      }
    }
    if(record < treespawnrate){
      tpos.z = 1;
      PImage treetexture = createImage(2,1,RGB);
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
      //treetexture.pixels[1] = color(107,74,4);
      branch = createShape();
      branch.beginShape(TRIANGLE_STRIP);
      branch.noStroke();
      branch.texture(treetexture);
      branch.noFill();
      branch.ambient(treetexture.pixels[1]);
      for(int l = -1; l < 9; l++){
        for(int r = 0; r < 6; r++){
          float xoff = (50-l*2) * cos(radians(r*72));
          float yoff = (50-l*2) * sin(radians(r*72));
          branch.vertex(xoff+tpos.x,yoff+tpos.y,-l*60+values[int(tpos.x/dens)][int(tpos.y/dens)],2,1);
          xoff = (50-(l+1)*2) * cos(radians(r*72));
          yoff = (50-(l+1)*2) * sin(radians(r*72));
          branch.vertex(xoff+tpos.x,yoff+tpos.y,-(l+1)*60+values[int(tpos.x/dens)][int(tpos.y/dens)],2,1);
        }
      }
      branch.endShape(CLOSE);
      object.addChild(branch);
      PVector[][] rnds = new PVector[5][6];
      for(int r = 0; r < 5; r++){
        for(int i = 0; i < 5; i++){
          rnds[4-i][r] = new PVector(random(-9*i,9*i),random(-9*i,9*i),random(-9*i,9*i));
        }
        rnds[r][5] = rnds[r][0];
      }
      row = createShape();
      for(int l = 0; l < 3; l++){
        row.beginShape(TRIANGLE_STRIP);
        row.noStroke();
        row.texture(treetexture);
        row.noFill();
        row.ambient(treetexture.pixels[0]);
        for(int r = 0; r < 6; r++){
          float xoff = (330-l*88) * cos(radians(r*72));
          float yoff = (330-l*88) * sin(radians(r*72));
          row.vertex(xoff+tpos.x+rnds[l][r].x,yoff+tpos.y+rnds[l][r].y,-l*150+values[int(tpos.x/dens)][int(tpos.y/dens)]+rnds[l][r].z-120,1,1);
          xoff = (264-(l+1)*88) * cos(radians(r*72));
          yoff = (264-(l+1)*88) * sin(radians(r*72));
          row.vertex(xoff+tpos.x+rnds[l+1][r].x,yoff+tpos.y+rnds[l+1][r].y,-(l+1)*150+values[int(tpos.x/dens)][int(tpos.y/dens)]+rnds[l+1][r].z-180,1,1);
        }
      }
      row.endShape();
      object.addChild(row);
    }
    
//------------------------------------------------------------------------------------------------------------------------------------------------------

    //Put grass ect here
    PShape grass = createShape();
    float val = noise(pos.x/dens,pos.y/dens);
    for(int i = 0; i < grassdens-val*grassdens; i++){
      PImage grassc = createImage(1,1,RGB);
      if(biome == 1){
        grassc.pixels[0] = color(31,227,93);
      }else if(biome == 2){
        grassc.pixels[0] = color(255,255,255);
      }else{
        grassc.pixels[0] = color(253,250,179);
      }
      grass.beginShape(TRIANGLES);
      grass.texture(grassc);
      grass.noStroke();
      grass.noFill();
      grass.ambient(grassc.pixels[0]);
      float rndx = random(-1,1);
      float rndy = random(-1,1);
      float grassh = 0;
      if(noise((pos.x/dens+rndx+(noise(pos.x/dens*i,i)-0.5)*2*wi)/divider,(pos.y/dens+rndy+(noise(pos.y/dens,i)-0.5)*2*hi)/divider)*multi < noise((pos.x/dens+(noise(pos.x*i/dens,i)-0.5)*2*wi)/divider,(pos.y/dens+(noise(pos.y/dens,i)-0.5)*2*hi)/divider)*multi){                                                                                        
        grassh=noise((pos.x/dens+(noise(pos.x/dens*i,i)-0.5)*2*wi)/divider,(pos.y/dens+(noise(pos.y/dens,i)-0.5)*2*hi)/divider)*multi;
      }else{
        grassh=noise((pos.x/dens+rndx+(noise(pos.x*i/dens,i)-0.5)*2*wi)/divider,(pos.y/dens+rndy+(noise(pos.y/dens,i)-0.5)*2*hi)/divider)*multi;
      }
      grass.vertex((noise(pos.x/dens*i,i)-0.5)*2*wi*dens,(noise(pos.y/dens,i)-0.5)*2*hi*dens,grassh,1,1);
      grass.vertex(((noise(pos.x/dens*i,i)-0.5)*2*wi+rndx*0.25)*dens,((noise(pos.y/dens,i)-0.5)*2*hi+rndy*0.25)*dens,grassh-random(20,30),1,1);              
      grass.vertex(((noise(pos.x/dens*i,i)-0.5)*2*wi+rndx*0.5)*dens,((noise(pos.y/dens,i)-0.5)*2*hi+rndy*0.5)*dens,grassh,1,1);
      grass.vertex(((noise(pos.x/dens*i,i)-0.5)*2*wi+rndx*0.375)*dens,((noise(pos.y/dens,i)-0.5)*2*hi+rndy*0.325)*dens,grassh,1,1);
      grass.vertex(((noise(pos.x/dens*i,i)-0.5)*2*wi+rndx*0.625)*dens,((noise(pos.y/dens,i)-0.5)*2*hi+rndy*0.625)*dens,grassh-random(20,30),1,1);      
      grass.vertex(((noise(pos.x/dens*i,i)-0.5)*2*wi+rndx*0.875)*dens,((noise(pos.y/dens,i)-0.5)*2*hi+rndy*0.875)*dens,grassh,1,1);
      grass.vertex(((noise(pos.x/dens*i,i)-0.5)*2*wi+rndx*0.75)*dens,((noise(pos.y/dens,i)-0.5)*2*hi+rndy*0.75)*dens,grassh,1,1);
      grass.vertex(((noise(pos.x/dens*i,i)-0.5)*2*wi+rndx*0.875)*dens,((noise(pos.y/dens,i)-0.5)*2*hi+rndy*0.875)*dens,grassh-random(20,30),1,1);              
      grass.vertex(((noise(pos.x/dens*i,i)-0.5)*2*wi+rndx)*dens,((noise(pos.y/dens,i)-0.5)*2*hi+rndy)*dens,grassh,1,1);
      grass.endShape();
    }
    object.addChild(grass);
    
//------------------------------------------------------------------------------------------------------------------------------------------------------

    if(noise(this.pos.x/dens,this.pos.y/dens) <= flowerspawnrate){
      fpos.z = 1;
      fpos.x = noise(pos.x/dens,values[0][0]/multi*divider)*wi*dens;
      fpos.y = noise(pos.y/dens,values[0][0]/multi*divider)*wi*dens;
      flower = createFlower(fpos.x,fpos.y);
      object.addChild(flower);
    }
  
//------------------------------------------------------------------------------------------------------------------------------------------------------

    if(noise(this.pos.x/dens,this.pos.y/dens) <= cloudspawnrate){
      float a = (noise(this.pos.x*this.pos.y/dens,this.pos.y/dens)-0.5)*wi*dens*2;
      float b = (noise(this.pos.x/dens,this.pos.y*this.pos.x/dens)-0.5)*wi*dens*2;
      float ah = noise((this.pos.x/dens)/float(divider),(this.pos.y/dens)/divider)*float(multi)-random(2000,2500);
      int rnd = int(random(4,6));
      for(int i = 0; i < rnd; i++){
        object.addChild(createCloud(a+random(-400,400),b+random(-400,400),ah+random(-100,100),300+random(-50,50)));
      }
      //object.addChild(createCloud(a,b,ah,150));
    }
  }

//------------------------------------------------------------------------------------------------------------------------------------------------------

  PShape createCloud(float a, float b, float c, float r){
    PShape cloud = createShape();
    PImage texture = createImage(1,1,RGB);
    texture.loadPixels();
    texture.pixels[0] = color(255,255,255);
    PVector[][] shape = new PVector[21][21];
    for(int i = 0; i < 21; i++){
      float lon = map(i,0,20,-PI,PI);
      for(int j = 0; j < 21; j++){
        float lat = map(j,0,20,-HALF_PI,HALF_PI);
        float x = r * sin(lon) * cos(lat);
        float y = r * sin(lon) * sin(lat);
        float z = r * cos(lon);
        float t = sqrt(x*x)+sqrt(y*y)+sqrt(z*z);
        shape[i][j] = new PVector(x+noise(x/r+r+a,y/r+r+b,z/r+r)*(x/t)*r*1.5+a,y+noise(x/r+r+a,y/r+r+b,z/r+r)*(y/t)*r*1.5+b,z+noise(x*2/r+r+a,y*2/r+r+b,z*2/r+r)*(z/t)*r/4+c);     
      }
    }  
    for(int i = 0; i < 20; i++){
      cloud.beginShape(TRIANGLE_STRIP);
      cloud.ambient(255);
      cloud.noStroke();
      cloud.texture(texture);
      for(int j = 0; j < 21; j++){
        cloud.vertex(shape[i][j].x,shape[i][j].y,shape[i][j].z,1,1);
        cloud.vertex(shape[i+1][j].x,shape[i+1][j].y,shape[i+1][j].z,1,1);
      }
      cloud.endShape();
    }
    return(cloud);
  }
  
//------------------------------------------------------------------------------------------------------------------------------------------------------

  PShape createFlower(float x, float y){
    PShape f = createShape();
    PImage ftext = createImage(3,1,RGB);
    ftext.pixels[0] = color(10,255,50);
    ftext.pixels[1] = color(0,0,0);
    ftext.pixels[2] = color(255,0,0);
    f.beginShape(TRIANGLE_STRIP);
    f.noFill();
    f.noStroke();
    f.ambient(ftext.pixels[0]);
    f.texture(ftext);
    float ground = noise((this.pos.x/dens+x/dens)/divider,(this.pos.y/dens+y/dens)/divider)*multi;
    for(int i = 0; i < 4; i++){
      for(int r = 0; r < 6; r++){
        float xoff = 4 * cos(radians(r*72));
        float yoff = 4 * sin(radians(r*72));
        f.vertex(xoff+x,yoff+y,-i*15+ground,1,1);
        f.vertex(xoff+x,yoff+y,-(i+1)*15+ground,1,1);
      }
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

  void display(){
    d3.noFill();
    d3.noStroke();
    d3.translate(this.pos.x,this.pos.y);
    d3.shape(object);
    d3.translate(-this.pos.x,-this.pos.y);
  }
  
//-----------------------------------------------------------------------------------------------------------------------------------------------------

  void collisions(boolean interact){
    if(dist(pos.x,pos.y,ppos.x,ppos.y) < 2000){
      if(dist(ppos.x,ppos.y,tpos.x+pos.x,tpos.y+pos.y) < treehit && tpos.z == 1){
        ppos.x+=lastmove.x;
        ppos.y+=lastmove.y;
      }
      if(dist(ppos.x,ppos.y,fpos.x+pos.x,fpos.y+pos.y) < flowerhit && interact && fpos.z == 1 && flowerc < 5){
        object.removeChild(object.getChildIndex(flower));
        flowerc++;
        flowerstotal++;
        updateflower();
        fpos.z = 0;
      }
    }
  }
}

//------------------------------------------------------------------------------------------------------------------------------------------------------

void initchunks(){
  chunkupdate();
  for(int x = 0; x < chunkdist/wi/dens; x++){
    for(int y = 0; y < chunkdist/wi/dens; y++){
      grounds.add(new ground(int(int(ppos.x)/(wi*dens))*(wi*dens)-chunkdist/2+x*wi*dens,int(int(ppos.y)/(wi*dens))*(wi*dens)-chunkdist/2+y*hi*dens,wi,hi,dens));
    }
  }
  chunkupdate();
}

//------------------------------------------------------------------------------------------------------------------------------------------------------

void allcollision(){
  boolean interact = keyp[7];
  for(int i = 0; i < grounds.size(); i++){
    grounds.get(i).collisions(interact);
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