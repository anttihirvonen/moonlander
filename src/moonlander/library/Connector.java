package moonlander.library;

import java.util.logging.Logger;


/*
 * Abstract baseclass for connection between demo and
 * Rocket / sync data files
 */
abstract class Connector implements TrackContainerListener, ControllerListener {
    protected TrackContainer tracks;
    protected Logger logger;
    protected Controller controller;

    Connector(Logger logger, TrackContainer tracks, Controller controller) {
        this.logger = logger;
        this.tracks = tracks;
        this.tracks.addEventListener(this);
        this.controller = controller;
        this.controller.addEventListener(this);
    }

    /**
     * If device has been initialized, it must be closed.
     *
     * Removes event listeners.
     */
    public void close() {
        logger.finer("Closing...");
        this.tracks.removeEventListener(this);
        this.controller.removeEventListener(this);
    }

    abstract public void update();
    public void trackAdded(String name) {}
    public void trackDeleted(String name) {}
    public void controllerRowChanged(int row) {}
    public void controllerStatusChanged(boolean isPlaying) {}
}
