package rocketpower.library;


/*
 * Connection device baseclass
 */
abstract class RocketDevice {
    boolean debug;

    RocketDevice(boolean debug) {
        this.debug = debug;
    }

    abstract public void update();
}
