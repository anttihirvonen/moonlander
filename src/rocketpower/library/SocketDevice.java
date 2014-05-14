package rocketpower.library;


import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.util.logging.Logger;

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
    public SocketDevice(Logger logger, TrackContainer tracks, String host, int port) throws Exception {
        super(logger, tracks);

        logger.fine(String.format("Trying to connect to Rocket at %s:%d", host, port));

        // Connect to rocket and initialize streams
        try {
            socket = new Socket(host, port);
        } catch (Exception e) {
            logger.warning(String.format("Connection to %s:%d failed.", host, port));
            throw e;
        }
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        logger.finer("Connection established.");
        
        // This raises if something goes terribly wrong
        greetServer();

        logger.info(String.format("Connected to Rocket running at %s:%d.", host, port));
    }

    private void greetServer() throws Exception {
        logger.finer("Sending greetings to Rocket.");
        
        // Write our greetings
        out.writeBytes(CLIENT_GREET);

        logger.finer("Greetings sent. Now reading greetings from Rocket...");

        // Expect server to return with correct greetings
        byte greet[] = new byte[SERVER_GREET.length()];
        try {
            in.readFully(greet, 0, SERVER_GREET.length());
        } catch (Exception e) {
            logger.severe("Couldn't read greetings from server.");
            throw new Exception("Reading greetings failed");
        }

        logger.finer("Greetings read successfully.");

        if (!SERVER_GREET.equals(new String(greet))) {
            logger.severe("Server didn't send correct greetings.");
            throw new Exception("Greetings mismatch");
        }
    }

    /*
     * Reads socket for updates
     */
    public void update() {

    }
}
