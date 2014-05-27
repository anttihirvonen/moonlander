# Moonlander

Moonlander is a library for integrating Processing with GNU Rocket, which allows you to take full control of any time-varying parameters in your sketches.

If you don't know what GNU Rocket is, read about it [here](https://github.com/kusma/rocket). In short, GNU Rocket is a sync-tracker, a tool mainly used for synchronizing music and visuals in demoscene productions. However, it can be also used to control basically anything that varies with time; cameras, colors, coordinates, you name it. Basically anything that can be presented by a floating point value can be controlled.

## Installation

Extract the library into the libraries folder of your Processing sketchbook. More info at [Processing Wiki](http://wiki.processing.org/w/How_to_Install_a_Contributed_Library).

TBD: This library should be in Processing's database (which makes install process super easy)


## Usage

Initialize the library, call `start()` at the end of `setup()`, call `update()` for every frame and you're done. You can query current value of any track by calling `getValue(String trackName)`.

Example:

```
void setup() {
    // Parameters: filename, beats per minute, rows per beat
    Moonlander moonlander = Moonlander.initWithSoundTrack("filename.mp3", 120, 4);

    // .. other initialization code ...

    moonlander.start();
}

void draw() {
    // Handles communication with Rocket
    moonlander.update();

    // Get current value of a track
    double value = moonlander.getValue("my_track");

    // Use it somehow
    background((int)value);
}

```

## API

TODO: document

## For developers

Want to contribute? As usual, submit a patch by opening a pull request.

Before sending your patch away, please make sure that old tests pass and write tests for new code if necessary. This project uses JUnit for automated testing, because testing Processing libraries by hand is very painful. Run tests with `ant tests.run`, which builds the library and runs all the tests. 

Note that the amount of unit tests should be kept at minimum and parts that would require way too much mocking (like socket connections) should not be unit tested. Use unit tests only for those parts of code that are running "behind the scenes" and thus hard to test and debug in Processing (eg. not easily testable by poking the public API).
