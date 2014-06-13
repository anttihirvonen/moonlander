import moonlander.library.*;

// Minim must be imported when using Moonlander with soundtrack.
import ddf.minim.*;

Moonlander moonlander;

void setup() {
    // Parameters: 
    // - PApplet
    // - soundtrack filename (relative to sketch's folder)
    // - beats per minute in the song
    // - how many rows in Rocket correspond to one beat
    moonlander = Moonlander.initWithSoundtrack(this, "../common/tekno_127bpm.mp3", 127, 8);

    // Other initialization code goes here.
    size(400, 400);

    // Last thing in setup; start Moonlander. This either
    // connects to Rocket (development mode) or loads data 
    // from 'syncdata.rocket' (player mode).
    // Also, in player mode the music playback starts immediately.
    moonlander.start();
}

void draw() {
    // Handles communication with Rocket. In player mode
    // does nothing. Must be called at the beginning of draw().
    moonlander.update();

    // This shows how you can query value of a track.
    // If track doesn't exist in Rocket, it's automatically
    // created.
    double bg_red = moonlander.getValue("background_red");

    // All values in Rocket are floats; however, there's 
    // a shortcut for querying integer value (getIntValue)
    // so you don't need to cast.
    int bg_green = moonlander.getIntValue("background_green");
    int bg_blue = moonlander.getIntValue("background_blue");
    
    // Use values to control anything (in this case, background color).
    background((int)bg_red, bg_blue, bg_green);
    
    // You can also ask current time and row from Moonlander if you
    // want to do something custom in code based on time.
    textSize(24);
    text("Time: " + String.format("%.2f", moonlander.getCurrentTime()), 10, 30);
    text("Row: " + String.format("%.2f", moonlander.getCurrentRow()), 10, 60);
    text("Color values: (" + (int)bg_red + ", " + bg_green + ", " + bg_blue + ")", 10, 90);
}
