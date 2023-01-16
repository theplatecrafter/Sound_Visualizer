import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.ugens.*;

///developer stuff
boolean debugMode = false;
boolean batch = true;
///

//minim stuff
int bands = 1024;
Minim minim;
MultiChannelBuffer song;
FFT Lfft;
FFT Rfft;

//minim output vars
float[] leftChannel;
float[] rightChannel;
float[] leftFFT = new float[bands];
float[] rightFFT = new float[bands];

//other vars
int lastCount;
int renderCount = 0;

//Batch stuff
float FPS = 30;
float framePeriod = 1.0 / FPS;
float samplesPerFrame;


void setup(){
  size(1024,800,P3D);
  //fullScreen(P3D);


  minim = new Minim(this);
  song = new MultiChannelBuffer(1,1);
  float sampleRate = minim.loadFileIntoBuffer("./data/HS.mp3", song);

  leftChannel = song.getChannel(0);
  rightChannel = song.getChannel(1);

  Lfft = new FFT(bands * 2, sampleRate);
  Rfft = new FFT(bands * 2, sampleRate);
  float samplesPerFrame = framePeriod * sampleRate;

  int N = song.getBufferSize();
  lastCount = int(N / samplesPerFrame);
}

void draw(){
  renderCount++;
  //analysis batch
  if(batch){
    int sampleIndex = int(renderCount * samplesPerFrame);
    Lfft.forward(leftChannel,sampleIndex);
    Rfft.forward(rightChannel,sampleIndex);

    for(int i = 0; i < bands; i++){
      leftFFT[i] = Lfft.getBand(i);
      rightFFT[i] = Rfft.getBand(i);
    }
  }

  //check end
  if(renderCount >= lastCount){
    exit();
  }

  //draw
  println(leftFFT);
}

void render(float[] Lspec,float[] Rspec){
  background(0);
  stroke(255,0,0);
  noFill();
  ellipse(width/2,height/2,Lspec[0]*100+40,Rspec[0]*100+40);
}