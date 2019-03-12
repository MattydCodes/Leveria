//Class will be used to load the ground sections in "chunks".
boolean updatedcollision = true;
color snowbiome = color(255,255,255);
color grassbiome = color(31,227,93);
color desertbiome = color(253,250,179);
color prevbiome;
int treehit = 200;
int flowerhit = 300;
class ground{
  int biome;
  int density;
  PVector pos = new PVector(0,0);
  PVector res = new PVector(0,0);
  PShape object = createShape(GROUP);
  PShape[] clouds;
  PShape[] trees = new PShape[treecount];
  PVector[] tposs = new PVector[treecount];
  PShape[] flowers = new PShape[flowercount];
  PVector[] fposs = new PVector[flowercount];
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
    for(int i = 0; i < flowercount; i++){
      fposs[i] = new PVector(0,0,0);
    }
    for(int i = 0; i < treecount; i++){
      tposs[i] = new PVector(0,0,0);
    }
    if(biome != 3){
    for(int n = 0; n < treecount; n++){
      if(noise(pos.x,pos.y,n) < treespawnrate){
        tposs[n].x = (noise(pos.x/dens*n,n*n*pos.y/pos.x*n*n)-0.25)*2*wi*dens;
        tposs[n].y = (noise(pos.y/dens*n,n*n*pos.x/pos.y)-0.25)*2*wi*dens;
        boolean found = true;
        for(int i = 0; i < treecount; i++){
          if(dist(tposs[n].x,tposs[n].y,tposs[i].x,tposs[i].y) < 300 && tposs[i].z == 1){
            found = false;
          }
        }
        if(found){
          trees[n] = createTree(tposs[n].x,tposs[n].y,biome);
          object.addChild(trees[n]);
          tposs[n].z = 1;
        }
      }
    }
    for(int n = 0; n < flowercount; n++){
      if(noise(pos.x,pos.y,n) < flowerspawnrate){
        fposs[n].x = (noise(pos.x/dens*cos(n)*100,n*n*pos.y/pos.x*n*n)-0.25)*2*wi*dens;
        fposs[n].y = (noise(pos.y/dens*cos(sin(n))*100,n*n*pos.x/pos.y)-0.25)*2*wi*dens;
        boolean found = true;
        for(int i = 0; i < flowercount; i++){
          if(dist(fposs[n].x,fposs[n].y,fposs[i].x,fposs[i].y) < 200 && i != n){
            found = false;
          }
        }
        if(found){
          flowers[n] = createFlower(fposs[n].x,fposs[n].y);
          object.addChild(flowers[n]);
          fposs[n].z = 1;
        }
      }
    }
  }
//------------------------------------------------------------------------------------------------------------------------------------------------------

    //Put grass ect here
    PShape grass = createShape();
    grass.beginShape(TRIANGLES);
    float val = noise(pos.x/dens,pos.y/dens)*1.5;
    if(biome == 3){
      val*=0.1;
    }
    for(int i = 0; i < grassdens*val; i++){
      PImage grassc = createImage(1,1,RGB);
      if(biome == 1){
        grassc.pixels[0] = color(31,227,93);
      }else if(biome == 2){
        grassc.pixels[0] = color(255,255,255);
      }else{
        grassc.pixels[0] = color(253,250,179);
      }
      grass.texture(grassc);
      grass.noStroke();
      grass.noFill();
      grass.ambient(grassc.pixels[0]);
      float rndx = random(-1,1);
      float rndy = random(-1,1);
      float grassh = 0;
      if(noise((pos.x/dens+rndx+(noise(pos.x/float(dens)*i,i)-0.25)*2*wi)/divider,(pos.y/float(dens)+rndy+(noise(pos.y/dens,i)-0.25)*2*hi)/divider)*multi < noise((pos.x/float(dens)+(noise(pos.x/dens*i,i)-0.25)*2*wi)/divider,(pos.y/dens+(noise(pos.y/float(dens),i)-0.25)*2*hi)/divider)*multi){                                                                                        
        grassh=noise((pos.x/float(dens)+(noise(pos.x/float(dens)*i,i)-0.25)*2*wi)/divider,(pos.y/float(dens)+(noise(pos.y/float(dens),i)-0.25)*2*hi)/divider)*multi;
        //                       ((noise(pos.x/dens*i,i)-0.25)*2*wi*dens,               (noise(pos.y/dens,i)-0.25)*2*hi*dens,grassh,1,1);
      }else{
        grassh=noise((pos.x/float(dens)+rndx+(noise(pos.x/float(dens)*i,i)-0.25)*2*wi)/divider,(pos.y/float(dens)+rndy+(noise(pos.y/float(dens),i)-0.25)*2*hi)/divider)*multi;
        //                            ((noise(pos.x/dens*i,i)-0.25)*2*wi*dens,(noise(pos.y/dens,i)-0.25)*2*hi*dens,grassh,1,1);
      }
      rndx*=100;
      rndy*=100;
      grass.vertex((noise(pos.x/float(dens)*i,i)-0.25)*2*wi*dens,(noise(pos.y/float(dens),i)-0.25)*2*hi*dens,grassh,1,1);
      grass.vertex(((noise(pos.x/float(dens)*i,i)-0.25)*2*wi)*dens+rndx*0.25,((noise(pos.y/float(dens),i)-0.25)*2*hi)*dens+rndy*0.25,grassh-random(20,30),1,1);              
      grass.vertex(((noise(pos.x/float(dens)*i,i)-0.25)*2*wi)*dens+rndx*0.5,((noise(pos.y/float(dens),i)-0.25)*2*hi)*dens+rndy*0.5,grassh,1,1);
      grass.vertex(((noise(pos.x/float(dens)*i,i)-0.25)*2*wi)*dens+rndx*0.375,((noise(pos.y/float(dens),i)-0.25)*2*hi)*dens+rndy*0.325,grassh,1,1);
      grass.vertex(((noise(pos.x/float(dens)*i,i)-0.25)*2*wi)*dens+rndx*0.625,((noise(pos.y/float(dens),i)-0.25)*2*hi)*dens+rndy*0.625,grassh-random(20,30),1,1);      
      grass.vertex(((noise(pos.x/float(dens)*i,i)-0.25)*2*wi)*dens+rndx*0.875,((noise(pos.y/float(dens),i)-0.25)*2*hi)*dens+rndy*0.875,grassh,1,1);
      grass.vertex(((noise(pos.x/float(dens)*i,i)-0.25)*2*wi)*dens+rndx*0.75,((noise(pos.y/float(dens),i)-0.25)*2*hi)*dens+rndy*0.75,grassh,1,1);
      grass.vertex(((noise(pos.x/float(dens)*i,i)-0.25)*2*wi)*dens+rndx*0.875,((noise(pos.y/float(dens),i)-0.25)*2*hi)*dens+rndy*0.875,grassh-random(20,30),1,1);              
      grass.vertex(((noise(pos.x/float(dens)*i,i)-0.25)*2*wi)*dens+rndx,((noise(pos.y/float(dens),i)-0.25)*2*hi)*dens+rndy,grassh,1,1);
    }
    grass.endShape();
    object.addChild(grass);
  
//------------------------------------------------------------------------------------------------------------------------------------------------------

    if(noise(this.pos.x/dens,this.pos.y/dens) <= cloudspawnrate){
      float a = (noise(this.pos.x*this.pos.y/dens,this.pos.y/dens))*wi*dens;
      float b = (noise(this.pos.x/dens,this.pos.y*this.pos.x/dens))*wi*dens;
      float ah = noise((this.pos.x/dens)/float(divider),(this.pos.y/dens)/divider)*float(multi)-random(3000,3500);
      int rnd = int(random(4,6));
      clouds = new PShape[rnd];
      for(int i = 0; i < rnd; i++){
        clouds[i] = createCloud(a+random(-400,400),b+random(-400,400),ah+random(-100,100),600+random(-200,200));
        object.addChild(clouds[i]);
      }
    }
  }

//------------------------------------------------------------------------------------------------------------------------------------------------------

  PShape createCloud(float a, float b, float c, float r){
    PShape cloud = createShape();
    PImage texture = createImage(1,1,RGB);
    texture.loadPixels();
    texture.pixels[0] = color(253,253,253);
    PVector[][] shape = new PVector[12][12];
    for(int i = 0; i < 12; i++){
      float lon = map(i,0,11,-PI,PI);
      for(int j = 0; j < 12; j++){
        float lat = map(j,0,11,-HALF_PI,HALF_PI);
        float x = r * sin(lon) * cos(lat);
        float y = r * sin(lon) * sin(lat);
        float z = r * cos(lon);
        float t = sqrt(x*x)+sqrt(y*y)+sqrt(z*z);
        shape[i][j] = new PVector(x+noise(x/r+r+a,y/r+r+b,z/r+r)*(x/t)*r*1.5+a,y+noise(x/r+r+a,y/r+r+b,z/r+r)*(y/t)*r*1.5+b,z+noise(x*2/r+r+a,y*2/r+r+b,z*2/r+r)*(z/t)*r/4+c);     
      }
    }  
    for(int i = 0; i < 11; i++){
      cloud.beginShape(TRIANGLE_STRIP);
      cloud.emissive(255,255,255);
      cloud.noStroke();
      for(int j = 0; j < 12; j++){
        cloud.vertex(shape[i][j].x,shape[i][j].y,shape[i][j].z,1,1);
        cloud.vertex(shape[i+1][j].x,shape[i+1][j].y,shape[i+1][j].z,1,1);
      }
      cloud.endShape();
    }
    return(cloud);
  }
  
  PShape createTree(float x, float y, int biome){
    PImage treetexture = createImage(2,1,RGB);
    PShape tree;
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
    tree = createShape();
    tree.beginShape(TRIANGLE_STRIP);
    tree.noStroke();
    tree.texture(treetexture);
    tree.noFill();
    tree.ambient(treetexture.pixels[1]);
    for(int l = -1; l < 4; l++){
      for(int r = 0; r < 6; r++){
        float xoff = (80-l*2) * cos(radians(r*72));
        float yoff = (80-l*2) * sin(radians(r*72));
        tree.vertex(xoff+x,yoff+y,-l*120+noise(((pos.x+x)/dens)/float(divider),((pos.y+y)/dens)/divider)*float(multi),2,1);
        xoff = (80-(l+1)*2) * cos(radians(r*72));
        yoff = (80-(l+1)*2) * sin(radians(r*72));
        tree.vertex(xoff+x,yoff+y,-(l+1)*120+noise(((pos.x+x)/dens)/float(divider),((pos.y+y)/dens)/divider)*float(multi),2,1);
      }
    }
    tree.endShape();
    PVector[][] rnds = new PVector[5][6];
    for(int r = 0; r < 5; r++){
      for(int i = 0; i < 5; i++){
        rnds[4-i][r] = new PVector(random(-9*i,9*i),random(-9*i,9*i),random(-9*i,9*i));
      }
      rnds[r][5] = rnds[r][0];
    }
    for(int l = 0; l < 3; l++){
      tree.beginShape(TRIANGLE_STRIP);
      tree.noStroke();
      tree.texture(treetexture);
      tree.noFill();
      tree.ambient(treetexture.pixels[0]);
      for(int r = 0; r < 6; r++){
        float xoff = (500-l*133) * cos(radians(r*72));
        float yoff = (500-l*133) * sin(radians(r*72));
        tree.vertex(xoff+x+rnds[l][r].x,yoff+y+rnds[l][r].y,-l*250+noise(((pos.x+x)/dens)/float(divider),((pos.y+y)/dens)/divider)*float(multi)-120,1,1);
        xoff = (400-(l+1)*133) * cos(radians(r*72));
        yoff = (400-(l+1)*133) * sin(radians(r*72));
        tree.vertex(xoff+x+rnds[l+1][r].x,yoff+y+rnds[l+1][r].y,-(l+1)*250+noise(((pos.x+x)/dens)/float(divider),((pos.y+y)/dens)/divider)*float(multi)-180,1,1);
      }
      tree.vertex(x,y,-(l+1)*150+noise(((pos.x+x)/dens)/float(divider),((pos.y+y)/dens)/divider)*float(multi)-180,1,1);
    }
    tree.endShape();
    return(tree);
  }
//------------------------------------------------------------------------------------------------------------------------------------------------------

  PShape createFlower(float x, float y){
    PShape f = createShape();
    PImage ftext = createImage(3,1,RGB);
    ftext.pixels[0] = color(10,255,50);
    ftext.pixels[1] = color(10,10,10);
    ftext.pixels[2] = color(255,0,0);
    f.beginShape(TRIANGLE_STRIP);
    f.emissive(5,25,25);
    f.noFill();
    f.noStroke();
    f.ambient(ftext.pixels[0]);
    f.texture(ftext);
    float ground = noise((this.pos.x/dens+x/dens)/divider,(this.pos.y/dens+y/dens)/divider)*multi;
    for(int r = 0; r < 6; r++){
        float xoff = 4 * cos(radians(r*72));
        float yoff = 4 * sin(radians(r*72));
        f.vertex(xoff+x,yoff+y,ground,1,1);
        f.vertex(xoff+x,yoff+y,-60+ground,1,1);
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
    d3.translate(pos.x,pos.y);
    d3.shape(object);
    try{
    for(int i = 0; i < clouds.length; i++){
      d3.shape(clouds[i]);
    }
    }catch(Exception e){
    }
    d3.translate(-pos.x,-pos.y);
  }
  
//-----------------------------------------------------------------------------------------------------------------------/-------------------------

  void collisions(boolean interact){
    if(dist(pos.x,pos.y,ppos.x,ppos.y) < wi*dens*2){
      for(int i = 0; i < treecount; i++){
        if(dist(ppos.x,ppos.y,tposs[i].x+pos.x,tposs[i].y+pos.y) < treehit && tposs[i].z == 1){
          ppos.x+=lastmove.x;
          ppos.y+=lastmove.y;
        }
      }
      for(int i = 0; i < flowercount; i++){
        if(dist(ppos.x,ppos.y,fposs[i].x+pos.x,fposs[i].y+pos.y) < flowerhit && fposs[i].z == 1 && interact && flowerc < 5){
          object.removeChild(object.getChildIndex(flowers[i]));
          flowerc++;
          flowerstotal++;
          updateflower();
          fposs[i].z = 0;
          fpickup.stop();
          fpickup.play();
        }
      }
    }
  }
}

//------------------------------------------------------------------------------------------------------------------------------------------------------

void initchunks(){
  //cx = int(int(ppos.x)/(wi*dens))*(wi*dens);
  int gx = int(int(ppos.x-int(wi*dens/2))/(wi*dens))*(wi*dens);
  int gy = int(int(ppos.y+int(wi*dens/2))/(wi*dens))*(wi*dens);
  for(int x = 0; x < chunkdist/wi/dens*2; x++){
    for(int y = 0; y < chunkdist/wi/dens*2; y++){
      grounds.add(new ground(gx+(x-chunkdist/wi/dens)*wi*dens,gy+(y-chunkdist/wi/dens)*wi*dens,wi,hi,dens));
    }
  }
  chunkupdate();
  ppos.x-=wi*dens;
  ppos.y-=wi*dens;
  chunkupdate();
  ppos.x+=wi*dens;
  ppos.y+=wi*dens;
  chunkupdate();
  ppos.x+=wi*dens;
  ppos.y+=wi*dens;
  chunkupdate();
  ppos.x-=wi*dens;
  ppos.y-=wi*dens;
  chunkupdate();
}

//------------------------------------------------------------------------------------------------------------------------------------------------------

void allcollision(){
  try{
  for(int i = 0; i < grounds.size(); i++){
    grounds.get(i).collisions(keyp[7]);
  }
  }catch(Exception e){
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

//------------------------------------------------------

void cloudtextmanager(){
  cloudtext.loadPixels();
  for(int i = 0; i < grounds.size(); i++){
    try{
      for(int j = 0; j < grounds.get(i).clouds.length; j++){
        int x = 51 + int((ppos.x-grounds.get(i).pos.x)/float(wi*dens)*50);
        int y = int((ppos.y-grounds.get(i).pos.y)/float(wi*dens)*50)+int(time*10);
        if(y <= 0){
          y = 3600-y;
        }else if(y >= 3600){
          y = y-3600;
        }
        int index = x + y * 101;
        grounds.get(i).clouds[j].setEmissive(cloudtext.pixels[index]);
      }
    }catch(Exception e){
      println(e + "CLOUDS");
    }
  }
}
