/* autogenerated by Processing revision 1289 on 2023-01-16 */
import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import ddf.minim.*;
import ddf.minim.analysis.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class Sound_Visualizer extends PApplet {

//Library import



//Minim
Minim minim;
MultiChannelBuffer song;
float[] leftChannel;
FFT fft;
BeatDetect beat;
int bands = 512;
float vol = 0;
float[] spectrum = new float[bands];

//other vars
float[] spectrumSmoothed = new float[bands];
float volSmoothed = 0;

float mainBoxRx = 0;
float mainBoxRy = 0;
float mainBoxRz = 0;
float brightness = 0;

int[] PType = new int[0];
float[] Px = new float[0];
float[] Py = new float[0];
float[] Pz = new float[0];
float[] Prx = new float[0];
float[] Pry = new float[0];
float[] Prz = new float[0];
float[] SPrx = new float[0];
float[] SPry = new float[0];
float[] SPrz = new float[0];
float[] Pa = new float[0];

float[] Lz = new float[0];
float[] Ls = new float[0];


//draw counter
int drawCount = 0;
int lastCount;

//target FPS
float FPS = 30;
float framePeriod = 1.0f / FPS;
float samplesPerFrame;

public void setup() {
  
  for (int i = 0; i < 10; i++) {
    Lz = append(Lz,i * ( -500));
    Ls = append(Ls,PApplet.parseInt(i * (bands / 171)));
  }
  
  /* size commented out by preprocessor */;
  background(0);
  
  minim = new Minim(this);
  song = new MultiChannelBuffer(1,1);
  float sampleRate = minim.loadFileIntoBuffer("shortTest.mp3", song);
  leftChannel = song.getChannel(0);
  fft = new FFT(bands * 2, sampleRate);
  samplesPerFrame = framePeriod * sampleRate;
  
  int N = song.getBufferSize();
  float songDuration = N / sampleRate;
  println("sample rate = ", sampleRate);
  println("samples per frame = ", samplesPerFrame);
  println("buffer size = ", N);
  println("song duration (secs) = ", songDuration);
  lastCount = PApplet.parseInt(N / samplesPerFrame);
  println("lastCount = ", lastCount);

  beat = new BeatDetect();
}

public void draw() { 
  drawCount++;
  if (drawCount >= lastCount) {
    exit();
  }
  
  background(brightness);
  
  //sound analyze
  int sampleIndex = PApplet.parseInt(drawCount * samplesPerFrame);
  fft.forward(leftChannel,sampleIndex);

  
  for (int i = 0; i < bands; i++) {
    spectrumSmoothed[i] += (fft.getBand(i) / bands - spectrumSmoothed[i]) / 2;
    spectrum[i] = fft.getBand(i);
  }
  beat.detect(spectrum);

  vol = avg(spectrum,0,bands-1)/bands*10;
  volSmoothed += (vol - volSmoothed) / 2;
  
  
  
  //3D lines
  pushMatrix();
  if (Lz[Lz.length - 1] > - 4000) {
    Lz = append(Lz, -4500);
    if (Ls[Ls.length - 1] / (bands / 171) > 10) {
      Ls = append(Ls,PApplet.parseInt(0));
    } else{
      Ls = append(Ls,PApplet.parseInt(Ls[Ls.length - 1] + (bands / 171)));
    }
  }
  for (int i = 0; i < Lz.length; i++) {
    pushMatrix();
    translate(0,0,Lz[i]);
    if (Lz[i] < - 4000) {
      stroke(255,0.3f * (Lz[i] + 4500) * volSmoothed + 105);
    } else{
      stroke(255,150 * volSmoothed + 105);
    }
    strokeWeight(spectrumSmoothed[0] * 13.35f + 3);
    noFill();
    rect(0,0,width,height);
    
    pushMatrix();
    translate(0,0,250);
    if (Lz[i] < - 4000) {
      stroke(volSmoothed * 100,0.51f * (Lz[i] + 4500));
    } else{
      stroke(volSmoothed * 100);
    }
    rect(0,0,width,height);
    if (Lz[i] < - 4000) {
      stroke(rgb((Lz[i] + 4500) / 5500 * 360,0.3f * (Lz[i] + 4500) * volSmoothed + 105));
    } else{
      stroke(rgb((Lz[i] + 4500) / 5500 * 360,150 * volSmoothed + 105));
    }
    line(0,height,spectrumSmoothed[PApplet.parseInt(Ls[i])] * width / 1.5f,height);
    line(width - spectrumSmoothed[PApplet.parseInt(Ls[i])] * width / 1.5f,height,width,height);
    line(0,0,0,spectrumSmoothed[PApplet.parseInt(Ls[i])] * height / 1.5f);
    line(0,height - spectrumSmoothed[PApplet.parseInt(Ls[i])] * height / 1.5f,0,height);
    line(0,0,spectrumSmoothed[PApplet.parseInt(Ls[i])] * width / 1.5f,0);
    line(width - spectrumSmoothed[PApplet.parseInt(Ls[i])] * width / 1.5f,0,width,0);
    line(width,0,width,spectrumSmoothed[PApplet.parseInt(Ls[i])] * height / 1.5f);
    line(width,height - spectrumSmoothed[PApplet.parseInt(Ls[i])] * height / 1.5f,width,height);
    popMatrix();
    
    if (Lz[i] < - 4000) {
      fill(255,0.3f * (Lz[i] + 4500) * volSmoothed + 105);
    } else{
      fill(255,150 * volSmoothed + 105);
    }
    noStroke();
    beginShape();
    curveVertex(0, height);
    curveVertex(0, height);
    for (int a = 0; a < 100; a++) {
      curveVertex((a + 1) * (width / 100), avg(spectrumSmoothed,a * PApplet.parseInt(bands / 100),a * PApplet.parseInt(bands / 100) + PApplet.parseInt(bands / 100)) * ( -600) + height - 5);
    }
    curveVertex(width, height);
    curveVertex(width, height);
    endShape();
    
    beginShape();
    curveVertex(width, 0);
    curveVertex(width, 0);
    for (int a = 0; a < 100; a++) {
      curveVertex(width - (a + 1) * (width / 100), avg(spectrumSmoothed,a * PApplet.parseInt(bands / 100),a * PApplet.parseInt(bands / 100) + PApplet.parseInt(bands / 100)) * 600 + 5);
    }
    curveVertex(0, 0);
    curveVertex(0, 0);
    endShape();
    
    popMatrix();
    Lz[i] += volSmoothed * 30 + spectrumSmoothed[0] * 50;
    if (Lz[i] > 1000) {
      Lz = remove(Lz,i);
      Ls = remove(Ls,i);
    }
  }
  popMatrix();
  
  //Cube Patricles
  for (int i = 0; i < floor(volSmoothed * 2 * random(1,1.8f)); i++) {
    Px = append(Px,random(0,width));
    Py = append(Py,random(0,height));
    Pz = append(Pz, -2000);
    Prx = append(Prx,random(0,6.2831f));
    Pry = append(Pry,random(0,6.2831f));
    Prz = append(Prz,random(0,6.2831f));
    SPrx = append(SPrx,random( -0.2f,0.2f));
    SPry = append(SPry,random( -0.2f,0.2f));
    SPrz = append(SPrz,random( -0.2f,0.2f));
    Pa = append(Pa,random(10,35));
  }
  pushMatrix();
  for (int i = 0; i < Px.length; i++) {
    pushMatrix();
    translate(Px[i],Py[i],Pz[i]);
    rotateX(Prx[i]);
    rotateY(Pry[i]);
    rotateZ(Prz[i]);
    if (Pz[i] < - 1500) {
      fill(rgb(Px[i] / PApplet.parseFloat(width) * 360,0.3f * (Pz[i] + 2000) * volSmoothed + 105));
    } else{
      fill(rgb(Px[i] / PApplet.parseFloat(width) * 360,150 * volSmoothed + 105));
    }
    if (beat.isKick()) {
      box(spectrumSmoothed[0] * Pa[i] * 5 + 4);
    } else{
      box(spectrumSmoothed[0] * Pa[i] * 3 + 4);
    }
    Pz[i] += volSmoothed * 30 + spectrumSmoothed[0] * 50;
    Prx[i] += SPrx[i];
    Pry[i] += SPry[i];
    Prz[i] += SPrz[i];
    popMatrix();
    
    if (Pz[i] > 1000) {
      Px = remove(Px,i);
      Py = remove(Py,i);
      Pz = remove(Pz,i);
      Prx = remove(Prx,i);
      Pry = remove(Pry,i);
      Prz = remove(Prz,i);
      SPrx = remove(SPrx,i);
      SPry = remove(SPry,i);
      SPrz = remove(SPrz,i);
      Pa = remove(Pa,i);
    }
    
  }
  popMatrix();
  
  
  mainBoxRx += spectrumSmoothed[PApplet.parseInt(15 / 100 * bands)] / 3;
  mainBoxRy += spectrumSmoothed[PApplet.parseInt(30 / 100 * bands)] / 2;
  mainBoxRz += spectrumSmoothed[PApplet.parseInt(45 / 100 * bands)];
  //small box
  pushMatrix();
  translate(width / 2,height / 2,0);
  rotateX(mainBoxRx);
  rotateY(mainBoxRy);
  rotateZ(mainBoxRz);
  stroke(0,0,200);
  strokeWeight(3);
  fill(0);
  box(volSmoothed * 100 + 40);
  
  
  for (int i = 0; i < 4; i++) {
    pushMatrix();
    rotateY(radians(i * 90));
    rectMode(CENTER);
    if (beat.isKick()) {
      translate(0,0,volSmoothed * 50 + 55 + spectrumSmoothed[0] * 500);
    } else{
      translate(0,0,volSmoothed * 50 + 55 + spectrumSmoothed[0] * 200);
    }
    stroke(255,0,0);
    fill(255,50,50,volSmoothed * 255);
    rect(0,0,volSmoothed * 100 + 35,volSmoothed * 100 + 35);
    rectMode(CORNER);
    popMatrix();
  }
  pushMatrix();
  rotateX(radians(90));
  rectMode(CENTER);
  if (beat.isKick()) {
    translate(0,0,volSmoothed * 50 + 55 + spectrumSmoothed[0] * 500);
  } else{
    translate(0,0,volSmoothed * 50 + 55 + spectrumSmoothed[0] * 200);
  }
  stroke(255,0,0);
  fill(255,50,50,volSmoothed * 255);
  rect(0,0,volSmoothed * 100 + 35,volSmoothed * 100 + 35);
  rectMode(CORNER);
  popMatrix();
  pushMatrix();
  rotateX(radians( -90));
  rectMode(CENTER);
  if (beat.isKick()) {
    translate(0,0,volSmoothed * 50 + 55 + spectrumSmoothed[0] * 500);
  } else{
    translate(0,0,volSmoothed * 50 + 55 + spectrumSmoothed[0] * 200);
  }
  stroke(255,0,0);
  fill(255,50,50,volSmoothed * 255);
  rect(0,0,volSmoothed * 100 + 35,volSmoothed * 100 + 35);
  rectMode(CORNER);
  popMatrix();
  
  popMatrix();
  
  //background spectrum left
  pushMatrix();
  translate(0,height / 2,0);
  rotateY(radians(60));
  noStroke();
  fill(255);
  for (int i = 0; i < bands; i += PApplet.parseInt(bands / 150)) {
    fill(rgb(360 / PApplet.parseFloat(bands) * i,255));
    rect(PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands) * i,0,PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands),spectrumSmoothed[i] * (height / 2));
    rect(PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands) * i,0,PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands),spectrumSmoothed[i] * (height / ( -2)));
    rect(PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands) * i + width / 3,0,PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands),spectrumSmoothed[bands - 1 - i] * (height / 2));
    rect(PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands) * i + width / 3,0,PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands),spectrumSmoothed[bands - 1 - i] * (height / ( -2)));
    rect(PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands) * i + width / 3 * 2,0,PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands),spectrumSmoothed[i] * (height / 2));
    rect(PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands) * i + width / 3 * 2,0,PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands),spectrumSmoothed[i] * (height / ( -2)));
  }
  popMatrix();
  
  //background spectrum right
  pushMatrix();
  translate(width,height / 2,0);
  rotateY(radians(120));
  noStroke();
  fill(255);
  for (int i = 0; i < bands; i += PApplet.parseInt(bands / 150)) {
    fill(rgb(360 / PApplet.parseFloat(bands) * i,255));
    rect(PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands) * i,0,PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands),spectrumSmoothed[i] * (height / 2));
    rect(PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands) * i,0,PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands),spectrumSmoothed[i] * (height / ( -2)));
    rect(PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands) * i + width / 3,0,PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands),spectrumSmoothed[bands - 1 - i] * (height / 2));
    rect(PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands) * i + width / 3,0,PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands),spectrumSmoothed[bands - 1 - i] * (height / ( -2)));
    rect(PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands) * i + width / 3 * 2,0,PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands),spectrumSmoothed[i] * (height / 2));
    rect(PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands) * i + width / 3 * 2,0,PApplet.parseFloat(width) / 3 / PApplet.parseFloat(bands),spectrumSmoothed[i] * (height / ( -2)));
  }
  popMatrix();
  
  //brightness controller
  if (beat.isKick()) {
    brightness = spectrumSmoothed[1 / 100 * bands] * 200;
  }
  else{
    brightness -= 5;
  }
  
  saveFrame("./video/######.png");
  
}

//hue, opacity to rgb
public int rgb(float hue,float opacity) {
  float X = 1 - abs((hue / 60) % 2 - 1);
  float[] rgbOUT = new float[3];
  
  if (hue < 60) {
    rgbOUT[0] = 255;
    rgbOUT[1] = X * 255;
  } else if (hue < 120) {
    rgbOUT[0] = X * 255;
    rgbOUT[1] = 255;
  } else if (hue < 180) {
    rgbOUT[1] = 255;
    rgbOUT[2] = X * 255;
  } else if (hue < 240) {
    rgbOUT[1] = X * 255;
    rgbOUT[2] = 255;
  } else if (hue < 300) {
    rgbOUT[0] = X * 255;
    rgbOUT[2] = 255;
  } else{
    rgbOUT[0] = 255;
    rgbOUT[2] = X * 255;
  }
  return color(rgbOUT[0],rgbOUT[1],rgbOUT[2],opacity);
}

//remove a certain index from array
public float[] remove(float[] input,int index) {
  float[] output = new float[0];
  for (int i = 0; i < input.length; i++) {
    if (i != index) {
      output = append(output,input[i]);
    }
  }
  return output;
}

//average of an array
public float avg(float[] input,int startIndex,int endIndex) {
  float output = 0;
  for (int i = startIndex; i <= endIndex; i++) {
    output += input[i];
  }
  return output / (endIndex - startIndex + 1);
}


  public void settings() { size(1024, 800, P3D); }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Sound_Visualizer" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
