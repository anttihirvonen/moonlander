package rocketpower.library;

import java.util.logging.Logger;


/*
 * Connection device baseclass
 */
abstract class RocketDevice {
    private TrackContainer tracks;
    protected Logger logger;

    RocketDevice(Logger logger, TrackContainer tracks) {
        this.logger = logger;
        this.tracks = tracks;
    }

    abstract public void update();
}
