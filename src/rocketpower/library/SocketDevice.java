package rocketpower.library;


import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;

/*
 * Implements connection with GNU Rocket.
 */
class SocketDevice extends RocketDevice {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    private final String CLIENT_GREET = "hello, synctracker!";
    private final String SERVER_GREET = "hello, demo!";

    /*
     * Connects to GNU Rocket.
     */
    public SocketDevice(TrackContainer tracks, boolean debug, String host, int port) throws Exception {
        super(tracks, debug);

        // Connect to rocket and initialize streams
        try {
            socket = new Socket(host, port);
        } catch (Exception e) {
            debugLog(String.format("Connection to %s:%d failed", host, port));
            throw e;
        }
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        // Write our greetings
        out.writeBytes(CLIENT_GREET);

        // Expect server to return with correct greetings
        byte greet[] = new byte[SERVER_GREET.length()];
        in.readFully(greet, 0, SERVER_GREET.length());

        if (!SERVER_GREET.equals(new String(greet))) {
            debugLog("Server didn't send correct greetings.");
            throw new Exception("Greetings mismatch");
        }

        debugLog(String.format("Connected to server running at %s:%d", host, port));
    }

    /*
     * Reads socket for updates
     */
    public void update() {

    }
}
