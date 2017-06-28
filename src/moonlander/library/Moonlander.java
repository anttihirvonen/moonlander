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
package moonlander.library;


import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Collections;
import java.util.logging.*;
import processing.core.*;
import ddf.minim.*;


/**
 * Moonlander
 * 
 * @example Integration 
 * 
 * (the tag @example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 */
public class Moonlander {
    public final static String VERSION = "##library.prettyVersion##";

    PApplet parent;

    // Main collection of all tracks
    private TrackContainer tracks;

    // Connection interface
    private Connector connector;

    private Controller controller;

    private static Logger logger = Logger.getLogger("moonlander.library");

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
    public Moonlander(PApplet parent, Controller controller) {
        // Start with logging off.
        // Can be enabled with changeLogLevel()
        setupLogging(Level.INFO);

        tracks = new TrackContainer();
        this.parent = parent;
        this.controller = controller;
    }

    /**
     * Shortcut to initializing Rocket connection
     * with given soundtrack file (uses MinimController).
     */
    public static Moonlander initWithSoundtrack(PApplet applet, String filename, int beatsPerMinute, int rowsPerBeat) {
        Minim minim = new Minim(applet);
        AudioPlayer song = minim.loadFile(filename, 1024);

        return new Moonlander(applet, new MinimController(song, beatsPerMinute, rowsPerBeat));
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

    /** 
     * Fires up the connection to Rocket, or in case of
     * connection failure, loads data from given XML syncfile.
     *
     * If both connectors fail, a message is logged with level SEVERE.
     * Use changeLogLevel() to see more fine grained output if you need
     * to investigate.
     *
     * @return true if connector was initialized, otherwise false.
     */
    public boolean start(String host, int port, String filePath) {

        // If connection to rocket fails, try to load syncdata from file
        try {
            connector = new SocketConnector(logger, tracks, controller, host, port);
        } catch (Exception e) {
            try {
                connector = new ProjectFileConnector(logger, tracks, controller, parent.sketchPath(filePath));
            } catch (Exception ex) {
                logger.severe("Both connectors failed. Either run Rocket or put 'syncdata.rocket' file into sketch's folder.");
                return false;
            }
        }
        
        logger.info("Moonlander initialized successfully.");

        return true;
    }

    /**
     * Shortcut for starting Moonlander with sane defaults.
     *
     * Rocket is assumed to be running at localhost:1338.
     * If connection fails, syncdata is loaded from a
     * file called 'syncdata.rocket' instead.
     */
    public boolean start() {
        return start("localhost", 1338, "syncdata.rocket");
    }

    /**
     * Handle communication and controller updates.
     *
     * This method must be called for every frame.
     */
    public void update() {
        if (controller != null && connector != null) {
            // Update controller values (may fire events)
            controller.update();
            // Communicate with device
            connector.update();
        }
    }

    // temporary(?) proxy method for getting track
    public Track getTrack(String name) {
        return tracks.getOrCreate(name);
    }

    /**
     * Returns value of a track.
     *
     * @param name track's name
     */
    public double getValue(String name) {
        return getTrack(name).getValue(controller.getCurrentRow());
    }

    /**
     * Returns int value of a track.
     *
     * Practically same as (int)Moonlander.getValue(...) -
     * just makes your code nicer to look at as no 
     * casts required!
     */
    public int getIntValue(String name) {
        return (int)getValue(name);
    }

    /**
     * Returns current time in seconds.
     */
    public double getCurrentTime() {
        return controller.getCurrentTime();
    }

    /**
     * Returns current (fractional) row.
     */
    public double getCurrentRow() {
        return controller.getCurrentRow();
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
