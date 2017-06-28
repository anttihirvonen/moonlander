```java
double time_varying_parameter = moonlander.getValue("my_parameter");
```

Moonlander is a library for integrating Processing with GNU Rocket, which allows you to easily control time-based variables in your sketches.

If you don't know what GNU Rocket is, read about it [here](https://github.com/kusma/rocket). In short, GNU Rocket is a sync-tracker, a tool mainly used for synchronizing music and visuals in demoscene productions. It can be used to control basically anything that can be presented by a floating point value and varies with time; cameras, colors, coordinates, you name it. Rocket with Moonlander doesn't necessarily require a music track, so it can be utilized as a general control tool for any parameters that change over time (e.g. data visualizations, animations, whatever you can imagine...).


## Installation

### From release package

Download the latest release from [this url](https://github.com/anttihirvonen/moonlander/releases/download/v1.0/moonlander-1.0.zip) or fetch a version of your choice from [releases page](https://github.com/anttihirvonen/moonlander/releases). Extract the library into the libraries folder of your Processing sketchbook (`<Your Processing folder>/libraries/Moonlander`) and restart Processing. The library should be then visible in menu (**Sketch** > **Import library**) – if it's there, you're ready to try the example below!

If you encounter any problems, see more info at [Processing Wiki](http://wiki.processing.org/w/How_to_Install_a_Contributed_Library#Manual_Install).

### From sources (Git repository)

Edit variables `sketchbook.location` and `classpath.local.location` in `resources/build.properties`. By default, the build process only works on Macs – for other platforms you must edit the mentioned variables for build to succeed (this is clumsy and should be replaced with better solution later on, but suffices for now).

Build the library with `ant -f resources/build.xml` – the build process also automatically installs the library into Processing's library folder, so it will be immediately usable after you restart Processing (this must be only done after the first build). You are then ready to try the example below! (or, since you probably cloned this repository, fix a bug you found or implement a feature and send me a pull request :)


## Usage

Initialize the library, call `Moonlander.start()` at the end of `setup()`, call `Moonlander.update()` for every frame and you're done. You can query current value of any track by calling `Moonlander.getValue(String trackName)`.

If GNU Rocket is running at it's default address and port (localhost:1338), Moonlander attaches itself to it. If connection cannot be made, the data is loaded from a file called `syncdata.rocket` in your sketch's folder. In general, the following workflow applies:

1. Run GNU Rocket.
2. Start your sketch. Sketch connects to GNU Rocket using Moonlander. Rocket takes the control of your sketch.
3. Develop and sync. GNU Rocket keeps your data even if you close the running sketch. When you start it again, Rocket lets you continue on the same line where you left off. 
4. Before you close Rocket, save the project to a file called `syncdata.rocket` and place it in your sketch's folder. When Rocket isn't running, this is the file Moonlander by default tries to load your syncdata from.
5. You can distribute your sketch with the saved syncdata file and everything should work as normal. Also, this file can be loaded into Rocket by your fellow developer and he or she can continue syncing where you left off (by the way, .rocket files are plain XML, so they work fine with Git and merging).

Example of usage:

```java
import moonlander.library.*;

// Minim must be imported when using Moonlander with soundtrack.
import ddf.minim.*;

Moonlander moonlander;

void setup() {
    // Parameters: PApplet, filename (file should be in sketch's folder), 
    // beats per minute, rows per beat
    moonlander = Moonlander.initWithSoundtrack(this, "filename.mp3", 120, 4);

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

If you want to run Moonlander without a music track, replace line `moonlander = Moonlander.initWithSoundTrack(this, "filename.mp3", 120, 4);` with `moonlander = new Moonlander(this, new TimeController(4));`, where the integer parameter passed to `TimeController` means "rows per second".

## Examples

These examples are distributed in the release package so you can easily try them. You can find the examples by navigating to path `<Your Processing folder>/libraries/Moonlander/examples`. However, if you just want to check the code, here are the direct quick links to related .pde-files:

[Moonlander basics](examples/Moonlander_Basics)

## API

Moonlander is mainly operated through it's main class `Moonlander`. Normally (I'd say 99,9% of cases) you only need this class to use the library. However, if you wan't to do something fancy, like build your own controller, you need to dig a little deeper. 

### Moonlander

```java
Moonlander initWithSoundtrack(PApplet applet, String filename, int beatsPerMinute, int rowsPerBeat)
```

Shortcut for initializing Moonlander with music playback. This effectively loads soundtrack from given file and constructs a new `MinimController` for it. `filename` is the soundtrack filename (should be in sketch's folder or under `data/`), `beatsPerMinute` is bpm of the soundtrack and `rowsPerBeat` tells Rocket how many rows correspond to one beat (sync precision).

```java
void Moonlander.changeLogLevel(Level logLevel)
```

Change Moonalander´s logging level. All levels from `java.utils.logging.Level` are supported - `Level.FINEST` naturally outputs most data. All logging data is output into stderr (visible on Processing's console).

```java
Moonlander.start(String host, int port, String filepath)
```

Starts Moonlander. Some version of `Moonlander.start` must be called before fetching values. Either connects to Rocket running at `host:port` or loads syncdata from `filepath` under sketch's path. 

```java
Moonlander.start()
```

Shortcut for `Moonlander.start("localhost", 1338, "syncdata.rocket")`

```java
Moonlander.getValue(String name)
```

Return `double` value of given track.

```java
Moonlander.getIntValue(String name)
```

Shortcut for running `(int)Moonlander.getValue(...)`.

```java
Moonlander.getCurrentTime()
```

Returns current playback time in seconds.

```java
Moonlander.getCurrentRow()
```

Returns current (fractional) row.


## For developers

Want to contribute? As usual, submit a patch by opening a pull request.

Before sending your patch away, please make sure that old tests pass and write tests for new code if necessary. This project uses JUnit for automated testing because testing Processing libraries by hand is very painful. Run tests with `ant tests.run` which builds the library and runs all the tests. 

Note that the amount of unit tests should be kept at minimum and parts that would require way too much mocking (like socket connections) should not be unit tested. Use unit tests only for those parts of code that are running "behind the scenes" and thus hard to test and debug in Processing (e.g. not easily testable by poking the public API).

## Similar libraries

* [pyrocket](https://github.com/Contraz/pyrocket) for Python
