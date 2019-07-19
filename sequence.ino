// This #include statement was automatically added by the Particle IDE.
#include <InternetButton.h>

#include "math.h"


InternetButton b = InternetButton();


// Global variables to store how many lights should be turned on
int numLights = 6;





bool numLightsChanged = true;
bool onStart = true;
bool ButtonPress = true;
bool showSeq = true;
bool restart = false;
bool StartButton = false;

String InputData = "";

String data;
String data2;
String data3;
String data4;


void setup() {

    // 1. Setup the Internet Button
    b.begin();

    // 2. Setup the API endpoint --
    // When person visits http://particle.io/..../lights, run the controlNumLights() function
    Particle.function("level", ShowSequence);
      Particle.function("callStart", StartPress);
       Particle.function("showInput", ShowInput);
       Particle.function("result", ShowResult);
    // 3. Setup the initial state of the LEDS
    // (by default, turn on 6 lights)
   // activateLEDS();

}


void loop(){

    // This loop just sits here and waits for the numLightsChanged variable to change to true
    // Once it changes to true, run the activateLEDS() function.


     //Start Game
    if(onStart == true ){

     b.allLedsOn(255, 0, 0);

    for(int i = 1; i <= 3; i++) {
        Starter();
    }
        b.allLedsOn( 0, 0, 255);
        b.playSong("C4,8,E4,8,G4,8,C5,8,G5,4");
        delay(3000);

        onStart = false;

    }


    //Show sequence
    if(showSeq == true){
      ShowSequence("level");
        showSeq = false;
    }

    //Activate button press
    if(ButtonPress == true){
        UserInputData();
    }


    if(restart == true){
        onStart = true;
        showSeq = true;
        ButtonPress = true;

        restart = false;

    }

    if(StartButton == true){
        onStart = true;
        showSeq = true;
        ButtonPress = true;

        StartButton = false;

    }




}



void UserInputData(){

     // Process individual buttons and LED response
    if (b.buttonOn(1)) {
        b.ledOn(1, 0, 0, 255); // Blue
         b.playNote("G3",8);
          data = "1";
          InputData = data ;
        b.ledOff(1);

    }

    if (b.buttonOn(2)) {
         b.ledOn(3, 255, 0, 0); // Red
         b.playNote("G3",8);
          data2 = "3";
          InputData = InputData + "3";
        b.ledOff(3);

    }

    if (b.buttonOn(3)) {
        b.ledOn(6, 255, 240, 0); // Yellow
        b.playNote("G3",8);
        data3 = "6";
        InputData = InputData + data3;
        b.ledOff(6);

    }


    if (b.buttonOn(4)) {
          b.ledOn(9, 0, 255, 0); // Green
       b.playNote("G3",8);
        data4 = "9";
          InputData = InputData + data4;
        b.ledOff(9);

    }


    if ( b.allButtonsOn()) {
       restart = true;
    }





    if(b.allButtonsOff()) {
        // Do something here when all buttons are off
    }


}

// Turn on your LEDS
int ShowSequence(String command){

String level = command;

 if (level == "easy"){

    // 1. turn off all lights
    b.allLedsOff();

    //Light 1
    b.ledOn(1, 0, 0, 255); // Blue
    b.playNote("G3",8);
    delay(1000);
    b.ledOff(1);
    delay(1000);

    //Light 2
    b.ledOn(6, 255, 0, 0); // Red
    b.playNote("G3",8);
    delay(1000);
    b.ledOff(6);
    delay(1000);


    //Light 3
    b.ledOn(9, 255, 240, 0); // Yellow
   b.playNote("G3",8);
    delay(1000);
    b.ledOff(6);
    delay(1000);

    //Light 4
    b.ledOn(3, 0, 255, 0); // Green
   b.playNote("G3",8);
    delay(1000);
    b.ledOff(9);
    delay(1000);

    b.allLedsOff();

 }


  if (level == "hard"){

    // 1. turn off all lights
    b.allLedsOff();

    //Light 1
    b.ledOn(1, 0, 0, 255); // Blue
   b.playNote("G3",8);
    delay(100);
    b.ledOff(1);
    delay(100);

    //Light 2
    b.ledOn(6, 255, 0, 0); // Red
    b.playNote("G3",8);
    delay(100);
    b.ledOff(6);
    delay(100);


    //Light 3
    b.ledOn(9, 255, 240, 0); // Yellow
   b.playNote("G3",8);
    delay(100);
    b.ledOff(6);
    delay(100);

    //Light 4
    b.ledOn(3, 0, 255, 0); // Green
   b.playNote("G3",8);
    delay(100);
    b.ledOff(9);
    delay(100);

    b.allLedsOff();

 }



   return 1;
}


void Starter(){

   // b.allLedsOn(255, 240, 0);
     b.rainbow(1);
    b.playSong("E5,8,G5,8,E6,8,C6,8,D6,8,G6,8,C4,8,E4,8");
    delay(10);
    b.allLedsOff();
    delay(10);
}

int StartPress(String alay){

    if (alay == "start"){

        StartButton = true;

    }
}

int ShowInput(String db){

    if (db == "show"){

         Particle.publish("broadcastMessage",InputData,60,PUBLIC);

    }
}

int ShowResult(String r){

    if (r == "WIN"){

    b.allLedsOn(0, 255, 0);
    b.playSong("D6,8,G6,8.D6,8,G6,8");
    delay(10);

    }

    if (r == "LOOSE"){

    b.allLedsOn(255, 0, 0);
    b.playSong("E5,5,E5,5,E5,5");
    delay(10);

    }
}
