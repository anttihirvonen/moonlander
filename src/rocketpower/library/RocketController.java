package rocketpower.library;


/**
 * RocketController and it's subclasses
 * interface Rocket controllation with music
 * / time source.
 */ 
abstract class RocketController {
    /*
     * For reading / settings current row,
     * subclasses implement as necessary
     */
    abstract public int getCurrentRow();
    abstract public void setCurrentRow(int row);

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
