/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */
package rocketpower.library;


import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Collections;
import java.util.logging.*;
import processing.core.*;
import ddf.minim.*;


/**
 * Rocketpower
 * 
 * @example Integration 
 * 
 * (the tag @example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 */
public class Rocketpower {
    public final static String VERSION = "##library.prettyVersion##";

    // Main collection of all tracks
    private TrackContainer tracks;

    // Connection device
    private RocketDevice device;

    private RocketController controller;

    private static Logger logger = Logger.getLogger("rocketpower.library");

    /**
     * Initializes library and tries to load syncdata.
     *
     * First, a connection to GNU Rocket is attempted using given host 
     * and port; if connection fails, tries to load sync data
     * from the given filepath. If both fail, no initial data is loaded.
     *
     * @param host 
     * @param port
     * @param filePath path to Rocket's XML-file
     * @param debug if true, output debug data to stdout
     */
    public Rocketpower(RocketController controller, String host, int port, String filePath) {
        setupLogging(Level.OFF);
        logger.info("Initializing Rocketpower");

        tracks = new TrackContainer();
        this.controller = controller;

        // If connection to rocket fails, try to load syncdata from file
        try {
            device = new SocketDevice(logger, tracks, controller, host, port);
        } catch (Exception e) {
            device = new PlayerDevice(logger, tracks, controller, filePath);
        }
    }

    /**
     * Initializes library with sane default values.
     */
    public Rocketpower(RocketController controller) {
        this(controller, "localhost", 1338, "syncdata.xml");
    }

    /**
     * Shortcut to initializing Rocket connection
     * with given soundtrack file (uses MinimController).
     */
    public static Rocketpower initWithSoundtrack(PApplet applet, String filename, int beatsPerMinute, int rowsPerBeat) {
        Minim minim = new Minim(applet);
        AudioPlayer song = minim.loadFile(filename, 1024);

        return new Rocketpower(new MinimController(song, beatsPerMinute, rowsPerBeat));
    }

    private void setupLogging(Level logLevel) {
        // Create own handler
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.FINEST);

        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
        // This controls the actual logging level
        logger.setLevel(logLevel);
    }

    public void changeLogLevel(Level logLevel) {
        logger.setLevel(logLevel);
    }

    // temporary update method, call in sketc#draw
    // TODO: extend from PApplet and hard-wire updating
    public void update() {
        // Update controller values (may fire events)
        controller.update();
        // Communicate with device
        device.update();
    }

    // temporary(?) proxy method for getting track
    public Track getTrack(String name) {
        return tracks.getOrCreate(name);
    }

    public double getValue(String name) {
        return getTrack(name).getValue(controller.getCurrentRow());
    }

    /**
     * Returns the version of the library.
     * 
     * @return String
     */
    public static String version() {
        return VERSION;
    }
}
