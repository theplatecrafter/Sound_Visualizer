/* autogenerated by Processing revision 1289 on 2023-01-15 */
import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import processing.sound.*;

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


//SoundFile
SoundFile file;

//fft
FFT fft;
int bands = 512;
float[] spectrum = new float[bands];

//amplitude
float vol;
Amplitude amp;

//BeatDetector
BeatDetector BDR;

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

float delayMS;

public void setup() {

  for(int i = 0; i < 10; i++){
    Lz = append(Lz,i*(-500));
    Ls = append(Ls,PApplet.parseInt(i*(bands/171)));
  }

  /* size commented out by preprocessor */;
  background(0);
  
  //file
  file = new SoundFile(this, "Cetus.mp3");
  
  //fft
  fft = new FFT(this,bands);
  fft.input(file);
  
  //Amplitude
  amp = new Amplitude(this);
  amp.input(file);
  
  //BeatDetector
  BDR = new BeatDetector(this);
  BDR.input(file);
  
  //play SoundFile
  file.play();
  delayMS = millis()/1000;
}      

public void draw() { 
  background(brightness);

  //sound analyze
  fft.analyze(spectrum);
  vol = amp.analyze();
  for (int i = 0; i < bands; i++) {
    spectrumSmoothed[i] += (spectrum[i] - spectrumSmoothed[i]) / 2;
  }
  volSmoothed += (vol - volSmoothed) / 2;

  //ending
  if(millis()/1000-delayMS > file.duration()){
    for(int i = 0; i < Pz.length; i++){
      Pz[i] -= 50;
    }
    for(int i = 0; i < Lz.length; i++){
      Lz[i] -= 100;
    }
    if(Lz[Lz.length-1] <= -4500){
      exit();
    }
  }

  //3D lines
  pushMatrix();
  if(Lz[Lz.length-1] > -4000){
    Lz = append(Lz,-4500);
    if(Ls[Ls.length-1]/(bands/171) > 10){
      Ls = append(Ls,PApplet.parseInt(0));
    }else{
      Ls = append(Ls,PApplet.parseInt(Ls[Ls.length-1]+(bands/171)));
    }
  }
  for(int i = 0; i < Lz.length; i++){
    pushMatrix();
    translate(0,0,Lz[i]);
    if (Lz[i] < - 4000) {
      stroke(255,0.51f * (Lz[i] + 4500));
    }else{
      stroke(255);
    }
    strokeWeight(spectrumSmoothed[0]*13.35f+3);
    noFill();
    rect(0,0,width,height);

    pushMatrix();
    translate(0,0,250);
    if (Lz[i] < - 4000) {
      stroke(volSmoothed*100,0.51f * (Lz[i] + 4500));
    }else{
      stroke(volSmoothed*100);
    }
    rect(0,0,width,height);
    if (Lz[i] < - 4000) {
      stroke(rgb((Lz[i]+4500)/5500*360,0.51f * (Lz[i] + 4500)));
    }else{
      stroke(rgb((Lz[i]+4500)/5500*360,255));
    }
    line(0,height,spectrumSmoothed[PApplet.parseInt(Ls[i])]*width/1.5f,height);
    line(width-spectrumSmoothed[PApplet.parseInt(Ls[i])]*width/1.5f,height,width,height);
    line(0,0,0,spectrumSmoothed[PApplet.parseInt(Ls[i])]*height/1.5f);
    line(0,height-spectrumSmoothed[PApplet.parseInt(Ls[i])]*height/1.5f,0,height);
    line(0,0,spectrumSmoothed[PApplet.parseInt(Ls[i])]*width/1.5f,0);
    line(width-spectrumSmoothed[PApplet.parseInt(Ls[i])]*width/1.5f,0,width,0);
    line(width,0,width,spectrumSmoothed[PApplet.parseInt(Ls[i])]*height/1.5f);
    line(width,height-spectrumSmoothed[PApplet.parseInt(Ls[i])]*height/1.5f,width,height);
    popMatrix();

    if (Lz[i] < - 4000) {
      fill(255,0.51f * (Lz[i] + 4500));
    }else{
      fill(255);
    }
    noStroke();
    beginShape();
    curveVertex(0, height);
    curveVertex(0, height);
    for(int a = 0; a < 100; a++){
      curveVertex((a+1)*(width/100), avg(spectrumSmoothed,a*PApplet.parseInt(bands/100),a*PApplet.parseInt(bands/100)+PApplet.parseInt(bands/100))*(-1000)+height-5);
    }
    curveVertex(width, height);
    curveVertex(width, height);
    endShape();

    beginShape();
    curveVertex(width, 0);
    curveVertex(width, 0);
    for(int a = 0; a < 100; a++){
      curveVertex(width-(a+1)*(width/100), avg(spectrumSmoothed,a*PApplet.parseInt(bands/100),a*PApplet.parseInt(bands/100)+PApplet.parseInt(bands/100))*1000+5);
    }
    curveVertex(0, 0);
    curveVertex(0, 0);
    endShape();

    popMatrix();
    Lz[i] += volSmoothed*20;
    if(Lz[i] > 1000){
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
    SPrx = append(SPrx,random(-0.2f,0.2f));
    SPry = append(SPry,random(-0.2f,0.2f));
    SPrz = append(SPrz,random(-0.2f,0.2f));
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
      stroke(255,0.51f * (Pz[i] + 2000));
    } else{
      stroke(255);
    }
    strokeWeight(0);
    if (Pz[i] < - 1500) {
      fill(rgb(Px[i] / PApplet.parseFloat(width) * 360,0.51f * (Pz[i] + 2000)));
    } else{
      fill(rgb(Px[i] / PApplet.parseFloat(width) * 360,255));
    }
    if (BDR.isBeat()) {
      box(spectrumSmoothed[0] * Pa[i] * 5 + 4);
    } else{
      box(spectrumSmoothed[0] * Pa[i]*3 + 4);
    }
    Pz[i] += volSmoothed * 20;
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
  fill(0,0,spectrum[0] * 200);
  box(volSmoothed * 100 + 60);

  
  for(int i = 0; i < 4; i++){
    pushMatrix();
    rotateY(radians(i*90));
    rectMode(CENTER);
    if(BDR.isBeat()){
      translate(0,0,volSmoothed * 50 + 75 + spectrumSmoothed[0]*500);
    }else{
      translate(0,0,volSmoothed * 50 + 75 + spectrumSmoothed[0]*200);
    }
    stroke(255,0,0);
    fill(255,50,50,volSmoothed*255);
    rect(0,0,volSmoothed * 100 + 55,volSmoothed * 100 + 55);
    rectMode(CORNER);
    popMatrix();
  }
  pushMatrix();
  rotateX(radians(90));
  rectMode(CENTER);
  if(BDR.isBeat()){
    translate(0,0,volSmoothed * 50 + 75 + spectrumSmoothed[0]*500);
  }else{
    translate(0,0,volSmoothed * 50 + 75 + spectrumSmoothed[0]*200);
  }
  stroke(255,0,0);
  fill(255,50,50,volSmoothed*255);
  rect(0,0,volSmoothed * 100 + 55,volSmoothed * 100 + 55);
  rectMode(CORNER);
  popMatrix();
  pushMatrix();
  rotateX(radians(-90));
  rectMode(CENTER);
  if(BDR.isBeat()){
    translate(0,0,volSmoothed * 50 + 75 + spectrumSmoothed[0]*500);
  }else{
    translate(0,0,volSmoothed * 50 + 75 + spectrumSmoothed[0]*200);
  }
  stroke(255,0,0);
  fill(255,50,50,volSmoothed*255);
  rect(0,0,volSmoothed * 100 + 55,volSmoothed * 100 + 55);
  rectMode(CORNER);
  popMatrix();

  popMatrix();
  
  //background spectrum left
  pushMatrix();
  translate(0,height / 2,0);
  rotateY(radians(60));
  noStroke();
  fill(255);
  for (int i = 0; i < bands; i++) {
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
  for (int i = 0; i < bands; i++) {
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
  if (BDR.isBeat()) {
    brightness = spectrum[1 / 100 * bands] * 200;
  }
  else{
    brightness -= 5;
  }


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


  public void settings() { fullScreen(P3D); }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Sound_Visualizer" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
