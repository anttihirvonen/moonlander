package moonlander.library;


import java.util.HashMap;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.logging.Logger;
import java.io.IOException;


interface RocketCommand {
    public void handle() throws Exception;
}


/*
 * Implements connection with GNU Rocket.
 */
class SocketConnector extends Connector {
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
    public SocketConnector(Logger logger, TrackContainer tracks, Controller controller, String host, int port) throws Exception {
        super(logger, tracks, controller);

        logger.fine(String.format("Trying to connect to Rocket at %s:%d", host, port));

        int triesAmount = 10; // Define max amount of connection tries
        for (int i = 0; i < triesAmount; i++) {
            try {
                initSocket(host, port);
                greetServer();
                break;
            } catch (Exception e) {
                if (i < triesAmount - 1 && socket != null) {
                    logger.warning("SocketConnector failed to connect to Rocket. Trying again.");
                    // Close socket if it has been opened already
                    socket.close();
                } else {
                    logger.warning("SocketConnector failed to connect to Rocket the last time.");
                    close();
                    throw e;
                }
            }
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
        out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

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
        out.flush();

        logger.finer("Greetings sent. Now reading greetings from Rocket...");

        // Expect server to return with correct greetings
        byte greet[] = new byte[SERVER_GREET.length()];
        try {
            in.readFully(greet, 0, SERVER_GREET.length());
        } catch (Exception e) {
            logger.severe("Couldn't read greetings from server. " + e);
            throw new Exception("Reading greetings failed", e);
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

    public void trackAdded(String name) {
        logger.finest("Adding track. Sending Commands.GET_TRACK to Rocket.");
        try {
            out.writeByte(Commands.GET_TRACK);
            out.writeInt(name.length());
            out.writeBytes(name);
            out.flush();
        } catch (Exception e) {
            logger.severe("Communication with Rocket failed!");
        }
        logger.finer("New track fetched from Rocket.");
    }

    public void controllerStatusChanged(boolean isPlaying) {
        // Rocket doesn't implement this yet!
        // Would be useful to control Rocket from demo's side
    }

    public void controllerRowChanged(int row) {
        logger.finest("Communicating row="+row+" change to rocket");

        try {
            out.writeByte(Commands.SET_ROW);
            out.writeInt(row);
            out.flush();
        } catch (Exception e) {
            logger.severe("Communication with Rocket failed!");
        }
    }

    /*
     * COMMAND HANDLING METHODS
     */
    public void handleCommandSetKey() throws IOException {
        logger.finest("Handling SET_KEY");

        int trackId = in.readInt(); 
        int row = in.readInt();
        float value = in.readFloat();
        byte type = in.readByte();

        Track t = tracks.getById(trackId);
        if (t != null) 
            t.addOrUpdateKey(new TrackKey(row, value, (int) type));
    }

    public void handleCommandDeleteKey() throws IOException {
        logger.finest("Handling DELETE_KEY");

        int trackId = in.readInt();
        int row = in.readInt();

        Track t = tracks.getById(trackId);
        if (t != null)
            t.deleteKey(row);
    }

    public void handleCommandSetRow() throws IOException {
        logger.finest("Handling SET_ROW");

        int row = in.readInt();
        // suppress events, otherwise a loop between demo
        // and Rocket emerges
        controller.setCurrentRow(row, true);
    }

    public void handleCommandPause() throws IOException {
        logger.finest("Handling PAUSE");

        byte flag = in.readByte();
        if (flag > 0)
            controller.pause();
        else
            controller.play();
    }

    public void handleCommandSaveTracks() throws IOException {
        logger.finest("Handling SAVE_TRACKS");

    }
    /**
     * Reads pending rocket commands from socket.
     *
     * Throws exception if command cannot be read
     * successfully; this usually means that Rocket
     * has died and input stream cannot be read anymore.
     */
    private void readCommand() throws Exception {
        byte cid;

        while (in.available() != 0) {
            logger.finest("Available bytes from Rocket: " + in.available());

            cid = in.readByte();

            switch (cid) {
                case Commands.SET_KEY:
                    handleCommandSetKey();
                    break;
                case Commands.DELETE_KEY:
                    handleCommandDeleteKey();
                    break;
                case Commands.SET_ROW:
                    handleCommandSetRow();
                    break;
                case Commands.PAUSE:
                    handleCommandPause();
                    break;
                case Commands.SAVE_TRACKS:
                    handleCommandSaveTracks();
                    break;
                default:
                    logger.warning(String.format("Unknown command id=%d", cid));
            }
        }
    }
}
