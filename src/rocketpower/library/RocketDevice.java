package rocketpower.library;

import java.util.logging.Logger;


/*
 * Connection device baseclass
 */
abstract class RocketDevice implements TrackContainerListener {
    protected TrackContainer tracks;
    protected Logger logger;

    RocketDevice(Logger logger, TrackContainer tracks) {
        this.logger = logger;
        this.tracks = tracks;
        this.tracks.addEventListener(this);
    }

    /**
     * If device has been initialized, it must be closed.
     *
     * Removes event listener.
     */
    public void close() {
        this.tracks.removeEventListener(this);
    }

    abstract public void update();
    public void trackAdded(String name) {}
    public void trackDeleted(String name) {}
}
