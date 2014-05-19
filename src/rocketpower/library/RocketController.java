package rocketpower.library;


import java.util.ArrayList;


interface ControllerListener {
    public void controllerStatusChanged(boolean isPlaying);
    public void controllerRowChanged(int row);
}


/**
 * RocketController and it's subclasses
 * interface Rocket controllation with music
 * / time source.
 */ 
abstract class RocketController {
    /*
     * Keeping track of fractional rows in baseclass
     * removes to need track time separately in subclasses
     */
    protected double currentRow;
    ArrayList<ControllerListener> listeners;

    public RocketController() {
        listeners = new ArrayList<ControllerListener>();
        currentRow = 0;
    }

    public void addEventListener(ControllerListener listener) {
        listeners.add(listener);
    }

    public double getCurrentRow() {
        return currentRow;
    }

    /**
     * @param row new row value
     * @param silent to suppress notifications
     */
    public void setCurrentRow(double row, boolean silent) {
        // If whole row changes, notify listeners
        if ((int)row != (int)currentRow && !silent) {
            for (ControllerListener listener: listeners)
                listener.controllerRowChanged((int)row);
        }

        currentRow = row;
    }

    abstract public void update();

    /**
     * Pauses the controller.
     */
    abstract public void pause();

    /**
     * Continues playing this controller.
     */
    abstract public void play();

    abstract public boolean isPlaying();
}
