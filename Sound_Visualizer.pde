import ddf.minim.*;
import ddf.minim.analysis.*;

///developer stuff
boolean debugMode = false;
boolean batch = false;
///

//minim stuff
int bands = 1024;
Minim minim;
MultiChannelBuffer song;

//minim output vars
float[] spectrum = new float[bands];

void setup(){
  if(debugMode){
    size(1024,512,P3D);
  }else{
    fullScreen(P3D);
  }

  minim = new Minim(this);
  song = minim.MultiChannelBuffer(1,1);
}

void draw(){
  //analysis batch
  if(batch){

  }


  //draw
  render(spectrum);
}

void render(float[] spec){
  background(0);
  stroke(255,0,0);
  noFill();
  ellipse(width/2,height/2,spec[0]*100+40,spec[0]*100+40);
}