package rocketpower.library;

import java.util.logging.Logger;


/*
 * Implements "connection" with exported sync data files.
 */
class PlayerDevice extends RocketDevice {
    /*
     * Read syncdata from given path
     */
    public PlayerDevice(Logger logger, TrackContainer tracks, String path) {
        super(logger, tracks);

    }

    public void update() {
        // Data loaded; do nothing
        return;
    }


}
