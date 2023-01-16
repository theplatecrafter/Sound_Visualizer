import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.ugens.*;

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
  size(1024,800,P3D);
  //fullScreen(P3D);

  
  minim = new Minim(this);
  song = new MultiChannelBuffer(1000,1);
  float sampleRate = minim.loadFileIntoBuffer("shortTest.mp3",song);

  println(song.getChannelCount());
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