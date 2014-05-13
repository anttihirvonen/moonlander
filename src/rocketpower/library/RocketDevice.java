package rocketpower.library;


/*
 * Connection device baseclass
 */
abstract class RocketDevice {
    private TrackContainer tracks;
    private boolean debug;

    RocketDevice(TrackContainer tracks, boolean debug) {
        this.debug = debug;
        this.tracks = tracks;
    }

    /**
     * Writes a message to stdout if debug = true.
     */
    private void debugLog(String message) {
        if (this.debug)
            System.out.println(message);
    }

    abstract public void update();
}
