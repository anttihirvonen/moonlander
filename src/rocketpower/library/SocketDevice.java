package rocketpower.library;


/*
 * Implements connection with GNU Rocket.
 */
class SocketDevice extends RocketDevice {
    /*
     * Connects to GNU Rocket.
     */
    public SocketDevice(TrackContainer tracks, boolean debug, String host, int port) throws Exception {
        super(tracks, debug);
        //throw new Exception();
    }

    /*
     * Reads socket for updates
     */
    public void update() {

    }
}
