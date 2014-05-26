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
    private boolean playing;
    ArrayList<ControllerListener> listeners;

    public RocketController() {
        listeners = new ArrayList<ControllerListener>();
        currentRow = 0;
        playing = false;
    }

    public void addEventListener(ControllerListener listener) {
        listeners.add(listener);
    }

    public void removeEventListener(ControllerListener listener) {
        listeners.remove(listener);
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

    public boolean isPlaying() {
        return playing;
    }

    private void setPlayState(boolean status) {
        if (status != playing) {
            playing = status;
            for (ControllerListener listener: listeners)
                listener.controllerStatusChanged(status);
        }
    }

    public void pause() {
        setPlayState(false);
    }

    public void play() {
        setPlayState(true);
    }
}
