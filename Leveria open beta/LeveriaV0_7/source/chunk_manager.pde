boolean updatedchunks = true;
PVector lastchunk = new PVector(0,0);
void chunkupdate(){
  int cx = int(int(ppos.x)/(wi*dens))*(wi*dens);
  int cy = int(int(ppos.y)/(hi*dens))*(hi*dens);
  if(cx > lastchunk.x){
    for(int i = grounds.size()-1; i > -1; i--){
      if(grounds.get(i).pos.x < cx-chunkdist/2){
        grounds.remove(i);
      }
    }
    for(int y = 0; y < chunkdist/wi/dens+1; y++){
      grounds.add(new ground(cx+chunkdist/2,cy-chunkdist/2+y*wi*dens,wi,hi,dens));
    }
    lastchunk.x = cx;
  }else if(cx < lastchunk.x){
    for(int i = grounds.size()-1; i > -1; i--){
      if(grounds.get(i).pos.x > cx+chunkdist/2){
        grounds.remove(i);
      }
    }
    for(int y = 0; y < chunkdist/wi/dens+1; y++){
      grounds.add(new ground(cx-chunkdist/2,cy-chunkdist/2+y*wi*dens,wi,hi,dens));
    }
    lastchunk.x = cx;  
  }
  
//------------------------------------------------------------------------------------------------------------------------------------------------------

  if(cy > lastchunk.y){
    for(int i = grounds.size()-1; i > -1; i--){
      if(grounds.get(i).pos.y < cy-chunkdist/2){
        grounds.remove(i);
      }
    }
    for(int x = 0; x < chunkdist/wi/dens+1; x++){
      grounds.add(new ground(cx-chunkdist/2+x*wi*dens,cy+chunkdist/2,wi,hi,dens));
    }
    lastchunk.y = cy;
  }else if(cy < lastchunk.y){
    for(int i = grounds.size()-1; i > -1; i--){
      if(grounds.get(i).pos.y > cy+chunkdist/2){
        grounds.remove(i);
      }
    }
    for(int x = 0; x < chunkdist/wi/dens+1; x++){
      grounds.add(new ground(cx-chunkdist/2+x*wi*dens,cy-chunkdist/2,wi,hi,dens));
    }
    lastchunk.y = cy;  
  }
 
//-------------------------------------------------------------------------------------------------------------------------------------------------------
  updatedchunks = true;
}