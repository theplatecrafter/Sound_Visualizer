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
FFT Lfft;
FFT Rfft;

//minim output vars
float[] spectrum = new float[bands];

float[] leftChannel;
float[] rightChannel;

//other vars
int lastCount;
int renderCount = 0;

void setup(){
  size(1024,800,P3D);
  //fullScreen(P3D);


  minim = new Minim(this);
  song = new MultiChannelBuffer(1,1);
  float sampleRate = minim.loadFileIntoBuffer("shortTest.mp3", song);

  leftChannel = song.getChannel(0);
  rightChannel = song.getChannel(1);

  Lfft = new FFT(bands * 2, sampleRate);
  Rfft = new FFT(bands * 2, sampleRate);
  float samplesPerFrame = framePeriod * sampleRate;
  
  int N = song.getBufferSize();
  float songDuration = N / sampleRate;
  lastCount = int(N / samplesPerFrame);


  println(song.getChannelCount());
}

void draw(){
  renderCount++;

  //analysis batch
  if(batch){

  }

  //check end
  if(renderCount >= lastCount){
    exit();
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