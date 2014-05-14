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

    private static class Commands {
        static final int SET_KEY        = 0;
        static final int DELETE_KEY     = 1;
        static final int GET_TRACK      = 2;
        static final int SET_ROW        = 3;
        static final int PAUSE          = 4;
        static final int SAVE_TRACKS    = 5;
    }

    /*
     * Connects to GNU Rocket.
     */
    public SocketDevice(Logger logger, TrackContainer tracks, String host, int port) throws Exception {
        super(logger, tracks);

        logger.fine(String.format("Trying to connect to Rocket at %s:%d", host, port));

        try {
            initSocket(host, port);
            greetServer();
        } catch (Exception e) {
            close();
            throw e;
        }

        logger.info(String.format("Successfully connected to Rocket running at %s:%d.", host, port));
    }

    /**
     * Connects to Rocket and initializes data streams.
     */
    private void initSocket(String host, int port) throws Exception {
        try {
            socket = new Socket(host, port);
        } catch (Exception e) {
            logger.warning(String.format("Connection to %s:%d failed.", host, port));
            throw e;
        }
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        logger.finer("Connection to Rocket established.");
    }

    /**
     * Greets Rocket. 
     *
     * Requires a connected socket, otherwise throws.
     */
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
        try {
            readCommand();
        } catch (Exception e) {
            logger.severe("Error in readCommand");
        }
    }

    /**
     * Reads pending rocket commands from socket.
     *
     * Throws exception if command cannot be read
     * successfully; this usually means that Rocket
     * has died and input stream cannot be read anymore.
     */
    private void readCommand() throws Exception {
        byte h;
        while (in.available() != 0) {
            h = in.readByte();
            logger.finer(String.format("Read command: %d", h));
            switch (h) {
                case Commands.SET_KEY:
                    break;
                case Commands.DELETE_KEY:
                    break;
                case Commands.GET_TRACK:
                    break;
                case Commands.SET_ROW:
                    in.readInt();
                    break;
                case Commands.PAUSE:
                    in.readByte();
                    break;
                case Commands.SAVE_TRACKS:
                    break;

            }
        }

    }
}
