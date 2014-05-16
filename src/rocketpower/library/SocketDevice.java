package rocketpower.library;


import java.util.HashMap;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.util.logging.Logger;


interface RocketCommand {
    public void handle() throws Exception;
}


/*
 * Implements connection with GNU Rocket.
 */
class SocketDevice extends RocketDevice {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    private final String CLIENT_GREET = "hello, synctracker!";
    private final String SERVER_GREET = "hello, demo!";

    private HashMap<Integer, RocketCommand> commands;

    private static class Commands {
        static final int SET_KEY        = 0;
        static final int DELETE_KEY     = 1;
        static final int GET_TRACK      = 2;
        static final int SET_ROW        = 3;
        static final int PAUSE          = 4;
        static final int SAVE_TRACKS    = 5;
    }

    private class InCommandSetKey implements RocketCommand {
        public void handle() throws Exception {
            int trackId = in.readInt(); 
            int row = in.readInt();
            float value = in.readFloat();
            byte type = in.readByte();

            Track t = tracks.getById(trackId);
            if (t != null) 
                t.addOrUpdateKey(new TrackKey(row, value, (int) type));

            t.printKeys();

        }
    }

    private class InCommandDeleteKey implements RocketCommand {
        public void handle() throws Exception {
            int trackId = in.readInt();
            int row = in.readInt();

            Track t = tracks.getById(trackId);
            if (t != null)
                t.deleteKey(row);
        }
    }

    private class InCommandSetRow implements RocketCommand {
        public void handle() throws Exception {
            // TODO: notify controller about row change
            in.readInt();
        }
    }

    private class InCommandPause implements RocketCommand {
        public void handle() throws Exception {
            // TODO: notify controller about pause
            in.readByte();
        }
    }

    private class InCommandSaveTracks implements RocketCommand {
        public void handle() throws Exception {
        }
    }

    private class OutCommandGetTrack implements RocketCommand {
        public void handle() throws Exception {
            // Impl elsewhere for now
        }
    }

    /*
     * Connects to GNU Rocket.
     */
    public SocketDevice(Logger logger, TrackContainer tracks, String host, int port) throws Exception {
        super(logger, tracks);

        // Init command mappings
        commands = new HashMap<Integer, RocketCommand>();
        commands.put(new Integer(Commands.SET_KEY), new InCommandSetKey());
        commands.put(new Integer(Commands.DELETE_KEY), new InCommandDeleteKey());
        commands.put(new Integer(Commands.SET_ROW), new InCommandSetRow());
        commands.put(new Integer(Commands.PAUSE), new InCommandPause());
        commands.put(new Integer(Commands.SAVE_TRACKS), new InCommandSaveTracks());

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

    public void trackAdded(String name) {
        logger.finest("Adding track. Sending Commands.GET_TRACK to Rocket.");
        try {
            out.writeByte(Commands.GET_TRACK);
            out.writeInt(name.length());
            out.writeBytes(name);
        } catch (Exception e) {
            logger.severe("Communication with Rocket failed!");
        }
        logger.finer("New track fetched from Rocket.");
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
            cid = in.readByte();
            logger.finer(String.format("Read command: %d", cid));

            RocketCommand command = commands.get(new Integer(cid));

            if (command != null)
                command.handle();
            else
                logger.warning(String.format("Unknown command id=%d", cid));
        }
    }
}
