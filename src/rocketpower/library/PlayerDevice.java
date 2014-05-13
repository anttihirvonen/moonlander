package rocketpower.library;


/*
 * Implements "connection" with exported sync data files.
 */
class PlayerDevice extends RocketDevice {
    /*
     * Read syncdata from given path
     */
    public PlayerDevice(TrackContainer tracks, boolean debug, String path) {
        super(tracks, debug);

    }

    public void update() {
        // Data loaded; do nothing
        return;
    }


}
