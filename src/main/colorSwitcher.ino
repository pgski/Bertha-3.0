int redChannel = 3;
int greenChannel = 9;
int blueChannel = 11;
int redChannel2 = 5;
int greenChannel2 = 6;
int blueChannel2 = 10;
int digitalChannel1 = 7;
int digitalChannel2 = 2;
int digitalChannel3 = 4;



int red = 0;
int green = 0;
int blue = 0;
int red2 = 0;
int green2 = 0;
int blue2 = 0;
int bit1 = 0;
int bit2 = 0;
int bit3 = 0;
float animationTimer = 0; //0-150

void setup() {
  Serial.begin(115200);

  pinMode(digitalChannel1, INPUT);
  pinMode(digitalChannel2, INPUT);
  pinMode(digitalChannel3, INPUT);
  pinMode(redChannel, OUTPUT);
  pinMode(greenChannel, OUTPUT);
  pinMode(blueChannel, OUTPUT);
  pinMode(redChannel2, OUTPUT);
  pinMode(greenChannel2, OUTPUT);
  pinMode(blueChannel2, OUTPUT);
}

void loop() {
  bit1 = digitalRead(digitalChannel1);
  bit2 = digitalRead(digitalChannel2);
  bit3 = digitalRead(digitalChannel3);
  red = red2 = 0;
  green = green2 = 0;
  blue = blue2 = 0;
  
  if(bit3) updateWithSecondaryColors();
  else{
    updateWithIslanderColors();
    red2 = red;
    green2 = green;
    blue2 = blue;
  }
  
  updateColors();
  
  // Serial.print('(');
  // Serial.print(bit1);
  // Serial.print(',');
  // Serial.print(bit2);
  // Serial.print(',');
  // Serial.print(bit3);
  // Serial.println(')');
}
  

  

void updateWithIslanderColors(){
  if(bit1 == 1 && bit2 == 1){ //CYCLING
    animationTimer = fmod((animationTimer+0.01F), 99.1F);
    //Progress is now forced to be 1 because color changing doesnt look good and its too tedious to change it.
    float progress = 1.F;//(float)(fmod(animationTimer, 33.F))/33.F;
    if(animationTimer < 33.F){ //orange to blue (255,25,0) to (0,0,255)
      red = lerp(255, 0, progress);
      green = lerp(25, 0, progress);
      blue = lerp(0, 255, progress);
    } else if(animationTimer < 66.F){ //blue to white (0,0,255) to (255,255,255)
      red = lerp(0, 255, progress);
      green = lerp(0, 255, progress);
      blue = 255;
    } else { //white to orange (255,255,255) to (255,25,0)
      red = 255;
      green = lerp(255, 25, progress);
      blue = lerp(255, 0, progress);
    }
  } else if(bit1 == 1){ //BLUE
    blue = 255;
  } else if(bit2 == 1){ //WHITE
    red = 255;
    green = 255;
    blue = 255;
  } else { //ORANGE
    red = 255;
    green = 25;
    blue = 0;
  }
}
void updateWithSecondaryColors(){
  // //if bit1 and bit2 are on, colors are off.
  if(bit1 == 1 && bit2 == 1){ //UNUSED
  }else if(bit1 == 1){ //UNUSED
  } else if(bit2 == 1){ //UNUSED
  } else { //ONE_BLUE, ONE_ORANGE
    animationTimer = fmod((animationTimer+0.01F), 100.0F);
    if(animationTimer < 50.F){ //1 is orange, 2 is blue
      red = 255;
      green = 25;
      blue = 0;
      red2 = 0;
      green2 = 0;
      blue2 = 255;
    } else {
      red = 0;
      green = 0;
      blue = 255;
      red2 = 255;
      green2 = 25;
      blue2 = 0;
    }
  }
}

void updateColors(){
  analogWrite(redChannel, red);
  analogWrite(greenChannel, green);
  analogWrite(blueChannel, blue);
  analogWrite(redChannel2, red2);
  analogWrite(greenChannel2, green2);
  analogWrite(blueChannel2, blue2);
}
float lerp(float start, float end, float delta)
{
  return start * (1.0 - delta) + (end * delta);
}