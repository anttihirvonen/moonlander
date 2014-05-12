package rocketpower.library;


/*
 * Implements "connection" with exported sync data files.
 */
class PlayerDevice extends RocketDevice {
    /*
     * Read syncdata from given path
     */
    public PlayerDevice(Rocketpower rocket, String path, boolean debug) {
        super(debug);

    }

    public void update() {
        // Data loaded; do nothing
        return;
    }


}
